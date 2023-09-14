package com.example.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class PlanRP implements Serializable {


    @SerializedName("status_code")
    private String status_code;

    @SerializedName("success")
    private String success;

    @SerializedName("EBOOK_APP")
    private List<ItemPlanList> itemPlanLists;

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

    public List<ItemPlanList> getItemPlanLists() {
        return itemPlanLists;
    }

    public void setItemPlanLists(List<ItemPlanList> itemPlanLists) {
        this.itemPlanLists = itemPlanLists;
    }


    public static class ItemPlanList implements Serializable {

        @SerializedName("plan_id")
        String plan_id;

        @SerializedName("plan_name")
        String plan_name;

        @SerializedName("plan_duration")
        String plan_duration;

        @SerializedName("plan_price")
        String plan_price;

        @SerializedName("currency_code")
        String currency_code;

        public String getPlan_id() {
            return plan_id;
        }

        public void setPlan_id(String plan_id) {
            this.plan_id = plan_id;
        }

        public String getPlan_name() {
            return plan_name;
        }

        public void setPlan_name(String plan_name) {
            this.plan_name = plan_name;
        }

        public String getPlan_duration() {
            return plan_duration;
        }

        public void setPlan_duration(String plan_duration) {
            this.plan_duration = plan_duration;
        }

        public String getPlan_price() {
            return plan_price;
        }

        public void setPlan_price(String plan_price) {
            this.plan_price = plan_price;
        }

        public String getCurrency_code() {
            return currency_code;
        }

        public void setCurrency_code(String currency_code) {
            this.currency_code = currency_code;
        }

    }
}
