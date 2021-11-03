package ssafy.third.bus.function;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;

import java.util.List;

public class Command {
    private STT stt;
    TTS tts;

    public static OnEndListeningListener onEndListeningListener = null;

    public void setOnEndListeningListener(OnEndListeningListener listener) {
        onEndListeningListener = listener;
    }

    public Command(Activity activity){
        stt = new STT(activity);
        tts = new TTS();

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

    public void executeCommand(String command) throws InterruptedException {

    }


}
