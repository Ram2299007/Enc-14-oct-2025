package com.Appzia.enclosure.Model;

import java.util.ArrayList;

public class contact_parent_model {

    ArrayList<get_contact_model> get_contact_modelList = new ArrayList<>();
    ArrayList<get_contact_invite_model> get_contact_invite_modelList = new ArrayList<>();

    public contact_parent_model(ArrayList<get_contact_model> get_contact_modelList, ArrayList<get_contact_invite_model> get_contact_invite_modelList) {
        this.get_contact_modelList = get_contact_modelList;
        this.get_contact_invite_modelList = get_contact_invite_modelList;
    }

    public ArrayList<get_contact_model> getGet_contact_modelList() {
        return get_contact_modelList;
    }

    public void setGet_contact_modelList(ArrayList<get_contact_model> get_contact_modelList) {
        this.get_contact_modelList = get_contact_modelList;
    }

    public ArrayList<get_contact_invite_model> getGet_contact_invite_modelList() {
        return get_contact_invite_modelList;
    }

    public void setGet_contact_invite_modelList(ArrayList<get_contact_invite_model> get_contact_invite_modelList) {
        this.get_contact_invite_modelList = get_contact_invite_modelList;
    }
}
