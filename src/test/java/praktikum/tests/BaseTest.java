package praktikum.tests;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;
import praktikum.utils.WebDriverFactory;

/**
 * Базовый класс для всех UI тестов
 * Содержит общую логику инициализации и завершения работы драйвера
 */
public class BaseTest {
    protected WebDriver driver;
    protected WebDriverWait wait;

    @Before
    public void setUp() {
        // Отключаем предупреждения CDP
        Logger.getLogger("org.openqa.selenium").setLevel(Level.SEVERE);
        Logger.getLogger("org.openqa.selenium.devtools").setLevel(Level.SEVERE);

        String browser = System.getProperty("browser", "chrome");
        driver = WebDriverFactory.createDriver(browser);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
        System.out.println("=== КОНФИГУРАЦИЯ ТЕСТА ===");
        System.out.println("Браузер: " + browser);
        System.out.println("==========================");
    }

    @After
    public void tearDown() {
        if (driver != null) {
            try {
                driver.quit();
                System.out.println("Драйвер успешно завершил работу");
            } catch (Exception e) {
                System.err.println("Ошибка при завершении работы драйвера: " + e.getMessage());
            }
        }
    }
}