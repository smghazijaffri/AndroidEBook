package com.example.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.example.androidebookapps.R;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.CacheFlag;
import com.facebook.ads.InterstitialAdListener;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.ixidev.gdpr.GDPRChecker;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.adlisteners.AdDisplayListener;
import com.startapp.sdk.adsbase.adlisteners.AdEventListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAds;

public class AdInterstitialAds {

    public static ProgressDialog pDialog;


    public static void ShowInterstitialAds(Context context,int adapterPosition,OnClick onClick) {

        if (Constant.isInterstitial) {
            switch (Constant.adNetworkType) {
                case "Admob":
                    Constant.AD_COUNT++;
                    if (Constant.AD_COUNT == Constant.interstitialClick) {
                        Constant.AD_COUNT = 0;
                        Loading(context);
                        GDPRChecker.Request request = GDPRChecker.getRequest();
                        AdRequest.Builder builder = new AdRequest.Builder();
                        if (request == GDPRChecker.Request.NON_PERSONALIZED) {
                            Bundle extras = new Bundle();
                            extras.putString("npa", "1");
                            builder.addNetworkExtrasBundle(AdMobAdapter.class, extras);
                        }
                        InterstitialAd.load(context, Constant.interstitialId, builder.build(), new InterstitialAdLoadCallback() {
                            @Override
                            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                                super.onAdLoaded(interstitialAd);
                                interstitialAd.show((Activity) context);
                                pDialog.dismiss();
                                interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        super.onAdDismissedFullScreenContent();
                                        onClick.position(adapterPosition);
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(com.google.android.gms.ads.AdError adError) {
                                        super.onAdFailedToShowFullScreenContent(adError);
                                        pDialog.dismiss();
                                        onClick.position(adapterPosition);
                                    }
                                });
                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                super.onAdFailedToLoad(loadAdError);
                                pDialog.dismiss();
                                onClick.position(adapterPosition);
                            }
                        });
                    } else {
                        onClick.position(adapterPosition);
                    }
                    break;
                case "Facebook":
                    Constant.AD_COUNT++;
                    if (Constant.AD_COUNT == Constant.interstitialClick) {
                        Constant.AD_COUNT = 0;
                        Loading(context);
                        final com.facebook.ads.InterstitialAd mInterstitialfb = new com.facebook.ads.InterstitialAd(context, Constant.interstitialId);
                        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
                            @Override
                            public void onInterstitialDisplayed(Ad ad) {
                            }

                            @Override
                            public void onInterstitialDismissed(Ad ad) {
                                onClick.position(adapterPosition);
                            }

                            @Override
                            public void onError(Ad ad, AdError adError) {
                                pDialog.dismiss();
                                onClick.position(adapterPosition);
                            }

                            @Override
                            public void onAdLoaded(Ad ad) {
                                pDialog.dismiss();
                                mInterstitialfb.show();
                            }

                            @Override
                            public void onAdClicked(Ad ad) {
                            }

                            @Override
                            public void onLoggingImpression(Ad ad) {
                            }
                        };
                        com.facebook.ads.InterstitialAd.InterstitialLoadAdConfig loadAdConfig = mInterstitialfb.buildLoadAdConfig().withAdListener(interstitialAdListener).withCacheFlags(CacheFlag.ALL).build();
                        mInterstitialfb.loadAd(loadAdConfig);
                    } else {
                        onClick.position(adapterPosition);
                    }
                    break;
                case "Unity Ads":
                    Constant.AD_COUNT++;
                    if (Constant.AD_COUNT == Constant.interstitialClick) {
                        Constant.AD_COUNT = 0;
                        Loading(context);
                        UnityAds.show((Activity) context, Constant.interstitialId, new IUnityAdsShowListener() {
                            @Override
                            public void onUnityAdsShowFailure(String s, UnityAds.UnityAdsShowError unityAdsShowError, String s1) {
                                pDialog.dismiss();
                                onClick.position(adapterPosition);
                            }

                            @Override
                            public void onUnityAdsShowStart(String s) {
                            }

                            @Override
                            public void onUnityAdsShowClick(String s) {
                            }

                            @Override
                            public void onUnityAdsShowComplete(String s, UnityAds.UnityAdsShowCompletionState unityAdsShowCompletionState) {
                                pDialog.dismiss();
                                onClick.position(adapterPosition);
                            }
                        });

                    } else {
                        onClick.position(adapterPosition);
                    }
                    break;
                case "StartApp":
                    Constant.AD_COUNT++;
                    if (Constant.AD_COUNT == Constant.interstitialClick) {
                        Constant.AD_COUNT = 0;
                        Loading(context);
                        StartAppAd startAppAd = new StartAppAd(context);
                        startAppAd.loadAd(new AdEventListener() {
                            @Override
                            public void onReceiveAd(@NonNull com.startapp.sdk.adsbase.Ad ad) {
                                pDialog.dismiss();
                                startAppAd.showAd(new AdDisplayListener() {
                                    @Override
                                    public void adHidden(com.startapp.sdk.adsbase.Ad ad) {
                                        pDialog.dismiss();
                                        onClick.position(adapterPosition);
                                    }

                                    @Override
                                    public void adDisplayed(com.startapp.sdk.adsbase.Ad ad) {

                                    }

                                    @Override
                                    public void adClicked(com.startapp.sdk.adsbase.Ad ad) {
                                        pDialog.dismiss();
                                    }

                                    @Override
                                    public void adNotDisplayed(com.startapp.sdk.adsbase.Ad ad) {
                                        pDialog.dismiss();
                                        onClick.position(adapterPosition);
                                    }
                                });
                            }

                            @Override
                            public void onFailedToReceiveAd(@Nullable com.startapp.sdk.adsbase.Ad ad) {
                                pDialog.dismiss();
                                onClick.position(adapterPosition);
                            }
                        });

                    } else {
                        onClick.position(adapterPosition);
                    }
                    break;
                case "AppLovins MAX":
                    Constant.AD_COUNT++;
                    if (Constant.AD_COUNT == Constant.interstitialClick) {
                        Constant.AD_COUNT = 0;
                        Loading(context);
                        MaxInterstitialAd maxInterstitialAd = new MaxInterstitialAd(Constant.interstitialId, (Activity) context);
                        maxInterstitialAd.setListener(new MaxAdListener() {
                            @Override
                            public void onAdLoaded(MaxAd ad) {
                                pDialog.dismiss();
                                maxInterstitialAd.showAd();
                            }

                            @Override
                            public void onAdDisplayed(MaxAd ad) {
                            }

                            @Override
                            public void onAdHidden(MaxAd ad) {
                                pDialog.dismiss();
                                onClick.position(adapterPosition);
                            }

                            @Override
                            public void onAdClicked(MaxAd ad) {
                            }

                            @Override
                            public void onAdLoadFailed(String adUnitId, MaxError error) {
                                pDialog.dismiss();
                                onClick.position(adapterPosition);
                            }

                            @Override
                            public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                                pDialog.dismiss();
                                onClick.position(adapterPosition);
                            }
                        });
                        // Load the first ad
                        maxInterstitialAd.loadAd();

                    } else {
                        onClick.position(adapterPosition);
                    }
                    break;
                case "Wortise":
                    Constant.AD_COUNT++;
                    if (Constant.AD_COUNT == Constant.interstitialClick) {
                        Constant.AD_COUNT = 0;
                        Loading(context);
                        com.wortise.ads.interstitial.InterstitialAd wInterstitial = new com.wortise.ads.interstitial.InterstitialAd(context, Constant.interstitialId);
                        wInterstitial.setListener(new com.wortise.ads.interstitial.InterstitialAd.Listener() {
                            @Override
                            public void onInterstitialClicked(@NonNull com.wortise.ads.interstitial.InterstitialAd interstitialAd) {

                            }

                            @Override
                            public void onInterstitialDismissed(@NonNull com.wortise.ads.interstitial.InterstitialAd interstitialAd) {
                                pDialog.dismiss();
                                onClick.position(adapterPosition);
                            }

                            @Override
                            public void onInterstitialFailed(@NonNull com.wortise.ads.interstitial.InterstitialAd interstitialAd, @NonNull com.wortise.ads.AdError adError) {
                                pDialog.dismiss();
                                onClick.position(adapterPosition);
                            }

                            @Override
                            public void onInterstitialLoaded(@NonNull com.wortise.ads.interstitial.InterstitialAd interstitialAd) {
                                if (wInterstitial.isAvailable()) {
                                    wInterstitial.showAd();
                                }
                            }

                            @Override
                            public void onInterstitialShown(@NonNull com.wortise.ads.interstitial.InterstitialAd interstitialAd) {
                                pDialog.dismiss();
                            }
                        });
                        wInterstitial.loadAd();

                    } else {
                        onClick.position(adapterPosition);
                    }
                    break;
            }
        } else {
            onClick.position(adapterPosition);
        }
    }

    public static void Loading(Context context) {
        pDialog = new ProgressDialog(context,R.style.MyAlertDialogStyle);
        pDialog.setMessage(context.getResources().getString(R.string.loading));
        pDialog.setCancelable(false);
        pDialog.show();
    }


}
