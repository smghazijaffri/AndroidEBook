package com.example.item;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class EbookApp implements Serializable {

    @SerializedName("continue_reading")
    @Expose
    private List<HomeContent> continue_reading;


    @SerializedName("trending_books")
    @Expose
    private List<HomeContent> trending_books;

    @SerializedName("featured_books")
    @Expose
    private List<FeaturedBook> featuredBooks;
    @SerializedName("home_sections")
    @Expose
    private List<HomeSection> homeSections;

    public List<FeaturedBook> getFeaturedBooks() {
        return featuredBooks;
    }

    public void setFeaturedBooks(List<FeaturedBook> featuredBooks) {
        this.featuredBooks = featuredBooks;
    }

    public List<HomeSection> getHomeSections() {
        return homeSections;
    }

    public void setHomeSections(List<HomeSection> homeSections) {
        this.homeSections = homeSections;
    }

    public List<HomeContent> getContinue_reading() {
        return continue_reading;
    }

    public void setContinue_reading(List<HomeContent> continue_reading) {
        this.continue_reading = continue_reading;
    }

    public List<HomeContent> getTrending_books() {
        return trending_books;
    }

    public void setTrending_books(List<HomeContent> trending_books) {
        this.trending_books = trending_books;
    }

}
