package framework.test_beans;

import framework.dependency_injection.Inject;

public class Y {
//    @Inject
    private Z z;

    public Y(Z z) {
        this.z = z;
    }
}
