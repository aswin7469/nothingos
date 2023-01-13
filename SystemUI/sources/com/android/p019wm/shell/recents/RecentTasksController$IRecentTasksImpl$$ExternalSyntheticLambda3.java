package com.android.p019wm.shell.recents;

import com.android.p019wm.shell.recents.RecentTasksController;
import java.util.function.Consumer;

/* renamed from: com.android.wm.shell.recents.RecentTasksController$IRecentTasksImpl$$ExternalSyntheticLambda3 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class RecentTasksController$IRecentTasksImpl$$ExternalSyntheticLambda3 implements Consumer {
    public final /* synthetic */ RecentTasksController.IRecentTasksImpl f$0;
    public final /* synthetic */ IRecentTasksListener f$1;

    public /* synthetic */ RecentTasksController$IRecentTasksImpl$$ExternalSyntheticLambda3(RecentTasksController.IRecentTasksImpl iRecentTasksImpl, IRecentTasksListener iRecentTasksListener) {
        this.f$0 = iRecentTasksImpl;
        this.f$1 = iRecentTasksListener;
    }

    public final void accept(Object obj) {
        this.f$0.mo50728x8012ebef(this.f$1, (RecentTasksController) obj);
    }
}
