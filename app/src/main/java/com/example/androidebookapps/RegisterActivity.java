package com.example.androidebookapps;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidebookapps.databinding.ActivityRegisterBinding;
import com.example.response.RegisterRP;
import com.example.rest.ApiClient;
import com.example.rest.ApiInterface;
import com.example.util.API;
import com.example.util.Constant;
import com.example.util.Method;
import com.example.util.StatusBar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import cn.refactor.library.SmoothCheckBox;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    ActivityRegisterBinding viewRegisterBinding;
    Method method;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.init(RegisterActivity.this);

        viewRegisterBinding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = viewRegisterBinding.getRoot();
        setContentView(view);

        method = new Method(this);
        method.forceRTLIfSupported();
        progressDialog = new ProgressDialog(this,R.style.MyAlertDialogStyle);

        viewRegisterBinding.tvRegPrivacyTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intentPage = new Intent(RegisterActivity.this, PagesActivity.class);
                    intentPage.putExtra("PAGE_TITLE", Constant.appListData.getPageList().get(1).getPageTitle());
                    intentPage.putExtra("PAGE_DESC", Constant.appListData.getPageList().get(1).getPageContent());
                    startActivity(intentPage);
            }
        });

        viewRegisterBinding.cbRegPrivacyTerms.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {

            }
        });

        viewRegisterBinding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                form();
            }
        });

        viewRegisterBinding.tvRegLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_login = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent_login);
            }
        });

    }

    private boolean isValidMail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void form() {

        String name = viewRegisterBinding.edtRegName.getText().toString();
        String email = viewRegisterBinding.edtRegEmail.getText().toString();
        String password = viewRegisterBinding.edtRegPass.getText().toString();
        String phoneNo = viewRegisterBinding.edtRegPhone.getText().toString();

        viewRegisterBinding.edtRegName.setError(null);
        viewRegisterBinding.edtRegEmail.setError(null);
        viewRegisterBinding.edtRegPass.setError(null);
        viewRegisterBinding.edtRegPhone.setError(null);

        if (name.equals("") || name.isEmpty()) {
            viewRegisterBinding.edtRegName.requestFocus();
            viewRegisterBinding.edtRegName.setError(getResources().getString(R.string.please_enter_name));
        } else if (!isValidMail(email) || email.isEmpty()) {
            viewRegisterBinding.edtRegEmail.requestFocus();
            viewRegisterBinding.edtRegEmail.setError(getResources().getString(R.string.please_enter_email));
        } else if (password.equals("") || password.isEmpty()) {
            viewRegisterBinding.edtRegPass.requestFocus();
            viewRegisterBinding.edtRegPass.setError(getResources().getString(R.string.please_enter_password));
        } else {
            viewRegisterBinding.edtRegName.clearFocus();
            viewRegisterBinding.edtRegEmail.clearFocus();
            viewRegisterBinding.edtRegPass.clearFocus();

            if (viewRegisterBinding.cbRegPrivacyTerms.isChecked()) {
                if (method.isNetworkAvailable()) {
                    register(name, email, password, phoneNo);
                } else {
                    method.alertBox(getResources().getString(R.string.internet_connection));
                }
            } else {
                Toast.makeText(RegisterActivity.this, getString(R.string.please_accept), Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void register(String sendName, String sendEmail, String sendPassword, String sendPhone) {

        progressDialog.show();
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);

        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(RegisterActivity.this));
        jsObj.addProperty("name", sendName);
        jsObj.addProperty("email", sendEmail);
        jsObj.addProperty("password", sendPassword);
        jsObj.addProperty("phone", sendPhone);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<RegisterRP> call = apiService.getRegisterData(API.toBase64(jsObj.toString()));
        call.enqueue(new Callback<RegisterRP>() {
            @Override
            public void onResponse(@NotNull Call<RegisterRP> call, @NotNull Response<RegisterRP> response) {

                try {
                    RegisterRP registerRP = response.body();

                    if (registerRP !=null && registerRP.getSuccess().equals("1")) {
                        if (registerRP.getItemUserListRegister().get(0).getSuccess().equals("1")) {
                            method.savePhone(sendPhone);
                            Toast.makeText(RegisterActivity.this, registerRP.getItemUserListRegister().get(0).getMsg(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            finishAffinity();
                        } else {
                            method.alertBox(registerRP.getItemUserListRegister().get(0).getMsg());
                        }

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
}