package com.android.p019wm.shell.tasksurfacehelper;

import android.view.SurfaceControl;
import java.util.function.Consumer;

/* renamed from: com.android.wm.shell.tasksurfacehelper.TaskSurfaceHelperController$TaskSurfaceHelperImpl$$ExternalSyntheticLambda2 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C3616x3c1cf255 implements Runnable {
    public final /* synthetic */ Consumer f$0;
    public final /* synthetic */ SurfaceControl.ScreenshotHardwareBuffer f$1;

    public /* synthetic */ C3616x3c1cf255(Consumer consumer, SurfaceControl.ScreenshotHardwareBuffer screenshotHardwareBuffer) {
        this.f$0 = consumer;
        this.f$1 = screenshotHardwareBuffer;
    }

    public final void run() {
        this.f$0.accept(this.f$1);
    }
}
