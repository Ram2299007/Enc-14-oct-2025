package com.Appzia.enclosure.models;

public class get_group_detailsResponseModel {
    String success;
    String error_code;
    String message;

    groupD data;

    public get_group_detailsResponseModel(String success, String error_code, String message, groupD data) {
        this.success = success;
        this.error_code = error_code;
        this.message = message;
        this.data = data;
    }

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

    public groupD getData() {
        return data;
    }

    public void setData(groupD data) {
        this.data = data;
    }
}
