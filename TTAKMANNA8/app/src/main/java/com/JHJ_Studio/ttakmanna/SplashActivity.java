package com.JHJ_Studio.ttakmanna;

import android.os.Handler;
import android.os.Bundle;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

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
