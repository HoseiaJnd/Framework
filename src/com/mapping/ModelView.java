package com.mapping;

import java.util.HashMap;
import java.util.Map;

public class ModelView {
    private String url;
    private Map<String, Object> data = new HashMap<>();

    public ModelView(String url) {
        this.url = url;
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
}
