package com.android.systemui.privacy;

import com.android.internal.logging.UiEventLogger;
/* compiled from: PrivacyDialogEvent.kt */
/* loaded from: classes.dex */
public enum PrivacyDialogEvent implements UiEventLogger.UiEventEnum {
    PRIVACY_DIALOG_ITEM_CLICKED_TO_APP_SETTINGS(904),
    PRIVACY_DIALOG_DISMISSED(905);
    
    private final int _id;

    /* renamed from: values  reason: to resolve conflict with enum method */
    public static PrivacyDialogEvent[] valuesCustom() {
        PrivacyDialogEvent[] valuesCustom = values();
        PrivacyDialogEvent[] privacyDialogEventArr = new PrivacyDialogEvent[valuesCustom.length];
        System.arraycopy(valuesCustom, 0, privacyDialogEventArr, 0, valuesCustom.length);
        return privacyDialogEventArr;
    }

    PrivacyDialogEvent(int i) {
        this._id = i;
    }

    public int getId() {
        return this._id;
    }
}
