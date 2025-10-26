package praktikum.tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Test;
import praktikum.api.ApiClient;
import praktikum.pages.LoginPage;
import praktikum.pages.MainPage;
import praktikum.pages.RegisterPage;
import praktikum.utils.UserGenerator;
import praktikum.constants.Constants;

import static org.junit.Assert.assertTrue;

/**
 * Тестовый класс для проверки функциональности регистрации пользователя
 * ИСПРАВЛЕНИЯ:
 * 1. Добавлено удаление созданного пользователя через API после тестов
 */
public class RegisterTest extends BaseTest {

    private String testUserEmail;
    private String testUserPassword;

    /**
     * Удаляет тестового пользователя через API после каждого теста
     * ИСПРАВЛЕНИЕ: Добавлено удаление пользователя после тестов регистрации
     */
    @After
    public void tearDown() {
        // Удаляем пользователя через API если он был создан
        if (testUserEmail != null && testUserPassword != null) {
            try {
                // Получаем токен через логин и удаляем пользователя
                String token = ApiClient.loginUserViaApi(testUserEmail, testUserPassword);
                if (token != null) {
                    ApiClient.deleteUserViaApi(token);
                    System.out.println("Тестовый пользователь удален: " + testUserEmail);
                }
            } catch (Exception e) {
                System.err.println("Ошибка при удалении тестового пользователя: " + e.getMessage());
            }
        }
        super.tearDown();
    }

    /**
     * Позитивный тест успешной регистрации пользователя с валидными данными
     * ИСПРАВЛЕНИЕ: Пользователь теперь удаляется после теста
     */
    @Test
    @DisplayName("Успешная регистрация пользователя")
    @Description("Проверка успешной регистрации с валидными данными: имя, email и пароль минимальной длины")
    public void testSuccessfulRegistration() {
        System.out.println("=== НАЧАЛО ТЕСТА: Успешная регистрация пользователя ===");

        // Подготовка тестовых данных
        String name = UserGenerator.generateName();
        testUserEmail = UserGenerator.generateEmail();
        testUserPassword = UserGenerator.generateValidPassword();

        System.out.println("Тестовые данные:");
        System.out.println("Имя: " + name);
        System.out.println("Email: " + testUserEmail);
        System.out.println("Длина пароля: " + testUserPassword.length() + " символов");

        // Предварительная проверка сгенерированного пароля
        assertTrue("Пароль должен быть не менее " + Constants.MIN_PASSWORD_LENGTH + " символов",
                testUserPassword.length() >= Constants.MIN_PASSWORD_LENGTH);

        // Выполнение регистрации
        navigateToRegisterPage();

        // Заполнение формы регистрации и отправка
        RegisterPage registerPage = new RegisterPage(driver);
        registerPage.waitForLoad();
        registerPage.register(name, testUserEmail, testUserPassword);
        System.out.println("Форма регистрации отправлена");

        // Проверка результата - ожидаем редирект на страницу входа
        checkRegistrationSuccess();

        System.out.println("=== ТЕСТ ЗАВЕРШЕН: Успешная регистрация пользователя ===");
    }

    /**
     * Негативный тест регистрации с паролем короче минимальной допустимой длины
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
}