package com.example.androidebookapps;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.androidebookapps.databinding.ActivityEditProfileBinding;
import com.example.fragment.ProfileImage;
import com.example.response.EditProfileRP;
import com.example.rest.ApiClient;
import com.example.rest.ApiInterface;
import com.example.util.API;
import com.example.util.Events;
import com.example.util.GlobalBus;
import com.example.util.Method;
import com.example.util.StatusBar;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.greenrobot.eventbus.Subscribe;
import org.jetbrains.annotations.NotNull;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EditProfileActivity extends AppCompatActivity {

    ActivityEditProfileBinding viewEditProfile;
    Method method;
    String uId, uName, uEmail, uImage, uPhone, uType;
    boolean isProfile = false;
    ProgressDialog progressDialog;
    String imageProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.initWhite(EditProfileActivity.this);
        viewEditProfile = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(viewEditProfile.getRoot());

        method = new Method(EditProfileActivity.this);
        method.forceRTLIfSupported();

        GlobalBus.getBus().register(this);
        progressDialog = new ProgressDialog(EditProfileActivity.this,R.style.MyAlertDialogStyle);

        Intent intent = getIntent();
        uId = intent.getStringExtra("uId");
        uName = intent.getStringExtra("uName");
        uEmail = intent.getStringExtra("uEmail");
        uImage = intent.getStringExtra("uImage");
        uPhone = intent.getStringExtra("uPhone");
        uType = intent.getStringExtra("uType");

        viewEditProfile.toolbarMain.tvToolbarTitle.setText(getString(R.string.profile_edt_title));
        viewEditProfile.toolbarMain.ivSearch.setVisibility(View.GONE);
        viewEditProfile.toolbarMain.imageFilter.setVisibility(View.GONE);
        viewEditProfile.toolbarMain.imageArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Glide.with(EditProfileActivity.this.getApplicationContext()).load(uImage)
                .placeholder(R.drawable.user_profile)
                .into(viewEditProfile.ivUser);
        viewEditProfile.tvProfileName.setText(uName);
        viewEditProfile.tvProfileEmail.setText(uEmail);
        viewEditProfile.edtName.setText(uName);
        viewEditProfile.edtEmail.setText(uEmail);
        viewEditProfile.edtPhone.setText(uPhone);

        viewEditProfile.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

        viewEditProfile.ivEditIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialogFragment fragment = new ProfileImage();
                fragment.show(getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");
            }
        });

    }

    @Subscribe
    public void getData(Events.ProImage proImage) {
        isProfile = proImage.isProfile();
        if (proImage.isProfile()) {
            imageProfile = proImage.getImagePath();
            Uri uri = Uri.fromFile(new File(imageProfile));
            Glide.with(getApplicationContext()).load(uri)
                    .placeholder(R.drawable.user_profile)
                    .into(viewEditProfile.ivUser);
        }

    }

    private void save() {

        String name = viewEditProfile.edtName.getText().toString();
        String phoneNo = viewEditProfile.edtPhone.getText().toString();

        viewEditProfile.edtName.setError(null);
        viewEditProfile.edtPhone.setError(null);

        if (name.equals("") || name.isEmpty()) {
            viewEditProfile.edtName.requestFocus();
            viewEditProfile.edtName.setError(getResources().getString(R.string.please_enter_name));
        } else if (phoneNo.equals("") || phoneNo.isEmpty()) {
            viewEditProfile.edtPhone.requestFocus();
            viewEditProfile.edtPhone.setError(getResources().getString(R.string.please_enter_phone));
        } else {
            if (method.isNetworkAvailable()) {

                viewEditProfile.edtName.clearFocus();
                viewEditProfile.edtPhone.clearFocus();
                String pass=viewEditProfile.edtPassword.getText().toString();
                if (pass.isEmpty()){
                    profileUpdate(method.getUserId(), name,uEmail,"", phoneNo, imageProfile);
                }else {
                    profileUpdate(method.getUserId(), name,uEmail,pass, phoneNo, imageProfile);
                }

            } else {
                method.alertBox(getResources().getString(R.string.internet_connection));
            }
        }

    }

    public void profileUpdate(String userId, String sendName,String sendEmail,String sendPass, String sendPhone, String profile_image) {


        progressDialog.show();
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);

        MultipartBody.Part body = null;

        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(EditProfileActivity.this));
        jsObj.addProperty("user_id", userId);
        jsObj.addProperty("name", sendName);
        jsObj.addProperty("email", sendEmail);
        jsObj.addProperty("password", sendPass);
        jsObj.addProperty("phone", sendPhone);
        if (isProfile) {
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), new File(profile_image));
            // MultipartBody.Part is used to send also the actual file name
            body = MultipartBody.Part.createFormData("user_image", new File(profile_image).getName(), requestFile);
        }
        // add another part within the multipart request
        RequestBody requestBody_data =
                RequestBody.create(MediaType.parse("multipart/form-data"), API.toBase64(jsObj.toString()));
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<EditProfileRP> call = apiService.getEditProfileData(requestBody_data, body);
        call.enqueue(new Callback<EditProfileRP>() {
            @Override
            public void onResponse(@NotNull Call<EditProfileRP> call, @NotNull Response<EditProfileRP> response) {

                try {
                    EditProfileRP editProfileRP = response.body();

                    if (editProfileRP !=null && editProfileRP.getSuccess().equals("1")) {
                        if (editProfileRP.getItemUserEdits().get(0).getSuccess().equals("1")) {
                            method.savePhone(sendPhone);
                            Events.ProfileUpdate profileUpdate = new Events.ProfileUpdate(editProfileRP.getItemUserEdits().get(0).getUser_image(),viewEditProfile.edtName.getText().toString(),sendPhone);
                            GlobalBus.getBus().post(profileUpdate);
                            Toast.makeText(EditProfileActivity.this, editProfileRP.getItemUserEdits().get(0).getMsg(), Toast.LENGTH_SHORT).show();
                            onBackPressed();
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
            public void onFailure(@NotNull Call<EditProfileRP> call, @NotNull Throwable t) {
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
