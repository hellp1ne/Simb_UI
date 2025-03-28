package pageelements;

import data.NewCustomerData;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

public class CustomersManagerPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Локаторы элементов
    private final By firstNameColumn = By.xpath("//tbody/tr/td[1]");
    private final By lastNameColumn = By.xpath("//tbody/tr/td[2]");
    private final By postCodeColumn = By.xpath("//tbody/tr/td[3]");
    private final By accountNumberColumn = By.xpath("//tbody/tr/td[4]");
    private final By deleteButtons = By.xpath("//button[contains(text(),'Delete')]");
    private final By firstNameSortButton = By.xpath("//a[contains(text(),'First Name')]");
    private final By searchField = By.xpath("//input[@placeholder='Search Customer']");

    public CustomersManagerPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    @Step("Удаление пользователя с пустым номером счета")
    public void deleteUserWithEmptyAccountNumber() {
        List<WebElement> accountNumbers = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(accountNumberColumn));
        List<WebElement> deleteBtns = driver.findElements(deleteButtons);

        for (int i = 0; i < accountNumbers.size(); i++) {
            if (accountNumbers.get(i).getText().isEmpty()) {
                deleteBtns.get(i).click();
                wait.until(ExpectedConditions.invisibilityOf(accountNumbers.get(i)));
                break;
            }
        }
    }

    @Step("Удаление пользователя со средней длиной имени")
    public NewCustomerData.CustomerData deleteUserWithAverageNameLength() {
        // Получаем все элементы с явным ожиданием
        List<WebElement> firstNameElements = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(firstNameColumn));
        List<WebElement> lastNameElements = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(lastNameColumn));
        List<WebElement> postCodeElements = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(postCodeColumn));
        List<WebElement> deleteBtns = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(deleteButtons));

        if (firstNameElements.isEmpty()) {
            return null;
        }

        // Проверяем, что все списки имеют одинаковый размер
        if (firstNameElements.size() != lastNameElements.size() ||
                firstNameElements.size() != postCodeElements.size() ||
                firstNameElements.size() != deleteBtns.size()) {
            return null;
        }

        // Создаем список пар (имя, длина имени)
        List<Map.Entry<String, Integer>> nameEntries = new ArrayList<>();
        for (WebElement element : firstNameElements) {
            String name = element.getText();
            nameEntries.add(Map.entry(name, name.length()));
        }

        // Вычисляем среднюю длину имен
        double averageLength = nameEntries.stream()
                .mapToInt(Map.Entry::getValue)
                .average()
                .orElse(0);

        // Находим имя с длиной, ближайшей к средней
        Optional<Map.Entry<String, Integer>> closestEntry = nameEntries.stream()
                .min(Comparator.comparingDouble(entry -> Math.abs(entry.getValue() - averageLength)));

        if (closestEntry.isPresent()) {
            String nameToDelete = closestEntry.get().getKey();

            // Находим индекс пользователя для удаления
            for (int i = 0; i < firstNameElements.size(); i++) {
                if (firstNameElements.get(i).getText().equals(nameToDelete)) {
                    // Сохраняем данные перед удалением
                    NewCustomerData.CustomerData deletedCustomer = new NewCustomerData.CustomerData(
                            nameToDelete,
                            lastNameElements.get(i).getText(),
                            postCodeElements.get(i).getText()
                    );

                    // Удаляем пользователя
                    deleteBtns.get(i).click();
                    wait.until(ExpectedConditions.stalenessOf(firstNameElements.get(i)));

                    return deletedCustomer;
                }
            }
        }
        return null;
    }

    @Step("Проверка сортировки по имени")
    public boolean verifyFirstNameSorting() {
        WebElement sortBtn = wait.until(ExpectedConditions.elementToBeClickable(firstNameSortButton));

        // Первый клик - сортировка по возрастанию
        sortBtn.click();
        wait.until(ExpectedConditions.refreshed(ExpectedConditions.presenceOfAllElementsLocatedBy(firstNameColumn)));

        List<String> namesAfterSort = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(firstNameColumn))
                .stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());

        // Создаем отсортированный список в обратном порядке для сравнения
        List<String> expectedSortedNames = new ArrayList<>(namesAfterSort);
        expectedSortedNames.sort(Collections.reverseOrder());

        return namesAfterSort.equals(expectedSortedNames);
    }

    @Step("Поиск пользователя по имени: {firstName}")
    public boolean searchCustomerByName(String firstName) {
        WebElement searchInput = wait.until(ExpectedConditions.elementToBeClickable(searchField));
        searchInput.clear();
        searchInput.sendKeys(firstName);
        wait.until(ExpectedConditions.textToBePresentInElementValue(searchField, firstName));
        if(firstNameColumn != null){
            List<String> foundNames = driver.findElements(firstNameColumn)
                    .stream()
                    .map(WebElement::getText)
                    .collect(Collectors.toList());

            return foundNames.stream().anyMatch(name -> name.equals(firstName));
        }
        return false;
    }

    @Step("Проверка наличия пользователя с именем: {firstName}")
    public boolean isCustomerPresent(String firstName) {
        List<WebElement> firstNames = driver.findElements(firstNameColumn);
        for (int i = 0; i < firstNames.size(); i++) {
            if (firstNames.get(i).getText().equals(firstName)) {
                return true;
            }
        }
        return false;
    }

    @Step("Очистка поля поиска")
    public void clearSearch() {
        WebElement searchInput = wait.until(ExpectedConditions.elementToBeClickable(searchField));
        searchInput.clear();
        wait.until(ExpectedConditions.textToBePresentInElementValue(searchField, ""));
    }
}