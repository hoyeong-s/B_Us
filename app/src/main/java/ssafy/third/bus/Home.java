package ssafy.third.bus;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import ssafy.third.bus.function.Command;
import ssafy.third.bus.function.STT;
import ssafy.third.bus.function.TTS;

public class Home extends AppCompatActivity {
    private Activity mainActivity = this;
    private static Context context;
    private TTS tts;
    private STT stt;
    private Command command;
    private Button station_info;
    private Button register;
    private Button register_speak;
    private Button alarm;
    private TextView station;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Home.context = getApplicationContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tts = new TTS();
        station = findViewById(R.id.station);
        register = findViewById(R.id.register);
        register_speak = findViewById(R.id.register_speak);
        alarm = findViewById(R.id.alarm);
        command = new Command(mainActivity);

        final int PERMISSION = 1;
        if ( Build.VERSION.SDK_INT >= 23 ){
            // 퍼미션 체크
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET,
                    Manifest.permission.RECORD_AUDIO},PERMISSION);
        }
        //TODO
        //***********************************************
        String s = "사당역"; // API로 역 정보 받아와야함 *****
        //***********************************************

        station.setText(s+"입니다.");
        station_info = findViewById(R.id.station_info);

        //정류장 정보 버튼
        station_info.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO
                //*********************************************************
                String station_api = "현재 정류장은 사당역이며, 인천 방향입니다. 정류장 번호는 1111입니다.";
                //*********************************************************
                tts.speakOut(station_api);
            }
        });

        // 버스 목록 버튼
        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getAppContext(),BusList.class);
                startActivity(intent);
            }
        });

        // 음성 인식 버튼
        Handler handler = new Handler();
        register_speak.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                tts.speakOut("탑승하실 버스를 말해주세요");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        command.getCommand();
                    }
                },3000);
            }
        });

        // 버스 목록 버튼
        alarm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getAppContext(),Alarm.class);
                startActivity(intent);
            }
        });
    }

    public static Context getAppContext(){
        return Home.context;
    }

}