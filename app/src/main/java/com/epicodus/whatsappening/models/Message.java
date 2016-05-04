package com.epicodus.whatsappening.models;

/**
 * Created by Guest on 5/4/16.
 */
public class Message {
    public String body;
    public String sender;
    public String getter;

    public Message() {}

    public Message(String body, String sender, String getter) {
        this.body = body;
        this.sender = sender;
        this.getter = getter;
    }

    public String getGetter() {
        return getter;
    }

    public String getBody() {
        return body;
    }

    public String getSender() {
        return sender;
    }
}
