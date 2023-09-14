package com.example.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class PayUMoneyHashRP implements Serializable {

    @SerializedName("status_code")
    private String status_code;

    @SerializedName("EBOOK_APP")
    private List<ItemPaypalToken> itemPaypalTokens;

    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public List<ItemPaypalToken> getItemPaypalTokens() {
        return itemPaypalTokens;
    }

    public void setItemPaypalTokens(List<ItemPaypalToken> itemPaypalTokens) {
        this.itemPaypalTokens = itemPaypalTokens;
    }

    public static class ItemPaypalToken implements Serializable {
        @SerializedName("success")
        private String success;

        @SerializedName("msg")
        private String msg;

        @SerializedName("payu_hash")
        private String payu_hash;

        public String getSuccess() {
            return success;
        }

        public void setSuccess(String success) {
            this.success = success;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }


        public String getPayu_hash() {
            return payu_hash;
        }

        public void setPayu_hash(String payu_hash) {
            this.payu_hash = payu_hash;
        }
    }




}
