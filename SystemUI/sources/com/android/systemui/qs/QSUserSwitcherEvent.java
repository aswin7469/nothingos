package com.android.systemui.qs;

import com.android.internal.logging.UiEventLogger;
/* compiled from: QSEvents.kt */
/* loaded from: classes.dex */
public enum QSUserSwitcherEvent implements UiEventLogger.UiEventEnum {
    QS_USER_SWITCH(424),
    QS_USER_DETAIL_OPEN(425),
    QS_USER_DETAIL_CLOSE(426),
    QS_USER_MORE_SETTINGS(427),
    QS_USER_GUEST_ADD(754),
    QS_USER_GUEST_WIPE(755),
    QS_USER_GUEST_CONTINUE(756),
    QS_USER_GUEST_REMOVE(757);
    
    private final int _id;

    /* renamed from: values  reason: to resolve conflict with enum method */
    public static QSUserSwitcherEvent[] valuesCustom() {
        QSUserSwitcherEvent[] valuesCustom = values();
        QSUserSwitcherEvent[] qSUserSwitcherEventArr = new QSUserSwitcherEvent[valuesCustom.length];
        System.arraycopy(valuesCustom, 0, qSUserSwitcherEventArr, 0, valuesCustom.length);
        return qSUserSwitcherEventArr;
    }

    QSUserSwitcherEvent(int i) {
        this._id = i;
    }

    public int getId() {
        return this._id;
    }
}
