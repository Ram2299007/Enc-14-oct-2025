package com.Appzia.enclosure.Model;

public class profileDBModel {

    String full_name;
    String caption;
    String mobile_no;
    String photo;
    String f_token;


    public profileDBModel(String full_name, String caption, String mobile_no, String photo, String f_token) {
        this.full_name = full_name;
        this.caption = caption;
        this.mobile_no = mobile_no;
        this.photo = photo;
        this.f_token = f_token;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getF_token() {
        return f_token;
    }

    public void setF_token(String f_token) {
        this.f_token = f_token;
    }
}
