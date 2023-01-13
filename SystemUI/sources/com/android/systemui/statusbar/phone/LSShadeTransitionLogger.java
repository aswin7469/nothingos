package com.android.systemui.statusbar.phone;

import android.util.DisplayMetrics;
import android.view.View;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessage;
import com.android.systemui.log.dagger.LSShadeTransitionLog;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.ExpandableView;
import com.android.systemui.statusbar.phone.LockscreenGestureLogger;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u000f\u0018\u00002\u00020\u0001B!\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\u000e\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fJ\u000e\u0010\r\u001a\u00020\n2\u0006\u0010\u000e\u001a\u00020\u000fJ\u0006\u0010\u0010\u001a\u00020\nJ\u0006\u0010\u0011\u001a\u00020\nJ\u0006\u0010\u0012\u001a\u00020\nJ\u000e\u0010\u0013\u001a\u00020\n2\u0006\u0010\u0014\u001a\u00020\u0015J\u0010\u0010\u0016\u001a\u00020\n2\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018J\u0018\u0010\u0019\u001a\u00020\n2\b\u0010\u0017\u001a\u0004\u0018\u00010\u001a2\u0006\u0010\u001b\u001a\u00020\u001cJ\u0010\u0010\u001d\u001a\u00020\n2\b\u0010\u0017\u001a\u0004\u0018\u00010\u001aJ\u000e\u0010\u001e\u001a\u00020\n2\u0006\u0010\u001f\u001a\u00020\fJ\u0006\u0010 \u001a\u00020\nJ\u0006\u0010!\u001a\u00020\nJ\u000e\u0010\"\u001a\u00020\n2\u0006\u0010#\u001a\u00020\fJ\u0006\u0010$\u001a\u00020\nJ\u0006\u0010%\u001a\u00020\nJ\u0006\u0010&\u001a\u00020\nJ\u0006\u0010'\u001a\u00020\nJ\u000e\u0010(\u001a\u00020\n2\u0006\u0010)\u001a\u00020\fJ\u0010\u0010*\u001a\u00020\n2\b\u0010\u0017\u001a\u0004\u0018\u00010\u001aR\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000¨\u0006+"}, mo65043d2 = {"Lcom/android/systemui/statusbar/phone/LSShadeTransitionLogger;", "", "buffer", "Lcom/android/systemui/log/LogBuffer;", "lockscreenGestureLogger", "Lcom/android/systemui/statusbar/phone/LockscreenGestureLogger;", "displayMetrics", "Landroid/util/DisplayMetrics;", "(Lcom/android/systemui/log/LogBuffer;Lcom/android/systemui/statusbar/phone/LockscreenGestureLogger;Landroid/util/DisplayMetrics;)V", "logAnimationCancelled", "", "isPulse", "", "logDefaultGoToFullShadeAnimation", "delay", "", "logDragDownAborted", "logDragDownAmountReset", "logDragDownAmountResetWhenFullyCollapsed", "logDragDownAnimation", "target", "", "logDragDownStarted", "startingChild", "Lcom/android/systemui/statusbar/notification/row/ExpandableView;", "logDraggedDown", "Landroid/view/View;", "dragLengthY", "", "logDraggedDownLockDownShade", "logGoingToLockedShade", "customAnimationHandler", "logGoingToLockedShadeAborted", "logOnHideKeyguard", "logPulseExpansionFinished", "cancelled", "logPulseExpansionStarted", "logPulseHeightNotResetWhenFullyCollapsed", "logShadeDisabledOnGoToLockedShade", "logShowBouncerOnGoToLockedShade", "logTryGoToLockedShade", "keyguard", "logUnSuccessfulDragDown", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: LSShadeTransitionLogger.kt */
public final class LSShadeTransitionLogger {
    private final LogBuffer buffer;
    private final DisplayMetrics displayMetrics;
    private final LockscreenGestureLogger lockscreenGestureLogger;

    @Inject
    public LSShadeTransitionLogger(@LSShadeTransitionLog LogBuffer logBuffer, LockscreenGestureLogger lockscreenGestureLogger2, DisplayMetrics displayMetrics2) {
        Intrinsics.checkNotNullParameter(logBuffer, "buffer");
        Intrinsics.checkNotNullParameter(lockscreenGestureLogger2, "lockscreenGestureLogger");
        Intrinsics.checkNotNullParameter(displayMetrics2, "displayMetrics");
        this.buffer = logBuffer;
        this.lockscreenGestureLogger = lockscreenGestureLogger2;
        this.displayMetrics = displayMetrics2;
    }

    public final void logUnSuccessfulDragDown(View view) {
        String str;
        NotificationEntry notificationEntry = null;
        ExpandableNotificationRow expandableNotificationRow = view instanceof ExpandableNotificationRow ? (ExpandableNotificationRow) view : null;
        if (expandableNotificationRow != null) {
            notificationEntry = expandableNotificationRow.getEntry();
        }
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("LockscreenShadeTransitionController", LogLevel.INFO, LSShadeTransitionLogger$logUnSuccessfulDragDown$2.INSTANCE);
        if (notificationEntry == null || (str = notificationEntry.getKey()) == null) {
            str = "no entry";
        }
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logDragDownAborted() {
        LogBuffer logBuffer = this.buffer;
        logBuffer.commit(logBuffer.obtain("LockscreenShadeTransitionController", LogLevel.INFO, LSShadeTransitionLogger$logDragDownAborted$2.INSTANCE));
    }

    public final void logDragDownStarted(ExpandableView expandableView) {
        String str;
        NotificationEntry notificationEntry = null;
        ExpandableNotificationRow expandableNotificationRow = expandableView instanceof ExpandableNotificationRow ? (ExpandableNotificationRow) expandableView : null;
        if (expandableNotificationRow != null) {
            notificationEntry = expandableNotificationRow.getEntry();
        }
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("LockscreenShadeTransitionController", LogLevel.INFO, LSShadeTransitionLogger$logDragDownStarted$2.INSTANCE);
        if (notificationEntry == null || (str = notificationEntry.getKey()) == null) {
            str = "no entry";
        }
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logDraggedDownLockDownShade(View view) {
        String str;
        NotificationEntry notificationEntry = null;
        ExpandableNotificationRow expandableNotificationRow = view instanceof ExpandableNotificationRow ? (ExpandableNotificationRow) view : null;
        if (expandableNotificationRow != null) {
            notificationEntry = expandableNotificationRow.getEntry();
        }
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("LockscreenShadeTransitionController", LogLevel.INFO, LSShadeTransitionLogger$logDraggedDownLockDownShade$2.INSTANCE);
        if (notificationEntry == null || (str = notificationEntry.getKey()) == null) {
            str = "no entry";
        }
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logDraggedDown(View view, int i) {
        String str;
        NotificationEntry notificationEntry = null;
        ExpandableNotificationRow expandableNotificationRow = view instanceof ExpandableNotificationRow ? (ExpandableNotificationRow) view : null;
        if (expandableNotificationRow != null) {
            notificationEntry = expandableNotificationRow.getEntry();
        }
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("LockscreenShadeTransitionController", LogLevel.INFO, LSShadeTransitionLogger$logDraggedDown$2.INSTANCE);
        if (notificationEntry == null || (str = notificationEntry.getKey()) == null) {
            str = "no entry";
        }
        obtain.setStr1(str);
        logBuffer.commit(obtain);
        this.lockscreenGestureLogger.write(187, (int) (((float) i) / this.displayMetrics.density), 0);
        this.lockscreenGestureLogger.log(LockscreenGestureLogger.LockscreenUiEvent.LOCKSCREEN_PULL_SHADE_OPEN);
    }

    public final void logDragDownAmountReset() {
        LogBuffer logBuffer = this.buffer;
        logBuffer.commit(logBuffer.obtain("LockscreenShadeTransitionController", LogLevel.DEBUG, LSShadeTransitionLogger$logDragDownAmountReset$2.INSTANCE));
    }

    public final void logDefaultGoToFullShadeAnimation(long j) {
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("LockscreenShadeTransitionController", LogLevel.DEBUG, LSShadeTransitionLogger$logDefaultGoToFullShadeAnimation$2.INSTANCE);
        obtain.setLong1(j);
        logBuffer.commit(obtain);
    }

    public final void logTryGoToLockedShade(boolean z) {
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("LockscreenShadeTransitionController", LogLevel.INFO, LSShadeTransitionLogger$logTryGoToLockedShade$2.INSTANCE);
        obtain.setBool1(z);
        logBuffer.commit(obtain);
    }

    public final void logShadeDisabledOnGoToLockedShade() {
        LogBuffer logBuffer = this.buffer;
        logBuffer.commit(logBuffer.obtain("LockscreenShadeTransitionController", LogLevel.WARNING, LSShadeTransitionLogger$logShadeDisabledOnGoToLockedShade$2.INSTANCE));
    }

    public final void logShowBouncerOnGoToLockedShade() {
        LogBuffer logBuffer = this.buffer;
        logBuffer.commit(logBuffer.obtain("LockscreenShadeTransitionController", LogLevel.INFO, LSShadeTransitionLogger$logShowBouncerOnGoToLockedShade$2.INSTANCE));
    }

    public final void logGoingToLockedShade(boolean z) {
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("LockscreenShadeTransitionController", LogLevel.INFO, new LSShadeTransitionLogger$logGoingToLockedShade$2(z));
        obtain.setBool1(z);
        logBuffer.commit(obtain);
    }

    public final void logOnHideKeyguard() {
        LogBuffer logBuffer = this.buffer;
        logBuffer.commit(logBuffer.obtain("LockscreenShadeTransitionController", LogLevel.INFO, LSShadeTransitionLogger$logOnHideKeyguard$2.INSTANCE));
    }

    public final void logPulseExpansionStarted() {
        LogBuffer logBuffer = this.buffer;
        logBuffer.commit(logBuffer.obtain("LockscreenShadeTransitionController", LogLevel.INFO, LSShadeTransitionLogger$logPulseExpansionStarted$2.INSTANCE));
    }

    public final void logPulseExpansionFinished(boolean z) {
        if (z) {
            LogBuffer logBuffer = this.buffer;
            logBuffer.commit(logBuffer.obtain("LockscreenShadeTransitionController", LogLevel.INFO, LSShadeTransitionLogger$logPulseExpansionFinished$2.INSTANCE));
            return;
        }
        LogBuffer logBuffer2 = this.buffer;
        logBuffer2.commit(logBuffer2.obtain("LockscreenShadeTransitionController", LogLevel.INFO, LSShadeTransitionLogger$logPulseExpansionFinished$4.INSTANCE));
    }

    public final void logDragDownAnimation(float f) {
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("LockscreenShadeTransitionController", LogLevel.DEBUG, LSShadeTransitionLogger$logDragDownAnimation$2.INSTANCE);
        obtain.setDouble1((double) f);
        logBuffer.commit(obtain);
    }

    public final void logAnimationCancelled(boolean z) {
        if (z) {
            LogBuffer logBuffer = this.buffer;
            logBuffer.commit(logBuffer.obtain("LockscreenShadeTransitionController", LogLevel.DEBUG, LSShadeTransitionLogger$logAnimationCancelled$2.INSTANCE));
            return;
        }
        LogBuffer logBuffer2 = this.buffer;
        logBuffer2.commit(logBuffer2.obtain("LockscreenShadeTransitionController", LogLevel.DEBUG, LSShadeTransitionLogger$logAnimationCancelled$4.INSTANCE));
    }

    public final void logDragDownAmountResetWhenFullyCollapsed() {
        LogBuffer logBuffer = this.buffer;
        logBuffer.commit(logBuffer.obtain("LockscreenShadeTransitionController", LogLevel.WARNING, C3001xc8baf00b.INSTANCE));
    }

    public final void logPulseHeightNotResetWhenFullyCollapsed() {
        LogBuffer logBuffer = this.buffer;
        logBuffer.commit(logBuffer.obtain("LockscreenShadeTransitionController", LogLevel.WARNING, C3002x831f3f06.INSTANCE));
    }

    public final void logGoingToLockedShadeAborted() {
        LogBuffer logBuffer = this.buffer;
        logBuffer.commit(logBuffer.obtain("LockscreenShadeTransitionController", LogLevel.INFO, LSShadeTransitionLogger$logGoingToLockedShadeAborted$2.INSTANCE));
    }
}
