package com.android.systemui.statusbar.policy;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessage;
import com.android.systemui.log.dagger.NotificationHeadsUpLog;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\b\u0018\u00002\u00020\u0001B\u0011\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u000e\u0010\t\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\bJ\u000e\u0010\u000b\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u000e\u0010\f\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u0006\u0010\r\u001a\u00020\u0006J\u0016\u0010\u000e\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\b2\u0006\u0010\u000f\u001a\u00020\u0010J\u0016\u0010\u0011\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\b2\u0006\u0010\u0012\u001a\u00020\u0010J\u000e\u0010\u0013\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\bJ\u000e\u0010\u0014\u001a\u00020\u00062\u0006\u0010\u0015\u001a\u00020\u0016J\u0016\u0010\u0017\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\b2\u0006\u0010\u0018\u001a\u00020\u0010J\u001e\u0010\u0019\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\b2\u0006\u0010\u001a\u001a\u00020\u00102\u0006\u0010\u001b\u001a\u00020\u0010J\u000e\u0010\u001c\u001a\u00020\u00062\u0006\u0010\u001d\u001a\u00020\u0010R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\u001e"}, mo64987d2 = {"Lcom/android/systemui/statusbar/policy/HeadsUpManagerLogger;", "", "buffer", "Lcom/android/systemui/log/LogBuffer;", "(Lcom/android/systemui/log/LogBuffer;)V", "logIsSnoozedReturned", "", "snoozeKey", "", "logNotificationActuallyRemoved", "key", "logPackageSnoozed", "logPackageUnsnoozed", "logReleaseAllImmediately", "logRemoveNotification", "releaseImmediately", "", "logSetEntryPinned", "isPinned", "logShowNotification", "logSnoozeLengthChange", "packageSnoozeLengthMs", "", "logUpdateEntry", "updatePostTime", "logUpdateNotification", "alert", "hasEntry", "logUpdatePinnedMode", "hasPinnedNotification", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: HeadsUpManagerLogger.kt */
public final class HeadsUpManagerLogger {
    private final LogBuffer buffer;

    @Inject
    public HeadsUpManagerLogger(@NotificationHeadsUpLog LogBuffer logBuffer) {
        Intrinsics.checkNotNullParameter(logBuffer, "buffer");
        this.buffer = logBuffer;
    }

    public final void logPackageSnoozed(String str) {
        Intrinsics.checkNotNullParameter(str, "snoozeKey");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("HeadsUpManager", LogLevel.INFO, HeadsUpManagerLogger$logPackageSnoozed$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logPackageUnsnoozed(String str) {
        Intrinsics.checkNotNullParameter(str, "snoozeKey");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("HeadsUpManager", LogLevel.INFO, HeadsUpManagerLogger$logPackageUnsnoozed$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logIsSnoozedReturned(String str) {
        Intrinsics.checkNotNullParameter(str, "snoozeKey");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("HeadsUpManager", LogLevel.INFO, HeadsUpManagerLogger$logIsSnoozedReturned$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logReleaseAllImmediately() {
        LogBuffer logBuffer = this.buffer;
        logBuffer.commit(logBuffer.obtain("HeadsUpManager", LogLevel.INFO, HeadsUpManagerLogger$logReleaseAllImmediately$2.INSTANCE));
    }

    public final void logShowNotification(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("HeadsUpManager", LogLevel.INFO, HeadsUpManagerLogger$logShowNotification$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logRemoveNotification(String str, boolean z) {
        Intrinsics.checkNotNullParameter(str, "key");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("HeadsUpManager", LogLevel.INFO, HeadsUpManagerLogger$logRemoveNotification$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setBool1(z);
        logBuffer.commit(obtain);
    }

    public final void logNotificationActuallyRemoved(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("HeadsUpManager", LogLevel.INFO, HeadsUpManagerLogger$logNotificationActuallyRemoved$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logUpdateNotification(String str, boolean z, boolean z2) {
        Intrinsics.checkNotNullParameter(str, "key");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("HeadsUpManager", LogLevel.INFO, HeadsUpManagerLogger$logUpdateNotification$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setBool1(z);
        obtain.setBool2(z2);
        logBuffer.commit(obtain);
    }

    public final void logUpdateEntry(String str, boolean z) {
        Intrinsics.checkNotNullParameter(str, "key");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("HeadsUpManager", LogLevel.INFO, new HeadsUpManagerLogger$logUpdateEntry$2(str));
        obtain.setStr1(str);
        obtain.setBool1(z);
        logBuffer.commit(obtain);
    }

    public final void logSnoozeLengthChange(int i) {
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("HeadsUpManager", LogLevel.INFO, HeadsUpManagerLogger$logSnoozeLengthChange$2.INSTANCE);
        obtain.setInt1(i);
        logBuffer.commit(obtain);
    }

    public final void logSetEntryPinned(String str, boolean z) {
        Intrinsics.checkNotNullParameter(str, "key");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("HeadsUpManager", LogLevel.VERBOSE, HeadsUpManagerLogger$logSetEntryPinned$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setBool1(z);
        logBuffer.commit(obtain);
    }

    public final void logUpdatePinnedMode(boolean z) {
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("HeadsUpManager", LogLevel.INFO, HeadsUpManagerLogger$logUpdatePinnedMode$2.INSTANCE);
        obtain.setBool1(z);
        logBuffer.commit(obtain);
    }
}
