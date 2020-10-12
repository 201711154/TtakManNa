package com.JHJ_Studio.ttakmanna;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

//새 일정 - 메인화면
public class NewScheduleActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 1001;
    private long backKeyPressedTime = 0;

    Button b1;
    EditText rnTxt;
    EditText numTxt;
    Spinner purSpinner;

    Room room = new Room();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_schedule);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //모드 선택 화면으로 이동
        b1 = (Button)findViewById(R.id.goModeButton);
        rnTxt = (EditText) findViewById(R.id.groupName);
        numTxt = (EditText) findViewById(R.id.member);
        purSpinner = (Spinner)findViewById(R.id.spinner_why);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRoomData();
                if(room.getRoomName() != null && room.getNumber() != -1 && room.getPurpose() != 0) {
                    Intent intent = new Intent(getBaseContext(), ChoiceModeActivity.class);
                    intent.putExtra("roomData", room);

                    startActivityForResult(intent, REQUEST_CODE);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                }

            }
        });


    }

    public void setRoomData(){
        String roomName = rnTxt.getText().toString();
        int number = -1;
        try{
            number = Integer.parseInt(numTxt.getText().toString());
        }catch (NumberFormatException e){number = -1;}
        int purpose = 0;
        String spinner = purSpinner.getSelectedItem().toString();
        Log.d("purpose",spinner);
        if(spinner.equals("회의")){purpose = 1;}
        else if(spinner.equals("식사")){purpose = 2;}
        else if(spinner.equals("수다")){purpose = 3;}
        Log.d("purpose",Integer.toString(purpose));

        room.setRoomName(roomName);
        room.setNumber(number);
        room.setPurpose(purpose);
        room.setClosed(0);
        room.setSelected(0);
    }

    //두번 눌러 뒤로가기
    @Override
    public void onBackPressed(){

        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(NewScheduleActivity.this, "한번 더 누르시면 이전화면으로 돌아갑니다.", Toast.LENGTH_SHORT).show();

        }
        else if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            super.onBackPressed();
            overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
        }
    }

}
