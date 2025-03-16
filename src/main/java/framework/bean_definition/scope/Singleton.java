package framework.bean_definition.scope;

import java.util.HashMap;
import java.util.Map;

import framework.bean_definition.scope.utils.ObjectFactory;

public class Singleton implements Scope {
    private final Map<String, Object> beans = new HashMap<>();

    @Override
    public Object get(String beanName, ObjectFactory<?> objectFactory) {
        return beans.computeIfAbsent(beanName, name -> {
            try {
                return objectFactory.getObject();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public ScopeType getType() {
        return ScopeType.SINGLETON;
    }
}
