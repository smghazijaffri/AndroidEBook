package com.example.item;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class AppList implements Serializable {

    @SerializedName("app_package_name")
    @Expose
    private String appPackageName;
    @SerializedName("default_language")
    @Expose
    private String defaultLanguage;
    @SerializedName("currency_code")
    @Expose
    private String currencyCode;
    @SerializedName("app_name")
    @Expose
    private String appName;
    @SerializedName("app_email")
    @Expose
    private String appEmail;
    @SerializedName("app_logo")
    @Expose
    private String appLogo;
    @SerializedName("app_company")
    @Expose
    private String appCompany;
    @SerializedName("app_website")
    @Expose
    private String appWebsite;
    @SerializedName("app_contact")
    @Expose
    private String appContact;
    @SerializedName("facebook_link")
    @Expose
    private String facebookLink;
    @SerializedName("twitter_link")
    @Expose
    private String twitterLink;
    @SerializedName("instagram_link")
    @Expose
    private String instagramLink;
    @SerializedName("youtube_link")
    @Expose
    private String youtubeLink;
    @SerializedName("google_play_link")
    @Expose
    private String googlePlayLink;
    @SerializedName("apple_store_link")
    @Expose
    private String appleStoreLink;
    @SerializedName("app_version")
    @Expose
    private String appVersion;
    @SerializedName("app_update_hide_show")
    @Expose
    private String appUpdateHideShow;
    @SerializedName("app_update_version_code")
    @Expose
    private int appUpdateVersionCode;
    @SerializedName("app_update_desc")
    @Expose
    private String appUpdateDesc;
    @SerializedName("app_update_link")
    @Expose
    private String appUpdateLink;
    @SerializedName("app_update_cancel_option")
    @Expose
    private String appUpdateCancelOption;
    @SerializedName("ads_list")
    @Expose
    private List<AdsList> adsList;
    @SerializedName("page_list")
    @Expose
    private List<PageList> pageList;
    public String getAppPackageName() {
        return appPackageName;
    }

    public void setAppPackageName(String appPackageName) {
        this.appPackageName = appPackageName;
    }

    public String getDefaultLanguage() {
        return defaultLanguage;
    }

    public void setDefaultLanguage(String defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppEmail() {
        return appEmail;
    }

    public void setAppEmail(String appEmail) {
        this.appEmail = appEmail;
    }

    public String getAppLogo() {
        return appLogo;
    }

    public void setAppLogo(String appLogo) {
        this.appLogo = appLogo;
    }

    public String getAppCompany() {
        return appCompany;
    }

    public void setAppCompany(String appCompany) {
        this.appCompany = appCompany;
    }

    public String getAppWebsite() {
        return appWebsite;
    }

    public void setAppWebsite(String appWebsite) {
        this.appWebsite = appWebsite;
    }

    public String getAppContact() {
        return appContact;
    }

    public void setAppContact(String appContact) {
        this.appContact = appContact;
    }

    public String getFacebookLink() {
        return facebookLink;
    }

    public void setFacebookLink(String facebookLink) {
        this.facebookLink = facebookLink;
    }

    public String getTwitterLink() {
        return twitterLink;
    }

    public void setTwitterLink(String twitterLink) {
        this.twitterLink = twitterLink;
    }

    public String getInstagramLink() {
        return instagramLink;
    }

    public void setInstagramLink(String instagramLink) {
        this.instagramLink = instagramLink;
    }

    public String getYoutubeLink() {
        return youtubeLink;
    }

    public void setYoutubeLink(String youtubeLink) {
        this.youtubeLink = youtubeLink;
    }

    public String getGooglePlayLink() {
        return googlePlayLink;
    }

    public void setGooglePlayLink(String googlePlayLink) {
        this.googlePlayLink = googlePlayLink;
    }

    public String getAppleStoreLink() {
        return appleStoreLink;
    }

    public void setAppleStoreLink(String appleStoreLink) {
        this.appleStoreLink = appleStoreLink;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getAppUpdateHideShow() {
        return appUpdateHideShow;
    }

    public void setAppUpdateHideShow(String appUpdateHideShow) {
        this.appUpdateHideShow = appUpdateHideShow;
    }

    public int getAppUpdateVersionCode() {
        return appUpdateVersionCode;
    }

    public void setAppUpdateVersionCode(int appUpdateVersionCode) {
        this.appUpdateVersionCode = appUpdateVersionCode;
    }

    public String getAppUpdateDesc() {
        return appUpdateDesc;
    }

    public void setAppUpdateDesc(String appUpdateDesc) {
        this.appUpdateDesc = appUpdateDesc;
    }

    public String getAppUpdateLink() {
        return appUpdateLink;
    }

    public void setAppUpdateLink(String appUpdateLink) {
        this.appUpdateLink = appUpdateLink;
    }

    public String getAppUpdateCancelOption() {
        return appUpdateCancelOption;
    }

    public void setAppUpdateCancelOption(String appUpdateCancelOption) {
        this.appUpdateCancelOption = appUpdateCancelOption;
    }

    public List<AdsList> getAdsList() {
        return adsList;
    }

    public void setAdsList(List<AdsList> adsList) {
        this.adsList = adsList;
    }

    public List<PageList> getPageList() {
        return pageList;
    }

    public void setPageList(List<PageList> pageList) {
        this.pageList = pageList;
    }



}
