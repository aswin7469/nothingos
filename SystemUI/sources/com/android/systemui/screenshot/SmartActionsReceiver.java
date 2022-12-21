package com.android.systemui.screenshot;

import android.app.ActivityOptions;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import javax.inject.Inject;

public class SmartActionsReceiver extends BroadcastReceiver {
    private static final String TAG = "SmartActionsReceiver";
    private final ScreenshotSmartActions mScreenshotSmartActions;

    @Inject
    SmartActionsReceiver(ScreenshotSmartActions screenshotSmartActions) {
        this.mScreenshotSmartActions = screenshotSmartActions;
    }

    public void onReceive(Context context, Intent intent) {
        PendingIntent pendingIntent = (PendingIntent) intent.getParcelableExtra("android:screenshot_action_intent");
        String stringExtra = intent.getStringExtra("android:screenshot_action_type");
        try {
            pendingIntent.send(context, 0, (Intent) null, (PendingIntent.OnFinished) null, (Handler) null, (String) null, ActivityOptions.makeBasic().toBundle());
        } catch (PendingIntent.CanceledException e) {
            Log.e(TAG, "Pending intent canceled", e);
        }
        this.mScreenshotSmartActions.notifyScreenshotAction(context, intent.getStringExtra("android:screenshot_id"), stringExtra, true, pendingIntent.getIntent());
    }
}
