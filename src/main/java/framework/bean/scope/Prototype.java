package framework.bean.scope;

import framework.bean.scope.utils.ObjectFactory;

public class Prototype implements Scope {
    @Override
    public Object get(String beanName, ObjectFactory<?> objectFactory) throws Exception {
        return objectFactory.getObject();
    }
}
