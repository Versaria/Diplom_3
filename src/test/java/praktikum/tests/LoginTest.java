package praktikum.tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import praktikum.api.ApiClient;
import praktikum.pages.*;
import praktikum.constants.Constants;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertTrue;

/**
 * Тестовый класс для проверки различных сценариев авторизации пользователя с параметризацией
 * Включает как позитивные тесты входа разными способами, так и негативные тесты с неверными данными
 */
@RunWith(Parameterized.class)
public class LoginTest extends BaseTest {

    // Данные тестового пользователя для авторизации
    private String testUserEmail;
    private String testUserPassword;
    private String testUserName;
    private String testUserToken;

    private final String loginMethod;
    private final String testDescription;
    private final boolean isPositiveTest;

    /**
     * Конструктор для параметризованных тестов
     *
     * @param loginMethod Способ входа для тестирования
     * @param testDescription Описание тестового сценария
     * @param isPositiveTest Флаг позитивного/негативного теста
     */
    public LoginTest(String loginMethod, String testDescription, boolean isPositiveTest) {
        this.loginMethod = loginMethod;
        this.testDescription = testDescription;
        this.isPositiveTest = isPositiveTest;
    }

    /**
     * Параметры для тестов входа - разные способы аутентификации и негативные сценарии
     */
    @Parameterized.Parameters(name = "{1}")
    public static Collection<Object[]> getLoginMethods() {
        return Arrays.asList(new Object[][]{
                {"main_page_button", "Вход через кнопку 'Войти в аккаунт' на главной странице", true},
                {"personal_account_button", "Вход через кнопку 'Личный Кабинет'", true},
                {"register_form_link", "Вход через ссылку в форме регистрации", true},
                {"forgot_password_link", "Вход через ссылку в форме восстановления пароля", true},
                {"invalid_credentials", "Ошибка входа с неверным email", false},
                {"invalid_password", "Ошибка входа с неверным паролем", false},
                {"empty_credentials", "Ошибка входа с пустыми полями", false}
        });
    }

    /**
     * Создает тестового пользователя через API перед каждым тестом
     * Для негативных тестов пользователь не создается
     */
    @Before
    public void setUp() {
        super.setUp();

        // Создаем пользователя только для позитивных тестов
        if (isPositiveTest) {
            System.out.println("Создание тестового пользователя через API...");
            String[] userData = ApiClient.createUserViaApi();
            testUserEmail = userData[0];
            testUserPassword = userData[1];
            testUserName = userData[2];
            testUserToken = userData[3];

            System.out.println("Создан тестовый пользователь: " + testUserName + " (" + testUserEmail + ")");
        } else {
            System.out.println("Негативный тест - пользователь не создается");
        }
    }

    /**
     * Удаляет тестового пользователя через API после каждого теста
     * Для негативных тестов удаление не требуется
     */
    @After
    public void tearDown() {
        if (testUserToken != null && isPositiveTest) {
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

    /**
     * Параметризованный тест входа разными способами и негативные тесты
     */
    @Test
    @DisplayName("Тесты авторизации")
    @Description("Проверка входа различными способами и обработки ошибок: {testDescription}")
    public void testParameterizedLogin() {
        System.out.println("=== НАЧАЛО ТЕСТА: " + testDescription + " ===");
        System.out.println("Метод: " + loginMethod);
        System.out.println("Тип: " + (isPositiveTest ? "ПОЗИТИВНЫЙ" : "НЕГАТИВНЫЙ"));

        if (isPositiveTest) {
            // Выполнение позитивного теста с валидными данными
            performPositiveLoginTest();
        } else {
            // Выполнение негативного теста с невалидными данными
            performNegativeLoginTest();
        }

        System.out.println("=== ТЕСТ ЗАВЕРШЕН: " + testDescription + " ===");
    }

    /**
     * Выполняет позитивный тест авторизации с валидными данными пользователя
     * Проверяет успешный вход и переход в личный кабинет
     */
    private void performPositiveLoginTest() {
        // Навигация к форме входа в зависимости от способа
        navigateToLoginForm(loginMethod);

        // Выполнение входа и проверка успешной авторизации
        loginAndVerify(testUserEmail, testUserPassword);
    }

    /**
     * Выполняет негативный тест авторизации с невалидными данными
     * Проверяет, что при неверных данных пользователь остается на странице входа
     */
    private void performNegativeLoginTest() {
        // Открываем главную страницу и переходим к форме входа
        MainPage mainPage = new MainPage(driver);
        mainPage.open();
        mainPage.waitForLoad();
        mainPage.clickLoginAccountButton();

        LoginPage loginPage = new LoginPage(driver);
        loginPage.waitForLoad();

        // Сохраняем начальный URL для проверки отсутствия редиректа
        String initialUrl = driver.getCurrentUrl();

        // Заполняем форму невалидными данными в зависимости от типа теста
        switch (loginMethod) {
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

        // Используем явное ожидание вместо Thread.sleep
        loginPage.waitForLoad();

        // Проверяем, что остались на странице логина (не произошел вход)
        String currentUrl = driver.getCurrentUrl();
        boolean stayedOnLoginPage = loginPage.isDisplayed() ||
                currentUrl.contains("/login") ||
                currentUrl.equals(initialUrl);

        assertTrue("При невалидных данных должна оставаться страница входа", stayedOnLoginPage);

        System.out.println("Негативный тест завершен: вход не выполнен, как и ожидалось");
    }

    /**
     * Навигация к форме входа разными способами для позитивных тестов
     *
     * @param method Способ навигации к форме входа
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
     * Выполняет авторизацию пользователя и проверяет её успешность
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

        assertTrue("После успешного входа должна отображаться кнопка выхода в личном кабинете",
                profilePage.isUserAuthorized());

        System.out.println("Авторизация прошла успешно для пользователя: " + email);
    }
}