package com.android.systemui.statusbar.policy;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.android.systemui.R$string;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: BatteryStateNotifier.kt */
/* loaded from: classes2.dex */
public final class BatteryStateNotifier implements BatteryController.BatteryStateChangeCallback {
    @NotNull
    private final Context context;
    @NotNull
    private final BatteryController controller;
    @NotNull
    private final DelayableExecutor delayableExecutor;
    @NotNull
    private final NotificationManager noMan;
    private boolean stateUnknown;

    public BatteryStateNotifier(@NotNull BatteryController controller, @NotNull NotificationManager noMan, @NotNull DelayableExecutor delayableExecutor, @NotNull Context context) {
        Intrinsics.checkNotNullParameter(controller, "controller");
        Intrinsics.checkNotNullParameter(noMan, "noMan");
        Intrinsics.checkNotNullParameter(delayableExecutor, "delayableExecutor");
        Intrinsics.checkNotNullParameter(context, "context");
        this.controller = controller;
        this.noMan = noMan;
        this.delayableExecutor = delayableExecutor;
        this.context = context;
    }

    @NotNull
    public final NotificationManager getNoMan() {
        return this.noMan;
    }

    public final boolean getStateUnknown() {
        return this.stateUnknown;
    }

    public final void startListening() {
        this.controller.addCallback(this);
    }

    @Override // com.android.systemui.statusbar.policy.BatteryController.BatteryStateChangeCallback
    public void onBatteryUnknownStateChanged(boolean z) {
        this.stateUnknown = z;
        if (z) {
            NotificationChannel notificationChannel = new NotificationChannel("battery_status", "Battery status", 3);
            this.noMan.createNotificationChannel(notificationChannel);
            this.noMan.notify("BatteryStateNotifier", 666, new Notification.Builder(this.context, notificationChannel.getId()).setAutoCancel(false).setContentTitle(this.context.getString(R$string.battery_state_unknown_notification_title)).setContentText(this.context.getString(R$string.battery_state_unknown_notification_text)).setSmallIcon(17303583).setContentIntent(PendingIntent.getActivity(this.context, 0, new Intent("android.intent.action.VIEW", Uri.parse(this.context.getString(R$string.config_batteryStateUnknownUrl))), 67108864)).setAutoCancel(true).setOngoing(true).build());
            return;
        }
        scheduleNotificationCancel();
    }

    private final void scheduleNotificationCancel() {
        final BatteryStateNotifier$scheduleNotificationCancel$r$1 batteryStateNotifier$scheduleNotificationCancel$r$1 = new BatteryStateNotifier$scheduleNotificationCancel$r$1(this);
        this.delayableExecutor.executeDelayed(new Runnable() { // from class: com.android.systemui.statusbar.policy.BatteryStateNotifierKt$sam$java_lang_Runnable$0
            @Override // java.lang.Runnable
            public final /* synthetic */ void run() {
                Function0.this.mo1951invoke();
            }
        }, 14400000L);
    }
}
