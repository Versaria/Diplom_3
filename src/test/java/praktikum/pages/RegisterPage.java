package praktikum.pages;

import io.qameta.allure.Step;
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

    @Step("Заполнение поля 'Имя': {name}")
    public void setName(String name) {
        setField(nameField, name);
    }

    @Step("Заполнение поля 'Email': {email}")
    public void setEmail(String email) {
        setField(emailField, email);
    }

    @Step("Заполнение поля 'Пароль'")
    public void setPassword(String password) {
        setField(passwordField, password);
    }

    // Методы для взаимодействия с формой

    @Step("Клик по кнопке 'Зарегистрироваться'")
    public void clickRegisterButton() {
        click(registerButton);
    }

    @Step("Клик по ссылке 'Войти'")
    public void clickLoginLink() {
        click(loginLink);
    }

    // Композиционные методы

    @Step("Регистрация пользователя: {name}, {email}")
    public void register(String name, String email, String password) {
        setName(name);
        setEmail(email);
        setPassword(password);
        clickRegisterButton();
    }

    // Методы для работы с ошибками валидации

    @Step("Получение сообщения об ошибке")
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

    @Step("Проверка отображения сообщения об ошибке")
    public boolean isErrorMessageDisplayed() {
        return isElementVisible(errorMessage);
    }

    // Методы проверки состояния

    @Step("Ожидание загрузки страницы регистрации")
    public void waitForLoad() {
        waitForElement(registerButton);
    }
}