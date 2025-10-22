package praktikum.tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import praktikum.pages.LoginPage;
import praktikum.pages.MainPage;
import praktikum.pages.ProfilePage;
import praktikum.pages.RegisterPage;
import praktikum.utils.UserGenerator;
import praktikum.constants.Constants;

import static org.junit.Assert.assertTrue;

/**
 * Тестовый класс для проверки функциональности регистрации пользователя
 * Проверяет как успешную регистрацию с валидными данными, так и обработку ошибок
 */
public class RegisterTest extends BaseTest {

    /**
     * Позитивный тест успешной регистрации пользователя с валидными данными
     * Проверяет полный цикл регистрации и автоматический переход на страницу входа
     * Дополнительно проверяет, что можно войти с созданными данными
     */
    @Test
    @DisplayName("Успешная регистрация пользователя")
    @Description("Проверка успешной регистрации с валидными данными: имя, email и пароль минимальной длины")
    public void testSuccessfulRegistration() {
        System.out.println("=== НАЧАЛО ТЕСТА: Успешная регистрация пользователя ===");

        // Подготовка тестовых данных
        String name = UserGenerator.generateName();
        String email = UserGenerator.generateEmail();
        String password = UserGenerator.generateValidPassword();

        System.out.println("Тестовые данные:");
        System.out.println("Имя: " + name);
        System.out.println("Email: " + email);
        System.out.println("Длина пароля: " + password.length() + " символов");

        // Предварительная проверка сгенерированного пароля на соответствие требованиям
        assertTrue("Пароль должен быть не менее " + Constants.MIN_PASSWORD_LENGTH + " символов",
                password.length() >= Constants.MIN_PASSWORD_LENGTH);

        // Выполнение регистрации
        navigateToRegisterPage();

        // Заполнение формы регистрации и отправка
        RegisterPage registerPage = new RegisterPage(driver);
        registerPage.waitForLoad();
        registerPage.register(name, email, password);
        System.out.println("Форма регистрации отправлена");

        // Проверка результата - ожидаем редирект на страницу входа
        checkRegistrationSuccess();

        // Дополнительная проверка - вход с созданными данными
        System.out.println("Дополнительная проверка: вход с созданными данными");
        loginAndVerify(email, password);

        System.out.println("=== ТЕСТ ЗАВЕРШЕН: Успешная регистрация пользователя ===");
    }

    /**
     * Негативный тест регистрации с паролем короче минимальной допустимой длины
     * Проверяет отображение корректного сообщения об ошибке валидации
     */
    @Test
    @DisplayName("Ошибка при коротком пароле")
    @Description("Проверка отображения ошибки при пароле менее 6 символов")
    public void testShortPasswordError() {
        System.out.println("=== НАЧАЛО ТЕСТА: Ошибка при коротком пароле ===");

        // Подготовка тестовых данных
        String name = UserGenerator.generateName();
        String email = UserGenerator.generateEmail();
        String password = UserGenerator.generateShortPassword();

        System.out.println("Тестовые данные для негативного теста:");
        System.out.println("Имя: " + name);
        System.out.println("Email: " + email);
        System.out.println("Длина пароля: " + password.length() + " символов (ожидается ошибка)");

        // Предварительная проверка, что пароль действительно короче минимальной длины
        assertTrue("Пароль должен быть короче " + Constants.MIN_PASSWORD_LENGTH + " символов для теста ошибки",
                password.length() < Constants.MIN_PASSWORD_LENGTH);

        // Выполнение регистрации с ошибкой
        navigateToRegisterPage();

        // Попытка регистрации с коротким паролем
        RegisterPage registerPage = new RegisterPage(driver);
        registerPage.waitForLoad();
        registerPage.register(name, email, password);
        System.out.println("Форма регистрации с коротким паролем отправлена");

        // Проверка ошибки валидации
        checkRegistrationError(registerPage);

        System.out.println("=== ТЕСТ ЗАВЕРШЕН: Ошибка при коротком пароле ===");
    }

    /**
     * Проверка успешной регистрации пользователя
     * Ожидает редирект на страницу входа после успешной регистрации
     */
    private void checkRegistrationSuccess() {
        // Ждем редирект на страницу входа
        LoginPage loginPageAfterRegister = new LoginPage(driver);
        loginPageAfterRegister.waitForLoad();

        assertTrue("После успешной регистрации должна отображаться страница входа",
                loginPageAfterRegister.isDisplayed());

        System.out.println("Успешная регистрация подтверждена - выполнено перенаправление на страницу входа");
    }

    /**
     * Проверка ошибки регистрации при некорректных данных
     * Убеждается, что отображается сообщение об ошибке с корректным содержанием
     *
     * @param registerPage Экземпляр страницы регистрации для проверки ошибки
     */
    private void checkRegistrationError(RegisterPage registerPage) {
        // Проверяем, что сообщение об ошибке отображается
        boolean isErrorDisplayed = registerPage.isErrorMessageDisplayed();
        assertTrue("Сообщение об ошибке должно отображаться при коротком пароле", isErrorDisplayed);

        System.out.println("Сообщение об ошибке отображается");

        // Получаем и проверяем текст сообщения об ошибке
        String errorMessage = registerPage.getErrorMessage();

        // Более гибкая проверка сообщения об ошибке
        boolean isExpectedError = errorMessage != null &&
                !errorMessage.trim().isEmpty() &&
                (errorMessage.toLowerCase().contains("парол") ||
                        errorMessage.toLowerCase().contains("некоррект") ||
                        errorMessage.toLowerCase().contains("коротк") ||
                        errorMessage.toLowerCase().contains("минимальн"));

        assertTrue("Сообщение об ошибке должно содержать информацию о проблеме с паролем. Получено: '" + errorMessage + "'",
                isExpectedError);

        System.out.println("Сообщение об ошибке корректно: " + errorMessage);
    }

    /**
     * Вспомогательный метод для навигации на страницу регистрации
     * Выполняет переход с главной страницы через форму авторизации
     */
    private void navigateToRegisterPage() {
        System.out.println("Навигация на страницу регистрации...");

        MainPage mainPage = new MainPage(driver);
        mainPage.open();
        mainPage.waitForLoad();
        mainPage.clickLoginAccountButton();

        LoginPage loginPage = new LoginPage(driver);
        loginPage.waitForLoad();
        loginPage.clickRegisterLink();

        System.out.println("Успешная навигация на страницу регистрации");
    }

    /**
     * Выполняет авторизацию пользователя и проверяет её успешность
     * Используется для проверки, что зарегистрированный пользователь может войти
     *
     * @param email Email пользователя для авторизации
     * @param password Пароль пользователя для авторизации
     */
    private void loginAndVerify(String email, String password) {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.waitForLoad();
        loginPage.login(email, password);

        // Проверка успешного редиректа на главную страницу
        MainPage mainPage = new MainPage(driver);
        mainPage.waitForLoad();

        assertTrue("После успешного входа должна отображаться главная страница",
                driver.getCurrentUrl().contains(Constants.BASE_URL));

        // Переход в личный кабинет для проверки авторизации
        mainPage.clickPersonalAccountButton();

        ProfilePage profilePage = new ProfilePage(driver);
        profilePage.waitForLoad();

        assertTrue("После успешной регистрации и входа должна отображаться кнопка выхода в личном кабинете",
                profilePage.isUserAuthorized());

        System.out.println("Дополнительная проверка пройдена: пользователь успешно авторизован после регистрации");
    }
}