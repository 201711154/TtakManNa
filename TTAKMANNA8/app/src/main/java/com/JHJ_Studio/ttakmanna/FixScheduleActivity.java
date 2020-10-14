package com.JHJ_Studio.ttakmanna;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

//일정 결정 화면
public class FixScheduleActivity extends AppCompatActivity {
    String UpdateDataUrl = "http://ttakmanna.com/Android/updateData.php";
    private long backKeyPressedTime = 0;
    public static final int REQUEST_CODE = 1001;
    Button b1;

    int roomKey;
    int selDest = 1;
    int selTime = 1;

    ArrayList<Room> rooms = new ArrayList<>();
    String roomName;
    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fix_schedule);

        rooms = (ArrayList<Room>) getIntent().getSerializableExtra("Room");
        roomName = getIntent().getStringExtra("RoomName");
        pos = getIntent().getIntExtra("Pos",0);
        roomKey = rooms.get(pos).getRoomKey();
        Log.d("RoomKey",Integer.toString(roomKey));


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //결과 출력 화면으로 이동
        b1 = (Button)findViewById(R.id.goPrintSchedule);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
                Intent intent = new Intent(getBaseContext(),PrintScheduleActivity.class);
                intent.putExtra("roomKey",roomKey);
                startActivityForResult(intent,REQUEST_CODE);
                overridePendingTransition(R.anim.enter,R.anim.exit);
            }
        });

    }

    public void updateData(){
        String rK = Integer.toString(roomKey);
        String sD = Integer.toString(selDest);
        String sT = Integer.toString(selTime);

        UpdateDataTask udt = new UpdateDataTask();
        udt.execute(rK,sD,sT);
    }

    class UpdateDataTask extends AsyncTask<String, Void, String> {
        ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(FixScheduleActivity.this, "PleaseWait", null, true, true);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            loading.dismiss();
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                int roomKey = Integer.parseInt(params[0]);
                int selDest = Integer.parseInt(params[1]);
                int selTime = Integer.parseInt(params[2]);

                String data = URLEncoder.encode("roomKey", "UTF-8") + "=" + roomKey;
                data += "&" + URLEncoder.encode("sel_dest", "UTF-8") + "=" + selDest;
                data += "&" + URLEncoder.encode("sel_time", "UTF-8") + "=" + selTime;
                data += "&" + URLEncoder.encode("selected", "UTF-8") + "=" + 1;

                URL url = new URL(UpdateDataUrl);
                URLConnection conn = url.openConnection();

                conn.setDoOutput(true);
                OutputStreamWriter os = new OutputStreamWriter(conn.getOutputStream());
                os.write(data);
                os.flush();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                    break;
                }
                Log.d("tag : ", sb.toString());
                return sb.toString();
            } catch (Exception e) {
                return new String("Exception : " + e.getMessage());
            }
        }
    }

    //두번 눌러 뒤로가기
    @Override
    public void onBackPressed(){

        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(FixScheduleActivity.this, "한번 더 누르시면 이전화면으로 돌아갑니다.", Toast.LENGTH_SHORT).show();

        }
        else if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            super.onBackPressed();
            overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
        }
    }
}
