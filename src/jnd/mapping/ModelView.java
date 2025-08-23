package jnd.mapping;

import jnd.validation.ValidationError;
import java.util.HashMap;
import java.util.Map;

public class ModelView {
    private String url;
    private Map<String, Object> data;
    private ValidationError validationErrors;
    private boolean hasValidationErrors;

    public ModelView(String url) {
        this.url = url;
        this.data = new HashMap<>();
        this.validationErrors = new ValidationError();
        this.hasValidationErrors = false;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void addObject(String name, Object value) {
        data.put(name, value);
    }

    public ValidationError getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(ValidationError validationErrors) {
        this.validationErrors = validationErrors;
        this.hasValidationErrors = validationErrors != null && validationErrors.hasErrors();
    }

    public boolean hasValidationErrors() {
        return hasValidationErrors;
    }
}
