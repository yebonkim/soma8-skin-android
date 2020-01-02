package com.soma.skinbutler.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.soma.skinbutler.R;
import com.soma.skinbutler.camera.CameraActivity;
import com.soma.skinbutler.login.LoginActivity;

public class SplashActivity extends AppCompatActivity  {
    private static final int WAIT_TIME = 2000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        }, WAIT_TIME);
    }
}