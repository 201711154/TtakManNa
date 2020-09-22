package com.JHJ_Studio.ttakmanna;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

//새 일정 - 세부사항화면
public class DetailModeActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 1001;
    private long backKeyPressedTime = 0;

    Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_mode);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //모두 참여했는지 안했는지에 따라서 다음 화면이 달라지는 버튼, i로 일단 연결해 놓았음
        b1 = (Button)findViewById(R.id.goCompleteButton);

        b1.setOnClickListener(new View.OnClickListener() {

            int i = 0;

            @Override
            public void onClick(View v) {
                if(i == 1) {
                    //일정 입력 현황화면 1(기다리는 화면)으로 이동
                    Intent intent = new Intent(getBaseContext(), ParticipationCheckActivity.class);
                    startActivityForResult(intent, REQUEST_CODE);

                    overridePendingTransition(R.anim.enter,R.anim.exit);
                }
                else{
                    //일정 입력 현황화면2(모두 참여한 후 화면)으로 이동
                    Intent intent = new Intent(getBaseContext(), ParticipationAllActivity.class);
                    startActivityForResult(intent, REQUEST_CODE);

                    overridePendingTransition(R.anim.enter,R.anim.exit);
                }
            }
        });
    }
    //두번눌러 뒤로가기
    @Override
    public void onBackPressed(){

        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(DetailModeActivity.this, "한번 더 누르시면 이전화면으로 돌아갑니다.", Toast.LENGTH_SHORT).show();

        }
        else if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            super.onBackPressed();
            overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
        }
    }
}
