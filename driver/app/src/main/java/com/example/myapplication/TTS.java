package com.example.myapplication;

import android.app.Activity;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import androidx.annotation.RequiresApi;
import java.util.Locale;

public class TTS extends Activity implements TextToSpeech.OnInitListener{
    TextToSpeech tts;

    public TTS(){
        tts = new TextToSpeech(MainActivity.getAppContext(),this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void speakOut(String station_api) {
        tts.speak(station_api, TextToSpeech.QUEUE_FLUSH,null,"id1");
    }

    public void onDestroy() {
        if (tts != null)  {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS)  {
            int result = tts.setLanguage(Locale.KOREA);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            }
        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }
}
