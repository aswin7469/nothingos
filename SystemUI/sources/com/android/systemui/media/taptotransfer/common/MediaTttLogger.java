package com.android.systemui.media.taptotransfer.common;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessage;
import com.android.systemui.log.dagger.MediaTttSenderLogBuffer;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0001\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u000e\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u0003J\u0016\u0010\n\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\u00032\u0006\u0010\f\u001a\u00020\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\r"}, mo64987d2 = {"Lcom/android/systemui/media/taptotransfer/common/MediaTttLogger;", "", "deviceTypeTag", "", "buffer", "Lcom/android/systemui/log/LogBuffer;", "(Ljava/lang/String;Lcom/android/systemui/log/LogBuffer;)V", "logChipRemoval", "", "reason", "logStateChange", "stateName", "mediaRouteId", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: MediaTttLogger.kt */
public final class MediaTttLogger {
    private final LogBuffer buffer;
    private final String deviceTypeTag;

    public MediaTttLogger(String str, @MediaTttSenderLogBuffer LogBuffer logBuffer) {
        Intrinsics.checkNotNullParameter(str, "deviceTypeTag");
        Intrinsics.checkNotNullParameter(logBuffer, "buffer");
        this.deviceTypeTag = str;
        this.buffer = logBuffer;
    }

    public final void logStateChange(String str, String str2) {
        Intrinsics.checkNotNullParameter(str, "stateName");
        Intrinsics.checkNotNullParameter(str2, "mediaRouteId");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("MediaTtt" + this.deviceTypeTag, LogLevel.DEBUG, MediaTttLogger$logStateChange$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setStr2(str2);
        logBuffer.commit(obtain);
    }

    public final void logChipRemoval(String str) {
        Intrinsics.checkNotNullParameter(str, "reason");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("MediaTtt" + this.deviceTypeTag, LogLevel.DEBUG, MediaTttLogger$logChipRemoval$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }
}
