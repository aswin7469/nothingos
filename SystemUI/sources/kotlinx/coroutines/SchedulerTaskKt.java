package kotlinx.coroutines;

import kotlin.Metadata;
import kotlinx.coroutines.scheduling.Task;
import kotlinx.coroutines.scheduling.TaskContext;

@Metadata(mo65042d1 = {"\u0000\u001e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0003\u001a\u0011\u0010\t\u001a\u00020\n*\u00060\u0001j\u0002`\u0002H\b\"&\u0010\u0000\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0003j\u0002`\u00048@X\u0004¢\u0006\f\u0012\u0004\b\u0005\u0010\u0006\u001a\u0004\b\u0007\u0010\b*\f\b\u0000\u0010\u000b\"\u00020\u00032\u00020\u0003*\f\b\u0000\u0010\f\"\u00020\u00012\u00020\u0001¨\u0006\r"}, mo65043d2 = {"taskContext", "Lkotlinx/coroutines/scheduling/TaskContext;", "Lkotlinx/coroutines/SchedulerTaskContext;", "Lkotlinx/coroutines/scheduling/Task;", "Lkotlinx/coroutines/SchedulerTask;", "getTaskContext$annotations", "(Lkotlinx/coroutines/scheduling/Task;)V", "getTaskContext", "(Lkotlinx/coroutines/scheduling/Task;)Lkotlinx/coroutines/scheduling/TaskContext;", "afterTask", "", "SchedulerTask", "SchedulerTaskContext", "kotlinx-coroutines-core"}, mo65044k = 2, mo65045mv = {1, 5, 1}, mo65047xi = 48)
/* compiled from: SchedulerTask.kt */
public final class SchedulerTaskKt {
    public static /* synthetic */ void getTaskContext$annotations(Task task) {
    }

    public static final TaskContext getTaskContext(Task task) {
        return task.taskContext;
    }

    public static final void afterTask(TaskContext taskContext) {
        taskContext.afterTask();
    }
}
