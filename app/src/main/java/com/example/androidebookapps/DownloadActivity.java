package com.example.androidebookapps;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidebookapps.databinding.ActivityDownloadBinding;
import com.example.fragment.DownloadFragment;
import com.example.util.Method;


public class DownloadActivity extends AppCompatActivity {

    ActivityDownloadBinding viewDownloadBinding;
    Method method;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(getResources().getColor(R.color.app_bg_white));
        viewDownloadBinding = ActivityDownloadBinding.inflate(getLayoutInflater());
        setContentView(viewDownloadBinding.getRoot());

        method = new Method(DownloadActivity.this);
        method.forceRTLIfSupported();

        viewDownloadBinding.tvToolbarTitle.setText(getString(R.string.tab_download));

        DownloadFragment downloadFragment = new DownloadFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.frameMain, downloadFragment, "")
                .commitAllowingStateLoss();

    }

    @Override
    public void onBackPressed() {
        if (method.isNetworkAvailable()){
            Intent intentMain = new Intent(getApplicationContext(), SplashActivity.class);
            intentMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intentMain);
            finishAffinity();
        }else {
            super.onBackPressed();
        }
    }
}
