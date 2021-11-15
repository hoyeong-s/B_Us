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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


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
    private TextView station_text;
    public static String station_name = "";
    public static String arsId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Home.context = getApplicationContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tts = new TTS();
        station_text = findViewById(R.id.station_text);
        register = findViewById(R.id.register);
        register_speak = findViewById(R.id.register_speak);
        alarm = findViewById(R.id.alarm);
        station_info = findViewById(R.id.station_info);
        command = new Command(mainActivity);

        if(station_name.length()==0){
            station_text.setText("버스 정류장 정보가 없습니다");
        }else{
            station_text.setText("현재 정류장은 "+station_name+"입니다");
        }

        final int PERMISSION = 1;
        if ( Build.VERSION.SDK_INT >= 23 ){
            // 퍼미션 체크
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET,
                    Manifest.permission.RECORD_AUDIO},PERMISSION);
        }

        //정류장 정보 버튼
        station_info.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO
                // 비콘에서 arsId 가져와야함
                arsId = "21028";

                try{
                    URLConnector connector = new URLConnector();
                    String result = connector.execute(arsId).get();
                    String [] arr = result.split(",");
                    station_name = arr[0].split("\"")[3];
                }catch (Exception e){
                }

                station_text.setText("현재 정류장은 "+station_name+"입니다"); // 상단 현재 정류장 정보

                String station_api = "현재 정류장은 "+station_name+"이며, 정류장 번호는 "+arsId.charAt(0)+" "+arsId.charAt(1)+" 다시 "+arsId.charAt(2)+" "+arsId.charAt(3)+" "+arsId.charAt(4)+" 입니다"; // TTS로 정보 읽어줌
                tts.speakOut(station_api);

            }
        });

        // 버스 목록 버튼
        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(arsId.length()!=0) {
                    Intent intent = new Intent(getAppContext(), BusList.class);
                    startActivity(intent);
                }else{
                    tts.speakOut("현재 버스 정류장이 아닙니다 등록 후 이용해주세요");
                }
            }
        });

        // 음성 인식 버튼
        Handler handler = new Handler();
        register_speak.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(arsId.length()!=0) {
                    tts.speakOut("탑승하실 버스를 말해주세요");
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            command.getCommand();
                        }
                    },3000);
                }else{
                    tts.speakOut("현재 버스 정류장이 아닙니다 등록 후 이용해주세요");
                }
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