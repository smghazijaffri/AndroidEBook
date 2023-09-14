package com.example.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.example.androidebookapps.R;
import com.example.androidebookapps.SplashActivity;
import com.example.androidebookapps.databinding.LayoutThemeBinding;
import com.example.util.Method;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.radiobutton.MaterialRadioButton;


public class ThemeChangeFragment extends BottomSheetDialogFragment {

    LayoutThemeBinding viewThemeBinding;
    Method method;
    private String themMode;

    @SuppressLint("NonConstantResourceId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        viewThemeBinding = LayoutThemeBinding.inflate(inflater, container, false);
        method = new Method(requireActivity());
        method.forceRTLIfSupported();


        switch (method.themMode()) {
            case "system":
                viewThemeBinding.radioGroupThem.check(viewThemeBinding.radioGroupThem.getChildAt(0).getId());
                break;
            case "light":
                viewThemeBinding.radioGroupThem.check(viewThemeBinding.radioGroupThem.getChildAt(1).getId());
                break;
            case "dark":
                viewThemeBinding.radioGroupThem.check(viewThemeBinding.radioGroupThem.getChildAt(2).getId());
                break;
        }

        viewThemeBinding.radioGroupThem.setOnCheckedChangeListener((group, checkedId) -> {
            MaterialRadioButton rb = group.findViewById(checkedId);
            if (null != rb && checkedId > -1) {
                switch (checkedId) {
                    case R.id.radioButton_system_them:
                        themMode = "system";
                        break;
                    case R.id.radioButton_light_them:
                        themMode = "light";
                        break;
                    case R.id.radioButton_dark_them:
                        themMode = "dark";
                        break;
                    default:
                        break;
                }
            }
        });

        viewThemeBinding.btnMaybeLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        viewThemeBinding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                method.editor.putString(method.themSetting, themMode);
                method.editor.commit();
                dismiss();

                startActivity(new Intent(requireActivity(), SplashActivity.class));
                requireActivity().finishAffinity();
            }
        });
        return viewThemeBinding.getRoot();
    }
}