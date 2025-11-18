package com.jfc;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.atomic.AtomicBoolean;

public class YouTubeWatcher extends AccessibilityService {

    static AtomicBoolean sInYT = new AtomicBoolean(false);

    private static final String TAG = "YouTubeWatcher";

    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private boolean mSkipCheckScheduled = false;

    private final Runnable mSkipCheckRunnable = new Runnable() {
        @Override
        public void run() {
            if (!sInYT.get()) {
                return;
            }
            checkForSkipButton();
            if (sInYT.get()) {
                mHandler.postDelayed(this, 1000);
            }
        }
    };

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // For now, just log basic event info; we will specialize this for YouTube later.
        if (event == null) {
            return;
        }
        CharSequence pkg = event.getPackageName();
        int type = event.getEventType();
        if (pkg != null && pkg.equals("com.google.android.youtube")) {
            if (!sInYT.get()) {
                Log.d(TAG, "YouTube is active");
                sInYT.set(true);
                startSkipChecks();
            }
        } else {
            if (sInYT.get()) {
                Log.d(TAG, "YouTube is *not* active");
                sInYT.set(false);
                stopSkipChecks();
            }
        }
    }

    @Override
    public void onInterrupt() {
        Log.d(TAG, "onInterrupt");
        stopSkipChecks();
    }

    private void startSkipChecks() {
        if (mSkipCheckScheduled) {
            return;
        }
        mSkipCheckScheduled = true;
        mHandler.post(mSkipCheckRunnable);
    }

    private void stopSkipChecks() {
        if (!mSkipCheckScheduled) {
            return;
        }
        mSkipCheckScheduled = false;
        mHandler.removeCallbacks(mSkipCheckRunnable);
    }

    private void checkForSkipButton() {
        AccessibilityNodeInfo root = getRootInActiveWindow();
        if (root == null) {
            return;
        }
        if (findEnabledSkipButton(root)) {
            Log.d(TAG, "Enabled 'Skip' button detected in current window");
        }
    }

    private boolean findEnabledSkipButton(AccessibilityNodeInfo node) {
        if (node == null) {
            return false;
        }

        CharSequence text = node.getText();
        if (text != null) {
            String t = text.toString();
            if (!t.isEmpty() && t.toLowerCase().contains("skip") && node.isEnabled()) {
                Log.d(TAG, "Found enabled Skip button with text: " + t);
                return true;
                // boolean clicked = node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                // Log.d(TAG, "Attempted click on Skip button, success=" + clicked);
                // return clicked;
            }
        }

        int childCount = node.getChildCount();
        for (int i = 0; i < childCount; i++) {
            AccessibilityNodeInfo child = node.getChild(i);
            if (findEnabledSkipButton(child)) {
                if (node.isClickable()) {
                    Log.d(TAG, "Trying to click this node");
                    boolean clicked = node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    Log.d(TAG, "Attempted click on Skip button, success=" + clicked);
                    return false;
                } else {
                    return true;
                }
            }
        }

        return false;
    }
}
