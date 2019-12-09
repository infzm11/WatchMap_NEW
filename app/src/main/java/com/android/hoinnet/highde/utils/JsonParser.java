package com.android.hoinnet.highde.utils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class JsonParser {
    public static String parseIatResult(String str) {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            JSONArray jSONArray = new JSONObject(new JSONTokener(str)).getJSONArray("ws");
            for (int i = 0; i < jSONArray.length(); i++) {
                stringBuffer.append(jSONArray.getJSONObject(i).getJSONArray("cw").getJSONObject(0).getString("w"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuffer.toString();
    }
}
