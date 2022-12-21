package com.android.systemui.p012qs;

import com.android.internal.logging.UiEventLogger;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\t\b\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u00012\u00020\u0002B\u000f\b\u0002\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\b\u0010\u0006\u001a\u00020\u0004H\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000j\u0002\b\u0007j\u0002\b\bj\u0002\b\tj\u0002\b\nj\u0002\b\u000bj\u0002\b\f¨\u0006\r"}, mo64987d2 = {"Lcom/android/systemui/qs/QSDndEvent;", "", "Lcom/android/internal/logging/UiEventLogger$UiEventEnum;", "_id", "", "(Ljava/lang/String;II)V", "getId", "QS_DND_CONDITION_SELECT", "QS_DND_TIME_UP", "QS_DND_TIME_DOWN", "QS_DND_DIALOG_ENABLE_FOREVER", "QS_DND_DIALOG_ENABLE_UNTIL_ALARM", "QS_DND_DIALOG_ENABLE_UNTIL_COUNTDOWN", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.systemui.qs.QSDndEvent */
/* compiled from: QSEvents.kt */
public enum QSDndEvent implements UiEventLogger.UiEventEnum {
    QS_DND_CONDITION_SELECT(420),
    QS_DND_TIME_UP(422),
    QS_DND_TIME_DOWN(423),
    QS_DND_DIALOG_ENABLE_FOREVER(946),
    QS_DND_DIALOG_ENABLE_UNTIL_ALARM(947),
    QS_DND_DIALOG_ENABLE_UNTIL_COUNTDOWN(948);
    
    private final int _id;

    private QSDndEvent(int i) {
        this._id = i;
    }

    public int getId() {
        return this._id;
    }
}
