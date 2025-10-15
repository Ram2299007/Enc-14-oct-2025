package com.Appzia.enclosure.Model;

public class call_log_history_model {

    String id;
    String uid;
    String kfriend_id;
    String kdate;
    String start_time;
    String end_time;
    String calling_flag;
    String call_type;

    public call_log_history_model(String id, String uid, String kfriend_id, String kdate, String start_time, String end_time, String calling_flag, String call_type) {
        this.id = id;
        this.uid = uid;
        this.kfriend_id = kfriend_id;
        this.kdate = kdate;
        this.start_time = start_time;
        this.end_time = end_time;
        this.calling_flag = calling_flag;
        this.call_type = call_type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getKfriend_id() {
        return kfriend_id;
    }

    public void setKfriend_id(String kfriend_id) {
        this.kfriend_id = kfriend_id;
    }

    public String getKdate() {
        return kdate;
    }

    public void setKdate(String kdate) {
        this.kdate = kdate;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
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
}
