package com.bonesnetwork.bonebox.redis;

import java.util.Date;

public abstract class TempMessageHandler extends MessageHandler {
    private boolean responded = false;

    public void setResponded(boolean b) {
        responded = b;
    }

    public boolean hasResponded() {
        return responded;
    }

    public void blockUntilResponse() { while (!responded) {} }

    public void blockUntilResponse(int timeoutms) {
        long start = new Date().getTime();
        while (!responded) {
            if ((new Date().getTime()) - start > timeoutms) return;
        }
    }
}
