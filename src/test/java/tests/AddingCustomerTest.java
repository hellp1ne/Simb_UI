package tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.*;

public class AddingCustomerTest extends BaseTest {

    @Test
    @DisplayName("Добавление нового клиента")
    @Description("Проверка успешного добавления нового клиента в систему")
    public void shouldAddCustomerSuccessfully() {
        mainManagerPage.clickBtnAddCustomer();

        addingCustomerManagerPage.fillFields(testCustomerData);
        addingCustomerManagerPage.clickAddCustomerButton();
        addingCustomerManagerPage.handleAlert();
        mainManagerPage.clickBtnCustomers();

        Assert.assertTrue(customersManagerPage.checkIsCustomerPresent(testCustomerData.firstName));
    }

    @After
    public void cleanUp() {
        mainManagerPage.clickBtnCustomers();

        customersManagerPage.searchCustomerByName(testCustomerData.firstName);
        if (customersManagerPage.checkIsCustomerPresent(testCustomerData.firstName)) {
            customersManagerPage.deleteUserWithEmptyAccountNumber();
        }
        customersManagerPage.clearSearch();
    }
}