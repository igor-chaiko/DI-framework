package framework.container;

public interface IIocContainer {
    <T> T getBean(Class<T> requiredType);
    Object getBean(String name);
}
