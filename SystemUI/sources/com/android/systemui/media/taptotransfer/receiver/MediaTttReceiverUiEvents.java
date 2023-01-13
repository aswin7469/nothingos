package com.android.systemui.media.taptotransfer.receiver;

import com.android.internal.logging.UiEventLogger;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0007\b\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u00012\u00020\u0002B\u000f\b\u0002\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\b\u0010\b\u001a\u00020\u0004H\u0016R\u0011\u0010\u0003\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007j\u0002\b\tj\u0002\b\n¨\u0006\u000b"}, mo65043d2 = {"Lcom/android/systemui/media/taptotransfer/receiver/MediaTttReceiverUiEvents;", "", "Lcom/android/internal/logging/UiEventLogger$UiEventEnum;", "metricId", "", "(Ljava/lang/String;II)V", "getMetricId", "()I", "getId", "MEDIA_TTT_RECEIVER_CLOSE_TO_SENDER", "MEDIA_TTT_RECEIVER_FAR_FROM_SENDER", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: MediaTttReceiverUiEventLogger.kt */
public enum MediaTttReceiverUiEvents implements UiEventLogger.UiEventEnum {
    MEDIA_TTT_RECEIVER_CLOSE_TO_SENDER(982),
    MEDIA_TTT_RECEIVER_FAR_FROM_SENDER(983);
    
    private final int metricId;

    private MediaTttReceiverUiEvents(int i) {
        this.metricId = i;
    }

    public final int getMetricId() {
        return this.metricId;
    }

    public int getId() {
        return this.metricId;
    }
}
