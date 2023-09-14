package com.example.item;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AdsList implements Serializable {

    @SerializedName("ad_id")
    @Expose
    private String adId;
    @SerializedName("ads_name")
    @Expose
    private String adsName;
    @SerializedName("ads_info")
    @Expose
    private AdsInfo adsInfo;
    @SerializedName("status")
    @Expose
    private String status;

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getAdsName() {
        return adsName;
    }

    public void setAdsName(String adsName) {
        this.adsName = adsName;
    }

    public AdsInfo getAdsInfo() {
        return adsInfo;
    }

    public void setAdsInfo(AdsInfo adsInfo) {
        this.adsInfo = adsInfo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
