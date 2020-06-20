package com.JHJ_Studio.ttakmanna;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UserData {
    private long ID = 0;
    private String Name = "";
    private String EMail="";
    private String ProfileURL = "";
    private Bitmap Profile;

    private static UserData instance = null;

    public static synchronized UserData getInstance(){
        if(null == instance){
            instance = new UserData();
        }
        return instance;
    }

    public void setID(long ID){ this.ID = ID; }
    public void setName(String Name){ this.Name = Name; }
    public void setEMail(String EMail){ this.EMail = EMail; }

    public void setProfileURL(final String profileURL) {
        this.ProfileURL = profileURL;
        if(profileURL != null){
            Log.d("ProfileURL : ", ProfileURL);
            Thread uThread = new Thread() {
                @Override
                public void run() {
                    try {
                        URL url = new URL(ProfileURL);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setDoInput(true); //Server 통신에서 입력 가능한 상태로 만듦
                        conn.connect(); //연결된 곳에 접속할 때 (connect() 호출해야 실제 통신 가능함)
                        InputStream is = conn.getInputStream(); //inputStream 값 가져오기
                        Profile = BitmapFactory.decodeStream(is); // Bitmap으로 반환
                    } catch (Exception e) {
                        Log.e("Profile", "프로필 불러오기 에러");
                    }
                }
            };
            uThread.start();
        }
    }


    public long getID(){return ID;}
    public String getName(){return Name;}
    public String getEMail(){return EMail;}
    public Bitmap getProfile(){return Profile;}

}
