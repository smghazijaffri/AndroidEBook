package com.example.item;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PostRateList implements Serializable {

    @SerializedName("total_rate")
    private String total_rate ;

    @SerializedName("msg")
    private String msg ;


    public String getTotal_rate() {
        return total_rate;
    }

    public void setTotal_rate(String total_rate) {
        this.total_rate = total_rate;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }




}
