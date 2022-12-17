package com.android.settings.network.helper;

import android.telephony.TelephonyManager;
import android.telephony.UiccCardInfo;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class QueryEsimCardId implements Callable<AtomicIntegerArray> {
    private TelephonyManager mTelephonyManager;

    public QueryEsimCardId(TelephonyManager telephonyManager) {
        this.mTelephonyManager = telephonyManager;
    }

    public AtomicIntegerArray call() {
        List<UiccCardInfo> uiccCardsInfo = this.mTelephonyManager.getUiccCardsInfo();
        if (uiccCardsInfo == null) {
            return new AtomicIntegerArray(0);
        }
        return new AtomicIntegerArray(uiccCardsInfo.stream().filter(new QueryEsimCardId$$ExternalSyntheticLambda0()).filter(new QueryEsimCardId$$ExternalSyntheticLambda1()).mapToInt(new QueryEsimCardId$$ExternalSyntheticLambda2()).toArray());
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$call$0(UiccCardInfo uiccCardInfo) {
        return !uiccCardInfo.isRemovable() && uiccCardInfo.getCardId() >= 0;
    }
}
