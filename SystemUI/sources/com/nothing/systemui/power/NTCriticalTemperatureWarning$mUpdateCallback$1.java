package com.nothing.systemui.power;

import android.os.PowerManager;
import android.util.Log;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.systemui.Dependency;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016¨\u0006\u0006"}, mo65043d2 = {"com/nothing/systemui/power/NTCriticalTemperatureWarning$mUpdateCallback$1", "Lcom/android/keyguard/KeyguardUpdateMonitorCallback;", "onCriticalTemperaturWarning", "", "warning", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: NTCriticalTemperatureWarning.kt */
public final class NTCriticalTemperatureWarning$mUpdateCallback$1 extends KeyguardUpdateMonitorCallback {
    final /* synthetic */ NTCriticalTemperatureWarning this$0;

    NTCriticalTemperatureWarning$mUpdateCallback$1(NTCriticalTemperatureWarning nTCriticalTemperatureWarning) {
        this.this$0 = nTCriticalTemperatureWarning;
    }

    public void onCriticalTemperaturWarning(boolean z) {
        PowerManager access$getMPowerMan$p = this.this$0.mPowerMan;
        Integer valueOf = access$getMPowerMan$p != null ? Integer.valueOf(access$getMPowerMan$p.getCurrentThermalStatus()) : null;
        if (valueOf != null) {
            boolean z2 = valueOf.intValue() >= 5;
            Log.d(this.this$0.TAG, "onCriticalTemperaturWarning: " + z2);
            if (!z2) {
                Object obj = Dependency.get(KeyguardUpdateMonitor.class);
                Intrinsics.checkNotNull(obj);
                ((KeyguardUpdateMonitor) obj).removeCallback(this);
                this.this$0.finish();
                return;
            }
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Int");
    }
}
