package com.example.androidebookapps;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.adapter.RateReviewListAdapter;
import com.example.androidebookapps.databinding.ActivityRateReviewListBinding;
import com.example.item.RateReviewList;
import com.example.response.RateReviewRP;
import com.example.rest.ApiClient;
import com.example.rest.ApiInterface;
import com.example.util.API;
import com.example.util.Method;
import com.example.util.StatusBar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RateReviewActivity extends AppCompatActivity {

    Method method;
    ActivityRateReviewListBinding viewRateReviewBinding;
    View[] viewBg;
    ImageView[] ivStar;
    TextView[] tvColor;
    String strPostId;
    RateReviewListAdapter rateReviewListAdapter;
    List<RateReviewList> rateReviewLists;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.initWhite(RateReviewActivity.this);
        rateReviewLists = new ArrayList<>();
        viewRateReviewBinding = ActivityRateReviewListBinding.inflate(getLayoutInflater());
        setContentView(viewRateReviewBinding.getRoot());
        method = new Method(RateReviewActivity.this);
        method.forceRTLIfSupported();

        Intent intent = getIntent();
        strPostId = intent.getStringExtra("RATE_BOOK_ID");

        viewRateReviewBinding.toolbarMain.tvToolbarTitle.setText(getString(R.string.lbl_ratings_review));

        viewRateReviewBinding.toolbarMain.imageArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        viewBg = new View[]{viewRateReviewBinding.viewRecAll, viewRateReviewBinding.viewRecStar1, viewRateReviewBinding.viewRecStar2, viewRateReviewBinding.viewRecStar3, viewRateReviewBinding.viewRecStar4, viewRateReviewBinding.viewRecStar5};
        ivStar = new ImageView[]{viewRateReviewBinding.ivStarAll, viewRateReviewBinding.ivStar1, viewRateReviewBinding.ivStar2, viewRateReviewBinding.ivStar3, viewRateReviewBinding.ivStar4, viewRateReviewBinding.ivStar5};
        tvColor = new TextView[]{viewRateReviewBinding.tvStarAll, viewRateReviewBinding.tvStar1, viewRateReviewBinding.tvStar2, viewRateReviewBinding.tvStar3, viewRateReviewBinding.tvStar4, viewRateReviewBinding.tvStar5};
        selectStarFilter(0);

        viewRateReviewBinding.viewRecAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectStarFilter(0);
                if (rateReviewListAdapter != null && !rateReviewLists.isEmpty())
                    rateReviewListAdapter.setList(rateReviewLists);
            }
        });
        viewRateReviewBinding.viewRecStar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectStarFilter(1);
                if (rateReviewListAdapter != null && !rateReviewLists.isEmpty())
                    rateReviewListAdapter.setList(getReviewListByStar("1"));
            }
        });
        viewRateReviewBinding.viewRecStar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectStarFilter(2);
                if (rateReviewListAdapter != null && !rateReviewLists.isEmpty())
                    rateReviewListAdapter.setList(getReviewListByStar("2"));
            }
        });
        viewRateReviewBinding.viewRecStar3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectStarFilter(3);
                if (rateReviewListAdapter != null && !rateReviewLists.isEmpty())
                    rateReviewListAdapter.setList(getReviewListByStar("3"));
            }
        });
        viewRateReviewBinding.viewRecStar4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectStarFilter(4);
                if (rateReviewListAdapter != null && !rateReviewLists.isEmpty())
                    rateReviewListAdapter.setList(getReviewListByStar("4"));
            }
        });
        viewRateReviewBinding.viewRecStar5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectStarFilter(5);
                if (rateReviewListAdapter != null && !rateReviewLists.isEmpty())
                    rateReviewListAdapter.setList(getReviewListByStar("5"));
            }
        });

        viewRateReviewBinding.llNoData.clNoDataFound.setVisibility(View.GONE);

        viewRateReviewBinding.rvReviewList.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(RateReviewActivity.this, 1);
        viewRateReviewBinding.rvReviewList.setLayoutManager(layoutManager);
        viewRateReviewBinding.rvReviewList.setFocusable(false);

        if (method.isNetworkAvailable()) {
            if (method.getIsLogin()) {
                rateReviewData(method.getUserId());
            } else {
                rateReviewData("0");
            }
        } else {
            method.alertBox(getResources().getString(R.string.internet_connection));
        }

    }

    private void selectStarFilter(int pos) {
        for (int i = 0; i < viewBg.length; i++) {
            if (i == pos) {
                viewBg[i].setBackgroundResource(R.drawable.rectangle_border_star_fill);
                ivStar[i].setColorFilter(getResources().getColor(R.color.orange_star_text), PorterDuff.Mode.SRC_IN);
                tvColor[i].setTextColor(getResources().getColor(R.color.orange_star_text));
            } else {
                viewBg[i].setBackgroundResource(R.drawable.rectangle_border_star_normal);
                ivStar[i].setColorFilter(getResources().getColor(R.color.orange_star), PorterDuff.Mode.SRC_IN);
                tvColor[i].setTextColor(getResources().getColor(R.color.orange_star));
            }
        }

    }

    private void rateReviewData(String userId) {

        viewRateReviewBinding.progressRate.setVisibility(View.VISIBLE);

        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(RateReviewActivity.this));
        jsObj.addProperty("book_id", strPostId);
        jsObj.addProperty("user_id", userId);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<RateReviewRP> call = apiService.getRateReviewListData(API.toBase64(jsObj.toString()));
        call.enqueue(new Callback<RateReviewRP>() {
            @Override
            public void onResponse(@NotNull Call<RateReviewRP> call, @NotNull Response<RateReviewRP> response) {

                try {

                    RateReviewRP rateReviewRP = response.body();

                    if (rateReviewRP !=null && rateReviewRP.getSuccess().equals("1")) {
                        if (rateReviewRP.getRateReviewLists().size() != 0) {
                            rateReviewLists = rateReviewRP.getRateReviewLists();
                            viewRateReviewBinding.rvReviewList.setVisibility(View.VISIBLE);
                            rateReviewListAdapter = new RateReviewListAdapter(RateReviewActivity.this,strPostId);
                            rateReviewListAdapter.setList(rateReviewLists);
                            viewRateReviewBinding.rvReviewList.setAdapter(rateReviewListAdapter);

                        } else {
                            viewRateReviewBinding.llNoData.clNoDataFound.setVisibility(View.VISIBLE);
                            viewRateReviewBinding.rvReviewList.setVisibility(View.GONE);
                            viewRateReviewBinding.progressRate.setVisibility(View.GONE);
                        }
                    } else {
                        viewRateReviewBinding.llNoData.clNoDataFound.setVisibility(View.VISIBLE);
                        viewRateReviewBinding.rvReviewList.setVisibility(View.GONE);
                        viewRateReviewBinding.progressRate.setVisibility(View.GONE);
                        method.alertBox(getResources().getString(R.string.failed_try_again));
                    }

                } catch (Exception e) {
                    Log.d("exception_error", e.toString());
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }
                viewRateReviewBinding.progressRate.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NotNull Call<RateReviewRP> call, @NotNull Throwable t) {
                // Log error here since request failed
                Log.e("fail", t.toString());
                viewRateReviewBinding.llNoData.clNoDataFound.setVisibility(View.VISIBLE);
                viewRateReviewBinding.progressRate.setVisibility(View.GONE);
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

    private List<RateReviewList> getReviewListByStar(String star) {
        List<RateReviewList> list = new ArrayList<>();
        for (RateReviewList rateReview : rateReviewLists) {
            if (rateReview.getRate().equals(star)) {
                list.add(rateReview);
            }
        }
        return list;
    }


}