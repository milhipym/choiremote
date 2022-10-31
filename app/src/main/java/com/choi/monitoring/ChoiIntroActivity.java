package com.choi.monitoring;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.appcompat.app.AppCompatActivity;

import com.choi.monitoring.callapi.CallAPIServise;

import org.json.JSONArray;

public class ChoiIntroActivity extends AppCompatActivity {
    public Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        //callApiFirst();
        //go2Main();
        Handler handle = new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                finish();
            }

        };
        handle.sendEmptyMessageDelayed(0, 15000);
    }


}
