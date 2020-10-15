package com.JHJ_Studio.ttakmanna;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    String GetAppointmentDataUrl = "http://ttakmanna.com/Android/getAppointmentData.php";

    private long backKeyPressedTime = 0;
    public static final int REQUEST_CODE = 1001;
    Button b1;

    int roomKey;
    int selDest = 1;
    int selTime = 1;

    ArrayList<Room> rooms = new ArrayList<>();
    String roomName;
    int pos;

    ArrayList<Integer> destRadioID = new ArrayList<>();
    ArrayList<Integer> timeRadioID = new ArrayList<>();
    ArrayList<String> dests = new ArrayList<>();
    ArrayList<String> times = new ArrayList<>();
    RadioGroup destRadio;
    RadioGroup timeRadio;

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

        destRadio = (RadioGroup) findViewById(R.id.destRadio);
        timeRadio = (RadioGroup) findViewById(R.id.timeRadio);
        //결과 출력 화면으로 이동
        b1 = (Button)findViewById(R.id.goPrintSchedule);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
                Intent intent = new Intent(getBaseContext(),PrintScheduleActivity.class);
                intent.putExtra("RoomName",roomName);
                intent.putExtra("Pos",pos);
                intent.putExtra("Room",rooms);
                startActivityForResult(intent,REQUEST_CODE);
                overridePendingTransition(R.anim.enter,R.anim.exit);
            }
        });

        destRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int pos = destRadioID.indexOf(i);
                selDest = pos+1;
                Log.d("dest",Integer.toString(selDest));
            }
        });

        timeRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int pos = timeRadioID.indexOf(i);
                selTime = pos+1;
                Log.d("time",Integer.toString(selTime));
            }
        });
        setAppointmentData();

    }

    public void setRadio(){
        for(int i=0;i<dests.size();i++) {
            String temp = dests.get(i);
            if(temp != null && temp != ""){
                RadioButton radioButton = new RadioButton(this);
                radioButton.setText(temp);
                radioButton.setId(View.generateViewId());
                destRadioID.add(radioButton.getId());
                radioButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                final int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,60,getResources().getDisplayMetrics());
                RadioGroup.LayoutParams rprms = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT,height);
                rprms.setMargins(5,5,5,5);;
                destRadio.addView(radioButton,rprms);
            }
        }
        for(int i=0;i<times.size();i++){
            String temp = times.get(i);
            if(!temp.equals("00:00 ~ 00:00") && temp!= null){
                RadioButton radioButton = new RadioButton(this);
                switch (i){
                    case 0:
                        temp = "월요일  " + temp; break;
                    case 1:
                        temp = "화요일  " + temp; break;
                    case 2:
                        temp = "수요일  " + temp; break;
                    case 3:
                        temp = "목요일  " + temp; break;
                    case 4:
                        temp = "금요일  " + temp; break;
                    case 5:
                        temp = "토요일  " + temp; break;
                    case 6:
                        temp = "일요일  " + temp; break;
                }
                radioButton.setText(temp);
                radioButton.setId(View.generateViewId());
                timeRadioID.add(radioButton.getId());
                radioButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                final int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,60,getResources().getDisplayMetrics());
                RadioGroup.LayoutParams rprms = new RadioGroup.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, height);
                rprms.setMargins(5,5,5,5);;
                timeRadio.addView(radioButton,rprms);
            }
        }
    }

    public void setAppointmentData(){
        ContentValues values = new ContentValues();
        values.put("roomKey",roomKey);
        GetAppointmentDataTask gadt = new GetAppointmentDataTask(GetAppointmentDataUrl,values);
        gadt.execute();
    }

    class GetAppointmentDataTask extends AsyncTask<Void, Void, String> {
        private String url;
        private ContentValues values;
        String result;

        public GetAppointmentDataTask(String url, ContentValues values){
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
            setRadio();
        }

        public void doJSONParser(String string){
            try{
                String result = "";

                JSONObject jsonObject = new JSONObject(string);
                JSONArray jsonArray = jsonObject.getJSONArray("RoomData");
                Log.d("JSON",jsonArray.toString());

                for(int i = 0;i<jsonArray.length();i++) {
                    JSONObject output = jsonArray.getJSONObject(i);
                    dests.add(output.getString("dest1"));
                    dests.add(output.getString("dest2"));
                    dests.add(output.getString("dest3"));
                    dests.add(output.getString("dest4"));
                    dests.add(output.getString("dest5"));
                    times.add(output.getString("mon"));
                    times.add(output.getString("tue"));
                    times.add(output.getString("wed"));
                    times.add(output.getString("thu"));
                    times.add(output.getString("fri"));
                    times.add(output.getString("sat"));
                    times.add(output.getString("sun"));

                    Log.d("dests",dests.toString());
                    Log.d("times",times.toString());
                }
            }catch (JSONException e){
                e.printStackTrace();
            }

        }

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
