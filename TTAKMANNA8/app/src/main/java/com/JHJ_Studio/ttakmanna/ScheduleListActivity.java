package com.JHJ_Studio.ttakmanna;

import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

//기존 일정 불러오기 화면
public class ScheduleListActivity extends AppCompatActivity {

    private BackPressHomeHandler backPressHomeHandler;

    public static final int REQUEST_CODE = 1001;
    String getRoomDataUrl = "http://ttakmanna.com/Android/getRoomData.php";

    Button b1,b2,b3;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;

    ArrayList<Room> room = new ArrayList<>();
    int roomNum;
    String[] names;
    String[] texts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_list);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        backPressHomeHandler = new BackPressHomeHandler(this);

        recyclerView = findViewById(R.id.recycler);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        GetRoomDataTask grdt = new GetRoomDataTask(getRoomDataUrl,null);
        grdt.execute();



       /* b1 = (Button)findViewById(R.id.previousGroup1);
        b2 = (Button)findViewById(R.id.previousGroup2);
        b3 = (Button)findViewById(R.id.previousGroup3);
        //해당 하는 일정 출력 화면으로 이동
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),PrintScheduleActivity.class);
                startActivityForResult(intent,REQUEST_CODE);

                overridePendingTransition(R.anim.enter,R.anim.exit);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),PrintScheduleActivity.class);
                startActivityForResult(intent,REQUEST_CODE);

                overridePendingTransition(R.anim.enter,R.anim.exit);
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),PrintScheduleActivity.class);
                startActivityForResult(intent,REQUEST_CODE);

                overridePendingTransition(R.anim.enter,R.anim.exit);
            }
        });*/

    }

    class GetRoomDataTask extends AsyncTask<Void, Void, String> {
        private String url;
        private ContentValues values;
        String result;
        private ArrayList<Room> _data = new ArrayList<>();

        public GetRoomDataTask(String url, ContentValues values){
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
            room = _data;
            getStrings();
            adapter = new RecyclerAdapter(names, texts, room);
            recyclerView.setAdapter(adapter);
        }

        public void doJSONParser(String string){
            try{
                String result = "";

                JSONObject jsonObject = new JSONObject(string);
                JSONArray jsonArray = jsonObject.getJSONArray("RoomData");

                for(int i = 0;i<jsonArray.length();i++){
                    Room room = new Room();
                    JSONObject output = jsonArray.getJSONObject(i);
                    room.setRoomKey(output.getInt("roomKey"));
                    room.setRoomName(output.getString("roomName"));
                    room.setClosed(output.getInt("closed"));
                    room.setMode(output.getInt("mode"));
                    room.setNumber(output.getInt("number"));
                    room.setPurpose(output.getInt("purpose"));
                    room.setSelected(output.getInt("selected"));

                    _data.add(room);
                    result += room.getRoomKey()
                            +" / "
                            +room.getRoomName()
                            +" / "
                            +room.getClosed()
                            +" / "
                            +room.getMode()
                            +" / "
                            +room.getNumber()
                            +" / "
                            +room.getPurpose()
                            +" / "
                            +room.getSelected()
                            +"\n";
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }

    }

    public void getStrings(){
        roomNum = room.size();
        names = new String[roomNum];
        texts = new String[roomNum];
        for(int i=0;i<roomNum;i++){
            names[i] = room.get(i).getRoomName();
            Log.d("name",names[i]);
            if(room.get(i).getSelected() == 0){
                if(room.get(i).getClosed() == 0){
                    texts[i] = "입력 중";
                }else if(room.get(i).getClosed() == 1){
                    texts[i] = "선택 중";
                }else{
                    texts[i] = "선택안됨 + 오류?";
                }
            }else if(room.get(i).getSelected() == 1){
                texts[i] = "선택 완료";
            }else{
                texts[i] = "오류?";
            }
            Log.d("select",texts[i]);
        }
    }

    //뒤로가기 두번눌러 홈화면으로
    @Override
    public void onBackPressed(){
        backPressHomeHandler.onBackPressed();
        overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
    }

}
