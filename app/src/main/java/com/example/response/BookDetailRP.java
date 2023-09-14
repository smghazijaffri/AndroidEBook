package com.example.response;

import com.example.item.BookDetailList;
import com.example.item.SubCategoryList;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class BookDetailRP implements Serializable {

    @SerializedName("status_code")
    private String status_code;

    @SerializedName("success")
    private String success;

    @SerializedName("EBOOK_APP")
    private List<BookDetailList> bookDetailLists;

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

    public List<BookDetailList> getBookDetailLists() {
        return bookDetailLists;
    }

    public void setBookDetailLists(List<BookDetailList> bookDetailLists) {
        this.bookDetailLists = bookDetailLists;
    }


}
