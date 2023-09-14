package com.example.androidebookapps;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.adapter.SettingAdapter;
import com.example.androidebookapps.databinding.ActivitySettingsBinding;
import com.example.fragment.ThemeChangeFragment;
import com.example.response.AppDetailRP;
import com.example.rest.ApiClient;
import com.example.rest.ApiInterface;
import com.example.util.API;
import com.example.util.Constant;
import com.example.util.Method;
import com.example.util.OnClick;
import com.example.util.StatusBar;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.onesignal.OneSignal;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SettingsActivity extends AppCompatActivity {

    ActivitySettingsBinding viewSetting;
    Method method;
    SettingAdapter settingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.initWhite(SettingsActivity.this);
        viewSetting = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(viewSetting.getRoot());
        method = new Method(SettingsActivity.this);
        method.forceRTLIfSupported();

        viewSetting.toolbarMain.tvToolbarTitle.setText(getString(R.string.lbl_setting));
        viewSetting.toolbarMain.ivSearch.setVisibility(View.GONE);
        viewSetting.toolbarMain.imageArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        viewSetting.progressHome.setVisibility(View.GONE);
        viewSetting.llNoData.clNoDataFound.setVisibility(View.GONE);
        viewSetting.nsSettingScroll.setVisibility(View.GONE);

        viewSetting.rvSettingList.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(SettingsActivity.this, 1);
        viewSetting.rvSettingList.setLayoutManager(layoutManager);
        viewSetting.rvSettingList.setFocusable(false);

        int[][] states = new int[][]{
                new int[]{-android.R.attr.state_checked},
                new int[]{android.R.attr.state_checked},
        };

        int[] thumbColors = new int[]{
                ContextCompat.getColor(SettingsActivity.this, R.color.switch_thumb_disable),
                ContextCompat.getColor(SettingsActivity.this, R.color.switch_thumb_enable),
        };

        int[] trackColors = new int[]{
                ContextCompat.getColor(SettingsActivity.this, R.color.switch_track_disable),
                ContextCompat.getColor(SettingsActivity.this, R.color.switch_track),
        };
        DrawableCompat.setTintList(DrawableCompat.wrap(viewSetting.scNotification.getThumbDrawable()), new ColorStateList(states, thumbColors));
        DrawableCompat.setTintList(DrawableCompat.wrap(viewSetting.scNotification.getTrackDrawable()), new ColorStateList(states, trackColors));

        if (method.getIsLogin()) {
            viewSetting.tvSettingLogIn.setText(getString(R.string.setting_log_out));
        } else {
            viewSetting.tvSettingLogIn.setText(getString(R.string.setting_log_in));
        }
        viewSetting.rlLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (method.getIsLogin()) {
                    logoutDialog();
                } else {
                    Intent intentLogIn = new Intent(SettingsActivity.this, LoginActivity.class);
                    startActivity(intentLogIn);
                    finish();
                }
            }
        });

        viewSetting.scNotification.setChecked(method.pref.getBoolean(method.notification, true));
        viewSetting.scNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                OneSignal.disablePush(!isChecked);
                OneSignal.unsubscribeWhenNotificationsAreDisabled(isChecked);
                method.editor.putBoolean(method.notification, isChecked);
                method.editor.commit();
            }
        });

        viewSetting.rlDarkMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                ThemeChangeFragment themeChangeFragment = new ThemeChangeFragment();
                themeChangeFragment.setArguments(bundle);
                themeChangeFragment.show(getSupportFragmentManager(), themeChangeFragment.getTag());

            }
        });

        viewSetting.rlMoreApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constant.appListData.getGooglePlayLink().isEmpty()) {
                    Toast.makeText(SettingsActivity.this, getString(R.string.no_link_found), Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.appListData.getGooglePlayLink())));
                }
            }
        });

        viewSetting.rlRateApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appName = getPackageName();//your application package name i.e play store application url
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("market://details?id="
                                    + appName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id="
                                    + appName)));
                }
            }
        });

        viewSetting.rlShareApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentShare = new Intent(Intent.ACTION_SEND);
                intentShare.setType("text/plain");
                intentShare.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_msg)+"\n" + "http://play.google.com/store/apps/details?id=" + getPackageName());
                startActivity(intentShare);
            }
        });

        if (method.isNetworkAvailable()) {
            settingPageData();
        } else {
            method.alertBox(getResources().getString(R.string.internet_connection));
        }

    }

    private void settingPageData() {

        viewSetting.progressHome.setVisibility(View.VISIBLE);

        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(SettingsActivity.this));
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<AppDetailRP> call = apiService.getSettingPageData(API.toBase64(jsObj.toString()));
        call.enqueue(new Callback<AppDetailRP>() {
            @Override
            public void onResponse(@NotNull Call<AppDetailRP> call, @NotNull Response<AppDetailRP> response) {

                try {

                    AppDetailRP appDetailRP = response.body();

                     if (appDetailRP !=null && appDetailRP.getSuccess().equals("1")) {
                        if (appDetailRP.getAppLists().get(0).getPageList().size() != 0) {
                            settingAdapter = new SettingAdapter(SettingsActivity.this, appDetailRP.getAppLists().get(0).getPageList());
                            viewSetting.rvSettingList.setAdapter(settingAdapter);
                            settingAdapter.setOnItemClickListener(new OnClick() {
                                @Override
                                public void position(int position) {
                                    if (appDetailRP.getAppLists().get(0).getPageList().get(position).getPageId().equals("1")) {
                                        Intent intentAbout = new Intent(SettingsActivity.this, AboutUsActivity.class);
                                        intentAbout.putExtra("ABOUT", appDetailRP.getAppLists().get(0).getPageList().get(position).getPageContent());
                                        startActivity(intentAbout);
                                    } else {
                                        Intent intentPage = new Intent(SettingsActivity.this, PagesActivity.class);
                                        intentPage.putExtra("PAGE_TITLE", appDetailRP.getAppLists().get(0).getPageList().get(position).getPageTitle());
                                        intentPage.putExtra("PAGE_DESC", appDetailRP.getAppLists().get(0).getPageList().get(position).getPageContent());
                                        startActivity(intentPage);
                                    }

                                }
                            });

                        } else {
                            viewSetting.llNoData.clNoDataFound.setVisibility(View.VISIBLE);
                            viewSetting.rvSettingList.setVisibility(View.GONE);
                            viewSetting.progressHome.setVisibility(View.GONE);
                        }
                    } else {
                        viewSetting.llNoData.clNoDataFound.setVisibility(View.VISIBLE);
                        viewSetting.rvSettingList.setVisibility(View.GONE);
                        viewSetting.progressHome.setVisibility(View.GONE);
                        method.alertBox(getResources().getString(R.string.failed_try_again));
                    }

                } catch (Exception e) {
                    Log.d("exception_error", e.toString());
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }

                viewSetting.progressHome.setVisibility(View.GONE);
                viewSetting.llNoData.clNoDataFound.setVisibility(View.GONE);
                viewSetting.nsSettingScroll.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(@NotNull Call<AppDetailRP> call, @NotNull Throwable t) {
                // Log error here since request failed
                Log.e("fail", t.toString());
                viewSetting.llNoData.clNoDataFound.setVisibility(View.VISIBLE);
                viewSetting.progressHome.setVisibility(View.GONE);
                viewSetting.nsSettingScroll.setVisibility(View.VISIBLE);
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

    public void logoutDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(SettingsActivity.this);
        dialog.setContentView(R.layout.layout_logout);
        if (method.isRtl()) {
            dialog.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        AppCompatButton buttonCancel = dialog.findViewById(R.id.btnMaybeLater);
        AppCompatButton buttonYes = dialog.findViewById(R.id.btnSubmit);

        buttonCancel.setOnClickListener(v -> dialog.dismiss());

        buttonYes.setOnClickListener(v -> {
            if (method.getUserType().equals("google")) {

                // Configure sign-in to request the ic_user_login's ID, email address, and basic
                // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build();

                // Build a GoogleSignInClient with the options specified by gso.
                //Google login
                GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(SettingsActivity.this, gso);

                mGoogleSignInClient.signOut()
                        .addOnCompleteListener(SettingsActivity.this, task -> {
                            method.saveIsLogin(false);
                            method.setUserId("");
                            startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
                            finishAffinity();
                        });
            } else if (method.getUserType().equals("facebook")) {
                LoginManager.getInstance().logOut();
                method.saveIsLogin(false);
                method.setUserId("");
                startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
                finishAffinity();
            } else {
                method.saveIsLogin(false);
                method.setUserId("");
                startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
                finishAffinity();
            }
            dialog.dismiss();
        });

        dialog.show();
    }

}
