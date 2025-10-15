package com.Appzia.enclosure.Model;

import java.util.ArrayList;

public class grp_list_model {

    String success;
    String error_code;
    String message;
    ArrayList<grp_list_child_model> data = new ArrayList<>();

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

    public ArrayList<grp_list_child_model> getData() {
        return data;
    }

    public void setData(ArrayList<grp_list_child_model> data) {
        this.data = data;
    }
}
