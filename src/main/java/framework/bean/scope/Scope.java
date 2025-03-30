package framework.bean.scope;

import framework.bean.scope.utils.ObjectFactory;

public interface Scope {
    Object get(String beanName, ObjectFactory<?> objectFactory) throws Exception;
}
