package com.JHJ_Studio.ttakmanna;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Random;

import com.JHJ_Studio.ttakmanna.adapter.LocationAdapter;
import com.JHJ_Studio.ttakmanna.api.ApiClient;
import com.JHJ_Studio.ttakmanna.api.ApiInterface;
import com.JHJ_Studio.ttakmanna.model.category_search.CategoryResult;
import com.JHJ_Studio.ttakmanna.model.category_search.Document;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.jetbrains.annotations.NotNull;

import kotlin.text.StringsKt;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//새 일정 - 세부사항화면
public class DetailModeActivity extends AppCompatActivity implements MapView.MapViewEventListener, MapView.POIItemEventListener, MapView.OpenAPIKeyAuthenticationResultListener, View.OnClickListener, MapView.CurrentLocationEventListener {

    public static final int REQUEST_CODE = 1001;
    private long backKeyPressedTime = 0;

    // view
    Button completeBtn;
    TextView titleTxt;
    EditText name;
    private RadioGroup checkModeGroup; // 불가능한 날짜인지 가능한 날짜인지
    private CheckBox[] dayOfWeek = new CheckBox[7]; // 요일 박스
    private int[] week = {R.id.monday, R.id.tuesday, R.id.wednesday, R.id.thursday, R.id.friday, R.id.saturday, R.id.sunday};

    // 지도 관련
    EditText address;
    MapView mapView;
    ViewGroup mapViewContainer;
    RelativeLayout loaderLayout;
    RecyclerView recyclerView;
    ArrayList<Document> documentArrayList = new ArrayList<>(); //지역명 검색 결과 리스트
    Document selectedLocation;

    //DB관련
    String insertRoomDataUrl = "http://ttakmanna.com/Android/insertRoomData.php";
    String checkRoomKeyUrl = "http://ttakmanna.com/Android/checkRoomKey.php";
    String insertInfoDataUrl = "http://ttakmanna.com/Android/insertInfoData.php";
    Room room = new Room();
    int check;

    //사용자 닉네임
    String nickName = null;
    //사용자 위도
    float latitude = 37.3571f;
    //사용자 경도
    float longitude = 126.926f;
    //시작시간
    int startHour;
    int startMin;
    String startText;
    //종료시간
    int endHour;
    int endMin;
    String endText;
    String nullTxt = "00:00:00";

    // 얻은 데이터
    private boolean is_it_possible_day = false; // 가능한 요일인지 불가능한 요일인지 확인
    private boolean[] dates = new boolean[7]; // 요일 확인
    private CheckBox[] datesBox = new CheckBox[7];
    private TimePicker time_from, time_to; // 시간

    // 데이터가 전부 제대로 입력되었는지 검증하는 용
    boolean didParticipationNameInput = false;
    boolean didPosInput = false;
    boolean didDateSet = false;


   @Override
    protected void onCreate(Bundle savedInstanceState) {
        //for (boolean datecheck : is_date_ok) datecheck = false; // 초기화
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_detail_mode);
        room = (Room) getIntent().getSerializableExtra("roomData");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // 입력 정보
        name = (EditText)findViewById(R.id.nickname);
        loaderLayout = findViewById(R.id.loaderLayout);
        recyclerView = findViewById(R.id.map_recyclerview);
        titleTxt = (TextView)findViewById(R.id.groupName2);
        titleTxt.setText(room.getRoomName());

        for (int i=0;i<7;i++){
            datesBox[i] = (CheckBox) findViewById(week[i]);
        }

        // map
        //mapSet();

        // 시간 정보 받아옴
        time_from = (TimePicker) findViewById(R.id.time_from);
        time_to = (TimePicker) findViewById(R.id.time_to);

