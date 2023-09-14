package com.example.androidebookapps;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.example.androidebookapps.databinding.ActivityIntroBinding;
import com.example.fragment.IntroFragment;
import com.example.util.Method;
import com.example.util.StatusBar;

public class IntroActivity extends AppCompatActivity {

    ActivityIntroBinding viewIntroBinding;
    PagerAdapter pagerAdapter;
    Method method;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.init(IntroActivity.this);

        viewIntroBinding = ActivityIntroBinding.inflate(getLayoutInflater());
        View view = viewIntroBinding.getRoot();
        setContentView(view);
        method = new Method(IntroActivity.this);
        method.forceRTLIfSupported();

        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        viewIntroBinding.vpIntro.setAdapter(pagerAdapter);
        viewIntroBinding.dotsIndicator.setViewPager(viewIntroBinding.vpIntro);

        viewIntroBinding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewIntroBinding.vpIntro.setCurrentItem(viewIntroBinding.vpIntro.getCurrentItem() + 1);
            }
        });

        viewIntroBinding.vpIntro.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 2) {
                    viewIntroBinding.btnNext.setVisibility(View.GONE);
                    viewIntroBinding.dotsIndicator.setVisibility(View.GONE);
                } else {
                    viewIntroBinding.btnNext.setVisibility(View.VISIBLE);
                    viewIntroBinding.dotsIndicator.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            IntroFragment tp;
            tp = IntroFragment.newInstance(position);
            return tp;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}