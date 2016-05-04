package com.epicodus.whatsappening.models;

import org.parceler.Parcel;

/**
 * Created by Guest on 5/4/16.
 */
@Parcel
public class Friend {
    private String name;
    private String email;
    private String uid;

    public Friend() {}

    public Friend(String name, String email, String uid) {
        this.name = name;
        this.email = email;
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUid() {
        return uid;
    }
}
