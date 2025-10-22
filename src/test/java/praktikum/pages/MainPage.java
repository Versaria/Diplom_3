package praktikum.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import praktikum.constants.Constants;

/**
 * Page Object Model для главной страницы Stellar Burgers
 * Инкапсулирует всю логику взаимодействия с элементами главной страницы
 * Обеспечивает удобный API для навигации и проверки состояния страницы
 * Наследует общие методы из BasePage для устранения дублирования кода
 */
public class MainPage extends BasePage {

    // Локаторы элементов страницы
    private final By loginAccountButton = By.xpath("//button[text()='" + Constants.LOGIN_ACCOUNT_BUTTON_TEXT + "']");
    private final By makeBurgerHeader = By.xpath("//h1[text()='Соберите бургер']");

    // Локаторы элементов хедера
    private final By logo = By.xpath("//div[contains(@class, 'AppHeader_header__logo')]");
    private final By constructorButton = By.xpath("//a[contains(@class, 'AppHeader_header__link') and p[text()='Конструктор']]");
    private final By personalAccountButton = By.xpath("//p[text()='Личный Кабинет']");

    /**
     * Конструктор класса MainPage
     *
     * @param driver Экземпляр WebDriver для управления браузером
     */
    public MainPage(WebDriver driver) {
        super(driver);
    }

    // Основные действия на странице

    /**
     * Открывает главную страницу Stellar Burgers в браузере
     * Использует базовый URL из констант и обрабатывает возможные диалоги
     */
    public void open() {
        System.out.println("Открытие главной страницы: " + Constants.BASE_URL);
        driver.get(Constants.BASE_URL);
        waitForLoad();
        handlePotentialDialogs(); // Обрабатываем возможные диалоги после загрузки
    }

    /**
     * Кликает по основной кнопке "Войти в аккаунт" на главной странице
     * Выполняет переход на страницу авторизации
     */
    public void clickLoginAccountButton() {
        System.out.println("Клик по кнопке 'Войти в аккаунт'");
        click(loginAccountButton);
    }

    /**
     * Кликает по кнопке "Личный Кабинет" в верхнем меню
     * Выполняет переход в личный кабинет (для авторизованных пользователей)
     * или на страницу авторизации (для неавторизованных)
     */
    public void clickPersonalAccountButton() {
        System.out.println("Клик по кнопке 'Личный Кабинет' в хедере");
        click(personalAccountButton);
    }

    /**
     * Кликает по логотипу для возврата в конструктор
     * Используется для навигации из других разделов
     */
    public void clickLogo() {
        System.out.println("Клик по логотипу для возврата в конструктор");
        click(logo);
    }

    /**
     * Кликает по кнопке "Конструктор" в хедере
     * Альтернативный способ возврата в конструктор
     */
    public void clickConstructorButton() {
        System.out.println("Клик по кнопке 'Конструктор' в хедере");
        click(constructorButton);
    }

    /**
     * Ожидает полной загрузки главной страницы
     * Проверяет видимость основного заголовка как индикатора завершения загрузки
     */
    public void waitForLoad() {
        System.out.println("Ожидание загрузки главной страницы...");
        waitForElement(makeBurgerHeader);
        System.out.println("Главная страница загружена");
    }

    /**
     * Проверяет, что текущий URL соответствует главной странице
     * Используется для подтверждения нахождения на правильной странице
     *
     * @return true если URL соответствует главной странице, иначе false
     */
    public boolean isMainPageUrl() {
        String currentUrl = driver.getCurrentUrl();
        boolean isMainPage = currentUrl.equals(Constants.BASE_URL + "/") ||
                currentUrl.equals(Constants.BASE_URL + "/#/") ||
                currentUrl.contains(Constants.BASE_URL);
        System.out.println("URL соответствует главной странице: " + isMainPage + " (" + currentUrl + ")");
        return isMainPage;
    }
}