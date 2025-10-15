package com.Appzia.enclosure.Model;

public class BlockedUserModel {
    private String uid;
    private String fullName;
    private String photo;

    public BlockedUserModel(String uid, String fullName, String photo) {
        this.uid = uid;
        this.fullName = fullName;
        this.photo = photo;
    }

    public String getUid() {
        return uid;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhoto() {
        return photo;
    }
}
