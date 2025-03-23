package framework;

import framework.container.IoCContainer;
import framework.test_beans.CarOwner;
import framework.test_beans.X;

public class Main {
    public static void main(String[] args) throws Exception {
        var container = new IoCContainer("src/main/resources/config.xml");

        var carOwner = container.getBean(CarOwner.class);
        carOwner.myCarInfo();

        var x = container.getBean(X.class);
        System.out.println("\n" + x.getInfo());
    }
}
