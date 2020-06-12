package com.fullstackoasis.stepdetectordemo;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;

/**
 * This is not intended to be a "bound service".
 * https://developer.android.com/guide/components/services#Types-of-services
 */
public class StepDetectorService extends Service implements SensorEventListener {
    private static String TAG = StepDetectorService.class.getCanonicalName();
    public static final int STEPPED = 1;
    private SensorManager sensorManager;
    private Sensor stepDetectorSensor;
    private StepDetectorService stepDetectorService;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "StepDetectorService.onCreate");
        sensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        stepDetectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        if (stepDetectorSensor != null) {
            sensorManager.registerListener(this, stepDetectorSensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "StepDetectorService.onDestroy");
        if (stepDetectorSensor != null) {
            sensorManager.registerListener(this, stepDetectorSensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
    }
    /**
     * Do not allow binding, always return null.
     * "You must always implement this method; however, if you don't want to allow binding, you
     * should return null."
     * https://developer.android.com/guide/components/services#Basics
     * @param intent
     * @return
     */
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // TODO FIXME may be expensive to record this in SharedPreferences every single step.
        DataStorage.addStepsToSharedPreferences(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO FIXME
    }

}