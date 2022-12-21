package com.android.systemui.media;

import android.content.ComponentName;
import com.android.launcher3.icons.cache.BaseIconCache;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessage;
import com.android.systemui.log.dagger.MediaBrowserLog;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo64986d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0011\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0016\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nJ\u000e\u0010\u000b\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u0016\u0010\f\u001a\u00020\u00062\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u0007\u001a\u00020\bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\u000f"}, mo64987d2 = {"Lcom/android/systemui/media/ResumeMediaBrowserLogger;", "", "buffer", "Lcom/android/systemui/log/LogBuffer;", "(Lcom/android/systemui/log/LogBuffer;)V", "logConnection", "", "componentName", "Landroid/content/ComponentName;", "reason", "", "logDisconnect", "logSessionDestroyed", "isBrowserConnected", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ResumeMediaBrowserLogger.kt */
public final class ResumeMediaBrowserLogger {
    private final LogBuffer buffer;

    @Inject
    public ResumeMediaBrowserLogger(@MediaBrowserLog LogBuffer logBuffer) {
        Intrinsics.checkNotNullParameter(logBuffer, "buffer");
        this.buffer = logBuffer;
    }

    public final void logConnection(ComponentName componentName, String str) {
        Intrinsics.checkNotNullParameter(componentName, BaseIconCache.IconDB.COLUMN_COMPONENT);
        Intrinsics.checkNotNullParameter(str, "reason");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("MediaBrowser", LogLevel.DEBUG, ResumeMediaBrowserLogger$logConnection$2.INSTANCE);
        obtain.setStr1(componentName.toShortString());
        obtain.setStr2(str);
        logBuffer.commit(obtain);
    }

    public final void logDisconnect(ComponentName componentName) {
        Intrinsics.checkNotNullParameter(componentName, BaseIconCache.IconDB.COLUMN_COMPONENT);
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("MediaBrowser", LogLevel.DEBUG, ResumeMediaBrowserLogger$logDisconnect$2.INSTANCE);
        obtain.setStr1(componentName.toShortString());
        logBuffer.commit(obtain);
    }

    public final void logSessionDestroyed(boolean z, ComponentName componentName) {
        Intrinsics.checkNotNullParameter(componentName, BaseIconCache.IconDB.COLUMN_COMPONENT);
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("MediaBrowser", LogLevel.DEBUG, ResumeMediaBrowserLogger$logSessionDestroyed$2.INSTANCE);
        obtain.setBool1(z);
        obtain.setStr1(componentName.toShortString());
        logBuffer.commit(obtain);
    }
}
