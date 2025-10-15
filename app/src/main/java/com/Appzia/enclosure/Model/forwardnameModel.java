package com.Appzia.enclosure.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class forwardnameModel implements Parcelable {

    String name;
    String friend_id;
    String f_token;
    String device_type;

    public forwardnameModel(String name, String friend_id, String f_token,String device_type) {
        this.name = name;
        this.friend_id = friend_id;
        this.f_token = f_token;
        this.device_type = device_type;
    }

    protected forwardnameModel(Parcel in) {
        name = in.readString();
        friend_id = in.readString();
        f_token = in.readString();
        device_type = in.readString();
    }

    public static final Creator<forwardnameModel> CREATOR = new Creator<forwardnameModel>() {
        @Override
        public forwardnameModel createFromParcel(Parcel in) {
            return new forwardnameModel(in);
        }

        @Override
        public forwardnameModel[] newArray(int size) {
            return new forwardnameModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(friend_id);
        dest.writeString(f_token);
        dest.writeString(device_type);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFriend_id() {
        return friend_id;
    }

    public void setFriend_id(String friend_id) {
        this.friend_id = friend_id;
    }

    public String getF_token() {
        return f_token;
    }

    public void setF_token(String f_token) {
        this.f_token = f_token;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }
}
