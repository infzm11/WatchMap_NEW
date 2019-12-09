package com.android.hoinnet.highde.utils;

import android.content.Context;
import android.content.pm.PackageManager;

public class ContextUtils {
    public static final String[] authBaseArr = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.ACCESS_FINE_LOCATION"};
    public static final String[] authComArr = {"android.permission.READ_PHONE_STATE"};
    private static long lastClickTime;

    public static boolean hasBasePhoneAuth(Context context) {
        PackageManager packageManager = context.getPackageManager();
        for (String checkPermission : authBaseArr) {
            if (packageManager.checkPermission(checkPermission, context.getPackageName()) != 0) {
                return false;
            }
        }
        return true;
    }

    public static boolean hasCompletePhoneAuth(Context context) {
        PackageManager packageManager = context.getPackageManager();
        for (String checkPermission : authComArr) {
            if (packageManager.checkPermission(checkPermission, context.getPackageName()) != 0) {
                return false;
            }
        }
        return true;
    }

    public static boolean isFastClick(int i) {
        long currentTimeMillis = System.currentTimeMillis();
        long j = currentTimeMillis - lastClickTime;
        if (0 < j && j < ((long) i)) {
            return true;
        }
        lastClickTime = currentTimeMillis;
        return false;
    }
}
