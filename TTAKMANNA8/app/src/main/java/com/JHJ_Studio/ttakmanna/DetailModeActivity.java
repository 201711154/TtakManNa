package com.JHJ_Studio.ttakmanna;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.daum.android.map.MapViewEventListener;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.file.FileAlreadyExistsException;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.logging.Logger;
import android.net.Uri;

import com.JHJ_Studio.ttakmanna.adapter.LocationAdapter;
import com.JHJ_Studio.ttakmanna.api.ApiClient;
import com.JHJ_Studio.ttakmanna.api.ApiInterface;
import com.JHJ_Studio.ttakmanna.model.category_search.CategoryResult;
import com.JHJ_Studio.ttakmanna.model.category_search.Document;
import com.JHJ_Studio.ttakmanna.R;
import com.JHJ_Studio.ttakmanna.adapter.LocationAdapter;
import com.JHJ_Studio.ttakmanna.api.ApiClient;
import com.JHJ_Studio.ttakmanna.api.ApiInterface;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//새 일정 - 세부사항화면
public class DetailModeActivity extends AppCompatActivity implements MapView.MapViewEventListener, MapView.POIItemEventListener, MapView.OpenAPIKeyAuthenticationResultListener, View.OnClickListener, MapView.CurrentLocationEventListener {

    public static final int REQUEST_CODE = 1001;
    private long backKeyPressedTime = 0;

    Button b1;
    TextView titleTxt;
    private RadioGroup checkModeGroup; // 불가능한 날짜인지 가능한 날짜인지
    private CheckBox[] dayOfWeek = new CheckBox[7]; // 요일 박스
    private int[] week = {R.id.monday, R.id.tuesday, R.id.wednesday, R.id.thursday, R.id.friday, R.id.saturday, R.id.sunday};

    // 지도 관련
    EditText address;
    MapView mapView;
    ViewGroup mapViewContainer;
    RecyclerView recyclerView;
    Button address_search_button;
    ArrayList<Document> documentArrayList = new ArrayList<>(); //지역명 검색 결과 리스트

    //DB관련
    String insertRoomDataUrl = "http://ttakmanna.com/Android/insertRoomData.php";
    Room room = new Room();
    String nickName;
    float latitude;
    float longitude;
    int startTime;
    int endTime;



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

    // 데이터가 전부 제대로 입력되었는지 검증하는 용
    boolean didParticipationNameInput = false;
    boolean didPosInput = false;
    boolean didDateSet = false;


   @Override
    protected void onCreate(Bundle savedInstanceState) {
        for (boolean datecheck : is_date_ok) datecheck = false; // 초기화
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_mode);
        room = (Room) getIntent().getSerializableExtra("roomData");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // 입력 정보
        EditText name = (EditText)findViewById(R.id.nickname);
        recyclerView = findViewById(R.id.map_recyclerview);
        titleTxt = (TextView)findViewById(R.id.groupName2);
        titleTxt.setText(room.getRoomName());

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


        // map
       //mapSet();


       /*
       // 주소 검색 버튼을 누르면 주소를 기반으로 geocoding 후
       // 주어진 좌표들 중 select 하고 해당 좌표로 맵 이동
       address.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               List<Address> pos_searched; // 좌표들을 집어넣을 리스트
               final Address pos_confirmed; // 그 중 골라진 좌표

               // geocording
               Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
               String address_str = address.toString();
               try {
                   pos_searched = geocoder.getFromLocationName(address_str, 15);
               } catch (IOException e) {
                   // 결과물을 찾지 못했을 경우 메세지 띄움
                   Toast.makeText(getApplicationContext(), "주소 검색 결과가 존재하지 않습니다.", Toast.LENGTH_LONG);
                   // e.printStackTrace();
                   // 이벤트 종료
                   return;
               }

               // 역 지오코딩으로 얻은 좌표 한글로 변환
               for (int i = 0; i < pos_searched.size(); i++){
                   pos_searched.get(i).
               }
               final CharSequence[] poss = {"여기에 한글 변환한 좌표 넣어야 하는데 시간이 부족"};

               // 팝업창에 좌표 리스트 선택하게 함
               // 참고: https://mixup.tistory.com/36
               AlertDialog.Builder select_pos_popup = new AlertDialog.Builder(DetailModeActivity.this,
                       android.R.style.Theme_DeviceDefault_Light_Dialog_Alert); //context 수정했음 확인 필요

               select_pos_popup.setTitle("원하는 위치를 선택하세요")
                       .setItems(poss, new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               Toast.makeText(getApplicationContext(),
                                       poss[which], Toast.LENGTH_LONG).show();
                           }
                       })
                       .setCancelable(false)
                       .show();

           }


       });*/


        // 시간 정보 받아옴
        time_from = (TimePicker) findViewById(R.id.time_from);
        time_to = (TimePicker) findViewById(R.id.time_to);


        //모두 참여했는지 안했는지에 따라서 다음 화면이 달라지는 버튼, i로 일단 연결해 놓았음
        b1 = (Button)findViewById(R.id.goCompleteButton);

        b1.setOnClickListener(new View.OnClickListener() {

            int i = 0;

            @Override
            public void onClick(View v) {
                insertRoomDataDB();
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

    public void insertRoomDataDB() {
        Random rnd = new Random();
        room.setRoomKey(rnd.nextInt(39999));
        String roomKey = Integer.toString(room.getRoomKey());
        String roomName = room.getRoomName();
        String closed = Integer.toString(room.getClosed());
        String mode = Integer.toString(room.getMode());
        String number = Integer.toString(room.getNumber());
        String purpose = Integer.toString(room.getPurpose());

        InsertRoomDataTask irdt = new InsertRoomDataTask();
        irdt.execute(roomKey, roomName, closed, mode, number, purpose);
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
            Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
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

                String data = URLEncoder.encode("roomKey","UTF-8") + "=" + roomKey;
                data += "&" + URLEncoder.encode("roomName","UTF-8") + "=" + URLEncoder.encode(roomName,"UTF-8");
                data += "&" + URLEncoder.encode("closed","UTF-8") + "=" + closed;
                data += "&" + URLEncoder.encode("mode","UTF-8") + "=" + mode;
                data += "&" + URLEncoder.encode("number","UTF-8") + "=" + number;
                data += "&" + URLEncoder.encode("purpose","UTF-8") + "=" + purpose;

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
        final LocationAdapter locationAdapter = new LocationAdapter(documentArrayList, getApplicationContext(), address, recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false); //레이아웃매니저 생성
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL)); //아래구분선 세팅
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(locationAdapter);

        // 맵 리스너
        mapView.setMapViewEventListener(this);
        mapView.setPOIItemEventListener(this);
        mapView.setOpenAPIKeyAuthenticationResultListener(this);

        // 주소 검색
        address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 입력 이전
                recyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                /*
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
                        }

                        @Override
                        public void onFailure(@NotNull Call<CategoryResult> call, @NotNull Throwable t) {

                        }
                    });
                } else {
                    if (s.length() <= 0){
                        recyclerView.setVisibility(View.GONE);
                    }
                }

                 */
            }



            @Override
            public void afterTextChanged(Editable s) {

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
