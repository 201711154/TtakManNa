<?php
   session_start();

   header("Content-Type:text/html; charset=UTF-8");

  //세션에저장된키값받아오기
  $roomkey=NULL;
  if(isset($_SESSION['roomkey'])){
    $roomkey=$_SESSION['roomkey'];
  }

  //입력값변수에저장하기
  if(!empty($_POST['nickname'])) {
    $nickname=$_POST['nickname'];

  }
  if(!empty($_POST['latclick'])) {
    $lat=$_POST['latclick'];

  }else{
    echo "<script>
      alert('장소를 선택해주세요.');
      history.back();
    </script>";
    exit;
  }

  if(!empty($_POST['lngclick'])) {
    $lng=$_POST['lngclick'];

  }

  if(!empty($_POST['days'])) {
    $days = $_POST['days'];

  }else{
    echo "<script>
      alert('가능한 요일에 체크를 해주세요.');
      history.back();
    </script>";
    exit;
  }

  if(!empty($_POST['start_time'])) {
    $start_time=$_POST['start_time'];
  }else{
    echo "<script>
      alert('시간을 선택해주세요.(1)');
      history.back();
    </script>";
    exit;
  }
  if(!empty($_POST['end_time'])) {
    $end_time=$_POST['end_time'];
  }else{
    echo "<script>
      alert('시간을 선택해주세요.(2)');
      history.back();
    </script>";
    exit;
  }

  //days배열에 해당 value가 있으면 1 없으면 null
  if(in_array('mon',$days)){
    $mon=1;
  }else{
      $mon=NULL;
  }
  if(in_array('tue',$days)){
    $tue=1;
  }else{
      $tue=NULL;
  }
  if(in_array('wed',$days)){
    $wed=1;
  }else{
      $wed=NULL;
  }
  if(in_array('thu',$days)){
    $thu=1;
  }else{
      $thu=NULL;
  }
  if(in_array('fri',$days)){
    $fri=1;
  }else{
      $fri=NULL;
  }
  if(in_array('sat',$days)){
    $sat=1;
  }else{
      $sat=NULL;
  }
  if(in_array('sun',$days)){
    $sun=1;
  }else{
      $sun=NULL;
  }

  //요일이 1이면 시간삽입
    if($mon == 1){
      $mon_s=$start_time;
      $mon_e=$end_time;
    }else{
      $mon_s=NULL;
      $mon_e=NULL;
    }
    if($tue== 1){
      $tue_s=$start_time;
      $tue_e=$end_time;
    }else{
      $tue_s=NULL;
      $tue_e=NULL;
    }
    if($wed== 1){
      $wed_s=$start_time;
      $wed_e=$end_time;
    }else{
      $wed_s=NULL;
      $wed_e=NULL;
    }
    if($thu== 1){
      $thu_s=$start_time;
      $thu_e=$end_time;
    }else{
      $thu_s=NULL;
      $thu_e=NULL;
    }
    if($fri== 1){
      $fri_s=$start_time;
      $fri_e=$end_time;
    }else{
      $fri_s=NULL;
      $fri_e=NULL;
    }
    if($sat== 1){
      $sat_s=$start_time;
      $sat_e=$end_time;
    }else{
      $sat_s=NULL;
      $sat_e=NULL;
    }
    if($sun== 1){
      $sun_s=$start_time;
      $sun_e=$end_time;
    }else{
      $sun_s=NULL;
      $sun_e=NULL;
    }

//db연결
  $mysql_user = 'ttakmanna';
  $mysql_password = 'whfdjq1gkwk^';
  $mysql_db = 'dbttakmanna';
  $mysql_host = '127.0.0.1';

  $conn = mysqli_connect($mysql_host, $mysql_user, $mysql_password, $mysql_db);

  if(mysqli_connect_errno()){
	  die("db연결실패".mysqli_connect_error()); }

  mysqli_query($conn, "set names utf8");

/*입력받은닉네임이중복인지확인하기
  $sql="SELECT nickname FROM InfoData WHERE roomKey='$roomkey'";
  $result=mysqli_query($conn,$sql);
  $nick=mysqli_fetch_assoc($result);
  $count1=mysqli_num_rows($result);
  for($k=0;$k<count1;$k= $k+1){
    if(in_array($nickname,$nick))
      {
        echo "
          <script>
            alert('이미 응답하셨습니다.');
            history.back();
          </script>
        ";
        exit;
      }
  }
*/


// 중복이 아니라면 입력받은데이터 db에삽입
    $insert_detail
    ="INSERT INTO InfoData(roomKey,nickname,latitude,longitue,mon_s,mon_e,tue_s,tue_e,wed_s,wed_e,thu_s,thu_e,fri_s,fri_e,sat_s,sat_e,sun_s,sun_e)
    VALUES('$roomkey','$nickname','$lat','$lng','$mon_s','$mon_e','$tue_s','$tue_e','$wed_s','$wed_e','$thu_s','$thu_e','$fri_s','$fri_e','$sat_s','$sat_e','$sun_s','$sun_e')";
    mysqli_query($conn,$insert_detail);

//InfoData에 number만큼 같은key값을 가진 tuple이 존재하면 closed값을 1로 바꾸기
    $sqlrec2="SELECT * FROM RoomData WHERE roomKey='$roomkey'";
    $info2=mysqli_query($conn, $sqlrec2);
    $number = mysqli_fetch_array($info2);

    $sqlrec="SELECT * FROM InfoData WHERE roomKey='$roomkey'";
    $info=mysqli_query($conn, $sqlrec);
    $count=mysqli_num_rows($info);

    if($count== $number[4]){
      $update_closed = "UPDATE RoomData SET closed = 1 WHERE roomKey='$roomkey'";
    }
    mysqli_query($conn,$update_closed);

//다음페이지로 이동
    echo "
      <script>
        document.location.href='http://ttakmanna.com/success/';
      </script>
    ";


 ?>
