package tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.*;

public class CustomerSortingTest extends BaseTest {

    @Test
    @DisplayName("Проверка сортировки клиентов по имени")
    @Description("Проверка корректности работы сортировки клиентов по полю First Name")
    public void shouldSortCustomersByNameCorrectly() {

        mainManagerPage.clickBtnCustomers();

        boolean isSorted = customersManagerPage.verifyFirstNameSorting();
        Assert.assertTrue("Customers should be sorted by first name correctly", isSorted);
    }
}