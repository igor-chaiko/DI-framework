import framework.container.IIocContainer;
import framework.container.IoCContainerImpl;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class AbstractFunctionalTest {
    protected IIocContainer container;
    protected SoftAssertions softly = new SoftAssertions();

    @BeforeEach
    void setup() throws Exception {
        container = new IoCContainerImpl("src/main/resources/config.xml");
    }

    @AfterEach
    void tearDown() {
        softly.assertAll();
    }
}
