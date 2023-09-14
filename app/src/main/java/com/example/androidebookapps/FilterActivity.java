package com.example.androidebookapps;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.adapter.AuthorNameAdapter;
import com.example.adapter.CatNameAdapter;
import com.example.androidebookapps.databinding.ActivityFilterBinding;
import com.example.item.AuthorList;
import com.example.item.CategoryList;
import com.example.response.AuthorRP;
import com.example.response.CatRP;
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

public class FilterActivity extends AppCompatActivity {

    Method method;
    ActivityFilterBinding viewFilterBinding;
    TextView[] tvColor;
    int selectFilterPos = 0;
    String sortByValue;
    AuthorNameAdapter authorNameAdapter;
    List<AuthorList> authorList;
    int SORTBY = 0, AUTHOR = 1, CAT = 2;
    CatNameAdapter catNameAdapter;
    List<CategoryList> categoryLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.initWhiteFilter(FilterActivity.this);
        viewFilterBinding = ActivityFilterBinding.inflate(getLayoutInflater());
        setContentView(viewFilterBinding.getRoot());
        method = new Method(FilterActivity.this);
        method.forceRTLIfSupported();
        authorList = new ArrayList<>();
        categoryLists = new ArrayList<>();


        viewFilterBinding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        viewFilterBinding.rlSortBy.setVisibility(View.VISIBLE);
        viewFilterBinding.rlCategory.setVisibility(View.GONE);
        viewFilterBinding.rlAuthor.setVisibility(View.GONE);

        tvColor = new TextView[]{viewFilterBinding.tvSortBy, viewFilterBinding.tvAuthorBy, viewFilterBinding.tvCatBy};
        selectStarFilter(SORTBY);


