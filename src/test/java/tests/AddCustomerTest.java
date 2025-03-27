package tests;

import browsers.Browser;
import browsers.WebDriverFactory;
import data.NewCustomerData;
import org.junit.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageelements.AddCustomerManagerPage;
import pageelements.CustomersManagerPage;
import pageelements.MainManagerPage;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

public class AddCustomerTest {
    private static WebDriver driver;
    private static WebDriverWait wait;
    private static MainManagerPage mainManagerPage;
    private static AddCustomerManagerPage addCustomerManagerPage;
    private static CustomersManagerPage customersManagerPage;
    private static NewCustomerData.CustomerData testCustomerData;
    private static Properties properties;

    @BeforeClass
    public static void setup() {
        // Загрузка конфигурации из файла
        properties = new Properties();
        try {
            properties.load(new FileInputStream("src/main/resources/config.properties"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }

        // Инициализация драйвера на основе конфигурации
        String browserType = properties.getProperty("browser.name");
        Browser browser = WebDriverFactory.getBrowser(browserType);
        driver = browser.createDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // Инициализация страниц
        driver.get("https://www.globalsqa.com/angularJs-protractor/BankingProject/#/manager");
        mainManagerPage = new MainManagerPage(driver);
        addCustomerManagerPage = new AddCustomerManagerPage(driver);
        customersManagerPage = new CustomersManagerPage(driver);
    }

    @Before
    public void prepareTest() {
        // Генерация тестовых данных
        NewCustomerData customerData = new NewCustomerData();
        testCustomerData = customerData.generateCustomerData();
    }

    @Test
    public void shouldAddCustomerSuccessfully() {
        // 1. Клик по кнопке Add Customer
        mainManagerPage.clickAddCustomer();

        // 2. Заполнение всех полей
        addCustomerManagerPage.fillingFields(testCustomerData);

        addCustomerManagerPage.clickAddCustomerButton();

        addCustomerManagerPage.handleAlert();

        mainManagerPage.clickCustomers();

        Assert.assertTrue(customersManagerPage.isCustomerPresent(testCustomerData.firstName));
    }

    @After
    public void cleanup() {
        // 4. Переход на страницу Customers
        mainManagerPage.clickCustomers();

        // 5. Удаление созданного пользователя
        customersManagerPage.searchCustomerByName(testCustomerData.firstName);
        if (customersManagerPage.isCustomerPresent(testCustomerData.firstName)) {
            customersManagerPage.deleteUserWithEmptyAccountNumber();
        }
        customersManagerPage.clearSearch();
    }

    @AfterClass
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
