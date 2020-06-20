package com.JHJ_Studio.ttakmanna;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class BackPressHomeHandler extends AppCompatActivity {

    private long backKeyPressedTime = 0;
    private Toast toast;

    private Activity activity;

    public BackPressHomeHandler(Activity context){
        this.activity = context;
    }

    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            toast.cancel();

            Intent t = new Intent(activity, HomeActivity.class);
            activity.startActivity(t);
            return;
        }
    }

    public void showGuide() {
        toast = Toast.makeText(activity, "한번 더 누르시면 홈화면으로 돌아갑니다.", Toast.LENGTH_SHORT);
        toast.show();
    }

}
