package com.JHJ_Studio.ttakmanna;

import android.content.ContentValues;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class RequestHttpURLConnection {
    public String request(String url, ContentValues params){
        HttpURLConnection urlConn = null;
        StringBuffer sbParams = new StringBuffer();

        //보낼 데이터로 파라미터 채우기
        //보낼 데이터가 없으면 파라미터 비우기
        if(params == null){
            sbParams.append("");
        }
        else{
            boolean isAnd = false;
            String key;
            String value;

            for(Map.Entry<String, Object> parameter : params.valueSet()){
                key = parameter.getKey();
                value = parameter.getValue().toString();

                //파라미터가 두개 이상일 때
                if(isAnd){
                    sbParams.append("&");
                }
                sbParams.append(key).append("=").append(value);
                if(!isAnd){
                    if(params.size()>= 2){
                        isAnd = true;
                    }
                }
            }
        }
        //HttpURLConnection을 통해 데이터 가져오기
        try{
            URL connUrl = new URL(url);
            urlConn = (HttpURLConnection) connUrl.openConnection();

            //urlConn설정
            urlConn.setRequestMethod("POST");
            urlConn.setRequestProperty("Accept-Charset","UTF-8");
            urlConn.setRequestProperty("Context_Type","application/x-www-form-urlencoded;charset=UTF-8");

            //파라미터 전달 / 데이터 읽어오기
            String strParams = sbParams.toString();
            OutputStream os = urlConn.getOutputStream();
            os.write(strParams.getBytes("UTF-8"));
            os.flush();
            os.close();

            //연결요청확인
            if(urlConn.getResponseCode()!=HttpURLConnection.HTTP_OK){
                return null;
            }

            //읽어온 결과물 리턴
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream(),"UTF-8"));

            String line;
            String page = "";

            while((line = reader.readLine()) != null){
                page += line;
            }
            Log.d("result", page);
            return page;
        }catch(MalformedURLException e) {
            e.printStackTrace();
        }catch(IOException e) {
            e.printStackTrace();
        }finally{
            if(urlConn != null){
                urlConn.disconnect();
            }
        }
        Log.d("result", "return null");
        return null;
    }
}
