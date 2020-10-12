package com.JHJ_Studio.ttakmanna;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

//새 일정 - 모드선택화면
public class ChoiceModeActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 1001;

    Button b1,b2,b3,b4;

    private long backKeyPressedTime = 0;
    public int selected_mode = -1;
    private Room room = new Room();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_mode);

        room = (Room) getIntent().getSerializableExtra("roomData");
        
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        b1 = (Button)findViewById(R.id.mode1);
        b2 = (Button)findViewById(R.id.mode2);
        b3 = (Button)findViewById(R.id.mode3);
        b4 = (Button)findViewById(R.id.mode4);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected_mode = 1;
                changePage(selected_mode);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected_mode = 2;
                changePage(selected_mode);
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected_mode = 3;
                changePage(selected_mode);
            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected_mode = 4;
               changePage(selected_mode);
            }
        });

    }

    public void changePage(int selectMode){
        room.setMode(selectMode);
        Intent intent = new Intent(getBaseContext(),DetailModeActivity.class);
        intent.putExtra("roomData", room);
        startActivityForResult(intent,REQUEST_CODE);

        overridePendingTransition(R.anim.enter,R.anim.exit);
        Log.d("name",room.getRoomName());
        Log.d("number",Integer.toString(room.getNumber()));
        Log.d("mode",Integer.toString(room.getMode()));
        Log.d("purpose",Integer.toString(room.getPurpose()));
        Log.d("closed",Integer.toString(room.getClosed()));
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
