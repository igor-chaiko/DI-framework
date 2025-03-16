package framework.bean_definition.reader;

import java.util.List;
import java.util.Map;

import framework.bean_definition.BeanDefinition;

public class XmlBeanDefinitionReaderImpl implements XmlBeanDefinitionReader {
    private final String pathToXmlCongih;

    public XmlBeanDefinitionReaderImpl(String pathToXmlConfig) {
        this.pathToXmlCongih = pathToXmlConfig;
    }

    @Override
    public List<BeanDefinition> getBeanDefinitions() throws ClassNotFoundException, NoSuchMethodException {
        return List.of(
            new BeanDefinition(
                "id1",
                "framework.test_beans.Car",
                "singleton",
                null,
                Map.of("maxSpeed", "300"),
                List.of("4", "BMW")
            ),
            new BeanDefinition(
                "id2",
                "framework.test_beans.Car",
                "prototype",
                null,
                Map.of("maxSpeed", "280"),
                List.of("4", "Mercedes")
            )
        );
    }
}
