package ssafy.third.bus;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import com.minew.beacon.BeaconValueIndex;
import com.minew.beacon.BluetoothState;
import com.minew.beacon.MinewBeacon;
import com.minew.beacon.MinewBeaconManager;
import com.minew.beacon.MinewBeaconManagerListener;
import com.minew.beacon.MinewBeaconValue;

import java.util.List;

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
    public static String android_id;

    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Home.context = getApplicationContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        // BLE
        MinewBeaconManager mMinewBeaconManager = MinewBeaconManager.getInstance(this);
        mMinewBeaconManager.setDeviceManagerDelegateListener(new MinewBeaconManagerListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onAppearBeacons(List<MinewBeacon> list) {
                Boolean check = new Boolean(Boolean.FALSE);
//                MinewBeacon minewBeacon = list.get(0);
//                Log.d("minewBeacon", String.valueOf(minewBeacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Major)));
//                MinewBeaconValue beaconMajor = minewBeacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Major);
//                String major = beaconMajor.getStringValue();
                for (MinewBeacon minewBeacon : list) {
                    MinewBeaconValue beaconValue = minewBeacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Major);
                    String stringMajor = beaconValue.getStringValue();
                    int major = Integer.parseInt(stringMajor);
                    if (major == 65432) {
                        MinewBeaconValue beaconMinor = minewBeacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Minor);
                        String minor = beaconMinor.getStringValue();
                        Log.d("minor", minor);
                        if (arsId != minor) {
                            arsId = minor;
                            check = Boolean.TRUE;
                            break;
                        }
                    }
                }


            }

            @Override
            public void onDisappearBeacons(List<MinewBeacon> minewBeacons) {
            }


            @Override
            public void onRangeBeacons(List<MinewBeacon> list) {
            }

            @Override
            public void onUpdateState(BluetoothState bluetoothState) {

            }
        });
        mMinewBeaconManager.startScan();



        // ??????????????? ?????? id
        android_id = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        tts = new TTS();
        station_text = findViewById(R.id.station_text);
        register = findViewById(R.id.register);
        register_speak = findViewById(R.id.register_speak);
        alarm = findViewById(R.id.alarm);
        station_info = findViewById(R.id.station_info);
        command = new Command(mainActivity);

        if(station_name.equals("")){
            station_text.setText("?????? ????????? ????????? ????????????");
        }else{
            station_text.setText("?????? ???????????? "+station_name+"?????????");
        }

        final int PERMISSION = 1;
        if ( Build.VERSION.SDK_INT >= 23 ){
            // ????????? ??????
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET,
                    Manifest.permission.RECORD_AUDIO},PERMISSION);
        }

        //TODO
        // ????????? arsId ??? ????????? ???????????? ?????? (?????????????????? ????????????.)
        //????????? ?????? ??????
        station_info.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


//                mMinewBeaconManager.startScan();
                Log.d("startScan", "startScan");

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //TODO
                // ???????????? arsId ???????????????

                if (arsId.length() == 0) {
                    tts.speakOut("?????? ????????? ????????? ????????????");
                    return;
                }

                try{
                    URLConnector connector = new URLConnector();
                    Log.d("arsId", arsId);
                    String result = connector.execute("1",arsId).get();
                    String [] arr = result.split(",");
                    station_name = arr[0].split("\"")[3];
                }catch (Exception e){
                }

                station_text.setText("?????? ???????????? "+station_name+"?????????"); // ?????? ?????? ????????? ??????

                String station_api = "?????? ???????????? "+station_name+"??????, ????????? ????????? "+arsId.charAt(0)+" "+arsId.charAt(1)+" ?????? "+arsId.charAt(2)+" "+arsId.charAt(3)+" "+arsId.charAt(4)+" ?????????"; // TTS??? ?????? ?????????
                tts.speakOut(station_api);

            }
        });

        // ?????? ?????? ??????
        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(arsId.length()!=0) {
                    Intent intent = new Intent(getAppContext(), BusList.class);
                    startActivity(intent);
                }else{
                    tts.speakOut("?????? ?????? ???????????? ???????????? ?????? ??? ??????????????????");
                }
            }
        });

        // ?????? ?????? ??????
        Handler handler = new Handler();
        register_speak.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(arsId.length()!=0) {
                    tts.speakOut("???????????? ????????? ???????????????");
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            command.getCommand();
                        }
                    },3000);
                }else{
                    tts.speakOut("?????? ?????? ???????????? ???????????? ?????? ??? ??????????????????");
                }
            }
        });

        // ?????? ?????? ??????
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