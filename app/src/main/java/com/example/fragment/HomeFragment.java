package com.example.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adapter.ContinueHomeAdapter;
import com.example.adapter.HomeSectionAdapter;
import com.example.adapter.SliderAdapter;
import com.example.adapter.TrendingHomeAdapter;
import com.example.androidebookapps.BookDetailsActivity;
import com.example.androidebookapps.BookListBySubCatActivity;
import com.example.androidebookapps.R;
import com.example.androidebookapps.SettingsActivity;
import com.example.androidebookapps.TrendingBookActivity;
import com.example.androidebookapps.databinding.FragmentHomeBinding;
import com.example.response.HomeRP;
import com.example.rest.ApiClient;
import com.example.rest.ApiInterface;
import com.example.util.API;
import com.example.util.Method;
import com.example.util.OnClick;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {

    FragmentHomeBinding viewHome;
    Method method;
    private Timer timer;
    final long DELAY_MS = 2000;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000;
    Boolean isTimerStart = false;
    private final Handler handler = new Handler();
    private Runnable Update;
    OnClick onClickPos;
    SliderAdapter sliderAdapter;
    HomeSectionAdapter homeSectionAdapter;
    ContinueHomeAdapter continueHomeAdapter;
    TrendingHomeAdapter trendingHomeAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        requireActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.app_bg));
        viewHome = FragmentHomeBinding.inflate(inflater, container, false);
        method = new Method(requireActivity());

        viewHome.progressHome.setVisibility(View.GONE);
        viewHome.llHomeMain.setVisibility(View.GONE);
        viewHome.llNoData.clNoDataFound.setVisibility(View.GONE);

        viewHome.vpFeatureHome.useScale();
        viewHome.vpFeatureHome.removeAlpha();

        viewHome.rvContBook.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManagerContinue = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        viewHome.rvContBook.setLayoutManager(layoutManagerContinue);
        viewHome.rvContBook.setFocusable(false);

        viewHome.rvHomeSec.setHasFixedSize(true);
        viewHome.rvHomeSec.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        viewHome.rvHomeSec.setFocusable(false);
        viewHome.rvHomeSec.setNestedScrollingEnabled(false);

        viewHome.rvHomeTrendingBook.setHasFixedSize(true);
        viewHome.rvHomeTrendingBook.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        viewHome.rvHomeTrendingBook.setFocusable(false);
        viewHome.rvHomeTrendingBook.setNestedScrollingEnabled(false);

        if (method.isNetworkAvailable()) {
            if (method.getIsLogin()) {
                homeData(method.getUserId());
            } else {
                homeData("0");
            }
        } else {
            method.alertBox(getResources().getString(R.string.internet_connection));
            viewHome.progressHome.setVisibility(View.GONE);
        }

        viewHome.ibSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSetting = new Intent(requireActivity(), SettingsActivity.class);
                startActivity(intentSetting);
            }
        });

        viewHome.imageHomeSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHome.edtHomeSearch.getText().toString().isEmpty()) {
                    Toast.makeText(requireActivity(), getString(R.string.search_msg), Toast.LENGTH_SHORT).show();
                } else {
                    Intent intentSearch = new Intent(requireActivity(), BookListBySubCatActivity.class);
                    intentSearch.putExtra("postSubCatName", viewHome.edtHomeSearch.getText().toString());
                    intentSearch.putExtra("type", "SearchHome");
                    startActivity(intentSearch);
                    viewHome.edtHomeSearch.setText("");
                    viewHome.edtHomeSearch.clearFocus();
                }
            }
        });

        viewHome.edtHomeSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (viewHome.edtHomeSearch.getText().toString().isEmpty()) {
                    Toast.makeText(requireActivity(), getString(R.string.search_msg), Toast.LENGTH_SHORT).show();
                } else {
                    Intent intentSearch = new Intent(requireActivity(), BookListBySubCatActivity.class);
                    intentSearch.putExtra("postSubCatName", viewHome.edtHomeSearch.getText().toString());
                    intentSearch.putExtra("type", "SearchHome");
                    startActivity(intentSearch);
                    viewHome.edtHomeSearch.setText("");
                    viewHome.edtHomeSearch.clearFocus();
                }
            }
            return false;
        });

        return viewHome.getRoot();
    }

    private void homeData(String userId) {

        if (getActivity() != null) {

            viewHome.progressHome.setVisibility(View.VISIBLE);

            JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(getActivity()));
            jsObj.addProperty("user_id", userId);
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<HomeRP> call = apiService.getHomeData(API.toBase64(jsObj.toString()));
            call.enqueue(new Callback<HomeRP>() {
                @Override
                public void onResponse(@NotNull Call<HomeRP> call, @NotNull Response<HomeRP> response) {

                    if (getActivity() != null) {

                        try {

                            HomeRP homeRP = response.body();

                            if (homeRP !=null && homeRP.getSuccess() == 1) {

                                if (homeRP.getEbookApp().getFeaturedBooks().size() != 0) {
                                    Update = () -> {
                                        isTimerStart = true;
                                        if (viewHome.vpFeatureHome.getCurrentItem() == (sliderAdapter.getCount() - 1)) {
                                            viewHome.vpFeatureHome.setCurrentItem(0, true);
                                        } else {
                                            viewHome.vpFeatureHome.setCurrentItem(viewHome.vpFeatureHome.getCurrentItem() + 1, true);
                                        }
                                    };

                                    sliderAdapter = new SliderAdapter(getActivity(), homeRP.getEbookApp().getFeaturedBooks());
                                    viewHome.vpFeatureHome.setAdapter(sliderAdapter);
                                    viewHome.vpFeatureHome.setCurrentItem(1);
                                    sliderAdapter.setOnItemClickListener(new OnClick() {
                                        @Override
                                        public void position(int position) {
                                            Intent intentDetail = new Intent(requireActivity(), BookDetailsActivity.class);
                                            intentDetail.putExtra("BOOK_ID", homeRP.getEbookApp().getFeaturedBooks().get(position).getPostId());
                                            startActivity(intentDetail);
                                        }
                                    });

                                    if (sliderAdapter.getCount() > 1) {
                                        timer = new Timer(); // This will create a new Thread
                                        timer.schedule(new TimerTask() { // task to be scheduled
                                            @Override
                                            public void run() {
                                                handler.post(Update);
                                            }
                                        }, DELAY_MS, PERIOD_MS);
                                    }

                                } else {
                                    viewHome.tvHomeFeature.setVisibility(View.GONE);
                                    viewHome.llFeature.setVisibility(View.GONE);
                                }
                                if(homeRP.getEbookApp().getHomeSections()!=null){
                                    homeSectionAdapter = new HomeSectionAdapter(getActivity(), homeRP.getEbookApp().getHomeSections());
                                    viewHome.rvHomeSec.setAdapter(homeSectionAdapter);
                                }

                                if (homeRP.getEbookApp().getContinue_reading().size() != 0) {
                                    continueHomeAdapter = new ContinueHomeAdapter(getActivity(), homeRP.getEbookApp().getContinue_reading());
                                    viewHome.rvContBook.setAdapter(continueHomeAdapter);
                                    continueHomeAdapter.setOnItemClickListener(new OnClick() {
                                        @Override
                                        public void position(int position) {
                                            Intent intentDetail = new Intent(requireActivity(), BookDetailsActivity.class);
                                            intentDetail.putExtra("BOOK_ID", homeRP.getEbookApp().getContinue_reading().get(position).getPostId());
                                            intentDetail.putExtra("LAST_POS", "continuePos");
                                            intentDetail.putExtra("PAGE_NUM", homeRP.getEbookApp().getContinue_reading().get(position).getPage_num());
                                            startActivity(intentDetail);
                                        }
                                    });

                                    viewHome.ivHomeContinue.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //int pos=0;
                                            onClickPos.position(0);
                                        }
                                    });

                                } else {
                                    viewHome.rlConSection.setVisibility(View.GONE);
                                    viewHome.rvContBook.setVisibility(View.GONE);
                                }

                                if (homeRP.getEbookApp().getTrending_books().size() != 0) {
                                    trendingHomeAdapter = new TrendingHomeAdapter(getActivity(), homeRP.getEbookApp().getTrending_books());
                                    viewHome.rvHomeTrendingBook.setAdapter(trendingHomeAdapter);
                                    trendingHomeAdapter.setOnItemClickListener(new OnClick() {
                                        @Override
                                        public void position(int position) {
                                            Intent intentDetail = new Intent(requireActivity(), BookDetailsActivity.class);
                                            intentDetail.putExtra("BOOK_ID", homeRP.getEbookApp().getTrending_books().get(position).getPostId());
                                            startActivity(intentDetail);
                                        }
                                    });

                                    viewHome.ivHomeTrendBook.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intentSubCat = new Intent(requireActivity(), TrendingBookActivity.class);
                                            startActivity(intentSubCat);
                                        }
                                    });
                                } else {
                                    viewHome.rlTrendSection.setVisibility(View.GONE);
                                    viewHome.rvHomeTrendingBook.setVisibility(View.GONE);
                                }

                                viewHome.llHomeMain.setVisibility(View.VISIBLE);

                            } else {
                                viewHome.llNoData.clNoDataFound.setVisibility(View.VISIBLE);
                                method.alertBox(getResources().getString(R.string.failed_try_again));
                            }

                        } catch (Exception e) {
                            Log.d("exception_error", e.toString());
                            method.alertBox(getResources().getString(R.string.failed_try_again));
                        }
                    }
                    viewHome.progressHome.setVisibility(View.GONE);

                }

                @Override
                public void onFailure(@NotNull Call<HomeRP> call, @NotNull Throwable t) {
                    // Log error here since request failed
                    Log.e("fail", t.toString());
                    viewHome.llNoData.clNoDataFound.setVisibility(View.VISIBLE);
                    viewHome.progressHome.setVisibility(View.GONE);
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }
            });
        }
    }

    public void setOnItemClickListener(OnClick clickListener) {
       onClickPos = clickListener;
    }
}
