package com.android.hoinnet.highde.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;

public class Utils {
    @SuppressLint({"StaticFieldLeak"})
    private static Context context;

    private Utils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static void init(@NonNull Context context2) {
        context = context2.getApplicationContext();
    }

    public static Context getContext() {
        if (context != null) {
            return context;
        }
        throw new NullPointerException("u should init first");
    }
}
