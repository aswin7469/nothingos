package com.android.systemui.media.muteawait;

import com.android.systemui.log.LogMessage;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(mo65042d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n¢\u0006\u0002\b\u0003"}, mo65043d2 = {"<anonymous>", "", "Lcom/android/systemui/log/LogMessage;", "invoke"}, mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: MediaMuteAwaitLogger.kt */
final class MediaMuteAwaitLogger$logMutedDeviceAdded$2 extends Lambda implements Function1<LogMessage, String> {
    public static final MediaMuteAwaitLogger$logMutedDeviceAdded$2 INSTANCE = new MediaMuteAwaitLogger$logMutedDeviceAdded$2();

    MediaMuteAwaitLogger$logMutedDeviceAdded$2() {
        super(1);
    }

    public final String invoke(LogMessage logMessage) {
        Intrinsics.checkNotNullParameter(logMessage, "$this$log");
        return "Muted device added: address=" + logMessage.getStr1() + " name=" + logMessage.getStr2() + " hasMediaUsage=" + logMessage.getBool1();
    }
}
