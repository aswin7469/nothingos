package com.android.systemui.statusbar.notification.interruption;

import android.service.notification.StatusBarNotification;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessage;
import com.android.systemui.log.dagger.NotificationHeadsUpLog;
import com.android.systemui.log.dagger.NotificationLog;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0014\u0018\u00002\u00020\u0001B\u001b\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0001\u0010\u0004\u001a\u00020\u0003¢\u0006\u0002\u0010\u0005J\u000e\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fJ\u0016\u0010\r\u001a\u00020\n2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\fJ\u000e\u0010\u0011\u001a\u00020\n2\u0006\u0010\u0012\u001a\u00020\u0013J\u000e\u0010\u0014\u001a\u00020\n2\u0006\u0010\u0015\u001a\u00020\u0016J\u000e\u0010\u0017\u001a\u00020\n2\u0006\u0010\u0012\u001a\u00020\u0013J\u000e\u0010\u0018\u001a\u00020\n2\u0006\u0010\u0012\u001a\u00020\u0013J\u000e\u0010\u0019\u001a\u00020\n2\u0006\u0010\u0012\u001a\u00020\u0013J\u001e\u0010\u001a\u001a\u00020\n2\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u0016J\u000e\u0010\u001e\u001a\u00020\n2\u0006\u0010\u0012\u001a\u00020\u0013J\u000e\u0010\u001f\u001a\u00020\n2\u0006\u0010\u0012\u001a\u00020\u0013J\u0016\u0010 \u001a\u00020\n2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\fJ\u0016\u0010!\u001a\u00020\n2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\fJ\u000e\u0010\"\u001a\u00020\n2\u0006\u0010\u0012\u001a\u00020\u0013J\u0006\u0010#\u001a\u00020\nJ\u000e\u0010$\u001a\u00020\n2\u0006\u0010\u0012\u001a\u00020\u0013J\u000e\u0010%\u001a\u00020\n2\u0006\u0010\u0012\u001a\u00020\u0013J\u000e\u0010&\u001a\u00020\n2\u0006\u0010\u0012\u001a\u00020\u0013J\u0016\u0010'\u001a\u00020\n2\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u001b\u001a\u00020\u001cJ\u000e\u0010(\u001a\u00020\n2\u0006\u0010\u0012\u001a\u00020\u0013J\u000e\u0010)\u001a\u00020\n2\u0006\u0010\u0012\u001a\u00020\u0013J\u000e\u0010*\u001a\u00020\n2\u0006\u0010\u0012\u001a\u00020\u0013J\u000e\u0010+\u001a\u00020\n2\u0006\u0010\u0012\u001a\u00020\u0013J\u000e\u0010,\u001a\u00020\n2\u0006\u0010\u0012\u001a\u00020\u0013J\u000e\u0010-\u001a\u00020\n2\u0006\u0010\u0012\u001a\u00020\u0013J\u000e\u0010.\u001a\u00020\n2\u0006\u0010\u0012\u001a\u00020\u0013J\u0006\u0010/\u001a\u00020\nR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0007¨\u00060"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/interruption/NotificationInterruptLogger;", "", "notifBuffer", "Lcom/android/systemui/log/LogBuffer;", "hunBuffer", "(Lcom/android/systemui/log/LogBuffer;Lcom/android/systemui/log/LogBuffer;)V", "getHunBuffer", "()Lcom/android/systemui/log/LogBuffer;", "getNotifBuffer", "keyguardHideNotification", "", "key", "", "logFullscreen", "entry", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "reason", "logHeadsUp", "sbn", "Landroid/service/notification/StatusBarNotification;", "logHeadsUpFeatureChanged", "useHeadsUp", "", "logNoAlertingFilteredOut", "logNoAlertingGroupAlertBehavior", "logNoAlertingRecentFullscreen", "logNoAlertingSuppressedBy", "suppressor", "Lcom/android/systemui/statusbar/notification/interruption/NotificationInterruptSuppressor;", "awake", "logNoBubbleNoMetadata", "logNoBubbleNotAllowed", "logNoFullscreen", "logNoFullscreenWarning", "logNoHeadsUpAlreadyBubbled", "logNoHeadsUpFeatureDisabled", "logNoHeadsUpNotImportant", "logNoHeadsUpNotInUse", "logNoHeadsUpPackageSnoozed", "logNoHeadsUpSuppressedBy", "logNoHeadsUpSuppressedByDnd", "logNoPulsingBatteryDisabled", "logNoPulsingNoAlert", "logNoPulsingNoAmbientEffect", "logNoPulsingNotImportant", "logNoPulsingSettingDisabled", "logPulsing", "logWillDismissAll", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: NotificationInterruptLogger.kt */
public final class NotificationInterruptLogger {
    private final LogBuffer hunBuffer;
    private final LogBuffer notifBuffer;

