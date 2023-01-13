package com.android.p019wm.shell.tasksurfacehelper;

import android.view.SurfaceControl;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

/* renamed from: com.android.wm.shell.tasksurfacehelper.TaskSurfaceHelperController$TaskSurfaceHelperImpl$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C3614x3c1cf253 implements Consumer {
    public final /* synthetic */ Executor f$0;
    public final /* synthetic */ Consumer f$1;

    public /* synthetic */ C3614x3c1cf253(Executor executor, Consumer consumer) {
        this.f$0 = executor;
        this.f$1 = consumer;
    }

    public final void accept(Object obj) {
        this.f$0.execute(new C3616x3c1cf255(this.f$1, (SurfaceControl.ScreenshotHardwareBuffer) obj));
    }
}
