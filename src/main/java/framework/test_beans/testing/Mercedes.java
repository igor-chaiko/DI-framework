package framework.test_beans.testing;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Mercedes implements SimpleCarr {
    private final int maxSpeed;
    private final String color;

    @Setter
    private boolean coupe;

    public Mercedes(int maxSpeed, String color) {
        this.maxSpeed = maxSpeed;
        this.color = color;
    }

    @Override
    public void carInfo() {
        String info = "Mercedes is "
            + (coupe ? "coupe" : "sedan")
            + " of "
            + color
            + " color\n"
            + "developing maximum speed of: "
            + maxSpeed;
        System.out.println(info);
    }
}
