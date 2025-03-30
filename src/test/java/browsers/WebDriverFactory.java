package browsers;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class WebDriverFactory {

    public static Browser getBrowser(String browserName) {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("src/test/resources/config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        switch (browserName.toLowerCase()) {
            case "chrome":
                return new ChromeBrowser();
            case "yandex":
                return new YandexBrowser();
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browserName);
        }
    }
}