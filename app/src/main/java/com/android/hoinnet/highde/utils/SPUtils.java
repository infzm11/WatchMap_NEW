package com.android.hoinnet.highde.utils;

import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.collection.SimpleArrayMap;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public final class SPUtils {
    private static SimpleArrayMap<String, SPUtils> SP_UTILS_MAP = new SimpleArrayMap<>();
    private SharedPreferences sp;

    public static SPUtils getInstance() {
        return getInstance("");
    }

    public static SPUtils getInstance(String str) {
        if (isSpace(str)) {
            str = "spName";
        }
        SPUtils sPUtils = SP_UTILS_MAP.get(str);
        if (sPUtils != null) {
            return sPUtils;
        }
        SPUtils sPUtils2 = new SPUtils(str);
        SP_UTILS_MAP.put(str, sPUtils2);
        return sPUtils2;
    }

    private SPUtils(String str) {
        this.sp = Utils.getContext().getSharedPreferences(str, 0);
    }

    public void put(@NonNull String str, @NonNull String str2) {
        this.sp.edit().putString(str, str2).apply();
    }

    public String getString(@NonNull String str) {
        return getString(str, "");
    }

    public String getString(@NonNull String str, @NonNull String str2) {
        return this.sp.getString(str, str2);
    }

    public void put(@NonNull String str, int i) {
        this.sp.edit().putInt(str, i).apply();
    }

    public int getInt(@NonNull String str) {
        return getInt(str, -1);
    }

    public int getInt(@NonNull String str, int i) {
        return this.sp.getInt(str, i);
    }

    public void put(@NonNull String str, long j) {
        this.sp.edit().putLong(str, j).apply();
    }

    public long getLong(@NonNull String str) {
        return getLong(str, -1);
    }

    public long getLong(@NonNull String str, long j) {
        return this.sp.getLong(str, j);
    }

    public void put(@NonNull String str, float f) {
        this.sp.edit().putFloat(str, f).apply();
    }

    public float getFloat(@NonNull String str) {
        return getFloat(str, -1.0f);
    }

    public float getFloat(@NonNull String str, float f) {
        return this.sp.getFloat(str, f);
    }

    public void put(@NonNull String str, boolean z) {
        this.sp.edit().putBoolean(str, z).apply();
    }

    public boolean getBoolean(@NonNull String str) {
        return getBoolean(str, false);
    }

    public boolean getBoolean(@NonNull String str, boolean z) {
        return this.sp.getBoolean(str, z);
    }

    public void put(@NonNull String str, @NonNull Set<String> set) {
        this.sp.edit().putStringSet(str, set).apply();
    }

    public Set<String> getStringSet(@NonNull String str) {
        return getStringSet(str, Collections.<String>emptySet());
    }

    public Set<String> getStringSet(@NonNull String str, @NonNull Set<String> set) {
        return this.sp.getStringSet(str, set);
    }

    public Map<String, ?> getAll() {
        return this.sp.getAll();
    }

    public boolean contains(@NonNull String str) {
        return this.sp.contains(str);
    }

    public void remove(@NonNull String str) {
        this.sp.edit().remove(str).apply();
    }

    public void clear() {
        this.sp.edit().clear().apply();
    }

    private static boolean isSpace(String str) {
        if (str == null) {
            return true;
        }
        int length = str.length();
        for (int i = 0; i < length; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
