package framework.test_beans.demo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Car1 implements SimpleCar {
    private Integer maxSpeed;
    private String color;

    @Override
    public void carInfo() {
        System.out.println(
            "max speed: " + maxSpeed + "\n"
            + "color: " + color
        );
    }
}
