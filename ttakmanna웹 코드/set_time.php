<?php
session_start();

header("Content-Type:text/html; charset=UTF-8");

//세션에저장된키값받아오기
$roomkey=NULL;
if(isset($_SESSION['roomkey'])){
 $roomkey=$_SESSION['roomkey'];
}

//요일별시간배열
$mon_s_Array=array();
$mon_e_Array=array();
$tue_s_Array=array();
$tue_e_Array=array();
$wed_s_Array=array();
$wed_e_Array=array();
$thu_s_Array=array();
$thu_e_Array=array();
$fri_s_Array=array();
$fri_e_Array=array();
$sat_s_Array=array();
$sat_e_Array=array();
$sun_s_Array=array();
$sun_e_Array=array();

//db 연결
 $mysql_host =
 $mysql_user =
 $mysql_password =
 $mysql_db = 

 $conn = mysqli_connect($mysql_host, $mysql_user, $mysql_password, $mysql_db);

 if(mysqli_connect_errno()){
   die("db연결실패".mysqli_connect_error()); }

 mysqli_query($conn, "set names utf8");

//키값이 같다면 각 변수에 데이터삽입
 $sqlroom ="SELECT * FROM RoomData WHERE roomKey='$roomkey'";
 $result_room=mysqli_query($conn,$sqlroom);
 $b=mysqli_fetch_assoc($result_room);
 $mode=$b['mode'];

 $sqlrec2="SELECT * FROM RoomData WHERE roomKey='$roomkey'";
 $info2=mysqli_query($conn, $sqlrec2);
 $number = mysqli_fetch_array($info2);

 $sqlinfo="SELECT * FROM InfoData WHERE roomKey='$roomkey'";
 $result_info=mysqli_query($conn,$sqlinfo);
 while($b1=mysqli_fetch_assoc($result_info)){
   $mon_s_Array[]=$b1['mon_s'];
   $mon_e_Array[]=$b1['mon_e'];
   $tue_s_Array[]=$b1['tue_s'];
   $tue_e_Array[]=$b1['tue_e'];
   $wed_s_Array[]=$b1['wed_s'];
   $wed_e_Array[]=$b1['wed_e'];
   $thu_s_Array[]=$b1['thu_s'];
   $thu_e_Array[]=$b1['thu_e'];
   $fri_s_Array[]=$b1['fri_s'];
   $fri_e_Array[]=$b1['fri_e'];
   $sat_s_Array[]=$b1['sat_s'];
   $sat_e_Array[]=$b1['sat_e'];
   $sun_s_Array[]=$b1['sun_s'];
   $sun_e_Array[]=$b1['sun_e'];
 }

 $n = $number[4];
