package tests;

import org.junit.*;

public class CustomerSortingTest extends BaseTest {
    @Test
    public void shouldSortCustomersByNameCorrectly() {
        // 1. Клик по кнопке Customers
        mainManagerPage.clickCustomers();

        // 3. Проверка корректности сортировки
        boolean isSorted = customersManagerPage.verifyFirstNameSorting();
        Assert.assertTrue("Customers should be sorted by first name correctly", isSorted);
    }
}