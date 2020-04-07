package com.example.ttakmanna8;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

//첫화면, 로그인 화면
public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 1001;

    private BackPressCloseHandler backPressCloseHandler;
    Button b1,b2,b3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        backPressCloseHandler = new BackPressCloseHandler(this);


        //b1,2,3 SNS에서 사용자 정보 받아와서 로그인 하고 홈으로 이동하는 버튼
        b1 = (Button)findViewById(R.id.login_facebook);
        b2 = (Button)findViewById(R.id.login_kakao);
        b3 = (Button)findViewById(R.id.login_twitter);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),HomeActivity.class);
                startActivityForResult(intent,REQUEST_CODE);

                overridePendingTransition(R.anim.enter,R.anim.exit);

            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),HomeActivity.class);
                startActivityForResult(intent,REQUEST_CODE);

                overridePendingTransition(R.anim.enter,R.anim.exit);
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),HomeActivity.class);
                startActivityForResult(intent,REQUEST_CODE);

                overridePendingTransition(R.anim.enter,R.anim.exit);
            }
        });
    }
    //백버튼 누르면 앱 종료
    @Override
    public void onBackPressed(){
        backPressCloseHandler.onBackPressed();
    }
}
