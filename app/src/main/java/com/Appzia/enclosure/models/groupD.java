package com.Appzia.enclosure.models;

import java.util.ArrayList;

public class groupD {
    String  group_id;
    String  group_name;
    String  group_icon;
    ArrayList<members> members;


    public groupD(String group_id, String group_name, String group_icon, ArrayList<com.Appzia.enclosure.models.members> members) {
        this.group_id = group_id;
        this.group_name = group_name;
        this.group_icon = group_icon;
        this.members = members;
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

    public ArrayList<com.Appzia.enclosure.models.members> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<com.Appzia.enclosure.models.members> members) {
        this.members = members;
    }
}
