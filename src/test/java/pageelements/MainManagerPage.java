package pageelements;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import tests.BaseTest;

import java.time.Duration;

public class MainManagerPage {
    private final WebDriverWait wait;

    /**
     * Локаторы элементов с аннотацией @FindBy
     */
    @FindBy(xpath = "//button[contains(text(),'Add Customer')]")
    private WebElement addCustomerBtn;

    @FindBy(xpath = "//button[contains(text(),'Customers')]")
    private WebElement customersBtn;

    public MainManagerPage(WebDriver driver) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    @Step("Нажатие кнопки 'Add Customer'")
    public void clickBtnAddCustomer() {
        wait.until(ExpectedConditions.elementToBeClickable(addCustomerBtn)).click();
        wait.until(ExpectedConditions.urlToBe(BaseTest.getFullUrl("add.customer.path")));
    }

    @Step("Нажатие кнопки 'Customers'")
    public void clickBtnCustomers() {
        wait.until(ExpectedConditions.elementToBeClickable(customersBtn)).click();
        wait.until(ExpectedConditions.urlToBe(BaseTest.getFullUrl("customers.list.path")));
    }
}