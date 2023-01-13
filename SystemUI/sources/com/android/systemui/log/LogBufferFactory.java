package com.android.systemui.log;

import android.app.ActivityManager;
import com.android.settingslib.datetime.ZoneGetter;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dump.DumpManager;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo65042d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\bH\u0002J\"\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\b2\b\b\u0002\u0010\u000f\u001a\u00020\u0010H\u0007R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0011"}, mo65043d2 = {"Lcom/android/systemui/log/LogBufferFactory;", "", "dumpManager", "Lcom/android/systemui/dump/DumpManager;", "logcatEchoTracker", "Lcom/android/systemui/log/LogcatEchoTracker;", "(Lcom/android/systemui/dump/DumpManager;Lcom/android/systemui/log/LogcatEchoTracker;)V", "adjustMaxSize", "", "requestedMaxSize", "create", "Lcom/android/systemui/log/LogBuffer;", "name", "", "maxSize", "systrace", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: LogBufferFactory.kt */
public final class LogBufferFactory {
    private final DumpManager dumpManager;
    private final LogcatEchoTracker logcatEchoTracker;

    public final LogBuffer create(String str, int i) {
        Intrinsics.checkNotNullParameter(str, ZoneGetter.KEY_DISPLAYNAME);
        return create$default(this, str, i, false, 4, (Object) null);
    }

    @Inject
    public LogBufferFactory(DumpManager dumpManager2, LogcatEchoTracker logcatEchoTracker2) {
        Intrinsics.checkNotNullParameter(dumpManager2, "dumpManager");
        Intrinsics.checkNotNullParameter(logcatEchoTracker2, "logcatEchoTracker");
        this.dumpManager = dumpManager2;
        this.logcatEchoTracker = logcatEchoTracker2;
    }

    private final int adjustMaxSize(int i) {
        return ActivityManager.isLowRamDeviceStatic() ? Math.min(i, 20) : i;
    }

    public static /* synthetic */ LogBuffer create$default(LogBufferFactory logBufferFactory, String str, int i, boolean z, int i2, Object obj) {
        if ((i2 & 4) != 0) {
            z = true;
        }
        return logBufferFactory.create(str, i, z);
    }

    public final LogBuffer create(String str, int i, boolean z) {
        Intrinsics.checkNotNullParameter(str, ZoneGetter.KEY_DISPLAYNAME);
        LogBuffer logBuffer = new LogBuffer(str, adjustMaxSize(i), this.logcatEchoTracker, z);
        this.dumpManager.registerBuffer(str, logBuffer);
        return logBuffer;
    }
}
