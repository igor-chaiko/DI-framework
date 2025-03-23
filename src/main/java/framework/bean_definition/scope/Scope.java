package framework.bean_definition.scope;

import framework.bean_definition.scope.utils.ObjectFactory;

public interface Scope {
    Object get(String beanName, ObjectFactory<?> objectFactory) throws Exception;
    ScopeType getType();
}
