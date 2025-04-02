package framework;

import framework.container.IIocContainer;
import framework.container.IoCContainerImpl;

public class Main {
    public static void main(String[] args) throws Exception {
        IIocContainer container = new IoCContainerImpl("src/main/resources/config.xml");
    }
}
