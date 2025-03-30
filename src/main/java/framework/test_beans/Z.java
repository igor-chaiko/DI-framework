package framework.test_beans;

import framework.dependency_injection.annotaitions.Inject;

public class Z {
    @Inject
    private X x;

    public void xInfo() {
        System.out.println("\n" + x.getInfo());
    }

//    public Z(X x) {
//        this.x = x;
//    }
}
