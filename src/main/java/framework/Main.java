package framework;

import framework.container.IoCContainer;
import framework.test_beans.Bmw;
import framework.test_beans.CarOwner;
import framework.test_beans.X;
import framework.test_beans.Y;
import framework.test_beans.Z;

public class Main {
    public static void main(String[] args) throws Exception {
        var container = new IoCContainer("src/main/resources/config.xml");

        var carOwner = container.getBean(CarOwner.class);
        carOwner.carOwnersInfo();
        var car1 = (Bmw) carOwner.getCar1();
        System.out.println(car1.getNumOfOwners());
        var x = container.getBean(X.class);
        var y = container.getBean(Y.class);
        var z = container.getBean(Z.class);
        z.xInfo();
    }
}
