package com.example.benedict.ConnectionApp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.lang.ref.WeakReference;

public class ManifestPermission {

    private static String TAG = ManifestPermission.class.getSimpleName();
    public static final int RECORD_AUDIO = 0;
    private boolean requestGranted;
    private static ManifestPermission manifestPermission;
    private static WeakReference<MainActivity> activityWeakReference;

    public static ManifestPermission newInstance(MainActivity activity) {
        Log.d(TAG,"newInstance()");
        manifestPermission = new ManifestPermission(activity);
        return manifestPermission;
    }

    private ManifestPermission(MainActivity activity) {
        Log.d(TAG,"Constructor");
        activityWeakReference = new WeakReference<MainActivity>(activity);
        requestGranted = false;
    }

    public void setRequestGranted() {
        Log.d(TAG,"setRequestGranted()");
        requestGranted = true;
    }

    public void check() {
        Log.d(TAG,"check()");
        MainActivity activity = activityWeakReference.get();
        if (activity == null || activity.isFinishing()) {
            return;
        }

        if (!hasMicrophone()) {
            activity.disableRecordView();
            return;
        }

        if (!isMicrophonePermissionGranted()) {
            String[] permissions = new String[] {Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            requestPermissions(permissions);
        }

        if(isExternalStorageMounted()) {
            String[] permissions = new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            requestPermissions(permissions);
        }
    }

    private boolean hasMicrophone() {
        Log.d(TAG,"hasMicrophone()");
        MainActivity activity = activityWeakReference.get();
        if (activity == null || activity.isFinishing()) {
            return false;
        }

        PackageManager packageManager = activity.getPackageManager();
        return packageManager.hasSystemFeature(
                PackageManager.FEATURE_MICROPHONE);
    }

    private boolean isMicrophonePermissionGranted() {
        Log.d(TAG,"isMicrophonePermissionGranted()");
        MainActivity activity = activityWeakReference.get();
        if (activity == null || activity.isFinishing()) {
            return false;
        }

        return ActivityCompat
                .checkSelfPermission(
                        activity,
                        Manifest.permission.RECORD_AUDIO
                ) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean isExternalStorageMounted() {
        Log.d(TAG,"isExternalStorageMounted()");
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    private void requestPermissions(String[] permissions) {
        Log.d(TAG,"requestPermissions()");
        MainActivity activity = activityWeakReference.get();
        if (activity == null || activity.isFinishing()) {
            return;
        }
        if (!requestGranted) {
            ActivityCompat.requestPermissions(
                    activity,
                    permissions,
                    RECORD_AUDIO
            );
        }
    }
}
