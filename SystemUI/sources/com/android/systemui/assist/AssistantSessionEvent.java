package com.android.systemui.assist;

import com.android.internal.logging.UiEventLogger;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\n\b\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u00012\u00020\u0002B\u000f\b\u0002\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\b\u0010\u0006\u001a\u00020\u0004H\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000j\u0002\b\u0007j\u0002\b\bj\u0002\b\tj\u0002\b\nj\u0002\b\u000bj\u0002\b\fj\u0002\b\r¨\u0006\u000e"}, mo65043d2 = {"Lcom/android/systemui/assist/AssistantSessionEvent;", "", "Lcom/android/internal/logging/UiEventLogger$UiEventEnum;", "id", "", "(Ljava/lang/String;II)V", "getId", "ASSISTANT_SESSION_UNKNOWN", "ASSISTANT_SESSION_TIMEOUT_DISMISS", "ASSISTANT_SESSION_INVOCATION_START", "ASSISTANT_SESSION_INVOCATION_CANCELLED", "ASSISTANT_SESSION_USER_DISMISS", "ASSISTANT_SESSION_UPDATE", "ASSISTANT_SESSION_CLOSE", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: AssistantSessionEvent.kt */
public enum AssistantSessionEvent implements UiEventLogger.UiEventEnum {
    ASSISTANT_SESSION_UNKNOWN(0),
    ASSISTANT_SESSION_TIMEOUT_DISMISS(524),
    ASSISTANT_SESSION_INVOCATION_START(525),
    ASSISTANT_SESSION_INVOCATION_CANCELLED(526),
    ASSISTANT_SESSION_USER_DISMISS(527),
    ASSISTANT_SESSION_UPDATE(528),
    ASSISTANT_SESSION_CLOSE(529);
    

    /* renamed from: id */
    private final int f292id;

    private AssistantSessionEvent(int i) {
        this.f292id = i;
    }

    public int getId() {
        return this.f292id;
    }
}
