package com.fuicuiedu.idedemo.easyshop_demo.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.fuicuiedu.idedemo.easyshop_demo.R;
import com.fuicuiedu.idedemo.easyshop_demo.commons.ActivityUtils;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    private ActivityUtils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        utils = new ActivityUtils(this);

        Timer timer = new Timer();
        //1.5秒后跳转到主页面并且finish
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                utils.startActivity(MainActivity.class);
                finish();
            }
        },1500);
    }
}
