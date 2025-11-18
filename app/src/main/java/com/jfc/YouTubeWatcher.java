package com.jfc;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import java.util.concurrent.atomic.AtomicBoolean;

public class YouTubeWatcher extends AccessibilityService {

    static AtomicBoolean sInYT = new AtomicBoolean(false);

    private static final String TAG = "YouTubeWatcher";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // For now, just log basic event info; we will specialize this for YouTube later.
        if (event == null) {
            return;
        }
        CharSequence pkg = event.getPackageName();
        int type = event.getEventType();
        if (pkg.equals("com.google.android.youtube")) {
            if (!sInYT.get()) {
                Log.d(TAG, "YouTube is active");
                sInYT.set(true);
            }
        } else {
            if (sInYT.get()) {
                Log.d(TAG, "YouTube is *not* active");
                sInYT.set(false);
            }
        }
    }

    @Override
    public void onInterrupt() {
        Log.d(TAG, "onInterrupt");
    }
}
