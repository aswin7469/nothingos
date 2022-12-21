package com.android.systemui.statusbar.window;

import com.android.systemui.statusbar.CommandQueue;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J \u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u0005H\u0016¨\u0006\b"}, mo64987d2 = {"com/android/systemui/statusbar/window/StatusBarWindowStateController$commandQueueCallback$1", "Lcom/android/systemui/statusbar/CommandQueue$Callbacks;", "setWindowState", "", "displayId", "", "window", "state", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: StatusBarWindowStateController.kt */
public final class StatusBarWindowStateController$commandQueueCallback$1 implements CommandQueue.Callbacks {
    final /* synthetic */ StatusBarWindowStateController this$0;

    StatusBarWindowStateController$commandQueueCallback$1(StatusBarWindowStateController statusBarWindowStateController) {
        this.this$0 = statusBarWindowStateController;
    }

    public void setWindowState(int i, int i2, int i3) {
        this.this$0.setWindowState(i, i2, i3);
    }
}
