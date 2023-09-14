package com.example.androidebookapps;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bumptech.glide.Glide;
import com.example.adapter.AuthorListAdapter;
import com.example.androidebookapps.databinding.ActivityAuthorDetailBinding;
import com.example.item.AuthorDetailList;
import com.example.item.BookRelatedList;
import com.example.response.AuthorDetailRP;
import com.example.rest.ApiClient;
import com.example.rest.ApiInterface;
import com.example.util.API;
import com.example.util.BannerAds;
import com.example.util.Constant;
import com.example.util.Method;
import com.example.util.OnClick;
import com.example.util.StatusBar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AuthorDetailsActivity extends AppCompatActivity {

    ActivityAuthorDetailBinding viewAuthorDetail;
    Method method;
    AuthorListAdapter authorListAdapter;
    AuthorDetailList authorDetailList;
    String authorId;
    List<BookRelatedList> relatedLists;
    int j = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.initWhite(AuthorDetailsActivity.this);
        viewAuthorDetail = ActivityAuthorDetailBinding.inflate(getLayoutInflater());
        setContentView(viewAuthorDetail.getRoot());
        method = new Method(AuthorDetailsActivity.this);
        method.forceRTLIfSupported();
        relatedLists = new ArrayList<>();

        Intent intent = getIntent();
        authorId = intent.getStringExtra("AUTHOR_ID");

        viewAuthorDetail.toolbarMain.tvToolbarTitle.setText(getString(R.string.lbl_author_info));
        viewAuthorDetail.toolbarMain.imageFilter.setVisibility(View.GONE);
        viewAuthorDetail.toolbarMain.imageArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        viewAuthorDetail.progressHome.setVisibility(View.GONE);
        viewAuthorDetail.llNoData.clNoDataFound.setVisibility(View.GONE);
        viewAuthorDetail.clMain.setVisibility(View.GONE);

        viewAuthorDetail.rvRelatedBook.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(AuthorDetailsActivity.this, 2);
        viewAuthorDetail.rvRelatedBook.setLayoutManager(layoutManager);
        viewAuthorDetail.rvRelatedBook.setFocusable(false);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (authorListAdapter.getItemViewType(position) == 1) {
                    return 2;
                }
                return 1;
            }
        });

        if (method.isNetworkAvailable()) {
            authorDetailData();
        } else {
            method.alertBox(getResources().getString(R.string.internet_connection));
        }

        BannerAds.showBannerAds(AuthorDetailsActivity.this, viewAuthorDetail.layoutAds);

    }

    private void authorDetailData() {

        viewAuthorDetail.progressHome.setVisibility(View.VISIBLE);

        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(AuthorDetailsActivity.this));
        jsObj.addProperty("author_id", authorId);
        jsObj.addProperty("user_id", method.getIsLogin() ? method.getUserId() : "");
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<AuthorDetailRP> call = apiService.getAuthorDetailData(API.toBase64(jsObj.toString()));
        call.enqueue(new Callback<AuthorDetailRP>() {
            @Override
            public void onResponse(@NotNull Call<AuthorDetailRP> call, @NotNull Response<AuthorDetailRP> response) {
                try {

                    AuthorDetailRP bookDetailRP = response.body();

                    if (bookDetailRP != null && bookDetailRP.getSuccess().equals("1")) {

                        if (bookDetailRP.getAuthorDetailLists().size() != 0) {
                            viewAuthorDetail.clMain.setVisibility(View.VISIBLE);
                            authorDetailList = bookDetailRP.getAuthorDetailLists().get(0);

                            viewAuthorDetail.tvAuthorName.setText(authorDetailList.getAuthor_name());

                            if (!authorDetailList.getAuthor_image().equals("")) {
                                Glide.with(AuthorDetailsActivity.this).load(authorDetailList.getAuthor_image())
                                        .placeholder(R.drawable.placeholder_author)
                                        .into(viewAuthorDetail.ivAuthor);
                            }

                            WebSettings webSettings = viewAuthorDetail.wvAuthorDesc.getSettings();
                            webSettings.setJavaScriptEnabled(true);
                            webSettings.setPluginState(WebSettings.PluginState.ON);
                            viewAuthorDetail.wvAuthorDesc.setBackgroundColor(Color.TRANSPARENT);
                            viewAuthorDetail.wvAuthorDesc.setFocusableInTouchMode(false);
                            viewAuthorDetail.wvAuthorDesc.setFocusable(false);
                            viewAuthorDetail.wvAuthorDesc.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

                            viewAuthorDetail.wvAuthorDesc.getSettings().setDefaultTextEncodingName("UTF-8");
                            String mimeType = "text/html";
                            String encoding = "utf-8";

                            String text = "<html dir=" + method.isWebViewTextRtl() + "><head>"
                                    + "<style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/fonts/opensansromanregular.ttf\")}body{font-family: MyFont;color: " + method.webViewTextAuthor() + "font-size: 15px;line-height:1.6;margin: 0px;padding: 0px;}"
                                    + "a {color:" + method.webViewLink() + "text-decoration:none}"
                                    + "</style></head>"
                                    + "<body>"
                                    + authorDetailList.getAuthor_info()
                                    + "</body></html>";

                            viewAuthorDetail.wvAuthorDesc.loadDataWithBaseURL(null, text, mimeType, encoding, null);

                            viewAuthorDetail.ivFb.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (authorDetailList.getFacebook_url().isEmpty()) {
                                        Toast.makeText(AuthorDetailsActivity.this, getString(R.string.no_link_found), Toast.LENGTH_SHORT).show();
                                    } else {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(authorDetailList.getFacebook_url())));
                                    }
                                }
                            });

                            viewAuthorDetail.ivInsta.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (authorDetailList.getInstagram_url().isEmpty()) {
                                        Toast.makeText(AuthorDetailsActivity.this, getString(R.string.no_link_found), Toast.LENGTH_SHORT).show();
                                    } else {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(authorDetailList.getInstagram_url())));
                                    }
                                }
                            });

                            viewAuthorDetail.ivYt.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (authorDetailList.getYoutube_url().isEmpty()) {
                                        Toast.makeText(AuthorDetailsActivity.this, getString(R.string.no_link_found), Toast.LENGTH_SHORT).show();
                                    } else {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(authorDetailList.getYoutube_url())));
                                    }
                                }
                            });

                            viewAuthorDetail.ivWeb.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (authorDetailList.getWebsite_url().isEmpty()) {
                                        Toast.makeText(AuthorDetailsActivity.this, getString(R.string.no_link_found), Toast.LENGTH_SHORT).show();
                                    } else {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(authorDetailList.getWebsite_url())));
                                    }
                                }
                            });

                            if (authorDetailList.getListAuthorBook().size() != 0) {
                                for (int i = 0; i < authorDetailList.getListAuthorBook().size(); i++) {
                                    if (Constant.isNative) {
                                        if (j % Constant.nativePosition == 0) {
                                            relatedLists.add(null);
                                            j++;
                                        }
                                    }
                                    relatedLists.add(authorDetailList.getListAuthorBook().get(i));
                                    j++;
                                }
                                authorListAdapter = new AuthorListAdapter(AuthorDetailsActivity.this, relatedLists, authorDetailList.getAuthor_name());
                                viewAuthorDetail.rvRelatedBook.setAdapter(authorListAdapter);
                                authorListAdapter.setOnItemClickListener(new OnClick() {
                                    @Override
                                    public void position(int position) {
                                        Intent intentDetail = new Intent(AuthorDetailsActivity.this, BookDetailsActivity.class);
                                        intentDetail.putExtra("BOOK_ID", relatedLists.get(position).getPost_id());
                                        startActivity(intentDetail);
                                    }
                                });
                            } else {
                                viewAuthorDetail.llNoDataAuthor.clNoDataFound.setVisibility(View.VISIBLE);
                                viewAuthorDetail.llNoDataAuthor.textViewNoDataNoDataFound.setText(getString(R.string.msg_no_author_book));
                            }

                        } else {
                            viewAuthorDetail.llNoData.clNoDataFound.setVisibility(View.VISIBLE);
                            viewAuthorDetail.clMain.setVisibility(View.GONE);
                            viewAuthorDetail.progressHome.setVisibility(View.GONE);
                        }
                    } else {
                        viewAuthorDetail.llNoData.clNoDataFound.setVisibility(View.VISIBLE);
                        viewAuthorDetail.clMain.setVisibility(View.GONE);
                        viewAuthorDetail.progressHome.setVisibility(View.GONE);
                        method.alertBox(getResources().getString(R.string.failed_try_again));
                    }

                } catch (Exception e) {
                    Log.d("exception_error", e.toString());
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }
                viewAuthorDetail.progressHome.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NotNull Call<AuthorDetailRP> call, @NotNull Throwable t) {
                // Log error here since request failed
                Log.e("fail", t.toString());
                viewAuthorDetail.llNoData.clNoDataFound.setVisibility(View.VISIBLE);
                viewAuthorDetail.progressHome.setVisibility(View.GONE);
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
