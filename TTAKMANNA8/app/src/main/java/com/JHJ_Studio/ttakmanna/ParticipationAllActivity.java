package com.JHJ_Studio.ttakmanna;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
//일정 입력 현황 화면 2(다 모인 후)
public class ParticipationAllActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 1001;

    Button b1;
    private BackPressHomeHandler backPressHomeHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participation_all);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        backPressHomeHandler = new BackPressHomeHandler(this);
        //일정 결정 화면
        b1 = (Button)findViewById(R.id.goFixScheduleButton);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),FixScheduleActivity.class);
                startActivityForResult(intent,REQUEST_CODE);

                overridePendingTransition(R.anim.enter,R.anim.exit);
            }
        });
    }
    //뒤로가기 두번 눌러 홈화면으로 이동
    @Override
    public void onBackPressed(){
        backPressHomeHandler.onBackPressed();
        overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
    }

}
