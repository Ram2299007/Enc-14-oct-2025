package com.Appzia.enclosure.Model;

public class Comment {
    private String user;
    private String text;

    public Comment(String user, String text) {
        this.user = user;
        this.text = text;
    }

    public String getUser() {
        return user;
    }

    public String getText() {
        return text;
    }
}