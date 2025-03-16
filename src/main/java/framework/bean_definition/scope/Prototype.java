package framework.bean_definition.scope;

import framework.bean_definition.scope.utils.ObjectFactory;

public class Prototype implements Scope {
    @Override
    public Object get(String beanName, ObjectFactory<?> objectFactory) throws Exception {
        return objectFactory.getObject();
    }

    @Override
    public ScopeType getType() {
        return ScopeType.PROTOTYPE;
    }
}
