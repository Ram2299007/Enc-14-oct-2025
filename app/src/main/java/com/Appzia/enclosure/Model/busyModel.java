package com.Appzia.enclosure.Model;

public class busyModel {
    String status;
    String room;

    public busyModel(){}


    public busyModel(String status, String room) {
        this.status = status;
        this.room = room;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}
