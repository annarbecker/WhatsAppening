package com.epicodus.whatsappening.models;

/**
 * Created by Guest on 5/4/16.
 */
public class Friend {
    private String name;
    private String email;

    public Friend() {}

    public Friend(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
