package com.Appzia.enclosure.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class grpFCMModel implements Parcelable {
    String friend_id;
    String f_token;
    String device_type;

    public grpFCMModel(String friend_id, String f_token, String device_type) {
        this.friend_id = friend_id;
        this.f_token = f_token;
        this.device_type = device_type;
    }


    public grpFCMModel(Parcel in) {
        // Read data from Parcel
        friend_id = in.readString();
        f_token = in.readString();
        device_type = in.readString();
    }

    public static final Creator<grpFCMModel> CREATOR = new Creator<grpFCMModel>() {
        @Override
        public grpFCMModel createFromParcel(Parcel in) {
            return new grpFCMModel(in);
        }

        @Override
        public grpFCMModel[] newArray(int size) {
            return new grpFCMModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // Write data to Parcel
        dest.writeString(friend_id);
        dest.writeString(f_token);
        dest.writeString(device_type);
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
