package com.android.systemui.statusbar.notification.collection.coordinator;

import android.util.ArraySet;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.coordinator.dagger.CoordinatorScope;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifLifetimeExtender;
import com.android.systemui.statusbar.notification.collection.render.NotifGutsViewListener;
import com.android.systemui.statusbar.notification.collection.render.NotifGutsViewManager;
import java.p026io.PrintWriter;
import java.util.Iterator;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000n\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u00012\u00020\u0002B\u001f\b\u0007\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b¢\u0006\u0002\u0010\tJ\u0010\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0017H\u0016J\u0010\u0010\u0018\u001a\u00020\u00152\u0006\u0010\u0019\u001a\u00020\u001aH\u0002J#\u0010\u001b\u001a\u00020\u00152\u0006\u0010\u001c\u001a\u00020\u001d2\f\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u00100\u001fH\u0016¢\u0006\u0002\u0010 J\u0010\u0010!\u001a\u00020\"2\u0006\u0010\u0019\u001a\u00020#H\u0002R\u000e\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000fX\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00100\u000fX\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0012\u001a\u0004\u0018\u00010\u0013X\u000e¢\u0006\u0002\n\u0000¨\u0006$"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/collection/coordinator/GutsCoordinator;", "Lcom/android/systemui/statusbar/notification/collection/coordinator/Coordinator;", "Lcom/android/systemui/Dumpable;", "notifGutsViewManager", "Lcom/android/systemui/statusbar/notification/collection/render/NotifGutsViewManager;", "logger", "Lcom/android/systemui/statusbar/notification/collection/coordinator/GutsCoordinatorLogger;", "dumpManager", "Lcom/android/systemui/dump/DumpManager;", "(Lcom/android/systemui/statusbar/notification/collection/render/NotifGutsViewManager;Lcom/android/systemui/statusbar/notification/collection/coordinator/GutsCoordinatorLogger;Lcom/android/systemui/dump/DumpManager;)V", "mGutsListener", "Lcom/android/systemui/statusbar/notification/collection/render/NotifGutsViewListener;", "mLifetimeExtender", "Lcom/android/systemui/statusbar/notification/collection/notifcollection/NotifLifetimeExtender;", "notifsExtendingLifetime", "Landroid/util/ArraySet;", "", "notifsWithOpenGuts", "onEndLifetimeExtensionCallback", "Lcom/android/systemui/statusbar/notification/collection/notifcollection/NotifLifetimeExtender$OnEndLifetimeExtensionCallback;", "attach", "", "pipeline", "Lcom/android/systemui/statusbar/notification/collection/NotifPipeline;", "closeGutsAndEndLifetimeExtension", "entry", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "dump", "pw", "Ljava/io/PrintWriter;", "args", "", "(Ljava/io/PrintWriter;[Ljava/lang/String;)V", "isCurrentlyShowingGuts", "", "Lcom/android/systemui/statusbar/notification/collection/ListEntry;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
@CoordinatorScope
/* compiled from: GutsCoordinator.kt */
public final class GutsCoordinator implements Coordinator, Dumpable {
    /* access modifiers changed from: private */
    public final GutsCoordinatorLogger logger;
    private final NotifGutsViewListener mGutsListener;
    private final NotifLifetimeExtender mLifetimeExtender;
    private final NotifGutsViewManager notifGutsViewManager;
    /* access modifiers changed from: private */
    public final ArraySet<String> notifsExtendingLifetime = new ArraySet<>();
    /* access modifiers changed from: private */
    public final ArraySet<String> notifsWithOpenGuts = new ArraySet<>();
    /* access modifiers changed from: private */
    public NotifLifetimeExtender.OnEndLifetimeExtensionCallback onEndLifetimeExtensionCallback;

    @Inject
    public GutsCoordinator(NotifGutsViewManager notifGutsViewManager2, GutsCoordinatorLogger gutsCoordinatorLogger, DumpManager dumpManager) {
        Intrinsics.checkNotNullParameter(notifGutsViewManager2, "notifGutsViewManager");
        Intrinsics.checkNotNullParameter(gutsCoordinatorLogger, "logger");
        Intrinsics.checkNotNullParameter(dumpManager, "dumpManager");
        this.notifGutsViewManager = notifGutsViewManager2;
        this.logger = gutsCoordinatorLogger;
        dumpManager.registerDumpable("GutsCoordinator", this);
        this.mLifetimeExtender = new GutsCoordinator$mLifetimeExtender$1(this);
        this.mGutsListener = new GutsCoordinator$mGutsListener$1(this);
    }

    public void attach(NotifPipeline notifPipeline) {
        Intrinsics.checkNotNullParameter(notifPipeline, "pipeline");
        this.notifGutsViewManager.setGutsListener(this.mGutsListener);
        notifPipeline.addNotificationLifetimeExtender(this.mLifetimeExtender);
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(strArr, "args");
        printWriter.println("  notifsWithOpenGuts: " + this.notifsWithOpenGuts.size());
        Iterator<String> it = this.notifsWithOpenGuts.iterator();
        while (it.hasNext()) {
            printWriter.println("   * " + it.next());
        }
        printWriter.println("  notifsExtendingLifetime: " + this.notifsExtendingLifetime.size());
        Iterator<String> it2 = this.notifsExtendingLifetime.iterator();
        while (it2.hasNext()) {
            printWriter.println("   * " + it2.next());
        }
        printWriter.println("  onEndLifetimeExtensionCallback: " + this.onEndLifetimeExtensionCallback);
    }

    /* access modifiers changed from: private */
    public final boolean isCurrentlyShowingGuts(ListEntry listEntry) {
        return this.notifsWithOpenGuts.contains(listEntry.getKey());
    }

    /* access modifiers changed from: private */
    public final void closeGutsAndEndLifetimeExtension(NotificationEntry notificationEntry) {
        NotifLifetimeExtender.OnEndLifetimeExtensionCallback onEndLifetimeExtensionCallback2;
        this.notifsWithOpenGuts.remove(notificationEntry.getKey());
        if (this.notifsExtendingLifetime.remove(notificationEntry.getKey()) && (onEndLifetimeExtensionCallback2 = this.onEndLifetimeExtensionCallback) != null) {
            onEndLifetimeExtensionCallback2.onEndLifetimeExtension(this.mLifetimeExtender, notificationEntry);
        }
    }
}
