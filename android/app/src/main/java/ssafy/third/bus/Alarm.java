package ssafy.third.bus;

import static ssafy.third.bus.Home.getAppContext;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

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

        list = new ArrayList<>();
        list.add("100번");
        list.add("101번");
        list.add("100번");
        list.add("101번");
        list.add("100번");


        adapter = new Alarm_Adapter(list,this);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(adapter);
        emptyView = findViewById(R.id.empty_view);
        fab = findViewById(R.id.fab);

        onchanged();

        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getAppContext(),Home.class);
                startActivity(intent);
                tts.speakOut("메인페이지로 이동합니다");
            }
        });
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
        adapter.notifyItemRemoved(position);
        onchanged();
    }
}
