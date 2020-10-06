package com.JHJ_Studio.ttakmanna;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

//새 일정 - 모드선택화면
public class ChoiceModeActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 1001;

    Button b1,b2,b3,b4;

    private long backKeyPressedTime = 0;
    public int selected_mode = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_mode);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //모드 별로 다른 세부사항화면으로 이동해야 하는데 일단 하나의 화면으로 해놓음
        b1 = (Button)findViewById(R.id.mode1);
        b2 = (Button)findViewById(R.id.mode2);
        b3 = (Button)findViewById(R.id.mode3);
        b4 = (Button)findViewById(R.id.mode4);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected_mode = 1;
                Intent intent1 = new Intent(getBaseContext(),DetailModeActivity.class);
                startActivityForResult(intent1,REQUEST_CODE);

                overridePendingTransition(R.anim.enter,R.anim.exit);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected_mode = 2;
                Intent intent1 = new Intent(getBaseContext(),DetailModeActivity.class);
                startActivityForResult(intent1,REQUEST_CODE);

                overridePendingTransition(R.anim.enter,R.anim.exit);
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected_mode = 3;
                Intent intent1 = new Intent(getBaseContext(),DetailModeActivity.class);
                startActivityForResult(intent1,REQUEST_CODE);

                overridePendingTransition(R.anim.enter,R.anim.exit);
            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected_mode = 4;
                Intent intent1 = new Intent(getBaseContext(),DetailModeActivity.class);
                startActivityForResult(intent1,REQUEST_CODE);

                overridePendingTransition(R.anim.enter,R.anim.exit);
            }
        });

    }

    //모드 값 전달
    public int getModeNum(){return selected_mode;};

    //두번 눌러 뒤로가기
    @Override
    public void onBackPressed(){

        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(ChoiceModeActivity.this, "한번 더 누르시면 이전화면으로 돌아갑니다.", Toast.LENGTH_SHORT).show();

        }
        else if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            super.onBackPressed();
            overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
        }
    }
}
