package praktikum.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import praktikum.constants.Constants;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Page Object Model для страницы регистрации нового пользователя
 * Инкапсулирует логику работы с формой регистрации, включая валидацию полей
 * Обеспечивает методы для заполнения данных и обработки ошибок валидации
 * Наследует общие методы из BasePage для устранения дублирования кода
 */
public class RegisterPage extends BasePage {

    // Локаторы элементов формы регистрации
    private final By nameField = By.xpath(".//label[text()='Имя']/following-sibling::input");
    private final By emailField = By.xpath(".//label[text()='Email']/following-sibling::input");
    private final By passwordField = By.xpath(".//label[text()='Пароль']/following-sibling::input");
    private final By registerButton = By.xpath(".//button[text()='" + Constants.REGISTER_BUTTON_TEXT + "']");
    private final By loginLink = By.xpath(".//a[text()='" + Constants.LOGIN_LINK_TEXT + "']");
    private final By errorMessage = By.xpath(".//p[contains(@class, 'input__error')]");

    /**
     * Конструктор класса RegisterPage
     *
     * @param driver Экземпляр WebDriver для управления браузером
     */
    public RegisterPage(WebDriver driver) {
        super(driver);
    }

    // Методы для заполнения формы

    /**
     * Заполняет поле "Имя" в форме регистрации
     *
     * @param name Полное имя пользователя для регистрации
     */
    public void setName(String name) {
        setField(nameField, name);
    }

    /**
     * Заполняет поле "Email" в форме регистрации
     *
     * @param email Email адрес пользователя для регистрации
     */
    public void setEmail(String email) {
        setField(emailField, email);
    }

    /**
     * Заполняет поле "Пароль" в форме регистрации
     *
     * @param password Пароль пользователя для регистрации
     */
    public void setPassword(String password) {
        setField(passwordField, password);
    }

    // Методы для взаимодействия с формой

    /**
     * Выполняет клик по кнопке "Зарегистрироваться" для отправки формы
     */
    public void clickRegisterButton() {
        click(registerButton);
    }

    /**
     * Кликает по ссылке "Войти" для перехода на страницу авторизации
     * Используется в тестах навигации между формами
     */
    public void clickLoginLink() {
        click(loginLink);
    }

    // Композиционные методы

    /**
     * Выполняет полный процесс регистрации пользователя
     * Заполняет все обязательные поля формы и отправляет её
     *
     * @param name Полное имя пользователя
     * @param email Email адрес пользователя
     * @param password Пароль пользователя
     */
    public void register(String name, String email, String password) {
        setName(name);
        setEmail(email);
        setPassword(password);
        clickRegisterButton();
    }

    // Методы для работы с ошибками валидации

    /**
     * Получает текст сообщения об ошибке валидации с проверкой содержания
     * Проверяет, что сообщение не пустое и содержит информацию о проблеме с паролем
     * Используется в негативных тестах для проверки корректности валидации
     *
     * @return Текст сообщения об ошибке
     * @throws AssertionError если сообщение об ошибке не отображается или не содержит нужную информацию
     */
    public String getErrorMessage() {
        try {
            // Используем существующий метод waitForElement с коротким таймаутом
            waitForElement(errorMessage, 5);
            String errorText = getElementText(errorMessage).trim();

            // Проверяем, что сообщение об ошибке не пустое
            assertFalse("Сообщение об ошибке не должно быть пустым", errorText.isEmpty());

            // Проверяем, что сообщение содержит информацию о проблеме с паролем
            // Используем частичное совпадение для большей стабильности тестов
            boolean isPasswordError = errorText.toLowerCase().contains("парол") ||
                    errorText.contains(Constants.SHORT_PASSWORD_ERROR) ||
                    errorText.toLowerCase().contains("password") ||
                    errorText.toLowerCase().contains("некоррект");

            assertTrue("Сообщение об ошибке должно относиться к валидации пароля. Получено: " + errorText,
                    isPasswordError);

            System.out.println("Получено сообщение об ошибке: " + errorText);
            return errorText;

        } catch (Exception e) {
            // Если сообщение об ошибке не появилось за отведенное время
            fail("Сообщение об ошибке должно отображаться при некорректных данных регистрации: " + e.getMessage());
            return "";
        }
    }

    /**
     * Проверяет, отображается ли сообщение об ошибке на странице
     * Используется для быстрой проверки наличия ошибки без получения текста
     *
     * @return true если сообщение об ошибке отображается, иначе false
     */
    public boolean isErrorMessageDisplayed() {
        return isElementVisible(errorMessage);
    }

    // Методы проверки состояния

    /**
     * Ожидает полной загрузки страницы регистрации
     * Проверяет видимость кнопки регистрации как индикатора готовности формы
     */
    public void waitForLoad() {
        waitForElement(registerButton);
    }
}
