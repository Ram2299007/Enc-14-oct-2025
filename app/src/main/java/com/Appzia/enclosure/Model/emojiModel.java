package com.Appzia.enclosure.Model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Objects;

public class emojiModel implements Parcelable {
    private String name;
    private String emoji;

    // Default constructor
    public emojiModel() {
    }

    // Constructor with parameters
    public emojiModel(String name, String emoji) {
        this.name = name;
        this.emoji = emoji;
    }

    // Getter for name
    public String getName() {
        return name;
    }

    // Setter for name
    public void setName(String name) {
        this.name = name;
    }

    // Getter for emoji
    public String getEmoji() {
        return emoji;
    }

    // Setter for emoji
    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    // equals() override: compares name and emoji
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof emojiModel)) return false;
        emojiModel that = (emojiModel) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(emoji, that.emoji);
    }

    // hashCode() override: includes name and emoji
    @Override
    public int hashCode() {
        return Objects.hash(name, emoji);
    }

    // Optional: for easy logging/debugging
    @Override
    public String toString() {
        return "emojiModel{" +
                "name='" + name + '\'' +
                ", emoji='" + emoji + '\'' +
                '}';
    }

    // Parcelable implementation
    protected emojiModel(Parcel in) {
        name = in.readString();
        emoji = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(emoji);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<emojiModel> CREATOR = new Creator<emojiModel>() {
        @Override
        public emojiModel createFromParcel(Parcel in) {
            return new emojiModel(in);
        }

        @Override
        public emojiModel[] newArray(int size) {
            return new emojiModel[size];
        }
    };
}
