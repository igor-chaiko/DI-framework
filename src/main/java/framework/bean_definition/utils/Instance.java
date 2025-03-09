package framework.bean_definition.utils;

import java.lang.reflect.Constructor;
import java.util.List;

public class Instance {
    private final Class<?> beanClass;
    private final Constructor<?> constructor;
    private final List<Object> constructorArgs;

    public Instance(Class<?> beanClass, List<Object> constructorArgs) throws NoSuchMethodException {
        this.beanClass = beanClass;
        this.constructor = beanClass.getConstructor();
        this.constructorArgs = constructorArgs;
    }

    public Object createInstance() throws Exception {
        return constructor.newInstance(constructorArgs.toArray());
    }
}
