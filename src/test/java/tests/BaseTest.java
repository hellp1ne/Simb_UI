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

/**
 * Базовый класс для всех тестов, содержащий общую логику инициализации и завершения работы.
 * Обеспечивает:
 * - Загрузку конфигурации из файла
 * - Инициализацию WebDriver
 * - Управление тестовыми данными
 * - Общие методы для работы с URL
 */
public abstract class BaseTest {
    protected static WebDriver driver;
    protected static WebDriverWait wait;
    protected static MainManagerPage mainManagerPage;
    protected static CustomersManagerPage customersManagerPage;
    protected static AddingCustomerManagerPage addingCustomerManagerPage;
    protected static GenerationCustomerData.CustomerData testCustomerData;
    protected static Properties properties;

    /**
     * Метод инициализации перед всеми тестами класса.
     * Выполняет:
     * 1. Загрузку конфигурации из файла config.properties
     * 2. Инициализацию WebDriver для указанного браузера
     * 3. Максимизацию окна браузера
     * 4. Инициализацию Page Objects
     *
     * @throws RuntimeException если не удалось загрузить конфигурационный файл
     */
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

    /**
     * Метод инициализации перед каждым тестом.
     * Генерирует новые тестовые данные для каждого тестового метода.
     */
    @Before
    public void setUpTest() {
        GenerationCustomerData customerData = new GenerationCustomerData();
        testCustomerData = customerData.generateCustomerData();
    }

    /**
     * Метод завершения работы после всех тестов класса.
     * Закрывает браузер и освобождает ресурсы WebDriver.
     */
    @AfterClass
    public static void tearDownClass() {
        if (driver != null) {
            driver.quit();
        }
    }

    /**
     * Формирует полный URL на основе базового адреса и пути из конфигурации.
     *
     * @param pathProperty имя свойства, содержащего путь
     * @return полный URL (базовый адрес + путь)
     */
    public static String getFullUrl(String pathProperty) {
        return properties.getProperty("base.url") + properties.getProperty(pathProperty);
    }
}