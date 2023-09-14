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

import com.example.adapter.AuthorAdapter;
import com.example.androidebookapps.AuthorDetailsActivity;
import com.example.androidebookapps.R;
import com.example.androidebookapps.SearchBookActivity;
import com.example.androidebookapps.databinding.FragmentAuthorBinding;
import com.example.item.AuthorList;
import com.example.response.AuthorRP;
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


public class AuthorFragment extends Fragment {

    FragmentAuthorBinding viewAuthor;
    Method method;
    AuthorAdapter authorAdapter;
    private int pageIndex = 1;
    private boolean isFirst = true, isOver = false;
    List<AuthorList> authorLists;
    int j = 1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        requireActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.app_bg));
        viewAuthor = FragmentAuthorBinding.inflate(inflater, container, false);

        method=new Method(requireActivity());
        authorLists=new ArrayList<>();

        viewAuthor.toolbarMain.imageArrowBack.setVisibility(View.GONE);
        viewAuthor.toolbarMain.tvToolbarTitle.setText(getString(R.string.author_title));
        viewAuthor.toolbarMain.ivSearch.setVisibility(View.VISIBLE);
        viewAuthor.toolbarMain.ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSearch=new Intent(requireActivity(), SearchBookActivity.class);
                startActivity(intentSearch);
            }
        });
        viewAuthor.progressHome.setVisibility(View.GONE);
        viewAuthor.llNoData.clNoDataFound.setVisibility(View.GONE);

        viewAuthor.rvCat.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        viewAuthor.rvCat.setLayoutManager(layoutManager);
        viewAuthor.rvCat.setFocusable(false);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (authorAdapter.getItemViewType(position) != 1 ) {
                    return 3;
                }
                return 1;
            }
        });
        if (method.isNetworkAvailable()) {
            authorData();
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
                            authorData();
                        }
                    }, 1000);
                } else {
                    authorAdapter.hideHeader();
                }
            }
        };

        viewAuthor.rvCat.addOnScrollListener(endlessRecyclerViewScrollListener);
        return viewAuthor.getRoot();
    }

    private void authorData() {

        if (getActivity() != null) {
            if (isFirst)
            viewAuthor.progressHome.setVisibility(View.VISIBLE);

            JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(getActivity()));
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<AuthorRP> call = apiService.getAuthorData(API.toBase64(jsObj.toString()),pageIndex);
            call.enqueue(new Callback<AuthorRP>() {
                @Override
                public void onResponse(@NotNull Call<AuthorRP> call, @NotNull Response<AuthorRP> response) {

                    if (getActivity() != null) {

                        try {

                            AuthorRP authorRP = response.body();
                            if (authorRP !=null && authorRP.getSuccess().equals("1")) {
                                if (authorRP.getAuthorLists().size() != 0) {
                                    for (int i = 0; i < authorRP.getAuthorLists().size(); i++) {
                                        if (Constant.isNative) {
                                            if (j %Constant.nativePosition== 0) {
                                                authorLists.add(null);
                                                j++;
                                            }
                                        }
                                        authorLists.add(authorRP.getAuthorLists().get(i));
                                        j++;
                                    }
                                    if (isFirst) {
                                        isFirst = false;
                                        authorAdapter = new AuthorAdapter(getActivity(), authorLists);
                                        viewAuthor.rvCat.setAdapter(authorAdapter);
                                    }else {
                                        authorAdapter.notifyDataSetChanged();
                                    }
                                    authorAdapter.setOnItemClickListener(new OnClick() {
                                        @Override
                                        public void position(int position) {
                                            Intent intentSubCat=new Intent(requireActivity(), AuthorDetailsActivity.class);
                                            intentSubCat.putExtra("AUTHOR_ID",authorLists.get(position).getPost_id());
                                            startActivity(intentSubCat);
                                        }
                                    });

                                } else {
                                    isOver = true;
                                    if (authorAdapter != null) { // when there is no data in first time
                                        authorAdapter.hideHeader();
                                    }
                                    if (isFirst) {
                                        viewAuthor.llNoData.clNoDataFound.setVisibility(View.VISIBLE);
                                        viewAuthor.rvCat.setVisibility(View.GONE);
                                        viewAuthor.progressHome.setVisibility(View.GONE);
                                    }
                                }
                            } else {
                                viewAuthor.llNoData.clNoDataFound.setVisibility(View.VISIBLE);
                                viewAuthor.rvCat.setVisibility(View.GONE);
                                viewAuthor.progressHome.setVisibility(View.GONE);
                                method.alertBox(getResources().getString(R.string.failed_try_again));
                            }

                        } catch (Exception e) {
                            Log.d("exception_error", e.toString());
                            method.alertBox(getResources().getString(R.string.failed_try_again));
                        }
                    }
                    viewAuthor.progressHome.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(@NotNull Call<AuthorRP> call, @NotNull Throwable t) {
                    // Log error here since request failed
                    Log.e("fail", t.toString());
                    viewAuthor.llNoData.clNoDataFound.setVisibility(View.VISIBLE);
                    viewAuthor.progressHome.setVisibility(View.GONE);
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }
            });
        }
    }
}
