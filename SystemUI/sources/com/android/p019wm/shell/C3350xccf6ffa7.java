package com.android.p019wm.shell;

import android.content.Context;
import com.android.p019wm.shell.TaskViewFactoryController;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

/* renamed from: com.android.wm.shell.TaskViewFactoryController$TaskViewFactoryImpl$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C3350xccf6ffa7 implements Runnable {
    public final /* synthetic */ TaskViewFactoryController.TaskViewFactoryImpl f$0;
    public final /* synthetic */ Context f$1;
    public final /* synthetic */ Executor f$2;
    public final /* synthetic */ Consumer f$3;

    public /* synthetic */ C3350xccf6ffa7(TaskViewFactoryController.TaskViewFactoryImpl taskViewFactoryImpl, Context context, Executor executor, Consumer consumer) {
        this.f$0 = taskViewFactoryImpl;
        this.f$1 = context;
        this.f$2 = executor;
        this.f$3 = consumer;
    }

    public final void run() {
        this.f$0.mo48021x9ee06bad(this.f$1, this.f$2, this.f$3);
    }
}
