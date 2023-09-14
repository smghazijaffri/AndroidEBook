package com.example.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.androidebookapps.EditProfileActivity;
import com.example.androidebookapps.LoginActivity;
import com.example.androidebookapps.R;
import com.example.androidebookapps.databinding.FragmentProfileBinding;
import com.example.response.LoginRP;
import com.example.rest.ApiClient;
import com.example.rest.ApiInterface;
import com.example.util.API;
import com.example.util.Events;
import com.example.util.GlobalBus;
import com.example.util.Method;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.greenrobot.eventbus.Subscribe;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileFragment extends Fragment {

    FragmentProfileBinding viewProfile;
    Method method;
    LoginRP.ItemUser itemUser;
    FragmentManager childFragmentManager;
    boolean isContinue = false;
    String imageProfile;
    int movePos;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        requireActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.app_bg_orange));
        viewProfile = FragmentProfileBinding.inflate(inflater, container, false);

        if (getActivity() != null) {
            method = new Method(requireActivity());
            childFragmentManager = getChildFragmentManager();
            GlobalBus.getBus().register(this);

            if (getArguments() != null) {
                isContinue = getArguments().getBoolean("isContinue");
                movePos = getArguments().getInt("movePos");
            }

            viewProfile.tvEdtTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentProfile = new Intent(requireActivity(), EditProfileActivity.class);
                    intentProfile.putExtra("uId", itemUser.getUser_id());
                    intentProfile.putExtra("uName", itemUser.getName());
                    intentProfile.putExtra("uEmail", itemUser.getEmail());
                    intentProfile.putExtra("uImage", imageProfile);
                    intentProfile.putExtra("uPhone", itemUser.getPhone());
                    intentProfile.putExtra("uType", method.getUserType());
                    startActivity(intentProfile);
                }
            });

            if (method.getIsLogin()) {
                viewProfile.llLogin.setVisibility(View.GONE);
                viewProfile.llProfile.setVisibility(View.GONE);
                viewProfile.llProfile2.setVisibility(View.GONE);
                viewProfile.tvEdtTitle.setVisibility(View.VISIBLE);
                viewProfile.progressProfile.setVisibility(View.GONE);
                viewProfile.llNoData.clNoDataFound.setVisibility(View.GONE);
                userProfile();
            } else {
                viewProfile.llLogin.setVisibility(View.VISIBLE);
                viewProfile.llProfile.setVisibility(View.GONE);
                viewProfile.llProfile2.setVisibility(View.GONE);
                viewProfile.tvEdtTitle.setVisibility(View.GONE);
                viewProfile.progressProfile.setVisibility(View.GONE);
                viewProfile.llNoData.clNoDataFound.setVisibility(View.GONE);
            }

            setupViewPager(viewProfile.vpTab);
            new TabLayoutMediator(viewProfile.tabLayout, viewProfile.vpTab, new TabLayoutMediator.TabConfigurationStrategy() {
                @Override
                public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        if (position == 0) {
                            tab.setText(getString(R.string.tab_favorite));
                        } else if (position == 1) {
                            tab.setText(getString(R.string.tab_download));
                        } else if (position == 2) {
                            tab.setText(getString(R.string.tab_continue));
                        } else if (position == 3) {
                            tab.setText(getString(R.string.tab_subs));
                        } else if (position == 4) {
                            tab.setText(getString(R.string.tab_rent));
                        }
                }
            }).attach();


            viewProfile.btnLogIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentLogin = new Intent(requireActivity(), LoginActivity.class);
                    startActivity(intentLogin);
                    requireActivity().finishAffinity();
                }
            });

        }
        return viewProfile.getRoot();
    }

    private void setupViewPager(final ViewPager2 viewPager) {
        final ViewPagerAdapter adapter = new ViewPagerAdapter(requireActivity());
        adapter.addFragment(new FavoriteFragment(), getString(R.string.tab_favorite));
        adapter.addFragment(new DownloadFragment(), getString(R.string.tab_download));
        adapter.addFragment(new ContinueFragment(), getString(R.string.tab_continue));
        adapter.addFragment(new DashBoardFragment(), getString(R.string.tab_subs));
        adapter.addFragment(new RentBookFragment(), getString(R.string.tab_rent));
        viewPager.setAdapter(adapter);
        if (isContinue) {
            viewPager.setCurrentItem(movePos,false);
        }
    }

    class ViewPagerAdapter extends FragmentStateAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        private ViewPagerAdapter(FragmentActivity activity) {
            super(activity);
        }

        private void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getItemCount() {
            return mFragmentList.size();
        }
    }

    public void userProfile() {
        if (getActivity() != null) {
            viewProfile.progressProfile.setVisibility(View.VISIBLE);

            JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(requireActivity()));
            jsObj.addProperty("user_id", method.getUserId());
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<LoginRP> call = apiService.getProfileData(API.toBase64(jsObj.toString()));
            call.enqueue(new Callback<LoginRP>() {
                @Override
                public void onResponse(@NotNull Call<LoginRP> call, @NotNull Response<LoginRP> response) {
                    if (getActivity() != null) {
                        try {
                            LoginRP loginRPSocial = response.body();

                            if (loginRPSocial != null && loginRPSocial.getSuccess().equals("1")) {
                                if (loginRPSocial.getItemUserList().get(0).getSuccess().equals("1")) {
                                    itemUser = loginRPSocial.getItemUserList().get(0);
                                    viewProfile.llProfile.setVisibility(View.VISIBLE);
                                    viewProfile.llProfile2.setVisibility(View.VISIBLE);
                                    viewProfile.tvEdtTitle.setVisibility(View.VISIBLE);
                                    imageProfile = itemUser.getUser_image();
                                    if (!imageProfile.isEmpty()) {
                                        Glide.with(requireActivity().getApplicationContext()).load(imageProfile)
                                                .placeholder(R.drawable.user_profile)
                                                .into(viewProfile.ivUser);
                                    }

                                    viewProfile.tvProfileName.setText(itemUser.getName());
                                    viewProfile.tvProfileEmail.setText(itemUser.getEmail());

                                } else {
                                    method.alertBox(loginRPSocial.getItemUserList().get(0).getMsg());
                                }
                            } else {
                                viewProfile.llNoData.clNoDataFound.setVisibility(View.VISIBLE);
                                viewProfile.progressProfile.setVisibility(View.GONE);
                                method.alertBox(getString(R.string.failed_try_again));
                            }

                        } catch (Exception e) {
                            Log.d("exception_error", e.toString());
                            method.alertBox(getString(R.string.failed_try_again));
                        }
                    }
                    viewProfile.progressProfile.setVisibility(View.GONE);

                }

                @Override
                public void onFailure(@NotNull Call<LoginRP> call, @NotNull Throwable t) {
                    // Log error here since request failed
                    Log.e("fail", t.toString());
                    viewProfile.llNoData.clNoDataFound.setVisibility(View.VISIBLE);
                    viewProfile.progressProfile.setVisibility(View.GONE);
                    method.alertBox(getString(R.string.failed_try_again));
                }
            });
        }
    }


    @Subscribe
    public void getName(Events.ProfileUpdate profileUpdate) {
        imageProfile = profileUpdate.getImage();
        Glide.with(requireActivity().getApplicationContext()).load(imageProfile)
                .placeholder(R.drawable.placeholder_portable)
                .into(viewProfile.ivUser);
        viewProfile.tvProfileName.setText(profileUpdate.getName());
        itemUser.setName(profileUpdate.getName());
        itemUser.setPhone(profileUpdate.getPhone());

    }
}
