package com.Appzia.enclosure.Model;

public class flagModel {

    int flagImg;
    String flagTxt,name;

    public flagModel(int flagImg, String flagTxt, String name) {
        this.flagImg = flagImg;
        this.flagTxt = flagTxt;
        this.name = name;
    }

    public int getFlagImg() {
        return flagImg;
    }

    public void setFlagImg(int flagImg) {
        this.flagImg = flagImg;
    }

    public String getFlagTxt() {
        return flagTxt;
    }

    public void setFlagTxt(String flagTxt) {
        this.flagTxt = flagTxt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
