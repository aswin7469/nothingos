package com.android.systemui.statusbar.notification.collection.coalescer;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessage;
import com.android.systemui.log.dagger.NotificationLog;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\u0011\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0016\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\bJ\u001e\u0010\n\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eJ\u000e\u0010\u000f\u001a\u00020\u00062\u0006\u0010\u0010\u001a\u00020\bJ\u0016\u0010\u0011\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\bJ\u000e\u0010\u0012\u001a\u00020\u00062\u0006\u0010\u0013\u001a\u00020\bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0014"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/collection/coalescer/GroupCoalescerLogger;", "", "buffer", "Lcom/android/systemui/log/LogBuffer;", "(Lcom/android/systemui/log/LogBuffer;)V", "logEarlyEmit", "", "modifiedKey", "", "groupKey", "logEmitBatch", "batchSize", "", "batchAgeMs", "", "logEventCoalesced", "key", "logMaxBatchTimeout", "logMissingRanking", "forKey", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: GroupCoalescerLogger.kt */
public final class GroupCoalescerLogger {
    private final LogBuffer buffer;

    @Inject
    public GroupCoalescerLogger(@NotificationLog LogBuffer logBuffer) {
        Intrinsics.checkNotNullParameter(logBuffer, "buffer");
        this.buffer = logBuffer;
    }

    public final void logEventCoalesced(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("GroupCoalescer", LogLevel.INFO, GroupCoalescerLogger$logEventCoalesced$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logEmitBatch(String str, int i, long j) {
        Intrinsics.checkNotNullParameter(str, "groupKey");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("GroupCoalescer", LogLevel.DEBUG, GroupCoalescerLogger$logEmitBatch$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setInt1(i);
        obtain.setLong1(j);
        logBuffer.commit(obtain);
    }

    public final void logEarlyEmit(String str, String str2) {
        Intrinsics.checkNotNullParameter(str, "modifiedKey");
        Intrinsics.checkNotNullParameter(str2, "groupKey");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("GroupCoalescer", LogLevel.DEBUG, GroupCoalescerLogger$logEarlyEmit$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setStr2(str2);
        logBuffer.commit(obtain);
    }

    public final void logMaxBatchTimeout(String str, String str2) {
        Intrinsics.checkNotNullParameter(str, "modifiedKey");
        Intrinsics.checkNotNullParameter(str2, "groupKey");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("GroupCoalescer", LogLevel.INFO, GroupCoalescerLogger$logMaxBatchTimeout$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setStr2(str2);
        logBuffer.commit(obtain);
    }

    public final void logMissingRanking(String str) {
        Intrinsics.checkNotNullParameter(str, "forKey");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("GroupCoalescer", LogLevel.WARNING, GroupCoalescerLogger$logMissingRanking$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }
}
