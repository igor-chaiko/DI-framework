package framework.bean_definition.utils;

import java.lang.reflect.Method;

public class Property {
    private final Object value;
    private final Method setterMethod;

    public Property(Object value, Method setterMethod) {
        this.value = value;
        this.setterMethod = setterMethod;
    }

    public void injectProperty(Object beanInstance) throws Exception {
        setterMethod.invoke(beanInstance, value);
    }
}
