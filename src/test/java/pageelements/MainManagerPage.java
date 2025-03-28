package pageelements;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class MainManagerPage {
    private final WebDriverWait wait;

    // Локаторы элементов
    private final By addCustomerButton = By.xpath("//button[contains(text(),'Add Customer')]");
    private final By customersButton = By.xpath("//button[contains(text(),'Customers')]");

    public MainManagerPage(WebDriver driver) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Нажать кнопку Add Customer и дождаться изменения URL
    public void clickAddCustomer() {
        WebElement addCustomerBtn = wait.until(ExpectedConditions.elementToBeClickable(addCustomerButton));
        addCustomerBtn.click();
        wait.until(ExpectedConditions.urlToBe("https://www.globalsqa.com/angularJs-protractor/BankingProject/#/manager/addCust"));
    }

    // Нажать кнопку Customers и дождаться изменения URL
    public void clickCustomers() {
        WebElement customersBtn = wait.until(ExpectedConditions.elementToBeClickable(customersButton));
        customersBtn.click();
        wait.until(ExpectedConditions.urlToBe("https://www.globalsqa.com/angularJs-protractor/BankingProject/#/manager/list"));
    }
}