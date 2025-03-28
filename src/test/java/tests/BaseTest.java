package tests;

import browsers.WebDriverFactory;
import data.NewCustomerData;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageelements.AddCustomerManagerPage;
import pageelements.CustomersManagerPage;
import pageelements.MainManagerPage;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

public abstract class BaseTest {
    protected static WebDriver driver;
    protected static WebDriverWait wait;
    protected static MainManagerPage mainManagerPage;
    protected static CustomersManagerPage customersManagerPage;
    protected static AddCustomerManagerPage addCustomerManagerPage;
    protected static NewCustomerData.CustomerData testCustomerData;

    @BeforeClass
    public static void setUpClass() {
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

        // Максимизация окна браузера
        driver.manage().window().maximize();

        // Инициализация страниц
        driver.get("https://www.globalsqa.com/angularJs-protractor/BankingProject/#/manager");
        mainManagerPage = new MainManagerPage(driver);
        customersManagerPage = new CustomersManagerPage(driver);
        addCustomerManagerPage = new AddCustomerManagerPage(driver);
    }

    @Before
    public void setUpTest() {
        // Генерация тестовых данных
        NewCustomerData customerData = new NewCustomerData();
        testCustomerData = customerData.generateCustomerData();
    }

    @AfterClass
    public static void tearDownClass() {
        if (driver != null) {
            driver.quit();
        }
    }
}