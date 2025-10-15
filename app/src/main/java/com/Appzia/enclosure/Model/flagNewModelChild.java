package com.Appzia.enclosure.Model;

public class flagNewModelChild {
    String c_id;
    String country_c_code;
    String country_name;
    String country_code;

    public flagNewModelChild(String c_id, String country_c_code, String country_name, String country_code) {
        this.c_id = c_id;
        this.country_c_code = country_c_code;
        this.country_name = country_name;
        this.country_code = country_code;
    }

    public String getC_id() {
        return c_id;
    }

    public void setC_id(String c_id) {
        this.c_id = c_id;
    }

    public String getCountry_c_code() {
        return country_c_code;
    }

    public void setCountry_c_code(String country_c_code) {
        this.country_c_code = country_c_code;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }
}
