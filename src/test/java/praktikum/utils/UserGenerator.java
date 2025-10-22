package praktikum.utils;

import praktikum.constants.Constants;
import java.util.Random;

/**
 * Утилитарный класс для генерации тестовых данных пользователей
 * Обеспечивает создание уникальных и валидных данных для тестов регистрации и авторизации
 * Все методы статические - не требуют создания экземпляра класса
 */
public class UserGenerator {
    // Генератор случайных чисел для создания разнообразных тестовых данных
    private static final Random random = new Random();

    // Базы данных для генерации реалистичных имен пользователей
    private static final String[] FIRST_NAMES = {"Анна", "Иван", "Мария", "Петр", "Ольга", "Сергей", "Елена", "Алексей"};
    private static final String[] LAST_NAMES = {"Иванова", "Петров", "Сидорова", "Кузнецов", "Смирнова", "Попов", "Васильева", "Федоров"};

    /**
     * Генерирует случайное полное имя пользователя в формате "Имя Фамилия"
     * Использует предопределенные массивы имен и фамилий для реалистичности
     *
     * @return Случайное полное имя пользователя
     */
    public static String generateName() {
        String firstName = FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
        String lastName = LAST_NAMES[random.nextInt(LAST_NAMES.length)];
        return firstName + " " + lastName;
    }

    /**
     * Генерирует уникальный email адрес на основе текущего времени
     * Гарантирует уникальность за счет использования временной метки
     * Формат: testuser{timestamp}@example.com
     *
     * @return Уникальный email адрес для тестирования
     */
    public static String generateEmail() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        return "testuser" + timestamp + "@example.com";
    }

    /**
     * Генерирует пароль заданной длины из буквенно-цифровых символов
     * Использует безопасный набор символов: A-Z, a-z, 0-9
     *
     * @param length Требуемая длина пароля
     * @return Случайный пароль указанной длины
     */
    public static String generatePassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < length; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        return password.toString();
    }

    /**
     * Генерирует специально короткий пароль для тестирования валидации
     * Длина пароля всегда на 1 символ меньше минимально допустимой
     * Используется в негативных тестах для проверки обработки ошибок
     *
     * @return Короткий пароль, который должен вызвать ошибку валидации
     */
    public static String generateShortPassword() {
        // Генерируем пароль на 1 символ меньше минимальной длины
        return generatePassword(Constants.MIN_PASSWORD_LENGTH - 1);
    }

    /**
     * Генерирует валидный пароль, соответствующий требованиям приложения
     * Длина пароля равна или превышает минимально допустимую
     * Используется в позитивных тестах регистрации и авторизации
     *
     * @return Валидный пароль, подходящий для успешной регистрации
     */
    public static String generateValidPassword() {
        // Генерируем пароль минимальной длины или немного длиннее (до +2 символов)
        return generatePassword(Constants.MIN_PASSWORD_LENGTH + random.nextInt(3));
    }
}