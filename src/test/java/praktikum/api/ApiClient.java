package praktikum.api;

import io.restassured.response.Response;
import org.json.JSONObject;
import praktikum.constants.Constants;
import praktikum.utils.UserGenerator;

import static io.restassured.RestAssured.given;

/**
 * Клиент для работы с API Stellar Burgers
 * Обеспечивает создание и удаление тестовых пользователей через REST API
 */
public class ApiClient {

    private static final String BASE_URL = Constants.BASE_URL + "/api";

    /**
     * Создает нового пользователя через API с повторными попытками при ошибках
     * Генерирует уникальные тестовые данные для каждого вызова
     *
     * @return Массив с данными пользователя [email, password, name, accessToken]
     * @throws RuntimeException если не удалось создать пользователя после 3 попыток
     */
    public static String[] createUserViaApi() {
        int attempts = 0;
        int maxAttempts = 3;

        while (attempts < maxAttempts) {
            try {
                System.out.println("Попытка создания пользователя через API (" + (attempts + 1) + "/" + maxAttempts + ")");

                String email = UserGenerator.generateEmail();
                String password = UserGenerator.generateValidPassword();
                String name = UserGenerator.generateName();

                // Используем JSONObject для безопасного формирования JSON
                JSONObject requestBody = new JSONObject();
                requestBody.put("email", email);
                requestBody.put("password", password);
                requestBody.put("name", name);

                System.out.println("Отправка запроса на создание пользователя: " + email);

                Response response = given()
                        .header("Content-type", "application/json")
                        .body(requestBody.toString())
                        .when()
                        .post(BASE_URL + "/auth/register");

                System.out.println("Получен ответ от API. Статус: " + response.statusCode());

                // Проверка статуса диапазон 2xx
                if (response.statusCode() < 200 || response.statusCode() >= 300) {
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

    /**
     * Удаляет пользователя через API используя токен доступа
     * Обрабатывает ошибки удаления без прерывания выполнения тестов
     *
     * @param accessToken Токен доступа пользователя для удаления
     */
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

            // Успешные статусы для удаления
            if (statusCode == 202 || statusCode == 200) {
                System.out.println("Пользователь успешно удален через API");
            } else {
                System.err.println("Предупреждение: удаление пользователя вернуло статус: " + statusCode);
                if (statusCode == 401) {
                    System.err.println("Токен устарел или невалиден");
                } else if (statusCode == 403) {
                    System.err.println("Нет прав для удаления пользователя");
                } else if (statusCode == 404) {
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