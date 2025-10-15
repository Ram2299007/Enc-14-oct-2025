package com.Appzia.enclosure.Model;

import java.io.Serializable;
import java.util.Objects;

public class group_messageModel2 implements Serializable {
    String uid, message, time, document, dataType, extension, name, phone, miceTiming, micPhoto, createdBy, userName;
    String modelId;
    String receiverUid;
    String docSize;
    String fileName;
    String thumbnail;
    String fileNameThumbnail;
    String caption;
    String currentDate;
    String senderRoom;
    int active;
    String imageWidth;
    String imageHeight;
    String aspectRatio;
    String selectionCount;

    //blank is required for firebase realtime database
    public group_messageModel2() {
    }

    public group_messageModel2(String uid, String message, String time, String document, String dataType, String extension, String name, String phone, String miceTiming, String micPhoto, String createdBy, String userName, String modelId, String receiverUid, String docSize, String fileName, String thumbnail, String fileNameThumbnail,String caption,String currentDate,String senderRoom,int active, String imageWidth, String imageHeight, String aspectRatio, String selectionCount) {
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
        this.senderRoom = senderRoom;
        this.active = active;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.aspectRatio = aspectRatio;
        this.selectionCount = selectionCount;

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

    public String getSenderRoom() {
        return senderRoom;
    }

    public void setSenderRoom(String senderRoom) {
        this.senderRoom = senderRoom;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
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

    public String getSelectionCount() {
        return selectionCount;
    }

    public void setSelectionCount(String selectionCount) {
        this.selectionCount = selectionCount;
    }

    // ✅ DiffUtil साठी equals() + hashCode()
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof group_messageModel2)) return false;
        group_messageModel2 other = (group_messageModel2) obj;

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
                Objects.equals(senderRoom, other.senderRoom) &&
                active == other.active &&
                Objects.equals(imageWidth, other.imageWidth) &&
                Objects.equals(imageHeight, other.imageHeight) &&
                Objects.equals(aspectRatio, other.aspectRatio) &&
                Objects.equals(selectionCount, other.selectionCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid, message, time, document, dataType, extension, name, phone,
                miceTiming, micPhoto, createdBy, userName, modelId, receiverUid, docSize, fileName,
                thumbnail, fileNameThumbnail, caption, currentDate, senderRoom, active,
                imageWidth, imageHeight, aspectRatio, selectionCount);
    }
}
