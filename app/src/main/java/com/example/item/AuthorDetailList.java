package com.example.item;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class AuthorDetailList implements Serializable {

    @SerializedName("author_id")
    private String author_id;

    @SerializedName("author_name")
    private String author_name;

    @SerializedName("author_info")
    private String author_info;

    @SerializedName("author_image")
    private String author_image;

    @SerializedName("facebook_url")
    private String facebook_url;

    @SerializedName("instagram_url")
    private String instagram_url;

    @SerializedName("youtube_url")
    private String youtube_url;

    @SerializedName("website_url")
    private String website_url;

    @SerializedName("author_books")
    private List<BookRelatedList> listAuthorBook;

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

    public String getAuthor_info() {
        return author_info;
    }

    public void setAuthor_info(String author_info) {
        this.author_info = author_info;
    }

    public String getAuthor_image() {
        return author_image;
    }

    public void setAuthor_image(String author_image) {
        this.author_image = author_image;
    }

    public String getFacebook_url() {
        return facebook_url;
    }

    public void setFacebook_url(String facebook_url) {
        this.facebook_url = facebook_url;
    }

    public String getInstagram_url() {
        return instagram_url;
    }

    public void setInstagram_url(String instagram_url) {
        this.instagram_url = instagram_url;
    }

    public String getYoutube_url() {
        return youtube_url;
    }

    public void setYoutube_url(String youtube_url) {
        this.youtube_url = youtube_url;
    }

    public String getWebsite_url() {
        return website_url;
    }

    public void setWebsite_url(String website_url) {
        this.website_url = website_url;
    }

    public List<BookRelatedList> getListAuthorBook() {
        return listAuthorBook;
    }

    public void setListAuthorBook(List<BookRelatedList> listAuthorBook) {
        this.listAuthorBook = listAuthorBook;
    }




}
