package com.android.systemui.demomode;

import android.database.ContentObserver;
import android.os.Handler;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016¨\u0006\u0006"}, mo64987d2 = {"com/android/systemui/demomode/DemoModeAvailabilityTracker$onObserver$1", "Landroid/database/ContentObserver;", "onChange", "", "selfChange", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: DemoModeAvailabilityTracker.kt */
public final class DemoModeAvailabilityTracker$onObserver$1 extends ContentObserver {
    final /* synthetic */ DemoModeAvailabilityTracker this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    DemoModeAvailabilityTracker$onObserver$1(DemoModeAvailabilityTracker demoModeAvailabilityTracker, Handler handler) {
        super(handler);
        this.this$0 = demoModeAvailabilityTracker;
    }

    public void onChange(boolean z) {
        boolean access$checkIsDemoModeOn = this.this$0.checkIsDemoModeOn();
        if (this.this$0.isInDemoMode() != access$checkIsDemoModeOn) {
            this.this$0.setInDemoMode(access$checkIsDemoModeOn);
            if (access$checkIsDemoModeOn) {
                this.this$0.onDemoModeStarted();
            } else {
                this.this$0.onDemoModeFinished();
            }
        }
    }
}
