package framework.container;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import framework.bean_definition.BeanDefinition;
import framework.bean_definition.cyclic_dependencies.DependencyGraph;
import framework.bean_definition.reader.XmlBeanDefinitionReaderImpl;
import framework.bean_definition.scope.Prototype;
import framework.bean_definition.scope.Scope;
import framework.bean_definition.scope.ScopeType;
import framework.bean_definition.scope.Singleton;
import framework.dependency_injection.Inject;
import framework.dependency_injection.Qualifier;

public class IoCContainer implements IIocContainer {

    public IoCContainer(String pathToXmlConfig) throws Exception {
        var xmlBeanDefinitionReader = new XmlBeanDefinitionReaderImpl(pathToXmlConfig);
        List<BeanDefinition> beanDefinitions = xmlBeanDefinitionReader.getBeanDefinitions();

        for (var beanDef : beanDefinitions) {
            if (beanDef.getScopeType() == ScopeType.SINGLETON) {
                initializeSingleton(beanDef);
            }
        }
    }

    @Override
    public <T> T getBean(Class<T> requiredType) throws Exception {
        var beanDefinitions = BeanDefinition.beanTypesToDefinitions.get(requiredType);

        if (beanDefinitions == null || beanDefinitions.size() != 1) {
            throw new IllegalStateException();
        }

        return (T) getBean(beanDefinitions.get(0).getId());
    }

    @Override
    public <T> List<T> getAllBeansByType(Class<T> requiredType) {
        var beanDefinitions = BeanDefinition.beanTypesToDefinitions.get(requiredType);

        if (beanDefinitions == null) {
            throw new IllegalStateException("No bean definitions found for type: " + requiredType);
        }

        return beanDefinitions.stream()
            .map(beanDef -> {
                try {
                    return (T) getBean(beanDef.getId());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            })
            .collect(Collectors.toList());
    }

    @Override
    public Object getBean(String beanId) throws Exception {
        var beanDefinition = BeanDefinition.beanIdToDefinition.get(beanId);

        if (beanDefinition == null) {
            throw new IllegalStateException("Bean with specified id is not exist");
        }

        return scopesMap.get(beanDefinition.getScopeType()).get(beanId, () -> createBeanByDefinition(beanDefinition));
    }

    private void initializeSingleton(BeanDefinition beanDefinition) throws Exception {
        var scope = scopesMap.get(ScopeType.SINGLETON);
        if (beanDefinition.getScopeType() != ScopeType.SINGLETON) {
            throw new IllegalStateException();
        }

        scope.get(beanDefinition.getId(), () -> createBeanByDefinition(beanDefinition));
    }

    private Object createBeanByDefinition(BeanDefinition beanDefinition) throws Exception {
        var object = beanDefinition.getInstanceMeta().createInstance();

        for (var property : beanDefinition.getProperties()) {
            property.injectProperty(object);
        }

        for (var field: object.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Inject.class)) {
                var fieldType = field.getType();
                var fieldName = field.getName();

                if (BeanDefinition.beanTypesToDefinitions.containsKey(fieldType)) {
                    var beanDefinitions = BeanDefinition.beanTypesToDefinitions.get(fieldType);
                    BeanDefinition fieldBeanDefinition =
                        getBeanDefinition(beanDefinition, beanDefinitions, fieldName, field);

                    var objectToSet = scopesMap.get(fieldBeanDefinition.getScopeType())
                        .get(fieldBeanDefinition.getId(), () -> createBeanByDefinition(fieldBeanDefinition));

                    field.setAccessible(true);
                    field.set(object, objectToSet);

                    continue;
                }

                if (BeanDefinition.beanInterfacesTypesToDefinitions.containsKey(fieldType)) {
                    var beanDefinitions = BeanDefinition.beanInterfacesTypesToDefinitions.get(fieldType);
                    BeanDefinition fieldBeanDefinition =
                        getBeanDefinition(beanDefinition, beanDefinitions, fieldName, field);

                    var objectToSet = scopesMap.get(fieldBeanDefinition.getScopeType())
                        .get(fieldBeanDefinition.getId(), () -> createBeanByDefinition(fieldBeanDefinition));

                    field.setAccessible(true);
                    field.set(object, objectToSet);

                    continue;
                }

                throw new IllegalStateException("No bean with required type exception");
            }
        }

        return object;
    }

    private BeanDefinition getBeanDefinition(
        BeanDefinition sourceBeanDefinition,
        List<BeanDefinition> beanDefinitions,
        String fieldName,
        Field field
    ) {
        BeanDefinition beanDef = null;

        if (beanDefinitions.size() > 1) {
            if (field.isAnnotationPresent(Qualifier.class)) {
                var qualifier = field.getAnnotation(Qualifier.class);
                fieldName = qualifier.name();
            }

            for (var beanDefinition : beanDefinitions) {
                if (beanDefinition.getId().equals(fieldName)) {
                    beanDef = beanDefinition;
                    break;
                }
            }

        } else {
            beanDef = beanDefinitions.getFirst();
        }

        if (beanDef == null) {
            throw new IllegalStateException("No satisfied bean exception");
        }

        dependencyGraph.addEdge(sourceBeanDefinition.getId(), beanDef.getId());

        return beanDef;
    }

    public static Map<ScopeType, Scope> scopesMap = Map.of(
        ScopeType.SINGLETON, new Singleton(),
        ScopeType.PROTOTYPE, new Prototype()
    );

    public static DependencyGraph dependencyGraph = new DependencyGraph();
}
