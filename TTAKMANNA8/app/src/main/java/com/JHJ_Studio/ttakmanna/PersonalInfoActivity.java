package com.JHJ_Studio.ttakmanna;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

//개인정보 설정 화면
public class PersonalInfoActivity extends AppCompatActivity {

    private long backKeyPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


    }
    //두번 눌러 뒤로가기
    @Override
    public void onBackPressed(){

        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(PersonalInfoActivity.this, "한번 더 누르시면 이전화면으로 돌아갑니다.", Toast.LENGTH_SHORT).show();

        }
        else if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            super.onBackPressed();
            overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
        }
    }
}
