package com.android.p019wm.shell;

import android.app.ActivityOptions;
import android.app.PendingIntent;
import android.content.Intent;

/* renamed from: com.android.wm.shell.TaskView$$ExternalSyntheticLambda13 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class TaskView$$ExternalSyntheticLambda13 implements Runnable {
    public final /* synthetic */ TaskView f$0;
    public final /* synthetic */ PendingIntent f$1;
    public final /* synthetic */ Intent f$2;
    public final /* synthetic */ ActivityOptions f$3;

    public /* synthetic */ TaskView$$ExternalSyntheticLambda13(TaskView taskView, PendingIntent pendingIntent, Intent intent, ActivityOptions activityOptions) {
        this.f$0 = taskView;
        this.f$1 = pendingIntent;
        this.f$2 = intent;
        this.f$3 = activityOptions;
    }

    public final void run() {
        this.f$0.m3390lambda$startActivity$1$comandroidwmshellTaskView(this.f$1, this.f$2, this.f$3);
    }
}
