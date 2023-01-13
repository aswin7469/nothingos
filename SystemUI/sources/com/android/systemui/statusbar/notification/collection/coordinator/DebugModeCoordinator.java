package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.coordinator.dagger.CoordinatorScope;
import com.android.systemui.statusbar.notification.collection.provider.DebugModeFilterProvider;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000%\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0006\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\u0004\n\u0002\u0010\u0007¨\u0006\f"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/collection/coordinator/DebugModeCoordinator;", "Lcom/android/systemui/statusbar/notification/collection/coordinator/Coordinator;", "debugModeFilterProvider", "Lcom/android/systemui/statusbar/notification/collection/provider/DebugModeFilterProvider;", "(Lcom/android/systemui/statusbar/notification/collection/provider/DebugModeFilterProvider;)V", "preGroupFilter", "com/android/systemui/statusbar/notification/collection/coordinator/DebugModeCoordinator$preGroupFilter$1", "Lcom/android/systemui/statusbar/notification/collection/coordinator/DebugModeCoordinator$preGroupFilter$1;", "attach", "", "pipeline", "Lcom/android/systemui/statusbar/notification/collection/NotifPipeline;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
@CoordinatorScope
/* compiled from: DebugModeCoordinator.kt */
public final class DebugModeCoordinator implements Coordinator {
    /* access modifiers changed from: private */
    public final DebugModeFilterProvider debugModeFilterProvider;
    private final DebugModeCoordinator$preGroupFilter$1 preGroupFilter = new DebugModeCoordinator$preGroupFilter$1(this);

    @Inject
    public DebugModeCoordinator(DebugModeFilterProvider debugModeFilterProvider2) {
        Intrinsics.checkNotNullParameter(debugModeFilterProvider2, "debugModeFilterProvider");
        this.debugModeFilterProvider = debugModeFilterProvider2;
    }

    public void attach(NotifPipeline notifPipeline) {
        Intrinsics.checkNotNullParameter(notifPipeline, "pipeline");
        notifPipeline.addPreGroupFilter(this.preGroupFilter);
        this.debugModeFilterProvider.registerInvalidationListener(new DebugModeCoordinator$$ExternalSyntheticLambda0(this.preGroupFilter));
    }
}
