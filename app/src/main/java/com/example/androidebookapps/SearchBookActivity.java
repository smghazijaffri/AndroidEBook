package com.example.androidebookapps;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.androidebookapps.databinding.ActivitySearchBookBinding;
import com.example.fragment.BookGridFragment;
import com.example.fragment.BookListFragment;
import com.example.util.BannerAds;
import com.example.util.Method;
import com.example.util.StatusBar;


public class SearchBookActivity extends AppCompatActivity {

    ActivitySearchBookBinding viewBookListSearch;
    String strSearchText;
    Method method;
    boolean isGridSelect = true;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.initWhite(SearchBookActivity.this);
        viewBookListSearch = ActivitySearchBookBinding.inflate(getLayoutInflater());
        setContentView(viewBookListSearch.getRoot());

        method = new Method(SearchBookActivity.this);
        method.forceRTLIfSupported();
        fragmentManager = getSupportFragmentManager();

        viewBookListSearch.edtSearch.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(viewBookListSearch.edtSearch, InputMethodManager.SHOW_IMPLICIT);
        viewBookListSearch.imageArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        viewBookListSearch.edtSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (viewBookListSearch.edtSearch.getText().toString().isEmpty()) {
                    Toast.makeText(SearchBookActivity.this, getString(R.string.search_msg), Toast.LENGTH_SHORT).show();
                } else {
                    strSearchText = viewBookListSearch.edtSearch.getText().toString();
                    if (method.isNetworkAvailable()) {
                        if (isGridSelect) {
                            goGrid();
                        } else {
                            goList();
                        }
                    } else {
                        method.alertBox(getResources().getString(R.string.internet_connection));
                    }
                }
            }
            return false;
        });

        viewBookListSearch.ivViewGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!viewBookListSearch.edtSearch.getText().toString().isEmpty()) {
                    isGridSelect = true;
                    viewBookListSearch.ivViewList.setColorFilter(getResources().getColor(R.color.icon_view_normal), PorterDuff.Mode.SRC_IN);
                    viewBookListSearch.ivViewGrid.setColorFilter(getResources().getColor(R.color.icon_view_select), PorterDuff.Mode.SRC_IN);
                    if (method.isNetworkAvailable()) {
                        goGrid();
                    } else {
                        method.alertBox(getResources().getString(R.string.internet_connection));
                    }
                }
            }
        });

        viewBookListSearch.ivViewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!viewBookListSearch.edtSearch.getText().toString().isEmpty()) {
                    isGridSelect = false;
                    viewBookListSearch.ivViewList.setColorFilter(getResources().getColor(R.color.icon_view_select), PorterDuff.Mode.SRC_IN);
                    viewBookListSearch.ivViewGrid.setColorFilter(getResources().getColor(R.color.icon_view_normal), PorterDuff.Mode.SRC_IN);
                    if (method.isNetworkAvailable()) {
                        goList();
                    } else {
                        method.alertBox(getResources().getString(R.string.internet_connection));
                    }
                }
            }
        });
        BannerAds.showBannerAds(SearchBookActivity.this, viewBookListSearch.layoutAds);
    }

    private void goGrid() {
        Bundle bundle = new Bundle();
        bundle.putString("postSubCatId", strSearchText);
        bundle.putString("postSubCatName", strSearchText);
        bundle.putString("type", "Search");
        BookGridFragment bookGridFragment = new BookGridFragment();
        bookGridFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.frameMain, bookGridFragment, "")
                .commitAllowingStateLoss();
    }

    private void goList() {
        Bundle bundle = new Bundle();
        bundle.putString("postSubCatId", strSearchText);
        bundle.putString("postSubCatName", strSearchText);
        bundle.putString("type", "Search");
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
