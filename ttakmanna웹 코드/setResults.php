<?php

  session_start();

  header("Content-Type:text/html; charset=UTF-8");

  //세션에저장된키값받아오기
  $roomkey=NULL;
  if(isset($_SESSION['roomkey'])){
    $roomkey=$_SESSION['roomkey'];
  }
  echo $roomkey."<br/>";

  $mon_s=NULL;
  if(isset($_SESSION['mon_s'])){
    $mon_s=$_SESSION['mon_s'];
  }
  echo $mon_s."<br/>";

  $mon_e=NULL;
  if(isset($_SESSION['mon_e'])){
    $mon_e=$_SESSION['mon_e'];
  }
  echo $mon_e."<br/>";

  $tue_s=NULL;
  if(isset($_SESSION['tue_s'])){
    $tue_s=$_SESSION['tue_s'];
  }
  echo $tue_s."<br/>";

  $tue_e=NULL;
  if(isset($_SESSION['tue_e'])){
    $tue_e=$_SESSION['tue_e'];
  }
  echo $tue_e."<br/>";

  $wed_s=NULL;
  if(isset($_SESSION['wed_s'])){
    $wed_s=$_SESSION['wed_s'];
  }
  echo $wed_s."<br/>";

  $wed_e=NULL;
  if(isset($_SESSION['wed_e'])){
    $wed_e=$_SESSION['wed_e'];
  }
  echo $wed_e."<br/>";

  $thu_s=NULL;
  if(isset($_SESSION['thu_s'])){
    $thu_s=$_SESSION['thu_s'];
  }
  echo $thu_s."<br/>";

  $thu_e=NULL;
  if(isset($_SESSION['thu_e'])){
    $thu_e=$_SESSION['thu_e'];
  }
  echo $thu_e."<br/>";

  $fri_s=NULL;
  if(isset($_SESSION['fri_s'])){
    $fri_s=$_SESSION['fri_s'];
  }
  echo $fri_s."<br/>";

  $fri_e=NULL;
  if(isset($_SESSION['fri_e'])){
    $fri_e=$_SESSION['fri_e'];
  }
  echo $fri_e."<br/>";

  $sat_s=NULL;
  if(isset($_SESSION['sat_s'])){
    $sat_s=$_SESSION['sat_s'];
  }
  echo $sat_s."<br/>";

  $sat_e=NULL;
  if(isset($_SESSION['sat_e'])){
    $sat_e=$_SESSION['sat_e'];
  }
  echo $sat_e."<br/>";

  $sun_s=NULL;
  if(isset($_SESSION['sun_s'])){
    $sun_s=$_SESSION['sun_s'];
  }
  echo $sun_s."<br/>";

  $sun_e=NULL;
  if(isset($_SESSION['sun_e'])){
    $sun_e=$_SESSION['sun_e'];
  }
  echo $sun_e."kkkkkk <br/>";

//form에서 받아온값 입력
  if(!empty($_POST['rn1'])) {
    $dest1=$_POST['rn1'];
  }
  echo $dest1."kk<br/>";

  if(!empty($_POST['rn2'])) {
    $dest2=$_POST['rn2'];
  }
  echo $dest2."<br/>";

  if(!empty($_POST['rn3'])) {
    $dest3=$_POST['rn3'];
  }
  echo $dest3."<br/>";

  if(!empty($_POST['rn4'])) {
    $dest4=$_POST['rn4'];
  }
  echo $dest4."<br/>";

  if(!empty($_POST['rn5'])) {
    $dest5=$_POST['rn5'];
  }
  echo $dest5."<br/>";

  if(!empty($_POST['lt1'])) {
    $dest1_lat=$_POST['lt1'];
  }
  echo $dest1_lat."<br/>";

  if(!empty($_POST['lt2'])) {
    $dest2_lat=$_POST['lt2'];
  }
  echo $dest2_lat."<br/>";

  if(!empty($_POST['lt3'])) {
    $dest3_lat=$_POST['lt3'];
  }
  echo $dest3_lat."<br/>";

  if(!empty($_POST['lt4'])) {
    $dest4_lat=$_POST['lt4'];
  }
  echo $dest4_lat."<br/>";

  if(!empty($_POST['lt5'])) {
    $dest5_lat=$_POST['lt5'];
  }
  echo $dest5_lat."<br/>";

  if(!empty($_POST['ln1'])) {
    $dest1_long=$_POST['ln1'];
  }
  echo $dest1_long."<br/>";

  if(!empty($_POST['ln2'])) {
    $dest2_long=$_POST['ln2'];
  }
  echo $dest2_long."<br/>";

  if(!empty($_POST['ln3'])) {
    $dest3_long=$_POST['ln3'];
  }
  echo $dest3_long."<br/>";

  if(!empty($_POST['ln4'])) {
    $dest4_long=$_POST['ln4'];
  }
  echo $dest4_long."<br/>";

  if(!empty($_POST['ln5'])) {
    $dest5_long=$_POST['ln5'];
  }
  echo $dest5_long."<br/>";

 //db 연결
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
  ="INSERT INTO AppointmentData(roomKey, dest1, dest1_lat, dest1_long, dest2, dest2_lat, dest2_long, dest3, dest3_lat, dest3_long, dest4, dest4_lat, dest4_long, dest5, dest5_lat, dest5_long, mon_s, mon_e, tue_s, tue_e, wed_s, wed_e, thu_s, thu_e, fri_s, fri_e ,sat_s, sat_e, sun_s, sun_e)
  VALUES('$roomkey',
    '$dest1', '$dest1_lat', '$dest1_long',
    '$dest2', '$dest2_lat', '$dest2_long',
    '$dest3', '$dest3_lat', '$dest3_long',
    '$dest4', '$dest4_lat', '$dest4_long',
    '$dest5', '$dest5_lat', '$dest5_long',
    '$mon_s','$mon_e','$tue_s','$tue_e','$wed_s','$wed_e','$thu_s','$thu_e','$fri_s','$fri_e','$sat_s','$sat_e','$sun_s','$sun_e')";
  mysqli_query($conn,$insert_result);

  //다음페이지로 이동
      echo "
        <script>
          document.location.href='http://ttakmanna.com/success/';
        </script>
      ";
 ?>
