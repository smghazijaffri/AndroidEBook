package com.example.androidebookapps;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.adapter.RelatedAdapter;
import com.example.androidebookapps.databinding.ActivityBookDetailBinding;
import com.example.fragment.ReportBookFragment;
import com.example.fragment.WriteRateReviewFragment;
import com.example.item.BookDetailList;
import com.example.response.BookDetailRP;
import com.example.rest.ApiClient;
import com.example.rest.ApiInterface;
import com.example.util.API;
import com.example.util.BannerAds;
import com.example.util.Constant;
import com.example.util.FavouriteIF;
import com.example.util.Method;
import com.example.util.OnClick;
import com.example.util.StatusBar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BookDetailsActivity extends AppCompatActivity {

    ActivityBookDetailBinding viewBookDetail;
    Method method;
    String postBookId, lastPosNum = "", pageNo = "";
    RelatedAdapter relatedAdapter;
    BookDetailList bookDetailListPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.initWhite(BookDetailsActivity.this);
        viewBookDetail = ActivityBookDetailBinding.inflate(getLayoutInflater());
        setContentView(viewBookDetail.getRoot());
        method = new Method(BookDetailsActivity.this);
        method.forceRTLIfSupported();

        Intent intent = getIntent();
        postBookId = intent.getStringExtra("BOOK_ID");
        if (intent.hasExtra("LAST_POS")) {
            lastPosNum = intent.getStringExtra("LAST_POS");
            pageNo = intent.getStringExtra("PAGE_NUM");
        }

        viewBookDetail.toolbarMain.tvToolbarTitle.setText(getString(R.string.book_detail_title));
        viewBookDetail.toolbarMain.imageFilter.setVisibility(View.VISIBLE);
        viewBookDetail.toolbarMain.imageFilter.setImageResource(R.drawable.img_share);
        viewBookDetail.toolbarMain.imageFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bookDetailListPos != null && bookDetailListPos.getShare_url() != null) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, bookDetailListPos.getShare_url());
                    startActivity(Intent.createChooser(intent, "Choose one"));
                } else {
                    method.alertBox(getResources().getString(R.string.wrong));
                }
            }
        });

        viewBookDetail.toolbarMain.imageArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        viewBookDetail.ivRateList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRateList = new Intent(BookDetailsActivity.this, RateReviewActivity.class);
                startActivity(intentRateList);
            }
        });

        viewBookDetail.progressHome.setVisibility(View.GONE);
        viewBookDetail.llNoData.clNoDataFound.setVisibility(View.GONE);
        viewBookDetail.nsView.setVisibility(View.GONE);
        viewBookDetail.btnBuyBook.setVisibility(View.GONE);

        viewBookDetail.rvRelatedBook.setHasFixedSize(true);
        viewBookDetail.rvRelatedBook.setLayoutManager(new LinearLayoutManager(BookDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false));
        viewBookDetail.rvRelatedBook.setFocusable(false);
        viewBookDetail.rvRelatedBook.setNestedScrollingEnabled(false);

        if (method.isNetworkAvailable()) {
            bookDetailData();
        } else {
            method.alertBox(getResources().getString(R.string.internet_connection));
        }

        BannerAds.showBannerAds(BookDetailsActivity.this, viewBookDetail.layoutAds);
    }

    private void bookDetailData() {

        viewBookDetail.progressHome.setVisibility(View.VISIBLE);

        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(BookDetailsActivity.this));
        jsObj.addProperty("book_id", postBookId);
        jsObj.addProperty("user_id", method.getIsLogin() ? method.getUserId() : "");
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<BookDetailRP> call = apiService.getBookDetailData(API.toBase64(jsObj.toString()));
        call.enqueue(new Callback<BookDetailRP>() {
            @Override
            public void onResponse(@NotNull Call<BookDetailRP> call, @NotNull Response<BookDetailRP> response) {
                try {

                    BookDetailRP bookDetailRP = response.body();

                    if (bookDetailRP !=null && bookDetailRP.getSuccess().equals("1")) {

                        if (bookDetailRP.getBookDetailLists().size() != 0) {
                            viewBookDetail.nsView.setVisibility(View.VISIBLE);
                            viewBookDetail.btnBuyBook.setVisibility(View.VISIBLE);
                            bookDetailListPos = bookDetailRP.getBookDetailLists().get(0);
                            bookViewData(postBookId);
                            viewBookDetail.tvBookName.setText(bookDetailListPos.getPost_title());
                            viewBookDetail.tvByAuthor.setSelected(true);
                            StringBuilder strAuthor = new StringBuilder();
                            if (!bookDetailListPos.getListBookDetailAuthor().isEmpty()) {
                                String prefix = "";
                                for (int k = 0; k < bookDetailListPos.getListBookDetailAuthor().size(); k++) {
                                    strAuthor.append(prefix);
                                    prefix = ", ";
                                    strAuthor.append(bookDetailListPos.getListBookDetailAuthor().get(k).getAuthor_name());
                                }
                            }
                            viewBookDetail.tvByAuthor.setText(getString(R.string.by_author, strAuthor.toString()));
                            viewBookDetail.tvBookView.setText(Method.Format(Integer.parseInt(bookDetailListPos.getTotal_views())));

                            if (bookDetailListPos.getBook_on_rent().equals("1")) {
                                viewBookDetail.llPremium.setVisibility(View.VISIBLE);
                                viewBookDetail.tvBookPrice.setText(getString(R.string.currency_code, Constant.constantCurrency, bookDetailListPos.getBook_rent_price()));
                            } else if (bookDetailListPos.getPost_access().equals("Paid")) {
                                viewBookDetail.llPremium.setVisibility(View.VISIBLE);
                                viewBookDetail.tvBookPrice.setText(getString(R.string.lbl_paid));
                            } else {
                                viewBookDetail.llPremium.setVisibility(View.GONE);
                                viewBookDetail.tvBookPrice.setText(getString(R.string.lbl_free));
                            }

                            if (!bookDetailListPos.getPost_image().equals("")) {
                                Glide.with(BookDetailsActivity.this).load(bookDetailListPos.getPost_image())
                                        .placeholder(R.drawable.placeholder_portable)
                                        .into(viewBookDetail.ivBook);
                            }

                            WebSettings webSettings = viewBookDetail.wvBookDesc.getSettings();
                            webSettings.setJavaScriptEnabled(true);
                            webSettings.setPluginState(WebSettings.PluginState.ON);
                            viewBookDetail.wvBookDesc.setBackgroundColor(Color.TRANSPARENT);
                            viewBookDetail.wvBookDesc.setFocusableInTouchMode(false);
                            viewBookDetail.wvBookDesc.setFocusable(false);

                            viewBookDetail.wvBookDesc.getSettings().setDefaultTextEncodingName("UTF-8");
                            String mimeType = "text/html";
                            String encoding = "utf-8";

                            String text = "<html dir=" + method.isWebViewTextRtl() + "><head>"
                                    + "<style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/fonts/opensansromanregular.ttf\")}body{font-family: MyFont;color: " + method.webViewText() + "font-size: 14px;line-height:1.7;margin-left: 0px;margin-right: 0px;margin-top: 0px;margin-bottom: 0px;padding: 0px;}"
                                    + "a {color:" + method.webViewLink() + "text-decoration:none}"
                                    + "</style></head>"
                                    + "<body>"
                                    + bookDetailListPos.getPost_description()
                                    + "</body></html>";

                            viewBookDetail.wvBookDesc.loadDataWithBaseURL(null, text, mimeType, encoding, null);

                            viewBookDetail.ivRateList.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intentDetail = new Intent(BookDetailsActivity.this, RateReviewActivity.class);
                                    intentDetail.putExtra("RATE_BOOK_ID", bookDetailListPos.getPost_id());
                                    startActivity(intentDetail);
                                }
                            });
                            viewBookDetail.tvRateAvg.setText(bookDetailListPos.getTotal_rate());
                            viewBookDetail.ratingView.setRating(Float.parseFloat(bookDetailListPos.getTotal_rate()));
                            viewBookDetail.tvRateTotalReviews.setText(getString(R.string.lbl_review_detail, Method.Format(Integer.parseInt(bookDetailListPos.getTotal_reviews()))));
                            viewBookDetail.btnWriteAReview.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (method.getIsLogin()) {
                                        Bundle bundle = new Bundle();
                                        bundle.putString("postId", bookDetailListPos.getPost_id());
                                        bundle.putString("userId", method.getUserId());
                                        WriteRateReviewFragment writeRateReviewFragment = new WriteRateReviewFragment();
                                        writeRateReviewFragment.setArguments(bundle);
                                        writeRateReviewFragment.show(getSupportFragmentManager(), writeRateReviewFragment.getTag());

                                    } else {
                                        Toast.makeText(BookDetailsActivity.this, getString(R.string.login_require), Toast.LENGTH_SHORT).show();
                                        Intent intentLogin = new Intent(BookDetailsActivity.this, LoginActivity.class);
                                        intentLogin.putExtra("isFromDetail", true);
                                        startActivity(intentLogin);
                                    }
                                }
                            });

                            if (bookDetailListPos.getListRelatedBook().size() != 0) {
                                relatedAdapter = new RelatedAdapter(BookDetailsActivity.this, bookDetailListPos.getListRelatedBook());
                                viewBookDetail.rvRelatedBook.setAdapter(relatedAdapter);
                                relatedAdapter.setOnItemClickListener(new OnClick() {
                                    @Override
                                    public void position(int position) {
                                        Intent intentDetail = new Intent(BookDetailsActivity.this, BookDetailsActivity.class);
                                        intentDetail.putExtra("BOOK_ID", bookDetailListPos.getListRelatedBook().get(position).getPost_id());
                                        startActivity(intentDetail);
                                        finish();
                                    }
                                });
                            } else {
                                viewBookDetail.rlRelatedSection.setVisibility(View.GONE);
                                viewBookDetail.rvRelatedBook.setVisibility(View.GONE);
                            }


                            viewBookDetail.llReportBook.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (method.getIsLogin()) {
                                        Bundle bundle = new Bundle();
                                        bundle.putString("postId", bookDetailListPos.getPost_id());
                                        bundle.putString("userId", method.getUserId());
                                        ReportBookFragment reportBookFragment = new ReportBookFragment();
                                        reportBookFragment.setArguments(bundle);
                                        reportBookFragment.show(getSupportFragmentManager(), reportBookFragment.getTag());

                                    } else {
                                        Toast.makeText(BookDetailsActivity.this, getString(R.string.login_require), Toast.LENGTH_SHORT).show();
                                        Intent intentLogin = new Intent(BookDetailsActivity.this, LoginActivity.class);
                                        intentLogin.putExtra("isFromDetail", true);
                                        startActivity(intentLogin);
                                    }

                                }
                            });

                            if (bookDetailListPos.getDownload_enable().equals("1")) {
                                viewBookDetail.llDownload.setVisibility(View.VISIBLE);
                                viewBookDetail.vlDownload.setVisibility(View.VISIBLE);
                            } else {
                                viewBookDetail.llDownload.setVisibility(View.GONE);
                                viewBookDetail.vlDownload.setVisibility(View.GONE);
                            }

                            if (checkRentSubsPlanStatus()) {
                                viewBookDetail.btnBuyBook.setVisibility(View.GONE);
                            } else {
                                viewBookDetail.btnBuyBook.setVisibility(View.VISIBLE);
                            }
                            if (bookDetailListPos.getBook_on_rent().equals("1")) {
                                viewBookDetail.btnBuyBook.setText(getString(R.string.lbl_buy_book));
                            } else if (bookDetailListPos.getPost_access().equals("Paid")) {
                                viewBookDetail.btnBuyBook.setText(getString(R.string.lbl_buy_plan));
                            }
                            viewBookDetail.btnBuyBook.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (method.getIsLogin()) {
                                        if (bookDetailListPos.getBook_on_rent().equals("1")) {
                                            Intent intentRent = new Intent(BookDetailsActivity.this, PaymentMethodActivity.class);
                                            intentRent.putExtra("planId", bookDetailListPos.getPost_id());
                                            intentRent.putExtra("planName", bookDetailListPos.getPost_title());
                                            intentRent.putExtra("planPrice", bookDetailListPos.getBook_rent_price());
                                            intentRent.putExtra("planPriceCurrency", Constant.constantCurrency);
                                            intentRent.putExtra("planUserId", method.getUserId());
                                            intentRent.putExtra("planDuration", bookDetailListPos.getBook_rent_time());
                                            intentRent.putExtra("isRent", true);
                                            startActivity(intentRent);
                                        } else {
                                            Intent intentPlan = new Intent(BookDetailsActivity.this, PlanListActivity.class);
                                            startActivity(intentPlan);
                                        }
                                    } else {
                                        Toast.makeText(BookDetailsActivity.this, getString(R.string.login_require), Toast.LENGTH_SHORT).show();
                                        Intent intentLogin = new Intent(BookDetailsActivity.this, LoginActivity.class);
                                        intentLogin.putExtra("isFromDetail", true);
                                        startActivity(intentLogin);
                                    }
                                }
                            });

                            viewBookDetail.llReadBook.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (checkRentSubsPlanStatus()) {
                                        openBook();
                                    } else {
                                        method.alertBox(getResources().getString(R.string.lbl_buy_plan_title));
                                    }
                                }
                            });

                            viewBookDetail.llDownload.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (method.getIsLogin()) {
                                        if (checkRentSubsPlanStatus()) {
                                            if (method.isNetworkAvailable()) {
                                                if (bookDetailListPos.getPost_file_url().contains(".epub")) {
                                                    method.download(bookDetailListPos.getPost_id(),
                                                            bookDetailListPos.getPost_title(),
                                                            bookDetailListPos.getPost_image(),
                                                            bookDetailListPos.getListBookDetailAuthor().isEmpty()?"":bookDetailListPos.getListBookDetailAuthor().get(0).getAuthor_name(),
                                                            bookDetailListPos.getPost_file_url(), "epub");
                                                } else {
                                                    method.download(bookDetailListPos.getPost_id(),
                                                            bookDetailListPos.getPost_title(),
                                                            bookDetailListPos.getPost_image(),
                                                            bookDetailListPos.getListBookDetailAuthor().isEmpty()?"":bookDetailListPos.getListBookDetailAuthor().get(0).getAuthor_name(),
                                                            bookDetailListPos.getPost_file_url(), "pdf");
                                                }
                                            }
                                        } else {
                                            method.alertBox(getResources().getString(R.string.lbl_buy_plan_title));
                                        }
                                    } else {
                                        Toast.makeText(BookDetailsActivity.this, getString(R.string.login_require), Toast.LENGTH_SHORT).show();
                                        Intent intentLogin = new Intent(BookDetailsActivity.this, LoginActivity.class);
                                        intentLogin.putExtra("isFromDetail", true);
                                        startActivity(intentLogin);
                                    }
                                }
                            });

                            if (bookDetailListPos.isFavourite()) {
                                viewBookDetail.ivFav.setImageResource(R.drawable.img_fav_hover);
                            } else {
                                viewBookDetail.ivFav.setImageResource(R.drawable.img_fav);
                            }

                            viewBookDetail.llFavourite.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (method.getIsLogin()) {
                                        FavouriteIF favouriteIF = (isFavourite, message) -> {
                                            if (isFavourite.equals("true")) {
                                                viewBookDetail.ivFav.setImageResource(R.drawable.img_fav_hover);
                                            } else {
                                                viewBookDetail.ivFav.setImageResource(R.drawable.img_fav);
                                            }
                                        };
                                        method.addToFav(bookDetailListPos.getPost_id(), method.getUserId(), "Book", favouriteIF);
                                    } else {
                                        Toast.makeText(BookDetailsActivity.this, getString(R.string.login_require), Toast.LENGTH_SHORT).show();
                                        Intent intentLogin = new Intent(BookDetailsActivity.this, LoginActivity.class);
                                        intentLogin.putExtra("isFromDetail", true);
                                        startActivity(intentLogin);
                                    }
                                }
                            });

                        } else {
                            viewBookDetail.llNoData.clNoDataFound.setVisibility(View.VISIBLE);
                            viewBookDetail.nsView.setVisibility(View.GONE);
                            viewBookDetail.progressHome.setVisibility(View.GONE);
                        }
                    } else {
                        viewBookDetail.llNoData.clNoDataFound.setVisibility(View.VISIBLE);
                        viewBookDetail.nsView.setVisibility(View.GONE);
                        viewBookDetail.progressHome.setVisibility(View.GONE);
                        method.alertBox(getResources().getString(R.string.failed_try_again));
                    }

                } catch (Exception e) {
                    Log.d("exception_error", e.toString());
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }
                viewBookDetail.progressHome.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NotNull Call<BookDetailRP> call, @NotNull Throwable t) {
                // Log error here since request failed
                Log.e("fail", t.toString());
                viewBookDetail.llNoData.clNoDataFound.setVisibility(View.VISIBLE);
                viewBookDetail.progressHome.setVisibility(View.GONE);
                method.alertBox(getResources().getString(R.string.failed_try_again));
            }
        });
    }

    private boolean checkRentSubsPlanStatus() {
        boolean isStatus;
        if (bookDetailListPos.getBook_on_rent().equals("1")) {
            isStatus = bookDetailListPos.isBook_purchased();
        } else {
            if (bookDetailListPos.getPost_access().equals("Paid")) {
                isStatus = bookDetailListPos.isUser_plan_status();
            } else {//free
                isStatus = true;
            }
        }


        return isStatus;
    }

    private void bookViewData(String bookId) {
        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(BookDetailsActivity.this));
        jsObj.addProperty("post_id", bookId);
        jsObj.addProperty("post_type", "Book");
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> call = apiService.getPostViewData(API.toBase64(jsObj.toString()));
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {
            }

            @Override
            public void onFailure(@NotNull Call<JsonObject> call, @NotNull Throwable t) {
                // Log error here since request failed
                Log.e("fail", t.toString());
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

    private void openBook() {
        if (bookDetailListPos.getPost_file_url().contains(".epub")) {
            DownloadEpub downloadEpub = new DownloadEpub(BookDetailsActivity.this);
            downloadEpub.pathEpub(bookDetailListPos.getPost_file_url(), bookDetailListPos.getPost_id(), lastPosNum, pageNo);
        } else {
            startActivity(new Intent(BookDetailsActivity.this, PDFShow.class)
                    .putExtra("id", bookDetailListPos.getPost_id())
                    .putExtra("link", bookDetailListPos.getPost_file_url())
                    .putExtra("toolbarTitle", bookDetailListPos.getPost_title())
                    .putExtra("type", "link")
                    .putExtra("PAGE_NUM", pageNo)
                    .putExtra("posLast", lastPosNum));
        }
    }
}
