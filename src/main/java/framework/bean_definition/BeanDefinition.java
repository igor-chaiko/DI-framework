package framework.bean_definition;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import framework.bean_definition.scope.ScopeType;
import framework.bean_definition.utils.InstanceMeta;
import framework.bean_definition.utils.Property;
import lombok.Getter;

@Getter
public class BeanDefinition {
    private final String id; // optional
    private final Class<?> clazz; // required
    private final ScopeType scopeType; // optional
    private final Method initMethod; // optional
    private final List<Property> properties = new ArrayList<>();
    private final InstanceMeta instanceMeta; // required

    public BeanDefinition(
        String id,
        String className,
        String scopeType,
        String initMethodName,
        Map<String, String > properties,
        List<String> constructorArgs
    ) throws ClassNotFoundException, NoSuchMethodException {
        this.id = id != null ? id : UUID.randomUUID().toString();
        this.clazz = Class.forName(className);
        this.scopeType = (scopeType == null) ? ScopeType.SINGLETON : getScopeType(scopeType);
        this.initMethod = initMethodName != null ? clazz.getMethod(initMethodName) : null;

        properties.forEach((name, value) -> {
            String setter = "set" + capitalize(name);
            this.properties.add(new Property(clazz, setter, value));
        });

        this.instanceMeta = new InstanceMeta(this.clazz, constructorArgs == null ? List.of() : constructorArgs);

        if (beanIdToDefinition.containsKey(this.id)) {
            throw new IllegalArgumentException("Bean Id must be unique");
        }

        beanIdToDefinition.put(this.id, this);
        beanTypesToDefinitions
            .computeIfAbsent(this.clazz, key -> new ArrayList<>())
            .add(this);
        for (Class<?> interfaceClass : this.clazz.getInterfaces()) {
            beanInterfacesTypesToDefinitions
                .computeIfAbsent(interfaceClass, key -> new ArrayList<>())
                .add(this);
        }
    }

    private String capitalize(String name) {
        if (name == null || name.isEmpty()) {
            return name;
        }
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    private ScopeType getScopeType(String scopeType) {
        switch (scopeType) {
            case "singleton" -> {
                return ScopeType.SINGLETON;
            }
            case "prototype" -> {
                return ScopeType.PROTOTYPE;
            }
        }

        throw new IllegalStateException("Unsupported scope type");
    }

    public static Map<String, BeanDefinition> beanIdToDefinition = new HashMap<>();
    public static Map<Class<?>, List<BeanDefinition>> beanTypesToDefinitions = new HashMap<>();
    public static Map<Class<?>, List<BeanDefinition>> beanInterfacesTypesToDefinitions = new HashMap<>();
}
