package data;

import com.github.javafaker.Faker;
import java.util.Random;

public class NewCustomerData {
    private final Faker faker;
    private final Random random;

    public NewCustomerData() {
        this.faker = new Faker();
        this.random = new Random();
    }

    // Генерация 10-значного Post Code
    public String generatePostCode() {
        StringBuilder postCode = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            postCode.append(random.nextInt(10));
        }
        return postCode.toString();
    }

    // Преобразование цифр в буквы согласно логике
    public String digitsToLetters(String digits) {
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

    // Генерация случайной фамилии
    public String generateLastName() {
        return faker.name().lastName();
    }

    // Генерация всех данных клиента
    public CustomerData generateCustomerData() {
        String postCode = generatePostCode();
        return new CustomerData(
                digitsToLetters(postCode),
                generateLastName(),
                postCode
        );
    }

    // Класс для хранения данных клиента
    public static class CustomerData {
        public final String firstName;
        public final String lastName;
        public final String postCode;

        public CustomerData(String firstName, String lastName, String postCode) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.postCode = postCode;
        }
    }
}