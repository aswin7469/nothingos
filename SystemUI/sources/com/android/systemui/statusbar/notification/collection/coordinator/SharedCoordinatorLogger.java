package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessage;
import com.android.systemui.log.dagger.NotificationLog;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0011\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u0016\u0010\t\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\r"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/collection/coordinator/SharedCoordinatorLogger;", "", "buffer", "Lcom/android/systemui/log/LogBuffer;", "(Lcom/android/systemui/log/LogBuffer;)V", "logKeyguardCoordinatorInvalidated", "", "reason", "", "logUserOrProfileChanged", "userId", "", "profiles", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: SharedCoordinatorLogger.kt */
public final class SharedCoordinatorLogger {
    private final LogBuffer buffer;

    @Inject
    public SharedCoordinatorLogger(@NotificationLog LogBuffer logBuffer) {
        Intrinsics.checkNotNullParameter(logBuffer, "buffer");
        this.buffer = logBuffer;
    }

    public final void logUserOrProfileChanged(int i, String str) {
        Intrinsics.checkNotNullParameter(str, "profiles");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("NotCurrentUserFilter", LogLevel.INFO, SharedCoordinatorLogger$logUserOrProfileChanged$2.INSTANCE);
        obtain.setInt1(i);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logKeyguardCoordinatorInvalidated(String str) {
        Intrinsics.checkNotNullParameter(str, "reason");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("KeyguardCoordinator", LogLevel.DEBUG, SharedCoordinatorLogger$logKeyguardCoordinatorInvalidated$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }
}
