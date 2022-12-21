package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.render.NotifShadeEventSource;
import java.util.List;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo64986d1 = {"\u0000M\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0004*\u0001\f\b\u0007\u0018\u00002\u00020\u00012\u00020\u0002B\u0019\b\u0001\u0012\b\b\u0001\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\u0010\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0014H\u0016J\u0016\u0010\u0015\u001a\u00020\u00122\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00180\u0017H\u0002J\u0010\u0010\u0019\u001a\u00020\u00122\u0006\u0010\u001a\u001a\u00020\u000fH\u0016J\u0010\u0010\u001b\u001a\u00020\u00122\u0006\u0010\u001a\u001a\u00020\u000fH\u0016R\u000e\u0010\b\u001a\u00020\tX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\tX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u00020\fX\u0004¢\u0006\u0004\n\u0002\u0010\rR\u0010\u0010\u000e\u001a\u0004\u0018\u00010\u000fX\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0010\u001a\u0004\u0018\u00010\u000fX\u000e¢\u0006\u0002\n\u0000¨\u0006\u001c"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/collection/coordinator/ShadeEventCoordinator;", "Lcom/android/systemui/statusbar/notification/collection/coordinator/Coordinator;", "Lcom/android/systemui/statusbar/notification/collection/render/NotifShadeEventSource;", "mMainExecutor", "Ljava/util/concurrent/Executor;", "mLogger", "Lcom/android/systemui/statusbar/notification/collection/coordinator/ShadeEventCoordinatorLogger;", "(Ljava/util/concurrent/Executor;Lcom/android/systemui/statusbar/notification/collection/coordinator/ShadeEventCoordinatorLogger;)V", "mEntryRemoved", "", "mEntryRemovedByUser", "mNotifCollectionListener", "com/android/systemui/statusbar/notification/collection/coordinator/ShadeEventCoordinator$mNotifCollectionListener$1", "Lcom/android/systemui/statusbar/notification/collection/coordinator/ShadeEventCoordinator$mNotifCollectionListener$1;", "mNotifRemovedByUserCallback", "Ljava/lang/Runnable;", "mShadeEmptiedCallback", "attach", "", "pipeline", "Lcom/android/systemui/statusbar/notification/collection/NotifPipeline;", "onBeforeRenderList", "entries", "", "Lcom/android/systemui/statusbar/notification/collection/ListEntry;", "setNotifRemovedByUserCallback", "callback", "setShadeEmptiedCallback", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ShadeEventCoordinator.kt */
public final class ShadeEventCoordinator implements Coordinator, NotifShadeEventSource {
    /* access modifiers changed from: private */
    public boolean mEntryRemoved;
    /* access modifiers changed from: private */
    public boolean mEntryRemovedByUser;
    private final ShadeEventCoordinatorLogger mLogger;
    private final Executor mMainExecutor;
    private final ShadeEventCoordinator$mNotifCollectionListener$1 mNotifCollectionListener = new ShadeEventCoordinator$mNotifCollectionListener$1(this);
    private Runnable mNotifRemovedByUserCallback;
    private Runnable mShadeEmptiedCallback;

    @Inject
    public ShadeEventCoordinator(@Main Executor executor, ShadeEventCoordinatorLogger shadeEventCoordinatorLogger) {
        Intrinsics.checkNotNullParameter(executor, "mMainExecutor");
        Intrinsics.checkNotNullParameter(shadeEventCoordinatorLogger, "mLogger");
        this.mMainExecutor = executor;
        this.mLogger = shadeEventCoordinatorLogger;
    }

    public void attach(NotifPipeline notifPipeline) {
        Intrinsics.checkNotNullParameter(notifPipeline, "pipeline");
        notifPipeline.addCollectionListener(this.mNotifCollectionListener);
        notifPipeline.addOnBeforeRenderListListener(new ShadeEventCoordinator$$ExternalSyntheticLambda0(this));
    }

    public void setNotifRemovedByUserCallback(Runnable runnable) {
        Intrinsics.checkNotNullParameter(runnable, "callback");
        if (this.mNotifRemovedByUserCallback == null) {
            this.mNotifRemovedByUserCallback = runnable;
            return;
        }
        throw new IllegalStateException("mNotifRemovedByUserCallback already set".toString());
    }

    public void setShadeEmptiedCallback(Runnable runnable) {
        Intrinsics.checkNotNullParameter(runnable, "callback");
        if (this.mShadeEmptiedCallback == null) {
            this.mShadeEmptiedCallback = runnable;
            return;
        }
        throw new IllegalStateException("mShadeEmptiedCallback already set".toString());
    }

    /* access modifiers changed from: private */
    public final void onBeforeRenderList(List<? extends ListEntry> list) {
        if (this.mEntryRemoved && list.isEmpty()) {
            this.mLogger.logShadeEmptied();
            Runnable runnable = this.mShadeEmptiedCallback;
            if (runnable != null) {
                this.mMainExecutor.execute(runnable);
            }
        }
        if (this.mEntryRemoved && this.mEntryRemovedByUser) {
            this.mLogger.logNotifRemovedByUser();
            Runnable runnable2 = this.mNotifRemovedByUserCallback;
            if (runnable2 != null) {
                this.mMainExecutor.execute(runnable2);
            }
        }
        this.mEntryRemoved = false;
        this.mEntryRemovedByUser = false;
    }
}
