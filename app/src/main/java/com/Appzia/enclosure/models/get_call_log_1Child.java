package com.Appzia.enclosure.models;

import java.util.ArrayList;

public class get_call_log_1Child {
    String date;
    int sr_nos;

    ArrayList<user_infoModel> user_info;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getSr_nos() {
        return sr_nos;
    }

    public void setSr_nos(int sr_nos) {
        this.sr_nos = sr_nos;
    }

    public ArrayList<user_infoModel> getUser_info() {
        return user_info;
    }

    public void setUser_info(ArrayList<user_infoModel> user_info) {
        this.user_info = user_info;
    }
}
