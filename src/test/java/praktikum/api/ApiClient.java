package praktikum.api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import praktikum.constants.Constants;
import praktikum.utils.UserGenerator;

import static io.restassured.RestAssured.given;

/**
 * Клиент для работы с API Stellar Burgers
 * Обеспечивает создание и удаление тестовых пользователей через REST API
 * ИСПРАВЛЕНИЯ:
 * 1. Добавлены аннотации @Step для Allure отчетов
 * 2. Использована сериализация через Data класс вместо ручного JSON
 * 3. Заменены числовые коды статусов на константы HttpStatus
 * 4. Добавлен метод loginUserViaApi для получения токена при удалении пользователей
 */
public class ApiClient {

    private static final String BASE_URL = Constants.BASE_URL + "/api";

    /**
     * Data class для сериализации пользователя
     * ИСПРАВЛЕНИЕ: использование сериализации вместо ручного формирования JSON
     */
    public static class UserRegistration {
        public String email;
        public String password;
        public String name;

        public UserRegistration(String email, String password, String name) {
            this.email = email;
            this.password = password;
            this.name = name;
        }
    }

    @Step("Создание пользователя через API")
    public static String[] createUserViaApi() {
        int attempts = 0;
        int maxAttempts = 3;

        while (attempts < maxAttempts) {
            try {
                System.out.println("Попытка создания пользователя через API (" + (attempts + 1) + "/" + maxAttempts + ")");

                // ИСПРАВЛЕНИЕ: Генерация данных через отдельный класс UserGenerator
                String email = UserGenerator.generateEmail();
                String password = UserGenerator.generateValidPassword();
                String name = UserGenerator.generateName();

                // ИСПРАВЛЕНИЕ: Используем сериализацию через объект вместо ручного JSON
                UserRegistration userData = new UserRegistration(email, password, name);

                System.out.println("Отправка запроса на создание пользователя: " + email);

                Response response = given()
                        .header("Content-type", "application/json")
                        .body(userData) // Сериализация через объект
                        .when()
                        .post(BASE_URL + "/auth/register");

                System.out.println("Получен ответ от API. Статус: " + response.statusCode());

                // ИСПРАВЛЕНИЕ: Используем HttpStatus константы вместо числовых кодов
                if (response.statusCode() != HttpStatus.SC_OK) {
                    String responseBody = response.getBody().asString();
                    System.err.println("Тело ответа при ошибке: " + responseBody);
                    throw new RuntimeException("API вернул статус: " + response.statusCode() + ". Ответ: " + responseBody);
                }

                String accessToken = response.jsonPath().getString("accessToken");
                if (accessToken == null || accessToken.isEmpty()) {
                    throw new RuntimeException("Токен доступа не получен в ответе API");
                }

                // Очистка токена от возможных кавычек
                accessToken = accessToken.replace("\"", "").trim();

                System.out.println("Пользователь успешно создан: " + name + " (" + email + ")");
                System.out.println("Токен доступа получен: " + (accessToken.length() > 20 ? accessToken.substring(0, 20) + "..." : accessToken));

                return new String[]{email, password, name, accessToken};

            } catch (RuntimeException e) {
                attempts++;
                System.err.println("Попытка " + attempts + " не удалась: " + e.getMessage());

                if (attempts == maxAttempts) {
                    System.err.println("Не удалось создать пользователя после " + maxAttempts + " попыток");
                    throw new RuntimeException("Не удалось создать пользователя через API после " + maxAttempts + " попыток", e);
                }

                // Пауза перед повторной попыткой
                try {
                    System.out.println("Пауза перед повторной попыткой...");
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Поток был прерван во время ожидания", ie);
                }
            }
        }

        throw new RuntimeException("Не удалось создать пользователя после " + maxAttempts + " попыток");
    }

    @Step("Логин пользователя через API для получения токена")
    public static String loginUserViaApi(String email, String password) {
        try {
            // ИСПРАВЛЕНИЕ: Используем сериализацию через объект
            UserRegistration loginData = new UserRegistration(email, password, null);

            Response response = given()
                    .header("Content-type", "application/json")
                    .body(loginData)
                    .when()
                    .post(BASE_URL + "/auth/login");

            // ИСПРАВЛЕНИЕ: Используем HttpStatus константы
            if (response.statusCode() == HttpStatus.SC_OK) {
                String accessToken = response.jsonPath().getString("accessToken");
                if (accessToken != null && !accessToken.isEmpty()) {
                    return accessToken.replace("\"", "").trim();
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка при логине пользователя через API: " + e.getMessage());
        }
        return null;
    }

    @Step("Удаление пользователя через API")
    public static void deleteUserViaApi(String accessToken) {
        if (accessToken == null || accessToken.isEmpty()) {
            System.err.println("Не передан токен для удаления пользователя");
            return;
        }

        try {
            System.out.println("Отправка запроса на удаление пользователя...");

            Response response = given()
                    .header("Authorization", accessToken)
                    .when()
                    .delete(BASE_URL + "/auth/user");

            int statusCode = response.statusCode();
            System.out.println("Статус ответа при удалении: " + statusCode);

            // ИСПРАВЛЕНИЕ: Используем HttpStatus константы вместо числовых кодов
            if (statusCode == HttpStatus.SC_ACCEPTED || statusCode == HttpStatus.SC_OK) {
                System.out.println("Пользователь успешно удален через API");
            } else {
                System.err.println("Предупреждение: удаление пользователя вернуло статус: " + statusCode);
                if (statusCode == HttpStatus.SC_UNAUTHORIZED) {
                    System.err.println("Токен устарел или невалиден");
                } else if (statusCode == HttpStatus.SC_FORBIDDEN) {
                    System.err.println("Нет прав для удаления пользователя");
                } else if (statusCode == HttpStatus.SC_NOT_FOUND) {
                    System.err.println("Пользователь не найден (уже удален?)");
                } else {
                    System.err.println("Неожиданный статус при удалении: " + statusCode);
                }
            }

        } catch (Exception e) {
            System.err.println("Ошибка при удалении пользователя через API: " + e.getMessage());
        }
    }
}