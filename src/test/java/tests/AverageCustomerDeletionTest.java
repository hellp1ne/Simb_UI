package tests;

import org.junit.*;

public class AverageCustomerDeletionTest extends BaseTest {


    @Test
    public void shouldDeleteAndRecreateCustomerSuccessfully() {
        // 1. Клик по кнопке Customers
        mainManagerPage.clickCustomers();

        // 2. Удаление пользователя со средней длиной имени
        testCustomerData = customersManagerPage.deleteUserWithAverageNameLength();
        Assert.assertNotNull("No customer was deleted", testCustomerData);

        // 3. Проверка, что пользователь удален
        boolean isCustomerPresent = customersManagerPage.searchCustomerByName(testCustomerData.firstName);
        Assert.assertFalse("Customer should not be present after deletion", isCustomerPresent);
    }

    @After
    public void recreateDeletedCustomer() {
        // 4. Клик по кнопке Add Customer
        mainManagerPage.clickAddCustomer();

        // 5. Добавление удаленного пользователя обратно
        if (testCustomerData != null) {
            addCustomerManagerPage.fillingFields(testCustomerData);
            addCustomerManagerPage.clickAddCustomerButton();
            addCustomerManagerPage.handleAlert();
        }
    }
}