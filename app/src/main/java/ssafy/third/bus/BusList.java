package ssafy.third.bus;

import static ssafy.third.bus.Home.getAppContext;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import ssafy.third.bus.function.TTS;

public class BusList extends AppCompatActivity implements BusList_Adapter.OnBtnClickListener{
    ArrayList<String> list;
    BusList_Adapter adapter;
    TTS tts = new TTS();
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buslist);

        list = new ArrayList<>();
        list.add("100번");
        list.add("101번");
        list.add("102번");
        list.add("103번");
        list.add("103번");
        list.add("103번");
        list.add("103번");
        list.add("103번");
        list.add("103번");
        list.add("103번");
        list.add("103번");
        list.add("103번");
        list.add("103번");
        list.add("103번");
        list.add("103번");
        list.add("103번");
        list.add("103번");



        adapter = new BusList_Adapter(list,this);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

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
        Intent intent = new Intent(getAppContext(),Alarm.class);
        startActivity(intent);
    }
}
