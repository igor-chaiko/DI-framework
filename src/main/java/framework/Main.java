package framework;

import framework.container.IoCContainer;
import framework.test_beans.CarOwner;

public class Main {
    public static void main(String[] args) throws Exception {
        var container = new IoCContainer("src/main/resources/config.xml");

        var carOwner = container.getBean(CarOwner.class);
        carOwner.myCarInfo();
    }
}
