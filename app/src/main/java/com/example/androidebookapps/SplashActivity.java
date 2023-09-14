package com.example.androidebookapps;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.applovin.sdk.AppLovinMediationProvider;
import com.applovin.sdk.AppLovinSdk;
import com.example.androidebookapps.databinding.ActivitySplashBinding;
import com.example.response.AppDetailRP;
import com.example.rest.ApiClient;
import com.example.rest.ApiInterface;
import com.example.util.API;
import com.example.util.Constant;
import com.example.util.Method;
import com.example.util.StatusBar;
import com.facebook.FacebookSdk;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;
import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.UnityAds;
import com.wortise.ads.WortiseSdk;
import com.wortise.ads.consent.ConsentManager;

import org.jetbrains.annotations.NotNull;

import java.util.Currency;

import kotlin.Unit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    ActivitySplashBinding viewSplashBinding;
    Method method;
    static int SPLASH_TIME_OUT = 1000;
    String str_package;
    Boolean isCancelled = false;
    int WAIT = 3000;
    private String id = "0", type = "", title = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.init(SplashActivity.this);
        method = new Method(SplashActivity.this);
        method.forceRTLIfSupported();

        switch (method.themMode()) {
            case "system":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
            case "light":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case "dark":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
        }

        viewSplashBinding = ActivitySplashBinding.inflate(getLayoutInflater());
        View view = viewSplashBinding.getRoot();
        setContentView(view);

        FacebookSdk.sdkInitialize(getApplicationContext());

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("id")) {
                id = intent.getStringExtra("id");
                type = intent.getStringExtra("type");
                title = intent.getStringExtra("title");
            } else {
                Uri data = intent.getData();
                if (data != null) {
                    id = data.getLastPathSegment();
                    type = "deepLink";
                }
            }
        }


        if (method.isNetworkAvailable()) {
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                if (method.getIsLogin()) {
                    appDetailData(method.getUserId());
                } else {
                    appDetailData("0");
                }

            }, SPLASH_TIME_OUT);
        } else {
            Intent intentMain = new Intent(getApplicationContext(), DownloadActivity.class);
            intentMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intentMain);
            finishAffinity();
        }

    }

    private void appDetailData(String userId) {

        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(SplashActivity.this));
        jsObj.addProperty("user_id", userId);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<AppDetailRP> call = apiService.getAppDetailData(API.toBase64(jsObj.toString()));
        call.enqueue(new Callback<AppDetailRP>() {
            @Override
            public void onResponse(@NotNull Call<AppDetailRP> call, @NotNull Response<AppDetailRP> response) {

                try {

                    AppDetailRP appDetailRP = response.body();
                    if (appDetailRP != null && appDetailRP.getSuccess().equals("1")) {
                        if (appDetailRP.getAppLists().size() != 0) {
                            Currency currency = Currency.getInstance(appDetailRP.getAppLists().get(0).getCurrencyCode());
                            Constant.appListData = appDetailRP.getAppLists().get(0);
                            if (!Constant.appListData.getAdsList().isEmpty()) {
                                Constant.adsInfo = Constant.appListData.getAdsList().get(0).getAdsInfo();
                                if (Constant.appListData.getAdsList().get(0).getAdsInfo().getNativeOnOff() != null) {
                                    Constant.isNative = Constant.appListData.getAdsList().get(0).getAdsInfo().getNativeOnOff().equals("1");
                                    Constant.nativeId = Constant.adsInfo.getNativeId();
                                    Constant.nativePosition = Constant.adsInfo.getNativePosition();
                                }
                                Constant.isBanner = Constant.appListData.getAdsList().get(0).getAdsInfo().getBannerOnOff().equals("1");
                                Constant.isInterstitial = Constant.appListData.getAdsList().get(0).getAdsInfo().getInterstitialOnOff().equals("1");
                                Constant.publisherId = Constant.adsInfo.getPublisherId();
                                Constant.bannerId = Constant.adsInfo.getBannerId();
                                Constant.interstitialId = Constant.adsInfo.getInterstitialId();
                                Constant.adNetworkType = Constant.appListData.getAdsList().get(0).getAdsName();
                                Constant.interstitialClick = Constant.adsInfo.getInterstitialClicks();
                                initializeAds();

                            }
                            Constant.appUpdateVersion = Constant.appListData.getAppUpdateVersionCode();
                            Constant.appUpdateUrl = Constant.appListData.getAppUpdateLink();
                            Constant.appUpdateDesc = Constant.appListData.getAppUpdateDesc();
                            Constant.isAppUpdate = Constant.appListData.getAppUpdateHideShow().equals("true");
                            Constant.isAppUpdateCancel = Constant.appListData.getAppUpdateCancelOption().equals("true");

                            String checkLogin = appDetailRP.getUser_status();
                            String symbol = currency.getSymbol();
                            str_package = appDetailRP.getAppLists().get(0).getAppPackageName();
                            Constant.constantCurrency = symbol;

                            if (str_package.equals(getPackageName())) {
                                new Handler().postDelayed(() -> {
                                    if (!isCancelled) {
                                        if (method.isWelcome()) {
                                            startActivity(new Intent(SplashActivity.this, IntroActivity.class));
                                            finish();
                                            method.setFirstWelcome(false);
                                        } else {
                                            if (method.getIsLogin()) {
                                                if (checkLogin.equals("true")) {
                                                    callActivity();
                                                } else {
                                                    method.saveIsLogin(false);
                                                    method.setUserId("");
                                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    startActivity(intent);
                                                    finish();
                                                    Toast.makeText(SplashActivity.this, getString(R.string.logout_disable), Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                callActivity();
                                            }
                                        }
                                    }

                                }, WAIT);
                            } else {
                                method.alertBox(getResources().getString(R.string.license_msg));
                            }
                        } else {
                            method.alertBox(getResources().getString(R.string.wrong));
                        }
                    } else {

                        method.alertBox(getResources().getString(R.string.failed_try_again));
                    }

                } catch (Exception e) {
                    Log.d("exception_error", e.toString());
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }
            }

            @Override
            public void onFailure(@NotNull Call<AppDetailRP> call, @NotNull Throwable t) {
                // Log error here since request failed
                Log.e("fail", t.toString());
                method.alertBox(getResources().getString(R.string.failed_try_again));
            }
        });
    }

    private void callActivity() {
        switch (type) {
            case "category":
                Intent intentSubCat = new Intent(SplashActivity.this, SubCategoryActivity.class);
                intentSubCat.putExtra("postCatId", id);
                intentSubCat.putExtra("postCatName", title);
                startActivity(intentSubCat);
                finishAffinity();
                break;
            case "authors":
                Intent intentAu = new Intent(SplashActivity.this, AuthorDetailsActivity.class);
                intentAu.putExtra("AUTHOR_ID", id);
                startActivity(intentAu);
                finishAffinity();
                break;
            case "books":
                startActivity(new Intent(SplashActivity.this, BookDetailsActivity.class)
                        .putExtra("BOOK_ID", id));
                finishAffinity();
                break;
            case "deepLink":
                startActivity(new Intent(SplashActivity.this, BookDetailsActivity.class)
                        .putExtra("BOOK_ID", id)
                        .putExtra("position", 0)
                        .putExtra("type", "external"));
                finishAffinity();
                break;
            default:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finishAffinity();
                break;
        }
    }

    private void initializeAds() {
        switch (Constant.adNetworkType) {
            case "Unity Ads":
                UnityAds.initialize(SplashActivity.this, Constant.adsInfo.getPublisherId(), false, new IUnityAdsInitializationListener() {
                    @Override
                    public void onInitializationComplete() {
                        Log.d(TAG, "Unity Ads Initialization Complete");
                    }

                    @Override
                    public void onInitializationFailed(UnityAds.UnityAdsInitializationError error, String message) {
                        Log.d(TAG, "Unity Ads Initialization Failed: [" + error + "] " + message);
                    }
                });
                break;
            case "AppLovins MAX":
                AppLovinSdk.getInstance(SplashActivity.this).setMediationProvider(AppLovinMediationProvider.MAX);
                AppLovinSdk.getInstance(SplashActivity.this).initializeSdk(config -> {

                });
                break;
            case "StartApp":
                StartAppSDK.init(this, Constant.adsInfo.getPublisherId(), false);
                StartAppAd.disableSplash();

                break;
            case "Wortise":
                WortiseSdk.initialize(this, Constant.adsInfo.getPublisherId(), true, () -> {
                    ConsentManager.requestOnce(SplashActivity.this);
                    return Unit.INSTANCE;
                });
                break;
        }

    }

    @Override
    protected void onDestroy() {
        isCancelled = true;
        super.onDestroy();
    }
}