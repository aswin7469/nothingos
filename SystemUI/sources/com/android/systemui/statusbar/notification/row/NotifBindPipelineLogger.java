package com.android.systemui.statusbar.notification.row;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessage;
import com.android.systemui.log.dagger.NotificationLog;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0011\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0016\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nJ\u000e\u0010\u000b\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u000e\u0010\f\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u000e\u0010\r\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u000e\u0010\u000e\u001a\u00020\u00062\u0006\u0010\u000f\u001a\u00020\bJ\u000e\u0010\u0010\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0011"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/row/NotifBindPipelineLogger;", "", "buffer", "Lcom/android/systemui/log/LogBuffer;", "(Lcom/android/systemui/log/LogBuffer;)V", "logFinishedPipeline", "", "notifKey", "", "numCallbacks", "", "logManagedRow", "logRequestPipelineRowNotSet", "logRequestPipelineRun", "logStageSet", "stageName", "logStartPipeline", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: NotifBindPipelineLogger.kt */
public final class NotifBindPipelineLogger {
    private final LogBuffer buffer;

    @Inject
    public NotifBindPipelineLogger(@NotificationLog LogBuffer logBuffer) {
        Intrinsics.checkNotNullParameter(logBuffer, "buffer");
        this.buffer = logBuffer;
    }

    public final void logStageSet(String str) {
        Intrinsics.checkNotNullParameter(str, "stageName");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("NotifBindPipeline", LogLevel.INFO, NotifBindPipelineLogger$logStageSet$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logManagedRow(String str) {
        Intrinsics.checkNotNullParameter(str, "notifKey");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("NotifBindPipeline", LogLevel.INFO, NotifBindPipelineLogger$logManagedRow$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logRequestPipelineRun(String str) {
        Intrinsics.checkNotNullParameter(str, "notifKey");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("NotifBindPipeline", LogLevel.INFO, NotifBindPipelineLogger$logRequestPipelineRun$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logRequestPipelineRowNotSet(String str) {
        Intrinsics.checkNotNullParameter(str, "notifKey");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("NotifBindPipeline", LogLevel.INFO, NotifBindPipelineLogger$logRequestPipelineRowNotSet$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logStartPipeline(String str) {
        Intrinsics.checkNotNullParameter(str, "notifKey");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("NotifBindPipeline", LogLevel.INFO, NotifBindPipelineLogger$logStartPipeline$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logFinishedPipeline(String str, int i) {
        Intrinsics.checkNotNullParameter(str, "notifKey");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("NotifBindPipeline", LogLevel.INFO, NotifBindPipelineLogger$logFinishedPipeline$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setInt1(i);
        logBuffer.commit(obtain);
    }
}
