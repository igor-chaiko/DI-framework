package framework.test_beans;

import framework.dependency_injection.Inject;
import lombok.Getter;
import lombok.Setter;

public class X {
//    @Inject
    private Y y;

    public X(Y y) {
        this.y = y;
    }

    @Setter
    @Getter
    private String info;
}
