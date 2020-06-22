package com.JHJ_Studio.ttakmanna;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

class point{
    float x;
    float y;
}

public class KaKaoMap extends Activity {
    point rcp[] = new point[4]; //recommand place point 저장

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MapView mapView = new MapView(this);
        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);

        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(37.5514579595, 126.951949155);
        mapView.setMapCenterPoint(mapPoint, true);
        //true면 앱 실행 시 애니메이션 효과가 나오고 false면 애니메이션이 나오지않음.
        mapViewContainer.addView(mapView);
    }

    //주소 입력 받아서 맵 검색 후 띄우기
    public void KaKaoMap_FindbyAddress(){
        EditText address = (EditText) findViewById(R.id.SerchAddress);
        String pointName = address.toString();

        //새로 띄울 맵 뷰
        MapView mapView = new MapView(this);
        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view2);

        //주소로 장소 찾고 이동 후 마커 표시
        String url = "kakaomap://place?id=7813422";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);

        //map view 활성화
        mapViewContainer.addView(mapView);

    }

    //스케줄 정하는 부분에서 라디오 버튼 선택 시 해당하는 포인트 반환
    //위젯을 변수에 대입
    RadioGroup pointGroup;
    MapPoint mapPoint;
    MapView mapView = new MapView(this);
    //radioGroup ClickListener
    RadioGroup.OnCheckedChangeListener radioGroupButtonChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            switch (pointGroup.getCheckedRadioButtonId()) {
                case R.id.choice_RecommendPlace1:
                    mapPoint = MapPoint.mapPointWithGeoCoord(rcp[0].x, rcp[0].y);
                    mapView.setMapCenterPoint(mapPoint, true);
                    break;
                case R.id.choice_RecommendPlace2:
                    mapPoint = MapPoint.mapPointWithGeoCoord(rcp[1].x, rcp[1].y);
                    mapView.setMapCenterPoint(mapPoint, true);
                    break;
                case R.id.choice_RecommendPlace3:
                    mapPoint = MapPoint.mapPointWithGeoCoord(rcp[2].x, rcp[2].y);
                    mapView.setMapCenterPoint(mapPoint, true);
                    break;
                case R.id.choice_RecommendPlace4:
                    mapPoint = MapPoint.mapPointWithGeoCoord(rcp[3].x, rcp[3].y);
                    mapView.setMapCenterPoint(mapPoint, true);
                    break;
                default:
                    Toast.makeText(getApplicationContext(), "이상하네 이거", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    //정한 장소 출력
    public void KaKaoMap_SetMapByPoint(MapView mapView, float x, float y){
        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);

        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(x, y);
        mapView.setMapCenterPoint(mapPoint, true);
        //true면 앱 실행 시 애니메이션 효과가 나오고 false면 애니메이션이 나오지않음.
        mapViewContainer.addView(mapView);
    }

}
