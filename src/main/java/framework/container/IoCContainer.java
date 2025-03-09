package framework.container;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import framework.bean_definition.BeanDefinition;
import framework.bean_definition.reader.XmlBeanDefinitionReaderImpl;

public class IoCContainer implements IIocContainer {
    private final XmlBeanDefinitionReaderImpl xmlBeanDefinitionReader;
    private final List<BeanDefinition> beanDefinitions;
    private final Map<String, Object> context = new HashMap<>();

    public IoCContainer(String pathToXmlCongih) {
        this.xmlBeanDefinitionReader = new XmlBeanDefinitionReaderImpl(pathToXmlCongih);
        this.beanDefinitions = xmlBeanDefinitionReader.getBeanDefinitions();
    }

    @Override
    public <T> T getBean(Class<T> requiredType) {
        return null;
    }

    @Override
    public Object getBean(String beanId) {
        return null;
    }

    private void initializeSingletons() {

    }
}
