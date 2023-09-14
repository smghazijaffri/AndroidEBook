package com.example.response;

import com.example.item.AppList;
import com.example.item.CategoryList;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class AppDetailRP implements Serializable {

     @SerializedName("status_code")
    private String status_code;

    @SerializedName("success")
    private String success;

    @SerializedName("user_status")
    private String user_status;

    @SerializedName("EBOOK_APP")
    private List<AppList> appLists;

    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<AppList> getAppLists() {
        return appLists;
    }

    public void setAppLists(List<AppList> appLists) {
        this.appLists = appLists;
    }


    public String getUser_status() {
        return user_status;
    }

    public void setUser_status(String user_status) {
        this.user_status = user_status;
    }
}
