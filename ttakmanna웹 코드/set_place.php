
<!DOCTYPE html>
<html>
<?php
session_start();

header("Content-Type:text/html; charset=UTF-8");

$roomkey=NULL;
if(isset($_SESSION['roomkey'])){
 $roomkey=$_SESSION['roomkey'];
}

$lat_Array=array();
$lng_Array=array();

//db 연결
 $mysql_host = '127.0.0.1';
 $mysql_user = 'ttakmanna';
 $mysql_password = 'whfdjq1gkwk^';
 $mysql_db = 'dbttakmanna';

 $conn = mysqli_connect($mysql_host, $mysql_user, $mysql_password, $mysql_db);

 if(mysqli_connect_errno()){
   die("db연결실패".mysqli_connect_error()); }

 mysqli_query($conn, "set names utf8");

//키값이 같다면 각 변수에 데이터삽입
 $sqlroom ="SELECT * FROM RoomData WHERE roomKey='$roomkey'";
 $result_room=mysqli_query($conn,$sqlroom);

 $b=mysqli_fetch_assoc($result_room);
 $number=$b['number'];
 $mode=$b['mode'];
 $purpose=$b['purpose'];

 $sqlinfo="SELECT * FROM InfoData WHERE roomKey='$roomkey'";
 $result_info=mysqli_query($conn,$sqlinfo);
 while($b1=mysqli_fetch_assoc($result_info)){
   $lat_Array[]=$b1['latitude'];
   $lng_Array[]=$b1['longitue'];
 }
 // 입력받은 경도,위도의 중간값구하기
 if ($mode == 2){
   $dest1=NULL;
   $dest1_lat=NULL;
   $dest1_long=NULL;
   $dest2=NULL;
   $dest2_lat=NULL;
   $dest2_long=NULL;
   $dest3=NULL;
   $dest3_lat=NULL;
   $dest3_long=NULL;
   $dest4=NULL;
   $dest4_lat=NULL;
   $dest4_long=NULL;
   $dest5=NULL;
   $dest5_lat=NULL;
   $dest5_long=NULL;
 }else{
   $sum1 = array_sum($lat_Array);
   $lat = $sum / $number;
   $sum2 = array_sum($lng_Array);
   $lng = $sum / $number;

   //purpose값에 따른 장소
   if($purpose==1){
     $placeSet="FD6";
   }elseif($purpose==2){
     $placeSet="CE7";

 }
?>
<body>

  <div id="map" style="width:100%;height:350px;"></div>

  <script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=a0cf5c8645e6c490b6119ea3e6dd4805&libraries=services"></script>
  <script>
  var latt = "<?php echo $lat; ?>"
  var lngg = "<?php echo $lng; ?>"
  var placeset= "<?php echo $placeSet; ?>"

  var mapContainer = document.getElementById('map'), // 지도를 표시할 div
      mapOption = {
          center: new kakao.maps.LatLng(37.566826, 126.9786567), // 지도의 중심좌표
          level: 2 // 지도의 확대 레벨
      };

  // 지도를 생성합니다
  var map = new kakao.maps.Map(mapContainer, mapOption);

  // 장소 검색 객체를 생성합니다
  var ps = new kakao.maps.services.Places(map);

  // 카테고리 검색합니다
  ps.categorySearch(placeSet, placesSearchCB, {
      location:new kakao.maps.LatLng(latt, lngg), // 해당 지점의
      radius:100, //100미터 범위 내에서
      sort: kakao.maps.services.SortBy.distance //거리순으로 정렬
  });

  // 키워드 검색 완료 시 호출되는 콜백함수 입니다
  function placesSearchCB (data, status, pagination) {
      if (status === kakao.maps.services.Status.OK) {
          for (var i=0; i<data.length; i++) {
              displayMarker(data[i]);
          }
          //반복문을 돌리면 자꾸 실행오류가 남 하나하나 입력해줌
          var result1 = data[0];
          document.getElementById('rn1').value= result1.place_name;
          document.getElementById('lt1').value= result1.y;
          document.getElementById('ln1').value= result1.x;
          var result2 = data[1];
          document.getElementById('rn2').value= result2.place_name;
          document.getElementById('lt2').value= result2.y;
          document.getElementById('ln2').value= result2.x;
          var result3 = data[2];
          document.getElementById('rn3').value= result3.place_name;
          document.getElementById('lt3').value= result3.y;
          document.getElementById('ln3').value= result3.x;
          var result4 = data[3];
          document.getElementById('rn4').value= result4.place_name;
          document.getElementById('lt4').value= result4.y;
          document.getElementById('ln4').value= result4.x;
          var result5 = data[4];
          document.getElementById('rn5').value= result5.place_name;
          document.getElementById('lt5').value= result5.y;
          document.getElementById('ln5').value= result5.x;

      }
  }
  var arr = new Array();
  // 지도에 마커를 표시하는 함수입니다
  function displayMarker(place) {
      // 마커를 생성하고 지도에 표시합니다
      var marker = new kakao.maps.Marker({
          map: map,
          position: new kakao.maps.LatLng(place.y, place.x)
      });

  }

  </script>
  <div>
      <input type = "text" id="rn1" value=""/>
      <input type = "text" id="rn2" value=""/>
      <input type = "text" id="rn3" value=""/>
      <input type = "text" id="rn4" value=""/>
      <input type = "text" id="rn5" value=""/>

      <input type = "text" id="lt1" value=""/>
      <input type = "text" id="lt2" value=""/>
      <input type = "text" id="lt3" value=""/>
      <input type = "text" id="lt4" value=""/>
      <input type = "text" id="lt5" value=""/>

      <input type = "text" id="ln1" value=""/>
      <input type = "text" id="ln2" value=""/>
      <input type = "text" id="ln3" value=""/>
      <input type = "text" id="ln4" value=""/>
      <input type = "text" id="ln5" value=""/>

  </div>

  </form>

  <script>
      this.document.getElementById("setplaceform").submit();
  </script>
  <form id=setplaceform name="setplaceform" method="post" action="http://ttakmanna.com/">

</body>
</html>
