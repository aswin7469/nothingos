package com.android.systemui.statusbar.phone.ongoingcall;

import com.android.systemui.plugins.statusbar.StatusBarStateController;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016¨\u0006\u0006"}, mo64987d2 = {"com/android/systemui/statusbar/phone/ongoingcall/OngoingCallController$statusBarStateListener$1", "Lcom/android/systemui/plugins/statusbar/StatusBarStateController$StateListener;", "onFullscreenStateChanged", "", "isFullscreen", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: OngoingCallController.kt */
public final class OngoingCallController$statusBarStateListener$1 implements StatusBarStateController.StateListener {
    final /* synthetic */ OngoingCallController this$0;

    OngoingCallController$statusBarStateListener$1(OngoingCallController ongoingCallController) {
        this.this$0 = ongoingCallController;
    }

    public void onFullscreenStateChanged(boolean z) {
        this.this$0.isFullscreen = z;
        this.this$0.updateChipClickListener();
        this.this$0.updateGestureListening();
    }
}
