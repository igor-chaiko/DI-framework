package framework;

import framework.container.IIocContainer;
import framework.container.IoCContainerImpl;
import framework.test_beans.demo.SimpleCar;
import framework.test_beans.demo.CarOwner;

public class Main {
    public static void main(String[] args) throws Exception {
        IIocContainer container = new IoCContainerImpl("src/main/resources/config.xml");

        var car1 = (SimpleCar) container.getBean("car1");
        car1.carInfo();

        var carOnwer = container.getBean(CarOwner.class);
        carOnwer.ownersCarInfo();
    }
}
