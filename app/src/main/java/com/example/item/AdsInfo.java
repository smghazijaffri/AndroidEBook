package com.example.item;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AdsInfo {

    @SerializedName("publisher_id")
    @Expose
    private String publisherId;
    @SerializedName("banner_on_off")
    @Expose
    private String bannerOnOff;
    @SerializedName("banner_id")
    @Expose
    private String bannerId;
    @SerializedName("interstitial_on_off")
    @Expose
    private String interstitialOnOff;
    @SerializedName("interstitial_id")
    @Expose
    private String interstitialId;
    @SerializedName("interstitial_clicks")
    @Expose
    private int interstitialClicks;
    @SerializedName("native_on_off")
    @Expose
    private String nativeOnOff;
    @SerializedName("native_id")
    @Expose
    private String nativeId;
    @SerializedName("native_position")
    @Expose
    private int nativePosition;

    public String getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }

    public String getBannerOnOff() {
        return bannerOnOff;
    }

    public void setBannerOnOff(String bannerOnOff) {
        this.bannerOnOff = bannerOnOff;
    }

    public String getBannerId() {
        return bannerId;
    }

    public void setBannerId(String bannerId) {
        this.bannerId = bannerId;
    }

    public String getInterstitialOnOff() {
        return interstitialOnOff;
    }

    public void setInterstitialOnOff(String interstitialOnOff) {
        this.interstitialOnOff = interstitialOnOff;
    }

    public String getInterstitialId() {
        return interstitialId;
    }

    public void setInterstitialId(String interstitialId) {
        this.interstitialId = interstitialId;
    }

    public int getInterstitialClicks() {
        return interstitialClicks;
    }

    public void setInterstitialClicks(int interstitialClicks) {
        this.interstitialClicks = interstitialClicks;
    }

    public String getNativeOnOff() {
        return nativeOnOff;
    }

    public void setNativeOnOff(String nativeOnOff) {
        this.nativeOnOff = nativeOnOff;
    }

    public String getNativeId() {
        return nativeId;
    }

    public void setNativeId(String nativeId) {
        this.nativeId = nativeId;
    }

    public int getNativePosition() {
        return nativePosition;
    }

    public void setNativePosition(int nativePosition) {
        this.nativePosition = nativePosition;
    }


}
