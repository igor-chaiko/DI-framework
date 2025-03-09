package framework.bean_definition.reader;

import java.util.List;

import framework.bean_definition.BeanDefinition;

public class XmlBeanDefinitionReaderImpl implements XmlBeanDefinitionReader {
    private final String pathToXmlCongih;

    public XmlBeanDefinitionReaderImpl(String pathToXmlConfig) {
        this.pathToXmlCongih = pathToXmlConfig;
    }

    @Override
    public List<BeanDefinition> getBeanDefinitions() {
        return List.of();
    }
}
