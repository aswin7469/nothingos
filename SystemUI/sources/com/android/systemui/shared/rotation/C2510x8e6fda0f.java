package com.android.systemui.shared.rotation;

import android.app.ActivityManager;
import com.android.systemui.shared.rotation.RotationButtonController;
import java.util.function.Consumer;

/* renamed from: com.android.systemui.shared.rotation.RotationButtonController$TaskStackListenerImpl$$ExternalSyntheticLambda1 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C2510x8e6fda0f implements Consumer {
    public final /* synthetic */ RotationButtonController.TaskStackListenerImpl f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ C2510x8e6fda0f(RotationButtonController.TaskStackListenerImpl taskStackListenerImpl, int i) {
        this.f$0 = taskStackListenerImpl;
        this.f$1 = i;
    }

    public final void accept(Object obj) {
        this.f$0.mo38006xe6bb7095(this.f$1, (ActivityManager.RunningTaskInfo) obj);
    }
}