    @Inject
    public NotificationInterruptLogger(@NotificationLog LogBuffer logBuffer, @NotificationHeadsUpLog LogBuffer logBuffer2) {
        Intrinsics.checkNotNullParameter(logBuffer, "notifBuffer");
        Intrinsics.checkNotNullParameter(logBuffer2, "hunBuffer");
        this.notifBuffer = logBuffer;
        this.hunBuffer = logBuffer2;
    }

    public final LogBuffer getNotifBuffer() {
        return this.notifBuffer;
    }

    public final LogBuffer getHunBuffer() {
        return this.hunBuffer;
    }

    public final void logHeadsUpFeatureChanged(boolean z) {
        LogBuffer logBuffer = this.hunBuffer;
        LogMessage obtain = logBuffer.obtain("InterruptionStateProvider", LogLevel.INFO, NotificationInterruptLogger$logHeadsUpFeatureChanged$2.INSTANCE);
        obtain.setBool1(z);
        logBuffer.commit(obtain);
    }

    public final void logWillDismissAll() {
        LogBuffer logBuffer = this.hunBuffer;
        logBuffer.commit(logBuffer.obtain("InterruptionStateProvider", LogLevel.INFO, NotificationInterruptLogger$logWillDismissAll$2.INSTANCE));
    }

    public final void logNoBubbleNotAllowed(StatusBarNotification statusBarNotification) {
        Intrinsics.checkNotNullParameter(statusBarNotification, "sbn");
        LogBuffer logBuffer = this.notifBuffer;
        LogMessage obtain = logBuffer.obtain("InterruptionStateProvider", LogLevel.DEBUG, NotificationInterruptLogger$logNoBubbleNotAllowed$2.INSTANCE);
        obtain.setStr1(statusBarNotification.getKey());
        logBuffer.commit(obtain);
    }

    public final void logNoBubbleNoMetadata(StatusBarNotification statusBarNotification) {
        Intrinsics.checkNotNullParameter(statusBarNotification, "sbn");
        LogBuffer logBuffer = this.notifBuffer;
        LogMessage obtain = logBuffer.obtain("InterruptionStateProvider", LogLevel.DEBUG, NotificationInterruptLogger$logNoBubbleNoMetadata$2.INSTANCE);
        obtain.setStr1(statusBarNotification.getKey());
        logBuffer.commit(obtain);
    }

    public final void logNoHeadsUpFeatureDisabled() {
        LogBuffer logBuffer = this.hunBuffer;
        logBuffer.commit(logBuffer.obtain("InterruptionStateProvider", LogLevel.DEBUG, NotificationInterruptLogger$logNoHeadsUpFeatureDisabled$2.INSTANCE));
    }

    public final void logNoHeadsUpPackageSnoozed(StatusBarNotification statusBarNotification) {
        Intrinsics.checkNotNullParameter(statusBarNotification, "sbn");
        LogBuffer logBuffer = this.hunBuffer;
        LogMessage obtain = logBuffer.obtain("InterruptionStateProvider", LogLevel.DEBUG, NotificationInterruptLogger$logNoHeadsUpPackageSnoozed$2.INSTANCE);
        obtain.setStr1(statusBarNotification.getKey());
        logBuffer.commit(obtain);
    }

    public final void logNoHeadsUpAlreadyBubbled(StatusBarNotification statusBarNotification) {
        Intrinsics.checkNotNullParameter(statusBarNotification, "sbn");
        LogBuffer logBuffer = this.hunBuffer;
        LogMessage obtain = logBuffer.obtain("InterruptionStateProvider", LogLevel.DEBUG, NotificationInterruptLogger$logNoHeadsUpAlreadyBubbled$2.INSTANCE);
        obtain.setStr1(statusBarNotification.getKey());
        logBuffer.commit(obtain);
    }

    public final void logNoHeadsUpSuppressedByDnd(StatusBarNotification statusBarNotification) {
        Intrinsics.checkNotNullParameter(statusBarNotification, "sbn");
        LogBuffer logBuffer = this.hunBuffer;
        LogMessage obtain = logBuffer.obtain("InterruptionStateProvider", LogLevel.DEBUG, NotificationInterruptLogger$logNoHeadsUpSuppressedByDnd$2.INSTANCE);
        obtain.setStr1(statusBarNotification.getKey());
        logBuffer.commit(obtain);
    }

    public final void logNoHeadsUpNotImportant(StatusBarNotification statusBarNotification) {
        Intrinsics.checkNotNullParameter(statusBarNotification, "sbn");
        LogBuffer logBuffer = this.hunBuffer;
        LogMessage obtain = logBuffer.obtain("InterruptionStateProvider", LogLevel.DEBUG, NotificationInterruptLogger$logNoHeadsUpNotImportant$2.INSTANCE);
        obtain.setStr1(statusBarNotification.getKey());
        logBuffer.commit(obtain);
    }

