package com.example.androidebookapps;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.androidebookapps.databinding.ActivityMainBinding;
import com.example.fragment.AuthorFragment;
import com.example.fragment.CategoryFragment;
import com.example.fragment.HomeFragment;
import com.example.fragment.LatestFragment;
import com.example.fragment.ProfileFragment;
import com.example.util.BannerAds;
import com.example.util.Constant;
import com.example.util.Method;
import com.example.util.OnClick;
import com.example.util.StatusBar;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.ixidev.gdpr.BuildConfig;
import com.ixidev.gdpr.GDPRChecker;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    FrameLayout[] frameLayout;
    ImageView[] imageViews;
    ActivityMainBinding viewMain;
    FragmentManager fragmentManager;
    boolean doubleBackToExitPressedOnce = false;
    Method method;
    int versionCode;
    FirebaseAnalytics firebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.initWhite(MainActivity.this);
        viewMain = ActivityMainBinding.inflate(getLayoutInflater());
        View view = viewMain.getRoot();
        setContentView(view);

        method = new Method(MainActivity.this);
        method.forceRTLIfSupported();
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        versionCode = BuildConfig.VERSION_CODE;

        try {
            PackageInfo info;
            info = getPackageManager().getPackageInfo(
                    getPackageName(), //Insert your own package name.
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        fragmentManager = getSupportFragmentManager();
        frameLayout = new FrameLayout[]{viewMain.bottomNav.frameHome, viewMain.bottomNav.frameLatest, viewMain.bottomNav.frameCategory, viewMain.bottomNav.frameAuthor, viewMain.bottomNav.frameProfile};
        imageViews = new ImageView[]{viewMain.bottomNav.imageHome, viewMain.bottomNav.imageLatest, viewMain.bottomNav.imageCategory, viewMain.bottomNav.imageAuthor, viewMain.bottomNav.imageProfile};


        if (Constant.isBanner){
            if (Constant.adNetworkType.equals("Admob")) {
                checkForConsent();
            } else {
                BannerAds.showBannerAds(MainActivity.this, viewMain.layoutAds);
            }
        }

        if (Constant.appUpdateVersion > versionCode && Constant.isAppUpdate) {
            newUpdateDialog();
        }
        selectBottomNav(0);
        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setOnItemClickListener(new OnClick() {
            @Override
            public void position(int position) {
                profileFragment(true, 2);
            }
        });
        loadFrag(homeFragment, "", fragmentManager);
        viewMain.toolbarMain.toolbarToolbar.setVisibility(View.GONE);
        viewMain.bottomNav.frameHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectBottomNav(0);
                viewMain.toolbarMain.toolbarToolbar.setVisibility(View.GONE);
                HomeFragment homeFragment = new HomeFragment();
                homeFragment.setOnItemClickListener(new OnClick() {
                    @Override
                    public void position(int position) {
                        profileFragment(true, 2);
                    }
                });
                loadFrag(homeFragment, "", fragmentManager);

            }
        });

        viewMain.bottomNav.frameLatest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectBottomNav(1);
                viewMain.toolbarMain.toolbarToolbar.setVisibility(View.GONE);
                LatestFragment latestFragment = new LatestFragment();
                loadFrag(latestFragment, "", fragmentManager);
            }
        });

        viewMain.bottomNav.frameCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectBottomNav(2);
                viewMain.toolbarMain.toolbarToolbar.setVisibility(View.GONE);
                CategoryFragment categoryFragment = new CategoryFragment();
                loadFrag(categoryFragment, "", fragmentManager);

            }
        });

        viewMain.bottomNav.frameAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectBottomNav(3);
                viewMain.toolbarMain.toolbarToolbar.setVisibility(View.GONE);
                AuthorFragment authorFragment = new AuthorFragment();
                loadFrag(authorFragment, "", fragmentManager);
            }
        });

        viewMain.bottomNav.frameProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileFragment(false, 0);
            }
        });

    }

    private void profileFragment(boolean isContinue, int pos) {
        Dexter.withContext(MainActivity.this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        selectBottomNav(4);
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("isContinue", isContinue);
                        bundle.putInt("movePos", pos);
                        ProfileFragment profileFragment = new ProfileFragment();
                        profileFragment.setArguments(bundle);
                        loadFrag(profileFragment, "", fragmentManager);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        // check for permanent denial of permission
                        showSettingsDialog();

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use profile feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                finish();
            }
        });
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }


    private void selectBottomNav(int pos) {
        for (int i = 0; i < frameLayout.length; i++) {
            if (i == pos) {
                frameLayout[i].setBackgroundResource(R.drawable.orange_bottom_menu_bg_select);
                imageViews[i].setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
            } else {
                frameLayout[i].setBackgroundResource(R.drawable.orange_bottom_menu_bg_normal);
                imageViews[i].setColorFilter(getResources().getColor(R.color.orange), PorterDuff.Mode.SRC_IN);
            }
        }
    }

    public void loadFrag(Fragment f1, String name, FragmentManager fm) {
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frameMain, f1, name);
        ft.commitAllowingStateLoss();
    }

    public void checkForConsent() {
        new GDPRChecker()
                .withContext(MainActivity.this)
                .withPrivacyUrl(getString(R.string.privacy_link))
                .withPublisherIds(Constant.publisherId)
                .check();
        BannerAds.showBannerAds(MainActivity.this, viewMain.layoutAds);
    }

    private void newUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(getString(R.string.app_update_title));
        builder.setCancelable(false);
        builder.setMessage(Constant.appUpdateDesc);
        builder.setPositiveButton(getString(R.string.app_update_btn), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(Constant.appUpdateUrl)));
            }
        });
        if (Constant.isAppUpdateCancel) {
            builder.setNegativeButton(getString(R.string.app_cancel_btn), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        }
        builder.setIcon(R.mipmap.ic_launcher);
        builder.show();
    }

    @Override
    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() != 0) {
            super.onBackPressed();
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, getString(R.string.back_key), Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }
}