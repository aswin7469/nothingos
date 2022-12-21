package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessage;
import com.android.systemui.log.dagger.NotificationLog;
import com.android.systemui.statusbar.notification.row.NotificationGuts;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;

@Metadata(mo64986d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0011\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u0016\u0010\t\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\n\u001a\u00020\u000bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\f"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/collection/coordinator/GutsCoordinatorLogger;", "", "buffer", "Lcom/android/systemui/log/LogBuffer;", "(Lcom/android/systemui/log/LogBuffer;)V", "logGutsClosed", "", "key", "", "logGutsOpened", "guts", "Lcom/android/systemui/statusbar/notification/row/NotificationGuts;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: GutsCoordinatorLogger.kt */
public final class GutsCoordinatorLogger {
    private final LogBuffer buffer;

    @Inject
    public GutsCoordinatorLogger(@NotificationLog LogBuffer logBuffer) {
        Intrinsics.checkNotNullParameter(logBuffer, "buffer");
        this.buffer = logBuffer;
    }

    public final void logGutsOpened(String str, NotificationGuts notificationGuts) {
        Intrinsics.checkNotNullParameter(str, "key");
        Intrinsics.checkNotNullParameter(notificationGuts, "guts");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("GutsCoordinator", LogLevel.DEBUG, GutsCoordinatorLogger$logGutsOpened$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setStr2(Reflection.getOrCreateKotlinClass(notificationGuts.getGutsContent().getClass()).getSimpleName());
        obtain.setBool1(notificationGuts.isLeavebehind());
        logBuffer.commit(obtain);
    }

    public final void logGutsClosed(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("GutsCoordinator", LogLevel.DEBUG, GutsCoordinatorLogger$logGutsClosed$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }
}
