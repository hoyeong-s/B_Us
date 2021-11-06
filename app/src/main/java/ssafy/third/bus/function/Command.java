package ssafy.third.bus.function;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;

import ssafy.third.bus.Home;

public class Command {
    private STT stt;
    TTS tts;
    Context context;
    Komoran komoran;
    int cnt = 0;
    String bus_num = "";
    Handler handler = new Handler();

    public static OnEndListeningListener onEndListeningListener = null;

    public void setOnEndListeningListener(OnEndListeningListener listener) {
        onEndListeningListener = listener;
    }

    public Command(Activity activity){
        stt = new STT(activity);
        tts = new TTS();
        context = Home.getAppContext();
        this.komoran = new Komoran(DEFAULT_MODEL.LIGHT);

        setOnEndListeningListener(new OnEndListeningListener() {
            @Override
            public void onEndListening(ListeningEvent listeningEvent) throws InterruptedException {
                executeCommand(stt.sb.toString());
            }
        });
    }

    public void getCommand() {
        stt.startListening();
    }

    public KomoranResult analyzeCommand(String command) {
        return this.komoran.analyze(command);
    }

    public void executeCommand(String command) throws InterruptedException {
        KomoranResult analyzeResult = analyzeCommand(command);
        List<String> token = analyzeResult.getMorphesByTags("SN");
        if(cnt == 0){
            String [] split = command.split("번");
            tts.speakOut(split[0]+"번 버스가 맞습니까?");
            bus_num = split[0];
            cnt = 1;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getCommand();
                }
            },3000);
        }
        if (command.contains("어") || command.contains("그래") || command.contains("네")) {
            if(cnt==1) {
                //TODO
                //API로 버스 시간 받아오기
                tts.speakOut(bus_num+"번 버스는 5분 후에 도착합니다"+bus_num+"번 버스를 등록할까요?");
                cnt = 2;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getCommand();
                    }
                }, 6500);
                //TODO
                //버스가 오지 않는 경우
            }
            else if(cnt==2){
                tts.speakOut(bus_num+"번 버스를 등록하였습니다");
                cnt = 0;
                //TODO
                //버스 등록 메소드
            }
        }
        else if(command.contains("아니오") || command.contains("아니") || command.contains("아니요")){
            tts.speakOut(" 버튼을 다시 눌러 등록해주세요");
            bus_num = "";
            cnt = 0;
        }
   }
}
