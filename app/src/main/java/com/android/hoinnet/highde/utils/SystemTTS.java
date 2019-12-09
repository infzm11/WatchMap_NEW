package com.android.hoinnet.highde.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import java.util.Locale;

@SuppressLint({"NewApi"})
public class SystemTTS extends UtteranceProgressListener implements TTS, TextToSpeech.OnUtteranceCompletedListener {
    private static SystemTTS singleton;
    ICallBack callBack = null;
    /* access modifiers changed from: private */
    public boolean isSuccess = true;
    private Context mContext;
    /* access modifiers changed from: private */
    public TextToSpeech textToSpeech;

    public void init() {
    }

    public void onDone(String str) {
    }

    public void onError(String str) {
    }

    public void onStart(String str) {
    }

    public void onUtteranceCompleted(String str) {
    }

    public static SystemTTS getInstance(Context context) {
        if (singleton == null) {
            synchronized (SystemTTS.class) {
                if (singleton == null) {
                    singleton = new SystemTTS(context);
                }
            }
        }
        return singleton;
    }

    private SystemTTS(Context context) {
        this.mContext = context.getApplicationContext();
        this.textToSpeech = new TextToSpeech(this.mContext, new TextToSpeech.OnInitListener() {
            public void onInit(int i) {
                if (i == 0) {
                    int language = SystemTTS.this.textToSpeech.setLanguage(Locale.CHINA);
                    SystemTTS.this.textToSpeech.setPitch(1.0f);
                    SystemTTS.this.textToSpeech.setSpeechRate(1.0f);
                    SystemTTS.this.textToSpeech.setOnUtteranceProgressListener(SystemTTS.this);
                    SystemTTS.this.textToSpeech.setOnUtteranceCompletedListener(SystemTTS.this);
                    if (language == -1 || language == -2) {
                        boolean unused = SystemTTS.this.isSuccess = false;
                    }
                }
            }
        });
    }

    public void destroy() {
        stopSpeak();
        if (this.textToSpeech != null) {
            this.textToSpeech.shutdown();
        }
        singleton = null;
    }

    public void playText(String str) {
        if (this.isSuccess && this.textToSpeech != null) {
            this.textToSpeech.speak(str, 1, (Bundle) null, (String) null);
        }
    }

    public void stopSpeak() {
        if (this.textToSpeech != null) {
            this.textToSpeech.stop();
        }
    }

    public boolean isPlaying() {
        return this.textToSpeech.isSpeaking();
    }

    public void setCallback(ICallBack iCallBack) {
        this.callBack = iCallBack;
    }
}
