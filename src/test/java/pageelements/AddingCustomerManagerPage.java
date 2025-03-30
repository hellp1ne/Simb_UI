package pageelements;

import data.GenerationCustomerData;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class AddingCustomerManagerPage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final GenerationCustomerData customerData;

    /**
     * Локаторы элементов с аннотацией @FindBy
     */
    @FindBy(xpath = "//input[@placeholder='First Name']")
    private WebElement firstNameField;

    @FindBy(xpath = "//input[@placeholder='Last Name']")
    private WebElement lastNameField;

    @FindBy(xpath = "//input[@placeholder='Post Code']")
    private WebElement postCodeField;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement addCustomerBtn;

    public AddingCustomerManagerPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.customerData = new GenerationCustomerData();
        PageFactory.initElements(driver, this); // Инициализация PageFactory
    }

    @Step("Заполнение полей данными клиента")
    public void fillFields(GenerationCustomerData.CustomerData data) {
        wait.until(ExpectedConditions.visibilityOf(firstNameField)).sendKeys(data.firstName);
        lastNameField.sendKeys(data.lastName);
        postCodeField.sendKeys(data.postCode);
    }

    @Step("Нажатие кнопки 'Add Customer'")
    public void clickAddCustomerButton() {
        wait.until(ExpectedConditions.elementToBeClickable(addCustomerBtn)).click();
    }

    @Step("Обработка алерта")
    public void handleAlert() {
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();
    }
}