package com.amz.internshipproject;

public class Post {
    private String  imageUrl,description,uid,selectedType,name;
    public Post() {
    }

    public Post(String imageUrl,String description,String uid,String selectedType) {
        this.imageUrl=imageUrl;
        this.description=description;
        this.uid=uid;
        this.selectedType=selectedType;
        this.name=name;
    }


    public String getSelectedType() {
        return selectedType;
    }

    public void setSelectedType(String selectedType) {
        this.selectedType = selectedType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }



    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}