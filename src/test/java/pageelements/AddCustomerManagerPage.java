package pageelements;

import data.NewCustomerData;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class AddCustomerManagerPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Локаторы элементов
    private final By firstNameField = By.xpath("//input[@placeholder='First Name']");
    private final By lastNameField = By.xpath("//input[@placeholder='Last Name']");
    private final By postCodeField = By.xpath("//input[@placeholder='Post Code']");
    private final By addCustomerBtn = By.xpath("//button[@type='submit']");

    public AddCustomerManagerPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void fillingFields(NewCustomerData.CustomerData data) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameField)).sendKeys(data.firstName);
        driver.findElement(lastNameField).sendKeys(data.lastName);
        driver.findElement(postCodeField).sendKeys(data.postCode);
    }

    public void clickAddCustomerButton() {
        wait.until(ExpectedConditions.elementToBeClickable(addCustomerBtn)).click();
    }

    public void handleAlert() {
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();
    }
}
