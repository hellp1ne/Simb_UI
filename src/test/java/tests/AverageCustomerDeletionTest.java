package tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.*;

public class AverageCustomerDeletionTest extends BaseTest {

    @Test
    @DisplayName("Удаление клиента со средней длиной имени")
    @Description("Проверка удаления клиента, чье имя имеет длину, наиболее близкую к средней")
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