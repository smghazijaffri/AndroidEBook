package com.example.androidebookapps;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.androidebookapps.databinding.ActivityAboutUsBinding;
import com.example.util.Constant;
import com.example.util.Method;
import com.example.util.StatusBar;


public class AboutUsActivity extends AppCompatActivity {

    ActivityAboutUsBinding viewAboutUs;
    Method method;
    String abData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.initWhite(AboutUsActivity.this);
        viewAboutUs = ActivityAboutUsBinding.inflate(getLayoutInflater());
        setContentView(viewAboutUs.getRoot());

        method = new Method(AboutUsActivity.this);
        method.forceRTLIfSupported();

        Intent intent=getIntent();
        abData=intent.getStringExtra("ABOUT");

        viewAboutUs.toolbarMain.tvToolbarTitle.setText(getString(R.string.lbl_about_us_title));
        viewAboutUs.toolbarMain.ivSearch.setVisibility(View.GONE);
        viewAboutUs.toolbarMain.imageArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (!Constant.appListData.getAppLogo().equals("")) {
            Glide.with(AboutUsActivity.this).load(Constant.appListData.getAppLogo())
                    .placeholder(R.drawable.placeholder_portable)
                    .into(viewAboutUs.ivAppLogo);
        }

        viewAboutUs.tvAppName.setText(Constant.appListData.getAppName());
        viewAboutUs.tvAppVersion.setText(Constant.appListData.getAppVersion());
        viewAboutUs.tvCompany.setText(Constant.appListData.getAppCompany());
        viewAboutUs.tvEmail.setText(Constant.appListData.getAppEmail());
        viewAboutUs.tvWebsite.setText(Constant.appListData.getAppWebsite());
        viewAboutUs.tvContact.setText(Constant.appListData.getAppContact());

        viewAboutUs.ivFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constant.appListData.getFacebookLink().isEmpty()) {
                    Toast.makeText(AboutUsActivity.this, getString(R.string.no_link_found), Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.appListData.getFacebookLink())));
                }
            }
        });

        viewAboutUs.ivInsta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constant.appListData.getInstagramLink().isEmpty()) {
                    Toast.makeText(AboutUsActivity.this, getString(R.string.no_link_found), Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.appListData.getInstagramLink())));
                }
            }
        });

        viewAboutUs.ivYt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constant.appListData.getYoutubeLink().isEmpty()) {
                    Toast.makeText(AboutUsActivity.this, getString(R.string.no_link_found), Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.appListData.getYoutubeLink())));
                }
            }
        });

        viewAboutUs.ivWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constant.appListData.getTwitterLink().isEmpty()) {
                    Toast.makeText(AboutUsActivity.this, getString(R.string.no_link_found), Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.appListData.getTwitterLink())));
                }
            }
        });

        WebSettings webSettings = viewAboutUs.webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        viewAboutUs.webView.setBackgroundColor(Color.TRANSPARENT);
        viewAboutUs.webView.setFocusableInTouchMode(false);
        viewAboutUs.webView.setFocusable(false);
        viewAboutUs.webView.getSettings().setDefaultTextEncodingName("UTF-8");
        String mimeType = "text/html";
        String encoding = "utf-8";

        String text = "<html dir=" +method.isWebViewTextRtl() + "><head>"
                + "<style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/fonts/opensansromanregular.ttf\")}body{font-family: MyFont;color: " + method.webViewAboutText() + " font-size: 14px;line-height:1.6}"
                + "a {color:" + method.webViewLink() + "text-decoration:none}"
                + "</style></head>"
                + "<body>"
                + abData
                + "</body></html>";

        viewAboutUs.webView.loadDataWithBaseURL(null, text, mimeType, encoding, null);


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
