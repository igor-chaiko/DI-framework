import framework.test_beans.demo.CarOwner;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Тесты на механизм DI")
public class InjectTest extends AbstractFunctionalTest {

    @Test
    @SneakyThrows
    @DisplayName("Тест на разные способы инджекта одного бина в другой")
    void dITest() {
        var car1 = container.getBean("car1");
        var carOwner = container.getBean(CarOwner.class);

        softly.assertThat(carOwner.getName()).isEqualTo("ivan");
        softly.assertThat(car1).isSameAs(carOwner.getSimpleCar());
    }
}
