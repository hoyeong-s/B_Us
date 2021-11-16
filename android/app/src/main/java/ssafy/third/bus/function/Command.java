package ssafy.third.bus.function;

import static ssafy.third.bus.Home.android_id;
import static ssafy.third.bus.Home.arsId;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.util.List;
import java.util.StringTokenizer;

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;

import ssafy.third.bus.Home;
import ssafy.third.bus.URLConnector;
import ssafy.third.bus.URLConnector_post;

public class Command {
    private STT stt;
    TTS tts;
    Context context;
    Komoran komoran;
    int cnt = 0;
    String bus_num = "";
    Handler handler = new Handler();
    static String time;
    static String vehId;
    static String staOrd;
    static Boolean check,checkt = false;

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
            try {
                URLConnector connector = new URLConnector();
                String result = connector.execute("1",arsId).get();
                check = translate(result);
            }catch (Exception e){

            }
            if(cnt==1) {
                if(check) { // 정류장에 해당 버스가 오는 경우
                    if(!checkt)
                        tts.speakOut(bus_num + "번 버스는 "+time+" 예정 입니다" + bus_num + "번 버스를 등록할까요?");

                    else { // 버스는 오나 운행 종료된 경우
                        tts.speakOut(bus_num + "번 버스는 도착 정보가 없습니다 버튼을 다시 눌러 등록해주세요");
                        bus_num = "";
                        cnt = 0;
                        checkt = false;
                        return;
                    }
                    cnt = 2;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getCommand();
                        }
                    }, 6500);
                }else{ // 정류장에 버스가 오지 않는 경우
                    tts.speakOut(bus_num + "번 버스는 현 정류장에 오지 않습니다 버튼을 다시 눌러 등록해주세요");
                    bus_num = "";
                    cnt = 0;
                }
            }
            else if(cnt==2){
                try{
                    URLConnector_post connector = new URLConnector_post();
                    connector.execute(arsId,bus_num,android_id,staOrd,vehId).get();
                }catch (Exception e){
                }

                tts.speakOut(bus_num+"번 버스를 등록하였습니다");
                cnt = 0;
            }
        }
        else if(command.contains("아니오") || command.contains("아니") || command.contains("아니요")){
            tts.speakOut(" 버튼을 다시 눌러 등록해주세요");
            bus_num = "";
            cnt = 0;
        }
   }
    Boolean translate(String result){
        String str = result.split("\"itemList\":")[1];
        StringTokenizer st = new StringTokenizer(str,"{");
        st.nextToken();
        while(st.hasMoreTokens()){
            String line = st.nextToken();
            String [] arr = line.split("\":\"|\",\"");
            if(arr[1].equals(bus_num)){
                time = arr[3];
                staOrd = arr[7];
                vehId = arr[9];
                if(time.equals("도착 정보 없음"))  checkt = true;
                return true; // 정류장에 해당 버스가 오는 경우
            }
        }
        return false; // 오지 않는 경우
    }
}
