package framework.container;

import java.util.List;

public interface IIocContainer {
    <T> T getBean(Class<T> requiredType) throws Exception;
    <T> List<T> getAllBeansByType(Class<T> requiredType) throws Exception;
    Object getBean(String name) throws Exception;
}
