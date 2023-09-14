package com.example.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.adapter.CategoryAdapter;
import com.example.androidebookapps.R;
import com.example.androidebookapps.BookListBySubCatActivity;
import com.example.androidebookapps.SearchBookActivity;
import com.example.androidebookapps.SubCategoryActivity;
import com.example.androidebookapps.databinding.FragmentCatBinding;
import com.example.item.CategoryList;
import com.example.response.CatRP;
import com.example.rest.ApiClient;
import com.example.rest.ApiInterface;
import com.example.util.API;
import com.example.util.Constant;
import com.example.util.EndlessRecyclerViewScrollListener;
import com.example.util.Method;
import com.example.util.OnClick;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CategoryFragment extends Fragment {

    FragmentCatBinding viewCat;
    Method method;
    CategoryAdapter categoryAdapter;
    private int pageIndex = 1;
    private boolean isFirst = true, isOver = false;
    List<CategoryList> categoryLists;
    int j = 1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        requireActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.app_bg));
        viewCat = FragmentCatBinding.inflate(inflater, container, false);

        method=new Method(requireActivity());
        categoryLists=new ArrayList<>();

        viewCat.toolbarMain.imageArrowBack.setVisibility(View.GONE);
        viewCat.toolbarMain.tvToolbarTitle.setText(getString(R.string.cat_title));
        viewCat.toolbarMain.ivSearch.setVisibility(View.VISIBLE);
        viewCat.toolbarMain.ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSearch=new Intent(requireActivity(), SearchBookActivity.class);
                startActivity(intentSearch);
            }
        });

        viewCat.progressHome.setVisibility(View.GONE);
        viewCat.llNoData.clNoDataFound.setVisibility(View.GONE);

        viewCat.rvCat.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        viewCat.rvCat.setLayoutManager(layoutManager);
        viewCat.rvCat.setFocusable(false);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (categoryAdapter.getItemViewType(position) != 1 ) {
                    return 2;
                }
                return 1;
            }
        });
        if (method.isNetworkAvailable()) {
            categoryData();
        } else {
            method.alertBox(getString(R.string.internet_connection));
        }

        EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (!isOver) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pageIndex++;
                            categoryData();
                        }
                    }, 1000);
                } else {
                    categoryAdapter.hideHeader();
                }
            }
        };

        viewCat.rvCat.addOnScrollListener(endlessRecyclerViewScrollListener);
        return viewCat.getRoot();
    }

    private void categoryData() {

        if (getActivity() != null) {
            if (isFirst)
            viewCat.progressHome.setVisibility(View.VISIBLE);

            JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(getActivity()));
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<CatRP> call = apiService.getCatData(API.toBase64(jsObj.toString()),pageIndex);
            call.enqueue(new Callback<CatRP>() {
                @Override
                public void onResponse(@NotNull Call<CatRP> call, @NotNull Response<CatRP> response) {

                    if (getActivity() != null) {

                        try {

                            CatRP catRP = response.body();

                            if (catRP !=null && catRP.getSuccess().equals("1")) {
                                if (catRP.getCategoryLists().size() != 0) {
                                    for (int i = 0; i < catRP.getCategoryLists().size(); i++) {
                                        if (Constant.isNative) {
                                            if (j %Constant.nativePosition == 0) {
                                                categoryLists.add(null);
                                                j++;
                                            }
                                        }
                                        categoryLists.add(catRP.getCategoryLists().get(i));
                                        j++;
                                    }
                                    if (isFirst) {
                                        isFirst = false;
                                        categoryAdapter = new CategoryAdapter(getActivity(), categoryLists);
                                        viewCat.rvCat.setAdapter(categoryAdapter);
                                    }else {
                                        categoryAdapter.notifyDataSetChanged();
                                    }
                                    categoryAdapter.setOnItemClickListener(new OnClick() {
                                        @Override
                                        public void position(int position) {
                                            if (categoryLists.get(position).getSub_cat_status().equals("true")){
                                                Intent intentSubCat=new Intent(requireActivity(), SubCategoryActivity.class);
                                                intentSubCat.putExtra("postCatId",categoryLists.get(position).getPost_id());
                                                intentSubCat.putExtra("postCatName",categoryLists.get(position).getPost_title());
                                                startActivity(intentSubCat);
                                            }else {
                                                Intent intentSubCat=new Intent(requireActivity(), BookListBySubCatActivity.class);
                                                intentSubCat.putExtra("postSubCatId",categoryLists.get(position).getPost_id());
                                                intentSubCat.putExtra("postSubCatName",categoryLists.get(position).getPost_title());
                                                intentSubCat.putExtra("type","Cat");
                                                startActivity(intentSubCat);
                                            }
                                        }
                                    });

                                } else {
                                    isOver = true;
                                    if (categoryAdapter != null) { // when there is no data in first time
                                        categoryAdapter.hideHeader();
                                    }
                                    if (isFirst){
                                        viewCat.llNoData.clNoDataFound.setVisibility(View.VISIBLE);
                                        viewCat.rvCat.setVisibility(View.GONE);
                                        viewCat.progressHome.setVisibility(View.GONE);
                                    }
                                }
                            } else {
                                viewCat.llNoData.clNoDataFound.setVisibility(View.VISIBLE);
                                viewCat.rvCat.setVisibility(View.GONE);
                                viewCat.progressHome.setVisibility(View.GONE);
                                method.alertBox(getString(R.string.failed_try_again));
                            }

                        } catch (Exception e) {
                            Log.d("exception_error", e.toString());
                            method.alertBox(getString(R.string.failed_try_again));
                        }
                    }

                    viewCat.progressHome.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(@NotNull Call<CatRP> call, @NotNull Throwable t) {
                    // Log error here since request failed
                    Log.e("fail", t.toString());
                    viewCat.llNoData.clNoDataFound.setVisibility(View.VISIBLE);
                    viewCat.progressHome.setVisibility(View.GONE);
                    method.alertBox(getString(R.string.failed_try_again));
                }
            });
        }
    }
}
