package com.android.systemui.controls.management;

import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000\u0011\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\u0016¨\u0006\u0004"}, mo64987d2 = {"com/android/systemui/controls/management/ControlsEditingActivity$animateExitAndFinish$1", "Ljava/lang/Runnable;", "run", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ControlsEditingActivity.kt */
public final class ControlsEditingActivity$animateExitAndFinish$1 implements Runnable {
    final /* synthetic */ ControlsEditingActivity this$0;

    ControlsEditingActivity$animateExitAndFinish$1(ControlsEditingActivity controlsEditingActivity) {
        this.this$0 = controlsEditingActivity;
    }

    public void run() {
        this.this$0.finish();
    }
}
