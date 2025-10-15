package com.Appzia.enclosure.Model;

import java.util.ArrayList;

public class callloglistModel {
    String date;
    ArrayList<callingUserInfoChildModel> list1 = new ArrayList<>();

    public callloglistModel(String date, ArrayList<callingUserInfoChildModel> list1) {
        this.date = date;
        this.list1 = list1;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<callingUserInfoChildModel> getList1() {
        return list1;
    }

    public void setList1(ArrayList<callingUserInfoChildModel> list1) {
        this.list1 = list1;
    }
}