//모드가 1,2면 시간 계산
 if($mode == 1 || $mode == 2){
   //각 요일별 최대start값
   $tmp1 = strtotime("");
    for($i=0;$i<$n;$i++){
      if((string)$mon_s_Array[$i] == "00:00:00" || $mon_s_Array[$i] == NULL){
        $tmp1=NULL;
        break;
      }
      elseif($tmp1 < $mon_s_Array[$i])
      {
        $tmp1 = $mon_s_Array[$i];
      }
    }
    $mon_s=$tmp1;

    $tmp2 = strtotime("");
    for($i=0;$i<$n;$i++){
      if((string)$tue_s_Array[$i] == "00:00:00" || $tue_s_Array[$i] == NULL){
        $tmp2="";
        break;
      }
      elseif($tmp2 < $tue_s_Array[$i])
      {
        $tmp2 = $tue_s_Array[$i];
      }
    }
    $tue_s=$tmp2;

    $tmp3 = strtotime("");
    for($i=0;$i<$n;$i++){
      if((string)$wed_s_Array[$i] == "00:00:00" || $wed_s_Array[$i] == NULL){
        $tmp3="";
        break;
      }
      elseif($tmp3 < $wed_s_Array[$i])
      {
        $tmp3 = $wed_s_Array[$i];
      }
    }
    $wed_s=$tmp3;

    $tmp4 = strtotime("");
    for($i=0;$i<$n;$i++){
      if((string)$thu_s_Array[$i] == "00:00:00" || $thu_s_Array[$i] == NULL){
        $tmp4="";
        break;
      }
      elseif($tmp4 < $thu_s_Array[$i])
      {
        $tmp4 = $thu_s_Array[$i];
      }
    }
    $thu_s=$tmp4;

    $tmp5 = strtotime("");
    for($i=0;$i<$n;$i++){
      if((string)$fri_s_Array[$i] == "00:00:00" || $fri_s_Array[$i] == NULL){
        $tmp5="";
        break;
      }
      elseif($tmp5 < $fri_s_Array[$i])
      {
        $tmp5 = $fri_s_Array[$i];
      }
    }
    $fri_s=$tmp5;

    $tmp6 = strtotime("");
    for($i=0;$i<$n;$i++){
      if((string)$sat_s_Array[$i] == "00:00:00" || $sat_s_Array[$i] == NULL){
        $tmp6="";
        break;
      }
      elseif($tmp6 < $sat_s_Array[$i])
      {
        $tmp6 = $sat_s_Array[$i];
      }
    }
    $sat_s=$tmp6;

    $tmp7 = strtotime("");
    for($i=0;$i<$n;$i++){
      if((string)$sun_s_Array[$i] == "00:00:00" || $sun_s_Array[$i] == NULL){
        $tmp7="";
        break;
      }
      elseif($tmp7 < $sun_s_Array[$i])
      {
        $tmp7 = $sun_s_Array[$i];
      }
    }
    $sun_s=$tmp7;

    //각요일별 최소 end값

    $tmp8 = $mon_e_Array[0];
     for($i=0;$i<$n;$i++){
       if((string)$mon_e_Array[$i] == "00:00:00" || $mon_e_Array[$i] == NULL){
         $tmp8=NULL;
         break;
       }
       elseif($tmp8 > $mon_e_Array[$i])
       {
         $tmp8 = $mon_e_Array[$i];
       }
     }
     $mon_e=$tmp8;

     $tmp9 = $tue_e_Array[0];
     for($i=0;$i<$n;$i++){
       if((string)$tue_e_Array[$i] == "00:00:00" || $tue_e_Array[$i] == NULL){
         $tmp9="";
         break;
       }
       elseif($tmp9 < $tue_e_Array[$i])
       {
         $tmp9 = $tue_e_Array[$i];
       }
     }
     $tue_e=$tmp9;

     $tmp10 = $wed_e_Array[0];
     for($i=0;$i<$n;$i++){
       if((string)$wed_e_Array[$i] == "00:00:00" || $wed_e_Array[$i] == NULL){
         $tmp10="";
         break;
       }
       elseif($tmp10 < $wed_e_Array[$i])
       {
         $tmp10 = $wed_e_Array[$i];
       }
     }
     $wed_e=$tmp10;

     $tmp11 = $thu_e_Array[0];
     for($i=0;$i<$n;$i++){
       if((string)$thu_e_Array[$i] == "00:00:00" || $thu_e_Array[$i] == NULL){
         $tmp11="";
         break;
       }
       elseif($tmp11 < $thu_e_Array[$i])
       {
         $tmp11 = $thu_e_Array[$i];
       }
     }
     $thu_e=$tmp11;

     $tmp12 = $fri_e_Array[0];
     for($i=0;$i<$n;$i++){
       if((string)$fri_e_Array[$i] == "00:00:00" || $fri_e_Array[$i] == NULL){
         $tmp12="";
         break;
       }
       elseif($tmp12 < $fri_e_Array[$i])
       {
         $tmp12 = $fri_e_Array[$i];
       }
     }
     $fri_e=$tmp12;

     $tmp13 = $sat_e_Array[0];
     for($i=0;$i<$n;$i++){
       if((string)$sat_e_Array[$i] == "00:00:00" || $sat_e_Array[$i] == NULL){
         $tmp13="";
         break;
       }
       elseif($tmp13 < $sat_e_Array[$i])
       {
         $tmp13 = $sat_e_Array[$i];
       }
     }
     $sat_e=$tmp13;

     $tmp14 = $sun_e_Array[0];
     for($i=0;$i<$n;$i++){
       if((string)$sun_e_Array[$i] == "00:00:00" || $sun_e_Array[$i] == NULL){
         $tmp14="";
         break;
       }
       elseif($tmp14 < $sun_e_Array[$i])
       {
         $tmp14 = $sun_e_Array[$i];
       }
     }
     $sun_e=$tmp14;

}else{ //모드가 3,4면 모든값 null
   $mon_s=NULL;
   $mon_e=NULL;
   $tue_s=NULL;
   $tue_e=NULL;
   $wed_s=NULL;
   $wed_e=NULL;
   $thu_s=NULL;
   $thu_e=NULL;
   $fri_s=NULL;
   $fri_e=NULL;
   $sat_s=NULL;
   $sat_e=NULL;
   $sun_s=NULL;
   $sun_e=NULL;
 }
//세션에 값 저장
 $_SESSION['mon_s']=$mon_s;
 $_SESSION['mon_e']=$mon_e;
 $_SESSION['tue_s']=$tue_s;
 $_SESSION['tue_e']=$tue_e;
 $_SESSION['wed_s']=$wed_s;
 $_SESSION['wed_e']=$wed_e;
 $_SESSION['thu_s']=$thu_s;
 $_SESSION['thu_e']=$thu_e;
 $_SESSION['fri_s']=$fri_s;
 $_SESSION['fri_e']=$fri_e;
 $_SESSION['sat_s']=$sat_s;
 $_SESSION['sat_e']=$sat_e;
 $_SESSION['sun_s']=$sun_s;
 $_SESSION['sun_e']=$sun_e;

//장소정하는 페이지로 이동
echo "
   <script>
     document.location.href='http://ttakmanna.com/ttak/set_place.php';
   </script>
   ";

 ?>
