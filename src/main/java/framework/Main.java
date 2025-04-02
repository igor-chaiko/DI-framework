package framework;

import framework.container.IIocContainer;
import framework.container.IoCContainerImpl;
import framework.test_beans.testing.Z;


public class Main {
    public static void main(String[] args) throws Exception {
        IIocContainer container = new IoCContainerImpl("src/main/resources/config.xml");

        var z = container.getBean(Z.class);
        z.xInfo();
    }
}
