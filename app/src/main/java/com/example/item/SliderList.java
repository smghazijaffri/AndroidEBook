package com.example.item;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SliderList implements Serializable {

    @SerializedName("book_id")
    private String book_id;

    @SerializedName("book_type")
    private String book_type;

    @SerializedName("book_title")
    private String book_title;

    @SerializedName("book_cover_img")
    private String book_cover_img;

    @SerializedName("book_bg_img")
    private String book_bg_img;

    @SerializedName("external_link")
    private String external_link;

    @SerializedName("total_rate")
    private String total_rate;

    @SerializedName("rate_avg")
    private String rate_avg;

    @SerializedName("author_name")
    private String author_name;

    public String getBook_id() {
        return book_id;
    }

    public String getBook_type() {
        return book_type;
    }

    public String getBook_title() {
        return book_title;
    }

    public String getBook_cover_img() {
        return book_cover_img;
    }

    public String getBook_bg_img() {
        return book_bg_img;
    }

    public String getExternal_link() {
        return external_link;
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
