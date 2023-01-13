package com.android.systemui.p012qs;

import com.android.internal.logging.UiEventLogger;
import com.android.systemui.shared.system.SysUiStatsLog;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u000b\b\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u00012\u00020\u0002B\u000f\b\u0002\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\b\u0010\u0006\u001a\u00020\u0004H\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000j\u0002\b\u0007j\u0002\b\bj\u0002\b\tj\u0002\b\nj\u0002\b\u000bj\u0002\b\fj\u0002\b\rj\u0002\b\u000e¨\u0006\u000f"}, mo65043d2 = {"Lcom/android/systemui/qs/QSUserSwitcherEvent;", "", "Lcom/android/internal/logging/UiEventLogger$UiEventEnum;", "_id", "", "(Ljava/lang/String;II)V", "getId", "QS_USER_SWITCH", "QS_USER_DETAIL_OPEN", "QS_USER_DETAIL_CLOSE", "QS_USER_MORE_SETTINGS", "QS_USER_GUEST_ADD", "QS_USER_GUEST_WIPE", "QS_USER_GUEST_CONTINUE", "QS_USER_GUEST_REMOVE", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.systemui.qs.QSUserSwitcherEvent */
/* compiled from: QSEvents.kt */
public enum QSUserSwitcherEvent implements UiEventLogger.UiEventEnum {
    QS_USER_SWITCH(424),
    QS_USER_DETAIL_OPEN(425),
    QS_USER_DETAIL_CLOSE(SysUiStatsLog.LAUNCHER_LATENCY),
    QS_USER_MORE_SETTINGS(427),
    QS_USER_GUEST_ADD(754),
    QS_USER_GUEST_WIPE(755),
    QS_USER_GUEST_CONTINUE(756),
    QS_USER_GUEST_REMOVE(757);
    
    private final int _id;

    private QSUserSwitcherEvent(int i) {
        this._id = i;
    }

    public int getId() {
        return this._id;
    }
}
