package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private static Context context;
    private final Handler handler = new Handler();
    private Button btn;
    private TextView tv;
    private TextView tv2;
    private String Bus = "504";
    private String busroute = "100100410";
    private String vehId = "121047041";

    TTS tts;
    //6e859a5c069a564b

    private static String next_present = " ";
    private static String next = " ";
    static boolean Present = false;
    static boolean check = false;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivity.context = getApplicationContext();
        btn = findViewById(R.id.button);
        tv = findViewById(R.id.textView3);
        tv2 = findViewById(R.id.textView2);
        tts = new TTS();

        tv2.setText(Bus);

        // 버튼 애니메이션 생성
        Animation mAnimation = new AlphaAnimation(1, 0);
        mAnimation.setDuration(1000);
        mAnimation.setInterpolator(new LinearInterpolator());
        mAnimation.setRepeatCount(Animation.INFINITE);
        mAnimation.setRepeatMode(Animation.REVERSE);

        final Timer timer = new Timer();
        final TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    URLConnector connector = new URLConnector();
                    String result = connector.execute(vehId,busroute).get();
                    Log.d("result",result);
                    result = result.replace("}","");
                    String [] arr = result.split("\":\"|\",\"|\":");

                    update(arr[5]);

                    if(!next.equals(arr[1])){
                        check = false;
                        next = arr[1];
                    }

                    Log.d("boolean",arr[7]);
                    Log.d("next_station",next_present);

                    if(next_present.equals(arr[5])) btn.clearAnimation();

                    if(next_present.equals(arr[5])) {
                        next_present = " ";
                        speak(); // 현 정류장에 교통 약자 있는 경우 음성
                    }


                    Present = Boolean.parseBoolean(arr[7]); // 다음 버스 정류장에 교통 약자가 있는 경우
                    if(Present) {
                        next_present = arr[1];
                        if(!check) btn.startAnimation(mAnimation); // 점등효과 -> 이전 버스정류장에 버스가 도착했을 경우 실행하도록 해야함
                    }
                } catch (Exception e) {
                }
            }
        };
        timer.schedule(task, 0, 30000);

        // 전 정류장에 도착 -> 버튼 점등 효과 -> 끌 수 있음
        // 정류장 도착 -> N번 버스 입니다. 5번 내줌

        btn.setOnClickListener(new View.OnClickListener() {
            Handler handler = new Handler();
            public void onClick(View v) {
                check = true;
                btn.clearAnimation();
            }
        });
    }

    public static Context getAppContext(){
        return MainActivity.context;
    }

    void update(String now){
        Runnable updater = new Runnable() {
            public void run() {
                tv.setText(now);
            }
        };
        handler.post(updater);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void speak(){
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<4; i++){
            sb.append(Bus+"번 버스입니다 ");
            sb.append(" ");
            sb.append("\n");
        }
        tts.speakOut(sb.toString());
    }
}