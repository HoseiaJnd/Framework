package jnd.validation;

public class ValidationException extends Exception {
    private final ValidationError validationErrors;

    public ValidationException(String message, ValidationError validationErrors) {
        super(message);
        this.validationErrors = validationErrors;
    }

    public ValidationError getValidationErrors() {
        return validationErrors;
    }
} 