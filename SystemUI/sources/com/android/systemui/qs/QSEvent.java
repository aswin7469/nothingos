package com.android.systemui.qs;

import com.android.internal.logging.UiEventLogger;
/* compiled from: QSEvents.kt */
/* loaded from: classes.dex */
public enum QSEvent implements UiEventLogger.UiEventEnum {
    QS_ACTION_CLICK(387),
    QS_ACTION_SECONDARY_CLICK(388),
    QS_ACTION_LONG_PRESS(389),
    QS_PANEL_EXPANDED(390),
    QS_PANEL_COLLAPSED(391),
    QS_TILE_VISIBLE(392),
    QQS_PANEL_EXPANDED(393),
    QQS_PANEL_COLLAPSED(394),
    QQS_TILE_VISIBLE(395);
    
    private final int _id;

    /* renamed from: values  reason: to resolve conflict with enum method */
    public static QSEvent[] valuesCustom() {
        QSEvent[] valuesCustom = values();
        QSEvent[] qSEventArr = new QSEvent[valuesCustom.length];
        System.arraycopy(valuesCustom, 0, qSEventArr, 0, valuesCustom.length);
        return qSEventArr;
    }

    QSEvent(int i) {
        this._id = i;
    }

    public int getId() {
        return this._id;
    }
}