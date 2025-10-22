package praktikum.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

/**
 * Базовый класс для всех Page Object моделей
 * Содержит общие методы для работы с элементами страницы
 * Устраняет дублирование кода в классах страниц
 */
public abstract class BasePage {

    // Экземпляр WebDriver для взаимодействия с браузером
    protected final WebDriver driver;

    // Настройка явных ожиданий для стабильности тестов
    protected final WebDriverWait wait;

    /**
     * Конструктор базового класса Page Object
     *
     * @param driver Экземпляр WebDriver для управления браузером
     */
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    /**
     * Ожидает появления и видимости элемента на странице
     * Используется для элементов, которые должны быть видны пользователю
     *
     * @param locator Локатор ожидаемого элемента
     * @return Найденный WebElement для дальнейших операций
     */
    protected WebElement waitForElement(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Ожидает появления элемента с кастомным таймаутом
     * Используется для элементов, которые могут загружаться дольше
     *
     * @param locator Локатор ожидаемого элемента
     * @param timeoutInSeconds Время ожидания в секундах
     * @return Найденный WebElement для дальнейших операций
     */
    protected WebElement waitForElement(By locator, int timeoutInSeconds) {
        WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        return customWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Ожидает, когда элемент станет кликабельным
     * Гарантирует, что элемент готов к взаимодействию перед выполнением клика
     *
     * @param locator Локатор элемента для проверки кликабельности
     */
    protected void waitForElementToBeClickable(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Ожидает, когда элемент будет содержать определенный текст
     * Используется для проверки изменения текста элемента
     *
     * @param locator Локатор элемента для проверки текста
     * @param text Ожидаемый текст
     */
    protected void waitForTextToBe(By locator, String text) {
        wait.until(ExpectedConditions.textToBe(locator, text));
    }

    /**
     * Универсальный метод для безопасного клика по элементу
     * Ожидает кликабельности элемента перед выполнением клика
     *
     * @param locator Локатор элемента для клика
     */
    protected void click(By locator) {
        waitForElementToBeClickable(locator);
        driver.findElement(locator).click();
    }

    /**
     * Универсальный метод для заполнения текстовых полей формы
     * Очищает поле перед вводом нового значения для надежности
     *
     * @param locator Локатор поля для заполнения
     * @param value Значение для ввода в поле
     */
    protected void setField(By locator, String value) {
        WebElement element = waitForElement(locator);
        element.clear();
        element.sendKeys(value);
    }

    /**
     * Проверяет видимость элемента на странице с обработкой исключений
     * Используется для безопасной проверки наличия элементов без падения теста
     *
     * @param locator Локатор элемента для проверки
     * @return true если элемент видим, false если элемент не найден или не видим
     */
    protected boolean isElementVisible(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Получает текст элемента с обработкой исключений
     * Возвращает пустую строку если элемент не найден
     *
     * @param locator Локатор элемента для получения текста
     * @return Текст элемента или пустая строка
     */
    protected String getElementText(By locator) {
        try {
            return driver.findElement(locator).getText();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Обрабатывает возможные диалоги браузера на странице
     * Использует явные ожидания вместо Thread.sleep
     */
    protected void handlePotentialDialogs() {
        try {
            // Проверяем наличие alert в течение короткого времени
            WebDriverWait alertWait = new WebDriverWait(driver, Duration.ofSeconds(2));
            alertWait.until(ExpectedConditions.alertIsPresent());

            // Если alert появился, закрываем его
            driver.switchTo().alert().dismiss();
            System.out.println("Обнаружен и закрыт диалог браузера");
        } catch (Exception e) {
            // Alert не появился - это нормально, продолжаем выполнение
        }
    }
}