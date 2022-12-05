package com.android.wm.shell;

import android.content.Context;
import com.android.wm.shell.TaskViewFactoryController;
import com.android.wm.shell.common.ShellExecutor;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public class TaskViewFactoryController {
    private final TaskViewFactory mImpl = new TaskViewFactoryImpl();
    private final ShellExecutor mShellExecutor;
    private final ShellTaskOrganizer mTaskOrganizer;

    public TaskViewFactoryController(ShellTaskOrganizer shellTaskOrganizer, ShellExecutor shellExecutor) {
        this.mTaskOrganizer = shellTaskOrganizer;
        this.mShellExecutor = shellExecutor;
    }

    public TaskViewFactory asTaskViewFactory() {
        return this.mImpl;
    }

    public void create(Context context, Executor executor, final Consumer<TaskView> consumer) {
        final TaskView taskView = new TaskView(context, this.mTaskOrganizer);
        executor.execute(new Runnable() { // from class: com.android.wm.shell.TaskViewFactoryController$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                consumer.accept(taskView);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class TaskViewFactoryImpl implements TaskViewFactory {
        private TaskViewFactoryImpl() {
        }

        @Override // com.android.wm.shell.TaskViewFactory
        public void create(final Context context, final Executor executor, final Consumer<TaskView> consumer) {
            TaskViewFactoryController.this.mShellExecutor.execute(new Runnable() { // from class: com.android.wm.shell.TaskViewFactoryController$TaskViewFactoryImpl$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    TaskViewFactoryController.TaskViewFactoryImpl.this.lambda$create$0(context, executor, consumer);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$create$0(Context context, Executor executor, Consumer consumer) {
            TaskViewFactoryController.this.create(context, executor, consumer);
        }
    }
}
