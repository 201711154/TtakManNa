package com.JHJ_Studio.ttakmanna;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import net.daum.mf.map.api.MapView;

import java.nio.file.FileAlreadyExistsException;
import java.time.DayOfWeek;
import java.util.logging.Logger;

//새 일정 - 세부사항화면
public class DetailModeActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 1001;
    private long backKeyPressedTime = 0;

    Button b1;
    private RadioGroup checkModeGroup; // 불가능한 날짜인지 가능한 날짜인지
    private CheckBox[] dayOfWeek = new CheckBox[7]; // 요일 박스
    private int[] week = {R.id.monday, R.id.tuesday, R.id.wednesday, R.id.thursday, R.id.friday, R.id.saturday, R.id.sunday};

    // 얻은 데이터
    private boolean is_it_possible_day = false; // 가능한 요일인지 불가능한 요일인지 확인
    private boolean[] is_date_ok = new boolean[7]; // 요일 확인
    private TimePicker time_from, time_to; // 시간

    // 데이터 전송
    public boolean get_Possible_OK(){return is_it_possible_day;};
    public boolean get_Is_Date_OK(int day){return is_date_ok[day];}; //0:monday ~ 6:sunday
    public TimePicker get_OK_Time(int type){
        if (type == 0) return time_from;
        else if (type == 1) return time_to;
        else {
            System.out.println("WARN: DetailModeActivity.java의 get_OK_Time에 잘못된 값이 입력되었습니다.\n확인 후 코드 수정 부탁합니다.");
            return time_to; //일단은 time_to 반환
        }
    }


   @Override
    protected void onCreate(Bundle savedInstanceState) {
        for (boolean datecheck : is_date_ok) datecheck = false; // 초기화
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_mode);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // 입력 정보
        EditText name = (EditText)findViewById(R.id.nickname);

        // 불가능한 요일인지 가능한 요일인지 체크
        checkModeGroup = (RadioGroup)findViewById(R.id.pos_impos);
        checkModeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.impossible){
                    is_it_possible_day = false;
                }
                else{
                    is_it_possible_day = true;
                }
            }
        });

        // 무슨 요일이 가능한지 체크 - 미안 for문으로 줄여두려고 했는데 안되더라 가독성은... 알아서 봐줘 S2
        dayOfWeek[0] = (CheckBox) findViewById(week[0]);
        dayOfWeek[0].setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    is_date_ok[0] = true;
                } else {
                    is_date_ok[0] = false;
                }
            }
        });
        dayOfWeek[1] = (CheckBox) findViewById(week[1]);
        dayOfWeek[1].setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    is_date_ok[1] = true;
                } else {
                    is_date_ok[1] = false;
                }
            }
        });
        dayOfWeek[2] = (CheckBox) findViewById(week[2]);
        dayOfWeek[2].setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    is_date_ok[2] = true;
                } else {
                    is_date_ok[2] = false;
                }
            }
        });
        dayOfWeek[3] = (CheckBox) findViewById(week[3]);
        dayOfWeek[3].setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    is_date_ok[3] = true;
                } else {
                    is_date_ok[3] = false;
                }
            }
        });
        dayOfWeek[4] = (CheckBox) findViewById(week[4]);
        dayOfWeek[4].setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    is_date_ok[4] = true;
                } else {
                    is_date_ok[4] = false;
                }
            }
        });
        dayOfWeek[5] = (CheckBox) findViewById(week[5]);
        dayOfWeek[5].setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    is_date_ok[5] = true;
                } else {
                    is_date_ok[5] = false;
                }
            }
        });
        dayOfWeek[6] = (CheckBox) findViewById(week[6]);
        dayOfWeek[6].setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    is_date_ok[6] = true;
                } else {
                    is_date_ok[6] = false;
                }
            }
        });

        // 위치 정보 받아옴
       MapView mapView = new MapView(this);

       RelativeLayout mapViewContainer = (RelativeLayout) findViewById(R.id.mapView);
       mapViewContainer.addView(mapView);

        // 시간 정보 받아옴
        time_from = (TimePicker) findViewById(R.id.time_from);
        time_to = (TimePicker) findViewById(R.id.time_to);


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
