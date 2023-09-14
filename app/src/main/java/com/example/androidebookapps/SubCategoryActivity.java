package com.example.androidebookapps;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import com.example.adapter.SubCategoryAdapter;
import com.example.androidebookapps.databinding.ActivitySubCatBinding;
import com.example.item.SubCategoryList;
import com.example.response.SubCatRP;
import com.example.rest.ApiClient;
import com.example.rest.ApiInterface;
import com.example.util.API;
import com.example.util.BannerAds;
import com.example.util.Constant;
import com.example.util.EndlessRecyclerViewScrollListener;
import com.example.util.Method;
import com.example.util.OnClick;
import com.example.util.StatusBar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubCategoryActivity extends AppCompatActivity {

    ActivitySubCatBinding viewSubCat;
    SubCategoryAdapter subCategoryAdapter;
    String strCatId, strCatName;
    Method method;
    private int pageIndex = 1;
    private boolean isFirst = true, isOver = false;
    List<SubCategoryList> subcategoryLists;
    int j = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.initWhite(SubCategoryActivity.this);
        viewSubCat = ActivitySubCatBinding.inflate(getLayoutInflater());
        setContentView(viewSubCat.getRoot());

        method = new Method(SubCategoryActivity.this);
        method.forceRTLIfSupported();
        subcategoryLists = new ArrayList<>();

        Intent intent = getIntent();
        strCatId = intent.getStringExtra("postCatId");
        strCatName = intent.getStringExtra("postCatName");

        viewSubCat.toolbarMain.tvToolbarTitle.setText(strCatName);
        viewSubCat.toolbarMain.ivSearch.setVisibility(View.VISIBLE);
        viewSubCat.toolbarMain.ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSearch = new Intent(SubCategoryActivity.this, SearchBookActivity.class);
                startActivity(intentSearch);
            }
        });
        viewSubCat.toolbarMain.imageArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        viewSubCat.progressHome.setVisibility(View.GONE);
        viewSubCat.llNoData.clNoDataFound.setVisibility(View.GONE);

        viewSubCat.rvSubCat.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(SubCategoryActivity.this, 2);
        viewSubCat.rvSubCat.setLayoutManager(layoutManager);
        viewSubCat.rvSubCat.setFocusable(false);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (subCategoryAdapter.getItemViewType(position) != 1) {
                    return 2;
                }
                return 1;
            }
        });

        if (method.isNetworkAvailable()) {
            subCategoryData();
        } else {
            method.alertBox(getResources().getString(R.string.internet_connection));
        }
        EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (!isOver) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pageIndex++;
                            subCategoryData();
                        }
                    }, 1000);
                } else {
                    subCategoryAdapter.hideHeader();
                }
            }
        };

        viewSubCat.rvSubCat.addOnScrollListener(endlessRecyclerViewScrollListener);

        BannerAds.showBannerAds(SubCategoryActivity.this, viewSubCat.layoutAds);
    }

    private void subCategoryData() {
        if (isFirst)
            viewSubCat.progressHome.setVisibility(View.VISIBLE);
        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(SubCategoryActivity.this));
        jsObj.addProperty("cat_id", strCatId);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<SubCatRP> call = apiService.getSubCatData(API.toBase64(jsObj.toString()), pageIndex);
        call.enqueue(new Callback<SubCatRP>() {
            @Override
            public void onResponse(@NotNull Call<SubCatRP> call, @NotNull Response<SubCatRP> response) {

                try {

                    SubCatRP subCatRP = response.body();

                    if (subCatRP != null && subCatRP.getSuccess().equals("1")) {
                        if (subCatRP.getSubCategoryLists().size() != 0) {
                            for (int i = 0; i < subCatRP.getSubCategoryLists().size(); i++) {
                                if (Constant.isNative) {
                                    if (j % Constant.nativePosition == 0) {
                                        subcategoryLists.add(null);
                                        j++;
                                    }
                                }
                                subcategoryLists.add(subCatRP.getSubCategoryLists().get(i));
                                j++;
                            }
                            if (isFirst) {
                                isFirst = false;
                                subCategoryAdapter = new SubCategoryAdapter(SubCategoryActivity.this, subcategoryLists);
                                viewSubCat.rvSubCat.setAdapter(subCategoryAdapter);
                            } else {
                                subCategoryAdapter.notifyDataSetChanged();
                            }
                            subCategoryAdapter.setOnItemClickListener(new OnClick() {
                                @Override
                                public void position(int position) {
                                    Intent intentSubCat = new Intent(SubCategoryActivity.this, BookListBySubCatActivity.class);
                                    intentSubCat.putExtra("postSubCatId", subcategoryLists.get(position).getPost_id());
                                    intentSubCat.putExtra("postSubCatName", subcategoryLists.get(position).getPost_title());
                                    intentSubCat.putExtra("type", "");
                                    startActivity(intentSubCat);
                                }
                            });

                        } else {
                            isOver = true;
                            if (subCategoryAdapter != null) { // when there is no data in first time
                                subCategoryAdapter.hideHeader();
                            }
                            if (isFirst) {
                                viewSubCat.llNoData.clNoDataFound.setVisibility(View.VISIBLE);
                                viewSubCat.rvSubCat.setVisibility(View.GONE);
                                viewSubCat.progressHome.setVisibility(View.GONE);
                            }
                        }
                    } else {
                        viewSubCat.llNoData.clNoDataFound.setVisibility(View.VISIBLE);
                        viewSubCat.rvSubCat.setVisibility(View.GONE);
                        viewSubCat.progressHome.setVisibility(View.GONE);
                        method.alertBox(getResources().getString(R.string.failed_try_again));

                    }
                } catch (Exception e) {
                    Log.d("exception_error", e.toString());
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }
                viewSubCat.progressHome.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NotNull Call<SubCatRP> call, @NotNull Throwable t) {
                // Log error here since request failed
                Log.e("fail", t.toString());
                viewSubCat.llNoData.clNoDataFound.setVisibility(View.VISIBLE);
                viewSubCat.progressHome.setVisibility(View.GONE);
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
