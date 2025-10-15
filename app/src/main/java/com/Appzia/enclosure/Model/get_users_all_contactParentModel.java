package com.Appzia.enclosure.Model;

import java.util.ArrayList;

public class get_users_all_contactParentModel {
    String success;
    String error_code;
    String message;

    ArrayList<get_users_all_contactChildModel> data = new ArrayList<>();
    ArrayList<get_users_all_contactChild2Model> invite_data = new ArrayList<>();


    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<get_users_all_contactChildModel> getData() {
        return data;
    }

    public void setData(ArrayList<get_users_all_contactChildModel> data) {
        this.data = data;
    }

    public ArrayList<get_users_all_contactChild2Model> getInvite_data() {
        return invite_data;
    }

    public void setInvite_data(ArrayList<get_users_all_contactChild2Model> invite_data) {
        this.invite_data = invite_data;
    }
}
