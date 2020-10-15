package com.JHJ_Studio.ttakmanna;

import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

//일정 입력 현황 화면1(다 모이기 전)
public class ParticipationCheckActivity extends AppCompatActivity {

    private String participantNumUrl = "http://ttakmanna.com/Android/getParticipantNum.php";
    public static final int REQUEST_CODE = 1001;

    private BackPressHomeHandler backPressHomeHandler;
    private String roomName;
    private ArrayList<Room> rooms;
    private Room room;
    private int pos;
    private int roomKey;
    private int nowNum;
    private int totalNum;
    TextView nowNumTxt;
    TextView totalNumTxt;
    Button kakaoBtn;

    KakaoSend kakao = new KakaoSend(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participation_check);
        roomName = getIntent().getStringExtra("RoomName");
        rooms = (ArrayList<Room>) getIntent().getSerializableExtra("Room");
        pos = getIntent().getIntExtra("Pos",-1);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        backPressHomeHandler = new BackPressHomeHandler(this);

        nowNumTxt = (TextView) findViewById(R.id.stateParticipation);
        totalNumTxt = (TextView) findViewById(R.id.totalParticipation);
        kakaoBtn = (Button) findViewById(R.id.shareFixKakao);
        kakaoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kakao.linkMessage(roomKey);
                Intent intent = new Intent(getBaseContext(),HomeActivity.class);
                startActivityForResult(intent,REQUEST_CODE);

                overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
            }
        });

        if(pos != -1){
            room = rooms.get(pos);
            totalNum = room.getNumber();
            roomKey = room.getRoomKey();
            nowNumTxt.setText(Integer.toString(totalNum));

            ContentValues value = new ContentValues();
            value.put("roomKey", roomKey);

            ParticipantNumTask pnt = new ParticipantNumTask(value, room.getRoomKey());
            pnt.execute();
        }

    }

    class ParticipantNumTask extends AsyncTask<Void, Void, String> {
        String result;
        int roomKey;
        int _now = -1;
        ContentValues values;

        ParticipantNumTask(ContentValues values, int roomKey){
            this.values = values;
            this.roomKey = roomKey;
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            result = requestHttpURLConnection.request(participantNumUrl, values);
            return result;
        }


        @Override
        protected void onPostExecute(String s){
            super.onPostExecute(s);
            getCheck(s);
            nowNum = _now;
            nowNumTxt.setText(Integer.toString(nowNum));
        }
        public void getCheck(String s){
            _now = Integer.parseInt(s);
            Log.d("check",Integer.toString(_now));

        }
    }

    //뒤로가기 두번 눌러 홈화면으로
    @Override
    public void onBackPressed(){
        backPressHomeHandler.onBackPressed();
        overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
    }

}
