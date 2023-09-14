package com.example.androidebookapps;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidebookapps.databinding.ActivityBookListBinding;
import com.example.fragment.BookGridFragment;
import com.example.fragment.BookListFragment;
import com.example.item.SubCatListBook;
import com.example.util.BannerAds;
import com.example.util.Method;
import com.example.util.StatusBar;

import java.util.ArrayList;
import java.util.List;


public class TrendingBookActivity extends AppCompatActivity {

    ActivityBookListBinding viewBookListSubCat;
    Method method;
    List<SubCatListBook> listBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.initWhite(TrendingBookActivity.this);
        viewBookListSubCat = ActivityBookListBinding.inflate(getLayoutInflater());
        setContentView(viewBookListSubCat.getRoot());

        method = new Method(TrendingBookActivity.this);
        method.forceRTLIfSupported();
        listBooks = new ArrayList<>();

        viewBookListSubCat.toolbarMain.tvToolbarTitle.setText(getString(R.string.lbl_trending));

        viewBookListSubCat.toolbarMain.ivSearch.setVisibility(View.VISIBLE);
        viewBookListSubCat.toolbarMain.ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSearch = new Intent(TrendingBookActivity.this, SearchBookActivity.class);
                startActivity(intentSearch);
            }
        });

        viewBookListSubCat.toolbarMain.imageFilter.setVisibility(View.VISIBLE);
        viewBookListSubCat.toolbarMain.imageFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentFil = new Intent(TrendingBookActivity.this, FilterActivity.class);
                startActivity(intentFil);
            }
        });
        viewBookListSubCat.toolbarMain.imageArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        viewBookListSubCat.ivViewGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewBookListSubCat.ivViewList.setColorFilter(getResources().getColor(R.color.icon_view_normal), PorterDuff.Mode.SRC_IN);
                viewBookListSubCat.ivViewGrid.setColorFilter(getResources().getColor(R.color.icon_view_select), PorterDuff.Mode.SRC_IN);

                if (method.isNetworkAvailable()) {
                    goGrid();
                } else {
                    method.alertBox(getResources().getString(R.string.internet_connection));
                }
            }
        });

        viewBookListSubCat.ivViewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewBookListSubCat.ivViewList.setColorFilter(getResources().getColor(R.color.icon_view_select), PorterDuff.Mode.SRC_IN);
                viewBookListSubCat.ivViewGrid.setColorFilter(getResources().getColor(R.color.icon_view_normal), PorterDuff.Mode.SRC_IN);
                if (method.isNetworkAvailable()) {
                    goList();
                } else {
                    method.alertBox(getResources().getString(R.string.internet_connection));
                }
            }
        });

        if (method.isNetworkAvailable()) {
            goGrid();
        } else {
            method.alertBox(getResources().getString(R.string.internet_connection));
        }
        BannerAds.showBannerAds(TrendingBookActivity.this, viewBookListSubCat.layoutAds);
    }

    private void goGrid() {
        Bundle bundle = new Bundle();
        bundle.putString("postSubCatId", "");
        bundle.putString("postSubCatName", "");
        bundle.putString("type", "TREND");
        BookGridFragment bookGridFragment = new BookGridFragment();
        bookGridFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.frameMain, bookGridFragment, "")
                .commitAllowingStateLoss();
    }

    private void goList() {
        Bundle bundle = new Bundle();
        bundle.putString("postSubCatId", "");
        bundle.putString("postSubCatName", "");
        bundle.putString("type", "TREND");
        BookListFragment bookListFragment = new BookListFragment();
        bookListFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.frameMain, bookListFragment, "")
                .commitAllowingStateLoss();
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
