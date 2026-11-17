package hw8;

/** Ошибка, найденная при проверке одного поля. */
public record ValidationError(String fieldName, String annotationName, String message) {
}
