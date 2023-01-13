package com.android.systemui.statusbar.connectivity;

import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u001a\f\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u0002\u001a\f\u0010\u0000\u001a\u00020\u0001*\u00020\u0003H\u0002¨\u0006\u0004"}, mo65043d2 = {"minLog", "", "Landroid/telephony/ServiceState;", "Landroid/telephony/SignalStrength;", "SystemUI_nothingRelease"}, mo65044k = 2, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: MobileState.kt */
public final class MobileStateKt {
    /* access modifiers changed from: private */
    public static final String minLog(ServiceState serviceState) {
        return "serviceState={state=" + serviceState.getState() + ",isEmergencyOnly=" + serviceState.isEmergencyOnly() + ",roaming=" + serviceState.getRoaming() + ",operatorNameAlphaShort=" + serviceState.getOperatorAlphaShort() + '}';
    }

    /* access modifiers changed from: private */
    public static final String minLog(SignalStrength signalStrength) {
        return "signalStrength={isGsm=" + signalStrength.isGsm() + ",level=" + signalStrength.getLevel() + '}';
    }
}
