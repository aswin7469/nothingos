package com.android.p019wm.shell;

import android.app.ActivityOptions;
import android.content.pm.ShortcutInfo;

/* renamed from: com.android.wm.shell.TaskView$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class TaskView$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ TaskView f$0;
    public final /* synthetic */ ShortcutInfo f$1;
    public final /* synthetic */ ActivityOptions f$2;

    public /* synthetic */ TaskView$$ExternalSyntheticLambda0(TaskView taskView, ShortcutInfo shortcutInfo, ActivityOptions activityOptions) {
        this.f$0 = taskView;
        this.f$1 = shortcutInfo;
        this.f$2 = activityOptions;
    }

    public final void run() {
        this.f$0.m3387lambda$startShortcutActivity$0$comandroidwmshellTaskView(this.f$1, this.f$2);
    }
}
