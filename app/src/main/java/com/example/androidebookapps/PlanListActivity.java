package com.example.androidebookapps;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.adapter.PlanAdapter;
import com.example.androidebookapps.databinding.ActivityPlanBinding;
import com.example.paymentmethod.Transaction;
import com.example.response.PlanRP;
import com.example.rest.ApiClient;
import com.example.rest.ApiInterface;
import com.example.util.API;
import com.example.util.Method;
import com.example.util.OnClick;
import com.example.util.StatusBar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlanListActivity extends AppCompatActivity {

    ActivityPlanBinding activityPlanBinding;
    Method method;
    PlanAdapter planAdapter;
    int selectedPlan = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.initDeepOrange(PlanListActivity.this);
        activityPlanBinding = ActivityPlanBinding.inflate(getLayoutInflater());
        setContentView(activityPlanBinding.getRoot());

        method = new Method(PlanListActivity.this);
        method.forceRTLIfSupported();

        activityPlanBinding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        activityPlanBinding.progressHome.setVisibility(View.GONE);
        activityPlanBinding.llNoData.clNoDataFound.setVisibility(View.GONE);
        activityPlanBinding.btnPlan.setVisibility(View.GONE);

        activityPlanBinding.rvPlanList.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(PlanListActivity.this, 1);
        activityPlanBinding.rvPlanList.setLayoutManager(layoutManager);
        activityPlanBinding.rvPlanList.setFocusable(false);

        if (method.isNetworkAvailable()) {
            planListData();
        } else {
            method.alertBox(getResources().getString(R.string.internet_connection));
        }
    }

    private void planListData() {

        activityPlanBinding.progressHome.setVisibility(View.VISIBLE);
        Call<PlanRP> call;
        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(PlanListActivity.this));
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        call = apiService.getPlanListData(API.toBase64(jsObj.toString()));
        call.enqueue(new Callback<PlanRP>() {
            @Override
            public void onResponse(@NotNull Call<PlanRP> call, @NotNull Response<PlanRP> response) {

                try {

                    PlanRP planRP = response.body();

                    if (planRP !=null && planRP.getSuccess().equals("1")) {
                        if (planRP.getItemPlanLists().size() != 0) {
                            //PlanRP.ItemPlanList itemPlanList=planRP.getItemPlanLists().get(0);
                            activityPlanBinding.rvPlanList.setVisibility(View.VISIBLE);
                            activityPlanBinding.btnPlan.setVisibility(View.VISIBLE);
                            planAdapter = new PlanAdapter(PlanListActivity.this, planRP.getItemPlanLists());
                            activityPlanBinding.rvPlanList.setAdapter(planAdapter);
                            planAdapter.select(0);
                            planAdapter.setOnItemClickListener(new OnClick() {
                                @Override
                                public void position(int position) {
                                    selectedPlan = position;
                                    planAdapter.select(position);
                                }
                            });

                            activityPlanBinding.btnPlan.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    PlanRP.ItemPlanList itemPlan = planRP.getItemPlanLists().get(selectedPlan);
                                    String isFreePlan = itemPlan.getPlan_price();
                                    if (isFreePlan.equals("0.00")) {
                                        if (method.isNetworkAvailable()) {
                                            new Transaction(PlanListActivity.this).purchasedItem(itemPlan.getPlan_id(), method.getUserId(), "N/A","Free",false);
                                        } else {
                                            Toast.makeText(PlanListActivity.this, getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Intent intent = new Intent(PlanListActivity.this, PaymentMethodActivity.class);
                                        intent.putExtra("planId", itemPlan.getPlan_id());
                                        intent.putExtra("planName", itemPlan.getPlan_name());
                                        intent.putExtra("planPrice", itemPlan.getPlan_price());
                                        intent.putExtra("planPriceCurrency", itemPlan.getCurrency_code());
                                        intent.putExtra("planUserId", method.getUserId());
                                        intent.putExtra("planDuration", itemPlan.getPlan_duration());//itemPlan.getPlanDuration()
                                        startActivity(intent);
                                    }
                                }
                            });
                        } else {
                            activityPlanBinding.llNoData.clNoDataFound.setVisibility(View.VISIBLE);
                            activityPlanBinding.rvPlanList.setVisibility(View.GONE);
                            activityPlanBinding.progressHome.setVisibility(View.GONE);
                            activityPlanBinding.btnPlan.setVisibility(View.GONE);
                            activityPlanBinding.llNoData.textViewNoDataNoDataFound.setText(getString(R.string.no_plan));
                        }
                    } else {
                        activityPlanBinding.llNoData.clNoDataFound.setVisibility(View.VISIBLE);
                        activityPlanBinding.llNoData.textViewNoDataNoDataFound.setText(getString(R.string.no_plan));
                        activityPlanBinding.rvPlanList.setVisibility(View.GONE);
                        activityPlanBinding.progressHome.setVisibility(View.GONE);
                        activityPlanBinding.btnPlan.setVisibility(View.GONE);
                        method.alertBox(getResources().getString(R.string.failed_try_again));
                    }

                } catch (Exception e) {
                    Log.d("exception_error", e.toString());
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }
                activityPlanBinding.progressHome.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NotNull Call<PlanRP> call, @NotNull Throwable t) {
                // Log error here since request failed
                Log.e("fail", t.toString());
                activityPlanBinding.llNoData.clNoDataFound.setVisibility(View.VISIBLE);
                activityPlanBinding.llNoData.textViewNoDataNoDataFound.setText(getString(R.string.no_plan));
                activityPlanBinding.progressHome.setVisibility(View.GONE);
                activityPlanBinding.btnPlan.setVisibility(View.GONE);
                method.alertBox(getResources().getString(R.string.failed_try_again));
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
        return super.onSupportNavigateUp();
    }
}