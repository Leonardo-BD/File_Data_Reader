package com.test.solution.app.enumerator;

public enum InputType {

    SELLER      ("001"),
    CUSTOMER    ("002"),
    SALE        ("003");

    private final String id;

    InputType(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
