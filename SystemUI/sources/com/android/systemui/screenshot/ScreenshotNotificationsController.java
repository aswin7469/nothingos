package com.android.systemui.screenshot;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.UserHandle;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import androidx.core.app.NotificationCompat;
import com.android.systemui.C1893R;
import com.android.systemui.SystemUIApplication;
import com.android.systemui.util.NotificationChannels;
import javax.inject.Inject;

public class ScreenshotNotificationsController {
    private static final String TAG = "ScreenshotNotificationManager";
    private final Context mContext;
    private final NotificationManager mNotificationManager;
    private final Resources mResources;

    @Inject
    ScreenshotNotificationsController(Context context, WindowManager windowManager) {
        this.mContext = context;
        this.mResources = context.getResources();
        this.mNotificationManager = (NotificationManager) context.getSystemService("notification");
        windowManager.getDefaultDisplay().getRealMetrics(new DisplayMetrics());
    }

    public void notifyScreenshotError(int i) {
        Resources resources = this.mContext.getResources();
        String string = resources.getString(i);
        Notification.Builder color = new Notification.Builder(this.mContext, NotificationChannels.ALERTS).setTicker(resources.getString(C1893R.string.screenshot_failed_title)).setContentTitle(resources.getString(C1893R.string.screenshot_failed_title)).setContentText(string).setSmallIcon(C1893R.C1895drawable.stat_notify_image_error).setWhen(System.currentTimeMillis()).setVisibility(1).setCategory(NotificationCompat.CATEGORY_ERROR).setAutoCancel(true).setColor(this.mContext.getColor(17170460));
        Intent createAdminSupportIntent = ((DevicePolicyManager) this.mContext.getSystemService("device_policy")).createAdminSupportIntent("policy_disable_screen_capture");
        if (createAdminSupportIntent != null) {
            color.setContentIntent(PendingIntent.getActivityAsUser(this.mContext, 0, createAdminSupportIntent, 67108864, (Bundle) null, UserHandle.CURRENT));
        }
        SystemUIApplication.overrideNotificationAppName(this.mContext, color, true);
        this.mNotificationManager.notify(1, new Notification.BigTextStyle(color).bigText(string).build());
    }
}
