package framework.bean_definition.utils;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class InstanceMeta {
    private final Constructor<?> constructor;
    private final List<Object> constructorArgs;

    public InstanceMeta(Class<?> beanClass, List<String> constructorArgs) throws NoSuchMethodException {
        this.constructor = findMatchingConstructor(beanClass, constructorArgs);
        this.constructorArgs = convertConstructorArgs(constructor.getParameterTypes(), constructorArgs);
    }

    public Object createInstance() throws Exception {
        return constructor.newInstance(constructorArgs.toArray());
    }

    private Constructor<?> findMatchingConstructor(Class<?> beanClass, List<String> args) throws NoSuchMethodException {
        for (Constructor<?> candidate : beanClass.getConstructors()) {
            if (candidate.getParameterCount() == args.size()) {
                return candidate;
            }
        }

        throw new NoSuchMethodException("No constructor with " + args.size() + " arguments found in: " + beanClass);
    }

    private List<Object> convertConstructorArgs(Class<?>[] targetTypes, List<String> stringArgs) {
        List<Object> result = new ArrayList<>();
        for (int i = 0; i < targetTypes.length; i++) {
            result.add(convertStringToType(stringArgs.get(i), targetTypes[i]));
        }
        return result;
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
