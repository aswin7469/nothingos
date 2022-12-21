package com.android.systemui.media.muteawait;

import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessage;
import com.android.systemui.log.dagger.MediaMuteAwaitLog;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo64986d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u0011\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u001e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\b2\u0006\u0010\n\u001a\u00020\u000bJ&\u0010\f\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\b2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\r\u001a\u00020\u000bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\u000e"}, mo64987d2 = {"Lcom/android/systemui/media/muteawait/MediaMuteAwaitLogger;", "", "buffer", "Lcom/android/systemui/log/LogBuffer;", "(Lcom/android/systemui/log/LogBuffer;)V", "logMutedDeviceAdded", "", "deviceAddress", "", "deviceName", "hasMediaUsage", "", "logMutedDeviceRemoved", "isMostRecentDevice", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: MediaMuteAwaitLogger.kt */
public final class MediaMuteAwaitLogger {
    private final LogBuffer buffer;

    @Inject
    public MediaMuteAwaitLogger(@MediaMuteAwaitLog LogBuffer logBuffer) {
        Intrinsics.checkNotNullParameter(logBuffer, "buffer");
        this.buffer = logBuffer;
    }

    public final void logMutedDeviceAdded(String str, String str2, boolean z) {
        Intrinsics.checkNotNullParameter(str, "deviceAddress");
        Intrinsics.checkNotNullParameter(str2, "deviceName");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("MediaMuteAwait", LogLevel.DEBUG, MediaMuteAwaitLogger$logMutedDeviceAdded$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setStr2(str2);
        obtain.setBool1(z);
        logBuffer.commit(obtain);
    }

    public final void logMutedDeviceRemoved(String str, String str2, boolean z, boolean z2) {
        Intrinsics.checkNotNullParameter(str, "deviceAddress");
        Intrinsics.checkNotNullParameter(str2, "deviceName");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("MediaMuteAwait", LogLevel.DEBUG, MediaMuteAwaitLogger$logMutedDeviceRemoved$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setStr2(str2);
        obtain.setBool1(z);
        obtain.setBool2(z2);
        logBuffer.commit(obtain);
    }
}
