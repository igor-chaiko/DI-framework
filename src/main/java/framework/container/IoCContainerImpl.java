package framework.container;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import framework.bean.bean_definition.BeanDefinition;
import framework.bean.bean_definition.reader.XmlBeanDefinitionReaderImpl;
import framework.bean.cyclic_dependencies.DependencyGraph;
import framework.bean.scope.Prototype;
import framework.bean.scope.Scope;
import framework.bean.scope.ScopeType;
import framework.bean.scope.Singleton;
import framework.bean.scope.ThreadLocal;
import framework.dependency_injection.annotaitions.Inject;
import framework.dependency_injection.annotaitions.Qualifier;

import static framework.bean.lazy_init.LazyInitCallback.createLazyProxy;

@SuppressWarnings("unchecked")
public class IoCContainerImpl implements IIocContainer {

    public IoCContainerImpl(String pathToXmlConfig) throws Exception {
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
        var beanDefinitions = BeanDefinition.beanClassesToDefinitions.get(requiredType);

        if (beanDefinitions == null || beanDefinitions.size() != 1) {
            throw new IllegalStateException();
        }

        return (T) getBean(beanDefinitions.getFirst().getId());
    }

    @Override
    public <T> List<T> getAllBeansByType(Class<T> requiredType) {
        var beanDefinitions = BeanDefinition.beanClassesToDefinitions.get(requiredType);

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
            throw new IllegalStateException("Bean with specified id is not found");
        }

        return scopesMap.get(beanDefinition.getScopeType()).get(beanId, () -> createObjectByDefinition(beanDefinition));
    }

    @Override
    public int getObjectsUnderContainerControlCount() {
        int count = 0;
        for (var entry: scopesMap.entrySet()) {
            count += entry.getValue().getBeansCount();
        }

        return count;
    }

    private void initializeSingleton(BeanDefinition beanDefinition) throws Exception {
        var scope = scopesMap.get(ScopeType.SINGLETON);
        if (beanDefinition.getScopeType() != ScopeType.SINGLETON) {
            throw new IllegalStateException();
        }

        scope.get(beanDefinition.getId(), () -> createObjectByDefinition(beanDefinition));
    }

    public static Object createObjectByDefinition(BeanDefinition beanDefinition) throws Exception {
        if (beanDefinition.getLazyInit()) {
            return createLazyProxy(beanDefinition.getInstanceMeta().getBeanClass(), beanDefinition);
        }
        return instantiate(beanDefinition);
    }

    public static Object instantiate(BeanDefinition beanDefinition) throws Exception {
        var object = beanDefinition.getInstanceMeta().createInstance();

        for (var property : beanDefinition.getProperties()) {
            property.injectProperty(object);
        }

        for (var field : object.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Inject.class)) {
                injectField(object, field, beanDefinition);
            }
        }

        return object;
    }

    private static void injectField(Object objectToSetField, Field field, BeanDefinition beanDefinition)
        throws Exception {
        var fieldType = field.getType();
        var fieldName = field.getName();

        if (BeanDefinition.beanClassesToDefinitions.containsKey(fieldType)) {
            var beanDefinitions = BeanDefinition.beanClassesToDefinitions.get(fieldType);
            BeanDefinition fieldBeanDefinition =
                getBeanDefinition(beanDefinition, beanDefinitions, fieldName, field);

            var objectToInject = scopesMap.get(fieldBeanDefinition.getScopeType())
                .get(fieldBeanDefinition.getId(), () -> createObjectByDefinition(fieldBeanDefinition));

            field.setAccessible(true);
            field.set(objectToSetField, objectToInject);

            return;
        }

        if (BeanDefinition.beanInterfacesToDefinitions.containsKey(fieldType)) {
            var beanDefinitions = BeanDefinition.beanInterfacesToDefinitions.get(fieldType);
            BeanDefinition fieldBeanDefinition =
                getBeanDefinition(beanDefinition, beanDefinitions, fieldName, field);

            var objectToInject = scopesMap.get(fieldBeanDefinition.getScopeType())
                .get(fieldBeanDefinition.getId(), () -> createObjectByDefinition(fieldBeanDefinition));

            field.setAccessible(true);
            field.set(objectToSetField, objectToInject);

            return;
        }

        throw new IllegalStateException("No bean with required type exception");
    }

    private static BeanDefinition getBeanDefinition(
        BeanDefinition sourceBeanDefinition,
        List<BeanDefinition> beanDefinitions,
        String fieldName, // если не указан Qualifier, используем его
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
            throw new IllegalStateException("No satisfied bean to inject exception");
        }

        dependencyGraph.addEdge(sourceBeanDefinition, beanDef);
        return beanDef;
    }

    public static Map<ScopeType, Scope> scopesMap = Map.of(
        ScopeType.SINGLETON, new Singleton(),
        ScopeType.PROTOTYPE, new Prototype(),
        ScopeType.THREAD_LOCAL, new ThreadLocal()
    );

    public static DependencyGraph dependencyGraph = new DependencyGraph();
}
