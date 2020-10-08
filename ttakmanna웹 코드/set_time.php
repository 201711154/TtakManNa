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

//db 연결 비밀번호 노출우려로 삭제하고 올립니다.
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
 for($i=0;$i<$number[4];$i++){
   echo "$mon_s_Array[i]";
   echo "$tue_s_Array[i]";
 }
 if($mode == 1 || $mode == 2){
   //배열이 비어있지 않으면 최대start값
   if($mon_s_Array!==NULL){
     $tmp = strtotime("");

     for($i=0;$i<$number[4];$i++){
       if($tmp < $mon_s_Array[i])
       {
         $tmp = $mon_s_Array[i];
        echo "$tmp";
       }
     }
     $mon_s=$tmp;
     echo "$mon_s";
   }

   if($tue_s_Array==NULL){
     $tue_s = NULL;
   }
   else{
     $tue_s=max($tue_s_Array);
   }

   if($wed_s_Array==NULL){
     $wed_s = NULL;
   }
   else{
     $wed_s=max($wed_s_Array);
   }

   if($thu_s_Array==NULL){
     $thu_s = NULL;
   }
   else{
     $thu_s=max($thu_s_Array);
   }

   if($fri_s_Array==NULL){
     $fri_s = NULL;
   }
   else{
     $fri_s=max($fri_s_Array);
   }

   if($sat_s_Array==NULL){
     $sat_s = NULL;
   }
   else{
     $sat_s=max($sat_s_Array);
   }

   if($sun_s_Array==NULL){
     $sun_s = NULL;
   }
   else{
     $sun_s=max($sun_s_Array);
   }
   //배열이 비어있지 않으면 최소end값

   if($mon_e_Array==NULL){
     $mon_e = NULL;
   }
   else{
     $mon_e=min($mon_e_Array);
   }
   if($tue_e_Array==NULL){
     $tue_e = NULL;
   }
   else{
     $tue_e=min($tue_e_Array);
   }

   if($wed_e_Array==NULL){
     $wed_e = NULL;
   }
   else{
     $wed_e=min($wed_e_Array);
   }

   if($thu_e_Array==NULL){
     $thu_e = NULL;
   }
   else{
     $thu_e=min($thu_e_Array);
   }

   if($fri_e_Array==NULL){
     $fri_e = NULL;
   }
   else{
     $fri_e=min($fri_e_Array);
   }

   if($sat_e_Array==NULL){
     $sat_e = NULL;
   }
   else{
     $sat_e=min($sun_e_Array);
   }

   if($sun_e_Array==NULL){
     $sun_e = NULL;
   }
   else{
     $sun_e=min($sun_e_Array);
   }
 // start값 > end값 이면 둘다 null
   if($mon_s>$mon_e){
     $mon_s=NULL;
     $mon_e=NULL;
   }
   if($tue_s>$tue_e){
     $tue_s=NULL;
     $tue_e=NULL;
   }
   if($wed_s>$wed_e){
     $wed_s=NULL;
     $wed_e=NULL;
   }
   if($thu_s>$thu_e){
     $thu_s=NULL;
     $thu_e=NULL;
   }
   if($fri_s>$fri_e){
     $fri_s=NULL;
     $fri_e=NULL;
   }
   if($sat_s>$sat_e){
     $sat_s=NULL;
     $sat_e=NULL;
   }
   if($sun_s>$sun_e){
     $sun_s=NULL;
     $sun_e=NULL;
   }
 }
 else{ //1,2가 아니면 모든값 null
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
/* echo "
   <script>
     window.location.href=http:'http://ttakmanna.com/set_place.php';
   </script>
   ";*/

 ?>
