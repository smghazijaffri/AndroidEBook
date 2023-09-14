package com.example.util;

import android.app.Activity;
import android.content.res.Configuration;
import android.view.View;
import android.view.WindowManager;

import com.example.androidebookapps.R;


public class StatusBar {

    public static void init(Activity activity) {
        Method method=new Method(activity);
        if (method.themMode().equals("light")) {
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else if (method.themMode().equals("system")) {
            int currentNightMode = activity.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
            switch (currentNightMode) {
                case Configuration.UI_MODE_NIGHT_NO:
                    activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                    activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    break;
                case Configuration.UI_MODE_NIGHT_YES:
                    // Night mode is active on device
                    activity. getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                    break;
            }
        }else if (method.themMode().equals("dark")) {
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
     }

    public static void initWhite(Activity activity) {
        Method method=new Method(activity);
        if (method.themMode().equals("light")) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            activity.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.app_bg));
        } else if (method.themMode().equals("system")) {
            int currentNightMode = activity.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
            switch (currentNightMode) {
                case Configuration.UI_MODE_NIGHT_NO:
                    activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    activity.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.app_bg));
                    break;
                case Configuration.UI_MODE_NIGHT_YES:
                    // Night mode is active on device
                    break;
            }
        }else if (method.themMode().equals("dark")) {
            activity.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.app_bg));
        }
    }

    public static void initWhiteFilter(Activity activity) {
        Method method=new Method(activity);
        if (method.themMode().equals("light")) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            activity.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.app_bg_white));
        } else if (method.themMode().equals("system")) {
            int currentNightMode = activity.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
            switch (currentNightMode) {
                case Configuration.UI_MODE_NIGHT_NO:
                    activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    activity.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.app_bg_white));
                    break;
                case Configuration.UI_MODE_NIGHT_YES:
                    // Night mode is active on device
                    break;
            }
        }else if (method.themMode().equals("dark")) {
            activity.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.app_bg_white));
        }
    }

    public static void initDeepOrange(Activity activity) {
        Method method=new Method(activity);
        if (method.themMode().equals("light")) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            activity.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.deep_orange_bg));
        } else if (method.themMode().equals("system")) {
            int currentNightMode = activity.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
            switch (currentNightMode) {
                case Configuration.UI_MODE_NIGHT_NO:
                    activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    activity.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.deep_orange_bg));
                    break;
                case Configuration.UI_MODE_NIGHT_YES:
                    // Night mode is active on device
                    break;
            }
        }else if (method.themMode().equals("dark")) {
            activity.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.deep_orange_bg));
        }
    }

}
