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
    KakaoSend(Context context){
        this.context = context;
    }

    public void linkMessage(int roomKey){
        TemplateParams params = FeedTemplate
                .newBuilder(ContentObject.newBuilder(
                        "약속정하기!",
                        "http://ttakmanna.com/Android/logo.png",
                        LinkObject.newBuilder()
                                .setWebUrl("")
                                .setMobileWebUrl("")
                                .build())
                        .setDescrption("링크에 접속하여 대답해주세요. 모임 코드는 "+roomKey+"입니다.")
                        .build())
                .addButton(new ButtonObject(
                        "응답 하기",
                        LinkObject.newBuilder()
                                .setWebUrl("http://ttakmanna.com/%eb%aa%a8%ec%9e%84%ec%bd%94%eb%93%9c%ec%9e%85%eb%a0%a5%ec%b0%bd/")
                                .setMobileWebUrl("http://ttakmanna.com/%eb%aa%a8%ec%9e%84%ec%bd%94%eb%93%9c%ec%9e%85%eb%a0%a5%ec%b0%bd/")
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
    public void shareMessage(String title, String participant, String  time, String location){
        String text="";
        //text += "-참가 : "+participant+"\n";
        text += "-일정 : "+time+"\n";
        text += "-장소 : "+location+"\n";

        TemplateParams params = LocationTemplate.newBuilder(
                location,
                ContentObject.newBuilder(
                        "★"+title+"★",
                        "", LinkObject.newBuilder().build())
                        .setDescrption(text)
                        .build())
                .setAddressTitle(location)
                /*.addButton(new ButtonObject(
                        "일정 보기",
                        LinkObject.newBuilder()
                                .setWebUrl("http://www.naver.com")
                                .setMobileWebUrl("http://m.naver.com")
                                .build()
                ))*/
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
