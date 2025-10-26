package praktikum.pages;

import io.qameta.allure.Step;
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

    @Step("Заполнение поля email: {email}")
    public void setEmail(String email) {
        System.out.println("Заполнение поля email: " + email);
        setField(emailField, email);
    }

    @Step("Заполнение поля пароля")
    public void setPassword(String password) {
        System.out.println("Заполнение поля пароля");
        setField(passwordField, password);
    }

    @Step("Клик по кнопке 'Войти'")
    public void clickLoginButton() {
        System.out.println("Клик по кнопке 'Войти'");
        click(loginButton);
    }

    // Методы для навигации

    @Step("Клик по ссылке 'Зарегистрироваться'")
    public void clickRegisterLink() {
        System.out.println("Клик по ссылке 'Зарегистрироваться'");
        click(registerLink);
    }

    @Step("Клик по ссылке 'Восстановить пароль'")
    public void clickForgotPasswordLink() {
        System.out.println("Клик по ссылке 'Восстановить пароль'");
        click(forgotPasswordLink);
    }

    // Композиционные методы

    @Step("Авторизация пользователя: {email}")
    public void login(String email, String password) {
        System.out.println("Выполнение авторизации для пользователя: " + email);
        setEmail(email);
        setPassword(password);
        clickLoginButton();
    }

    // Методы проверки состояния

    @Step("Ожидание загрузки страницы авторизации")
    public void waitForLoad() {
        System.out.println("Ожидание загрузки страницы авторизации...");
        waitForElement(loginFormTitle);
        System.out.println("Страница авторизации загружена");
    }

    @Step("Проверка отображения страницы авторизации")
    public boolean isDisplayed() {
        boolean displayed = isElementVisible(loginFormTitle);
        System.out.println("Страница авторизации отображается: " + displayed);
        return displayed;
    }
}