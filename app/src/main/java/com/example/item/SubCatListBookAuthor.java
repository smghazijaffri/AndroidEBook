package com.example.item;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SubCatListBookAuthor implements Serializable {

    @SerializedName("author_id")
    private String author_id ;

    @SerializedName("author_name")
    private String author_name ;


    public String getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(String author_id) {
        this.author_id = author_id;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

}
