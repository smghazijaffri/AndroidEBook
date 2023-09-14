package com.example.paymentmethod;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidebookapps.R;
import com.example.androidebookapps.databinding.ActivityStripePaymentBinding;
import com.example.response.StripeCheckOutRP;
import com.example.rest.ApiClient;
import com.example.rest.ApiInterface;
import com.example.util.API;
import com.example.util.Method;
import com.example.util.StatusBar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;
import com.stripe.android.paymentsheet.PaymentSheetResultCallback;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class StripeActivity extends AppCompatActivity {

    String planId, planPrice, planCurrency, planGateway, stripePublisherKey;
    ProgressDialog pDialog;
    private String paymentToken = "";
    private PaymentSheet paymentSheet;
    private String customerId;
    private String ephemeralKeySecret;
    private String paymentId;
    Method method;
    ActivityStripePaymentBinding stripePaymentBinding;
    boolean isRent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.initWhite(StripeActivity.this);
        stripePaymentBinding = ActivityStripePaymentBinding.inflate(getLayoutInflater());
        setContentView(stripePaymentBinding.getRoot());
        method = new Method(StripeActivity.this);

        stripePaymentBinding.toolbarMain.tvToolbarTitle.setText(getString(R.string.payment_stripe));
        stripePaymentBinding.toolbarMain.imageArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        pDialog = new ProgressDialog(this,R.style.MyAlertDialogStyle);

        Intent intent = getIntent();
        planId = intent.getStringExtra("planId");
        planPrice = intent.getStringExtra("planPrice");
        planCurrency = intent.getStringExtra("planCurrency");
        planGateway = intent.getStringExtra("planGateway");
        stripePublisherKey = intent.getStringExtra("stripePublisherKey");
        if (intent.hasExtra("isRent")){
            isRent=intent.getBooleanExtra("isRent",false);
        }

        PaymentConfiguration.init(this, stripePublisherKey);
        paymentSheet = new PaymentSheet((ComponentActivity) StripeActivity.this, new PaymentSheetResultCallback() {
            @Override
            public void onPaymentSheetResult(@NotNull PaymentSheetResult paymentSheetResult) {
                onPaymentSheetResult1(paymentSheetResult);
            }
        });


        stripePaymentBinding.btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (method.isNetworkAvailable()) {
                    getToken();
                } else {
                    Toast.makeText(StripeActivity.this, getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void presentPaymentSheet() {
        paymentSheet.presentWithPaymentIntent(
                paymentToken,
                new PaymentSheet.Configuration(
                        getString(R.string.app_name),
                        new PaymentSheet.CustomerConfiguration(
                                customerId,
                                ephemeralKeySecret
                        )
                )
        );

    }

    private void onPaymentSheetResult1(final PaymentSheetResult paymentSheetResult) {
        if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
            showError(getString(R.string.paypal_payment_error_2));
        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            showError(getString(R.string.paypal_payment_error_1));
        } else if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            if (method.isNetworkAvailable()) {
                new Transaction(StripeActivity.this)
                        .purchasedItem(planId, method.getUserId(), paymentId, planGateway,isRent);
            } else {
                showError(StripeActivity.this.getString(R.string.internet_connection));
            }
        }
    }

    private void showError(String Title) {
        new AlertDialog.Builder(StripeActivity.this)
                .setTitle(getString(R.string.stripe_payment_error_1))
                .setMessage(Title)
                .setIcon(R.mipmap.ic_launcher)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .show();
    }

    private void getToken() {
        pDialog.show();
        pDialog.setMessage(getResources().getString(R.string.loading));
        pDialog.setCancelable(false);

        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(StripeActivity.this));
        jsObj.addProperty("amount", planPrice);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<StripeCheckOutRP> call = apiService.getStripeTokenData(API.toBase64(jsObj.toString()));
        call.enqueue(new Callback<StripeCheckOutRP>() {
            @Override
            public void onResponse(@NotNull Call<StripeCheckOutRP> call, @NotNull Response<StripeCheckOutRP> response) {
                try {
                    StripeCheckOutRP paymentCheckOutRP = response.body();

                     if (paymentCheckOutRP !=null && paymentCheckOutRP.getItemPaymentCheckOuts().get(0).getSuccess().equals("1")) {
                        StripeCheckOutRP.ItemPaymentCheckOut itemPaymentCheckOut = paymentCheckOutRP.getItemPaymentCheckOuts().get(0);
                        if (method.isNetworkAvailable()) {
                            paymentToken = itemPaymentCheckOut.getStripe_payment_token();
                            ephemeralKeySecret = itemPaymentCheckOut.getStripe_ephemeralKey();
                            customerId = itemPaymentCheckOut.getStripe_customer();
                            paymentId = itemPaymentCheckOut.getStripe_id();
                            if (paymentToken.isEmpty() && ephemeralKeySecret.isEmpty() && customerId.isEmpty() && paymentId.isEmpty()) {
                                showError(getString(R.string.stripe_token_error));
                            } else {
                                presentPaymentSheet();
                            }
                        } else {
                            showError(getString(R.string.internet_connection));
                        }
                    } else {
                        showError(paymentCheckOutRP.getItemPaymentCheckOuts().get(0).getMsg());
                    }

                } catch (Exception e) {
                    Log.d("exception_error", e.toString());
                    method.alertBox(getResources().getString(R.string.stripe_token_error));
                }

                pDialog.dismiss();

            }

            @Override
            public void onFailure(@NotNull Call<StripeCheckOutRP> call, @NotNull Throwable t) {
                // Log error here since request failed
                Log.e("fail", t.toString());
                pDialog.dismiss();
                method.alertBox(getResources().getString(R.string.stripe_token_error));
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
