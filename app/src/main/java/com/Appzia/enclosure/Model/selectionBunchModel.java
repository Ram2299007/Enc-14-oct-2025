package com.Appzia.enclosure.Model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class selectionBunchModel implements Serializable {
    String imgUrl;
    String fileName;

    public selectionBunchModel() {
    }

    public selectionBunchModel(String imgUrl, String fileName) {
        this.imgUrl = imgUrl;
        this.fileName = fileName;
    }

    // Firebase साठी Map मध्ये convert करणारा method
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("imgUrl", imgUrl);
        map.put("fileName", fileName);
        return map;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
