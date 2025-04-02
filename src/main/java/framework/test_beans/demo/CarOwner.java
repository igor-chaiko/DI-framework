package framework.test_beans.demo;

import framework.dependency_injection.annotaitions.Inject;
import framework.dependency_injection.annotaitions.Qualifier;

public class CarOwner {
    @Inject
    @Qualifier(name = "car1")
    private SimpleCar simpleCar;

    public void ownersCarInfo() {
        System.out.println("info about the owner's car");
        simpleCar.carInfo();
    }
}
