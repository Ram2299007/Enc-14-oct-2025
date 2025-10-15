package com.Appzia.enclosure.Model;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ServerValue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class messageModel implements Serializable {

    private String uid, message, time, document, dataType, extension, name, phone, miceTiming;
    private String micPhoto;
    private String userName, replytextData, replyKey, replyType, replyOldData, replyCrtPostion;
    private String modelId, receiverUid, forwaredKey, groupName, docSize, fileName, thumbnail;
    private String fileNameThumbnail, caption, currentDate, emojiCount;
    long timestamp;
    private String imageWidth, imageHeight, aspectRatio;
    private int notification;
    private ArrayList<emojiModel> emojiModel;

    private String selectionCount;
    private ArrayList<selectionBunchModel> selectionBunch;

    public messageModel() {
    }

    // Overloaded constructor for backward compatibility
    public messageModel(String uid, String message, String time, String document, String dataType,
                        String extension, String name, String phone, String micPhoto, String miceTiming,
                        String userName, String replytextData, String replyKey, String replyType, String replyOldData,
                        String replyCrtPostion, String modelId, String receiverUid, String forwaredKey, String groupName,
                        String docSize, String fileName, String thumbnail, String fileNameThumbnail, String caption,
                        int notification, String currentDate, ArrayList<emojiModel> emojiModel, String emojiCount,
                        long timestamp, String imageWidth, String imageHeight, String aspectRatio, String selectionCount) {
        this(uid, message, time, document, dataType, extension, name, phone, micPhoto, miceTiming,
                userName, replytextData, replyKey, replyType, replyOldData, replyCrtPostion, modelId, receiverUid,
                forwaredKey, groupName, docSize, fileName, thumbnail, fileNameThumbnail, caption, notification,
                currentDate, emojiModel, emojiCount, timestamp, imageWidth, imageHeight, aspectRatio, selectionCount, null);
    }

    public messageModel(String uid, String message, String time, String document, String dataType,
                        String extension, String name, String phone, String micPhoto, String miceTiming,
                        String userName, String replytextData, String replyKey, String replyType, String replyOldData,
                        String replyCrtPostion, String modelId, String receiverUid, String forwaredKey, String groupName,
                        String docSize, String fileName, String thumbnail, String fileNameThumbnail, String caption,
                        int notification, String currentDate, ArrayList<emojiModel> emojiModel, String emojiCount,
                        long timestamp, String imageWidth, String imageHeight, String aspectRatio, String selectionCount, ArrayList<selectionBunchModel> selectionBunch) {

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
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.aspectRatio = aspectRatio;
        this.selectionCount = selectionCount;
        this.selectionBunch = selectionBunch;
    }

    // ✅ Firebase साठी Map मध्ये convert करणारा method
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
        map.put("imageWidth", imageWidth);
        map.put("imageHeight", imageHeight);
        map.put("aspectRatio", aspectRatio);

        // 🚨 Local time ठेवायचाय तर `timestamp` वापर
        // पण Server time हवा असेल तर Firebase लिहिताना override कर
        map.put("timestamp", timestamp);
        map.put("selectionCount", selectionCount);
        
        // Convert selectionBunch to Map format for Firebase
        if (selectionBunch != null && !selectionBunch.isEmpty()) {
            ArrayList<Map<String, Object>> selectionBunchMap = new ArrayList<>();
            for (selectionBunchModel bunch : selectionBunch) {
                selectionBunchMap.add(bunch.toMap());
            }
            map.put("selectionBunch", selectionBunchMap);
            Log.d("SelectionBunch", "messageModel.toMap(): selectionBunch converted to Map with " + selectionBunch.size() + " items");
        } else {
            map.put("selectionBunch", null);
            Log.d("SelectionBunch", "messageModel.toMap(): selectionBunch is null or empty");
        }
        
        Log.d("SelectionCount", "messageModel.toMap(): selectionCount=" + selectionCount);
        Log.d("ImageDimensions", "messageModel.toMap(): imageWidth=" + imageWidth + ", imageHeight=" + imageHeight + ", aspectRatio=" + aspectRatio);

        return map;
    }

    // ✅ DiffUtil साठी equals() + hashCode()
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof messageModel)) return false;
        messageModel other = (messageModel) obj;

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
                notification == other.notification &&
                Objects.equals(emojiModel, other.emojiModel) &&
                Objects.equals(selectionCount, other.selectionCount) &&
                Objects.equals(selectionBunch, other.selectionBunch);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid, message, time, document, dataType, extension, name, phone,
                miceTiming, micPhoto, userName, replytextData, replyKey, replyType, replyOldData,
                replyCrtPostion, modelId, receiverUid, forwaredKey, groupName, docSize, fileName,
                thumbnail, fileNameThumbnail, caption, currentDate, emojiCount, timestamp,
                imageWidth, imageHeight, aspectRatio, notification, emojiModel, selectionCount,selectionBunch);
    }


    // ✅ Getters & Setters
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMiceTiming() {
        return miceTiming;
    }

    public void setMiceTiming(String miceTiming) {
        this.miceTiming = miceTiming;
    }

    public String getMicPhoto() {
        return micPhoto;
    }

    public void setMicPhoto(String micPhoto) {
        this.micPhoto = micPhoto;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getReplytextData() {
        return replytextData;
    }

    public void setReplytextData(String replytextData) {
        this.replytextData = replytextData;
    }

    public String getReplyKey() {
        return replyKey;
    }

    public void setReplyKey(String replyKey) {
        this.replyKey = replyKey;
    }

    public String getReplyType() {
        return replyType;
    }

    public void setReplyType(String replyType) {
        this.replyType = replyType;
    }

    public String getReplyOldData() {
        return replyOldData;
    }

    public void setReplyOldData(String replyOldData) {
        this.replyOldData = replyOldData;
    }

    public String getReplyCrtPostion() {
        return replyCrtPostion;
    }

    public void setReplyCrtPostion(String replyCrtPostion) {
        this.replyCrtPostion = replyCrtPostion;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getReceiverUid() {
        return receiverUid;
    }

    public void setReceiverUid(String receiverUid) {
        this.receiverUid = receiverUid;
    }

    public String getForwaredKey() {
        return forwaredKey;
    }

    public void setForwaredKey(String forwaredKey) {
        this.forwaredKey = forwaredKey;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDocSize() {
        return docSize;
    }

    public void setDocSize(String docSize) {
        this.docSize = docSize;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getFileNameThumbnail() {
        return fileNameThumbnail;
    }

    public void setFileNameThumbnail(String fileNameThumbnail) {
        this.fileNameThumbnail = fileNameThumbnail;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public int getNotification() {
        return notification;
    }

    public void setNotification(int notification) {
        this.notification = notification;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public ArrayList<emojiModel> getEmojiModel() {
        return emojiModel;
    }

    public void setEmojiModel(ArrayList<emojiModel> emojiModel) {
        this.emojiModel = emojiModel;
    }

    public String getEmojiCount() {
        return emojiCount == null ? "" : emojiCount;
    }

    public void setEmojiCount(String emojiCount) {
        this.emojiCount = emojiCount;
    }

    public String getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(String imageWidth) {
        this.imageWidth = imageWidth;
    }

    public String getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(String imageHeight) {
        this.imageHeight = imageHeight;
    }

    public String getAspectRatio() {
        return aspectRatio;
    }

    public void setAspectRatio(String aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public String getSelectionCount() {
        return selectionCount;
    }

    public void setSelectionCount(String selectionCount) {
        this.selectionCount = selectionCount;
    }

    public ArrayList<selectionBunchModel> getSelectionBunch() {
        return selectionBunch;
    }

    public void setSelectionBunch(ArrayList<selectionBunchModel> selectionBunch) {
        this.selectionBunch = selectionBunch;
    }

    // ✅ Custom method to parse selectionBunch from Firebase DataSnapshot
    public static void parseSelectionBunchFromSnapshot(DataSnapshot snapshot, messageModel model) {
        try {
            Log.d("SelectionBunch", "parseSelectionBunchFromSnapshot called for messageId=" + model.getModelId());
            DataSnapshot selectionBunchSnapshot = snapshot.child("selectionBunch");
            Log.d("SelectionBunch", "selectionBunchSnapshot exists: " + selectionBunchSnapshot.exists());
            Log.d("SelectionBunch", "selectionBunchSnapshot children count: " + selectionBunchSnapshot.getChildrenCount());
            
            if (selectionBunchSnapshot.exists()) {
                ArrayList<selectionBunchModel> selectionBunch = new ArrayList<>();
                for (DataSnapshot bunchSnapshot : selectionBunchSnapshot.getChildren()) {
                    Log.d("SelectionBunch", "Processing bunchSnapshot: " + bunchSnapshot.getKey());
                    Log.d("SelectionBunch", "bunchSnapshot value: " + bunchSnapshot.getValue());
                    
                    selectionBunchModel bunch = bunchSnapshot.getValue(selectionBunchModel.class);
                    Log.d("SelectionBunch", "Parsed bunch: " + (bunch != null ? "not null" : "null"));
                    if (bunch != null) {
                        Log.d("SelectionBunch", "Bunch imgUrl: " + bunch.getImgUrl() + ", fileName: " + bunch.getFileName());
                        selectionBunch.add(bunch);
                    }
                }
                model.setSelectionBunch(selectionBunch);
                Log.d("SelectionBunch", "Parsed selectionBunch from Firebase: " + selectionBunch.size() + " items for messageId=" + model.getModelId());
            } else {
                Log.d("SelectionBunch", "No selectionBunch data found in Firebase for messageId=" + model.getModelId());
                // Don't clear existing selectionBunch if it's not empty (for pending uploads)
                if (model.getSelectionBunch() == null || model.getSelectionBunch().isEmpty()) {
                    model.setSelectionBunch(new ArrayList<>());
                } else {
                    Log.d("SelectionBunch", "Preserving existing selectionBunch with " + model.getSelectionBunch().size() + " items for pending upload");
                }
            }
        } catch (Exception e) {
            Log.e("SelectionBunch", "Error parsing selectionBunch from Firebase: " + e.getMessage(), e);
            model.setSelectionBunch(new ArrayList<>());
        }
    }
}
