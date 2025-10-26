package praktikum.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import praktikum.constants.Constants;

/**
 * Page Object Model для раздела конструктора бургеров
 * Обеспечивает взаимодействие с разделами ингредиентов (булки, соусы, начинки)
 * ИСПРАВЛЕНИЯ:
 * 1. Добавлены аннотации @Step для Allure отчетов
 */
public class ConstructorPage extends BasePage {

    // Локаторы секций конструктора
    private final By bunSection = By.xpath("//div[contains(@class, 'tab_tab')]//span[text()='" + Constants.BUNS_SECTION_TEXT + "']/parent::div");
    private final By sauceSection = By.xpath("//div[contains(@class, 'tab_tab')]//span[text()='" + Constants.SAUCES_SECTION_TEXT + "']/parent::div");
    private final By fillingSection = By.xpath("//div[contains(@class, 'tab_tab')]//span[text()='" + Constants.FILLINGS_SECTION_TEXT + "']/parent::div");

    // Улучшенный локатор активной секции
    private final By activeSection = By.xpath("//div[contains(@class, '" + Constants.ACTIVE_SECTION_CLASS + "')]//span");

    private final By constructorContainer = By.xpath("//section[contains(@class, 'BurgerIngredients_ingredients')]");

    // Локаторы заголовков групп ингредиентов для проверки скролла
    private final By bunsGroupTitle = By.xpath("//h2[text()='" + Constants.BUNS_SECTION_TEXT + "']");
    private final By saucesGroupTitle = By.xpath("//h2[text()='" + Constants.SAUCES_SECTION_TEXT + "']");
    private final By fillingsGroupTitle = By.xpath("//h2[text()='" + Constants.FILLINGS_SECTION_TEXT + "']");

    /**
     * Конструктор класса ConstructorPage
     */
    public ConstructorPage(WebDriver driver) {
        super(driver);
    }

    @Step("Активировать секцию 'Булки'")
    public void clickBunSection() {
        System.out.println("Переключение на секцию 'Булки'");
        clickWithJS(bunSection);
        waitForSectionActivation(Constants.BUNS_SECTION_TEXT);
        waitForScrollToBuns();
    }

    @Step("Активировать секцию 'Соусы'")
    public void clickSauceSection() {
        System.out.println("Переключение на секцию 'Соусы'");
        clickWithJS(sauceSection);
        waitForSectionActivation(Constants.SAUCES_SECTION_TEXT);
        waitForScrollToSauces();
    }

    @Step("Активировать секцию 'Начинки'")
    public void clickFillingSection() {
        System.out.println("Переключение на секцию 'Начинки'");
        clickWithJS(fillingSection);
        waitForSectionActivation(Constants.FILLINGS_SECTION_TEXT);
        waitForScrollToFillings();
    }

    @Step("Получить текст активной секции")
    public String getActiveSectionText() {
        waitForElement(activeSection);
        String activeText = getElementText(activeSection);
        System.out.println("Активная секция: " + activeText);
        return activeText;
    }

    @Step("Ожидание загрузки конструктора")
    public void waitForLoad() {
        System.out.println("Ожидание загрузки конструктора...");
        waitForElement(constructorContainer);
        System.out.println("Конструктор загружен");
    }

    @Step("Проверить скролл к разделу 'Булки'")
    public boolean isBunsSectionScrolledIntoView() {
        return isElementInViewport(bunsGroupTitle);
    }

    @Step("Проверить скролл к разделу 'Соусы'")
    public boolean isSaucesSectionScrolledIntoView() {
        return isElementInViewport(saucesGroupTitle);
    }

    @Step("Проверить скролл к разделу 'Начинки'")
    public boolean isFillingsSectionScrolledIntoView() {
        return isElementInViewport(fillingsGroupTitle);
    }

    // Вспомогательные приватные методы
    private void clickWithJS(By locator) {
        System.out.println("Выполнение JS клика по элементу: " + locator);
        WebElement element = waitForElement(locator, 10);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', behavior: 'smooth'});", element);
        waitForElementToBeClickable(locator);

        try {
            element.click();
            System.out.println("Обычный клик выполнен успешно");
        } catch (Exception e) {
            System.out.println("Обычный клик не сработал, используем JS клик");
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
    }

    private void waitForSectionActivation(String sectionText) {
        System.out.println("Ожидание активации секции: " + sectionText);
        waitForTextToBe(activeSection, sectionText);
        System.out.println("Секция '" + sectionText + "' активирована");
    }

    private boolean isElementInViewport(By locator) {
        try {
            WebElement element = driver.findElement(locator);
            return (Boolean) ((JavascriptExecutor) driver).executeScript(
                    "var elem = arguments[0];" +
                            "var rect = elem.getBoundingClientRect();" +
                            "return (" +
                            "  rect.top >= 0 &&" +
                            "  rect.left >= 0 &&" +
                            "  rect.bottom <= (window.innerHeight || document.documentElement.clientHeight) &&" +
                            "  rect.right <= (window.innerWidth || document.documentElement.clientWidth)" +
                            ");", element);
        } catch (Exception e) {
            System.err.println("Ошибка при проверке видимости элемента: " + e.getMessage());
            return false;
        }
    }

    private void waitForScrollToBuns() {
        System.out.println("Ожидание скролла к разделу 'Булки'");
        waitForElementToBeInViewport(bunsGroupTitle);
        System.out.println("Скролл к разделу 'Булки' выполнен");
    }

    private void waitForScrollToSauces() {
        System.out.println("Ожидание скролла к разделу 'Соусы'");
        waitForElementToBeInViewport(saucesGroupTitle);
        System.out.println("Скролл к разделу 'Соусы' выполнен");
    }

    private void waitForScrollToFillings() {
        System.out.println("Ожидание скролла к разделу 'Начинки'");
        waitForElementToBeInViewport(fillingsGroupTitle);
        System.out.println("Скролл к разделу 'Начинки' выполнен");
    }

    private void waitForElementToBeInViewport(By locator) {
        WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(5));
        customWait.until(driver -> {
            try {
                WebElement element = driver.findElement(locator);
                return (Boolean) ((JavascriptExecutor) driver).executeScript(
                        "var elem = arguments[0];" +
                                "var rect = elem.getBoundingClientRect();" +
                                "return (" +
                                "  rect.top >= 0 &&" +
                                "  rect.left >= 0 &&" +
                                "  rect.bottom <= (window.innerHeight || document.documentElement.clientHeight) &&" +
                                "  rect.right <= (window.innerWidth || document.documentElement.clientWidth)" +
                                ");", element);
            } catch (Exception e) {
                return false;
            }
        });
    }
}