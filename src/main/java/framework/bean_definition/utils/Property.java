package framework.bean_definition.utils;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.lang.reflect.Method;

public class Property {
    private final Object valueToSet;
    private final Method setterMethod;

    public Property(Class<?> clazz, String setterName, String valueToSet) {
        this.setterMethod = findSetterMethod(clazz, setterName);
        Class<?> paramType = setterMethod.getParameterTypes()[0];

        this.valueToSet = convertToRequiredType(valueToSet, paramType);
    }

    public void injectProperty(Object beanInstance) throws Exception {
        setterMethod.invoke(beanInstance, valueToSet);
    }

    private Method findSetterMethod(Class<?> clazz, String setterName) {
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (method.getName().equals(setterName) && method.getParameterCount() == 1) {
                return method;
            }
        }

        throw new IllegalStateException("Setter with name: " + setterName + " is not found");
    }

    private Object convertToRequiredType(String value, Class<?> requiredType) {
        PropertyEditor editor = PropertyEditorManager.findEditor(requiredType);
        if (editor == null) {
            throw new IllegalArgumentException("Cannot convert to " + requiredType);
        }
        editor.setAsText(value);
        return editor.getValue();
    }
}
