package tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.*;

public class AverageCustomerDeletionTest extends BaseTest {

    @Test
    @DisplayName("Удаление клиента со средней длиной имени")
    @Description("Проверка удаления клиента, чье имя имеет длину, наиболее близкую к средней")
    public void shouldDeleteAndRecreateCustomerSuccessfully() {

        mainManagerPage.clickBtnCustomers();

        testCustomerData = customersManagerPage.deleteUserWithAverageNameLength();
        Assert.assertNotNull("No customer was deleted", testCustomerData);

        boolean isCustomerPresent = customersManagerPage.searchCustomerByName(testCustomerData.firstName);
        Assert.assertFalse("Customer should not be present after deletion", isCustomerPresent);
    }

    @After
    public void recreateDeletedCustomer() {

        mainManagerPage.clickBtnAddCustomer();

        if (testCustomerData != null) {
            addingCustomerManagerPage.fillFields(testCustomerData);
            addingCustomerManagerPage.clickAddCustomerButton();
            addingCustomerManagerPage.handleAlert();
        }
    }
}