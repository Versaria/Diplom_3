package praktikum.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WebDriverFactory {

    public static WebDriver createDriver(String browserType) {
        // Отключаем логи Selenium
        Logger.getLogger("org.openqa.selenium").setLevel(Level.SEVERE);
        System.setProperty("webdriver.chrome.silentOutput", "true");

        String browser = browserType.toLowerCase();
        System.out.println("Создание драйвера для браузера: " + browser);

        switch (browser) {
            case "yandex":
                ChromeOptions optionsYandex = createChromeOptions();
                optionsYandex.setBinary("/Applications/Yandex.app/Contents/MacOS/Yandex");
                return new ChromeDriver(optionsYandex);

            case "chrome":
            default:
                ChromeOptions options = createChromeOptions();
                return new ChromeDriver(options);
        }
    }

    private static ChromeOptions createChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments(
                "--window-size=1920,1080",
                "--no-sandbox",
                "--disable-dev-shm-usage",
                "--remote-allow-origins=*",
                "--log-level=3",
                "--disable-logging"
        );
        return options;
    }
}