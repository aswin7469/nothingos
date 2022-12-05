package com.android.systemui.qs;

import com.android.internal.logging.UiEventLogger;
/* compiled from: QSEvents.kt */
/* loaded from: classes.dex */
public enum QSEditEvent implements UiEventLogger.UiEventEnum {
    QS_EDIT_REMOVE(210),
    QS_EDIT_ADD(211),
    QS_EDIT_MOVE(212),
    QS_EDIT_OPEN(213),
    QS_EDIT_CLOSED(214),
    QS_EDIT_RESET(215);
    
    private final int _id;

    /* renamed from: values  reason: to resolve conflict with enum method */
    public static QSEditEvent[] valuesCustom() {
        QSEditEvent[] valuesCustom = values();
        QSEditEvent[] qSEditEventArr = new QSEditEvent[valuesCustom.length];
        System.arraycopy(valuesCustom, 0, qSEditEventArr, 0, valuesCustom.length);
        return qSEditEventArr;
    }

    QSEditEvent(int i) {
        this._id = i;
    }

    public int getId() {
        return this._id;
    }
}
