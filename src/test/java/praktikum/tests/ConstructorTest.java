package praktikum.tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import praktikum.pages.ConstructorPage;
import praktikum.pages.MainPage;
import praktikum.constants.Constants;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Тестовый класс для проверки функциональности конструктора бургеров
 * ИСПРАВЛЕНИЯ:
 * 1. Убрана избыточная параметризация - заменена на отдельные тесты
 * 2. Упрощена структура тестов для лучшей поддерживаемости
 */
public class ConstructorTest extends BaseTest {

    /**
     * Тест перехода к разделу 'Булки'
     * ИСПРАВЛЕНИЕ: Отдельный тест вместо параметризованного
     */
    @Test
    @DisplayName("Переход к разделу 'Булки'")
    @Description("Проверка переключения на раздел булок с проверкой скролла")
    public void testBunSectionNavigation() {
        System.out.println("=== НАЧАЛО ТЕСТА: Переход к разделу 'Булки' ===");

        MainPage mainPage = new MainPage(driver);
        mainPage.open();
        mainPage.waitForLoad();

        ConstructorPage constructorPage = new ConstructorPage(driver);
        constructorPage.waitForLoad();

        // Проверяем начальное состояние
        String initialSection = constructorPage.getActiveSectionText();
        assertEquals("Начальное состояние конструктора должно быть 'Булки'",
                Constants.BUNS_SECTION_TEXT, initialSection);

        // Переход к соусам и обратно к булкам
        constructorPage.clickSauceSection();
        constructorPage.clickBunSection();

        // Проверка активной секции
        String activeSection = constructorPage.getActiveSectionText();
        assertEquals("После переключения должен быть активен раздел 'Булки'",
                Constants.BUNS_SECTION_TEXT, activeSection);

        // Проверка скролла
        assertTrue("Должен произойти скролл к разделу 'Булки'",
                constructorPage.isBunsSectionScrolledIntoView());

        System.out.println("=== ТЕСТ ЗАВЕРШЕН: Переход к разделу 'Булки' ===");
    }

    /**
     * Тест перехода к разделу 'Соусы'
     * ИСПРАВЛЕНИЕ: Отдельный тест вместо параметризованного
     */
    @Test
    @DisplayName("Переход к разделу 'Соусы'")
    @Description("Проверка переключения на раздел соусов с проверкой скролла")
    public void testSauceSectionNavigation() {
        System.out.println("=== НАЧАЛО ТЕСТА: Переход к разделу 'Соусы' ===");

        MainPage mainPage = new MainPage(driver);
        mainPage.open();
        mainPage.waitForLoad();

        ConstructorPage constructorPage = new ConstructorPage(driver);
        constructorPage.waitForLoad();

        // Активация секции соусов
        constructorPage.clickSauceSection();

        // Проверка активной секции
        String activeSection = constructorPage.getActiveSectionText();
        assertEquals("После переключения должен быть активен раздел 'Соусы'",
                Constants.SAUCES_SECTION_TEXT, activeSection);

        // Проверка скролла
        assertTrue("Должен произойти скролл к разделу 'Соусы'",
                constructorPage.isSaucesSectionScrolledIntoView());

        System.out.println("=== ТЕСТ ЗАВЕРШЕН: Переход к разделу 'Соусы' ===");
    }

    /**
     * Тест перехода к разделу 'Начинки'
     * ИСПРАВЛЕНИЕ: Отдельный тест вместо параметризованного
     */
    @Test
    @DisplayName("Переход к разделу 'Начинки'")
    @Description("Проверка переключения на раздел начинок с проверкой скролла")
    public void testFillingSectionNavigation() {
        System.out.println("=== НАЧАЛО ТЕСТА: Переход к разделу 'Начинки' ===");

        MainPage mainPage = new MainPage(driver);
        mainPage.open();
        mainPage.waitForLoad();

        ConstructorPage constructorPage = new ConstructorPage(driver);
        constructorPage.waitForLoad();

        // Активация секции начинок
        constructorPage.clickFillingSection();

        // Проверка активной секции
        String activeSection = constructorPage.getActiveSectionText();
        assertEquals("После переключения должен быть активен раздел 'Начинки'",
                Constants.FILLINGS_SECTION_TEXT, activeSection);

        // Проверка скролла
        assertTrue("Должен произойти скролл к разделу 'Начинки'",
                constructorPage.isFillingsSectionScrolledIntoView());

        System.out.println("=== ТЕСТ ЗАВЕРШEN: Переход к разделу 'Начинки' ===");
    }

    /**
     * Дополнительный тест для проверки последовательного переключения всех разделов
     */
    @Test
    @DisplayName("Последовательное переключение всех разделов конструктора")
    @Description("Проверка последовательного переключения между всеми разделами конструктора с проверкой скролла")
    public void testSequentialSectionNavigation() {
        System.out.println("=== НАЧАЛО ТЕСТА: Последовательное переключение разделов конструктора ===");

        MainPage mainPage = new MainPage(driver);
        mainPage.open();
        mainPage.waitForLoad();

        ConstructorPage constructorPage = new ConstructorPage(driver);
        constructorPage.waitForLoad();

        // 1. Переход к Соусам
        System.out.println("1. Переход к разделу 'Соусы'");
        constructorPage.clickSauceSection();

        assertEquals("Должны быть активны 'Соусы' после переключения",
                Constants.SAUCES_SECTION_TEXT, constructorPage.getActiveSectionText());
        assertTrue("Должен произойти скролл к разделу 'Соусы'",
                constructorPage.isSaucesSectionScrolledIntoView());
        System.out.println("✓ Переход к 'Соусам' выполнен успешно");

        // 2. Переход к Начинкам
        System.out.println("2. Переход к разделу 'Начинки'");
        constructorPage.clickFillingSection();

        assertEquals("Должны быть активны 'Начинки' после переключения",
                Constants.FILLINGS_SECTION_TEXT, constructorPage.getActiveSectionText());
        assertTrue("Должен произойти скролл к разделу 'Начинки'",
                constructorPage.isFillingsSectionScrolledIntoView());
        System.out.println("✓ Переход к 'Начинкам' выполнен успешно");

        // 3. Возврат к Букам
        System.out.println("3. Возврат к разделу 'Булки'");
        constructorPage.clickBunSection();

        assertEquals("Должны быть активны 'Булки' после переключения",
                Constants.BUNS_SECTION_TEXT, constructorPage.getActiveSectionText());
        assertTrue("Должен произойти скролл к разделу 'Булки'",
                constructorPage.isBunsSectionScrolledIntoView());
        System.out.println("✓ Возврат к 'Булкам' выполнен успешно");

        System.out.println("=== ТЕСТ ЗАВЕРШЕН: Последовательное переключение разделов конструктора ===");
    }
}