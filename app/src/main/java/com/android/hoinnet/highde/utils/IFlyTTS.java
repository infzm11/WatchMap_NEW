package com.android.hoinnet.highde.utils;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
/*import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;

public class IFlyTTS implements TTS, SynthesizerListener, AudioManager.OnAudioFocusChangeListener {
    private static IFlyTTS iflyTTS;
    ICallBack callBack = null;
    private boolean isPlaying = false;
    private AudioManager mAm = null;
    Context mContext = null;
    private SpeechSynthesizer mTts;

    public void onAudioFocusChange(int i) {
    }

    public void onBufferProgress(int i, int i2, int i3, String str) {
    }

    public void onEvent(int i, int i2, int i3, Bundle bundle) {
    }

    public void onSpeakProgress(int i, int i2, int i3) {
    }

    public void onSpeakResumed() {
    }

    public static IFlyTTS getInstance(Context context) {
        if (iflyTTS == null) {
            iflyTTS = new IFlyTTS(context);
        }
        return iflyTTS;
    }

    private IFlyTTS(Context context) {
        this.mContext = context;
        createSynthesizer();
        this.mAm = (AudioManager) this.mContext.getSystemService("audio");
    }

    public void init() {
        this.mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");
        this.mTts.setParameter(SpeechConstant.SPEED, "55");
        this.mTts.setParameter(SpeechConstant.VOLUME, "tts_volume");
        this.mTts.setParameter(SpeechConstant.PITCH, "tts_pitch");
        this.mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "false");
        this.mTts.setParameter(SpeechConstant.VOICE_NAME, "vixy");
    }

    private void createSynthesizer() {
        this.mTts = SpeechSynthesizer.createSynthesizer(this.mContext, new InitListener() {
            public void onInit(int i) {
            }
        });
    }

    public void playText(String str) {
        if (str != null && str.contains("京藏")) {
            str = str.replace("京藏", "京藏[=zang4]");
        }
        if (str != null && str.length() > 0 && this.mAm.requestAudioFocus(this, 3, 3) == 1) {
            this.mTts.startSpeaking(str, this);
            this.isPlaying = true;
        }
    }

    public void stopSpeak() {
        if (this.mTts != null) {
            this.mTts.stopSpeaking();
        }
        this.isPlaying = false;
    }

    public void destroy() {
        stopSpeak();
        if (this.mTts != null) {
            this.mTts.destroy();
        }
        iflyTTS = null;
    }

    public void onCompleted(SpeechError speechError) {
        this.isPlaying = false;
        if (this.mAm != null) {
            this.mAm.abandonAudioFocus(this);
        }
        if (this.callBack != null && speechError == null) {
            this.callBack.onCompleted(0);
        }
    }

    public void onSpeakBegin() {
        this.isPlaying = true;
    }

    public void onSpeakPaused() {
        this.isPlaying = false;
    }

    public boolean isPlaying() {
        return this.isPlaying;
    }

    public void setCallback(ICallBack iCallBack) {
        this.callBack = iCallBack;
    }
}*/
