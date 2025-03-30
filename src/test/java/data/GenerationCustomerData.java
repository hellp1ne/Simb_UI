package data;

import com.github.javafaker.Faker;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Класс для генерации тестовых данных нового клиента.
 * Использует JavaFaker для генерации реалистичных данных и Random для создания числовых значений.
 */
public class GenerationCustomerData {
    private final Faker faker;

    /**
     * Конструктор инициализирует генераторы случайных данных.
     */
    public GenerationCustomerData() {
        this.faker = new Faker();
    }

    /**
     * Генерирует 10-значный почтовый код (число от 1000000000 до 9999999999)
     * @return строка, содержащая 10 случайных цифр
     */
    public String generatePostCode() {
        return Long.toString(ThreadLocalRandom.current().nextLong(1_000_000_000L, 10_000_000_000L));
    }

    /**
     * Преобразует последовательность цифр в буквы согласно алгоритму:
     * каждые две цифры преобразуются в одну букву (a-z) на основе их числового значения.
     *
     * @param digits строка цифр для преобразования
     * @return строка, содержащая буквы, полученные из пар цифр
     */
    public String convertDigitsToLetters(String digits) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < digits.length(); i += 2) {
            if (i + 2 > digits.length()) break;
            String twoDigits = digits.substring(i, i + 2);
            int num = Integer.parseInt(twoDigits);
            char c = (char) ('a' + (num % 26));
            result.append(c);
        }
        return result.toString();
    }

    /**
     * Генерирует случайную фамилию с использованием JavaFaker.
     *
     * @return случайно сгенерированная фамилия
     */
    public String generateLastName() {
        return faker.name().lastName();
    }

    /**
     * Генерирует полный набор данных клиента, включая:
     * - Имя (на основе почтового кода)
     * - Фамилию (случайную)
     * - Почтовый код (10 цифр)
     *
     * @return объект CustomerData с заполненными полями
     */
    public CustomerData generateCustomerData() {
        String postCode = generatePostCode();
        return new CustomerData(
                convertDigitsToLetters(postCode),
                generateLastName(),
                postCode
        );
    }

    /**
     * Внутренний класс для хранения данных о клиенте.
     * Содержит имя, фамилию и почтовый код.
     */
    public static class CustomerData {
        /** Имя клиента */
        public final String firstName;
        /** Фамилия клиента */
        public final String lastName;
        /** Почтовый код клиента (10 цифр) */
        public final String postCode;

        /**
         * Конструктор для создания объекта с данными клиента.
         *
         * @param firstName имя клиента
         * @param lastName фамилия клиента
         * @param postCode почтовый код клиента
         */
        public CustomerData(String firstName, String lastName, String postCode) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.postCode = postCode;
        }
    }
}