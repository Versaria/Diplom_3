package praktikum.tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import praktikum.pages.LoginPage;
import praktikum.pages.MainPage;
import praktikum.pages.RegistrationPage;
import praktikum.utils.DataGenerator;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Тесты для функциональности регистрации
 */
public class RegistrationTest extends BaseTest {

    /**
     * Тест проверяет успешную регистрацию нового пользователя
     */
    @Test
    @DisplayName("Успешная регистрация")
    @Description("Проверка регистрации с валидными данными")
    public void testSuccessfulRegistration() {
        try {
            // Arrange: генерируем тестовые данные
            String name = DataGenerator.generateName();
            String email = DataGenerator.generateEmail();
            String password = DataGenerator.generatePassword(8);

            System.out.println("Регистрация пользователя:");
            System.out.println("Имя: " + name);
            System.out.println("Email: " + email);

            // Act: выполняем регистрацию
            MainPage mainPage = new MainPage(driver);
            mainPage.open();
            mainPage.waitForLoad();
            mainPage.clickLoginButton();

            LoginPage loginPage = new LoginPage(driver);
            loginPage.waitForLoad();
            loginPage.clickRegisterLink();

            RegistrationPage registrationPage = new RegistrationPage(driver);
            registrationPage.waitForLoad();
            registrationPage.register(name, email, password);

            // Assert: проверяем, что произошел переход на страницу логина
            LoginPage loginPageAfterRegistration = new LoginPage(driver);
            loginPageAfterRegistration.waitForLoad();
            assertTrue("После регистрации должна отображаться страница входа",
                    loginPageAfterRegistration.isDisplayed());

            System.out.println("Регистрация прошла успешно!");

        } catch (Exception e) {
            System.out.println("Ошибка при регистрации: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Тест проверяет ошибку при вводе некорректного пароля (менее 6 символов)
     */
    @Test
    @DisplayName("Ошибка при некорректном пароле")
    @Description("Проверка отображения ошибки при пароле менее 6 символов")
    public void testRegistrationWithShortPassword() {
        try {
            // Arrange: генерируем тестовые данные с коротким паролем
            String name = DataGenerator.generateName();
            String email = DataGenerator.generateEmail();
            String password = DataGenerator.generateShortPassword();

            System.out.println("Попытка регистрации с коротким паролем:");
            System.out.println("Пароль: " + password + " (длина: " + password.length() + ")");

            // Act: выполняем регистрацию с коротким паролем
            MainPage mainPage = new MainPage(driver);
            mainPage.open();
            mainPage.waitForLoad();
            mainPage.clickLoginButton();

            LoginPage loginPage = new LoginPage(driver);
            loginPage.waitForLoad();
            loginPage.clickRegisterLink();

            RegistrationPage registrationPage = new RegistrationPage(driver);
            registrationPage.waitForLoad();
            registrationPage.register(name, email, password);

            // Ждем появления ошибки
            Thread.sleep(2000); // Даем время для отображения ошибки

            // Assert: проверяем сообщение об ошибке
            String errorMessage = registrationPage.getErrorMessage();
            System.out.println("Сообщение об ошибке: " + errorMessage);

            assertTrue("Должно отображаться сообщение об ошибке пароля",
                    errorMessage != null && !errorMessage.isEmpty());

        } catch (Exception e) {
            System.out.println("Ошибка при тестировании короткого пароля: " + e.getMessage());
            throw e;
        }
    }
}