package praktikum.pages;

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
 * Использует JavaScript для надежного переключения между секциями
 * Наследует общие методы из BasePage для устранения дублирования кода
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
     *
     * @param driver Экземпляр WebDriver для управления браузером
     */
    public ConstructorPage(WebDriver driver) {
        super(driver);
    }

    // Методы для переключения между секциями

    /**
     * Активирует секцию "Булки" в конструкторе
     * Использует JavaScript клик для надежности и прокрутки к элементу
     */
    public void clickBunSection() {
        System.out.println("Переключение на секцию 'Булки'");
        clickWithJS(bunSection);
        waitForSectionActivation(Constants.BUNS_SECTION_TEXT);
        waitForScrollToBuns();
    }

    /**
     * Активирует секцию "Соусы" в конструкторе
     * Использует JavaScript клик для надежности и прокрутки к элементу
     */
    public void clickSauceSection() {
        System.out.println("Переключение на секцию 'Соусы'");
        clickWithJS(sauceSection);
        waitForSectionActivation(Constants.SAUCES_SECTION_TEXT);
        waitForScrollToSauces();
    }

    /**
     * Активирует секцию "Начинки" в конструкторе
     * Использует JavaScript клик для надежности и прокрутки к элементу
     */
    public void clickFillingSection() {
        System.out.println("Переключение на секцию 'Начинки'");
        clickWithJS(fillingSection);
        waitForSectionActivation(Constants.FILLINGS_SECTION_TEXT);
        waitForScrollToFillings();
    }

    // Методы для проверки состояния

    /**
     * Получает текст текущей активной секции конструктора
     * Используется для верификации успешного переключения между секциями
     *
     * @return Текст активной секции ("Булки", "Соусы" или "Начинки")
     */
    public String getActiveSectionText() {
        waitForElement(activeSection);
        String activeText = getElementText(activeSection);
        System.out.println("Активная секция: " + activeText);
        return activeText;
    }

    /**
     * Ожидает полной загрузки конструктора бургеров
     * Проверяет видимость основного контейнера конструктора
     */
    public void waitForLoad() {
        System.out.println("Ожидание загрузки конструктора...");
        waitForElement(constructorContainer);
        System.out.println("Конструктор загружен");
    }

    // Методы для проверки скролла к разделам

    /**
     * Проверяет, что произошел скролл к разделу "Булки"
     * Использует JavaScript для определения видимости элемента в viewport
     *
     * @return true если раздел "Булки" виден в viewport после скролла
     */
    public boolean isBunsSectionScrolledIntoView() {
        return isElementInViewport(bunsGroupTitle);
    }

    /**
     * Проверяет, что произошел скролл к разделу "Соусы"
     * Использует JavaScript для определения видимости элемента в viewport
     *
     * @return true если раздел "Соусы" виден в viewport после скролла
     */
    public boolean isSaucesSectionScrolledIntoView() {
        return isElementInViewport(saucesGroupTitle);
    }

    /**
     * Проверяет, что произошел скролл к разделу "Начинки"
     * Использует JavaScript для определения видимости элемента в viewport
     *
     * @return true если раздел "Начинки" виден в viewport после скролла
     */
    public boolean isFillingsSectionScrolledIntoView() {
        return isElementInViewport(fillingsGroupTitle);
    }

    // Вспомогательные приватные методы

    /**
     * Выполняет клик по элементу с использованием JavaScript
     * Обеспечивает надежное взаимодействие с элементами, которые могут быть
     * перекрыты другими элементами или требовать прокрутки.
     *
     * @param locator Локатор элемента для клика
     */
    private void clickWithJS(By locator) {
        System.out.println("Выполнение JS клика по элементу: " + locator);

        // Ожидаем появления элемента с увеличенным таймаутом
        WebElement element = waitForElement(locator, 10);

        // Прокручиваем страницу к элементу для обеспечения видимости
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', behavior: 'smooth'});", element);

        // Ожидаем кликабельности элемента после прокрутки
        waitForElementToBeClickable(locator);

        // Сначала пробуем обычный клик
        try {
            element.click();
            System.out.println("Обычный клик выполнен успешно");
        } catch (Exception e) {
            // Если обычный клик не работает, используем JS
            System.out.println("Обычный клик не сработал, используем JS клик");
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
    }

    /**
     * Ожидает активации указанной секции
     * Использует метод из BasePage для единообразия
     *
     * @param sectionText Текст секции для проверки активации
     */
    private void waitForSectionActivation(String sectionText) {
        System.out.println("Ожидание активации секции: " + sectionText);
        waitForTextToBe(activeSection, sectionText);
        System.out.println("Секция '" + sectionText + "' активирована");
    }

    /**
     * Проверяет, находится ли элемент в области видимости (viewport)
     * Использует JavaScript для точного определения видимости элемента
     *
     * @param locator Локатор элемента для проверки
     * @return true если элемент виден в viewport
     */
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

    /**
     * Ожидает скролла к разделу "Булки"
     */
    private void waitForScrollToBuns() {
        System.out.println("Ожидание скролла к разделу 'Булки'");
        waitForElementToBeInViewport(bunsGroupTitle);
        System.out.println("Скролл к разделу 'Булки' выполнен");
    }

    /**
     * Ожидает скролла к разделу "Соусы"
     */
    private void waitForScrollToSauces() {
        System.out.println("Ожидание скролла к разделу 'Соусы'");
        waitForElementToBeInViewport(saucesGroupTitle);
        System.out.println("Скролл к разделу 'Соусы' выполнен");
    }

    /**
     * Ожидает скролла к разделу "Начинки"
     */
    private void waitForScrollToFillings() {
        System.out.println("Ожидание скролла к разделу 'Начинки'");
        waitForElementToBeInViewport(fillingsGroupTitle);
        System.out.println("Скролл к разделу 'Начинки' выполнен");
    }

    /**
     * Ожидает, пока элемент окажется в области видимости
     *
     * @param locator Локатор элемента
     */
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