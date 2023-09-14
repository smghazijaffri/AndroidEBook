package com.example.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.adapter.ContinueAdapter;
import com.example.androidebookapps.BookDetailsActivity;
import com.example.androidebookapps.R;
import com.example.androidebookapps.databinding.FragmentFavoriteBinding;
import com.example.item.SubCatListBook;
import com.example.response.SubCatListBookRP;
import com.example.rest.ApiClient;
import com.example.rest.ApiInterface;
import com.example.util.API;
import com.example.util.Method;
import com.example.util.OnClick;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ContinueFragment extends Fragment {

    FragmentFavoriteBinding viewContinue;
    Method method;
    ContinueAdapter continueAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        requireActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.app_bg_orange));
        viewContinue = FragmentFavoriteBinding.inflate(inflater, container, false);
        method = new Method(requireActivity());

        viewContinue.progressFav.setVisibility(View.GONE);
        viewContinue.llNoData.clNoDataFound.setVisibility(View.GONE);

        viewContinue.rvFav.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(requireActivity(), 3);
        viewContinue.rvFav.setLayoutManager(layoutManager);
        viewContinue.rvFav.setFocusable(false);

        if (method.isNetworkAvailable()) {
            ContinueData();
        } else {
            method.alertBox(getString(R.string.internet_connection));
        }

        return viewContinue.getRoot();
    }

    private void ContinueData() {
        if (getActivity() != null) {
            viewContinue.progressFav.setVisibility(View.VISIBLE);
            Call<SubCatListBookRP> call;
            JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(requireActivity()));
            jsObj.addProperty("user_id", method.getUserId());
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            call = apiService.getContinueData(API.toBase64(jsObj.toString()));
            call.enqueue(new Callback<SubCatListBookRP>() {
                @Override
                public void onResponse(@NotNull Call<SubCatListBookRP> call, @NotNull Response<SubCatListBookRP> response) {
                    if (getActivity() != null) {
                        try {

                            SubCatListBookRP favListBookRP = response.body();

                            if (favListBookRP != null && favListBookRP.getSuccess().equals("1")) {
                                if (favListBookRP.getSubCatListBooks().size() != 0) {
                                    viewContinue.rvFav.setVisibility(View.VISIBLE);
                                    continueAdapter = new ContinueAdapter(requireActivity(), favListBookRP.getSubCatListBooks());
                                    viewContinue.rvFav.setAdapter(continueAdapter);
                                    continueAdapter.setOnItemClickListener(new OnClick() {
                                        @Override
                                        public void position(int position) {
                                            SubCatListBook book = favListBookRP.getSubCatListBooks().get(position);
                                            Intent intentDetail = new Intent(requireActivity(), BookDetailsActivity.class);
                                            intentDetail.putExtra("BOOK_ID", book.getPost_id());
                                            intentDetail.putExtra("LAST_POS", "continuePos");
                                            intentDetail.putExtra("PAGE_NUM", book.getPage_num());
                                            startActivity(intentDetail);
                                        }
                                    });

                                } else {
                                    viewContinue.llNoData.clNoDataFound.setVisibility(View.VISIBLE);
                                    viewContinue.llNoData.textViewNoDataNoDataFound.setText(getString(R.string.no_continue));
                                    viewContinue.rvFav.setVisibility(View.GONE);
                                    viewContinue.progressFav.setVisibility(View.GONE);
                                }
                            } else {
                                viewContinue.llNoData.clNoDataFound.setVisibility(View.VISIBLE);
                                viewContinue.rvFav.setVisibility(View.GONE);
                                viewContinue.progressFav.setVisibility(View.GONE);
                                method.alertBox(getString(R.string.failed_try_again));
                            }

                        } catch (Exception e) {
                            Log.d("exception_error", e.toString());
                            method.alertBox(getString(R.string.failed_try_again));
                        }
                    }
                    viewContinue.progressFav.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(@NotNull Call<SubCatListBookRP> call, @NotNull Throwable t) {
                    // Log error here since request failed
                    Log.e("fail", t.toString());
                    viewContinue.llNoData.clNoDataFound.setVisibility(View.VISIBLE);
                    viewContinue.progressFav.setVisibility(View.GONE);
                    method.alertBox(getString(R.string.failed_try_again));
                }
            });
        }
    }
}
