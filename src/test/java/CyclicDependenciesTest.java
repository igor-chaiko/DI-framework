import framework.test_beans.testing.Z;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Тесты на циклические зависимости")
public class CyclicDependenciesTest extends AbstractFunctionalTest {

    @Test
    @SneakyThrows
    @DisplayName("Разрешение циклической зависимости")
    void cyclicDependency() {
        var z = container.getBean(Z.class);
        softly.assertThat(z.xInfo()).isEqualTo("some info");
    }
}
