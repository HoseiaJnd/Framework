package com.mapping;

import jakarta.servlet.http.HttpSession;
import java.util.Enumeration;

public class MySession {
    private HttpSession session;

    public MySession(HttpSession session) {
        this.session = session;
    }

    public Object get(String key) {
        return session.getAttribute(key);
    }

    public void add(String key, Object value) {
        session.setAttribute(key, value);
    }

    public void delete(String key) {
        session.removeAttribute(key);
    }

    public Enumeration<String> getAttributeNames() {
        return session.getAttributeNames();
    }
}
