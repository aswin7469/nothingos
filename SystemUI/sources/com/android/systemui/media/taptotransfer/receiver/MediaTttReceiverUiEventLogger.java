package com.android.systemui.media.taptotransfer.receiver;

import com.android.internal.logging.UiEventLogger;
import com.android.systemui.dagger.SysUISingleton;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo64986d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\t"}, mo64987d2 = {"Lcom/android/systemui/media/taptotransfer/receiver/MediaTttReceiverUiEventLogger;", "", "logger", "Lcom/android/internal/logging/UiEventLogger;", "(Lcom/android/internal/logging/UiEventLogger;)V", "logReceiverStateChange", "", "chipState", "Lcom/android/systemui/media/taptotransfer/receiver/ChipStateReceiver;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: MediaTttReceiverUiEventLogger.kt */
public final class MediaTttReceiverUiEventLogger {
    private final UiEventLogger logger;

    @Inject
    public MediaTttReceiverUiEventLogger(UiEventLogger uiEventLogger) {
        Intrinsics.checkNotNullParameter(uiEventLogger, "logger");
        this.logger = uiEventLogger;
    }

    public final void logReceiverStateChange(ChipStateReceiver chipStateReceiver) {
        Intrinsics.checkNotNullParameter(chipStateReceiver, "chipState");
        this.logger.log(chipStateReceiver.getUiEvent());
    }
}
