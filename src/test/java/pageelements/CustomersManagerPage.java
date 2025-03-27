package pageelements;

import data.NewCustomerData;
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
    private final By firstNameColumn = By.xpath("//td[@class='ng-binding'][1]");
    private final By lastNameColumn = By.xpath("//td[@class='ng-binding'][3]");
    private final By postCodeColumn = By.xpath("//td[@class='ng-binding'][4]");
    private final By accountNumberColumn = By.xpath("//td[@class='ng-binding'][2]");
    private final By deleteButtons = By.xpath("//button[contains(text(),'Delete')]");
    private final By firstNameSortButton = By.xpath("//a[contains(text(),'First Name')]");
    private final By searchField = By.xpath("//input[@placeholder='Search Customer']");

    public CustomersManagerPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // Метод для удаления пользователя с пустым Account Number
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

    // Метод для удаления пользователя со средней длиной имени с возвратом его данных
    public NewCustomerData.CustomerData deleteUserWithAverageNameLength() {
        List<WebElement> firstNameElements = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(firstNameColumn));
        List<WebElement> lastNameElements = driver.findElements(lastNameColumn);
        List<WebElement> postCodeElements = driver.findElements(postCodeColumn);
        List<WebElement> deleteBtns = driver.findElements(deleteButtons);

        if (firstNameElements.isEmpty()) {
            return null;
        }

        // Создаем список пар (имя, длина имени)
        List<Map.Entry<String, Integer>> nameEntries = firstNameElements.stream()
                .map(element -> Map.entry(element.getText(), element.getText().length()))
                .collect(Collectors.toList());

        // Вычисляем среднюю длину имен
        double averageLength = nameEntries.stream()
                .mapToInt(Map.Entry::getValue)
                .average()
                .orElse(0);

        // Находим имя с длиной, ближайшей к средней
        Optional<Map.Entry<String, Integer>> closestEntry = nameEntries.stream()
                .min(Comparator.comparingDouble(
                        entry -> Math.abs(entry.getValue() - averageLength)
                ));

        if (closestEntry.isPresent()) {
            String nameToDelete = closestEntry.get().getKey();
            int index = -1;

            // Находим индекс пользователя для удаления
            for (int i = 0; i < firstNameElements.size(); i++) {
                if (firstNameElements.get(i).getText().equals(nameToDelete)) {
                    index = i;
                    break;
                }
            }

            if (index >= 0) {
                // Сохраняем данные перед удалением
                NewCustomerData.CustomerData deletedCustomer = new NewCustomerData.CustomerData(
                        nameToDelete,
                        lastNameElements.get(index).getText(),
                        postCodeElements.get(index).getText()
                );

                // Удаляем пользователя
                deleteBtns.get(index).click();
                wait.until(ExpectedConditions.stalenessOf(firstNameElements.get(index)));

                return deletedCustomer;
            }
        }
        return null;
    }

    // Метод для проверки сортировки по FirstName
    public boolean verifyFirstNameSorting() {
        WebElement sortBtn = wait.until(ExpectedConditions.elementToBeClickable(firstNameSortButton));

        // Первый клик - сортировка по возрастанию
        sortBtn.click();
        wait.until(ExpectedConditions.refreshed(ExpectedConditions.presenceOfAllElementsLocatedBy(firstNameColumn)));

        // Второй клик - сортировка по убыванию
        sortBtn.click();
        List<String> namesAfterSort = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(firstNameColumn))
                .stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());

        // Создаем отсортированный список в обратном порядке для сравнения
        List<String> expectedSortedNames = new ArrayList<>(namesAfterSort);
        expectedSortedNames.sort(Collections.reverseOrder());

        return namesAfterSort.equals(expectedSortedNames);
    }

    // Метод для поиска пользователя по имени
    public boolean searchCustomerByName(String firstName) {
        WebElement searchInput = wait.until(ExpectedConditions.elementToBeClickable(searchField));
        searchInput.clear();
        searchInput.sendKeys(firstName);
        wait.until(ExpectedConditions.textToBePresentInElementValue(searchField, firstName));

        List<String> foundNames = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(firstNameColumn))
                .stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());

        return foundNames.stream().anyMatch(name -> name.equals(firstName));
    }

    // Метод для получения всех имен пользователей
    public List<String> getAllFirstNames() {
        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(firstNameColumn))
                .stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    // Метод для получения всех номеров счетов
    public List<String> getAllAccountNumbers() {
        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(accountNumberColumn))
                .stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    // Метод для проверки наличия пользователя в таблице
    public boolean isCustomerPresent(String firstName, String lastName, String postCode) {
        List<WebElement> firstNames = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(firstNameColumn));
        List<WebElement> lastNames = driver.findElements(lastNameColumn);
        List<WebElement> postCodes = driver.findElements(postCodeColumn);

        for (int i = 0; i < firstNames.size(); i++) {
            if (firstNames.get(i).getText().equals(firstName) &&
                    lastNames.get(i).getText().equals(lastName) &&
                    postCodes.get(i).getText().equals(postCode)) {
                return true;
            }
        }
        return false;
    }

    // Метод для очистки поля поиска
    public void clearSearch() {
        WebElement searchInput = wait.until(ExpectedConditions.elementToBeClickable(searchField));
        searchInput.clear();
        wait.until(ExpectedConditions.textToBePresentInElementValue(searchField, ""));
    }
}