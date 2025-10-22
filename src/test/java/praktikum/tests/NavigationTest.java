package praktikum.tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.api.ApiClient;
import praktikum.pages.ConstructorPage;
import praktikum.pages.LoginPage;
import praktikum.pages.MainPage;
import praktikum.pages.ProfilePage;

import static org.junit.Assert.assertTrue;

/**
 * Тестовый класс для проверки навигации между разделами приложения
 * Проверяет переходы из личного кабинета в конструктор и обратно
 */
public class NavigationTest extends BaseTest {

    private String testUserEmail;
    private String testUserPassword;
    private String testUserName;
    private String testUserToken;

    /**
     * Создает тестового пользователя перед каждым тестом
     */
    @Before
    public void setUp() {
        super.setUp();
        System.out.println("Создание тестового пользователя для навигационных тестов...");
        String[] userData = ApiClient.createUserViaApi();
        testUserEmail = userData[0];
        testUserPassword = userData[1];
        testUserName = userData[2];
        testUserToken = userData[3];
        System.out.println("Создан пользователь для тестов навигации: " + testUserName);
    }

    /**
     * Удаляет тестового пользователя после каждого теста
     */
    @After
    public void tearDown() {
        if (testUserToken != null) {
            try {
                System.out.println("Удаление тестового пользователя после навигационных тестов...");
                ApiClient.deleteUserViaApi(testUserToken);
                System.out.println("Пользователь удален: " + testUserName);
            } catch (Exception e) {
                System.err.println("Ошибка при удалении пользователя: " + e.getMessage());
            }
        }
        super.tearDown();
    }

    /**
     * Тест перехода из Личного кабинета в Конструктор через логотип
     * Проверяет корректность навигации между основными разделами приложения
     */
    @Test
    @DisplayName("Переход из Личного кабинета в Конструктор через логотип")
    @Description("Проверка навигации из личного кабинета обратно в конструктор по клику на логотип")
    public void testNavigationFromProfileToConstructorViaLogo() {
        System.out.println("=== НАЧАЛО ТЕСТА: Переход из Личного кабинета в Конструктор через логотип ===");

        // Шаг 1: Открытие главной страницы и авторизация
        System.out.println("Шаг 1: Открытие главной страницы и авторизация");
        MainPage mainPage = new MainPage(driver);
        mainPage.open();
        mainPage.waitForLoad();

        // Шаг 2: Логин пользователя
        System.out.println("Шаг 2: Логин пользователя");
        mainPage.clickLoginAccountButton();
        LoginPage loginPage = new LoginPage(driver);
        loginPage.waitForLoad();
        loginPage.login(testUserEmail, testUserPassword);
        System.out.println("Пользователь авторизован: " + testUserEmail);

        // Шаг 3: Переход в личный кабинет
        System.out.println("Шаг 3: Переход в личный кабинет");
        mainPage.waitForLoad();
        mainPage.clickPersonalAccountButton();

        ProfilePage profilePage = new ProfilePage(driver);
        profilePage.waitForLoad();

        // Проверяем, что находимся в личном кабинете
        assertTrue("Должны находиться в личном кабинете после авторизации",
                profilePage.isUserAuthorized());
        System.out.println("Успешный переход в личный кабинет");

        // Шаг 4: Клик по логотипу для возврата в конструктор
        System.out.println("Шаг 4: Возврат в конструктор через логотип");
        mainPage.clickLogo();
        System.out.println("Выполнен клик по логотипу для возврата в конструктор");

        // Шаг 5: Проверка, что вернулись в конструктор через методы Page Object
        System.out.println("Шаг 5: Проверка возврата в конструктор");
        ConstructorPage constructorPage = new ConstructorPage(driver);
        constructorPage.waitForLoad();

        // Проверяем URL через метод Page Object - ЭТОЙ ПРОВЕРКИ ДОСТАТОЧНО
        boolean isOnConstructorPage = mainPage.isMainPageUrl();
        assertTrue("После клика по логотипу должна отображаться главная страница с конструктором",
                isOnConstructorPage);

        System.out.println("Успешный возврат в конструктор");
        System.out.println("=== ТЕСТ ЗАВЕРШЕН: Переход из Личного кабинета в Конструктор через логотип ===");
    }

    /**
     * Тест перехода из Личного кабинета в Конструктор через кнопку "Конструктор"
     * Проверяет альтернативный способ навигации между разделами
     */
    @Test
    @DisplayName("Переход из Личного кабинета в Конструктор через кнопку")
    @Description("Проверка навигации из личного кабинета в конструктор по клику на кнопку 'Конструктор'")
    public void testNavigationFromProfileToConstructorViaButton() {
        System.out.println("=== НАЧАЛО ТЕСТА: Переход из Личного кабинета в Конструктор через кнопку ===");

        // Шаг 1: Открытие главной страницы и авторизация
        System.out.println("Шаг 1: Открытие главной страницы и авторизация");
        MainPage mainPage = new MainPage(driver);
        mainPage.open();
        mainPage.waitForLoad();

        // Шаг 2: Логин пользователя
        System.out.println("Шаг 2: Логин пользователя");
        mainPage.clickLoginAccountButton();
        LoginPage loginPage = new LoginPage(driver);
        loginPage.waitForLoad();
        loginPage.login(testUserEmail, testUserPassword);
        System.out.println("Пользователь авторизован: " + testUserEmail);

        // Шаг 3: Переход в личный кабинет
        System.out.println("Шаг 3: Переход в личный кабинет");
        mainPage.waitForLoad();
        mainPage.clickPersonalAccountButton();

        ProfilePage profilePage = new ProfilePage(driver);
        profilePage.waitForLoad();

        // Проверяем, что находимся в личном кабинете
        assertTrue("Должны находиться в личном кабинете после авторизации",
                profilePage.isUserAuthorized());
        System.out.println("Успешный переход в личный кабинет");

        // Шаг 4: Клик по кнопке "Конструктор"
        System.out.println("Шаг 4: Возврат в конструктор через кнопку");
        mainPage.clickConstructorButton();
        System.out.println("Выполнен клик по кнопке 'Конструктор'");

        // Шаг 5: Проверка, что вернулись в конструктор через методы Page Object
        System.out.println("Шаг 5: Проверка возврата в конструктор");
        ConstructorPage constructorPage = new ConstructorPage(driver);
        constructorPage.waitForLoad();

        // Проверяем URL через метод Page Object
        boolean isOnConstructorPage = mainPage.isMainPageUrl();
        assertTrue("После клика по кнопке 'Конструктор' должна отображаться главная страница",
                isOnConstructorPage);

        System.out.println("Успешный возврат в конструктор через кнопку");
        System.out.println("=== ТЕСТ ЗАВЕРШЕН: Переход из Личного кабинета в Конструктор через кнопку ===");
    }
}