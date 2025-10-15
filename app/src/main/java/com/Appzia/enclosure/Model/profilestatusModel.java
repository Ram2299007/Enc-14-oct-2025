package com.Appzia.enclosure.Model;

public class profilestatusModel {

    String photo,id;

    public profilestatusModel(String photo, String id) {
        this.photo = photo;
        this.id = id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
