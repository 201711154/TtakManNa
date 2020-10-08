<?php
    session_start();
 header("Content-Type:text/html; charset=UTF-8");

 //입력된key값변수에저장하기
  $roomkey=$_POST['roomkey'];

//가져올값
  $closed=NULL;

//db연결하기 비밀번호 노출 우려로 삭제하고 올립니다.
  $mysql_host =
  $mysql_user =
  $mysql_password =
  $mysql_db = 

  $conn = mysqli_connect($mysql_host, $mysql_user, $mysql_password, $mysql_db);

  if(mysqli_connect_errno()){
	  die("db연결실패".mysqli_connect_error()); }

  mysqli_query($conn, "set names utf8");

// 입력값과db에저장된값일치여부확인
  $sqlrec="SELECT * FROM RoomData WHERE roomKey='$roomkey'";
  $info=mysqli_query($conn, $sqlrec);
  $rowNum=mysqli_num_rows($info);
  if(!$rowNum)
    {
      echo "
        <script>
          alert('일치하는 모임코드가 없습니다.');
          history.back();
        </script>
      ";
      session_unset();
      exit;
    }

    $closed = mysqli_fetch_assoc($info);
    if(!($closed['closed'] == '0')){
      echo "
        <script>
          alert('모든 인원이 참여했습니다.');
          document.location.href='http://ttakmanna.com/success/';
        </script>
      ";

    }

//세션에키값저장

    $_SESSION['roomkey']=$roomkey;

//key값이일치하면다음페이지로이동
    echo "
      <script>
        document.location.href='http://ttakmanna.com/%ec%84%b8%eb%b6%80%ec%82%ac%ed%95%ad/';
      </script>
    ";
 ?>
