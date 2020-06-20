package com.JHJ_Studio.ttakmanna;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

//첫화면, 로그인 화면
public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 1001;

    private BackPressCloseHandler backPressCloseHandler;
    Button b1,b2,b3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        backPressCloseHandler = new BackPressCloseHandler(this);

        getHashKey();


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
                Intent intent = new Intent(getBaseContext(),HomeActivity.class);
                startActivityForResult(intent,REQUEST_CODE);

                overridePendingTransition(R.anim.enter,R.anim.exit);
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

    private void getHashKey(){
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo == null)
            Log.e("KeyHash", "KeyHash:null");

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            } catch (NoSuchAlgorithmException e) {
                Log.e("KeyHash", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
    }

    //백버튼 누르면 앱 종료
    @Override
    public void onBackPressed(){
        backPressCloseHandler.onBackPressed();
    }
}
