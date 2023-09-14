package com.example.response;

import com.example.item.EbookApp;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class HomeRP implements Serializable {

    @SerializedName("EBOOK_APP")
    @Expose
    private EbookApp ebookApp;
    @SerializedName("status_code")
    @Expose
    private Integer statusCode;
    @SerializedName("success")
    @Expose
    private Integer success;

    public EbookApp getEbookApp() {
        return ebookApp;
    }

    public void setEbookApp(EbookApp ebookApp) {
        this.ebookApp = ebookApp;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }
}