        viewFilterBinding.tvSortBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectStarFilter(SORTBY);
                viewFilterBinding.rlSortBy.setVisibility(View.VISIBLE);
                viewFilterBinding.rlCategory.setVisibility(View.GONE);
                viewFilterBinding.rlAuthor.setVisibility(View.GONE);
            }
        });
        viewFilterBinding.tvAuthorBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectStarFilter(AUTHOR);
                viewFilterBinding.rlSortBy.setVisibility(View.GONE);
                viewFilterBinding.rlCategory.setVisibility(View.GONE);
                viewFilterBinding.rlAuthor.setVisibility(View.VISIBLE);
                viewFilterBinding.llNoData.clNoDataFound.setVisibility(View.GONE);
                viewFilterBinding.progressHome.setVisibility(View.GONE);
                authorData();
                viewFilterBinding.rvList.setHasFixedSize(true);
                GridLayoutManager layoutManager = new GridLayoutManager(FilterActivity.this, 1);
                viewFilterBinding.rvList.setLayoutManager(layoutManager);
                viewFilterBinding.rvList.setFocusable(false);
            }
        });

        viewFilterBinding.tvCatBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectStarFilter(CAT);
                viewFilterBinding.rlSortBy.setVisibility(View.GONE);
                viewFilterBinding.rlCategory.setVisibility(View.VISIBLE);
                viewFilterBinding.rlAuthor.setVisibility(View.GONE);
                viewFilterBinding.llNoDataCat.clNoDataFound.setVisibility(View.GONE);
                viewFilterBinding.progressCategory.setVisibility(View.GONE);
                categoryData();
                viewFilterBinding.rvListCategory.setHasFixedSize(true);
                GridLayoutManager layoutManager = new GridLayoutManager(FilterActivity.this, 1);
                viewFilterBinding.rvListCategory.setLayoutManager(layoutManager);
                viewFilterBinding.rvListCategory.setFocusable(false);
            }
        });


        viewFilterBinding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectFilterPos == SORTBY) {
                    if (viewFilterBinding.rbSortByNew.isChecked()) {
                        sortByValue = "NewArrival";
                        goSortBy();
                    } else if (viewFilterBinding.rbSortByPop.isChecked()) {
                        sortByValue = "Popularity";
                        goSortBy();
                    } else if (viewFilterBinding.rbSortByRating.isChecked()) {
                        sortByValue = "Ratings";
                        goSortBy();
                    } else {
                        Toast.makeText(FilterActivity.this, getString(R.string.select_sort), Toast.LENGTH_SHORT).show();
                    }

                } else if (selectFilterPos == AUTHOR) {//author
                    if (getAuthorCommaSepIds().isEmpty()) {
                        Toast.makeText(FilterActivity.this, getString(R.string.select_sort), Toast.LENGTH_SHORT).show();
                    } else {
                        goAuthor();
                    }

                } else if (selectFilterPos == CAT) {
                    if (getCatCommaSepIds().isEmpty()) {
                        Toast.makeText(FilterActivity.this, getString(R.string.select_sort), Toast.LENGTH_SHORT).show();
                    } else {
                        goCat();
                    }
                }
            }
        });

        viewFilterBinding.btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectFilterPos == SORTBY) {
                    viewFilterBinding.rgSortBy.clearCheck();
                } else if (selectFilterPos == AUTHOR) {
                    for (AuthorList author : authorList) {
                        author.setSelected(false);
                    }
                    authorNameAdapter.notifyDataSetChanged();

                } else if (selectFilterPos == CAT) {
                    for (CategoryList author : categoryLists) {
                        author.setSelectedCat(false);
                    }
                    catNameAdapter.notifyDataSetChanged();
                }
            }
        });


    }

    private void selectStarFilter(int pos) {
        selectFilterPos = pos;
        for (int i = 0; i < tvColor.length; i++) {
            if (i == pos) {
                tvColor[i].setBackgroundResource(R.drawable.rectangle_border_star_fill);
                tvColor[i].setTextColor(getResources().getColor(R.color.orange_star_text));
            } else {
                tvColor[i].setBackgroundResource(R.drawable.rectangle_border_star_normal);
                tvColor[i].setTextColor(getResources().getColor(R.color.orange_star));
            }
        }

    }

    private void goSortBy() {
        Intent intentN = new Intent(FilterActivity.this, FilterSearchBookActivity.class);
        intentN.putExtra("searchValue", sortByValue);
        intentN.putExtra("isFrom", "sortByFilter");
        startActivity(intentN);
    }

    private void goAuthor() {
        Intent intentN = new Intent(FilterActivity.this, FilterSearchBookActivity.class);
        intentN.putExtra("searchValue", getAuthorCommaSepIds());
        intentN.putExtra("isFrom", "AuthorFilter");
        startActivity(intentN);
    }

    private void goCat() {
        Intent intentN = new Intent(FilterActivity.this, FilterSearchBookActivity.class);
        intentN.putExtra("searchValue", getCatCommaSepIds());
        intentN.putExtra("isFrom", "CatFilter");
        startActivity(intentN);
    }


    private void authorData() {
        viewFilterBinding.progressHome.setVisibility(View.VISIBLE);

        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(FilterActivity.this));
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<AuthorRP> call = apiService.getAllAuthorData(API.toBase64(jsObj.toString()));
        call.enqueue(new Callback<AuthorRP>() {
            @Override
            public void onResponse(@NotNull Call<AuthorRP> call, @NotNull Response<AuthorRP> response) {

                try {

                    AuthorRP authorRP = response.body();

                    if (authorRP !=null && authorRP.getSuccess().equals("1")) {
                        if (authorRP.getAuthorLists().size() != 0) {
                            authorList = authorRP.getAuthorLists();
                            authorNameAdapter = new AuthorNameAdapter(FilterActivity.this, authorList);
                            viewFilterBinding.rvList.setAdapter(authorNameAdapter);

                            viewFilterBinding.edtAuthorSearch.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    if (authorNameAdapter != null) {
                                        authorNameAdapter.filter(s.toString());
                                    }
                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                }
                            });

                        } else {
                            viewFilterBinding.llNoData.clNoDataFound.setVisibility(View.VISIBLE);
                            viewFilterBinding.rvList.setVisibility(View.GONE);
                            viewFilterBinding.progressHome.setVisibility(View.GONE);
                        }
                    } else {
                        viewFilterBinding.llNoData.clNoDataFound.setVisibility(View.VISIBLE);
                        viewFilterBinding.rvList.setVisibility(View.GONE);
                        viewFilterBinding.progressHome.setVisibility(View.GONE);
                        method.alertBox(getResources().getString(R.string.failed_try_again));
                    }

                } catch (Exception e) {
                    Log.d("exception_error", e.toString());
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }

                viewFilterBinding.progressHome.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NotNull Call<AuthorRP> call, @NotNull Throwable t) {
                // Log error here since request failed
                Log.e("fail", t.toString());
                viewFilterBinding.llNoData.clNoDataFound.setVisibility(View.VISIBLE);
                viewFilterBinding.progressHome.setVisibility(View.GONE);
                method.alertBox(getResources().getString(R.string.failed_try_again));
            }
        });

    }

    private void categoryData() {
        viewFilterBinding.progressCategory.setVisibility(View.VISIBLE);

        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(FilterActivity.this));
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<CatRP> call = apiService.getAllCatData(API.toBase64(jsObj.toString()));
        call.enqueue(new Callback<CatRP>() {
            @Override
            public void onResponse(@NotNull Call<CatRP> call, @NotNull Response<CatRP> response) {

                try {

                    CatRP catRP = response.body();

                    if (catRP !=null && catRP.getSuccess().equals("1")) {
                        if (catRP.getCategoryLists().size() != 0) {
                            categoryLists = catRP.getCategoryLists();
                            catNameAdapter = new CatNameAdapter(FilterActivity.this, catRP.getCategoryLists());
                            viewFilterBinding.rvListCategory.setAdapter(catNameAdapter);
                            viewFilterBinding.edtCatSearch.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    if (catNameAdapter != null) {
                                        catNameAdapter.filter(s.toString());
                                    }
                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                }
                            });

                        } else {
                            viewFilterBinding.llNoDataCat.clNoDataFound.setVisibility(View.VISIBLE);
                            viewFilterBinding.rvListCategory.setVisibility(View.GONE);
                            viewFilterBinding.progressCategory.setVisibility(View.GONE);
                        }
                    } else {
                        viewFilterBinding.llNoDataCat.clNoDataFound.setVisibility(View.VISIBLE);
                        viewFilterBinding.rvListCategory.setVisibility(View.GONE);
                        viewFilterBinding.progressCategory.setVisibility(View.GONE);
                        method.alertBox(getResources().getString(R.string.failed_try_again));
                    }

                } catch (Exception e) {
                    Log.d("exception_error", e.toString());
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }

                viewFilterBinding.progressCategory.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NotNull Call<CatRP> call, @NotNull Throwable t) {
                // Log error here since request failed
                Log.e("fail", t.toString());
                viewFilterBinding.llNoDataCat.clNoDataFound.setVisibility(View.VISIBLE);
                viewFilterBinding.progressCategory.setVisibility(View.GONE);
                method.alertBox(getResources().getString(R.string.failed_try_again));
            }
        });
    }

    @NonNull
    private String getCatCommaSepIds() {
        StringBuilder stringBuilder = new StringBuilder();
        String prefix = "";
        for (CategoryList itemFilter : categoryLists) {
            if (itemFilter.isSelectedCat()) {
                stringBuilder.append(prefix);
                prefix = ",";
                stringBuilder.append(itemFilter.getPost_id());
            }
        }

        return stringBuilder.toString();
    }

    @NonNull
    private String getAuthorCommaSepIds() {
        StringBuilder stringBuilder = new StringBuilder();
        String prefix = "";
        for (AuthorList itemFilter : authorList) {
            if (itemFilter.isSelected()) {
                stringBuilder.append(prefix);
                prefix = ",";
                stringBuilder.append(itemFilter.getPost_id());
            }
        }

        return stringBuilder.toString();
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