package com.example.item;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class BookDetailList implements Serializable {

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

    @SerializedName("post_url_type")
    private String post_url_type;

    @SerializedName("post_file_url")
    private String post_file_url;

    @SerializedName("download_enable")
    private String download_enable;

    @SerializedName("book_on_rent")
    private String book_on_rent;

    @SerializedName("book_rent_price")
    private String book_rent_price;

    @SerializedName("book_rent_time")
    private String book_rent_time;

    @SerializedName("user_plan_status")
    private boolean user_plan_status;

    @SerializedName("book_purchased")
    private boolean book_purchased;

    @SerializedName("total_views")
    private String total_views;

    @SerializedName("total_rate")
    private String total_rate;

    @SerializedName("total_reviews")
    private String total_reviews;

    @SerializedName("share_url")
    private String share_url;

    @SerializedName("favourite")
    private boolean favourite;

    @SerializedName("author_list")
    private List<SubCatListBookAuthor> listBookDetailAuthor;

    @SerializedName("related_books")
    private List<BookRelatedList> listRelatedBook;

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

    public String getPost_url_type() {
        return post_url_type;
    }

    public void setPost_url_type(String post_url_type) {
        this.post_url_type = post_url_type;
    }

    public String getPost_file_url() {
        return post_file_url;
    }

    public void setPost_file_url(String post_file_url) {
        this.post_file_url = post_file_url;
    }

    public String getDownload_enable() {
        return download_enable;
    }

    public void setDownload_enable(String download_enable) {
        this.download_enable = download_enable;
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

    public String getTotal_reviews() {
        return total_reviews;
    }

    public void setTotal_reviews(String total_reviews) {
        this.total_reviews = total_reviews;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public List<SubCatListBookAuthor> getListBookDetailAuthor() {
        return listBookDetailAuthor;
    }

    public void setListBookDetailAuthor(List<SubCatListBookAuthor> listBookDetailAuthor) {
        this.listBookDetailAuthor = listBookDetailAuthor;
    }

    public List<BookRelatedList> getListRelatedBook() {
        return listRelatedBook;
    }

    public void setListRelatedBook(List<BookRelatedList> listRelatedBook) {
        this.listRelatedBook = listRelatedBook;
    }

    public boolean isUser_plan_status() {
        return user_plan_status;
    }

    public void setUser_plan_status(boolean user_plan_status) {
        this.user_plan_status = user_plan_status;
    }

    public boolean isBook_purchased() {
        return book_purchased;
    }

    public void setBook_purchased(boolean book_purchased) {
        this.book_purchased = book_purchased;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }
}
