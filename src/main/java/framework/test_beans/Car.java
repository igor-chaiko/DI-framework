package framework.test_beans;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Car {
    private final int numOfWheels;
    private final String carBrand;

    @Setter
    private int maxSpeed;

    public Car(int numOfWheels, String carBrand) {
        this.numOfWheels = numOfWheels;
        this.carBrand = carBrand;
    }

    public void drive() {
        System.out.println("I am driving with max speed: " + this.maxSpeed);
    }
}
