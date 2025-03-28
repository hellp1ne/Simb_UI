package tests;

import org.junit.*;

public class AddCustomerTest extends BaseTest {
    @Test
    public void shouldAddCustomerSuccessfully() {
        // 1. Клик по кнопке Add Customer
        mainManagerPage.clickAddCustomer();

        // 2. Заполнение всех полей
        addCustomerManagerPage.fillingFields(testCustomerData);

        addCustomerManagerPage.clickAddCustomerButton();

        addCustomerManagerPage.handleAlert();

        mainManagerPage.clickCustomers();

        Assert.assertTrue(customersManagerPage.isCustomerPresent(testCustomerData.firstName));
    }

    @After
    public void cleanup() {
        // 4. Переход на страницу Customers
        mainManagerPage.clickCustomers();

        // 5. Удаление созданного пользователя
        customersManagerPage.searchCustomerByName(testCustomerData.firstName);
        if (customersManagerPage.isCustomerPresent(testCustomerData.firstName)) {
            customersManagerPage.deleteUserWithEmptyAccountNumber();
        }
        customersManagerPage.clearSearch();
    }
}
