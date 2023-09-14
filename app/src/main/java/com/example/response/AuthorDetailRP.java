package com.example.response;

import com.example.item.AuthorDetailList;
import com.example.item.BookDetailList;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class AuthorDetailRP implements Serializable {

    @SerializedName("status_code")
    private String status_code;

    @SerializedName("success")
    private String success;

    @SerializedName("EBOOK_APP")
    private List<AuthorDetailList> authorDetailLists;

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


    public List<AuthorDetailList> getAuthorDetailLists() {
        return authorDetailLists;
    }

    public void setAuthorDetailLists(List<AuthorDetailList> authorDetailLists) {
        this.authorDetailLists = authorDetailLists;
    }
}
