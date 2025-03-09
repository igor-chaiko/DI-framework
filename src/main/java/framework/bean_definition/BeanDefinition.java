package framework.bean_definition;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import framework.bean_definition.utils.Instance;
import framework.bean_definition.utils.Property;
import framework.bean_definition.utils.Scope;

public class BeanDefinition {
    private final String id;
    private final Class<?> clazz;
    private final Scope scope;
    private final Method initMethod;
    private final List<Property> properties = new ArrayList<>();
    private final Instance instaceInfo;

    public BeanDefinition(
        String id,
        String className,
        String scope,
        String initMethodName,
        Map<String, Object> properties,
        List<Object> constructorArgs
    ) throws ClassNotFoundException, NoSuchMethodException {
        this.id = id;
        this.clazz = Class.forName(className);
        this.scope = (scope == null) ? Scope.SINGLETON : Scope.valueOf(scope);
        this.initMethod = clazz.getMethod(initMethodName);

        properties.forEach((name, value) -> {
            try {
                String setter = "set" + capitalize(name);
                this.properties.add(new Property(value, clazz.getMethod(setter, value.getClass())));
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });

        this.instaceInfo = new Instance(clazz, constructorArgs == null ? List.of() : constructorArgs);
    }

    private String capitalize(String name) {
        if (name == null || name.isEmpty()) {
            return name;
        }
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }
}
