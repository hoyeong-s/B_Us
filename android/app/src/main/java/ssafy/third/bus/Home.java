package ssafy.third.bus;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import ssafy.third.bus.ble.MainActivity;
import ssafy.third.bus.function.STT;
import ssafy.third.bus.function.TTS;

public class Home extends AppCompatActivity {
    private static Context context;
    private TTS tts;
    private STT stt;
    private Button station_info;
    private Button register_speak;
    private TextView station;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Home.context = getApplicationContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tts = new TTS();
        station = findViewById(R.id.station);
        register_speak = findViewById(R.id.register_speak);

        final int PERMISSION = 1;
        if ( Build.VERSION.SDK_INT >= 23 ){
            // 퍼미션 체크
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET,
                    Manifest.permission.RECORD_AUDIO},PERMISSION);
        }


        //***********************************************
        String s = "사당역"; // API로 역 정보 받아와야함 *****
        //***********************************************

        station.setText(s+"입니다.");
        station_info = findViewById(R.id.station_info);


        station_info.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //*********************************************************
                String station_api = "현재 정류장은 사당역이며, 인천 방향입니다. 정류장 번호는 1111입니다.";
                //*********************************************************
                tts.speakOut(station_api);
            }
        });

        register_speak.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

            }
        });
    }

    public static Context getAppContext(){
        return Home.context;
    }

    public void move(View v) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

}