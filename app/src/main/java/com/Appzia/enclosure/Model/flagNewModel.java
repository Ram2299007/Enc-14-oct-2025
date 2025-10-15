package com.Appzia.enclosure.Model;

import java.util.ArrayList;

public class flagNewModel {


    String success;
    String error_code;
    String message;

    ArrayList<flagNewModelChild> data = new ArrayList<>();

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

    public ArrayList<flagNewModelChild> getData() {
        return data;
    }

    public void setData(ArrayList<flagNewModelChild> data) {
        this.data = data;
    }

    public flagNewModel(String success, String error_code, String message, ArrayList<flagNewModelChild> data) {
        this.success = success;
        this.error_code = error_code;
        this.message = message;
        this.data = data;
    }
}
