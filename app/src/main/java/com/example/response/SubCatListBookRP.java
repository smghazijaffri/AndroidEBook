package com.example.response;

import com.example.item.SubCatListBook;
import com.example.item.SubCategoryList;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SubCatListBookRP implements Serializable {

    @SerializedName("status_code")
    private String status_code;

    @SerializedName("total_records")
    private String total_records;

    @SerializedName("success")
    private String success;

    @SerializedName("EBOOK_APP")
    private List<SubCatListBook> subCatListBooks;

    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public String getTotal_records() {
        return total_records;
    }

    public void setTotal_records(String total_records) {
        this.total_records = total_records;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }


    public List<SubCatListBook> getSubCatListBooks() {
        return subCatListBooks;
    }

    public void setSubCatListBooks(List<SubCatListBook> subCatListBooks) {
        this.subCatListBooks = subCatListBooks;
    }
}
