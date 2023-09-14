package com.example.item;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AuthorList implements Serializable {


    @SerializedName("post_id")
    private String post_id ;

    private boolean isSelected=false;

    @SerializedName("post_title")
    private String post_title ;

    @SerializedName("post_image")
    private String post_image;

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getPost_title() {
        return post_title;
    }

    public void setPost_title(String post_title) {
        this.post_title = post_title;
    }

    public String getPost_image() {
        return post_image;
    }

    public void setPost_image(String post_image) {
        this.post_image = post_image;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
