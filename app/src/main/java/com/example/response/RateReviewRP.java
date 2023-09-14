package com.example.response;

import com.example.item.AuthorList;
import com.example.item.RateReviewList;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class RateReviewRP implements Serializable {


    @SerializedName("status_code")
    private String status_code;

    @SerializedName("success")
    private String success;

    @SerializedName("EBOOK_APP")
    private List<RateReviewList> rateReviewLists;

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


    public List<RateReviewList> getRateReviewLists() {
        return rateReviewLists;
    }

    public void setRateReviewLists(List<RateReviewList> rateReviewLists) {
        this.rateReviewLists = rateReviewLists;
    }
}