    public final void logNoHeadsUpNotInUse(StatusBarNotification statusBarNotification) {
        Intrinsics.checkNotNullParameter(statusBarNotification, "sbn");
        LogBuffer logBuffer = this.hunBuffer;
        LogMessage obtain = logBuffer.obtain("InterruptionStateProvider", LogLevel.DEBUG, NotificationInterruptLogger$logNoHeadsUpNotInUse$2.INSTANCE);
        obtain.setStr1(statusBarNotification.getKey());
        logBuffer.commit(obtain);
    }

    public final void logNoHeadsUpSuppressedBy(StatusBarNotification statusBarNotification, NotificationInterruptSuppressor notificationInterruptSuppressor) {
        Intrinsics.checkNotNullParameter(statusBarNotification, "sbn");
        Intrinsics.checkNotNullParameter(notificationInterruptSuppressor, "suppressor");
        LogBuffer logBuffer = this.hunBuffer;
        LogMessage obtain = logBuffer.obtain("InterruptionStateProvider", LogLevel.DEBUG, NotificationInterruptLogger$logNoHeadsUpSuppressedBy$2.INSTANCE);
        obtain.setStr1(statusBarNotification.getKey());
        obtain.setStr2(notificationInterruptSuppressor.getName());
        logBuffer.commit(obtain);
    }

    public final void logHeadsUp(StatusBarNotification statusBarNotification) {
        Intrinsics.checkNotNullParameter(statusBarNotification, "sbn");
        LogBuffer logBuffer = this.hunBuffer;
        LogMessage obtain = logBuffer.obtain("InterruptionStateProvider", LogLevel.DEBUG, NotificationInterruptLogger$logHeadsUp$2.INSTANCE);
        obtain.setStr1(statusBarNotification.getKey());
        logBuffer.commit(obtain);
    }

    public final void logNoAlertingFilteredOut(StatusBarNotification statusBarNotification) {
        Intrinsics.checkNotNullParameter(statusBarNotification, "sbn");
        LogBuffer logBuffer = this.hunBuffer;
        LogMessage obtain = logBuffer.obtain("InterruptionStateProvider", LogLevel.DEBUG, NotificationInterruptLogger$logNoAlertingFilteredOut$2.INSTANCE);
        obtain.setStr1(statusBarNotification.getKey());
        logBuffer.commit(obtain);
    }

    public final void logNoAlertingGroupAlertBehavior(StatusBarNotification statusBarNotification) {
        Intrinsics.checkNotNullParameter(statusBarNotification, "sbn");
        LogBuffer logBuffer = this.hunBuffer;
        LogMessage obtain = logBuffer.obtain("InterruptionStateProvider", LogLevel.DEBUG, NotificationInterruptLogger$logNoAlertingGroupAlertBehavior$2.INSTANCE);
        obtain.setStr1(statusBarNotification.getKey());
        logBuffer.commit(obtain);
    }

    public final void logNoAlertingSuppressedBy(StatusBarNotification statusBarNotification, NotificationInterruptSuppressor notificationInterruptSuppressor, boolean z) {
        Intrinsics.checkNotNullParameter(statusBarNotification, "sbn");
        Intrinsics.checkNotNullParameter(notificationInterruptSuppressor, "suppressor");
        LogBuffer logBuffer = this.hunBuffer;
        LogMessage obtain = logBuffer.obtain("InterruptionStateProvider", LogLevel.DEBUG, NotificationInterruptLogger$logNoAlertingSuppressedBy$2.INSTANCE);
        obtain.setStr1(statusBarNotification.getKey());
        obtain.setStr2(notificationInterruptSuppressor.getName());
        obtain.setBool1(z);
        logBuffer.commit(obtain);
    }

    public final void logNoAlertingRecentFullscreen(StatusBarNotification statusBarNotification) {
        Intrinsics.checkNotNullParameter(statusBarNotification, "sbn");
        LogBuffer logBuffer = this.hunBuffer;
        LogMessage obtain = logBuffer.obtain("InterruptionStateProvider", LogLevel.DEBUG, NotificationInterruptLogger$logNoAlertingRecentFullscreen$2.INSTANCE);
        obtain.setStr1(statusBarNotification.getKey());
        logBuffer.commit(obtain);
    }

