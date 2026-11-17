package hw8;

public class Main {
    static class User {
        @NotNull(message = "Имя обязательно")
        @Size(min = 2, max = 30, message = "Имя слишком короткое или длинное")
        private String name = "A";

        @Email(message = "Неверный email")
        private String email = "invalid-email";

        @Range(min = 0, max = 120, message = "Неверный возраст")
        private int age = 20;
    }

    public static void main(String[] args) {
        ValidationResult result = Validator.validate(new User());
        System.out.println("Успешная проверка: " + result.isValid());
        for (ValidationError error : result.getErrors()) {
            System.out.println(error.fieldName() + ": " + error.message());
        }
    }
}
