package com.android.hoinnet.highde.utils;

public interface TTS {
    void destroy();

    void init();

    boolean isPlaying();

    void playText(String str);

    void setCallback(ICallBack iCallBack);

    void stopSpeak();
}
