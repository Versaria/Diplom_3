package praktikum.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import praktikum.constants.Constants;

/**
 * Page Object Model для страницы авторизации Stellar Burgers
 * Предоставляет методы для заполнения формы входа и навигации по связанным страницам
 * Используется в тестах входа через различные сценарии
 * Наследует общие методы из BasePage для устранения дублирования кода
 */
public class LoginPage extends BasePage {

    // Локаторы элементов формы авторизации
    private final By emailField = By.xpath(".//input[@name='name']");
    private final By passwordField = By.xpath(".//input[@name='Пароль']");
    private final By loginButton = By.xpath(".//button[text()='" + Constants.LOGIN_BUTTON_TEXT + "']");
    private final By registerLink = By.xpath(".//a[text()='" + Constants.REGISTER_LINK_TEXT + "']");
    private final By forgotPasswordLink = By.xpath(".//a[text()='" + Constants.FORGOT_PASSWORD_TEXT + "']");
    private final By loginFormTitle = By.xpath(".//h2[text()='Вход']");

    /**
     * Конструктор класса LoginPage
     *
     * @param driver Экземпляр WebDriver для управления браузером
     */
    public LoginPage(WebDriver driver) {
        super(driver);
    }

    // Методы для работы с формой

    /**
     * Заполняет поле email/логина указанным значением
     *
     * @param email Email или логин пользователя для авторизации
     */
    public void setEmail(String email) {
        System.out.println("Заполнение поля email: " + email);
        setField(emailField, email);
    }

    /**
     * Заполняет поле пароля указанным значением
     *
     * @param password Пароль пользователя для авторизации
     */
    public void setPassword(String password) {
        System.out.println("Заполнение поля пароля");
        setField(passwordField, password);
    }

    /**
     * Выполняет клик по кнопке "Войти" для отправки формы
     */
    public void clickLoginButton() {
        System.out.println("Клик по кнопке 'Войти'");
        click(loginButton);
    }

    // Методы для навигации

    /**
     * Кликает по ссылке "Зарегистрироваться" для перехода на страницу регистрации
     */
    public void clickRegisterLink() {
        System.out.println("Клик по ссылке 'Зарегистрироваться'");
        click(registerLink);
    }

    /**
     * Кликает по ссылке "Восстановить пароль" для перехода на страницу восстановления
     */
    public void clickForgotPasswordLink() {
        System.out.println("Клик по ссылке 'Восстановить пароль'");
        click(forgotPasswordLink);
    }

    // Композиционные методы

    /**
     * Выполняет полный процесс авторизации пользователя
     * Заполняет оба поля и отправляет форму
     *
     * @param email Email пользователя
     * @param password Пароль пользователя
     */
    public void login(String email, String password) {
        System.out.println("Выполнение авторизации для пользователя: " + email);
        setEmail(email);
        setPassword(password);
        clickLoginButton();
    }

    // Методы проверки состояния

    /**
     * Ожидает полной загрузки страницы авторизации
     * Проверяет видимость заголовка формы как индикатора готовности
     */
    public void waitForLoad() {
        System.out.println("Ожидание загрузки страницы авторизации...");
        waitForElement(loginFormTitle);
        System.out.println("Страница авторизации загружена");
    }

    /**
     * Проверяет, отображается ли страница авторизации в текущий момент
     * Используется для подтверждения успешной навигации на страницу входа
     *
     * @return true если страница авторизации отображается, иначе false
     */
    public boolean isDisplayed() {
        boolean displayed = isElementVisible(loginFormTitle);
        System.out.println("Страница авторизации отображается: " + displayed);
        return displayed;
    }
}