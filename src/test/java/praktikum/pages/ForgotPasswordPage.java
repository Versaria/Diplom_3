package praktikum.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import praktikum.constants.Constants;

/**
 * Page Object Model для страницы восстановления пароля
 * Предоставляет минимальный функционал для навигации со страницы восстановления
 * Основное назначение - переход обратно на страницу авторизации
 * Наследует общие методы из BasePage для устранения дублирования кода
 */
public class ForgotPasswordPage extends BasePage {

    // Локаторы элементов страницы

    /**
     * Локатор ссылки "Войти" для возврата на страницу авторизации
     */
    private final By loginLink = By.xpath(".//a[text()='" + Constants.LOGIN_LINK_TEXT + "']");

    /**
     * Конструктор класса ForgotPasswordPage
     *
     * @param driver Экземпляр WebDriver для управления браузером
     */
    public ForgotPasswordPage(WebDriver driver) {
        super(driver);
    }

    // Основные действия на странице

    /**
     * Кликает по ссылке "Войти" для возврата на страницу авторизации
     * Используется в тестах навигации между формами аутентификации
     */
    public void clickLoginLink() {
        click(loginLink);
    }

    /**
     * Ожидает полной загрузки страницы восстановления пароля
     * Проверяет видимость ссылки "Войти" как индикатора готовности страницы
     */
    public void waitForLoad() {
        waitForElement(loginLink);
    }
}