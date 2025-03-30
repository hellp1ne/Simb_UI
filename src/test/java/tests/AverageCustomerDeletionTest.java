package tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.*;

/**
 * Тестовый класс для проверки функциональности удаления клиента
 * со средней длиной имени и его последующего восстановления.
 */
public class AverageCustomerDeletionTest extends BaseTest {

    /**
     * Тест удаления клиента со средней длиной имени.
     * Выполняет следующие шаги:
     * 1. Переход на страницу списка клиентов
     * 2. Удаление клиента с именем, длина которого ближе всего к средней
     * 3. Проверка, что клиент успешно удален из системы
     */
    @Test
    @DisplayName("Удаление клиента со средней длиной имени")
    @Description("Проверка удаления клиента, чье имя имеет длину, наиболее близкую к средней")
    public void shouldDeleteAndRecreateCustomerSuccessfully() {
        // 1. Клик по кнопке Customers
        mainManagerPage.clickBtnCustomers();

        // 2. Удаление пользователя со средней длиной имени
        testCustomerData = customersManagerPage.deleteUserWithAverageNameLength();
        Assert.assertNotNull("No customer was deleted", testCustomerData);

        // 3. Проверка, что пользователь удален
        boolean isCustomerPresent = customersManagerPage.searchCustomerByName(testCustomerData.firstName);
        Assert.assertFalse("Customer should not be present after deletion", isCustomerPresent);
    }

    /**
     * Метод для восстановления состояния после теста.
     * Выполняет следующие действия:
     * 1. Переход на страницу добавления клиента
     * 2. Восстановление удаленного клиента (если он был удален)
     */
    @After
    public void recreateDeletedCustomer() {
        // 4. Клик по кнопке Add Customer
        mainManagerPage.clickBtnAddCustomer();

        // 5. Добавление удаленного пользователя обратно
        if (testCustomerData != null) {
            addingCustomerManagerPage.fillFields(testCustomerData);
            addingCustomerManagerPage.clickAddCustomerButton();
            addingCustomerManagerPage.handleAlert();
        }
    }
}