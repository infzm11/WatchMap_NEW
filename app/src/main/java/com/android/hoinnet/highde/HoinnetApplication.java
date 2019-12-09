package com.android.hoinnet.highde;

import android.app.Application;
import com.android.hoinnet.highde.utils.CrashHandler;
import com.android.hoinnet.highde.utils.Utils;
import com.iflytek.cloud.Setting;
import com.iflytek.cloud.SpeechUtility;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

public class HoinnetApplication extends Application {
    private static final String TAG = "HoinnetApplication";
    private final String appId = "=59e9b1ff";

    public void onCreate() {
        super.onCreate();
        CrashHandler.getInstance().init(this);
        Logger.addLogAdapter(new AndroidLogAdapter(PrettyFormatStrategy.newBuilder().showThreadInfo(false).methodCount(0).methodOffset(7).tag("Hoinnet-WearMap").build()) {
            public boolean isLoggable(int i, String str) {
                return false;
            }
        });
        Logger.d("开始初始化");
        SpeechUtility.createUtility(this, "appid=59e9b1ff");
        Setting.setShowLog(false);
        Utils.init(this);
        Logger.d("初始化结束");
    }
}
