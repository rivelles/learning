package org.rivelles.cardefect.domain;

public abstract class User {
    private final String id;
    private String name;

    protected User(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
