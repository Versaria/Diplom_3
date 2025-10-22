package praktikum.tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.api.ApiClient;
import praktikum.pages.LoginPage;
import praktikum.pages.MainPage;
import praktikum.pages.ProfilePage;

import static org.junit.Assert.assertTrue;

/**
 * Тестовый класс для проверки функциональности выхода из аккаунта
 * Проверяет корректность завершения сессии и возврата на страницу авторизации
 * Демонстрирует полный цикл аутентификации: вход -> проверка -> выход
 */
public class LogoutTest extends BaseTest {

    // Данные тестового пользователя для авторизации
    private String testUserEmail;
    private String testUserPassword;
    private String testUserName;
    private String testUserToken;

    /**
     * Создает тестового пользователя через API перед каждым тестом
     * Пользователь необходим для проверки процесса входа и выхода
     */
    @Before
    public void setUp() {
        super.setUp();

        System.out.println("=== ПОДГОТОВКА ТЕСТА ВЫХОДА ИЗ АККАУНТА ===");
        System.out.println("Создание тестового пользователя через API...");

        String[] userData = ApiClient.createUserViaApi();
        testUserEmail = userData[0];
        testUserPassword = userData[1];
        testUserName = userData[2];
        testUserToken = userData[3];

        System.out.println("Создан тестовый пользователь: " + testUserName + " (" + testUserEmail + ")");
        System.out.println("===========================================");
    }

    /**
     * Удаляет тестового пользователя через API после каждого теста
     * Обеспечивает чистоту тестового окружения
     */
    @After
    public void tearDown() {
        if (testUserToken != null) {
            try {
                System.out.println("=== ЗАВЕРШЕНИЕ ТЕСТА ВЫХОДА ИЗ АККАУНТА ===");
                System.out.println("Удаление тестового пользователя через API...");
                ApiClient.deleteUserViaApi(testUserToken);
                System.out.println("Удален тестовый пользователь: " + testUserName);
                System.out.println("=============================================");
            } catch (Exception e) {
                System.err.println("Ошибка при удалении пользователя: " + e.getMessage());
            }
        }
        super.tearDown();
    }

    /**
     * Тест корректного выхода из аккаунта авторизованного пользователя
     * Проверяет полный цикл: вход -> проверка авторизации -> выход -> проверка деавторизации
     * Сценарий:
     * 1. Открыть главную страницу
     * 2. Выполнить вход в аккаунт
     * 3. Перейти в личный кабинет
     * 4. Выполнить выход из аккаунта
     * 5. Проверить, что произошел возврат на страницу входа
     */
    @Test
    @DisplayName("Выход из аккаунта")
    @Description("Проверка корректного завершения сессии и возврата на страницу авторизации")
    public void testLogout() {
        System.out.println("=== НАЧАЛО ТЕСТА: Выход из аккаунта ===");

        // Шаг 1: Открытие главной страницы
        System.out.println("Шаг 1: Открытие главной страницы");
        MainPage mainPage = new MainPage(driver);
        mainPage.open();
        mainPage.waitForLoad();
        System.out.println("Главная страница успешно загружена");

        // Шаг 2: Переход на страницу авторизации и вход в аккаунт
        System.out.println("Шаг 2: Выполнение входа в аккаунт");
        mainPage.clickLoginAccountButton();

        LoginPage loginPage = new LoginPage(driver);
        loginPage.waitForLoad();
        System.out.println("Страница авторизации загружена");

        loginPage.login(testUserEmail, testUserPassword);
        System.out.println("Форма авторизации заполнена и отправлена");

        // Проверка успешного входа - ожидание загрузки главной страницы после авторизации
        mainPage.waitForLoad();
        System.out.println("Успешный вход в аккаунт пользователя: " + testUserEmail);

        // Шаг 3: Переход в личный кабинет для подтверждения авторизации
        System.out.println("Шаг 3: Переход в личный кабинет");
        mainPage.clickPersonalAccountButton();

        ProfilePage profilePage = new ProfilePage(driver);
        profilePage.waitForLoad();
        System.out.println("Страница личного кабинета загружена");

        // Проверка, что пользователь авторизован (отображается кнопка выхода)
        boolean isAuthorized = profilePage.isUserAuthorized();
        assertTrue("Пользователь должен быть авторизован перед выходом", isAuthorized);
        System.out.println("Подтверждена авторизация пользователя");

        // Шаг 4: Выполнение выхода из аккаунта через метод ProfilePage
        System.out.println("Шаг 4: Выполнение выхода из аккаунта");
        profilePage.clickLogoutButton();
        System.out.println("Кнопка выхода нажата");

        // Шаг 5: Проверка, что произошел выход и отображается страница входа
        System.out.println("Шаг 5: Проверка результата выхода");

        // Ожидание загрузки страницы входа после выхода
        LoginPage loginPageAfterLogout = new LoginPage(driver);
        loginPageAfterLogout.waitForLoad();

        // ОДНА ОСНОВНАЯ ПРОВЕРКА - отображение страницы входа
        assertTrue("После выхода из аккаунта должна отображаться страница входа",
                loginPageAfterLogout.isDisplayed());

        System.out.println("Подтвержден переход на страницу авторизации после выхода");
        System.out.println("=== ТЕСТ ЗАВЕРШЕН: Выход из аккаунта ===");
        System.out.println("Результат: Пользователь успешно вышел из аккаунта");
    }

