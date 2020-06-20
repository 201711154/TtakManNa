package com.JHJ_Studio.ttakmanna;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

//일정 입력 현황 화면1(다 모이기 전)
public class ParticipationCheckActivity extends AppCompatActivity {

    private BackPressHomeHandler backPressHomeHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participation_check);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        backPressHomeHandler = new BackPressHomeHandler(this);

    }
    //뒤로가기 두번 눌러 홈화면으로
    @Override
    public void onBackPressed(){
        backPressHomeHandler.onBackPressed();
        overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
    }

}
