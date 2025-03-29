package framework.test_beans;

import framework.dependency_injection.Inject;
import framework.dependency_injection.Qualifier;

public class CarOwner {
//    @Inject
//    @Qualifier(name = "bmw")
    private final SimpleCar car1;
    private final String carOwnerName;
    private final int carOwnerAge;

    @Inject
    public CarOwner(String carOwnerName, @Qualifier(name = "mercedes") SimpleCar mercedes, int carOwnerAge) {
        this.car1 = mercedes;
        this.carOwnerName = carOwnerName;
        this.carOwnerAge = carOwnerAge;
    }

    public CarOwner() {
        this.car1 = null;
        this.carOwnerName = null;
        this.carOwnerAge = 0;
    }

    public void carOwnersInfo() {
        car1.carInfo();
        System.out.println("car owner name: " + carOwnerName);
        System.out.println("car owner age: " + carOwnerAge);
    }
}
