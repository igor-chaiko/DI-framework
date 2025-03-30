package framework.bean.bean_definition.reader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;

import framework.bean.bean_definition.BeanDefinition;

public class XmlBeanDefinitionReaderImpl implements XmlBeanDefinitionReader {
    private final String pathToXmlCongih;

    public XmlBeanDefinitionReaderImpl(String pathToXmlConfig) {
        this.pathToXmlCongih = pathToXmlConfig;
    }

    private List<BeanDefinition> xmlConfigReader() throws Exception {
        List<BeanDefinition> beans = new ArrayList<>();
        File xmlFile = new File(this.pathToXmlCongih);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(xmlFile);
        document.getDocumentElement().normalize();

        NodeList beanList = document.getElementsByTagName("bean");

        for (int i = 0; i < beanList.getLength(); i++) {
            Element bean = (Element) beanList.item(i);
            String id = getAttrOrNull(bean, "id");
            String className = getAttrOrNull(bean, "class");
            String scope = getAttrOrDefault(bean, "scope", "singleton");
            String initMethod = getAttrOrNull(bean, "init-method");
            boolean lazyInit = Boolean.parseBoolean(getAttrOrDefault(bean, "lazy-init", "false"));

            Map<String, String> properties = new HashMap<>();
            NodeList propertiesList = bean.getElementsByTagName("property");
            for (int j = 0; j < propertiesList.getLength(); j++) {
                Element property = (Element) propertiesList.item(j);
                String propName = getAttrOrNull(property, "name");
                String propValue = getAttrOrNull(property, "value");
                if (propName != null && propValue != null) {
                    properties.put(propName, propValue);
                }
            }

            List<String> constructorArgs = new ArrayList<>();
            NodeList constructorList = bean.getElementsByTagName("constructor");
            for (int j = 0; j < constructorList.getLength(); j++) {
                Element constructor = (Element) constructorList.item(j);
                NodeList args = constructor.getElementsByTagName("arg");
                for (int k = 0; k < args.getLength(); k++) {
                    Element arg = (Element) args.item(k);
                    String value = getAttrOrNull(arg, "value");
                    if (value != null) {
                        constructorArgs.add(value);
                    }
                }
            }

            BeanDefinition beanDef =
                new BeanDefinition(id, className, scope, initMethod, properties, constructorArgs, lazyInit);
            beans.add(beanDef);
        }

        return beans;
    }

    private static String getAttrOrNull(Element element, String attr) {
        return element.hasAttribute(attr) ? element.getAttribute(attr) : null;
    }

    private static String getAttrOrDefault(Element element, String attr, String defaultValue) {
        return element.hasAttribute(attr) ? element.getAttribute(attr) : defaultValue;
    }


    @Override
    public List<BeanDefinition> getBeanDefinitions() throws ClassNotFoundException, NoSuchMethodException {
        try {
            return xmlConfigReader();
        } catch (Exception e) {
            throw new RuntimeException("Error while reading xml config", e);
        }
    }
}
