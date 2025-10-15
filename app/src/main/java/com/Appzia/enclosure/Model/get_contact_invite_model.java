package com.Appzia.enclosure.Model;

public class get_contact_invite_model {

    String contact_name,contact_number,f_token;


    public get_contact_invite_model(String contact_name, String contact_number, String f_token) {
        this.contact_name = contact_name;
        this.contact_number = contact_number;
        this.f_token = f_token;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public String getF_token() {
        return f_token;
    }

    public void setF_token(String f_token) {
        this.f_token = f_token;
    }
}
