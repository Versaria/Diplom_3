package praktikum.tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import praktikum.pages.ConstructorPage;
import praktikum.pages.MainPage;
import praktikum.constants.Constants;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Тестовый класс для проверки функциональности конструктора бургеров с параметризацией
 * Проверяет переключение между разделами конструктора и корректность отображения активной секции
 * А также проверяет скролл к соответствующим группам ингредиентов
 */
@RunWith(Parameterized.class)
public class ConstructorTest extends BaseTest {

    private final String sectionName;
    private final String expectedText;
    private final String methodName;
    private final String scrollCheckMethod;

    /**
     * Конструктор для параметризованных тестов конструктора
     *
     * @param sectionName Название раздела для тестирования
     * @param expectedText Ожидаемый текст активной секции
     * @param methodName Название метода для вызова
     * @param scrollCheckMethod Название метода для проверки скролла
     */
    public ConstructorTest(String sectionName, String expectedText, String methodName, String scrollCheckMethod) {
        this.sectionName = sectionName;
        this.expectedText = expectedText;
        this.methodName = methodName;
        this.scrollCheckMethod = scrollCheckMethod;
    }

    /**
     * Параметры для тестов конструктора - разные разделы
     */
    @Parameterized.Parameters(name = "Раздел: {0}")
    public static Collection<Object[]> getSections() {
        return Arrays.asList(new Object[][]{
                {"Булки", Constants.BUNS_SECTION_TEXT, "clickBunSection", "isBunsSectionScrolledIntoView"},
                {"Соусы", Constants.SAUCES_SECTION_TEXT, "clickSauceSection", "isSaucesSectionScrolledIntoView"},
                {"Начинки", Constants.FILLINGS_SECTION_TEXT, "clickFillingSection", "isFillingsSectionScrolledIntoView"}
        });
    }

    /**
     * Параметризованный тест переключения между разделами конструктора
     * Проверяет корректность работы навигации по разделам конструктора бургеров
     * и скролл к соответствующим группам ингредиентов
     */
    @Test
    @DisplayName("Переход к разделу конструктора со скроллом")
    @Description("Проверка переключения на раздел: {sectionName} с проверкой скролла к ингредиентам")
    public void testParameterizedSectionNavigationWithScroll() {
        System.out.println("=== НАЧАЛО ТЕСТА: Переход к разделу конструктора '" + sectionName + "' со скроллом ===");
        System.out.println("Раздел: " + sectionName);
        System.out.println("Ожидаемый текст: " + expectedText);
        System.out.println("Метод: " + methodName);
        System.out.println("Проверка скролла: " + scrollCheckMethod);

        // Подготовка: открытие конструктора
        MainPage mainPage = new MainPage(driver);
        mainPage.open();
        mainPage.waitForLoad();

        ConstructorPage constructorPage = new ConstructorPage(driver);
        constructorPage.waitForLoad();

        // Проверяем начальное состояние (должны быть активны Булки)
        String initialSection = constructorPage.getActiveSectionText();
        assertEquals("Начальное состояние конструктора должно быть 'Булки'",
                Constants.BUNS_SECTION_TEXT, initialSection);
        System.out.println("Начальная секция подтверждена: " + initialSection);

        // Выполнение: переключение на указанную секцию
        switch (methodName) {
            case "clickBunSection":
                System.out.println("Активация секции 'Булки'");
                constructorPage.clickBunSection();
                break;
            case "clickSauceSection":
                System.out.println("Активация секции 'Соусы'");
                constructorPage.clickSauceSection();
                break;
            case "clickFillingSection":
                System.out.println("Активация секции 'Начинки'");
                constructorPage.clickFillingSection();
                break;
            default:
                throw new IllegalArgumentException("Неизвестный метод: " + methodName);
        }

        // Проверка 1: подтверждение активной секции после переключения
        String activeSection = constructorPage.getActiveSectionText();
        assertEquals("После переключения должен быть активен раздел '" + sectionName + "'",
                expectedText, activeSection);

        System.out.println("Активная секция после переключения: " + activeSection);

        // Проверка 2: подтверждение скролла к соответствующей группе ингредиентов
        switch (scrollCheckMethod) {
            case "isBunsSectionScrolledIntoView":
                assertTrue("После переключения на раздел '" + sectionName + "' должен произойти скролл к соответствующей группе ингредиентов",
                        constructorPage.isBunsSectionScrolledIntoView());
                break;
            case "isSaucesSectionScrolledIntoView":
                assertTrue("После переключения на раздел '" + sectionName + "' должен произойти скролл к соответствующей группе ингредиентов",
                        constructorPage.isSaucesSectionScrolledIntoView());
                break;
            case "isFillingsSectionScrolledIntoView":
                assertTrue("После переключения на раздел '" + sectionName + "' должен произойти скролл к соответствующей группе ингредиентов",
                        constructorPage.isFillingsSectionScrolledIntoView());
                break;
            default:
                throw new IllegalArgumentException("Неизвестный метод проверки скролла: " + scrollCheckMethod);
        }

        System.out.println("Скролл к разделу '" + sectionName + "' подтвержден");
        System.out.println("=== ТЕСТ ЗАВЕРШЕН: Переход к разделу конструктора '" + sectionName + "' со скроллом ===");
    }

    /**
     * Дополнительный тест для проверки последовательного переключения всех разделов
     * Проверяет, что скролл работает корректно при последовательном переключении
     */
    @Test
    @DisplayName("Последовательное переключение всех разделов конструктора")
    @Description("Проверка последовательного переключения между всеми разделами конструктора с проверкой скролла")
    public void testSequentialSectionNavigation() {
        System.out.println("=== НАЧАЛО ТЕСТА: Последовательное переключение разделов конструктора ===");

        // Подготовка: открытие конструктора
        MainPage mainPage = new MainPage(driver);
        mainPage.open();
        mainPage.waitForLoad();

        ConstructorPage constructorPage = new ConstructorPage(driver);
        constructorPage.waitForLoad();

        // Последовательно переключаем все разделы и проверяем скролл

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