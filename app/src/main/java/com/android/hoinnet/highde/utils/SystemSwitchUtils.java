package com.android.hoinnet.highde.utils;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.hardware.Camera;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.PowerManager;
import android.provider.Settings;
//import android.support.v4.media.TransportMediator;
import com.amap.api.services.geocoder.GeocodeSearch;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class SystemSwitchUtils {
    private static final int LIGHT_100_PERCENT = 255;
    private static final int LIGHT_50_PERCENT = 127;
    private static final int LIGHT_75_PERCENT = 191;
    private static final int LIGHT_AUTO = 0;
    private static final int LIGHT_ERR = -1;
    private static final int LIGHT_NORMAL = 64;

    private SystemSwitchUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    @SuppressLint({"WifiManagerLeak"})
    public static boolean isWifiOn() {
        return ((WifiManager) Utils.getContext().getSystemService(Context.WIFI_SERVICE)).isWifiEnabled();
    }

    @SuppressLint({"WifiManagerLeak"})
    public void wifiUtils() {
        if (isWifiOn()) {
            ((WifiManager) Utils.getContext().getSystemService(Context.WIFI_SERVICE)).setWifiEnabled(false);
        } else {
            ((WifiManager) Utils.getContext().getSystemService(Context.WIFI_SERVICE)).setWifiEnabled(true);
        }
    }

    public static boolean isBlueToothOn() {
        switch (BluetoothAdapter.getDefaultAdapter().getState()) {
            case 10:
                return false;
            case 11:
                return true;
            case 12:
                return true;
            case 13:
                return false;
            default:
                return false;
        }
    }

    public static void bluetoothUtils() {
        if (isBlueToothOn()) {
            BluetoothAdapter.getDefaultAdapter().disable();
        } else {
            BluetoothAdapter.getDefaultAdapter().enable();
        }
    }

    public boolean isRotationOn() {
        int i;
        try {
            i = Settings.System.getInt(Utils.getContext().getContentResolver(), "accelerometer_rotation");
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            i = 0;
        }
        if (i == 0) {
            return false;
        }
        return true;
    }

    public void rotationUtils() {
        Uri uriFor = Settings.System.getUriFor("accelerometer_rotation");
        Settings.System.putInt(Utils.getContext().getContentResolver(), "accelerometer_rotation", !isRotationOn() ? 1 : 0);
        Utils.getContext().getContentResolver().notifyChange(uriFor, (ContentObserver) null);
    }

    public boolean isSyncSwitchOn() {
        return ((ConnectivityManager) Utils.getContext().getSystemService(Context.CONNECTIVITY_SERVICE)).getBackgroundDataSetting() && ContentResolver.getMasterSyncAutomatically();
    }

    public void syncSwitchUtils() {
        ContentResolver.setMasterSyncAutomatically(!isSyncSwitchOn());
    }

    public static void brightnessSwitchUtils() {
        int i;
        ContentResolver contentResolver = Utils.getContext().getContentResolver();
        try {
            if (!(Settings.System.getInt(contentResolver, "screen_brightness_mode") == 1)) {
                int i2 = Settings.System.getInt(contentResolver, "screen_brightness", -1);
                i = (i2 <= 0 || i2 > 64) ? (i2 <= 64 || i2 > 127) ? (i2 <= 127 || i2 > LIGHT_75_PERCENT) ? 255 : LIGHT_75_PERCENT : 127 : 64;
            } else {
                i = 0;
            }
            if (i == 64) {
                i = 130;//TransportMediator.KEYCODE_MEDIA_PLAY;
            } else if (i == 127) {
                i = 190;
            } else if (i != LIGHT_75_PERCENT) {
                if (i != 255) {
                    switch (i) {
                        case -1:
                            break;
                        case 0:
                            Settings.System.putInt(contentResolver, "screen_brightness_mode", 0);
                            break;
                    }
                } else {
                    Settings.System.putInt(contentResolver, "screen_brightness_mode", 1);
                }
                i = 63;
            } else {
                i = 254;
            }
            setLight(i);
            Settings.System.putInt(contentResolver, "screen_brightness", i);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void setLight(int i) {
        try {
            PowerManager powerManager = (PowerManager) Utils.getContext().getSystemService(Context.POWER_SERVICE);
            Field declaredField = Class.forName(powerManager.getClass().getName()).getDeclaredField("mService");
            declaredField.setAccessible(true);
            Object obj = declaredField.get(powerManager);
            Method declaredMethod = Class.forName(obj.getClass().getName()).getDeclaredMethod("setBacklightBrightness", new Class[]{Integer.TYPE});
            declaredMethod.setAccessible(true);
            declaredMethod.invoke(obj, new Object[]{Integer.valueOf(i)});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isAirplaneModeOn() {
        return Settings.System.getInt(Utils.getContext().getContentResolver(), "airplane_mode_on", 0) != 0;
    }

    public static void airplaneModeSwitchUtils() {
        boolean isAirplaneModeOn = isAirplaneModeOn();
        Settings.System.putInt(Utils.getContext().getContentResolver(), "airplane_mode_on", isAirplaneModeOn ^ true ? 1 : 0);
        Intent intent = new Intent("android.intent.action.AIRPLANE_MODE");
        intent.putExtra("state", !isAirplaneModeOn);
        Utils.getContext().sendBroadcast(intent);
    }

    public static boolean isMobileDataOn() {
        Boolean bool;
        Context context = Utils.getContext();
        Utils.getContext();
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            bool = (Boolean) connectivityManager.getClass().getMethod("getMobileDataEnabled", new Class[0]).invoke(connectivityManager, new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
            bool = false;
        }
        return bool.booleanValue();
    }

    public void MobileDataSwitchUtils() {
        Context context = Utils.getContext();
        Utils.getContext();
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            connectivityManager.getClass().getMethod("getMobileDataEnabled", new Class[0]);
            Field declaredField = Class.forName(connectivityManager.getClass().getName()).getDeclaredField("mService");
            declaredField.setAccessible(true);
            Object obj = declaredField.get(connectivityManager);
            Method declaredMethod = Class.forName(obj.getClass().getName()).getDeclaredMethod("setMobileDataEnabled", new Class[]{Boolean.TYPE});
            declaredMethod.setAccessible(true);
            declaredMethod.invoke(obj, new Object[]{Boolean.valueOf(true ^ isMobileDataOn())});
        } catch (Exception unused) {
        }
    }

    public static boolean isRingerModeSilent() {
        return ((AudioManager) Utils.getContext().getSystemService(Context.AUDIO_SERVICE)).getRingerMode() == 0;
    }

    public static void silentSwitchUtil() {
        AudioManager audioManager = (AudioManager) Utils.getContext().getSystemService(Context.AUDIO_SERVICE);
        int ringerMode = audioManager.getRingerMode();
        if (ringerMode == 0) {
            audioManager.setRingerMode(2);
        } else if (ringerMode == 2) {
            audioManager.setRingerMode(0);
        }
    }

    public static void silentSwitchUtils() {
        AudioManager audioManager = (AudioManager) Utils.getContext().getSystemService(Context.AUDIO_SERVICE);
        switch (audioManager.getRingerMode()) {
            case 0:
                audioManager.setRingerMode(1);
                return;
            case 1:
                audioManager.setRingerMode(2);
                return;
            case 2:
                audioManager.setRingerMode(0);
                return;
            default:
                return;
        }
    }

    public static boolean isGpsSwitchOn() {
        return Settings.Secure.isLocationProviderEnabled(Utils.getContext().getContentResolver(), GeocodeSearch.GPS);
    }

    public static void GpsSwitchUtils() {
        Settings.Secure.setLocationProviderEnabled(Utils.getContext().getContentResolver(), GeocodeSearch.GPS, !isGpsSwitchOn());
    }

    public static void rebootUtils() {
        ((PowerManager) Utils.getContext().getSystemService(Context.POWER_SERVICE)).reboot((String) null);
    }

    public static void shutDownSwitchUtils() {
        Intent intent = new Intent("android.intent.action.ACTION_REQUEST_SHUTDOWN");
        intent.putExtra("android.intent.extra.KEY_CONFIRM", false);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Utils.getContext().startActivity(intent);
    }

    public static boolean isFlashlightOn() {
        return Camera.open().getParameters().getFlashMode().equals("torch");
    }

    public static void flashlightUtils() {
        Camera open = Camera.open();
        Camera.Parameters parameters = open.getParameters();
        if (isFlashlightOn()) {
            parameters.setFlashMode("off");
            open.setParameters(parameters);
            open.release();
            return;
        }
        parameters.setFlashMode("torch");
        open.setParameters(parameters);
    }

    public static void flashUtils() {
        Camera open = Camera.open();
        Camera.Parameters parameters = open.getParameters();
        if (parameters.getFlashMode().equals("torch")) {
            open.stopPreview();
            open.release();
            return;
        }
        parameters.setFlashMode("torch");
        open.setParameters(parameters);
        open.autoFocus(new Camera.AutoFocusCallback() {
            public void onAutoFocus(boolean z, Camera camera) {
            }
        });
        open.startPreview();
    }

    public static void systemSetUtils() {
        Intent intent = new Intent("android.settings.SETTINGS");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Utils.getContext().startActivity(intent);
    }

    public static void systemAppsUtils() {
        Intent intent = new Intent("android.settings.APPLICATION_SETTINGS");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Utils.getContext().startActivity(intent);
    }

    public static void systemDisplayUtils() {
        Intent intent = new Intent("android.settings.DISPLAY_SETTINGS");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Utils.getContext().startActivity(intent);
    }

    public static void systemSoundUtils() {
        Intent intent = new Intent("android.settings.SOUND_SETTINGS");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Utils.getContext().startActivity(intent);
    }

    public static void systemDateUtils() {
        Intent intent = new Intent("android.settings.DATE_SETTINGS");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Utils.getContext().startActivity(intent);
    }

    public static void systemLocationUtils() {
        Intent intent = new Intent("android.settings.LOCATION_SOURCE_SETTINGS");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Utils.getContext().startActivity(intent);
    }

    public void systemSyncUtils() {
        Intent intent = new Intent("android.settings.SYNC_SETTINGS");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Utils.getContext().startActivity(intent);
    }

    public static void systemInputUtils() {
        Intent intent = new Intent("android.settings.INPUT_METHOD_SETTINGS");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Utils.getContext().startActivity(intent);
    }

    public static void setMusicAudio() {
        ((AudioManager) Utils.getContext().getSystemService(Context.POWER_SERVICE)).adjustStreamVolume(3, 1, 1);
    }
}
