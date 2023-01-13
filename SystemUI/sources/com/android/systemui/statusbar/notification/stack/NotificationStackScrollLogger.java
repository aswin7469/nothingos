package com.android.systemui.statusbar.notification.stack;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessage;
import com.android.systemui.log.dagger.NotificationHeadsUpLog;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0011\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0016\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nJ\u0016\u0010\u000b\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\f\u001a\u00020\bJ\u001e\u0010\r\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000fR\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0011"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/stack/NotificationStackScrollLogger;", "", "buffer", "Lcom/android/systemui/log/LogBuffer;", "(Lcom/android/systemui/log/LogBuffer;)V", "hunAnimationEventAdded", "", "key", "", "type", "", "hunAnimationSkipped", "reason", "hunSkippedForUnexpectedState", "expected", "", "actual", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: NotificationStackScrollLogger.kt */
public final class NotificationStackScrollLogger {
    private final LogBuffer buffer;

    @Inject
    public NotificationStackScrollLogger(@NotificationHeadsUpLog LogBuffer logBuffer) {
        Intrinsics.checkNotNullParameter(logBuffer, "buffer");
        this.buffer = logBuffer;
    }

    public final void hunAnimationSkipped(String str, String str2) {
        Intrinsics.checkNotNullParameter(str, "key");
        Intrinsics.checkNotNullParameter(str2, "reason");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("NotificationStackScroll", LogLevel.INFO, NotificationStackScrollLogger$hunAnimationSkipped$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setStr2(str2);
        logBuffer.commit(obtain);
    }

    public final void hunAnimationEventAdded(String str, int i) {
        String str2;
        Intrinsics.checkNotNullParameter(str, "key");
        if (i != 0) {
            switch (i) {
                case 11:
                    str2 = "HEADS_UP_APPEAR";
                    break;
                case 12:
                    str2 = "HEADS_UP_DISAPPEAR";
                    break;
                case 13:
                    str2 = "HEADS_UP_DISAPPEAR_CLICK";
                    break;
                case 14:
                    str2 = "HEADS_UP_OTHER";
                    break;
                default:
                    str2 = String.valueOf(i);
                    break;
            }
        } else {
            str2 = "ADD";
        }
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("NotificationStackScroll", LogLevel.INFO, NotificationStackScrollLogger$hunAnimationEventAdded$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setStr2(str2);
        logBuffer.commit(obtain);
    }

    public final void hunSkippedForUnexpectedState(String str, boolean z, boolean z2) {
        Intrinsics.checkNotNullParameter(str, "key");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("NotificationStackScroll", LogLevel.INFO, NotificationStackScrollLogger$hunSkippedForUnexpectedState$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setBool1(z);
        obtain.setBool2(z2);
        logBuffer.commit(obtain);
    }
}
