package com.android.systemui.statusbar.events;

import com.android.systemui.statusbar.policy.BatteryController;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000#\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\b\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u000b\u001a\u00020\fH\u0002J \u0010\r\u001a\u00020\f2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00032\u0006\u0010\u0011\u001a\u00020\u0003H\u0016R\u001a\u0010\u0002\u001a\u00020\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0004\u0010\u0005\"\u0004\b\u0006\u0010\u0007R\u001a\u0010\b\u001a\u00020\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\u0005\"\u0004\b\n\u0010\u0007¨\u0006\u0012"}, mo65043d2 = {"com/android/systemui/statusbar/events/SystemEventCoordinator$batteryStateListener$1", "Lcom/android/systemui/statusbar/policy/BatteryController$BatteryStateChangeCallback;", "plugged", "", "getPlugged", "()Z", "setPlugged", "(Z)V", "stateKnown", "getStateKnown", "setStateKnown", "notifyListeners", "", "onBatteryLevelChanged", "level", "", "pluggedIn", "charging", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: SystemEventCoordinator.kt */
public final class SystemEventCoordinator$batteryStateListener$1 implements BatteryController.BatteryStateChangeCallback {
    private boolean plugged;
    private boolean stateKnown;
    final /* synthetic */ SystemEventCoordinator this$0;

    SystemEventCoordinator$batteryStateListener$1(SystemEventCoordinator systemEventCoordinator) {
        this.this$0 = systemEventCoordinator;
    }

    public final boolean getPlugged() {
        return this.plugged;
    }

    public final void setPlugged(boolean z) {
        this.plugged = z;
    }

    public final boolean getStateKnown() {
        return this.stateKnown;
    }

    public final void setStateKnown(boolean z) {
        this.stateKnown = z;
    }

    public void onBatteryLevelChanged(int i, boolean z, boolean z2) {
        if (!this.stateKnown) {
            this.stateKnown = true;
            this.plugged = z;
            notifyListeners();
        } else if (this.plugged != z) {
            this.plugged = z;
            notifyListeners();
        }
    }

    private final void notifyListeners() {
        if (this.plugged) {
            this.this$0.notifyPluggedIn();
        }
    }
}
