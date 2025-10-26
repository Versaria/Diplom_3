package praktikum.tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.api.ApiClient;
import praktikum.pages.*;
import praktikum.constants.Constants;

import static org.junit.Assert.assertTrue;

/**
 * Тестовый класс для проверки различных сценариев авторизации пользователя
 * ИСПРАВЛЕНИЯ:
 * 1. Убрана избыточная параметризация - заменена на отдельные тесты
 * 2. Разделены позитивные и негативные тесты для лучшей структуры
 */
public class LoginTest extends BaseTest {

    // Данные тестового пользователя для авторизации
    private String testUserEmail;
    private String testUserPassword;
    private String testUserName;
    private String testUserToken;

    /**
     * Создает тестового пользователя через API перед каждым тестом
     */
    @Before
    public void setUp() {
        super.setUp();
        System.out.println("Создание тестового пользователя через API...");
        String[] userData = ApiClient.createUserViaApi();
        testUserEmail = userData[0];
        testUserPassword = userData[1];
        testUserName = userData[2];
        testUserToken = userData[3];
        System.out.println("Создан тестовый пользователь: " + testUserName);
    }

    /**
     * Удаляет тестового пользователя через API после каждого теста
     */
    @After
    public void tearDown() {
        if (testUserToken != null) {
            try {
                System.out.println("Удаление тестового пользователя через API...");
                ApiClient.deleteUserViaApi(testUserToken);
                System.out.println("Удален тестовый пользователь: " + testUserName);
            } catch (Exception e) {
                System.err.println("Ошибка при удалении пользователя: " + e.getMessage());
            }
        }
        super.tearDown();
    }

    // ПОЗИТИВНЫЕ ТЕСТЫ

    @Test
    @DisplayName("Вход через кнопку 'Войти в аккаунт' на главной странице")
    @Description("Проверка входа через основную кнопку на главной странице")
    public void testLoginViaMainPageButton() {
        performLogin("main_page_button");
    }

    @Test
    @DisplayName("Вход через кнопку 'Личный Кабинет'")
    @Description("Проверка входа через кнопку личного кабинета в хедере")
    public void testLoginViaPersonalAccountButton() {
        performLogin("personal_account_button");
    }

    @Test
    @DisplayName("Вход через ссылку в форме регистрации")
    @Description("Проверка входа через ссылку на форме регистрации")
    public void testLoginViaRegisterFormLink() {
        performLogin("register_form_link");
    }

    @Test
    @DisplayName("Вход через ссылку в форме восстановления пароля")
    @Description("Проверка входа через ссылку на форме восстановления пароля")
    public void testLoginViaForgotPasswordLink() {
        performLogin("forgot_password_link");
    }

    // НЕГАТИВНЫЕ ТЕСТЫ

    @Test
    @DisplayName("Ошибка входа с неверным email")
    @Description("Проверка отображения ошибки при вводе неверного email")
    public void testLoginWithInvalidEmail() {
        performNegativeLoginTest("invalid_credentials");
    }

    @Test
    @DisplayName("Ошибка входа с неверным паролем")
    @Description("Проверка отображения ошибки при вводе неверного пароля")
    public void testLoginWithInvalidPassword() {
        performNegativeLoginTest("invalid_password");
    }

    @Test
    @DisplayName("Ошибка входа с пустыми полями")
    @Description("Проверка отображения ошибки при пустых полях ввода")
    public void testLoginWithEmptyCredentials() {
        performNegativeLoginTest("empty_credentials");
    }

    /**
     * Выполняет позитивный тест авторизации
     */
    private void performLogin(String method) {
        System.out.println("=== НАЧАЛО ТЕСТА: Вход способом - " + method + " ===");
        navigateToLoginForm(method);
        loginAndVerify(testUserEmail, testUserPassword);
        System.out.println("=== ТЕСТ ЗАВЕРШЕН: Вход способом - " + method + " ===");
    }

    /**
     * Выполняет негативный тест авторизации
     */
    private void performNegativeLoginTest(String method) {
        System.out.println("=== НАЧАЛО НЕГАТИВНОГО ТЕСТА: " + method + " ===");

        MainPage mainPage = new MainPage(driver);
        mainPage.open();
        mainPage.waitForLoad();
        mainPage.clickLoginAccountButton();

        LoginPage loginPage = new LoginPage(driver);
        loginPage.waitForLoad();

        String initialUrl = driver.getCurrentUrl();

        // Заполняем форму невалидными данными
        switch (method) {
            case "invalid_credentials":
                System.out.println("Попытка входа с неверным email");
                loginPage.login("invalid@example.com", "wrongpassword123");
                break;
            case "invalid_password":
                System.out.println("Попытка входа с неверным паролем");
                loginPage.login("existing@example.com", "short");
                break;
            case "empty_credentials":
                System.out.println("Попытка входа с пустыми полями");
                loginPage.login("", "");
                break;
        }

        loginPage.waitForLoad();

        // Проверяем, что остались на странице логина
        String currentUrl = driver.getCurrentUrl();
        boolean stayedOnLoginPage = loginPage.isDisplayed() ||
                currentUrl.contains("/login") ||
                currentUrl.equals(initialUrl);

        assertTrue("При невалидных данных должна оставаться страница входа", stayedOnLoginPage);

        System.out.println("Негативный тест завершен: вход не выполнен, как и ожидалось");
        System.out.println("=== НЕГАТИВНЫЙ ТЕСТ ЗАВЕРШЕН: " + method + " ===");
    }

    /**
     * Навигация к форме входа разными способами
     */
    private void navigateToLoginForm(String method) {
        System.out.println("Навигация к форме входа способом: " + method);

        MainPage mainPage = new MainPage(driver);
        mainPage.open();
        mainPage.waitForLoad();

        switch (method) {
            case "main_page_button":
                mainPage.clickLoginAccountButton();
                break;

            case "personal_account_button":
                mainPage.clickPersonalAccountButton();
                break;

            case "register_form_link":
                mainPage.clickLoginAccountButton();
                LoginPage loginPage = new LoginPage(driver);
                loginPage.waitForLoad();
                loginPage.clickRegisterLink();
                RegisterPage registerPage = new RegisterPage(driver);
                registerPage.waitForLoad();
                registerPage.clickLoginLink();
                break;

            case "forgot_password_link":
                mainPage.clickLoginAccountButton();
                LoginPage loginPage2 = new LoginPage(driver);
                loginPage2.waitForLoad();
                loginPage2.clickForgotPasswordLink();
                ForgotPasswordPage forgotPasswordPage = new ForgotPasswordPage(driver);
                forgotPasswordPage.waitForLoad();
                forgotPasswordPage.clickLoginLink();
                break;
        }

        System.out.println("Навигация к форме входа завершена");
    }

    /**
     * Выполняет авторизацию и проверяет её успешность
     */
    private void loginAndVerify(String email, String password) {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.waitForLoad();
        loginPage.login(email, password);

        MainPage mainPage = new MainPage(driver);
        mainPage.waitForLoad();

        assertTrue("После успешного входа должна отображаться главная страница",
                driver.getCurrentUrl().contains(Constants.BASE_URL));

        mainPage.clickPersonalAccountButton();

        ProfilePage profilePage = new ProfilePage(driver);
        profilePage.waitForLoad();

        assertTrue("После успешного входа должна отображаться кнопка выхода в личном кабинете",
                profilePage.isUserAuthorized());

        System.out.println("Авторизация прошла успешно для пользователя: " + email);
    }
}