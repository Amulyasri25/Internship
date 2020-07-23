package com.amz.internshipproject;


public class Feed {
    private String description,imageLink,userId,name;
    public Feed() {
    }

    public Feed(String description, String imageLink, String userId,String name) {
        this.description = description;
        this.imageLink = imageLink;
        this.userId = userId;
        this.name=name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
