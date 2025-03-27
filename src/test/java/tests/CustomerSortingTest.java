package tests;

import browsers.WebDriverFactory;
import org.junit.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageelements.CustomersManagerPage;
import pageelements.MainManagerPage;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

public class CustomerSortingTest {
    private static WebDriver driver;
    private static WebDriverWait wait;
    private static MainManagerPage mainManagerPage;
    private static CustomersManagerPage customersManagerPage;

    @BeforeClass
    public static void setup() {
        // Загрузка конфигурации из файла
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("src/main/resources/config.properties"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }

        // Инициализация драйвера
        String browserName = properties.getProperty("browser.name", "yandex");
        driver = WebDriverFactory.getBrowser(browserName).createDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // Инициализация страниц
        driver.get("https://www.globalsqa.com/angularJs-protractor/BankingProject/#/manager");
        mainManagerPage = new MainManagerPage(driver);
        customersManagerPage = new CustomersManagerPage(driver);
    }

    @Test
    public void shouldSortCustomersByNameCorrectly() {
        // 1. Клик по кнопке Customers
        mainManagerPage.clickCustomers();

        // 2. Клик по кнопке FirstName для сортировки
        // Метод verifyFirstNameSorting() сам выполняет двойной клик для проверки сортировки

        // 3. Проверка корректности сортировки
        boolean isSorted = customersManagerPage.verifyFirstNameSorting();
        Assert.assertTrue("Customers should be sorted by first name correctly", isSorted);
    }

    @AfterClass
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}