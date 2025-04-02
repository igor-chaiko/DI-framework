package framework.test_beans.testing;

import framework.dependency_injection.annotaitions.Inject;

public class Z {
    @Inject
    private X x;

    public String xInfo() {
        return x.getInfo();
    }
}
