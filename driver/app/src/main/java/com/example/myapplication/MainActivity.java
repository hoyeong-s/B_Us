package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private static Context context;
    private Button btn;
    private Button btn_remove;
    TTS tts;
    Boolean check;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivity.context = getApplicationContext();
        btn = findViewById(R.id.button);
        tts = new TTS();

        // 버튼 애니메이션 생성
        Animation mAnimation = new AlphaAnimation(1, 0);
        mAnimation.setDuration(1000);
        mAnimation.setInterpolator(new LinearInterpolator());
        mAnimation.setRepeatCount(Animation.INFINITE);
        mAnimation.setRepeatMode(Animation.REVERSE);


        btn.startAnimation(mAnimation); // 점등효과 -> 이전 버스정류장에 버스가 도착했을 경우 실행하도록 해야함

        // 전 정류장에 도착 -> 버튼 점등 효과 -> 끌 수 있음
        // 정류장 도착 -> N번 버스 입니다. 5번 내줌


        btn.setOnClickListener(new View.OnClickListener() {
            Handler handler = new Handler();
            public void onClick(View v) {
                btn.clearAnimation();
                StringBuilder sb = new StringBuilder();
                for(int i=0; i<5; i++){
                    sb.append("1111번 버스입니다 ");
                    sb.append(" ");
                    sb.append("\n");
                }
                tts.speakOut(sb.toString());
            }
        });
    }

    public static Context getAppContext(){
        return MainActivity.context;
    }

}