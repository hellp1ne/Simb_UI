package tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.*;

/**
 * Тестовый класс для проверки функциональности добавления нового клиента.
 */
public class AddingCustomerTest extends BaseTest {

    /**
     * Тест успешного добавления нового клиента в систему.
     * Шаги выполнения:
     * 1. Переход на страницу добавления клиента
     * 2. Заполнение данных нового клиента
     * 3. Подтверждение добавления
     * 4. Проверка наличия клиента в списке
     */
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

    /**
     * Метод очистки после каждого теста.
     * Выполняет следующие действия:
     * 1. Переход на страницу списка клиентов
     * 2. Поиск созданного клиента
     * 3. Удаление клиента, если он был найден
     * 4. Очистка поля поиска
     */
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