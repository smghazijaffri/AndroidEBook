package com.example.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class EditProfileRP implements Serializable {

    @SerializedName("status_code")
    private String status_code;

    @SerializedName("success")
    private String success;

    @SerializedName("EBOOK_APP")
    private List<ItemUserEdit> itemUserEdits;



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

    public List<ItemUserEdit> getItemUserEdits() {
        return itemUserEdits;
    }

    public void setItemUserEdits(List<ItemUserEdit> itemUserEdits) {
        this.itemUserEdits = itemUserEdits;
    }


    public static class ItemUserEdit implements Serializable {


        @SerializedName("success")
        String success;

        @SerializedName("msg")
        String msgReg;

        @SerializedName("user_image")
        String user_image;

        public String getUser_image() {
            return user_image;
        }

        public void setUser_image(String user_image) {
            this.user_image = user_image;
        }

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
