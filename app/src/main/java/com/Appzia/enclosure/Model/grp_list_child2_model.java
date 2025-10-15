package com.Appzia.enclosure.Model;

public class grp_list_child2_model {
    String friend_id;
    String f_token;
    String device_type;

    public grp_list_child2_model(String friend_id, String f_token, String device_type) {
        this.friend_id = friend_id;
        this.f_token = f_token;
        this.device_type = device_type;
    }

    public String getF_token() {
        return f_token;
    }

    public void setF_token(String f_token) {
        this.f_token = f_token;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }


    public String getFriend_id() {
        return friend_id;
    }

    public void setFriend_id(String friend_id) {
        this.friend_id = friend_id;
    }
}
