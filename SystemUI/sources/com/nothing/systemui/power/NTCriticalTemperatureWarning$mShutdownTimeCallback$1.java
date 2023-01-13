package com.nothing.systemui.power;

import android.app.AlertDialog;
import android.os.Temperature;
import com.android.systemui.C1894R;
import com.android.systemui.power.TemperatureController;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000'\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\u0010\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\bH\u0016J\u0010\u0010\t\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u000bH\u0016¨\u0006\f"}, mo65043d2 = {"com/nothing/systemui/power/NTCriticalTemperatureWarning$mShutdownTimeCallback$1", "Lcom/android/systemui/power/TemperatureController$ShutdownTimeCallback;", "onCountdown", "", "millisUntilFinished", "", "onCountdownEnd", "isCancel", "", "onShutdownDialogShow", "temperature", "Landroid/os/Temperature;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: NTCriticalTemperatureWarning.kt */
public final class NTCriticalTemperatureWarning$mShutdownTimeCallback$1 implements TemperatureController.ShutdownTimeCallback {
    final /* synthetic */ NTCriticalTemperatureWarning this$0;

    NTCriticalTemperatureWarning$mShutdownTimeCallback$1(NTCriticalTemperatureWarning nTCriticalTemperatureWarning) {
        this.this$0 = nTCriticalTemperatureWarning;
    }

    public void onShutdownDialogShow(Temperature temperature) {
        Intrinsics.checkNotNullParameter(temperature, "temperature");
        this.this$0.mTemperatureType = temperature.getType();
        this.this$0.showShutdownDialog();
    }

    public void onCountdown(long j) {
        AlertDialog access$getMShutdownDialog$p = this.this$0.mShutdownDialog;
        if (access$getMShutdownDialog$p != null) {
            access$getMShutdownDialog$p.setMessage(this.this$0.getResources().getString(C1894R.string.shutdown_temperature_warning_content, new Object[]{Long.valueOf(j)}));
        }
    }

    public void onCountdownEnd(boolean z) {
        if (z && this.this$0.mShutdownDialog != null) {
            AlertDialog access$getMShutdownDialog$p = this.this$0.mShutdownDialog;
            if (access$getMShutdownDialog$p != null) {
                access$getMShutdownDialog$p.dismiss();
            }
            this.this$0.mShutdownDialog = null;
        }
    }
}
