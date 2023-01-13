package com.android.systemui.controls.management;

import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.settings.CurrentUserTracker;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0003H\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0007"}, mo65043d2 = {"com/android/systemui/controls/management/ControlsEditingActivity$currentUserTracker$1", "Lcom/android/systemui/settings/CurrentUserTracker;", "startingUser", "", "onUserSwitched", "", "newUserId", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ControlsEditingActivity.kt */
public final class ControlsEditingActivity$currentUserTracker$1 extends CurrentUserTracker {
    private final int startingUser;
    final /* synthetic */ ControlsEditingActivity this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    ControlsEditingActivity$currentUserTracker$1(ControlsEditingActivity controlsEditingActivity, BroadcastDispatcher broadcastDispatcher) {
        super(broadcastDispatcher);
        this.this$0 = controlsEditingActivity;
        this.startingUser = controlsEditingActivity.controller.getCurrentUserId();
    }

    public void onUserSwitched(int i) {
        if (i != this.startingUser) {
            stopTracking();
            this.this$0.finish();
        }
    }
}
