package com.example.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class RegisterRP implements Serializable {

    @SerializedName("status_code")
    private String status_code;

    @SerializedName("success")
    private String success;

    @SerializedName("EBOOK_APP")
    private List<ItemUserRegister> itemUserListRegister;



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

    public List<ItemUserRegister> getItemUserListRegister() {
        return itemUserListRegister;
    }

    public void setItemUserListRegister(List<ItemUserRegister> itemUserListRegister) {
        this.itemUserListRegister = itemUserListRegister;
    }


    public static class ItemUserRegister implements Serializable {


        @SerializedName("success")
        String success;

        @SerializedName("msg")
        String msgReg;

        public String getMsg() {
            return msgReg;
        }

        public void setMsg(String msgReg) {
            this.msgReg = msgReg;
        }

        public String getSuccess() {
            return success;
        }

        public void setSuccess(String success) {
            this.success = success;
        }


    }

}
