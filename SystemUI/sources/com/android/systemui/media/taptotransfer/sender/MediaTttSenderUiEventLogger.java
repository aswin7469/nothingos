package com.android.systemui.media.taptotransfer.sender;

import android.util.Log;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.dagger.SysUISingleton;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;

@SysUISingleton
@Metadata(mo65042d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u000e\u0010\t\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\u000bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\f"}, mo65043d2 = {"Lcom/android/systemui/media/taptotransfer/sender/MediaTttSenderUiEventLogger;", "", "logger", "Lcom/android/internal/logging/UiEventLogger;", "(Lcom/android/internal/logging/UiEventLogger;)V", "logSenderStateChange", "", "chipState", "Lcom/android/systemui/media/taptotransfer/sender/ChipStateSender;", "logUndoClicked", "undoUiEvent", "Lcom/android/internal/logging/UiEventLogger$UiEventEnum;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: MediaTttSenderUiEventLogger.kt */
public final class MediaTttSenderUiEventLogger {
    private final UiEventLogger logger;

    @Inject
    public MediaTttSenderUiEventLogger(UiEventLogger uiEventLogger) {
        Intrinsics.checkNotNullParameter(uiEventLogger, "logger");
        this.logger = uiEventLogger;
    }

    public final void logSenderStateChange(ChipStateSender chipStateSender) {
        Intrinsics.checkNotNullParameter(chipStateSender, "chipState");
        this.logger.log(chipStateSender.getUiEvent());
    }

    public final void logUndoClicked(UiEventLogger.UiEventEnum uiEventEnum) {
        Intrinsics.checkNotNullParameter(uiEventEnum, "undoUiEvent");
        if (!(uiEventEnum == MediaTttSenderUiEvents.MEDIA_TTT_SENDER_UNDO_TRANSFER_TO_RECEIVER_CLICKED || uiEventEnum == MediaTttSenderUiEvents.MEDIA_TTT_SENDER_UNDO_TRANSFER_TO_THIS_DEVICE_CLICKED)) {
            String simpleName = Reflection.getOrCreateKotlinClass(MediaTttSenderUiEventLogger.class).getSimpleName();
            Intrinsics.checkNotNull(simpleName);
            Log.w(simpleName, "Must pass an undo-specific UiEvent.");
            return;
        }
        this.logger.log(uiEventEnum);
    }
}
