package framework.test_beans.testing;

import framework.dependency_injection.annotaitions.Qualifier;
import lombok.Getter;

public class CarOwner {
    @Getter
//    @Inject
//    @Qualifier(name = "bmw")
    private SimpleCar car1;
    private String carOwnerName;
    private int carOwnerAge;

//    @Inject
    public CarOwner(String carOwnerName, int carOwnerAge, @Qualifier(name = "bmw") SimpleCar car1) { // @Qualifier(name = "mercedes") SimpleCar mercedes
        this.carOwnerName = carOwnerName;
        this.carOwnerAge = carOwnerAge;
        this.car1 = car1;
    }

//    public CarOwner() {
//        this.car1 = null;
//        this.carOwnerName = null;
//        this.carOwnerAge = 0;
//    }

    public void carOwnersInfo() {
        car1.carInfo();
        System.out.println("car owner name: " + carOwnerName);
        System.out.println("car owner age: " + carOwnerAge);
    }
}
