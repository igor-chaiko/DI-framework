package framework.bean.bean_definition.meta;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import framework.bean.bean_definition.BeanDefinition;
import framework.dependency_injection.annotaitions.Inject;
import framework.dependency_injection.annotaitions.Qualifier;
import lombok.Getter;

import static framework.bean.bean_definition.BeanDefinition.beanClassesToDefinitions;
import static framework.bean.bean_definition.BeanDefinition.beanInterfacesToDefinitions;
import static framework.container.IoCContainerImpl.createObjectByDefinition;
import static framework.container.IoCContainerImpl.dependencyGraph;
import static framework.container.IoCContainerImpl.scopesMap;

public class InstanceMeta {
    @Getter
    private final Class<?> beanClass;
    private final List<String> constructorConfigArgs;
    private final BeanDefinition beanDefinition;
    @Getter
    private final List<Object> argsToInvokeConstructorWith = new ArrayList<>();
    private Constructor<?> constructor = null;

    public InstanceMeta(Class<?> beanClass, List<String> constructorConfigArgs, BeanDefinition beanDefinition) {
        this.beanClass = beanClass;
        this.constructorConfigArgs = constructorConfigArgs;
        this.beanDefinition = beanDefinition;
    }

    public Object createInstance() throws Exception {
        if (constructor == null) {
            this.constructor = findMatchingConstructor();
        }
        return constructor.newInstance(argsToInvokeConstructorWith.toArray());
    }

    public Constructor<?> getConstructor() throws Exception {
        if (this.constructor == null) {
            this.constructor = findMatchingConstructor();
        }
        return constructor;
    }

    private Constructor<?> findMatchingConstructor() throws Exception {
        var constructor = findSuitableConstructor(beanClass.getConstructors());
        var constructorParams = constructor.getParameters();
        var configArgsPointer = 0;

        for (var param: constructorParams) {
            if (beanClassesToDefinitions.containsKey(param.getType())) {
                addBeanToConstructorArgs(beanClassesToDefinitions.get(param.getType()), param);
            } else if (beanInterfacesToDefinitions.containsKey(param.getType())) {
                addBeanToConstructorArgs(beanInterfacesToDefinitions.get(param.getType()), param);
            } else {
                this.argsToInvokeConstructorWith.add(convertStringToType(
                    constructorConfigArgs.get(configArgsPointer++),
                    param.getType()
                ));
            }
        }

        return constructor;
    }

    private Constructor<?> findSuitableConstructor(Constructor<?>[] constructors) {
        var constructor = constructors[0];
        var constructorFound = false;

        if (constructors.length > 1) {
            for (var candidate: constructors) {
                if (candidate.isAnnotationPresent(Inject.class)) {
                    if (constructorFound) {
                        throw new IllegalStateException("No unique constructor exception");
                    }
                    constructor = candidate;
                    constructorFound = true;
                }
            }
        }

        return constructor;
    }

    private void addBeanToConstructorArgs(
        List<BeanDefinition> targetBeanDefinitions,
        Parameter parameter
    ) throws Exception {
        String fieldName;
        BeanDefinition beanDef = null;

        if (parameter.isAnnotationPresent(Qualifier.class)) {
            var qualifier = parameter.getAnnotation(Qualifier.class);
            fieldName = qualifier.name();

            for (var beanDefinition : targetBeanDefinitions) {
                if (beanDefinition.getId().equals(fieldName)) {
                    beanDef = beanDefinition;
                    break;
                }
            }
        } else {
            if (targetBeanDefinitions.size() == 1) {
                beanDef = targetBeanDefinitions.getFirst();
            }
        }

        if (beanDef == null) {
            throw new IllegalStateException("No satisfied bean to inject throw constructor exception");
        }

        dependencyGraph.addEdge(beanDefinition, beanDef);
        BeanDefinition finalBeanDef = beanDef;
        this.argsToInvokeConstructorWith.add(
            scopesMap.get(beanDef.getScopeType()).get(beanDef.getId(), () -> createObjectByDefinition(finalBeanDef))
        );
    }

    private Object convertStringToType(String arg, Class<?> targetType) {
        if (targetType == String.class) return arg;
        if (targetType == int.class || targetType == Integer.class) return Integer.parseInt(arg);
        if (targetType == long.class || targetType == Long.class) return Long.parseLong(arg);
        if (targetType == boolean.class || targetType == Boolean.class) return Boolean.parseBoolean(arg);
        if (targetType == double.class || targetType == Double.class) return Double.parseDouble(arg);
        if (targetType == float.class || targetType == Float.class) return Float.parseFloat(arg);
        if (targetType == short.class || targetType == Short.class) return Short.parseShort(arg);
        if (targetType == byte.class || targetType == Byte.class) return Byte.parseByte(arg);
        if (targetType == char.class || targetType == Character.class) {
            if (arg.length() != 1) throw new IllegalArgumentException("Expected single char, but got: " + arg);
            return arg.charAt(0);
        }

        throw new IllegalArgumentException("Unsupported target type: " + targetType.getName());
    }
}
