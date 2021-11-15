package ssafy.third.bus;

import static ssafy.third.bus.Home.arsId;
import static ssafy.third.bus.Home.getAppContext;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

import ssafy.third.bus.function.TTS;

public class BusList extends AppCompatActivity implements BusList_Adapter.OnBtnClickListener{
    ArrayList<String> list = new ArrayList<>();
    BusList_Adapter adapter;
    TTS tts = new TTS();
    FloatingActionButton fab;
    static RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buslist);
         recyclerView = findViewById(R.id.recyclerView);


        try {
            URLConnector connector = new URLConnector();
            String result = connector.execute("1",arsId).get();
            translate(result);
        }catch (Exception e){

        }
        show();

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getAppContext(),Home.class);
                startActivity(intent);
                tts.speakOut("메인페이지로 이동합니다 ");
            }
        });

    }

    @Override
    public void onBtnClick() {
        Handler handler = new Handler();
        
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getAppContext(),Alarm.class);
                startActivity(intent);
            }
        },2500);
    }

    //TODO
    // 갱신 처리
    public void BusCheck() {
        final Timer timer = new Timer();
        final TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    try{
                        list.clear();
                        Log.d("in","in");
                        URLConnector connector = new URLConnector();
                        String result = connector.execute(arsId).get();
                        translate(result);
                        Log.d("list1",Integer.toString(list.size()));
                        show();
                    }catch (Exception e){
                    }
                } catch (Exception e) {

                }
            }
        };
        timer.schedule(task, 0, 30000); // 1분마다 버스 정류장 리스트 받아옴
    }

    void translate(String result){
        String str = result.split("\"itemList\":")[1];
        StringTokenizer st = new StringTokenizer(str,"{");
        st.nextToken();
        while(st.hasMoreTokens()){
            list.add(st.nextToken());
        }
    }

    void show(){
        adapter = new BusList_Adapter(list,this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}
