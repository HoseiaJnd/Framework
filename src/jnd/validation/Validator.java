package jnd.validation;

import jnd.annotation.validation.*;
import java.lang.reflect.Field;
import java.util.regex.Pattern;

public class Validator {
    public static ValidationError validate(Object object) {
        ValidationError errors = new ValidationError();
        
        if (object == null) {
            return errors;
        }

        for (Field field : object.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object value = field.get(object);
                
                // Validation Required
                if (field.isAnnotationPresent(Required.class)) {
                    Required required = field.getAnnotation(Required.class);
                    if (value == null || (value instanceof String && ((String) value).trim().isEmpty())) {
                        errors.addError(field.getName(), required.message());
                    }
                }

                if (value != null) {
                    // Validation Min
                    if (field.isAnnotationPresent(Min.class)) {
                        Min min = field.getAnnotation(Min.class);
                        if (value instanceof Number) {
                            double numValue = ((Number) value).doubleValue();
                            if (numValue < min.value()) {
                                errors.addError(field.getName(), min.message().replace("{value}", String.valueOf(min.value())));
                            }
                        }
                    }

                    // Validation Max
                    if (field.isAnnotationPresent(Max.class)) {
                        Max max = field.getAnnotation(Max.class);
                        if (value instanceof Number) {
                            double numValue = ((Number) value).doubleValue();
                            if (numValue > max.value()) {
                                errors.addError(field.getName(), max.message().replace("{value}", String.valueOf(max.value())));
                            }
                        }
                    }

                    // Validation Email
                    if (field.isAnnotationPresent(Email.class)) {
                        Email email = field.getAnnotation(Email.class);
                        if (value instanceof String) {
                            String strValue = (String) value;
                            if (!Pattern.matches(email.pattern(), strValue)) {
                                errors.addError(field.getName(), email.message());
                            }
                        }
                    }
                }
            } catch (IllegalAccessException e) {
                errors.addError(field.getName(), "Erreur lors de la validation: " + e.getMessage());
            }
        }
        
        return errors;
    }
} 