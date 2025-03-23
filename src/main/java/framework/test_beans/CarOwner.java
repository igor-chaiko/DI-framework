package framework.test_beans;

import framework.dependency_injection.Inject;
import framework.dependency_injection.Qualifier;

public class CarOwner {
    @Inject
    @Qualifier(name = "bmw")
    private SimpleCar myCar;

    public void myCarInfo() {
        myCar.carInfo();
    }
}
