package praktikum.utils;

import com.github.javafaker.Faker;
import java.util.Locale;
import java.util.Random;

/**
 * Утилитарный класс для генерации тестовых данных пользователей
 */
public class UserGenerator {
    private static final Faker faker = new Faker(new Locale("ru"));
    private static final Random random = new Random();
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static String generateName() {
        return faker.name().fullName();
    }

    public static String generateEmail() {
        return faker.internet().emailAddress();
    }

    /**
     * Генерирует пароль заданной длины вручную, чтобы избежать проблем с Faker
     */
    public static String generatePassword(int length) {
        if (length <= 0) {
            length = 1;
        }

        StringBuilder password = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            password.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return password.toString();
    }

    /**
     * Генерирует специально короткий пароль для тестирования валидации
     * ИСПРАВЛЕНИЕ: Генерируем пароль длиной 5 символов (меньше минимальных 6)
     */
    public static String generateShortPassword() {
        return generatePassword(5); // Явно указываем 5 символов
    }

    /**
     * Генерирует валидный пароль, соответствующий требованиям приложения
     * ИСПРАВЛЕНИЕ: Генерируем пароль длиной 8 символов (больше минимальных 6)
     */
    public static String generateValidPassword() {
        return generatePassword(8); // Явно указываем 8 символов
    }
}