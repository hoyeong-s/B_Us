package ssafy.third.bus;

import static ssafy.third.bus.Home.android_id;
import static ssafy.third.bus.Home.arsId;
import static ssafy.third.bus.Home.getAppContext;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

import ssafy.third.bus.function.TTS;

public class Alarm extends AppCompatActivity implements Alarm_Adapter.OnBtnClickListener{
    //TODO
    //String 외의 다른 형으로 받을 경우 Alarm_Adapter도 수정해줘야함
    ArrayList<String> list;
    Alarm_Adapter adapter;
    RecyclerView recyclerView;
    TextView emptyView;
    FloatingActionButton fab;
    TTS tts = new TTS();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        recyclerView = findViewById(R.id.recyclerView);
        emptyView = findViewById(R.id.empty_view);
        fab = findViewById(R.id.fab);
        list = new ArrayList<>();

        try {
            URLConnector connector = new URLConnector();
            String result = connector.execute("2", android_id).get();
            translate(result);
        } catch (Exception e) {

        }

        adapter = new Alarm_Adapter(list,this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        onchanged();

        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getAppContext(),Home.class);
                startActivity(intent);
                tts.speakOut("메인페이지로 이동합니다");
            }
        });

        final Timer timer = new Timer();
        final TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    URLConnector connector = new URLConnector();
                    String result = connector.execute("2", android_id).get();
                    list.clear();
                    translate(result);
                } catch (Exception e) {

                }
                Message msg = handler.obtainMessage();
                handler.sendMessage(msg);
            }
        };
        timer.schedule(task, 0, 10000);
    }

    public void onchanged() {
        if(list.size()==0){
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }else{
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDeleteBtnClick(int position) {
        list.remove(position);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.notifyItemRemoved(position);
                onchanged();
            }
        },2000);

    }

    void translate(String result){
        StringTokenizer st = new StringTokenizer(result,"}|]");
        while(st.hasMoreTokens()){
            list.add(st.nextToken());
        }
    }

    final Handler handler = new Handler(){
        public void handleMessage(Message msg){
            adapter.updateList(list);
        }
    };

}
