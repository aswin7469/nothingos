package com.android.systemui.statusbar.notification.interruption;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessage;
import com.android.systemui.log.dagger.NotificationHeadsUpLog;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0011\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u000e\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nJ\u000e\u0010\u000b\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nJ\u000e\u0010\f\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nJ\u000e\u0010\r\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nJ\u000e\u0010\u000e\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u000f"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/interruption/HeadsUpViewBinderLogger;", "", "buffer", "Lcom/android/systemui/log/LogBuffer;", "(Lcom/android/systemui/log/LogBuffer;)V", "getBuffer", "()Lcom/android/systemui/log/LogBuffer;", "currentOngoingBindingAborted", "", "key", "", "entryBoundSuccessfully", "entryContentViewMarkedFreeable", "entryUnbound", "startBindingHun", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: HeadsUpViewBinderLogger.kt */
public final class HeadsUpViewBinderLogger {
    private final LogBuffer buffer;

    @Inject
    public HeadsUpViewBinderLogger(@NotificationHeadsUpLog LogBuffer logBuffer) {
        Intrinsics.checkNotNullParameter(logBuffer, "buffer");
        this.buffer = logBuffer;
    }

    public final LogBuffer getBuffer() {
        return this.buffer;
    }

    public final void startBindingHun(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("HeadsUpViewBinder", LogLevel.INFO, HeadsUpViewBinderLogger$startBindingHun$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void currentOngoingBindingAborted(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("HeadsUpViewBinder", LogLevel.INFO, HeadsUpViewBinderLogger$currentOngoingBindingAborted$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void entryBoundSuccessfully(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("HeadsUpViewBinder", LogLevel.INFO, HeadsUpViewBinderLogger$entryBoundSuccessfully$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void entryUnbound(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("HeadsUpViewBinder", LogLevel.INFO, HeadsUpViewBinderLogger$entryUnbound$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void entryContentViewMarkedFreeable(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("HeadsUpViewBinder", LogLevel.INFO, HeadsUpViewBinderLogger$entryContentViewMarkedFreeable$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }
}
