package praktikum.pages;

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

    /**
     * Ожидает полной загрузки страницы личного кабинета
     * Проверяет видимость ссылки "Профиль" как индикатора готовности страницы
     */
    public void waitForLoad() {
        waitForElement(profileLink);
    }

    /**
     * Проверяет видимость кнопки "Выход" на странице
     * Кнопка выхода отображается только для авторизованных пользователей
     *
     * @return true если кнопка выхода видима, иначе false
     */
    public boolean isLogoutButtonVisible() {
        return isElementVisible(logoutButton);
    }

    /**
     * Проверяет, что пользователь успешно авторизован в системе
     * Основной критерий авторизации - наличие кнопки выхода в личном кабинете
     *
     * @return true если пользователь авторизован, иначе false
     */
    public boolean isUserAuthorized() {
        return isLogoutButtonVisible();
    }

    // Методы взаимодействия

    /**
     * Выполняет клик по кнопке "Выход" для завершения сессии пользователя
     * Используется в тестах выхода из аккаунта
     */
    public void clickLogoutButton() {
        System.out.println("Клик по кнопке выхода из аккаунта");
        click(logoutButton);
    }
}