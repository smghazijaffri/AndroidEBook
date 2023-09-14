package com.example.item;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class HomeSection implements Serializable {

    @SerializedName("home_id")
    @Expose
    private String homeId;
    @SerializedName("home_title")
    @Expose
    private String homeTitle;
    @SerializedName("home_type")
    @Expose
    private String homeType;
    @SerializedName("home_content")
    @Expose
    private List<HomeContent> homeContent;

    public String getHomeId() {
        return homeId;
    }

    public void setHomeId(String homeId) {
        this.homeId = homeId;
    }

    public String getHomeTitle() {
        return homeTitle;
    }

    public void setHomeTitle(String homeTitle) {
        this.homeTitle = homeTitle;
    }

    public String getHomeType() {
        return homeType;
    }

    public void setHomeType(String homeType) {
        this.homeType = homeType;
    }

    public List<HomeContent> getHomeContent() {
        return homeContent;
    }

    public void setHomeContent(List<HomeContent> homeContent) {
        this.homeContent = homeContent;
    }
}
