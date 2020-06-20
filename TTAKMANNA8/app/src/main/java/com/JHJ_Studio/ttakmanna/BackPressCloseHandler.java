package com.JHJ_Studio.ttakmanna;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class BackPressCloseHandler extends AppCompatActivity {
    private long backKeyPressedTime = 0;
    private Toast toast;

    private Activity activity;

    public BackPressCloseHandler(Activity context){
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

            activity.moveTaskToBack(true);
            activity.finish();

            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    public void showGuide() {
        toast = Toast.makeText(activity, "한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
        toast.show();
    }

}
