package com.example.item;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class HomeContent implements Serializable {

    @SerializedName("post_id")
    @Expose
    private String postId;
    @SerializedName("post_type")
    @Expose
    private String postType;
    @SerializedName("post_title")
    @Expose
    private String postTitle;
    @SerializedName("post_image")
    @Expose
    private String postImage;

    @SerializedName("post_access")
    @Expose
    private String post_access;

    @SerializedName("book_on_rent")
    @Expose
    private String book_on_rent;

    @SerializedName("sub_cat_status")
    @Expose
    private String sub_cat_status;


    @SerializedName("book_rent_price")
    @Expose
    private String book_rent_price;

    @SerializedName("book_rent_time")
    @Expose
    private String book_rent_time;

    @SerializedName("total_views")
    @Expose
    private String total_views;

    @SerializedName("total_rate")
    @Expose
    private String total_rate;

    @SerializedName("page_num")
    @Expose
    private String page_num;

    @SerializedName("author_list")
    @Expose
    private List<SubCatListBookAuthor> author_list;

    public String getPost_access() {
        return post_access;
    }

    public void setPost_access(String post_access) {
        this.post_access = post_access;
    }

    public String getBook_on_rent() {
        return book_on_rent;
    }

    public void setBook_on_rent(String book_on_rent) {
        this.book_on_rent = book_on_rent;
    }

    public String getBook_rent_price() {
        return book_rent_price;
    }

    public void setBook_rent_price(String book_rent_price) {
        this.book_rent_price = book_rent_price;
    }

    public String getBook_rent_time() {
        return book_rent_time;
    }

    public void setBook_rent_time(String book_rent_time) {
        this.book_rent_time = book_rent_time;
    }

    public String getTotal_views() {
        return total_views;
    }

    public void setTotal_views(String total_views) {
        this.total_views = total_views;
    }

    public String getTotal_rate() {
        return total_rate;
    }

    public void setTotal_rate(String total_rate) {
        this.total_rate = total_rate;
    }

    public List<SubCatListBookAuthor> getAuthor_list() {
        return author_list;
    }

    public void setAuthor_list(List<SubCatListBookAuthor> author_list) {
        this.author_list = author_list;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getPage_num() {
        return page_num;
    }

    public void setPage_num(String page_num) {
        this.page_num = page_num;
    }

    public String getSub_cat_status() {
        return sub_cat_status;
    }

    public void setSub_cat_status(String sub_cat_status) {
        this.sub_cat_status = sub_cat_status;
    }
}
