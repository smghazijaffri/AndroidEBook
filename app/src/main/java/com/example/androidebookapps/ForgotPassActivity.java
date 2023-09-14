package com.example.androidebookapps;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidebookapps.databinding.ActivityForgotPasswordBinding;
import com.example.response.RegisterRP;
import com.example.rest.ApiClient;
import com.example.rest.ApiInterface;
import com.example.util.API;
import com.example.util.Method;
import com.example.util.StatusBar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPassActivity extends AppCompatActivity {

    ActivityForgotPasswordBinding viewForPassBinding;
    ProgressDialog progressDialog;
    Method method;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.init(ForgotPassActivity.this);

        viewForPassBinding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(viewForPassBinding.getRoot());

        method = new Method(ForgotPassActivity.this);
        method.forceRTLIfSupported();

        progressDialog = new ProgressDialog(ForgotPassActivity.this,R.style.MyAlertDialogStyle);
        viewForPassBinding.toolbarMain.tvToolbarTitle.setText(getString(R.string.lbl_forgot_password));

        viewForPassBinding.toolbarMain.imageArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        viewForPassBinding.btnForgotSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = viewForPassBinding.edtForgotEmail.getText().toString();
                viewForPassBinding.edtForgotEmail.setError(null);

                if (!isValidMail(string) || string.isEmpty()) {
                    viewForPassBinding.edtForgotEmail.requestFocus();
                    viewForPassBinding.edtForgotEmail.setError(getResources().getString(R.string.please_enter_email));
                } else {

                    viewForPassBinding.edtForgotEmail.clearFocus();

                    if (method.isNetworkAvailable()) {
                        forgetPassword(string);
                    } else {
                        method.alertBox(getResources().getString(R.string.internet_connection));
                    }
                }
            }
        });

    }

    private boolean isValidMail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void forgetPassword(String email) {

        progressDialog.show();
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);

        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(ForgotPassActivity.this));
        jsObj.addProperty("email", email);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<RegisterRP> call = apiService.getForgotData(API.toBase64(jsObj.toString()));
        call.enqueue(new Callback<RegisterRP>() {
            @Override
            public void onResponse(@NotNull Call<RegisterRP> call, @NotNull Response<RegisterRP> response) {

                try {
                    RegisterRP dataRP = response.body();

                    if (dataRP !=null && dataRP.getSuccess().equals("1")) {
                        if (dataRP.getItemUserListRegister().get(0).getSuccess().equals("1")) {
                            viewForPassBinding.edtForgotEmail.setText("");
                        }
                        method.alertBox(dataRP.getItemUserListRegister().get(0).getMsg());

                    } else {
                        method.alertBox(getResources().getString(R.string.failed_try_again));
                    }

                } catch (Exception e) {
                    Log.d("exception_error", e.toString());
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }

                progressDialog.dismiss();

            }

            @Override
            public void onFailure(@NotNull Call<RegisterRP> call, @NotNull Throwable t) {
                // Log error here since request failed
                Log.e("fail", t.toString());
                progressDialog.dismiss();
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