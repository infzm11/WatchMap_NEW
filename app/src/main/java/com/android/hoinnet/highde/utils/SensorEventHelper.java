package com.android.hoinnet.highde.utils;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.WindowManager;
import com.amap.api.maps.model.Marker;

public class SensorEventHelper implements SensorEventListener {
    private final int TIME_SENSOR = 100;
    private long lastTime = 0;
    private float mAngle;
    private Context mContext;
    private Marker mMarker;
    private Sensor mSensor;
    private SensorManager mSensorManager;

    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    public SensorEventHelper(Context context) {
        this.mContext = context;
        this.mSensorManager = (SensorManager) context.getSystemService("sensor");
        this.mSensor = this.mSensorManager.getDefaultSensor(3);
    }

    public void registerSensorListener() {
        this.mSensorManager.registerListener(this, this.mSensor, 3);
    }

    public void unRegisterSensorListener() {
        this.mSensorManager.unregisterListener(this, this.mSensor);
    }

    public void setCurrentMarker(Marker marker) {
        this.mMarker = marker;
    }

    public void onSensorChanged(SensorEvent sensorEvent) {
        if (System.currentTimeMillis() - this.lastTime >= 100 && sensorEvent.sensor.getType() == 3) {
            float screenRotationOnPhone = (sensorEvent.values[0] + ((float) getScreenRotationOnPhone(this.mContext))) % 360.0f;
            if (screenRotationOnPhone > 180.0f) {
                screenRotationOnPhone -= 360.0f;
            } else if (screenRotationOnPhone < -180.0f) {
                screenRotationOnPhone += 360.0f;
            }
            if (Math.abs(this.mAngle - screenRotationOnPhone) >= 3.0f) {
                if (Float.isNaN(screenRotationOnPhone)) {
                    screenRotationOnPhone = 0.0f;
                }
                this.mAngle = screenRotationOnPhone;
                if (this.mMarker != null) {
                    this.mMarker.setRotateAngle(360.0f - this.mAngle);
                }
                this.lastTime = System.currentTimeMillis();
            }
        }
    }

    public static int getScreenRotationOnPhone(Context context) {
        switch (((WindowManager) context.getSystemService("window")).getDefaultDisplay().getRotation()) {
            case 0:
                return 0;
            case 1:
                return 90;
            case 2:
                return 180;
            case 3:
                return -90;
            default:
                return 0;
        }
    }
}
