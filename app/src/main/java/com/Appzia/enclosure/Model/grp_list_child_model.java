package com.Appzia.enclosure.Model;

import java.util.ArrayList;

public class grp_list_child_model {

    int sr_nos;
    int dec_flg;
    String group_id;
    String group_name;
    String group_icon;
    String group_created_by;
    String f_token;
    String group_members_count;
    String sent_time;
    String l_msg;
    String data_type;

    ArrayList<grp_list_child2_model> group_members = new ArrayList<>();

    public grp_list_child_model(int sr_nos, String group_id, String group_name, String group_icon, String group_created_by, String f_token, String group_members_count, ArrayList<grp_list_child2_model> group_members,String sent_time,int dec_flg,String l_msg,String data_type) {
        this.sr_nos = sr_nos;
        this.group_id = group_id;
        this.group_name = group_name;
        this.group_icon = group_icon;
        this.group_created_by = group_created_by;
        this.f_token = f_token;
        this.group_members_count = group_members_count;
        this.group_members = group_members;
        this.sent_time = sent_time;
        this.dec_flg = dec_flg;
        this.l_msg = l_msg;
        this.data_type = data_type;
    }

    public String getData_type() {
        return data_type;
    }

    public void setData_type(String data_type) {
        this.data_type = data_type;
    }

    public int getSr_nos() {
        return sr_nos;
    }

    public void setSr_nos(int sr_nos) {
        this.sr_nos = sr_nos;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getGroup_icon() {
        return group_icon;
    }

    public void setGroup_icon(String group_icon) {
        this.group_icon = group_icon;
    }

    public String getGroup_created_by() {
        return group_created_by;
    }

    public void setGroup_created_by(String group_created_by) {
        this.group_created_by = group_created_by;
    }

    public String getF_token() {
        return f_token;
    }

    public void setF_token(String f_token) {
        this.f_token = f_token;
    }

    public String getGroup_members_count() {
        return group_members_count;
    }

    public void setGroup_members_count(String group_members_count) {
        this.group_members_count = group_members_count;
    }

    public ArrayList<grp_list_child2_model> getGroup_members() {
        return group_members;
    }

    public void setGroup_members(ArrayList<grp_list_child2_model> group_members) {
        this.group_members = group_members;
    }

    public String getSent_time() {
        return sent_time;
    }

    public void setSent_time(String sent_time) {
        this.sent_time = sent_time;
    }

    public int getDec_flg() {
        return dec_flg;
    }

    public void setDec_flg(int dec_flg) {
        this.dec_flg = dec_flg;
    }

    public String getL_msg() {
        return l_msg;
    }

    public void setL_msg(String l_msg) {
        this.l_msg = l_msg;
    }
}
