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

import com.example.adapter.RentAdapter;
import com.example.androidebookapps.BookDetailsActivity;
import com.example.androidebookapps.R;
import com.example.androidebookapps.databinding.FragmentFavoriteBinding;
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


public class RentBookFragment extends Fragment {

    FragmentFavoriteBinding viewFavorite;
    Method method;
    RentAdapter rentAdapter;
    SubCatListBookRP favListBookRP;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        requireActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.app_bg_orange));
        viewFavorite = FragmentFavoriteBinding.inflate(inflater, container, false);
        method = new Method(requireActivity());

        viewFavorite.progressFav.setVisibility(View.GONE);
        viewFavorite.llNoData.clNoDataFound.setVisibility(View.GONE);

        viewFavorite.rvFav.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(requireActivity(), 3);
        viewFavorite.rvFav.setLayoutManager(layoutManager);
        viewFavorite.rvFav.setFocusable(false);

        if (method.isNetworkAvailable()) {
            favData();
        } else {
            method.alertBox(getString(R.string.internet_connection));
        }

        return viewFavorite.getRoot();
    }

    private void favData() {
        if (getActivity() != null) {
            viewFavorite.progressFav.setVisibility(View.VISIBLE);
            Call<SubCatListBookRP> call;
            JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(requireActivity()));
            jsObj.addProperty("user_id", method.getUserId());
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            call = apiService.getRentBookListData(API.toBase64(jsObj.toString()));
            call.enqueue(new Callback<SubCatListBookRP>() {
                @Override
                public void onResponse(@NotNull Call<SubCatListBookRP> call, @NotNull Response<SubCatListBookRP> response) {
                    if (getActivity() != null) {
                        try {

                            favListBookRP = response.body();
                            if (favListBookRP != null && favListBookRP.getSuccess().equals("1")) {
                                if (favListBookRP.getSubCatListBooks().size() != 0) {
                                    viewFavorite.rvFav.setVisibility(View.VISIBLE);
                                    rentAdapter = new RentAdapter(requireActivity(), favListBookRP.getSubCatListBooks());
                                    viewFavorite.rvFav.setAdapter(rentAdapter);
                                    rentAdapter.setOnItemClickListener(new OnClick() {
                                        @Override
                                        public void position(int position) {
                                            Intent intentDetail = new Intent(requireActivity(), BookDetailsActivity.class);
                                            intentDetail.putExtra("BOOK_ID", favListBookRP.getSubCatListBooks().get(position).getPost_id());
                                            startActivity(intentDetail);
                                        }
                                    });

                                } else {
                                    viewFavorite.llNoData.clNoDataFound.setVisibility(View.VISIBLE);
                                    viewFavorite.llNoData.textViewNoDataNoDataFound.setText(getString(R.string.no_rent));
                                    viewFavorite.rvFav.setVisibility(View.GONE);
                                    viewFavorite.progressFav.setVisibility(View.GONE);
                                }
                            } else {
                                viewFavorite.llNoData.clNoDataFound.setVisibility(View.VISIBLE);
                                viewFavorite.rvFav.setVisibility(View.GONE);
                                viewFavorite.progressFav.setVisibility(View.GONE);
                                method.alertBox(getString(R.string.failed_try_again));
                            }

                        } catch (Exception e) {
                            Log.d("exception_error", e.toString());
                            method.alertBox(getString(R.string.failed_try_again));
                        }
                    }
                    viewFavorite.progressFav.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(@NotNull Call<SubCatListBookRP> call, @NotNull Throwable t) {
                    // Log error here since request failed
                    Log.e("fail", t.toString());
                    viewFavorite.llNoData.clNoDataFound.setVisibility(View.VISIBLE);
                    viewFavorite.progressFav.setVisibility(View.GONE);
                    method.alertBox(getString(R.string.failed_try_again));
                }
            });
        }
    }

}
