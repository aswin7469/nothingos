package com.android.systemui.privacy;

import com.android.internal.logging.UiEventLogger;
/* compiled from: PrivacyChipEvent.kt */
/* loaded from: classes.dex */
public enum PrivacyChipEvent implements UiEventLogger.UiEventEnum {
    ONGOING_INDICATORS_CHIP_VIEW(601),
    ONGOING_INDICATORS_CHIP_CLICK(602);
    
    private final int _id;

    /* renamed from: values  reason: to resolve conflict with enum method */
    public static PrivacyChipEvent[] valuesCustom() {
        PrivacyChipEvent[] valuesCustom = values();
        PrivacyChipEvent[] privacyChipEventArr = new PrivacyChipEvent[valuesCustom.length];
        System.arraycopy(valuesCustom, 0, privacyChipEventArr, 0, valuesCustom.length);
        return privacyChipEventArr;
    }

    PrivacyChipEvent(int i) {
        this._id = i;
    }

    public int getId() {
        return this._id;
    }
}
