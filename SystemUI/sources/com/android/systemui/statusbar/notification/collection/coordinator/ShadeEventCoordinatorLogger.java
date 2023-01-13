package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.dagger.NotificationLog;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0011\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0006\u0010\u0005\u001a\u00020\u0006J\u0006\u0010\u0007\u001a\u00020\u0006R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\b"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/collection/coordinator/ShadeEventCoordinatorLogger;", "", "buffer", "Lcom/android/systemui/log/LogBuffer;", "(Lcom/android/systemui/log/LogBuffer;)V", "logNotifRemovedByUser", "", "logShadeEmptied", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ShadeEventCoordinatorLogger.kt */
public final class ShadeEventCoordinatorLogger {
    private final LogBuffer buffer;

    @Inject
    public ShadeEventCoordinatorLogger(@NotificationLog LogBuffer logBuffer) {
        Intrinsics.checkNotNullParameter(logBuffer, "buffer");
        this.buffer = logBuffer;
    }

    public final void logShadeEmptied() {
        LogBuffer logBuffer = this.buffer;
        logBuffer.commit(logBuffer.obtain("ShadeEventCoordinator", LogLevel.DEBUG, ShadeEventCoordinatorLogger$logShadeEmptied$2.INSTANCE));
    }

    public final void logNotifRemovedByUser() {
        LogBuffer logBuffer = this.buffer;
        logBuffer.commit(logBuffer.obtain("ShadeEventCoordinator", LogLevel.DEBUG, ShadeEventCoordinatorLogger$logNotifRemovedByUser$2.INSTANCE));
    }
}
