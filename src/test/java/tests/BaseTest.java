package tests;

import browsers.WebDriverFactory;
import data.GenerationCustomerData;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageelements.AddingCustomerManagerPage;
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
    protected static AddingCustomerManagerPage addingCustomerManagerPage;
    protected static GenerationCustomerData.CustomerData testCustomerData;
    protected static Properties properties;

    @BeforeClass
    public static void setUpClass() {
        properties = new Properties();
        try {
            properties.load(new FileInputStream("src/test/resources/config.properties"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }

        String browserName = properties.getProperty("browser.name");
        driver = WebDriverFactory.getBrowser(browserName).createDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        driver.manage().window().maximize();

        driver.get(getFullUrl("manager.path"));

        mainManagerPage = new MainManagerPage(driver);
        customersManagerPage = new CustomersManagerPage(driver);
        addingCustomerManagerPage = new AddingCustomerManagerPage(driver);
    }

    @Before
    public void setUpTest() {
        GenerationCustomerData customerData = new GenerationCustomerData();
        testCustomerData = customerData.generateCustomerData();
    }

    @AfterClass
    public static void tearDownClass() {
        if (driver != null) {
            driver.quit();
        }
    }

    public static String getFullUrl(String pathProperty) {
        return properties.getProperty("base.url") + properties.getProperty(pathProperty);
    }
}