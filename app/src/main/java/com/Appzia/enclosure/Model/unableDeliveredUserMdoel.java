package com.Appzia.enclosure.Model;

public class unableDeliveredUserMdoel {
    String u_id;
    String full_name;
    String p_img;

    public unableDeliveredUserMdoel(String u_id, String full_name, String p_img) {
        this.u_id = u_id;
        this.full_name = full_name;
        this.p_img = p_img;
    }

    public String getU_id() {
        return u_id;
    }

    public void setU_id(String u_id) {
        this.u_id = u_id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getP_img() {
        return p_img;
    }

    public void setP_img(String p_img) {
        this.p_img = p_img;
    }
}
