package com.example.ttakmanna8;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;

public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_TIME = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplication(),MainActivity.class));
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_TIME);
    }
    @Override
    public void onBackPressed(){

    }
}
