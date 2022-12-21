package com.nothing.systemui.power;

import android.util.Log;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000\u0011\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\u0016¨\u0006\u0004"}, mo64987d2 = {"com/nothing/systemui/power/PowerNotificationWarningsEx$mDismissRunnable$1", "Ljava/lang/Runnable;", "run", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: PowerNotificationWarningsEx.kt */
public final class PowerNotificationWarningsEx$mDismissRunnable$1 implements Runnable {
    final /* synthetic */ PowerNotificationWarningsEx this$0;

    PowerNotificationWarningsEx$mDismissRunnable$1(PowerNotificationWarningsEx powerNotificationWarningsEx) {
        this.this$0 = powerNotificationWarningsEx;
    }

    public void run() {
        this.this$0.lastUpdateTime = System.currentTimeMillis();
        Log.i(this.this$0.TAG, " mDismissRunnable:");
        this.this$0.dismissCriticaTemperatureWarningView();
    }
}
