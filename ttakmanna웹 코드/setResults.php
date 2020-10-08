<?php

  session_start();

  header("Content-Type:text/html; charset=UTF-8");

  //세션에저장된키값받아오기
  $roomkey=NULL;
  if(isset($_SESSION['roomkey'])){
    $roomkey=$_SESSION['roomkey'];
  }

  $mon_s=NULL;
  if(isset($_SESSION['mon_s'])){
    $roomkey=$_SESSION['mon_s'];
  }
  $mon_e=NULL;
  if(isset($_SESSION['mon_e'])){
    $roomkey=$_SESSION['mon_e'];
  }
  $tue_s=NULL;
  if(isset($_SESSION['tue_s'])){
    $roomkey=$_SESSION['tue_s'];
  }
  $tue_e=NULL;
  if(isset($_SESSION['tue_e'])){
    $roomkey=$_SESSION['tue_e'];
  }
  $wed_s=NULL;
  if(isset($_SESSION['wed_s'])){
    $roomkey=$_SESSION['wed_s'];
  }
  $wed_e=NULL;
  if(isset($_SESSION['wed_e'])){
    $roomkey=$_SESSION['wed_e'];
  }
  $thu_s=NULL;
  if(isset($_SESSION['thu_s'])){
    $roomkey=$_SESSION['thu_s'];
  }
  $thu_e=NULL;
  if(isset($_SESSION['thu_e'])){
    $roomkey=$_SESSION['thu_e'];
  }
  $fri_s=NULL;
  if(isset($_SESSION['fri_s'])){
    $roomkey=$_SESSION['fri_s'];
  }
  $fri_e=NULL;
  if(isset($_SESSION['fri_e'])){
    $roomkey=$_SESSION['fri_e'];
  }
  $sat_s=NULL;
  if(isset($_SESSION['sat_s'])){
    $roomkey=$_SESSION['sat_s'];
  }
  $sat_e=NULL;
  if(isset($_SESSION['sat_e'])){
    $roomkey=$_SESSION['sat_e'];
  }
  $sun_s=NULL;
  if(isset($_SESSION['sun_s'])){
    $roomkey=$_SESSION['sun_s'];
  }
  $sun_e=NULL;
  if(isset($_SESSION['sun_e'])){
    $roomkey=$_SESSION['sun_e'];
  }

//form에서 받아온값 입력
  if(!empty($_POST['rn1'])) {
    $dest1=$_POST['rn1'];
  }
  if(!empty($_POST['rn2'])) {
    $dest2=$_POST['rn2'];
  }
  if(!empty($_POST['rn3'])) {
    $dest3=$_POST['rn3'];
  }
  if(!empty($_POST['rn4'])) {
    $dest4=$_POST['rn4'];
  }
  if(!empty($_POST['rn5'])) {
    $dest5=$_POST['rn5'];
  }

  if(!empty($_POST['lt1'])) {
    $dest1_lat=$_POST['lt1'];
  }
  if(!empty($_POST['lt2'])) {
    $dest2_lat=$_POST['lt2'];
  }
  if(!empty($_POST['lt3'])) {
    $dest3_lat=$_POST['lt3'];
  }
  if(!empty($_POST['lt4'])) {
    $dest4_lat=$_POST['lt4'];
  }
  if(!empty($_POST['lt5'])) {
    $dest5_lat=$_POST['lt5'];
  }

  if(!empty($_POST['ln1'])) {
    $dest1_long=$_POST['ln1'];
  }
  if(!empty($_POST['ln2'])) {
    $dest2_long=$_POST['ln2'];
  }
  if(!empty($_POST['ln3'])) {
    $dest3_long=$_POST['ln3'];
  }
  if(!empty($_POST['ln4'])) {
    $dest4_long=$_POST['ln4'];
  }
  if(!empty($_POST['ln5'])) {
    $dest5_long=$_POST['ln5'];
  }

 //db 연결 비밀번호 노출 우려로 삭제하고 올립니다.
  $mysql_host =
  $mysql_user = 
  $mysql_password =
  $mysql_db =

  $conn = mysqli_connect($mysql_host, $mysql_user, $mysql_password, $mysql_db);

  if(mysqli_connect_errno()){
    die("db연결실패".mysqli_connect_error()); }

  mysqli_query($conn, "set names utf8");

 //DB에삽입
  $insert_result
  ="INSERT INTO AppointmentData(roomKey,dest1,dest1_lat,dest1_long,dest2,dest2_lat,dest2_long,dest3,dest3_lat,dest3_long,dest4,dest4_lat,dest4_long,dest5,dest5_lat,dest5_long,mon_s,mon_e,tue_s,tue_e,wed_s,wed_e,thu_s,thu_e,fri_s,fri_e,sat_s,sat_e,sun_s,sun_e)
  VALUES('$roomkey','$dest1','$dest1_lat','$dest1_long','$dest2','$dest2_lat '$dest2_long,'$dest3','$dest3_lat','$dest3_long','$dest4','$dest4_lat','$dest4_long','$dest5','$dest5_lat','$dest5_long','$lat','$lng','$mon_s','$mon_e','$tue_s','$tue_e','$wed_s','$wed_e','$thu_s','$thu_e','$fri_s','$fri_e','$sat_s','$sat_e','$sun_s','$sun_e')";
  mysqli_query($conn,$insert_result);

  //다음페이지로 이동
      echo "
        <script>
          document.location.href='http://ttakmanna.com/success/';
        </script>
      ";
 ?>
