package com.example.androidebookapps;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.example.androidebookapps.databinding.ActivityPaymentMethodBinding;
import com.example.item.ItemPaymentSetting;
import com.example.paymentmethod.PayPalActivity;
import com.example.paymentmethod.PayStackActivity;
import com.example.paymentmethod.PayUMoneyActivity;
import com.example.paymentmethod.RazorPayActivity;
import com.example.paymentmethod.StripeActivity;
import com.example.rest.ApiClient;
import com.example.rest.ApiInterface;
import com.example.util.API;
import com.example.util.Method;
import com.example.util.StatusBar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentMethodActivity extends AppCompatActivity {

    ActivityPaymentMethodBinding viewPaymentBinding;
    Method method;
    String planId, planName, planPrice, planCur, planDuration, planUserId, currencyCode;
    ItemPaymentSetting paymentSetting;
    CardView[] cardViews;
    TextView[] textViews;
    View[] views;
    int gatewayPos;
    boolean isRent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.initDeepOrange(PaymentMethodActivity.this);
        viewPaymentBinding = ActivityPaymentMethodBinding.inflate(getLayoutInflater());
        setContentView(viewPaymentBinding.getRoot());
        method = new Method(PaymentMethodActivity.this);
        method.forceRTLIfSupported();
        paymentSetting = new ItemPaymentSetting();

        Intent intent = getIntent();
        planId = intent.getStringExtra("planId");
        planName = intent.getStringExtra("planName");
        planPrice = intent.getStringExtra("planPrice");
        planCur = intent.getStringExtra("planPriceCurrency");
        planDuration = intent.getStringExtra("planDuration");
        planUserId = intent.getStringExtra("planUserId");
        if (intent.hasExtra("isRent")) {
            isRent = intent.getBooleanExtra("isRent", false);
        }
        viewPaymentBinding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        viewPaymentBinding.progressHome.setVisibility(View.GONE);
        viewPaymentBinding.llNoData.clNoDataFound.setVisibility(View.GONE);
        viewPaymentBinding.btnPay.setVisibility(View.GONE);

        viewPaymentBinding.llPlan.tvPlanName.setText(planName);
        viewPaymentBinding.llPlan.tvAmount.setText(planPrice + getString(R.string.plan_line));
        viewPaymentBinding.llPlan.tvCurrency.setText(planCur);
        if (intent.hasExtra("isRent")) {
            viewPaymentBinding.llPlan.tvPlanTime.setText(getString(R.string.rent_days,planDuration));
        }else {
            viewPaymentBinding.llPlan.tvPlanTime.setText(planDuration);
        }


        if (method.isNetworkAvailable()) {
            paymentMethodListData();
        } else {
            method.alertBox(getResources().getString(R.string.internet_connection));
        }
        viewPaymentBinding.llPayment.setVisibility(View.GONE);

        viewPaymentBinding.btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        cardViews = new CardView[]{viewPaymentBinding.cvPaypal, viewPaymentBinding.cvStripe, viewPaymentBinding.cvRazorPay, viewPaymentBinding.cvPayStack, viewPaymentBinding.cvPayUMoney};
        textViews = new TextView[]{viewPaymentBinding.tvPaypal, viewPaymentBinding.tvStripe, viewPaymentBinding.tvRazorPay, viewPaymentBinding.tvPayStack, viewPaymentBinding.tvPayUMoney};
        views = new View[]{viewPaymentBinding.rdPaypal, viewPaymentBinding.rdStripe, viewPaymentBinding.rdRazorPay, viewPaymentBinding.rdPayStack, viewPaymentBinding.rdPayUMoney};

        viewPaymentBinding.cvPaypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectBottomNav(0);
                gatewayPos = 1;
            }
        });

        viewPaymentBinding.cvStripe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectBottomNav(1);
                gatewayPos = 2;
            }
        });

        viewPaymentBinding.cvRazorPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectBottomNav(2);
                gatewayPos = 3;
            }
        });

        viewPaymentBinding.cvPayStack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectBottomNav(3);
                gatewayPos = 4;
            }
        });

        viewPaymentBinding.cvPayUMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectBottomNav(4);
                gatewayPos = 6;
            }
        });


        viewPaymentBinding.btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gatewayPos == 0) {
                    Toast.makeText(PaymentMethodActivity.this, getString(R.string.payment_select), Toast.LENGTH_SHORT).show();
                } else {
                    if (gatewayPos == 1) {
                        goPayPal();
                    } else if (gatewayPos == 2) {
                        goStripe();
                    } else if (gatewayPos == 3) {
                        goRazorPay();
                    } else if (gatewayPos == 4) {
                        goPayStack();
                    } else if (gatewayPos == 6) {
                        goPayUMoney();
                    }
                }
            }
        });
    }

    private void selectBottomNav(int pos) {
        for (int i = 0; i < cardViews.length; i++) {
            if (i == pos) {
                cardViews[i].setCardBackgroundColor(getResources().getColor(R.color.plan_card_bg_select));
                views[i].setBackgroundResource(R.drawable.plan_circle_select);
                textViews[i].setTextColor(getResources().getColor(R.color.plan_text_select));
            } else {
                cardViews[i].setCardBackgroundColor(getResources().getColor(R.color.plan_card_bg_unselect));
                views[i].setBackgroundResource(R.drawable.plan_circle_unselect);
                textViews[i].setTextColor(getResources().getColor(R.color.plan_name_unselect));
            }
        }
    }

    private void paymentMethodListData() {

        viewPaymentBinding.progressHome.setVisibility(View.VISIBLE);
        Call<ResponseBody> call;
        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(PaymentMethodActivity.this));
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        call = apiService.getPaymentMethodData(API.toBase64(jsObj.toString()));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                String responseBody = null;
                try {
                    responseBody = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    JSONObject mainJson = new JSONObject(responseBody);
                    assert mainJson != null;

                    paymentSetting.setCurrencyCode(mainJson.getString("currency_code"));

                    JSONArray jsonArray = mainJson.getJSONArray("EBOOK_APP");
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject objJson = jsonArray.getJSONObject(i);
                            String gatewayName = objJson.getString("gateway_name");
                            String gatewayId = objJson.getString("gateway_id");
                            String gatewayLogo = objJson.getString("gateway_logo");
                            boolean status = objJson.getBoolean("status");
                            JSONObject gatewayInfoJson = objJson.getJSONObject("gateway_info");
                            switch (gatewayId) {
                                case "1":
                                    viewPaymentBinding.tvPaypal.setText(gatewayName);
                                    paymentSetting.setPayPal(status);
                                    paymentSetting.setPayPalSandbox(gatewayInfoJson.getString("mode").equals("sandbox"));
                                    Glide.with(getApplicationContext()).load(gatewayLogo)
                                            .placeholder(R.drawable.placeholder_portable)
                                            .into(viewPaymentBinding.ivPaypal);
                                    break;
                                case "2":
                                    viewPaymentBinding.tvStripe.setText(gatewayName);
                                    paymentSetting.setStripe(status);
                                    paymentSetting.setStripePublisherKey(gatewayInfoJson.getString("stripe_publishable_key"));
                                    Glide.with(getApplicationContext()).load(gatewayLogo)
                                            .placeholder(R.drawable.placeholder_portable)
                                            .into(viewPaymentBinding.ivStripe);
                                    break;
                                case "3":
                                    viewPaymentBinding.tvRazorPay.setText(gatewayName);
                                    paymentSetting.setRazorPay(status);
                                    paymentSetting.setRazorPayKey(gatewayInfoJson.getString("razorpay_key"));
                                    Glide.with(getApplicationContext()).load(gatewayLogo)
                                            .placeholder(R.drawable.placeholder_portable)
                                            .into(viewPaymentBinding.ivRazorPay);
                                    break;
                                case "4":
                                    viewPaymentBinding.tvPayStack.setText(gatewayName);
                                    paymentSetting.setPayStack(status);
                                    paymentSetting.setPayStackPublicKey(gatewayInfoJson.getString("paystack_public_key"));
                                    Glide.with(getApplicationContext()).load(gatewayLogo)
                                            .placeholder(R.drawable.placeholder_portable)
                                            .into(viewPaymentBinding.ivPayStack);
                                    break;
                                case "6":
                                    viewPaymentBinding.tvPayUMoney.setText(gatewayName);
                                    paymentSetting.setPayUMoney(status);
                                    paymentSetting.setPayUMoneySandbox(gatewayInfoJson.getString("mode").equals("sandbox"));
                                    paymentSetting.setPayUMoneyMerchantId(gatewayInfoJson.getString("payu_merchant_id"));
                                    paymentSetting.setPayUMoneyMerchantKey(gatewayInfoJson.getString("payu_key"));
                                    Glide.with(getApplicationContext()).load(gatewayLogo)
                                            .placeholder(R.drawable.placeholder_portable)
                                            .into(viewPaymentBinding.ivPayUMoney);
                                    break;

                            }
                        }
                        viewPaymentBinding.llPayment.setVisibility(View.VISIBLE);
                        viewPaymentBinding.btnPay.setVisibility(View.VISIBLE);
                        displayData();
                    } else {
                        viewPaymentBinding.llNoData.clNoDataFound.setVisibility(View.VISIBLE);
                        viewPaymentBinding.llNoData.textViewNoDataNoDataFound.setText(getString(R.string.no_payment));
                        viewPaymentBinding.llPayment.setVisibility(View.GONE);
                        viewPaymentBinding.progressHome.setVisibility(View.GONE);
                        viewPaymentBinding.btnPay.setVisibility(View.GONE);
                        method.alertBox(getResources().getString(R.string.failed_try_again));
                    }
                } catch (Exception e) {
                    Log.d("exception_error", e.toString());
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                    viewPaymentBinding.llPayment.setVisibility(View.GONE);
                }
                viewPaymentBinding.progressHome.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                // Log error here since request failed
                Log.e("fail", t.toString());
                viewPaymentBinding.llNoData.clNoDataFound.setVisibility(View.VISIBLE);
                viewPaymentBinding.llNoData.textViewNoDataNoDataFound.setText(getString(R.string.no_payment));
                viewPaymentBinding.progressHome.setVisibility(View.GONE);
                viewPaymentBinding.btnPay.setVisibility(View.GONE);
                viewPaymentBinding.llPayment.setVisibility(View.GONE);
                method.alertBox(getResources().getString(R.string.failed_try_again));
            }
        });
    }

    private void displayData() {
        viewPaymentBinding.cvPaypal.setVisibility(paymentSetting.isPayPal() ? View.VISIBLE : View.GONE);
        viewPaymentBinding.cvStripe.setVisibility(paymentSetting.isStripe() ? View.VISIBLE : View.GONE);
        viewPaymentBinding.cvRazorPay.setVisibility(paymentSetting.isRazorPay() ? View.VISIBLE : View.GONE);
        viewPaymentBinding.cvPayStack.setVisibility(paymentSetting.isPayStack() ? View.VISIBLE : View.GONE);
        viewPaymentBinding.cvPayUMoney.setVisibility(paymentSetting.isPayUMoney() ? View.VISIBLE : View.GONE);

        if (!paymentSetting.isPayPal() && !paymentSetting.isStripe()
                && !paymentSetting.isRazorPay() && !paymentSetting.isPayStack()
                && !paymentSetting.isPayUMoney()
                && !paymentSetting.isPayTM()) {
            viewPaymentBinding.llNoData.clNoDataFound.setVisibility(View.VISIBLE);
            viewPaymentBinding.llNoData.textViewNoDataNoDataFound.setText(getString(R.string.no_payment));
            viewPaymentBinding.btnPay.setVisibility(View.GONE);
        }
    }

    private void goPayPal() {
        Intent intentPayPal = new Intent(PaymentMethodActivity.this, PayPalActivity.class);
        intentPayPal.putExtra("planId", planId);
        intentPayPal.putExtra("planPrice", planPrice);
        intentPayPal.putExtra("planCurrency", paymentSetting.getCurrencyCode());
        intentPayPal.putExtra("planGateway", "Paypal");
        intentPayPal.putExtra("isSandbox", paymentSetting.isPayPalSandbox());
        intentPayPal.putExtra("isRent", isRent);
        startActivity(intentPayPal);
    }

    private void goStripe() {
        Intent intentStripe = new Intent(PaymentMethodActivity.this, StripeActivity.class);
        intentStripe.putExtra("planId", planId);
        intentStripe.putExtra("planPrice", planPrice);
        intentStripe.putExtra("planCurrency", paymentSetting.getCurrencyCode());
        intentStripe.putExtra("planGateway", "Stripe");
        intentStripe.putExtra("stripePublisherKey", paymentSetting.getStripePublisherKey());
        intentStripe.putExtra("isRent", isRent);
        startActivity(intentStripe);
    }

    private void goRazorPay() {
        Intent intentRazor = new Intent(PaymentMethodActivity.this, RazorPayActivity.class);
        intentRazor.putExtra("planId", planId);
        intentRazor.putExtra("planName", planName);
        intentRazor.putExtra("planPrice", planPrice);
        intentRazor.putExtra("planCurrency", paymentSetting.getCurrencyCode());
        intentRazor.putExtra("planGateway", "Razorpay");
        intentRazor.putExtra("razorPayKey", paymentSetting.getRazorPayKey());
        intentRazor.putExtra("isRent", isRent);
        startActivity(intentRazor);
    }

    private void goPayStack() {
        Intent intentPayStack = new Intent(PaymentMethodActivity.this, PayStackActivity.class);
        intentPayStack.putExtra("planId", planId);
        intentPayStack.putExtra("planPrice", planPrice);
        intentPayStack.putExtra("planCurrency", paymentSetting.getCurrencyCode());
        intentPayStack.putExtra("planGateway", "Paystack");
        intentPayStack.putExtra("payStackPublicKey", paymentSetting.getPayStackPublicKey());
        intentPayStack.putExtra("isRent", isRent);
        startActivity(intentPayStack);
    }

    private void goPayUMoney() {
        Intent intentPayU = new Intent(PaymentMethodActivity.this, PayUMoneyActivity.class);
        intentPayU.putExtra("planId", planId);
        intentPayU.putExtra("planName", planName);
        intentPayU.putExtra("planPrice", planPrice);
        intentPayU.putExtra("planCurrency", paymentSetting.getCurrencyCode());
        intentPayU.putExtra("planGateway", "PayUMoney");
        intentPayU.putExtra("isSandbox", paymentSetting.isPayUMoneySandbox());
        intentPayU.putExtra("payUMoneyMerchantId", paymentSetting.getPayUMoneyMerchantId());
        intentPayU.putExtra("payUMoneyMerchantKey", paymentSetting.getPayUMoneyMerchantKey());
        intentPayU.putExtra("isRent", isRent);
        startActivity(intentPayU);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}