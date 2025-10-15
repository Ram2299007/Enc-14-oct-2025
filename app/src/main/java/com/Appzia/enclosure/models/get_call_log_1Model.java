package com.Appzia.enclosure.models;

import java.util.ArrayList;

public class get_call_log_1Model {

    String success;
    String error_code;
    String message;

    ArrayList<get_call_log_1Child> data;

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

    public ArrayList<get_call_log_1Child> getData() {
        return data;
    }

    public void setData(ArrayList<get_call_log_1Child> data) {
        this.data = data;
    }
}
