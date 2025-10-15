package com.Appzia.enclosure.Model;

import android.app.Activity;
import android.content.Context;

public class incomingVideoCallModel {

    String token;
    String name;
    String meetingId;
    String phone;
    String photo;
    String sampleToken;
    String uid;
    String callerId;
    String invited_friend_list;


    public incomingVideoCallModel() {
    }

    public incomingVideoCallModel(String token, String name, String meetingId, String phone, String photo, String sampleToken, String uid, String callerId, String invited_friend_list) {
        this.token = token;
        this.name = name;
        this.meetingId = meetingId;
        this.phone = phone;
        this.photo = photo;
        this.sampleToken = sampleToken;
        this.uid = uid;
        this.callerId = callerId;
        this.invited_friend_list = invited_friend_list;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getSampleToken() {
        return sampleToken;
    }

    public void setSampleToken(String sampleToken) {
        this.sampleToken = sampleToken;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCallerId() {
        return callerId;
    }

    public void setCallerId(String callerId) {
        this.callerId = callerId;
    }

    public String getInvited_friend_list() {
        return invited_friend_list;
    }

    public void setInvited_friend_list(String invited_friend_list) {
        this.invited_friend_list = invited_friend_list;
    }
}
