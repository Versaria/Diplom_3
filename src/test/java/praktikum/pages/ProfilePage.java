package praktikum.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import praktikum.constants.Constants;

/**
 * Page Object Model для страницы личного кабинета пользователя
 * Предоставляет методы для работы с профилем и проверки состояния авторизации
 * Используется для подтверждения успешного входа в систему и выхода из нее
 * Наследует общие методы из BasePage для устранения дублирования кода
 */
public class ProfilePage extends BasePage {

    // Локаторы элементов личного кабинета
    private final By profileLink = By.xpath(String.format("//a[text()='%s']", Constants.PROFILE_LINK_TEXT));
    private final By logoutButton = By.xpath(String.format("//button[text()='%s']", Constants.LOGOUT_BUTTON_TEXT));

    /**
     * Конструктор класса ProfilePage
     *
     * @param driver Экземпляр WebDriver для управления браузером
     */
    public ProfilePage(WebDriver driver) {
        super(driver);
    }

    // Методы проверки состояния

    @Step("Ожидание загрузки страницы личного кабинета")
    public void waitForLoad() {
        waitForElement(profileLink);
    }

    @Step("Проверка видимости кнопки 'Выход'")
    public boolean isLogoutButtonVisible() {
        return isElementVisible(logoutButton);
    }

    @Step("Проверка авторизации пользователя")
    public boolean isUserAuthorized() {
        return isLogoutButtonVisible();
    }

    // Методы взаимодействия

    @Step("Клик по кнопке 'Выход'")
    public void clickLogoutButton() {
        System.out.println("Клик по кнопке выхода из аккаунта");
        click(logoutButton);
    }
}