package framework.bean.bean_definition;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import framework.bean.scope.ScopeType;
import framework.bean.bean_definition.meta.InstanceMeta;
import framework.bean.bean_definition.meta.Property;
import lombok.Getter;

@Getter
public class BeanDefinition {
    private final String id; // optional
    private final Class<?> clazz; // required
    private final ScopeType scopeType; // optional
    private final Method initMethod; // optional
    private final List<Property> properties = new ArrayList<>();
    private final InstanceMeta instanceMeta; // required
    private final Boolean lazyInit;

    public BeanDefinition(
        String id,
        String className,
        String scopeType,
        String initMethodName,
        Map<String, String> properties,
        List<String> constructorArgs,
        Boolean lazyInit
    ) throws Exception {
        this.id = id != null ? id : UUID.randomUUID().toString();
        this.clazz = Class.forName(className);
        this.scopeType = (scopeType == null) ? ScopeType.SINGLETON : getScopeType(scopeType);
        this.initMethod = initMethodName != null ? clazz.getMethod(initMethodName) : null;
        this.lazyInit = lazyInit;

        properties.forEach((name, value) -> {
            String setter = "set" + capitalize(name);
            this.properties.add(new Property(clazz, setter, value));
        });

        this.instanceMeta = new InstanceMeta(this.clazz, constructorArgs == null ? List.of() : constructorArgs, this);

        if (beanIdToDefinition.containsKey(this.id)) {
            throw new IllegalArgumentException("Bean Id must be unique");
        }

        beanIdToDefinition.put(this.id, this);
        beanClassesToDefinitions
            .computeIfAbsent(this.clazz, key -> new ArrayList<>())
            .add(this);
        for (Class<?> interfaceClass : this.clazz.getInterfaces()) {
            beanInterfacesToDefinitions
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
            case "thread-local" -> {
                return ScopeType.THREAD_LOCAL;
            }
        }

        throw new IllegalStateException("Unsupported scope type");
    }

    public static Map<String, BeanDefinition> beanIdToDefinition = new HashMap<>();
    public static Map<Class<?>, List<BeanDefinition>> beanClassesToDefinitions = new HashMap<>();
    public static Map<Class<?>, List<BeanDefinition>> beanInterfacesToDefinitions = new HashMap<>();
}
