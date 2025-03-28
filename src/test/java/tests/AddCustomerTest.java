package tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.*;

public class AddCustomerTest extends BaseTest {

    @Test
    @DisplayName("Добавление нового клиента")
    @Description("Проверка успешного добавления нового клиента в систему")
    public void shouldAddCustomerSuccessfully() {
        // 1. Клик по кнопке Add Customer
        mainManagerPage.clickAddCustomer();

        // 2. Добавление нового пользователя и переход к вкладке Customers
        addCustomerManagerPage.fillingFields(testCustomerData);
        addCustomerManagerPage.clickAddCustomerButton();
        addCustomerManagerPage.handleAlert();
        mainManagerPage.clickCustomers();

        // 3. Проверка, что созданный пользователь появился в общей таблице
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
