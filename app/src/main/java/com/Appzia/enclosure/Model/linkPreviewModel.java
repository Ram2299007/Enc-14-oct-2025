package com.Appzia.enclosure.Model;

public class linkPreviewModel {

        String modelId;
        String url;
        String title;
        String description;
        String favIcon;
        String image_url;

    public linkPreviewModel(String modelId, String url, String title, String description, String favIcon, String image_url) {
        this.modelId = modelId;
        this.url = url;
        this.title = title;
        this.description = description;
        this.favIcon = favIcon;
        this.image_url = image_url;
    }


    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFavIcon() {
        return favIcon;
    }

    public void setFavIcon(String favIcon) {
        this.favIcon = favIcon;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
