package tests;

import browsers.WebDriverFactory;
import data.NewCustomerData;
import org.junit.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageelements.AddCustomerManagerPage;
import pageelements.CustomersManagerPage;
import pageelements.MainManagerPage;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

public class AverageCustomerDeletionTest {
    private static WebDriver driver;
    private static WebDriverWait wait;
    private static MainManagerPage mainManagerPage;
    private static CustomersManagerPage customersManagerPage;
    private static AddCustomerManagerPage addCustomerManagerPage;
    private static NewCustomerData.CustomerData deletedCustomer;

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
        String browserName = properties.getProperty("browser.name");
        driver = WebDriverFactory.getBrowser(browserName).createDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // Инициализация страниц
        driver.get("https://www.globalsqa.com/angularJs-protractor/BankingProject/#/manager");
        mainManagerPage = new MainManagerPage(driver);
        customersManagerPage = new CustomersManagerPage(driver);
        addCustomerManagerPage = new AddCustomerManagerPage(driver);
    }

    @Test
    public void shouldDeleteAndRecreateCustomerSuccessfully() {
        // 1. Клик по кнопке Customers
        mainManagerPage.clickCustomers();

        // 2. Удаление пользователя со средней длиной имени
        deletedCustomer = customersManagerPage.deleteUserWithAverageNameLength();
        Assert.assertNotNull("No customer was deleted", deletedCustomer);

        // 3. Проверка, что пользователь удален
        boolean isCustomerPresent = customersManagerPage.searchCustomerByName(deletedCustomer.firstName);
        Assert.assertFalse("Customer should not be present after deletion", isCustomerPresent);
    }

    @After
    public void recreateDeletedCustomer() {
        // 4. Клик по кнопке Add Customer
        mainManagerPage.clickAddCustomer();

        // 5. Добавление удаленного пользователя обратно
        if (deletedCustomer != null) {
            addCustomerManagerPage.fillingFields(deletedCustomer);
            addCustomerManagerPage.clickAddCustomerButton();
            addCustomerManagerPage.handleAlert();
            System.out.println("Recreated customer: " + deletedCustomer.firstName);
        }
    }

    @AfterClass
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}