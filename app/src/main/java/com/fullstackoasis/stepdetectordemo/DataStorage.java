package com.fullstackoasis.stepdetectordemo;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class DataStorage {
    private static String TAG = DataStorage.class.getCanonicalName();
    private static String VERSION_CODE = "VERSION_CODE";
    protected static final String N_STEPS_TAKEN = "N_STEPS_TAKEN";
    protected static final String SHARED_PREFS_NAME = "com.fullstackoasis.stepdetectordemo" +
            ".DataStorage";

    public DataStorage(Context context) {
        /**
         * Immediately test to see if this application's SharedPreferences should be cleared.
         * This is done if there's a new version of the app. See AndroidManifest.xml
         * android:versionCode.
         */
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    0);
            SharedPreferences pref = context.getSharedPreferences(SHARED_PREFS_NAME, 0);
            // For information about versionCode and versioning, see here:
            // https://developer.android.com/studio/publish/versioning
            if (pref.getInt(VERSION_CODE, 0) != pInfo.versionCode) {
                SharedPreferences.Editor edit = pref.edit();
                edit.clear();
                edit.putInt(VERSION_CODE, pInfo.versionCode);
                edit.commit();
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

        }
    }

    /**
     * Stores the fact that one step was taken, which is all the sensor does (detects a single step)
     * @param context the Service used for detecting steps
     */
    protected static void addStepsToSharedPreferences(Context context) {
        int nSteps = getStepsFromSharedPreferences(context);
        SharedPreferences mySharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, 0);
        SharedPreferences.Editor myEditor = mySharedPreferences.edit();
        nSteps++;
        myEditor.putInt(N_STEPS_TAKEN, nSteps);
        myEditor.commit();
    }

    /**
     * Deletes the entry for steps taken from SharedPreferences so we start from 0 steps.
     * @param context the Service used for detecting steps or an Activity.
     */
    protected static void deleteStepsSharedPreferences(Context context) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, 0);
        SharedPreferences.Editor myEditor = mySharedPreferences.edit();
        myEditor.putInt(N_STEPS_TAKEN, 0);
        myEditor.commit();
    }

    /**
     * Returns the number of steps found in SharedPreferences.
     * @return the number of steps counted as found in SharedPreferences.
     */
    protected static int getStepsFromSharedPreferences(Context context) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, 0);
        int result = mySharedPreferences.getInt(N_STEPS_TAKEN, 0);
        return result;
    }

}
