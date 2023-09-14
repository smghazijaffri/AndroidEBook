package com.example.paymentmethod;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;

import com.example.androidebookapps.R;
import com.example.androidebookapps.SuccessActivity;
import com.example.response.PaymentSuccessRP;
import com.example.rest.ApiClient;
import com.example.rest.ApiInterface;
import com.example.util.API;
import com.example.util.Method;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Transaction {
    ProgressDialog pDialog;
    Activity mContext;
    Method method;

    public Transaction(Activity context) {
        this.mContext = context;
        pDialog = new ProgressDialog(mContext,R.style.MyAlertDialogStyle);
        method = new Method(mContext);
    }

    public void purchasedItem(String planId, String userId, String paymentId, String paymentGateway, boolean isRent) {
        pDialog.show();
        pDialog.setMessage(mContext.getResources().getString(R.string.loading));
        pDialog.setCancelable(false);

        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(mContext));
        jsObj.addProperty(isRent ? "rent_id" : "plan_id", planId);
        jsObj.addProperty("user_id", userId);
        jsObj.addProperty("payment_id", paymentId);
        jsObj.addProperty("payment_gateway", paymentGateway);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<PaymentSuccessRP> call = apiService.getPaymentSuccessData(isRent ? "transaction_rent_add" : "transaction_add", API.toBase64(jsObj.toString()));
        call.enqueue(new Callback<PaymentSuccessRP>() {
            @Override
            public void onResponse(@NotNull Call<PaymentSuccessRP> call, @NotNull Response<PaymentSuccessRP> response) {
                try {

                    PaymentSuccessRP paymentSuccessRP = response.body();

                    if (paymentSuccessRP !=null && paymentSuccessRP.getSuccess().equals("1")) {
                        Intent intent = new Intent(mContext, SuccessActivity.class);
                        intent.putExtra("MSG", paymentSuccessRP.getItemSuccesses().get(0).getMsg());
                        mContext.startActivity(intent);
                        mContext.finishAffinity();
                    } else {
                        method.alertBox(paymentSuccessRP.getItemSuccesses().get(0).getMsg());
                    }

                } catch (Exception e) {
                    Log.d("exception_error", e.toString());
                    method.alertBox(mContext.getResources().getString(R.string.failed_try_again));
                }

                pDialog.dismiss();

            }

            @Override
            public void onFailure(@NotNull Call<PaymentSuccessRP> call, @NotNull Throwable t) {
                // Log error here since request failed
                Log.e("fail", t.toString());
                pDialog.dismiss();
                method.alertBox(mContext.getResources().getString(R.string.failed_try_again));
            }
        });
    }
}
