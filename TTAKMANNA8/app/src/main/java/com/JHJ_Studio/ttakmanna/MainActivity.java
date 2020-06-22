package com.JHJ_Studio.ttakmanna;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.ApiErrorCode;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.usermgmt.response.model.Profile;
import com.kakao.usermgmt.response.model.UserAccount;
import com.kakao.util.OptionalBoolean;
import com.kakao.util.exception.KakaoException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

//첫화면, 로그인 화면
public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 1001;

    private BackPressCloseHandler backPressCloseHandler;
    Button b1,b2,b3;
    Session session;
    private SessionCallback sessionCallback = new SessionCallback();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        session = Session.getCurrentSession();
        session.addCallback(sessionCallback);
        session.checkAndImplicitOpen(); //자동 로그인
       backPressCloseHandler = new BackPressCloseHandler(this);

        //b1,2,3 SNS에서 사용자 정보 받아와서 로그인 하고 홈으로 이동하는 버튼
        b1 = (Button)findViewById(R.id.login_facebook);
        b2 = (Button)findViewById(R.id.login_kakao);
        b3 = (Button)findViewById(R.id.login_twitter);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),HomeActivity.class);
                startActivityForResult(intent,REQUEST_CODE);

                overridePendingTransition(R.anim.enter,R.anim.exit);

            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.open(AuthType.KAKAO_LOGIN_ALL,MainActivity.this);
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),HomeActivity.class);
                startActivityForResult(intent,REQUEST_CODE);

                overridePendingTransition(R.anim.enter,R.anim.exit);
            }
        });
    }

    //백버튼 누르면 앱 종료
    @Override
    public void onBackPressed(){
        backPressCloseHandler.onBackPressed();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Session.getCurrentSession().removeCallback(sessionCallback);
    }

    @Override
    protected  void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private class SessionCallback implements ISessionCallback {
        @Override
        public void onSessionOpened() {
            UserManagement.getInstance().me(new MeV2ResponseCallback() {
                @Override
                public void onFailure(ErrorResult errorResult) {
                    int result = errorResult.getErrorCode();

                    if(result == ApiErrorCode.CLIENT_ERROR_CODE) {
                        Toast.makeText(getApplicationContext(), "네트워크 연결이 불안정합니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(),"로그인 도중 오류가 발생했습니다: "+errorResult.getErrorMessage(),Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                    Toast.makeText(getApplicationContext(),"세션이 닫혔습니다. 다시 시도해 주세요: "+errorResult.getErrorMessage(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(MeV2Response result) {
                    UserAccount kakaoAccount = result.getKakaoAccount();
                    Profile profile = kakaoAccount.getProfile();
                    UserData.getInstance().setID(result.getId());
                    UserData.getInstance().setEMail(kakaoAccount.getEmail());
                    UserData.getInstance().setName(profile.getNickname());
                    UserData.getInstance().setProfileURL(profile.getProfileImageUrl());
                    Log.i("KAKAO_API", "사용자 아이디: " + UserData.getInstance().getID());
                    Log.i("KAKAO_API", "사용자 이메일: " + UserData.getInstance().getEMail());
                    Log.i("KAKAO_API", "사용자 이름: " + UserData.getInstance().getName());


                    Intent intent = new Intent(getBaseContext(),HomeActivity.class);
                    startActivityForResult(intent,REQUEST_CODE);
                    overridePendingTransition(R.anim.enter,R.anim.exit);
                    finish();
                }
            });
        }

        @Override
        public void onSessionOpenFailed(KakaoException e) {
            Toast.makeText(getApplicationContext(), "로그인 도중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
        }
    }
}
