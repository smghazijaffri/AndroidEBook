package com.example.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class PaymentSuccessRP implements Serializable {

    @SerializedName("status_code")
    private String status_code;

    @SerializedName("success")
    private String success;

    @SerializedName("EBOOK_APP")
    private List<ItemSuccess> itemSuccesses;

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

    public List<ItemSuccess> getItemSuccesses() {
        return itemSuccesses;
    }

    public void setItemSuccesses(List<ItemSuccess> itemSuccesses) {
        this.itemSuccesses = itemSuccesses;
    }


    public static class ItemSuccess implements Serializable {

        @SerializedName("msg")
        String msg;

        @SerializedName("success")
        String success;


        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getSuccess() {
            return success;
        }

        public void setSuccess(String success) {
            this.success = success;
        }



    }
}
