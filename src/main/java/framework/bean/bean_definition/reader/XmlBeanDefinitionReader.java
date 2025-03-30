package framework.bean.bean_definition.reader;

import java.util.List;

import framework.bean.bean_definition.BeanDefinition;

public interface XmlBeanDefinitionReader {
    List<BeanDefinition> getBeanDefinitions() throws ClassNotFoundException, NoSuchMethodException;
}
