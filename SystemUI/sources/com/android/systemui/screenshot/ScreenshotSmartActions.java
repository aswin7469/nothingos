package com.android.systemui.screenshot;

import android.app.ActivityManager;
import android.app.Notification;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.SystemClock;
import android.os.UserHandle;
import android.util.Log;
import com.android.systemui.SystemUIFactory;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.screenshot.ScreenshotNotificationSmartActionsProvider;
import com.android.systemui.shared.system.ActivityManagerWrapper;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@SysUISingleton
public class ScreenshotSmartActions {
    private static final String TAG = LogConfig.logTag(ScreenshotSmartActions.class);

    /* access modifiers changed from: package-private */
    public CompletableFuture<List<Notification.Action>> getSmartActionsFuture(String str, Uri uri, Bitmap bitmap, ScreenshotNotificationSmartActionsProvider screenshotNotificationSmartActionsProvider, ScreenshotNotificationSmartActionsProvider.ScreenshotSmartActionType screenshotSmartActionType, boolean z, UserHandle userHandle) {
        ComponentName componentName;
        if (!z) {
            return CompletableFuture.completedFuture(Collections.emptyList());
        }
        if (bitmap.getConfig() != Bitmap.Config.HARDWARE) {
            return CompletableFuture.completedFuture(Collections.emptyList());
        }
        long uptimeMillis = SystemClock.uptimeMillis();
        try {
            ActivityManager.RunningTaskInfo runningTask = ActivityManagerWrapper.getInstance().getRunningTask();
            if (runningTask == null || runningTask.topActivity == null) {
                componentName = new ComponentName("", "");
            } else {
                componentName = runningTask.topActivity;
            }
            return screenshotNotificationSmartActionsProvider.getActions(str, uri, bitmap, componentName, screenshotSmartActionType, userHandle);
        } catch (Throwable unused) {
            CompletableFuture<List<Notification.Action>> completedFuture = CompletableFuture.completedFuture(Collections.emptyList());
            String str2 = str;
            ScreenshotNotificationSmartActionsProvider screenshotNotificationSmartActionsProvider2 = screenshotNotificationSmartActionsProvider;
            notifyScreenshotOp(str2, screenshotNotificationSmartActionsProvider2, ScreenshotNotificationSmartActionsProvider.ScreenshotOp.REQUEST_SMART_ACTIONS, ScreenshotNotificationSmartActionsProvider.ScreenshotOpStatus.ERROR, SystemClock.uptimeMillis() - uptimeMillis);
            return completedFuture;
        }
    }

    /* access modifiers changed from: package-private */
    public List<Notification.Action> getSmartActions(String str, CompletableFuture<List<Notification.Action>> completableFuture, int i, ScreenshotNotificationSmartActionsProvider screenshotNotificationSmartActionsProvider, ScreenshotNotificationSmartActionsProvider.ScreenshotSmartActionType screenshotSmartActionType) {
        ScreenshotNotificationSmartActionsProvider.ScreenshotOpStatus screenshotOpStatus;
        long uptimeMillis = SystemClock.uptimeMillis();
        try {
            CompletableFuture<List<Notification.Action>> completableFuture2 = completableFuture;
            List<Notification.Action> list = completableFuture.get((long) i, TimeUnit.MILLISECONDS);
            String str2 = str;
            ScreenshotNotificationSmartActionsProvider screenshotNotificationSmartActionsProvider2 = screenshotNotificationSmartActionsProvider;
            notifyScreenshotOp(str2, screenshotNotificationSmartActionsProvider2, ScreenshotNotificationSmartActionsProvider.ScreenshotOp.WAIT_FOR_SMART_ACTIONS, ScreenshotNotificationSmartActionsProvider.ScreenshotOpStatus.SUCCESS, SystemClock.uptimeMillis() - uptimeMillis);
            return list;
        } catch (Throwable th) {
            long uptimeMillis2 = SystemClock.uptimeMillis() - uptimeMillis;
            if (th instanceof TimeoutException) {
                screenshotOpStatus = ScreenshotNotificationSmartActionsProvider.ScreenshotOpStatus.TIMEOUT;
            } else {
                screenshotOpStatus = ScreenshotNotificationSmartActionsProvider.ScreenshotOpStatus.ERROR;
            }
            String str3 = str;
            ScreenshotNotificationSmartActionsProvider screenshotNotificationSmartActionsProvider3 = screenshotNotificationSmartActionsProvider;
            notifyScreenshotOp(str3, screenshotNotificationSmartActionsProvider3, ScreenshotNotificationSmartActionsProvider.ScreenshotOp.WAIT_FOR_SMART_ACTIONS, screenshotOpStatus, uptimeMillis2);
            return Collections.emptyList();
        }
    }

    /* access modifiers changed from: package-private */
    public void notifyScreenshotOp(String str, ScreenshotNotificationSmartActionsProvider screenshotNotificationSmartActionsProvider, ScreenshotNotificationSmartActionsProvider.ScreenshotOp screenshotOp, ScreenshotNotificationSmartActionsProvider.ScreenshotOpStatus screenshotOpStatus, long j) {
        try {
            screenshotNotificationSmartActionsProvider.notifyOp(str, screenshotOp, screenshotOpStatus, j);
        } catch (Throwable th) {
            Log.e(TAG, "Error in notifyScreenshotOp: ", th);
        }
    }

    /* access modifiers changed from: package-private */
    public void notifyScreenshotAction(Context context, String str, String str2, boolean z, Intent intent) {
        try {
            SystemUIFactory.getInstance().createScreenshotNotificationSmartActionsProvider(context, AsyncTask.THREAD_POOL_EXECUTOR, new Handler()).notifyAction(str, str2, z, intent);
        } catch (Throwable th) {
            Log.e(TAG, "Error in notifyScreenshotAction: ", th);
        }
    }
}
