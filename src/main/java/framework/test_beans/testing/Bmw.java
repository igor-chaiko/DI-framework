package framework.test_beans.testing;

import framework.dependency_injection.annotaitions.Inject;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Bmw implements SimpleCarr {
    private final int maxSpeed;
    private final String color;

    @Inject
    private CarOwner carOwner;

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