    public final void logNoPulsingSettingDisabled(StatusBarNotification statusBarNotification) {
        Intrinsics.checkNotNullParameter(statusBarNotification, "sbn");
        LogBuffer logBuffer = this.hunBuffer;
        LogMessage obtain = logBuffer.obtain("InterruptionStateProvider", LogLevel.DEBUG, NotificationInterruptLogger$logNoPulsingSettingDisabled$2.INSTANCE);
        obtain.setStr1(statusBarNotification.getKey());
        logBuffer.commit(obtain);
    }

    public final void logNoPulsingBatteryDisabled(StatusBarNotification statusBarNotification) {
        Intrinsics.checkNotNullParameter(statusBarNotification, "sbn");
        LogBuffer logBuffer = this.hunBuffer;
        LogMessage obtain = logBuffer.obtain("InterruptionStateProvider", LogLevel.DEBUG, NotificationInterruptLogger$logNoPulsingBatteryDisabled$2.INSTANCE);
        obtain.setStr1(statusBarNotification.getKey());
        logBuffer.commit(obtain);
    }

    public final void logNoPulsingNoAlert(StatusBarNotification statusBarNotification) {
        Intrinsics.checkNotNullParameter(statusBarNotification, "sbn");
        LogBuffer logBuffer = this.hunBuffer;
        LogMessage obtain = logBuffer.obtain("InterruptionStateProvider", LogLevel.DEBUG, NotificationInterruptLogger$logNoPulsingNoAlert$2.INSTANCE);
        obtain.setStr1(statusBarNotification.getKey());
        logBuffer.commit(obtain);
    }

    public final void logNoPulsingNoAmbientEffect(StatusBarNotification statusBarNotification) {
        Intrinsics.checkNotNullParameter(statusBarNotification, "sbn");
        LogBuffer logBuffer = this.hunBuffer;
        LogMessage obtain = logBuffer.obtain("InterruptionStateProvider", LogLevel.DEBUG, NotificationInterruptLogger$logNoPulsingNoAmbientEffect$2.INSTANCE);
        obtain.setStr1(statusBarNotification.getKey());
        logBuffer.commit(obtain);
    }

    public final void logNoPulsingNotImportant(StatusBarNotification statusBarNotification) {
        Intrinsics.checkNotNullParameter(statusBarNotification, "sbn");
        LogBuffer logBuffer = this.hunBuffer;
        LogMessage obtain = logBuffer.obtain("InterruptionStateProvider", LogLevel.DEBUG, NotificationInterruptLogger$logNoPulsingNotImportant$2.INSTANCE);
        obtain.setStr1(statusBarNotification.getKey());
        logBuffer.commit(obtain);
    }

    public final void logPulsing(StatusBarNotification statusBarNotification) {
        Intrinsics.checkNotNullParameter(statusBarNotification, "sbn");
        LogBuffer logBuffer = this.hunBuffer;
        LogMessage obtain = logBuffer.obtain("InterruptionStateProvider", LogLevel.DEBUG, NotificationInterruptLogger$logPulsing$2.INSTANCE);
        obtain.setStr1(statusBarNotification.getKey());
        logBuffer.commit(obtain);
    }

    public final void logNoFullscreen(NotificationEntry notificationEntry, String str) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        Intrinsics.checkNotNullParameter(str, "reason");
        LogBuffer logBuffer = this.hunBuffer;
        LogMessage obtain = logBuffer.obtain("InterruptionStateProvider", LogLevel.DEBUG, NotificationInterruptLogger$logNoFullscreen$2.INSTANCE);
        obtain.setStr1(notificationEntry.getKey());
        obtain.setStr2(str);
        logBuffer.commit(obtain);
    }

    public final void logNoFullscreenWarning(NotificationEntry notificationEntry, String str) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        Intrinsics.checkNotNullParameter(str, "reason");
        LogBuffer logBuffer = this.hunBuffer;
        LogMessage obtain = logBuffer.obtain("InterruptionStateProvider", LogLevel.WARNING, NotificationInterruptLogger$logNoFullscreenWarning$2.INSTANCE);
        obtain.setStr1(notificationEntry.getKey());
        obtain.setStr2(str);
        logBuffer.commit(obtain);
    }

    public final void logFullscreen(NotificationEntry notificationEntry, String str) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        Intrinsics.checkNotNullParameter(str, "reason");
        LogBuffer logBuffer = this.hunBuffer;
        LogMessage obtain = logBuffer.obtain("InterruptionStateProvider", LogLevel.DEBUG, NotificationInterruptLogger$logFullscreen$2.INSTANCE);
        obtain.setStr1(notificationEntry.getKey());
        obtain.setStr2(str);
        logBuffer.commit(obtain);
    }

    public final void keyguardHideNotification(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        LogBuffer logBuffer = this.hunBuffer;
        LogMessage obtain = logBuffer.obtain("InterruptionStateProvider", LogLevel.DEBUG, NotificationInterruptLogger$keyguardHideNotification$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }
}
