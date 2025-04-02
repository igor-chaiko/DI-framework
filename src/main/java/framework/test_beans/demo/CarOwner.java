package framework.test_beans.demo;

import framework.dependency_injection.annotaitions.Inject;
import framework.dependency_injection.annotaitions.Qualifier;
import lombok.Getter;

@Getter
public class CarOwner {
    private final String name;

    @Inject
    @Qualifier(name = "car1")
    private SimpleCar simpleCar;

    public CarOwner(String name) {
        this.name = name;
    }

    public void ownersCarInfo() {
        System.out.println("info about the owner's car");
        simpleCar.carInfo();
    }
}
