package com.android.systemui.wmshell;

import com.android.wm.shell.tasksurfacehelper.TaskSurfaceHelper;
import com.android.wm.shell.tasksurfacehelper.TaskSurfaceHelperController;
import java.util.function.Function;
/* loaded from: classes2.dex */
public final /* synthetic */ class WMShellBaseModule$$ExternalSyntheticLambda6 implements Function {
    public static final /* synthetic */ WMShellBaseModule$$ExternalSyntheticLambda6 INSTANCE = new WMShellBaseModule$$ExternalSyntheticLambda6();

    private /* synthetic */ WMShellBaseModule$$ExternalSyntheticLambda6() {
    }

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        TaskSurfaceHelper asTaskSurfaceHelper;
        asTaskSurfaceHelper = ((TaskSurfaceHelperController) obj).asTaskSurfaceHelper();
        return asTaskSurfaceHelper;
    }
}
