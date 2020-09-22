package com.JHJ_Studio.ttakmanna;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

//일정 출력 화면
public class PrintScheduleActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 1001;

    private BackPressHomeHandler backPressHomeHandler;
    Button b1, b2,b3,b4;
    KakaoSend kakao = new KakaoSend(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_schedule);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        backPressHomeHandler = new BackPressHomeHandler(this);

        //다시 일정 결정 화면으로 이동
        b1 = (Button)findViewById(R.id.backToFix);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),FixScheduleActivity.class);
                startActivityForResult(intent,REQUEST_CODE);

                overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
            }
        });

        //b2~b4는 공유하고 홈으로 이동하는 버튼
        /*b2 = (Button)findViewById(R.id.shareFixFacebook);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kakao.linkMessage();
                Intent intent = new Intent(getBaseContext(),HomeActivity.class);
                startActivityForResult(intent,REQUEST_CODE);

                overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
            }
        });*/
        b3 = (Button)findViewById(R.id.shareFixKakao);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kakao.shareMessage("졸프","유빈, 윤주, 지원","목요일","02:00 PM","시청역 파스쿠찌","서울특별시 중구 세종대로22길 12 뉴 국제호텔 1층");
                Intent intent = new Intent(getBaseContext(),HomeActivity.class);
                startActivityForResult(intent,REQUEST_CODE);

                overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
            }
        });
        /*b4 = (Button)findViewById(R.id.shareFixTwitter);
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),HomeActivity.class);
                startActivityForResult(intent,REQUEST_CODE);

                overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
            }
        });*/
    }


    //백버튼 누르면 홈화면으로 이동
    @Override
    public void onBackPressed(){
        backPressHomeHandler.onBackPressed();
        overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
    }

}
