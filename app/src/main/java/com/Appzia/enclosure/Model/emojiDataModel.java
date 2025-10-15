package com.Appzia.enclosure.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class emojiDataModel {
    @SerializedName("data")  // Ensure this matches the JSON key
    @Expose
    private ArrayList<Emoji> data;

    public emojiDataModel(ArrayList<Emoji> data) {
        this.data = data;
    }

    public ArrayList<Emoji> getData() {
        return data;
    }

    public void setData(ArrayList<Emoji> data) {
        this.data = data;
    }
}
