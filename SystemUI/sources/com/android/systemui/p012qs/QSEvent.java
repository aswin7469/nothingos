package com.android.systemui.p012qs;

import com.android.internal.logging.UiEventLogger;
import com.android.systemui.shared.system.SysUiStatsLog;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\f\b\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u00012\u00020\u0002B\u000f\b\u0002\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\b\u0010\u0006\u001a\u00020\u0004H\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000j\u0002\b\u0007j\u0002\b\bj\u0002\b\tj\u0002\b\nj\u0002\b\u000bj\u0002\b\fj\u0002\b\rj\u0002\b\u000ej\u0002\b\u000f¨\u0006\u0010"}, mo65043d2 = {"Lcom/android/systemui/qs/QSEvent;", "", "Lcom/android/internal/logging/UiEventLogger$UiEventEnum;", "_id", "", "(Ljava/lang/String;II)V", "getId", "QS_ACTION_CLICK", "QS_ACTION_SECONDARY_CLICK", "QS_ACTION_LONG_PRESS", "QS_PANEL_EXPANDED", "QS_PANEL_COLLAPSED", "QS_TILE_VISIBLE", "QQS_PANEL_EXPANDED", "QQS_PANEL_COLLAPSED", "QQS_TILE_VISIBLE", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.systemui.qs.QSEvent */
/* compiled from: QSEvents.kt */
public enum QSEvent implements UiEventLogger.UiEventEnum {
    QS_ACTION_CLICK(387),
    QS_ACTION_SECONDARY_CLICK(388),
    QS_ACTION_LONG_PRESS(389),
    QS_PANEL_EXPANDED(390),
    QS_PANEL_COLLAPSED(391),
    QS_TILE_VISIBLE(392),
    QQS_PANEL_EXPANDED(SysUiStatsLog.ACCESSIBILITY_FLOATING_MENU_UI_CHANGED),
    QQS_PANEL_COLLAPSED(394),
    QQS_TILE_VISIBLE(395);
    
    private final int _id;

    private QSEvent(int i) {
        this._id = i;
    }

    public int getId() {
        return this._id;
    }
}
