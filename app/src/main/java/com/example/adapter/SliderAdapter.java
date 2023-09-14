package com.example.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.androidebookapps.R;
import com.example.androidebookapps.databinding.RowSliderHomeBinding;
import com.example.item.FeaturedBook;
import com.example.util.AdInterstitialAds;
import com.example.util.EnchantedViewPager;
import com.example.util.OnClick;

import java.util.List;


public class SliderAdapter extends PagerAdapter {

    Activity activity;
    List<FeaturedBook> sliderLists;
    LayoutInflater inflater;
    RowSliderHomeBinding viewAdapterSlider;
    OnClick onClick;

    public SliderAdapter(Activity activity, List<FeaturedBook> sliderLists) {
        this.activity = activity;
        this.sliderLists = sliderLists;
        inflater = activity.getLayoutInflater();
    }


    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        viewAdapterSlider = RowSliderHomeBinding.inflate(inflater, container, false);
        View view = viewAdapterSlider.getRoot();

        if (!sliderLists.get(position).getPostImage().equals("")) {
            Glide.with(activity.getApplicationContext()).load(sliderLists.get(position).getPostImage())
                    .placeholder(R.drawable.placeholder_landscape)
                    .into(viewAdapterSlider.ivHomeFeature);
        }

        viewAdapterSlider.tvBookName.setText(sliderLists.get(position).getPostTitle());

        viewAdapterSlider.cvFeature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdInterstitialAds.ShowInterstitialAds(activity,position,onClick);
            }
        });


        view.setTag(EnchantedViewPager.ENCHANTED_VIEWPAGER_POSITION + position);
        container.addView(view, 0);
        return view;

    }

    @Override
    public int getCount() {
        return sliderLists.size();
    }

    public void setOnItemClickListener(OnClick clickListener) {
        this.onClick = clickListener;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        (container).removeView((View) object);
    }
}

