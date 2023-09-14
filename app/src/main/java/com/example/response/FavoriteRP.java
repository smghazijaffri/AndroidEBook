package com.example.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class FavoriteRP implements Serializable {

    @SerializedName("status_code")
    private String status_code;

    @SerializedName("success")
    private String success;

    @SerializedName("EBOOK_APP")
    private List<ItemFavorite> itemFavoriteList;


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

    public List<ItemFavorite> getItemFavoriteList() {
        return itemFavoriteList;
    }

    public void setItemFavoriteList(List<ItemFavorite> itemFavoriteList) {
        this.itemFavoriteList = itemFavoriteList;
    }


    public static class ItemFavorite implements Serializable {


        @SerializedName("success")
        String successFav;

        @SerializedName("msg")
        String msgFav;

        public String getSuccessFav() {
            return successFav;
        }

        public void setSuccessFav(String successFav) {
            this.successFav = successFav;
        }

        public String getMsgFav() {
            return msgFav;
        }

        public void setMsgFav(String msgFav) {
            this.msgFav = msgFav;
        }

    }

}
