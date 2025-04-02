package framework.test_beans.testing;

import framework.dependency_injection.annotaitions.Inject;
import lombok.Getter;
import lombok.Setter;

public class X {
    @Inject
    private Y y;

    @Setter
    @Getter
    private String info;
}
