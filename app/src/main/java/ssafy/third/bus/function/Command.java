package ssafy.third.bus.function;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class Command {
    private STT stt;
    TTS tts;
    Context context;

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
        String [] split = command.split("번");
        Log.d("excute",split[0]);
        Toast.makeText(context.getApplicationContext(), split[0], Toast.LENGTH_SHORT).show();
    }


}
