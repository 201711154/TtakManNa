package com.JHJ_Studio.ttakmanna;

import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import net.daum.mf.map.api.MapView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

//일정 출력 화면
public class PrintScheduleActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 1001;
    String GetFixDataUrl = "http://ttakmanna.com/Android/getFixData.php";

    private BackPressHomeHandler backPressHomeHandler;
    Button changeBtn;
    Button kakaoBtn;
    TextView groupNameTv;
    TextView participantTv;
    TextView timeTv;
    TextView destinationTv;

    KakaoSend kakao = new KakaoSend(this);

    int roomKey;
    ArrayList<Room> rooms = new ArrayList<>();
    String roomName;
    int pos;

    String time;
    String destination;
    float destLat;
    float destLong;
    String printText;
    String printNick="";
    ArrayList<String> participant = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_schedule);
        rooms = (ArrayList<Room>) getIntent().getSerializableExtra("Room");
        roomName = getIntent().getStringExtra("RoomName");
        pos = getIntent().getIntExtra("Pos",0);
        roomKey = rooms.get(pos).getRoomKey();
        Log.d("roomKey",Integer.toString(roomKey));

        groupNameTv = (TextView)findViewById(R.id.printGroupName2);
        participantTv = (TextView) findViewById(R.id.printFixWho);
        timeTv = (TextView) findViewById(R.id.printFixWhen);
        destinationTv = (TextView) findViewById(R.id.printFixWhere);
        changeBtn = (Button)findViewById(R.id.backToFix);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        backPressHomeHandler = new BackPressHomeHandler(this);

        //다시 일정 결정 화면으로 이동
        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),FixScheduleActivity.class);
                intent.putExtra("RoomName",roomName);
                intent.putExtra("Pos",pos);
                intent.putExtra("Room",rooms);
                startActivityForResult(intent,REQUEST_CODE);

                overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
            }
        });

        kakaoBtn = (Button)findViewById(R.id.shareFixKakao);
        kakaoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kakao.shareMessage(roomName,printNick,time,destination);
                Intent intent = new Intent(getBaseContext(),HomeActivity.class);
                startActivityForResult(intent,REQUEST_CODE);

                overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
            }
        });

        printFixDataDB();
        groupNameTv.setText(roomName);

        /*
        // 위치 정보 출력 - 차후 설정된 위치 받아와서 출력되도록 수정해야 함
        MapView mapView = new MapView(this);

        RelativeLayout mapViewContainer = (RelativeLayout) findViewById(R.id.mapView);
        mapViewContainer.addView(mapView);
         */
    }

    public void printFixDataDB(){
        ContentValues values = new ContentValues();
        values.put("roomKey",roomKey);
        GetFixDataTask gfdt = new GetFixDataTask(GetFixDataUrl,values);
        gfdt.execute();
    }

    class GetFixDataTask extends AsyncTask<Void, Void, String> {
        private String url;
        private ContentValues values;
        String result;
        String _text;

        public GetFixDataTask(String url, ContentValues values){
            this.url = url;
            this.values = values;
        }

        @Override
        protected String doInBackground(Void... params) {
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            result = requestHttpURLConnection.request(url, values);
            return result;
        }

        @Override
        protected void onPostExecute(String s){
            super.onPostExecute(s);
            doJSONParser(s);
            printText = _text;
            destinationTv.setText(destination);
            participantTv.setText(printNick);
            timeTv.setText(time);
        }

        public void doJSONParser(String string){
            try{
                String result = "";

                JSONObject jsonObject = new JSONObject(string);
                JSONArray jsonArray = jsonObject.getJSONArray("Appointment");
                Log.d("JSON",jsonArray.toString());

                for(int i = 0;i<jsonArray.length();i++) {
                    JSONObject output = jsonArray.getJSONObject(i);
                    time = output.getString("time");
                    destination = output.getString("dest");
                    String lati = output.getString("latitude");
                    destLat = Float.parseFloat(lati);
                    String longi = output.getString("longitude");
                    destLong = Float.parseFloat(longi);
                    int num = Integer.parseInt(output.getString("num"));
                    JSONArray nick = output.getJSONArray("nickName");
                    for (int j = 0; j < num; j++) {
                        if(j!=0){
                            printNick += ", ";
                        }
                        JSONObject nickname = nick.getJSONObject(j);
                        participant.add(nickname.getString("nickName"));
                        printNick += participant.get(j);
                    }

                    result += time
                            +" / "
                            +destination
                            +" / "
                            +Float.toString(destLat)
                            +" / "
                            +Float.toString(destLong)
                            +" / "
                            +Integer.toString(num)
                            +" / "
                            +printNick
                            +"\n";
                    Log.d("text",result);
                    _text = result;
                }
            }catch (JSONException e){
                e.printStackTrace();
            }

        }

    }

    //백버튼 누르면 홈화면으로 이동
    @Override
    public void onBackPressed(){
        backPressHomeHandler.onBackPressed();
        overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
    }

}
