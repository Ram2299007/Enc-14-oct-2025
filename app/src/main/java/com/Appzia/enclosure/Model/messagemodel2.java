package com.Appzia.enclosure.Model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import com.google.firebase.database.ServerValue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class messagemodel2 implements Parcelable {
    String uid, message, time, document, dataType, extension, name, phone, miceTiming;
    String micPhoto;
    String userName;
    String replytextData;
    String replyKey;
    String replyType;
    String replyOldData;
    String replyCrtPostion;
    String modelId;
    String receiverUid;
    String forwaredKey;
    String groupName;
    String docSize;
    String fileName;
    String thumbnail;
    String fileNameThumbnail;
    String caption;
    int notification;
    String currentDate;
    ArrayList<emojiModel> emojiModel;
    String emojiCount;
    long timestamp;              // 🔹 resolved timestamp (millis)
    Object serverTimestamp;      // 🔹 placeholder for Firebase server timestamp
    int active;
    String imageWidth;
    String imageHeight;
    String aspectRatio;
    String selectionCount;

    public messagemodel2() {
    }

    public messagemodel2(String uid, String message, String time, String document, String dataType, String extension,
                         String name, String phone, String micPhoto, String miceTiming, String userName,
                         String replytextData, String replyKey, String replyType, String replyOldData,
                         String replyCrtPostion, String modelId, String receiverUid, String forwaredKey,
                         String groupName, String docSize, String fileName, String thumbnail,
                         String fileNameThumbnail, String caption, int notification, String currentDate,
                         ArrayList<emojiModel> emojiModel, String emojiCount, long timestamp, int active,
                         String imageWidth, String imageHeight, String aspectRatio, String selectionCount) {
        this.uid = uid;
        this.message = message;
        this.time = time;
        this.document = document;
        this.dataType = dataType;
        this.extension = extension;
        this.name = name;
        this.phone = phone;
        this.micPhoto = micPhoto;
        this.miceTiming = miceTiming;
        this.userName = userName;
        this.replytextData = replytextData;
        this.replyKey = replyKey;
        this.replyType = replyType;
        this.replyOldData = replyOldData;
        this.replyCrtPostion = replyCrtPostion;
        this.modelId = modelId;
        this.receiverUid = receiverUid;
        this.forwaredKey = forwaredKey;
        this.groupName = groupName;
        this.docSize = docSize;
        this.fileName = fileName;
        this.thumbnail = thumbnail;
        this.fileNameThumbnail = fileNameThumbnail;
        this.caption = caption;
        this.notification = notification;
        this.currentDate = currentDate;
        this.emojiModel = emojiModel;
        this.emojiCount = emojiCount;
        this.timestamp = timestamp;
        this.active = active;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.aspectRatio = aspectRatio;
        this.selectionCount = selectionCount;

        // default → set server timestamp
        this.serverTimestamp = ServerValue.TIMESTAMP;
    }

    // 🔹 Convert to Map (useful for Firebase updateChildren)
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("uid", uid);
        map.put("message", message);
        map.put("time", time);
        map.put("document", document);
        map.put("dataType", dataType);
        map.put("extension", extension);
        map.put("name", name);
        map.put("phone", phone);
        map.put("micPhoto", micPhoto);
        map.put("miceTiming", miceTiming);
        map.put("userName", userName);
        map.put("replytextData", replytextData);
        map.put("replyKey", replyKey);
        map.put("replyType", replyType);
        map.put("replyOldData", replyOldData);
        map.put("replyCrtPostion", replyCrtPostion);
        map.put("modelId", modelId);
        map.put("receiverUid", receiverUid);
        map.put("forwaredKey", forwaredKey);
        map.put("groupName", groupName);
        map.put("docSize", docSize);
        map.put("fileName", fileName);
        map.put("thumbnail", thumbnail);
        map.put("fileNameThumbnail", fileNameThumbnail);
        map.put("caption", caption);
        map.put("notification", notification);
        map.put("currentDate", currentDate);
        map.put("emojiModel", emojiModel);
        map.put("emojiCount", emojiCount);
        map.put("timestamp", timestamp);
        map.put("serverTimestamp", serverTimestamp); // ✅ Firebase server timestamp
        map.put("active", active);
        map.put("imageWidth", imageWidth);
        map.put("imageHeight", imageHeight);
        map.put("aspectRatio", aspectRatio);
        map.put("selectionCount", selectionCount);
        Log.d("SelectionCount", "messagemodel2.toMap(): selectionCount=" + selectionCount);
        return map;
    }

    // 🔹 Getters and Setters
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public Object getServerTimestamp() { return serverTimestamp; }
    public void setServerTimestamp(Object serverTimestamp) { this.serverTimestamp = serverTimestamp; }

    public String getImageWidth() { return imageWidth; }
    public void setImageWidth(String imageWidth) { this.imageWidth = imageWidth; }

    public String getImageHeight() { return imageHeight; }
    public void setImageHeight(String imageHeight) { this.imageHeight = imageHeight; }

    public String getAspectRatio() { return aspectRatio; }
    public void setAspectRatio(String aspectRatio) { this.aspectRatio = aspectRatio; }

    public int getActive() { return active; }
    public void setActive(int active) { this.active = active; }

    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getDocument() { return document; }
    public void setDocument(String document) { this.document = document; }

    public String getDataType() { return dataType; }
    public void setDataType(String dataType) { this.dataType = dataType; }

    public String getExtension() { return extension; }
    public void setExtension(String extension) { this.extension = extension; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getMiceTiming() { return miceTiming; }
    public void setMiceTiming(String miceTiming) { this.miceTiming = miceTiming; }

    public String getMicPhoto() { return micPhoto; }
    public void setMicPhoto(String micPhoto) { this.micPhoto = micPhoto; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getReplytextData() { return replytextData; }
    public void setReplytextData(String replytextData) { this.replytextData = replytextData; }

    public String getReplyKey() { return replyKey; }
    public void setReplyKey(String replyKey) { this.replyKey = replyKey; }

    public String getReplyType() { return replyType; }
    public void setReplyType(String replyType) { this.replyType = replyType; }

    public String getReplyOldData() { return replyOldData; }
    public void setReplyOldData(String replyOldData) { this.replyOldData = replyOldData; }

    public String getReplyCrtPostion() { return replyCrtPostion; }
    public void setReplyCrtPostion(String replyCrtPostion) { this.replyCrtPostion = replyCrtPostion; }

    public String getModelId() { return modelId; }
    public void setModelId(String modelId) { this.modelId = modelId; }

    public String getReceiverUid() { return receiverUid; }
    public void setReceiverUid(String receiverUid) { this.receiverUid = receiverUid; }

    public String getForwaredKey() { return forwaredKey; }
    public void setForwaredKey(String forwaredKey) { this.forwaredKey = forwaredKey; }

    public String getGroupName() { return groupName; }
    public void setGroupName(String groupName) { this.groupName = groupName; }

    public String getDocSize() { return docSize; }
    public void setDocSize(String docSize) { this.docSize = docSize; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getThumbnail() { return thumbnail; }
    public void setThumbnail(String thumbnail) { this.thumbnail = thumbnail; }

    public String getFileNameThumbnail() { return fileNameThumbnail; }
    public void setFileNameThumbnail(String fileNameThumbnail) { this.fileNameThumbnail = fileNameThumbnail; }

    public String getCaption() { return caption; }
    public void setCaption(String caption) { this.caption = caption; }

    public int getNotification() { return notification; }
    public void setNotification(int notification) { this.notification = notification; }

    public String getCurrentDate() { return currentDate; }
    public void setCurrentDate(String currentDate) { this.currentDate = currentDate; }

    public ArrayList<emojiModel> getEmojiModel() { return emojiModel; }
    public void setEmojiModel(ArrayList<emojiModel> emojiModel) { this.emojiModel = emojiModel; }

    public String getEmojiCount() { return emojiCount; }
    public void setEmojiCount(String emojiCount) { this.emojiCount = emojiCount; }

    public String getSelectionCount() { return selectionCount; }
    public void setSelectionCount(String selectionCount) { this.selectionCount = selectionCount; }

    // ✅ DiffUtil साठी equals() + hashCode()
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof messagemodel2)) return false;
        messagemodel2 other = (messagemodel2) obj;

        return Objects.equals(uid, other.uid) &&
                Objects.equals(message, other.message) &&
                Objects.equals(time, other.time) &&
                Objects.equals(document, other.document) &&
                Objects.equals(dataType, other.dataType) &&
                Objects.equals(extension, other.extension) &&
                Objects.equals(name, other.name) &&
                Objects.equals(phone, other.phone) &&
                Objects.equals(miceTiming, other.miceTiming) &&
                Objects.equals(micPhoto, other.micPhoto) &&
                Objects.equals(userName, other.userName) &&
                Objects.equals(replytextData, other.replytextData) &&
                Objects.equals(replyKey, other.replyKey) &&
                Objects.equals(replyType, other.replyType) &&
                Objects.equals(replyOldData, other.replyOldData) &&
                Objects.equals(replyCrtPostion, other.replyCrtPostion) &&
                Objects.equals(modelId, other.modelId) &&
                Objects.equals(receiverUid, other.receiverUid) &&
                Objects.equals(forwaredKey, other.forwaredKey) &&
                Objects.equals(groupName, other.groupName) &&
                Objects.equals(docSize, other.docSize) &&
                Objects.equals(fileName, other.fileName) &&
                Objects.equals(thumbnail, other.thumbnail) &&
                Objects.equals(fileNameThumbnail, other.fileNameThumbnail) &&
                Objects.equals(caption, other.caption) &&
                Objects.equals(currentDate, other.currentDate) &&
                Objects.equals(emojiCount, other.emojiCount) &&
                timestamp == other.timestamp &&
                Objects.equals(imageWidth, other.imageWidth) &&
                Objects.equals(imageHeight, other.imageHeight) &&
                Objects.equals(aspectRatio, other.aspectRatio) &&
                active == other.active &&
                Objects.equals(emojiModel, other.emojiModel) &&
                Objects.equals(selectionCount, other.selectionCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid, message, time, document, dataType, extension, name, phone,
                miceTiming, micPhoto, userName, replytextData, replyKey, replyType, replyOldData,
                replyCrtPostion, modelId, receiverUid, forwaredKey, groupName, docSize, fileName,
                thumbnail, fileNameThumbnail, caption, currentDate, emojiCount, timestamp,
                imageWidth, imageHeight, aspectRatio, active, emojiModel, selectionCount);
    }

    // Parcelable implementation
    protected messagemodel2(Parcel in) {
        uid = in.readString();
        message = in.readString();
        time = in.readString();
        document = in.readString();
        dataType = in.readString();
        extension = in.readString();
        name = in.readString();
        phone = in.readString();
        miceTiming = in.readString();
        micPhoto = in.readString();
        userName = in.readString();
        replytextData = in.readString();
        replyKey = in.readString();
        replyType = in.readString();
        replyOldData = in.readString();
        replyCrtPostion = in.readString();
        modelId = in.readString();
        receiverUid = in.readString();
        forwaredKey = in.readString();
        groupName = in.readString();
        docSize = in.readString();
        fileName = in.readString();
        thumbnail = in.readString();
        fileNameThumbnail = in.readString();
        caption = in.readString();
        notification = in.readInt();
        currentDate = in.readString();
        emojiModel = in.createTypedArrayList(com.Appzia.enclosure.Model.emojiModel.CREATOR);
        emojiCount = in.readString();
        timestamp = in.readLong();
        active = in.readInt();
        imageWidth = in.readString();
        imageHeight = in.readString();
        aspectRatio = in.readString();
        selectionCount = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(message);
        dest.writeString(time);
        dest.writeString(document);
        dest.writeString(dataType);
        dest.writeString(extension);
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(miceTiming);
        dest.writeString(micPhoto);
        dest.writeString(userName);
        dest.writeString(replytextData);
        dest.writeString(replyKey);
        dest.writeString(replyType);
        dest.writeString(replyOldData);
        dest.writeString(replyCrtPostion);
        dest.writeString(modelId);
        dest.writeString(receiverUid);
        dest.writeString(forwaredKey);
        dest.writeString(groupName);
        dest.writeString(docSize);
        dest.writeString(fileName);
        dest.writeString(thumbnail);
        dest.writeString(fileNameThumbnail);
        dest.writeString(caption);
        dest.writeInt(notification);
        dest.writeString(currentDate);
        dest.writeTypedList(emojiModel);
        dest.writeString(emojiCount);
        dest.writeLong(timestamp);
        dest.writeInt(active);
        dest.writeString(imageWidth);
        dest.writeString(imageHeight);
        dest.writeString(aspectRatio);
        dest.writeString(selectionCount);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<messagemodel2> CREATOR = new Creator<messagemodel2>() {
        @Override
        public messagemodel2 createFromParcel(Parcel in) {
            return new messagemodel2(in);
        }

        @Override
        public messagemodel2[] newArray(int size) {
            return new messagemodel2[size];
        }
    };
}
