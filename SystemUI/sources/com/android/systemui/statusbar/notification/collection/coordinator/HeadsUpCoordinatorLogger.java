package com.android.systemui.statusbar.notification.collection.coordinator;

import android.util.Log;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessage;
import com.android.systemui.log.dagger.NotificationHeadsUpLog;
import com.android.systemui.statusbar.notification.collection.coordinator.HeadsUpCoordinator;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0011\b\u0017\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\u001e\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\rJ\u000e\u0010\u000f\u001a\u00020\t2\u0006\u0010\u0010\u001a\u00020\rJ\u0016\u0010\u0011\u001a\u00020\t2\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u000bJ\u0016\u0010\u0015\u001a\u00020\t2\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u000bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0016"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/collection/coordinator/HeadsUpCoordinatorLogger;", "", "buffer", "Lcom/android/systemui/log/LogBuffer;", "(Lcom/android/systemui/log/LogBuffer;)V", "verbose", "", "(Lcom/android/systemui/log/LogBuffer;Z)V", "logEvaluatingGroup", "", "groupKey", "", "numPostedEntries", "", "logicalGroupSize", "logEvaluatingGroups", "numGroups", "logPostedEntryWillEvaluate", "posted", "Lcom/android/systemui/statusbar/notification/collection/coordinator/HeadsUpCoordinator$PostedEntry;", "reason", "logPostedEntryWillNotEvaluate", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: HeadsUpCoordinatorLogger.kt */
public final class HeadsUpCoordinatorLogger {
    private final LogBuffer buffer;
    private final boolean verbose;

    public HeadsUpCoordinatorLogger(LogBuffer logBuffer, boolean z) {
        Intrinsics.checkNotNullParameter(logBuffer, "buffer");
        this.buffer = logBuffer;
        this.verbose = z;
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    @Inject
    public HeadsUpCoordinatorLogger(@NotificationHeadsUpLog LogBuffer logBuffer) {
        this(logBuffer, Log.isLoggable("HeadsUpCoordinator", 2));
        Intrinsics.checkNotNullParameter(logBuffer, "buffer");
    }

    public final void logPostedEntryWillEvaluate(HeadsUpCoordinator.PostedEntry postedEntry, String str) {
        Intrinsics.checkNotNullParameter(postedEntry, "posted");
        Intrinsics.checkNotNullParameter(str, "reason");
        if (this.verbose) {
            LogBuffer logBuffer = this.buffer;
            LogMessage obtain = logBuffer.obtain("HeadsUpCoordinator", LogLevel.VERBOSE, HeadsUpCoordinatorLogger$logPostedEntryWillEvaluate$2.INSTANCE);
            obtain.setStr1(postedEntry.getKey());
            obtain.setStr2(str);
            obtain.setBool1(postedEntry.getShouldHeadsUpEver());
            obtain.setBool2(postedEntry.getShouldHeadsUpAgain());
            logBuffer.commit(obtain);
        }
    }

    public final void logPostedEntryWillNotEvaluate(HeadsUpCoordinator.PostedEntry postedEntry, String str) {
        Intrinsics.checkNotNullParameter(postedEntry, "posted");
        Intrinsics.checkNotNullParameter(str, "reason");
        if (this.verbose) {
            LogBuffer logBuffer = this.buffer;
            LogMessage obtain = logBuffer.obtain("HeadsUpCoordinator", LogLevel.VERBOSE, HeadsUpCoordinatorLogger$logPostedEntryWillNotEvaluate$2.INSTANCE);
            obtain.setStr1(postedEntry.getKey());
            obtain.setStr2(str);
            logBuffer.commit(obtain);
        }
    }

    public final void logEvaluatingGroups(int i) {
        if (this.verbose) {
            LogBuffer logBuffer = this.buffer;
            LogMessage obtain = logBuffer.obtain("HeadsUpCoordinator", LogLevel.VERBOSE, HeadsUpCoordinatorLogger$logEvaluatingGroups$2.INSTANCE);
            obtain.setInt1(i);
            logBuffer.commit(obtain);
        }
    }

    public final void logEvaluatingGroup(String str, int i, int i2) {
        Intrinsics.checkNotNullParameter(str, "groupKey");
        if (this.verbose) {
            LogBuffer logBuffer = this.buffer;
            LogMessage obtain = logBuffer.obtain("HeadsUpCoordinator", LogLevel.VERBOSE, HeadsUpCoordinatorLogger$logEvaluatingGroup$2.INSTANCE);
            obtain.setStr1(str);
            obtain.setInt1(i);
            obtain.setInt2(i2);
            logBuffer.commit(obtain);
        }
    }
}
