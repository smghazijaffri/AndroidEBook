package com.example.response;

import com.example.item.SubCategoryList;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SubCatRP implements Serializable {

    @SerializedName("status_code")
    private String status_code;

    @SerializedName("total_records")
    private String total_records;

    @SerializedName("success")
    private String success;

    @SerializedName("EBOOK_APP")
    private List<SubCategoryList> subCategoryLists;
    
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

    public List<SubCategoryList> getSubCategoryLists() {
        return subCategoryLists;
    }

    public void setSubCategoryLists(List<SubCategoryList> subCategoryLists) {
        this.subCategoryLists = subCategoryLists;
    }

}
