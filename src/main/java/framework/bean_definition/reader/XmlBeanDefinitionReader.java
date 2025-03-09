package framework.bean_definition.reader;

import java.util.List;

import framework.bean_definition.BeanDefinition;

public interface XmlBeanDefinitionReader {
    List<BeanDefinition> getBeanDefinitions();
}
