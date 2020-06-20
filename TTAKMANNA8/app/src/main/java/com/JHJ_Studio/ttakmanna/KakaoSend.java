package com.JHJ_Studio.ttakmanna;

import android.content.Context;
import android.util.Log;

import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.message.template.ButtonObject;
import com.kakao.message.template.ContentObject;
import com.kakao.message.template.FeedTemplate;
import com.kakao.message.template.LinkObject;
import com.kakao.message.template.LocationTemplate;
import com.kakao.message.template.SocialObject;
import com.kakao.message.template.TemplateParams;
import com.kakao.message.template.TextTemplate;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;

public class KakaoSend {
    Context context;
    public KakaoSend(Context c){this.context = c;}
    public void linkMessage(){
        TemplateParams params = FeedTemplate
                .newBuilder(ContentObject.newBuilder(
                        "약속정하기!",
                        "http://mud-kage.kakao.co.kr/dn/NTmhS/btqfEUdFAUf/FjKzkZsnoeE4o19klTOVI1/openlink_640x640s.jpg",
                        LinkObject.newBuilder()
                                .setWebUrl("")
                                .setMobileWebUrl("")
                                .build())
                        .setDescrption("링크에 접속하여 대답해주세요.")
                        .build())
                .addButton(new ButtonObject(
                        "응답 하기",
                        LinkObject.newBuilder()
                                .setWebUrl("http://www.naver.com")
                                .setMobileWebUrl("http://m.naver.com")
                                .build()))
                .build();
        KakaoLinkService.getInstance()
                .sendDefault(context, params, new ResponseCallback<KakaoLinkResponse>() {
                    @Override
                    public void onFailure(ErrorResult errorResult) {
                        Log.e("KAKAO_API", "카카오링크 공유 실패: " + errorResult);
                    }

                    @Override
                    public void onSuccess(KakaoLinkResponse result) {
                        Log.i("KAKAO_API", "카카오링크 공유 성공");

                        // 카카오링크 보내기에 성공했지만 아래 경고 메시지가 존재할 경우 일부 컨텐츠가 정상 동작하지 않을 수 있습니다.
                        Log.w("KAKAO_API", "warning messages: " + result.getWarningMsg());
                        Log.w("KAKAO_API", "argument messages: " + result.getArgumentMsg());
                    }
                });

    }
    public void shareMessage(String title, String participant, String date, String time, String location,String address){
        String text="";
        text += "-일정 : "+date+" / "+time+"\n";
        text += "-장소 : "+location+"\n";

        TemplateParams params = LocationTemplate.newBuilder(
                address,
                ContentObject.newBuilder(
                        "★"+title+"★",
                        "", LinkObject.newBuilder().build())
                        .setDescrption(text)
                        .build())
                .setAddressTitle(location)
                .addButton(new ButtonObject(
                        "일정 보기",
                        LinkObject.newBuilder()
                                .setWebUrl("http://www.naver.com")
                                .setMobileWebUrl("http://m.naver.com")
                                .build()
                ))
                .build();
        KakaoLinkService.getInstance()
                .sendDefault(context, params, new ResponseCallback<KakaoLinkResponse>() {
                    @Override
                    public void onFailure(ErrorResult errorResult) {
                        Log.e("KAKAO_API", "카카오링크 공유 실패: " + errorResult);
                    }

                    @Override
                    public void onSuccess(KakaoLinkResponse result) {
                        Log.i("KAKAO_API", "카카오링크 공유 성공");

                        // 카카오링크 보내기에 성공했지만 아래 경고 메시지가 존재할 경우 일부 컨텐츠가 정상 동작하지 않을 수 있습니다.
                        Log.w("KAKAO_API", "warning messages: " + result.getWarningMsg());
                        Log.w("KAKAO_API", "argument messages: " + result.getArgumentMsg());
                    }
                });
    }

}
