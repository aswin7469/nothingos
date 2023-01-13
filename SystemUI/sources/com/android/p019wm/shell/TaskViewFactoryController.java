package com.android.p019wm.shell;

import android.content.Context;
import com.android.p019wm.shell.common.ShellExecutor;
import com.android.p019wm.shell.common.SyncTransactionQueue;
import com.android.p019wm.shell.common.annotations.ExternalThread;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

/* renamed from: com.android.wm.shell.TaskViewFactoryController */
public class TaskViewFactoryController {
    private final TaskViewFactory mImpl = new TaskViewFactoryImpl();
    /* access modifiers changed from: private */
    public final ShellExecutor mShellExecutor;
    private final SyncTransactionQueue mSyncQueue;
    private final ShellTaskOrganizer mTaskOrganizer;
    private final TaskViewTransitions mTaskViewTransitions;

    public TaskViewFactoryController(ShellTaskOrganizer shellTaskOrganizer, ShellExecutor shellExecutor, SyncTransactionQueue syncTransactionQueue, TaskViewTransitions taskViewTransitions) {
        this.mTaskOrganizer = shellTaskOrganizer;
        this.mShellExecutor = shellExecutor;
        this.mSyncQueue = syncTransactionQueue;
        this.mTaskViewTransitions = taskViewTransitions;
    }

    public TaskViewFactoryController(ShellTaskOrganizer shellTaskOrganizer, ShellExecutor shellExecutor, SyncTransactionQueue syncTransactionQueue) {
        this.mTaskOrganizer = shellTaskOrganizer;
        this.mShellExecutor = shellExecutor;
        this.mSyncQueue = syncTransactionQueue;
        this.mTaskViewTransitions = null;
    }

    public TaskViewFactory asTaskViewFactory() {
        return this.mImpl;
    }

    public void create(Context context, Executor executor, Consumer<TaskView> consumer) {
        executor.execute(new TaskViewFactoryController$$ExternalSyntheticLambda0(consumer, new TaskView(context, this.mTaskOrganizer, this.mTaskViewTransitions, this.mSyncQueue)));
    }

    /* renamed from: com.android.wm.shell.TaskViewFactoryController$TaskViewFactoryImpl */
    private class TaskViewFactoryImpl implements TaskViewFactory {
        private TaskViewFactoryImpl() {
        }

        @ExternalThread
        public void create(Context context, Executor executor, Consumer<TaskView> consumer) {
            TaskViewFactoryController.this.mShellExecutor.execute(new C3360xccf6ffa7(this, context, executor, consumer));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$create$0$com-android-wm-shell-TaskViewFactoryController$TaskViewFactoryImpl */
        public /* synthetic */ void mo48030x9ee06bad(Context context, Executor executor, Consumer consumer) {
            TaskViewFactoryController.this.create(context, executor, consumer);
        }
    }
}
