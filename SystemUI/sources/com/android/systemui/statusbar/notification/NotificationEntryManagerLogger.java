package com.android.systemui.statusbar.notification;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessage;
import com.android.systemui.log.dagger.NotificationLog;
import javax.inject.Inject;
import javax.xml.transform.OutputKeys;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0010\u000b\n\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0011\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u001e\u0010\t\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\b2\u0006\u0010\u0007\u001a\u00020\bJ\u001e\u0010\f\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\b2\u0006\u0010\r\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\bJ\u000e\u0010\u000e\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\bJ\u0016\u0010\u000f\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\b2\u0006\u0010\u0010\u001a\u00020\u0011J\u0016\u0010\u0012\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\b2\u0006\u0010\u0013\u001a\u00020\u0011J\u000e\u0010\u0014\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\bJ\u000e\u0010\u0015\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\bJ\u0016\u0010\u0016\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\b2\u0006\u0010\u0007\u001a\u00020\bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0018"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/NotificationEntryManagerLogger;", "", "buffer", "Lcom/android/systemui/log/LogBuffer;", "(Lcom/android/systemui/log/LogBuffer;)V", "logFilterAndSort", "", "reason", "", "logInflationAborted", "key", "status", "logLifetimeExtended", "extenderName", "logNotifAdded", "logNotifInflated", "isNew", "", "logNotifRemoved", "removedByUser", "logNotifUpdated", "logRemovalIntercepted", "logUseWhileNewPipelineActive", "method", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: NotificationEntryManagerLogger.kt */
public final class NotificationEntryManagerLogger {
    private final LogBuffer buffer;

    @Inject
    public NotificationEntryManagerLogger(@NotificationLog LogBuffer logBuffer) {
        Intrinsics.checkNotNullParameter(logBuffer, "buffer");
        this.buffer = logBuffer;
    }

    public final void logNotifAdded(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("NotificationEntryMgr", LogLevel.INFO, NotificationEntryManagerLogger$logNotifAdded$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logNotifUpdated(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("NotificationEntryMgr", LogLevel.INFO, NotificationEntryManagerLogger$logNotifUpdated$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logInflationAborted(String str, String str2, String str3) {
        Intrinsics.checkNotNullParameter(str, "key");
        Intrinsics.checkNotNullParameter(str2, "status");
        Intrinsics.checkNotNullParameter(str3, "reason");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("NotificationEntryMgr", LogLevel.DEBUG, NotificationEntryManagerLogger$logInflationAborted$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setStr2(str2);
        obtain.setStr3(str3);
        logBuffer.commit(obtain);
    }

    public final void logNotifInflated(String str, boolean z) {
        Intrinsics.checkNotNullParameter(str, "key");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("NotificationEntryMgr", LogLevel.DEBUG, NotificationEntryManagerLogger$logNotifInflated$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setBool1(z);
        logBuffer.commit(obtain);
    }

    public final void logRemovalIntercepted(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("NotificationEntryMgr", LogLevel.INFO, NotificationEntryManagerLogger$logRemovalIntercepted$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logLifetimeExtended(String str, String str2, String str3) {
        Intrinsics.checkNotNullParameter(str, "key");
        Intrinsics.checkNotNullParameter(str2, "extenderName");
        Intrinsics.checkNotNullParameter(str3, "status");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("NotificationEntryMgr", LogLevel.INFO, NotificationEntryManagerLogger$logLifetimeExtended$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setStr2(str2);
        obtain.setStr3(str3);
        logBuffer.commit(obtain);
    }

    public final void logNotifRemoved(String str, boolean z) {
        Intrinsics.checkNotNullParameter(str, "key");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("NotificationEntryMgr", LogLevel.INFO, NotificationEntryManagerLogger$logNotifRemoved$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setBool1(z);
        logBuffer.commit(obtain);
    }

    public final void logFilterAndSort(String str) {
        Intrinsics.checkNotNullParameter(str, "reason");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("NotificationEntryMgr", LogLevel.INFO, NotificationEntryManagerLogger$logFilterAndSort$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logUseWhileNewPipelineActive(String str, String str2) {
        Intrinsics.checkNotNullParameter(str, OutputKeys.METHOD);
        Intrinsics.checkNotNullParameter(str2, "reason");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("NotificationEntryMgr", LogLevel.WARNING, NotificationEntryManagerLogger$logUseWhileNewPipelineActive$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setStr2(str2);
        logBuffer.commit(obtain);
    }
}
