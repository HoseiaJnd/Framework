package jnd.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValidationError {
    private Map<String, List<String>> errors;

    public ValidationError() {
        this.errors = new HashMap<>();
    }

    public void addError(String field, String message) {
        if (!errors.containsKey(field)) {
            errors.put(field, new ArrayList<>());
        }
        errors.get(field).add(message);
    }

    public Map<String, List<String>> getErrors() {
        return errors;
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public List<String> getFieldErrors(String field) {
        return errors.getOrDefault(field, new ArrayList<>());
    }

    public void clear() {
        errors.clear();
    }
} 