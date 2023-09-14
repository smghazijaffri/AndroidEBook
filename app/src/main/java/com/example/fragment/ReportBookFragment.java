package com.example.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.androidebookapps.R;
import com.example.androidebookapps.databinding.LayoutReportBookBinding;
import com.example.response.PostRateRP;
import com.example.rest.ApiClient;
import com.example.rest.ApiInterface;
import com.example.util.API;
import com.example.util.Method;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ReportBookFragment extends BottomSheetDialogFragment {

    LayoutReportBookBinding viewReportBookBinding;
    String postId, userId;
    ProgressDialog progressDialog;
    Method method;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        viewReportBookBinding = LayoutReportBookBinding.inflate(inflater, container, false);
        method = new Method(requireActivity());
        method.forceRTLIfSupported();

        if (getArguments() != null) {
            postId = getArguments().getString("postId");
            userId = getArguments().getString("userId");
        }
        progressDialog = new ProgressDialog(requireActivity(), R.style.MyAlertDialogStyle);
        viewReportBookBinding.btnMaybeLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        viewReportBookBinding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewReportBookBinding.etYourReviewText.getText().toString().isEmpty()) {
                    Toast.makeText(requireActivity(), getString(R.string.lbl_detail_report_msg), Toast.LENGTH_SHORT).show();
                } else {
                    dismiss();
                    sendRateReviewData();
                }
            }
        });
        return viewReportBookBinding.getRoot();
    }


    private void sendRateReviewData() {
        if (getActivity() != null) {
            progressDialog.show();
            progressDialog.setMessage(getResources().getString(R.string.loading));
            progressDialog.setCancelable(false);

            JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(requireActivity()));
            jsObj.addProperty("post_id", postId);
            jsObj.addProperty("user_id", userId);
            jsObj.addProperty("post_type", "Book");
            jsObj.addProperty("review_id", "0");
            jsObj.addProperty("message", viewReportBookBinding.etYourReviewText.getText().toString());
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<PostRateRP> call = apiService.getReportBookData(API.toBase64(jsObj.toString()));
            call.enqueue(new Callback<PostRateRP>() {
                @Override
                public void onResponse(@NotNull Call<PostRateRP> call, @NotNull Response<PostRateRP> response) {
                    if (getActivity() != null) {
                        try {

                            PostRateRP postRateRP = response.body();

                            if (postRateRP != null && postRateRP.getSuccess().equals("1")) {
                                if (postRateRP.getPostRateLists().size() != 0) {
                                    method.alertBox(postRateRP.getPostRateLists().get(0).getMsg());

                                } else {
                                    progressDialog.dismiss();
                                    method.alertBox(getResources().getString(R.string.failed_try_again));
                                }
                            } else {
                                progressDialog.dismiss();
                                method.alertBox(getResources().getString(R.string.failed_try_again));
                            }

                        } catch (Exception e) {
                            Log.d("exception_error", e.toString());
                            method.alertBox(getResources().getString(R.string.failed_try_again));
                        }
                    }
                    progressDialog.dismiss();
                }


                @Override
                public void onFailure(@NotNull Call<PostRateRP> call, @NotNull Throwable t) {
                    // Log error here since request failed
                    Log.e("fail", t.toString());
                    progressDialog.dismiss();
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }
            });
        }
    }
}