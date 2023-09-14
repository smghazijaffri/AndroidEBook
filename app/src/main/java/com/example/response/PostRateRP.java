package com.example.response;

import com.example.item.CategoryList;
import com.example.item.PostRateList;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class PostRateRP implements Serializable {


    @SerializedName("status_code")
    private String status_code;

    @SerializedName("success")
    private String success;

    @SerializedName("EBOOK_APP")
    private List<PostRateList> postRateLists;

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

    public List<PostRateList> getPostRateLists() {
        return postRateLists;
    }

    public void setPostRateLists(List<PostRateList> postRateLists) {
        this.postRateLists = postRateLists;
    }

}
