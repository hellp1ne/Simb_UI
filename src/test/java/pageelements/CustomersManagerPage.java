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
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CustomersManagerPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    /**
     * Локаторы элементов с аннотацией @FindBy
     */
    @FindBy(xpath = "//tbody/tr/td[1]")
    private List<WebElement> firstNameElements;

    @FindBy(xpath = "//tbody/tr/td[2]")
    private List<WebElement> lastNameElements;

    @FindBy(xpath = "//tbody/tr/td[3]")
    private List<WebElement> postCodeElements;

    @FindBy(xpath = "//tbody/tr/td[4]")
    private List<WebElement> accountNumberElements;

    @FindBy(xpath = "//button[contains(text(),'Delete')]")
    private List<WebElement> deleteButtons;

    @FindBy(xpath = "//a[contains(text(),'First Name')]")
    private WebElement firstNameSortButton;

    @FindBy(xpath = "//input[@placeholder='Search Customer']")
    private WebElement searchField;

    public CustomersManagerPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
    }

    @Step("Удаление пользователя с пустым номером счета")
    public void deleteUserWithEmptyAccountNumber() {
        List<WebElement> accountNumbers = wait.until(ExpectedConditions.visibilityOfAllElements(accountNumberElements));
        List<WebElement> deleteBtns = wait.until(ExpectedConditions.visibilityOfAllElements(deleteButtons));

        IntStream.range(0, accountNumbers.size())
                .filter(i -> accountNumbers.get(i).getText().isEmpty())
                .findFirst()
                .ifPresent(i -> deleteBtns.get(i).click());
    }

    @Step("Удаление пользователя со средней длиной имени")
    public GenerationCustomerData.CustomerData deleteUserWithAverageNameLength() {
        List<WebElement> firstNameElems = wait.until(ExpectedConditions.visibilityOfAllElements(firstNameElements));
        List<WebElement> lastNameElems = wait.until(ExpectedConditions.visibilityOfAllElements(lastNameElements));
        List<WebElement> postCodeElems = wait.until(ExpectedConditions.visibilityOfAllElements(postCodeElements));
        List<WebElement> deleteBtns = wait.until(ExpectedConditions.visibilityOfAllElements(deleteButtons));

        if (firstNameElems.isEmpty()) {
            return null;
        }

        return firstNameElems.stream()
                .collect(Collectors.toMap(
                        WebElement::getText,
                        el -> el.getText().length()
                ))
                .entrySet().stream()
                .min(Comparator.comparingDouble(entry ->
                        Math.abs(entry.getValue() -
                                firstNameElems.stream()
                                        .mapToInt(el -> el.getText().length())
                                        .average()
                                        .orElse(0))
                ))
                .flatMap(closestEntry ->
                        IntStream.range(0, firstNameElems.size())
                                .filter(i -> firstNameElems.get(i).getText().equals(closestEntry.getKey()))
                                .boxed()
                                .findFirst()
                                .map(i -> {
                                    GenerationCustomerData.CustomerData customer = new GenerationCustomerData.CustomerData(
                                            closestEntry.getKey(),
                                            lastNameElems.get(i).getText(),
                                            postCodeElems.get(i).getText()
                                    );
                                    deleteBtns.get(i).click();
                                    return customer;
                                })
                )
                .orElse(null);
    }

    @Step("Проверка сортировки по имени")
    public boolean verifyFirstNameSorting() {
        WebElement sortBtn = wait.until(ExpectedConditions.elementToBeClickable(firstNameSortButton));
        sortBtn.click();

        List<String> namesAfterSort = wait.until(ExpectedConditions.refreshed(
                        ExpectedConditions.visibilityOfAllElements(firstNameElements)))
                .stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());

        return namesAfterSort.equals(
                namesAfterSort.stream()
                        .sorted(Collections.reverseOrder())
                        .collect(Collectors.toList())
        );
    }

    @Step("Поиск пользователя по имени: {firstName}")
    public boolean searchCustomerByName(String firstName) {
        WebElement searchInput = wait.until(ExpectedConditions.elementToBeClickable(searchField));
        searchInput.clear();
        searchInput.sendKeys(firstName);
        wait.until(ExpectedConditions.textToBePresentInElementValue(searchField, firstName));

        return !firstNameElements.isEmpty() &&
                firstNameElements.stream()
                        .map(WebElement::getText)
                        .anyMatch(name -> name.equals(firstName));
    }

    @Step("Проверка наличия пользователя с именем: {firstName}")
    public boolean checkIsCustomerPresent(String firstName) {
        return wait.until(ExpectedConditions.visibilityOfAllElements(firstNameElements))
                .stream()
                .map(WebElement::getText)
                .anyMatch(name -> name.equals(firstName));
    }

    @Step("Очистка поля поиска")
    public void clearSearch() {
        WebElement searchInput = wait.until(ExpectedConditions.elementToBeClickable(searchField));
        searchInput.clear();
        wait.until(ExpectedConditions.textToBePresentInElementValue(searchField, ""));
    }
}