package pageelements;

import data.NewCustomerData;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class AddCustomerManagerPage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final NewCustomerData customerData;
    private NewCustomerData.CustomerData lastDeletedCustomerData;

    // Локаторы элементов
    private final By firstNameField = By.xpath("//input[@placeholder='First Name']");
    private final By lastNameField = By.xpath("//input[@placeholder='Last Name']");
    private final By postCodeField = By.xpath("//input[@placeholder='Post Code']");
    private final By addCustomerBtn = By.xpath("//button[@type='submit']");

    public AddCustomerManagerPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.customerData = new NewCustomerData();
    }

    // Метод для создания пользователя с заданными параметрами
    public void createSpecificCustomer(NewCustomerData.CustomerData data) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameField)).sendKeys(data.firstName);
        driver.findElement(lastNameField).sendKeys(data.lastName);
        driver.findElement(postCodeField).sendKeys(data.postCode);
        clickAddCustomerButton();
        handleAlert();
    }

    // Метод для воссоздания удаленного пользователя (со средней длиной имени)
    public void recreateDeletedAverageUser() {
        if (lastDeletedCustomerData != null) {
            createSpecificCustomer(lastDeletedCustomerData);
            System.out.println("Recreated user: " + lastDeletedCustomerData.firstName);
        } else {
            throw new IllegalStateException("No customer data available to recreate");
        }
    }

    // Метод для получения данных последнего удаленного пользователя
    public void setLastDeletedCustomerData(NewCustomerData.CustomerData data) {
        this.lastDeletedCustomerData = data;
    }

    // Остальные методы остаются без изменений
    public void fillCustomerDetails() {
        NewCustomerData.CustomerData data = customerData.generateCustomerData();
        createSpecificCustomer(data);
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameField)).sendKeys(data.firstName);
        driver.findElement(lastNameField).sendKeys(data.lastName);
        driver.findElement(postCodeField).sendKeys(data.postCode);
    }

    public void fillingFields(NewCustomerData.CustomerData data) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameField)).sendKeys(data.firstName);
        driver.findElement(lastNameField).sendKeys(data.lastName);
        driver.findElement(postCodeField).sendKeys(data.postCode);
        System.out.println(data.postCode);
        System.out.println(data.firstName);
    }

    public void clickAddCustomerButton() {
        wait.until(ExpectedConditions.elementToBeClickable(addCustomerBtn)).click();
    }

    public void handleAlert() {
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();
    }

    public void addNewCustomer() {
        fillCustomerDetails();
    }
}
