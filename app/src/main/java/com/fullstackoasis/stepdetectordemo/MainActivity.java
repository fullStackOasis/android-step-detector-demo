package com.fullstackoasis.stepdetectordemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

/**
 * Dead simple implementation of a step counter, not working on performance or good architecture.
 * This is like the StepCounterProject, but it uses a simple step detector sensor, instead of the
 * step counter. In theory, this should work better for something like a pedometer. You want to
 * count individual steps, and not be concerned about steps since boot.
 */
public class MainActivity extends AppCompatActivity {
    private static String TAG = MainActivity.class.getCanonicalName();
    private Handler handler;
    private int interval = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btnDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataStorage.deleteStepsSharedPreferences(MainActivity.this);
                setStepCounterText(0);
            }
        });
        setStepCounterText(DataStorage.getStepsFromSharedPreferences(MainActivity.this));
        handler = new Handler();
        startRepeatingTask();
        Intent i = new Intent(this, StepDetectorService.class);
        startService(i);
    }

    private void updateStepCounterText() {
        setStepCounterText(DataStorage.getStepsFromSharedPreferences(MainActivity.this));
    }

    private void setStepCounterText(int nSteps) {
        TextView tv = findViewById(R.id.tvMessage);
        String template = getResources().getString(R.string.stepping_message);
        String s = String.format(template, nSteps);
        tv.setText(s);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /* you want to stop the service when the activity is destroyed? If so, do this:
        try {
            Intent intent = new Intent(MainActivity.this, StepDetectorService.class);
            stopService(intent);
        } finally {
            // Make sure task stops
            stopRepeatingTask();
        }
        */
        stopRepeatingTask();
    }

    Runnable updater = new Runnable() {
        @Override
        public void run() {
            try {
                updateStepCounterText(); //this function can change value of mInterval.
            } finally {
                // Make sure this happens, even if exception is thrown
                handler.postDelayed(updater, interval);
            }
        }
    };

    void startRepeatingTask() {
        updater.run();
    }

    void stopRepeatingTask() {
        handler.removeCallbacks(updater);
    }
}
