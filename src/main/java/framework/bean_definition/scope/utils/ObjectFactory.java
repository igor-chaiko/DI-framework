package framework.bean_definition.scope.utils;

@FunctionalInterface
public interface ObjectFactory<T> {
    T getObject() throws Exception;
}
