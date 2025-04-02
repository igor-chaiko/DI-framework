import framework.test_beans.demo.Car1;
import framework.test_beans.demo.Car2;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Тесты на создание бинов c конкретными скоупами")
public class ScopesTest extends AbstractFunctionalTest {

    @Test
    @SneakyThrows
    @DisplayName("Бины со скоупом singleton")
    void singletonScopeTest() {
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
    }

    @Test
    @SneakyThrows
    @DisplayName("Бины со скоупом thread-local")
    void threadLocalScopeTest() {
        var threadBean1 = container.getBean("thread-1");
        var threadBean2 = container.getBean("thread-1");

        softly.assertThat(threadBean1).isSameAs(threadBean2);

        Object[] threadBeanHolder = new Object[1];
        Thread thread = new Thread(() -> {
            try {
                threadBeanHolder[0] = container.getBean("thread-1");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        thread.start();
        thread.join();

        softly.assertThat(threadBeanHolder[0]).isNotSameAs(threadBean1);
    }

    @Test
    @SneakyThrows
    @DisplayName("Бины со скоупом prototype")
    void prototypeScopeTest() {
        var bean1 = container.getBean("car-proto");
        var bean2 = container.getBean("car-proto");

        softly.assertThat(bean1).isNotSameAs(bean2);
    }
}
