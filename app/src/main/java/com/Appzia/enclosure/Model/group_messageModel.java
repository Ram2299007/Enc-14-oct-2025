package com.Appzia.enclosure.Model;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class group_messageModel implements Serializable {
    String uid, message, time, document, dataType, extension, name, phone, miceTiming, micPhoto, createdBy, userName;
    String modelId;
    String receiverUid;
    String docSize;
    String fileName;
    String thumbnail;
    String fileNameThumbnail;
    String caption;
    String currentDate;
    String imageWidth;
    String imageHeight;
    String aspectRatio;
    int active; // 0 = sending, 1 = sent
    String selectionCount;
    private ArrayList<selectionBunchModel> selectionBunch;

    //blank is required for firebase realtime database
    public group_messageModel() {
    }
    
    // Overloaded constructor for backward compatibility
    public group_messageModel(String uid, String message, String time, String document, String dataType, String extension, String name, String phone, String miceTiming, String micPhoto, String createdBy, String userName, String modelId, String receiverUid, String docSize, String fileName, String thumbnail, String fileNameThumbnail,String caption,String currentDate, String imageWidth, String imageHeight, String aspectRatio, String selectionCount) {
        this(uid, message, time, document, dataType, extension, name, phone, miceTiming, micPhoto, createdBy, userName, modelId, receiverUid, docSize, fileName, thumbnail, fileNameThumbnail, caption, currentDate, imageWidth, imageHeight, aspectRatio, selectionCount, null);
    }

    public group_messageModel(String uid, String message, String time, String document, String dataType, String extension, String name, String phone, String miceTiming, String micPhoto, String createdBy, String userName, String modelId, String receiverUid, String docSize, String fileName, String thumbnail, String fileNameThumbnail,String caption,String currentDate, String imageWidth, String imageHeight, String aspectRatio, String selectionCount, ArrayList<selectionBunchModel> selectionBunch) {
        this.uid = uid;
        this.message = message;
        this.time = time;
        this.document = document;
        this.dataType = dataType;
        this.extension = extension;
        this.name = name;
        this.phone = phone;
        this.miceTiming = miceTiming;
        this.micPhoto = micPhoto;
        this.createdBy = createdBy;
        this.userName = userName;
        this.modelId = modelId;
        this.receiverUid = receiverUid;
        this.docSize = docSize;
        this.fileName = fileName;
        this.thumbnail = thumbnail;
        this.fileNameThumbnail = fileNameThumbnail;
        this.caption = caption;
        this.currentDate = currentDate;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.aspectRatio = aspectRatio;
        this.selectionCount = selectionCount;
        this.selectionBunch = selectionBunch;
        this.active = 0; // Default to sending state

    }


    public void setSelectionBunch(ArrayList<selectionBunchModel> selectionBunch) {
        this.selectionBunch = selectionBunch;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
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

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }
    
    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
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
        map.put("createdBy", createdBy);
        map.put("userName", userName);
        map.put("modelId", modelId);
        map.put("receiverUid", receiverUid);
        map.put("docSize", docSize);
        map.put("fileName", fileName);
        map.put("thumbnail", thumbnail);
        map.put("fileNameThumbnail", fileNameThumbnail);
        map.put("caption", caption);
        map.put("currentDate", currentDate);
        map.put("imageWidth", imageWidth);
        map.put("imageHeight", imageHeight);
        map.put("aspectRatio", aspectRatio);
        map.put("active", active);
        map.put("selectionCount", selectionCount);
        // Serialize selectionBunch as a list of maps for reliable Firebase storage
        if (selectionBunch != null && !selectionBunch.isEmpty()) {
            ArrayList<Map<String, Object>> bunchList = new ArrayList<>();
            for (selectionBunchModel item : selectionBunch) {
                if (item != null) {
                    bunchList.add(item.toMap());
                }
            }
            map.put("selectionBunch", bunchList);
            Log.d("SelectionBunch", "toMap(): writing selectionBunch size=" + bunchList.size());
        } else {
            map.put("selectionBunch", null);
            Log.d("SelectionBunch", "toMap(): selectionBunch is null/empty");
        }
        Log.d("SelectionCount", "group_messageModel.toMap(): selectionCount=" + selectionCount);
        Log.d("ImageDimensions", "group_messageModel.toMap(): imageWidth=" + imageWidth + ", imageHeight=" + imageHeight + ", aspectRatio=" + aspectRatio);
        return map;
    }

    // ✅ DiffUtil साठी equals() + hashCode()
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof group_messageModel)) return false;
        group_messageModel other = (group_messageModel) obj;

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
                Objects.equals(createdBy, other.createdBy) &&
                Objects.equals(userName, other.userName) &&
                Objects.equals(modelId, other.modelId) &&
                Objects.equals(receiverUid, other.receiverUid) &&
                Objects.equals(docSize, other.docSize) &&
                Objects.equals(fileName, other.fileName) &&
                Objects.equals(thumbnail, other.thumbnail) &&
                Objects.equals(fileNameThumbnail, other.fileNameThumbnail) &&
                Objects.equals(caption, other.caption) &&
                Objects.equals(currentDate, other.currentDate) &&
                Objects.equals(imageWidth, other.imageWidth) &&
                Objects.equals(imageHeight, other.imageHeight) &&
                Objects.equals(aspectRatio, other.aspectRatio) &&
                active == other.active &&
                Objects.equals(selectionCount, other.selectionCount) &&
                Objects.equals(selectionBunch, other.selectionBunch);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid, message, time, document, dataType, extension, name, phone,
                miceTiming, micPhoto, createdBy, userName, modelId, receiverUid, docSize, fileName,
                thumbnail, fileNameThumbnail, caption, currentDate, imageWidth, imageHeight,
                aspectRatio, active, selectionCount,selectionBunch);
    }

    public static void parseSelectionBunchFromSnapshot(DataSnapshot snapshot,group_messageModel model) {
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
