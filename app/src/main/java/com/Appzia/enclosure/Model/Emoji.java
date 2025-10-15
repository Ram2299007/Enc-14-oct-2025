package com.Appzia.enclosure.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Emoji {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("slug")
    @Expose
    private String slug;

    @SerializedName("character")
    @Expose
    private String character;

    @SerializedName("unicode_name")
    @Expose
    private String unicodeName;

    @SerializedName("code_point")
    @Expose
    private String codePoint;

    @SerializedName("group")
    @Expose
    private String group;

    @SerializedName("sub_group")
    @Expose
    private String subGroup;

    public Emoji( String slug, String character, String unicodeName, String codePoint, String group, String subGroup) {

        this.slug = slug;
        this.character = character;
        this.unicodeName = unicodeName;
        this.codePoint = codePoint;
        this.group = group;
        this.subGroup = subGroup;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getUnicodeName() {
        return unicodeName;
    }

    public void setUnicodeName(String unicodeName) {
        this.unicodeName = unicodeName;
    }

    public String getCodePoint() {
        return codePoint;
    }

    public void setCodePoint(String codePoint) {
        this.codePoint = codePoint;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getSubGroup() {
        return subGroup;
    }

    public void setSubGroup(String subGroup) {
        this.subGroup = subGroup;
    }
}
