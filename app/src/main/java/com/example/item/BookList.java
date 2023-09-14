package com.example.item;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BookList implements Serializable {

    @SerializedName("is_ads")
    private boolean is_ads;

    @SerializedName("native_ad_type")
    private String native_ad_type ;

    @SerializedName("native_ad_id")
    private String native_ad_id ;

    @SerializedName("id")
    private String id;

    @SerializedName("book_title")
    private String book_title;

    @SerializedName("book_cover_img")
    private String book_cover_img;

    @SerializedName("book_description")
    private String book_description;

    @SerializedName("book_views")
    private String book_views;

    @SerializedName("total_rate")
    private String total_rate;

    @SerializedName("rate_avg")
    private String rate_avg;

    @SerializedName("author_name")
    private String author_name;

    public boolean isIs_ads() {
        return is_ads;
    }

    public String getNative_ad_type() {
        return native_ad_type;
    }

    public String getNative_ad_id() {
        return native_ad_id;
    }

    public String getId() {
        return id;
    }

    public String getBook_title() {
        return book_title;
    }

    public String getBook_cover_img() {
        return book_cover_img;
    }

    public String getBook_description() {
        return book_description;
    }

    public String getBook_views() {
        return book_views;
    }

    public String getTotal_rate() {
        return total_rate;
    }

    public String getRate_avg() {
        return rate_avg;
    }

    public String getAuthor_name() {
        return author_name;
    }
}
