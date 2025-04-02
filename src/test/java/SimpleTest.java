import framework.container.IIocContainer;
import framework.container.IoCContainerImpl;
import framework.test_beans.demo.Car1;
import framework.test_beans.demo.Car2;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Тесты на создание бинов")
public class SimpleTest {
    private static IIocContainer container;
    private final SoftAssertions softly = new SoftAssertions();

    @BeforeAll
    static void setup() throws Exception {
        container = new IoCContainerImpl("src/main/resources/config.xml");
    }

    @Test
    void fieldInjected() throws Exception {
        // Проверка бинов автомобилей
        Car1 car1 = (Car1) container.getBean("car1");
        Car2 car2 = (Car2) container.getBean("car2");

        // Проверка car1
        softly.assertThat(car1.getMaxSpeed())
            .isEqualTo(320);
        softly.assertThat(car1.getColor())
            .isEqualTo("green");

        // Проверка car2
        softly.assertThat(car2.getMaxSpeed())
            .isEqualTo(270);
        softly.assertThat(car2.getColor())
            .isEqualTo("blue");

        softly.assertAll();
    }
}
