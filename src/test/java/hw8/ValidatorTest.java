package hw8;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidatorTest {

    static class Parent {
        @NotNull(message = "id null")
        private String id = "1";

        @NotNull
        private static String staticIgnored = null;
    }

    static class Person extends Parent {
        @NotNull(message = "name null")
        @Size(min = 2, max = 4, message = "name size")
        private String name;

        @Email(message = "bad email")
        private String email;

        @Range(min = 10, max = 20, message = "bad age")
        private Integer age;

        Person(String id, String name, String email, Integer age) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.age = age;
        }
    }

    @Test
    void validObjectIsValid() {
        Person p = new Person("1", "Ivan", "a@b.c", 15);
        ValidationResult res = Validator.validate(p);
        assertTrue(res.isValid());
        assertTrue(res.getErrors().isEmpty());
    }

    @Test
    void collectsMultipleErrorsAcrossFieldsAndInheritance() {
        Person p = new Person(null, "A", "wrong", 5);
        ValidationResult res = Validator.validate(p);
        assertFalse(res.isValid());
        assertEquals(4, res.getErrors().size());
        assertTrue(res.getErrors().contains(new ValidationError("id", "NotNull", "id null")));
        assertTrue(res.getErrors().contains(new ValidationError("name", "Size", "name size")));
        assertTrue(res.getErrors().contains(new ValidationError("email", "Email", "bad email")));
        assertTrue(res.getErrors().contains(new ValidationError("age", "Range", "bad age")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"ab", "abc", "abcd"})
    void sizeBoundariesPass(String name) {
        Person p = new Person("1", name, "a@b.c", 15);
        assertTrue(Validator.validate(p).isValid());
    }

    @ParameterizedTest
    @ValueSource(strings = {"a", "abcde"})
    void sizeOutsideBoundariesFails(String name) {
        Person p = new Person("1", name, "a@b.c", 15);
        assertFalse(Validator.validate(p).isValid());
    }

    @ParameterizedTest
    @ValueSource(ints = {10, 15, 20})
    void rangeBoundariesPass(int age) {
        Person p = new Person("1", "Ivan", "a@b.c", age);
        assertTrue(Validator.validate(p).isValid());
    }

    @ParameterizedTest
    @ValueSource(ints = {9, 21})
    void rangeOutsideBoundariesFails(int age) {
        Person p = new Person("1", "Ivan", "a@b.c", age);
        assertFalse(Validator.validate(p).isValid());
    }

    @Test
    void exceptionsAndUnmodifiableListWork() {
        assertThrows(NullPointerException.class, () -> Validator.validate(null));

        class BadType {
            @Size(min = 1, max = 2)
            private Integer x = null;
        }
        class BadBounds {
            @Range(min = 10, max = 5)
            private int x = 7;
        }

        assertThrows(ValidationConfigurationException.class, () -> Validator.validate(new BadType()));
        assertThrows(ValidationConfigurationException.class, () -> Validator.validate(new BadBounds()));

        ValidationResult res = Validator.validate(new Person("1", "A", "a@b.c", 15));
        assertThrows(UnsupportedOperationException.class, () -> res.getErrors().clear());
    }
    @Test
    void nullOnlyFailsNotNull() {
        Person p = new Person("1", null, null, null);
        ValidationResult result = Validator.validate(p);
        assertEquals(1, result.getErrors().size());
        assertEquals("NotNull", result.getErrors().get(0).annotationName());
    }

    @Test
    void invalidEmailWithWhitespaceFails() {
        Person p = new Person("1", "Ivan", "a@b.c\n", 15);
        assertFalse(Validator.validate(p).isValid());
    }

    @Test
    void wrongSizeBoundsAndNaNFail() {
        class WrongSize {
            @Size(min = 3, max = 1)
            private String value = null;
        }
        class NanValue {
            @Range(min = 0, max = 10)
            private double number = Double.NaN;
        }
        assertThrows(ValidationConfigurationException.class, () -> Validator.validate(new WrongSize()));
        ValidationResult result = Validator.validate(new NanValue());
        assertEquals(1, result.getErrors().size());
        assertEquals("Range", result.getErrors().get(0).annotationName());
    }

}