        completeBtn = (Button)findViewById(R.id.goCompleteButton);
        completeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkNull()) {
                    if (insertRoomDataDB()) {
                        insertInfoDataDB();
                        Intent intent = new Intent(getBaseContext(), ParticipationCheckActivity.class);
                        intent.putExtra("room",room);
                        intent.putExtra("roomKey", room.getRoomKey());
                        startActivityForResult(intent, REQUEST_CODE);
                    }
                }
            }
        });
    }

    public boolean checkNull(){
       getData();
       return !(nickName == null || nickName.trim().isEmpty());
    }

    public void insertInfoDataDB(){
        String roomKeyTxt = Integer.toString(room.getRoomKey());
        String latTxt = Float.toString(latitude);
        String longTxt = Float.toString(longitude);
        String date1 = Boolean.toString(dates[0]);
        String date2 = Boolean.toString(dates[1]);
        String date3 = Boolean.toString(dates[2]);
        String date4 = Boolean.toString(dates[3]);
        String date5 = Boolean.toString(dates[4]);
        String date6 = Boolean.toString(dates[5]);
        String date7 = Boolean.toString(dates[6]);

        InsertInfoDataTask iidt = new InsertInfoDataTask();
        iidt.execute(roomKeyTxt, nickName, latTxt,longTxt,date1,date2,date3,date4,date5,date6,date7);
    }

    class InsertInfoDataTask extends AsyncTask<String, Void, String> {
        ProgressDialog loading;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            loading = ProgressDialog.show(DetailModeActivity.this, "PleaseWait",null,true,true);
        }

        @Override
        protected void onPostExecute(String s){
            super.onPostExecute(s);
            loading.dismiss();
            Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
        }
        @Override
        protected String doInBackground(String... params) {
            try{
                int roomKey =  Integer.parseInt(params[0]);
                String nickName = (String) params[1];
                float latitude = Float.parseFloat(params[2]);
                float longitude = Float.parseFloat(params[3]);
                boolean dates[] = new boolean[7];
                dates[0] = Boolean.parseBoolean(params[4]);
                dates[1] = Boolean.parseBoolean(params[5]);
                dates[2] = Boolean.parseBoolean(params[6]);
                dates[3] = Boolean.parseBoolean(params[7]);
                dates[4] = Boolean.parseBoolean(params[8]);
                dates[5] = Boolean.parseBoolean(params[9]);
                dates[6] = Boolean.parseBoolean(params[10]);

                String data = URLEncoder.encode("roomKey","UTF-8") + "=" + roomKey;
                data += "&" + URLEncoder.encode("nickName","UTF-8") + "=" + URLEncoder.encode(nickName,"UTF-8");
                data += "&" + URLEncoder.encode("latitude","UTF-8") + "=" + latitude;
                data += "&" + URLEncoder.encode("longitude","UTF-8") + "=" + longitude;
                data += "&" + URLEncoder.encode("mon_s","UTF-8") + "=" + URLEncoder.encode(setStart(dates[0]),"UTF-8");
                data += "&" + URLEncoder.encode("mon_e","UTF-8") + "=" + URLEncoder.encode(setEnd(dates[0]),"UTF-8");
                data += "&" + URLEncoder.encode("tue_s","UTF-8") + "=" + URLEncoder.encode(setStart(dates[1]),"UTF-8");
                data += "&" + URLEncoder.encode("tue_e","UTF-8") + "=" + URLEncoder.encode(setEnd(dates[1]),"UTF-8");
                data += "&" + URLEncoder.encode("wed_s","UTF-8") + "=" + URLEncoder.encode(setStart(dates[2]),"UTF-8");
                data += "&" + URLEncoder.encode("wed_e","UTF-8") + "=" + URLEncoder.encode(setEnd(dates[2]),"UTF-8");
                data += "&" + URLEncoder.encode("thu_s","UTF-8") + "=" + URLEncoder.encode(setStart(dates[3]),"UTF-8");
                data += "&" + URLEncoder.encode("thu_e","UTF-8") + "=" + URLEncoder.encode(setEnd(dates[3]),"UTF-8");
                data += "&" + URLEncoder.encode("fri_s","UTF-8") + "=" + URLEncoder.encode(setStart(dates[4]),"UTF-8");
                data += "&" + URLEncoder.encode("fri_e","UTF-8") + "=" + URLEncoder.encode(setEnd(dates[4]),"UTF-8");
                data += "&" + URLEncoder.encode("sat_s","UTF-8") + "=" + URLEncoder.encode(setStart(dates[5]),"UTF-8");
                data += "&" + URLEncoder.encode("sat_e","UTF-8") + "=" + URLEncoder.encode(setEnd(dates[5]),"UTF-8");
                data += "&" + URLEncoder.encode("sun_s","UTF-8") + "=" + URLEncoder.encode(setStart(dates[6]),"UTF-8");
                data += "&" + URLEncoder.encode("sun_e","UTF-8") + "=" + URLEncoder.encode(setEnd(dates[6]),"UTF-8");

                URL url = new URL(insertInfoDataUrl);
                URLConnection conn = url.openConnection();

                conn.setDoOutput(true);
                OutputStreamWriter os = new OutputStreamWriter(conn.getOutputStream());
                os.write(data);
                os.flush();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line = reader.readLine()) != null){
                    sb.append(line);
                    break;
                }
                Log.d("tag : ", sb.toString());
                return sb.toString();
            }catch(Exception e){
                return new String("Exception : "+e.getMessage());
            }
        }
    }

    public String setStart(boolean tf){
        if(tf){return startText;}
        else{return nullTxt;}
    }

    public String setEnd(boolean tf){
        if(tf){return endText;}
        else{return nullTxt;}
    }

    public void getData(){
        nickName = name.getText().toString();
        //latitude = (float)Float.parseFloat(latTxt.getText().toString());
        //longitude = (float)Float.parseFloat(longTxt.getText().toString());
        if(Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            startHour = time_from.getHour();
            endHour = time_to.getHour();
            startMin = time_from.getMinute();
            endMin = time_to.getMinute();
        }else{
            startHour = time_from.getCurrentHour();
            endHour = time_to.getCurrentHour();
            startHour = time_from.getCurrentMinute();
            endHour = time_to.getCurrentMinute();
        }
        startText = String.format("%02d:%02d:00",startHour,startMin);
        endText = String.format("%02d:%02d:00",endHour,endMin);

        for(int i=0;i<7;i++){
            if(datesBox[i].isChecked()){
                dates[i] = true;
            }else{
                dates[i] = false;
            }
        }
    }

    public boolean insertRoomDataDB() {
        Random rnd = new Random();
        room.setRoomKey(rnd.nextInt(100000));
        ContentValues value = new ContentValues();
        value.put("roomKey", room.getRoomKey());

        CheckRoomKeyTask crt = new CheckRoomKeyTask(value, room.getRoomKey());
        crt.execute();

        String roomKey = Integer.toString(room.getRoomKey());
        String roomName = room.getRoomName();
        String closed = Integer.toString(room.getClosed());
        String mode = Integer.toString(room.getMode());
        String number = Integer.toString(room.getNumber());
        String purpose = Integer.toString(room.getPurpose());
        String selected = Integer.toString(room.getSelected());

        InsertRoomDataTask irdt = new InsertRoomDataTask();
        if(check == 1){irdt.execute(roomKey, roomName, closed, mode, number, purpose, selected); return true;}
        else{return false;}
    }

    class CheckRoomKeyTask extends AsyncTask<Void, Void, String>{
        String result;
        int roomKey;
        int _check = -1;
        ContentValues values;

        CheckRoomKeyTask(ContentValues values, int roomKey){
            this.values = values;
            this.roomKey = roomKey;
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            result = requestHttpURLConnection.request(checkRoomKeyUrl, values);
            return result;
        }


        @Override
        protected void onPostExecute(String s){
            super.onPostExecute(s);
            getCheck(s);
            check = _check;
        }
        public void getCheck(String s){
            if(s.equals("1")){_check = 0;}
            else if (s.equals("0")){_check = 1;}
            else {_check = -1;}
            Log.d("check",Integer.toString(_check));

        }
    }

    class InsertRoomDataTask extends AsyncTask<String, Void, String> {
        ProgressDialog loading;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            loading = ProgressDialog.show(DetailModeActivity.this, "PleaseWait",null,true,true);
        }

        @Override
        protected void onPostExecute(String s){
            super.onPostExecute(s);
            loading.dismiss();
            //Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
        }
        @Override
        protected String doInBackground(String... params) {
            try{
                int roomKey =  Integer.parseInt(params[0]);
                String roomName = (String) params[1];
                int closed = Integer.parseInt(params[2]);
                int mode = Integer.parseInt(params[3]);
                int number = Integer.parseInt(params[4]);
                int purpose = Integer.parseInt(params[5]);
                int selected = Integer.parseInt(params[6]);

                String data = URLEncoder.encode("roomKey","UTF-8") + "=" + roomKey;
                data += "&" + URLEncoder.encode("roomName","UTF-8") + "=" + URLEncoder.encode(roomName,"UTF-8");
                data += "&" + URLEncoder.encode("closed","UTF-8") + "=" + closed;
                data += "&" + URLEncoder.encode("mode","UTF-8") + "=" + mode;
                data += "&" + URLEncoder.encode("number","UTF-8") + "=" + number;
                data += "&" + URLEncoder.encode("purpose","UTF-8") + "=" + purpose;
                data += "&" + URLEncoder.encode("selected", "UTF-8") + "=" + selected;

                URL url = new URL(insertRoomDataUrl);
                URLConnection conn = url.openConnection();

                conn.setDoOutput(true);
                OutputStreamWriter os = new OutputStreamWriter(conn.getOutputStream());
                os.write(data);
                os.flush();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line = reader.readLine()) != null){
                    sb.append(line);
                    break;
                }
                Log.d("tag : ", sb.toString());
                return sb.toString();
            }catch(Exception e){
                return new String("Exception : "+e.getMessage());
            }
        }
    }

    // 검색 & 맵 뷰 구현
    private void mapSet(){

        // 맵 뷰 띄우기
        mapView = new MapView(this);
        mapViewContainer = findViewById(R.id.mapView);
        mapViewContainer.addView(mapView);

        // 주소
        address = (EditText) findViewById(R.id.address);
        LocationAdapter locationAdapter = new LocationAdapter(documentArrayList, getApplicationContext(), address, recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false); //레이아웃매니저 생성
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL)); //아래구분선 세팅
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(locationAdapter);

        // 맵 리스너
        mapView.setMapViewEventListener(this);
        mapView.setPOIItemEventListener(this);
        mapView.setOpenAPIKeyAuthenticationResultListener(this);

        mapView.setCurrentLocationEventListener(this);
        loaderLayout.setVisibility(View.VISIBLE);

        // 주소 검색
        address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 입력 이전
                recyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 1){
                    documentArrayList.clear();
                    locationAdapter.clear();
                    locationAdapter.notifyDataSetChanged();
                    com.JHJ_Studio.ttakmanna.api.ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                    Call<CategoryResult> call = apiInterface.getSearchLocation(getString(R.string.restapi_key), s.toString(), 15);
                    call.enqueue(new Callback<CategoryResult>() {
                        @Override
                        public void onResponse(@NotNull Call<CategoryResult> call, @NotNull Response<CategoryResult> response) {
                            if (response.isSuccessful()) {
                                assert response.body() != null;
                                for (Document document : response.body().getDocuments()) {
                                    locationAdapter.addItem(document);
                                }
                                locationAdapter.notifyDataSetChanged();
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "주소 검색 요청 실패", Toast.LENGTH_LONG);
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call<CategoryResult> call, @NotNull Throwable t) {
                            Toast.makeText(getApplicationContext(), "지도 관련 문제 생김", Toast.LENGTH_LONG);
                        }
                    });
                } else {
                    if (s.length() <= 0) {
                        recyclerView.setVisibility(View.GONE);
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                // 입력이 끝났을 때
            }
        });

        address.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                } else {
                    recyclerView.setVisibility(View.GONE);
                }
            }
        });
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FancyToast.makeText(getApplicationContext(), "검색리스트에서 장소를 선택해주세요", FancyToast.LENGTH_SHORT, FancyToast.INFO, true).show();
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        //if (id == R.id.fab_detail){
            // 장소 선택
            //selectedLocation = locationAdaptor;
        //}
    }

    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float v) {

    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {

    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {

    }

    @Override
    public void onMapViewInitialized(MapView mapView) {

    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onDaumMapOpenAPIKeyAuthenticationResult(MapView mapView, int i, String s) {

    }

    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

    }
}
