package tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.*;

/**
 * Тестовый класс для проверки функциональности сортировки клиентов.
 * Проверяет корректность работы сортировки по полю "First Name".
 */
public class CustomerSortingTest extends BaseTest {

    /**
     * Тест проверки сортировки клиентов по имени.
     * Выполняет следующие шаги:
     * 1. Переход на страницу списка клиентов
     * 2. Проверка корректности работы сортировки по имени
     * <p>
     * Ожидаемый результат: клиенты должны быть корректно отсортированы по имени
     */
    @Test
    @DisplayName("Проверка сортировки клиентов по имени")
    @Description("Проверка корректности работы сортировки клиентов по полю First Name")
    public void shouldSortCustomersByNameCorrectly() {
        // 1. Клик по кнопке Customers
        mainManagerPage.clickBtnCustomers();

        // 2. Проверка корректности сортировки
        boolean isSorted = customersManagerPage.verifyFirstNameSorting();
        Assert.assertTrue("Customers should be sorted by first name correctly", isSorted);
    }
}