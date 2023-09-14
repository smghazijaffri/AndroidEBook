package com.example.androidebookapps;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidebookapps.databinding.LayoutPaymentSuccessBinding;
import com.example.util.Method;
import com.example.util.StatusBar;

public class SuccessActivity extends AppCompatActivity {

    LayoutPaymentSuccessBinding successBinding;
    String msg;
    Method method;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.initDeepOrange(SuccessActivity.this);
        successBinding = LayoutPaymentSuccessBinding.inflate(getLayoutInflater());
        setContentView(successBinding.getRoot());

        method=new Method(SuccessActivity.this);
        method.forceRTLIfSupported();

        Intent intent=getIntent();
        msg=intent.getStringExtra("MSG");

        successBinding.tvSuccessMsg.setText(msg);
        successBinding.btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMain=new Intent(SuccessActivity.this,MainActivity.class);
                startActivity(intentMain);
                finishAffinity();
            }
        });


    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}