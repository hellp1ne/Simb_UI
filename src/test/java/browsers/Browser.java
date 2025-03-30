package browsers;

import org.openqa.selenium.WebDriver;

public interface Browser {
    WebDriver createDriver();
}