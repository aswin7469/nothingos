package com.android.systemui.demomode;

import android.content.Context;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000\u0013\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\u0016J\b\u0010\u0004\u001a\u00020\u0003H\u0016J\b\u0010\u0005\u001a\u00020\u0003H\u0016¨\u0006\u0006"}, mo64987d2 = {"com/android/systemui/demomode/DemoModeController$tracker$1", "Lcom/android/systemui/demomode/DemoModeAvailabilityTracker;", "onDemoModeAvailabilityChanged", "", "onDemoModeFinished", "onDemoModeStarted", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: DemoModeController.kt */
public final class DemoModeController$tracker$1 extends DemoModeAvailabilityTracker {
    final /* synthetic */ DemoModeController this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    DemoModeController$tracker$1(DemoModeController demoModeController, Context context) {
        super(context);
        this.this$0 = demoModeController;
    }

    public void onDemoModeAvailabilityChanged() {
        this.this$0.setIsDemoModeAllowed(isDemoModeAvailable());
    }

    public void onDemoModeStarted() {
        if (this.this$0.isInDemoMode() != isInDemoMode()) {
            this.this$0.enterDemoMode();
        }
    }

    public void onDemoModeFinished() {
        if (this.this$0.isInDemoMode() != isInDemoMode()) {
            this.this$0.exitDemoMode();
        }
    }
}
