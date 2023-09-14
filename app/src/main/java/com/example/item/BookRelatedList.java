package com.example.item;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class BookRelatedList implements Serializable {

    @SerializedName("post_id")
    private String post_id;

    @SerializedName("cat_id")
    private String cat_id;

    @SerializedName("sub_cat_id")
    private String sub_cat_id;

    @SerializedName("post_access")
    private String post_access;

    @SerializedName("post_title")
    private String post_title;

    @SerializedName("post_description")
    private String post_description;

    @SerializedName("post_image")
    private String post_image;

    @SerializedName("book_on_rent")
    private String book_on_rent;

    @SerializedName("book_rent_price")
    private String book_rent_price;

    @SerializedName("book_rent_time")
    private String book_rent_time;

    @SerializedName("total_views")
    private String total_views;

    @SerializedName("total_rate")
    private String total_rate;

    @SerializedName("favourite")
    private boolean favourite;

    @SerializedName("author_list")
    private List<SubCatListBookAuthor> listRelatedAuthor;


    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getSub_cat_id() {
        return sub_cat_id;
    }

    public void setSub_cat_id(String sub_cat_id) {
        this.sub_cat_id = sub_cat_id;
    }

    public String getPost_access() {
        return post_access;
    }

    public void setPost_access(String post_access) {
        this.post_access = post_access;
    }

    public String getPost_title() {
        return post_title;
    }

    public void setPost_title(String post_title) {
        this.post_title = post_title;
    }

    public String getPost_description() {
        return post_description;
    }

    public void setPost_description(String post_description) {
        this.post_description = post_description;
    }

    public String getPost_image() {
        return post_image;
    }

    public void setPost_image(String post_image) {
        this.post_image = post_image;
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

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public List<SubCatListBookAuthor> getListRelatedAuthor() {
        return listRelatedAuthor;
    }

    public void setListRelatedAuthor(List<SubCatListBookAuthor> listRelatedAuthor) {
        this.listRelatedAuthor = listRelatedAuthor;
    }


 }
