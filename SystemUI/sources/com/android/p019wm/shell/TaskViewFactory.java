package com.android.p019wm.shell;

import android.content.Context;
import com.android.p019wm.shell.common.annotations.ExternalThread;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

@ExternalThread
/* renamed from: com.android.wm.shell.TaskViewFactory */
public interface TaskViewFactory {
    void create(Context context, Executor executor, Consumer<TaskView> consumer);
}
