package ssafy.third.bus.function;

import java.util.EventListener;

public interface OnEndListeningListener extends EventListener {
    void onEndListening(ListeningEvent listeningEvent) throws InterruptedException;
}