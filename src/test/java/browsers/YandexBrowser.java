package browsers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class YandexBrowser implements Browser {

    @Override
    public WebDriver createDriver() {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("src/test/resources/config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String driverPath = properties.getProperty("yandex.driver.path");
        String binaryPath = properties.getProperty("yandex.binary.path");

        System.setProperty("webdriver.chrome.driver", driverPath);
        ChromeOptions options = new ChromeOptions();
        options.setBinary(binaryPath);
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        return new ChromeDriver(options);
    }
}