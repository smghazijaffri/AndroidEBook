package com.example.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.androidebookapps.LoginActivity;
import com.example.androidebookapps.R;
import com.example.androidebookapps.databinding.FragmentIntroBinding;


public class IntroFragment extends Fragment {

    FragmentIntroBinding viewIntro;
    final static String LAYOUT_ID = "layoutid";

    public static IntroFragment newInstance(int layoutId) {
        IntroFragment pane = new IntroFragment();
        Bundle args = new Bundle();
        args.putInt(LAYOUT_ID, layoutId);
        pane.setArguments(args);
        return pane;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        requireActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.app_bg));
        viewIntro = FragmentIntroBinding.inflate(inflater, container, false);
        int position = getArguments().getInt(LAYOUT_ID, -1);

        Integer[] Images = {R.drawable.img_intro_1, R.drawable.img_intro_2, R.drawable.img_intro_3};
        String[] Title = {getResources().getString(R.string.intro_title_1),
                getResources().getString(R.string.intro_title_2),
                getResources().getString(R.string.intro_title_3)};
        String[] Desc = {getResources().getString(R.string.intro_desc_1),
                getResources().getString(R.string.intro_desc_2),
                getResources().getString(R.string.intro_desc_3)};

        viewIntro.ivIntro.setImageResource(Images[position]);
        viewIntro.tvBooksTitle.setText(Title[position]);
        viewIntro.tvBookDesc.setText(Desc[position]);

        if (position == 2) {
            viewIntro.btnGetStart.setVisibility(View.VISIBLE);
            viewIntro.view.setVisibility(View.GONE);
            viewIntro.ivIntro.setPadding(0, 0, 0, 0);
            viewIntro.ivIntro.setScaleType(ImageView.ScaleType.FIT_XY);
        } else {
            viewIntro.btnGetStart.setVisibility(View.GONE);
            viewIntro.view.setVisibility(View.VISIBLE);
        }

        viewIntro.btnGetStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                requireActivity().finish();
            }
        });


        return viewIntro.getRoot();
    }
}
