package com.android.systemui.statusbar.policy;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.android.systemui.C1894R;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B'\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t¢\u0006\u0002\u0010\nJ\u0010\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u0014H\u0016J\b\u0010\u001c\u001a\u00020\u001aH\u0002J\u0006\u0010\u001d\u001a\u00020\u001aJ\u0006\u0010\u001e\u001a\u00020\u001aR\u0011\u0010\b\u001a\u00020\t¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u001a\u0010\u0013\u001a\u00020\u0014X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\u0016\"\u0004\b\u0017\u0010\u0018¨\u0006\u001f"}, mo65043d2 = {"Lcom/android/systemui/statusbar/policy/BatteryStateNotifier;", "Lcom/android/systemui/statusbar/policy/BatteryController$BatteryStateChangeCallback;", "controller", "Lcom/android/systemui/statusbar/policy/BatteryController;", "noMan", "Landroid/app/NotificationManager;", "delayableExecutor", "Lcom/android/systemui/util/concurrency/DelayableExecutor;", "context", "Landroid/content/Context;", "(Lcom/android/systemui/statusbar/policy/BatteryController;Landroid/app/NotificationManager;Lcom/android/systemui/util/concurrency/DelayableExecutor;Landroid/content/Context;)V", "getContext", "()Landroid/content/Context;", "getController", "()Lcom/android/systemui/statusbar/policy/BatteryController;", "getDelayableExecutor", "()Lcom/android/systemui/util/concurrency/DelayableExecutor;", "getNoMan", "()Landroid/app/NotificationManager;", "stateUnknown", "", "getStateUnknown", "()Z", "setStateUnknown", "(Z)V", "onBatteryUnknownStateChanged", "", "isUnknown", "scheduleNotificationCancel", "startListening", "stopListening", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: BatteryStateNotifier.kt */
public final class BatteryStateNotifier implements BatteryController.BatteryStateChangeCallback {
    private final Context context;
    private final BatteryController controller;
    private final DelayableExecutor delayableExecutor;
    private final NotificationManager noMan;
    private boolean stateUnknown;

    @Inject
    public BatteryStateNotifier(BatteryController batteryController, NotificationManager notificationManager, DelayableExecutor delayableExecutor2, Context context2) {
        Intrinsics.checkNotNullParameter(batteryController, "controller");
        Intrinsics.checkNotNullParameter(notificationManager, "noMan");
        Intrinsics.checkNotNullParameter(delayableExecutor2, "delayableExecutor");
        Intrinsics.checkNotNullParameter(context2, "context");
        this.controller = batteryController;
        this.noMan = notificationManager;
        this.delayableExecutor = delayableExecutor2;
        this.context = context2;
    }

    public final BatteryController getController() {
        return this.controller;
    }

    public final NotificationManager getNoMan() {
        return this.noMan;
    }

    public final DelayableExecutor getDelayableExecutor() {
        return this.delayableExecutor;
    }

    public final Context getContext() {
        return this.context;
    }

    public final boolean getStateUnknown() {
        return this.stateUnknown;
    }

    public final void setStateUnknown(boolean z) {
        this.stateUnknown = z;
    }

    public final void startListening() {
        this.controller.addCallback(this);
    }

    public final void stopListening() {
        this.controller.removeCallback(this);
    }

    public void onBatteryUnknownStateChanged(boolean z) {
        this.stateUnknown = z;
        if (z) {
            NotificationChannel notificationChannel = new NotificationChannel("battery_status", "Battery status", 3);
            this.noMan.createNotificationChannel(notificationChannel);
            this.noMan.notify("BatteryStateNotifier", 666, new Notification.Builder(this.context, notificationChannel.getId()).setAutoCancel(false).setContentTitle(this.context.getString(C1894R.string.battery_state_unknown_notification_title)).setContentText(this.context.getString(C1894R.string.battery_state_unknown_notification_text)).setSmallIcon(17303599).setContentIntent(PendingIntent.getActivity(this.context, 0, new Intent("android.intent.action.VIEW", Uri.parse(this.context.getString(C1894R.string.config_batteryStateUnknownUrl))), 67108864)).setAutoCancel(true).setOngoing(true).build());
            return;
        }
        scheduleNotificationCancel();
    }

    private final void scheduleNotificationCancel() {
        this.delayableExecutor.executeDelayed(new BatteryStateNotifier$$ExternalSyntheticLambda0(new BatteryStateNotifier$scheduleNotificationCancel$r$1(this)), 14400000);
    }

    /* access modifiers changed from: private */
    /* renamed from: scheduleNotificationCancel$lambda-0  reason: not valid java name */
    public static final void m3229scheduleNotificationCancel$lambda0(Function0 function0) {
        Intrinsics.checkNotNullParameter(function0, "$tmp0");
        function0.invoke();
    }
}
