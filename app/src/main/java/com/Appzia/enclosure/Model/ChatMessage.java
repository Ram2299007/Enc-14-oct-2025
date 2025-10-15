package com.Appzia.enclosure.Model;

public class ChatMessage {
    public String userName;
    public String message;
    public String profile; // Added profile field (e.g., drawable resource name or image URL)

    public ChatMessage(String userName, String message, String profile) {
        this.userName = userName;
        this.message = message;
        this.profile = profile;
    }
}