package com.JHJ_Studio.ttakmanna;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;

//홈화면
public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final int REQUEST_CODE = 1001;

    private BackPressCloseHandler backPressCloseHandler;
    Button b1,b2;
    TextView nameTxt;
    ImageView profileImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        backPressCloseHandler = new BackPressCloseHandler(this);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        b1 = (Button)findViewById(R.id.go_NewScheduleActivity);
        b2 = (Button)findViewById(R.id.go_Introduce);

        View header = navigationView.getHeaderView(0);

        nameTxt = (TextView)header.findViewById(R.id.text_nickname);
        profileImg = (ImageView)header.findViewById(R.id.image_profile);

        nameTxt.setText(UserData.getInstance().getName());
        if(UserData.getInstance().getProfile() != null)
        { profileImg.setImageBitmap(UserData.getInstance().getProfile());}

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),NewScheduleActivity.class);
                startActivityForResult(intent,REQUEST_CODE);

                overridePendingTransition(R.anim.enter,R.anim.exit);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),ScheduleListActivity.class);
                startActivityForResult(intent,REQUEST_CODE);

                overridePendingTransition(R.anim.enter,R.anim.exit);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            backPressCloseHandler.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.go_new) {
            Intent intent = new Intent(getBaseContext(),NewScheduleActivity.class);
            startActivityForResult(intent,REQUEST_CODE);

            overridePendingTransition(R.anim.enter,R.anim.exit);
        } else if (id == R.id.go_lise) {
            Intent intent = new Intent(getBaseContext(),ScheduleListActivity.class);
            startActivityForResult(intent,REQUEST_CODE);

            overridePendingTransition(R.anim.enter,R.anim.exit);

        } else if (id == R.id.go_setting) { //설정화면으로 이동x -> 고객문의화면으로 이동o
            Intent intent = new Intent(getBaseContext(),IntroductionActivity.class);
            startActivityForResult(intent,REQUEST_CODE);

            overridePendingTransition(R.anim.enter,R.anim.exit);

        }else if(id == R.id.logout){
            UserManagement.getInstance()
                    .requestLogout(new LogoutResponseCallback() {
                        @Override
                        public void onCompleteLogout() {
                            Toast.makeText(HomeActivity.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });

            Intent intent = new Intent(getBaseContext(),MainActivity.class);
            startActivityForResult(intent,REQUEST_CODE);
            overridePendingTransition(R.anim.enter,R.anim.exit);
        }else if(id == R.id.quit){
            UserManagement.getInstance()
                    .requestUnlink(new UnLinkResponseCallback() {
                        @Override
                        public void onSessionClosed(ErrorResult errorResult) {

                        }

                        @Override
                        public void onSuccess(Long result) {
                            Toast.makeText(HomeActivity.this, "탈퇴되었습니다.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getBaseContext(),MainActivity.class);
                            startActivityForResult(intent,REQUEST_CODE);
                            overridePendingTransition(R.anim.enter,R.anim.exit);
                        }
                    });
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
