package com.Appzia.enclosure.Model;

import org.json.JSONObject;

public class callingUserInfoChildModel {


    String friend_id;
    String photo;
    String full_name;
    String f_token;

    String end_time;
    String calling_flag;
    String call_type;
    String mobile_number;
    String start_time;

    String date;
    String device_type;

    public callingUserInfoChildModel(String friend_id, String photo, String full_name, String f_token, String end_time, String calling_flag, String call_type, String mobile_number, String start_time, String date,String device_type) {
        this.friend_id = friend_id;
        this.photo = photo;
        this.full_name = full_name;
        this.f_token = f_token;
        this.end_time = end_time;
        this.calling_flag = calling_flag;
        this.call_type = call_type;
        this.mobile_number = mobile_number;
        this.start_time = start_time;
        this.date = date;
        this.device_type = device_type;
    }


    public String getFriend_id() {
        return friend_id;
    }

    public void setFriend_id(String friend_id) {
        this.friend_id = friend_id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getF_token() {
        return f_token;
    }

    public void setF_token(String f_token) {
        this.f_token = f_token;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getCalling_flag() {
        return calling_flag;
    }

    public void setCalling_flag(String calling_flag) {
        this.calling_flag = calling_flag;
    }

    public String getCall_type() {
        return call_type;
    }

    public void setCall_type(String call_type) {
        this.call_type = call_type;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }
}
