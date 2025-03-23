package framework.test_beans;

import framework.dependency_injection.Inject;
import lombok.Getter;
import lombok.Setter;

public class X {
    @Inject
    private Y y;

    @Setter
    @Getter
    private String info;
}
