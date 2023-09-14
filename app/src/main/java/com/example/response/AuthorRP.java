package com.example.response;

import com.example.item.AuthorList;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class AuthorRP implements Serializable {

    @SerializedName("total_records")
    private String total_records;

    @SerializedName("status_code")
    private String status_code;

    @SerializedName("success")
    private String success;

    @SerializedName("EBOOK_APP")
    private List<AuthorList> authorLists;

    public String getTotal_records() {
        return total_records;
    }

    public void setTotal_records(String total_records) {
        this.total_records = total_records;
    }

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

    public List<AuthorList> getAuthorLists() {
        return authorLists;
    }

    public void setAuthorLists(List<AuthorList> authorLists) {
        this.authorLists = authorLists;
    }
}
