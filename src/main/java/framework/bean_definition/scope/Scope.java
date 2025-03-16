package framework.bean_definition.scope;

import framework.bean_definition.scope.utils.ObjectFactory;

public interface Scope {
    // Тут происходит вся логика по контролю за объектами, ObjectFactory просто возвращает новый объект

    Object get(String beanName, ObjectFactory<?> objectFactory) throws Exception;
    ScopeType getType();
}