    /**
     * Тест выхода из аккаунта через альтернативный сценарий (вход через личный кабинет)
     * Проверяет, что функциональность выхода работает независимо от способа входа
     * Демонстрирует переиспользование компонента хедера для навигации
     */
    @Test
    @DisplayName("Выход из аккаунта при входе через личный кабинет")
    @Description("Проверка выхода при альтернативном сценарии входа через кнопку 'Личный кабинет'")
    public void testLogoutAfterPersonalAccountLogin() {
        System.out.println("=== НАЧАЛО ТЕСТА: Выход из аккаунта при входе через личный кабинет ===");

        // Шаг 1: Открытие главной страницы
        System.out.println("Шаг 1: Открытие главной страницы");
        MainPage mainPage = new MainPage(driver);
        mainPage.open();
        mainPage.waitForLoad();

        // Шаг 2: Вход через кнопку "Личный кабинет" (альтернативный способ)
        System.out.println("Шаг 2: Вход через кнопку 'Личный кабинет'");
        mainPage.clickPersonalAccountButton();

        LoginPage loginPage = new LoginPage(driver);
        loginPage.waitForLoad();
        loginPage.login(testUserEmail, testUserPassword);
        System.out.println("Вход выполнен через личный кабинет");

        // Шаг 3: Повторный переход в личный кабинет (после авторизации)
        mainPage.waitForLoad();
        mainPage.clickPersonalAccountButton();

        ProfilePage profilePage = new ProfilePage(driver);
        profilePage.waitForLoad();

        // Проверка авторизации
        boolean isAuthorized = profilePage.isUserAuthorized();
        assertTrue("Пользователь должен быть авторизован", isAuthorized);
        System.out.println("Авторизация подтверждена при альтернативном входе");

        // Шаг 4: Выход из аккаунта через метод ProfilePage
        System.out.println("Шаг 4: Выполнение выхода");
        profilePage.clickLogoutButton();

        // Шаг 5: Проверка возврата на страницу входа через методы Page Object
        LoginPage loginPageAfterLogout = new LoginPage(driver);
        loginPageAfterLogout.waitForLoad();

        // Отображение страницы входа
        assertTrue("После выхода должна отображаться страница входа",
                loginPageAfterLogout.isDisplayed());

        System.out.println("Выход выполнен успешно при альтернативном сценарии входа");
        System.out.println("=== ТЕСТ ЗАВЕРШЕН: Выход из аккаунта при входе через личный кабинет ===");
    }
}