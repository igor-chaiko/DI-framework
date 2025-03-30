package framework.test_beans;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Bmw implements SimpleCar {
    private final int maxSpeed;
    private final String color;

    @Setter
    private Boolean coupe;
    @Setter
    private Integer numOfOwners;

    public Bmw(int maxSpeed, String color) {
//        System.out.println("\n---------------------------------------------------------\n");
        this.maxSpeed = maxSpeed;
        this.color = color;
    }

    @Override
    public void carInfo() {
        String info = "Bmw is "
            + (coupe ? "coupe" : "sedan")
            + " of "
            + color
            + " color\n"
            + "developing maximum speed of: "
            + maxSpeed
            + " with " + numOfOwners + " owners";
        System.out.println(info);
    }
}
