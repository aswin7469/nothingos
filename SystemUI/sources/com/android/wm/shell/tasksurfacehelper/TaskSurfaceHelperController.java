package com.android.wm.shell.tasksurfacehelper;

import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.common.ShellExecutor;
/* loaded from: classes2.dex */
public class TaskSurfaceHelperController {
    private final TaskSurfaceHelperImpl mImpl = new TaskSurfaceHelperImpl();
    private final ShellExecutor mMainExecutor;
    private final ShellTaskOrganizer mTaskOrganizer;

    public TaskSurfaceHelperController(ShellTaskOrganizer shellTaskOrganizer, ShellExecutor shellExecutor) {
        this.mTaskOrganizer = shellTaskOrganizer;
        this.mMainExecutor = shellExecutor;
    }

    public TaskSurfaceHelper asTaskSurfaceHelper() {
        return this.mImpl;
    }

    /* loaded from: classes2.dex */
    private class TaskSurfaceHelperImpl implements TaskSurfaceHelper {
        private TaskSurfaceHelperImpl() {
        }
    }
}
