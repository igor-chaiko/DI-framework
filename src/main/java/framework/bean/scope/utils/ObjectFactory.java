package framework.bean.scope.utils;

@FunctionalInterface
public interface ObjectFactory<T> {
    T getObject() throws Exception;
}
