package com.android.systemui.statusbar.notification.collection.inflation;

import android.app.Notification;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.notification.SectionClassifier;
import com.android.systemui.statusbar.notification.collection.GroupEntry;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.listbuilder.NotifSection;
import com.android.systemui.util.ListenerSet;
import java.util.List;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo64986d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u000e\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\tJ\u000e\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012J\u0010\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0011\u001a\u00020\u0012H\u0002J\u000e\u0010\u0015\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\tR\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0016"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/collection/inflation/NotifUiAdjustmentProvider;", "", "lockscreenUserManager", "Lcom/android/systemui/statusbar/NotificationLockscreenUserManager;", "sectionClassifier", "Lcom/android/systemui/statusbar/notification/SectionClassifier;", "(Lcom/android/systemui/statusbar/NotificationLockscreenUserManager;Lcom/android/systemui/statusbar/notification/SectionClassifier;)V", "dirtyListeners", "Lcom/android/systemui/util/ListenerSet;", "Ljava/lang/Runnable;", "notifStateChangedListener", "Lcom/android/systemui/statusbar/NotificationLockscreenUserManager$NotificationStateChangedListener;", "addDirtyListener", "", "listener", "calculateAdjustment", "Lcom/android/systemui/statusbar/notification/collection/inflation/NotifUiAdjustment;", "entry", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "isEntryMinimized", "", "removeDirtyListener", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: NotifUiAdjustmentProvider.kt */
public final class NotifUiAdjustmentProvider {
    private final ListenerSet<Runnable> dirtyListeners = new ListenerSet<>();
    private final NotificationLockscreenUserManager lockscreenUserManager;
    private final NotificationLockscreenUserManager.NotificationStateChangedListener notifStateChangedListener = new NotifUiAdjustmentProvider$$ExternalSyntheticLambda0(this);
    private final SectionClassifier sectionClassifier;

    @Inject
    public NotifUiAdjustmentProvider(NotificationLockscreenUserManager notificationLockscreenUserManager, SectionClassifier sectionClassifier2) {
        Intrinsics.checkNotNullParameter(notificationLockscreenUserManager, "lockscreenUserManager");
        Intrinsics.checkNotNullParameter(sectionClassifier2, "sectionClassifier");
        this.lockscreenUserManager = notificationLockscreenUserManager;
        this.sectionClassifier = sectionClassifier2;
    }

    public final void addDirtyListener(Runnable runnable) {
        Intrinsics.checkNotNullParameter(runnable, "listener");
        if (this.dirtyListeners.isEmpty()) {
            this.lockscreenUserManager.addNotificationStateChangedListener(this.notifStateChangedListener);
        }
        this.dirtyListeners.addIfAbsent(runnable);
    }

    public final void removeDirtyListener(Runnable runnable) {
        Intrinsics.checkNotNullParameter(runnable, "listener");
        this.dirtyListeners.remove(runnable);
        if (this.dirtyListeners.isEmpty()) {
            this.lockscreenUserManager.removeNotificationStateChangedListener(this.notifStateChangedListener);
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: notifStateChangedListener$lambda-0  reason: not valid java name */
    public static final void m3116notifStateChangedListener$lambda0(NotifUiAdjustmentProvider notifUiAdjustmentProvider) {
        Intrinsics.checkNotNullParameter(notifUiAdjustmentProvider, "this$0");
        for (Runnable run : notifUiAdjustmentProvider.dirtyListeners) {
            run.run();
        }
    }

    private final boolean isEntryMinimized(NotificationEntry notificationEntry) {
        NotifSection section = notificationEntry.getSection();
        if (section != null) {
            GroupEntry parent = notificationEntry.getParent();
            if (parent != null) {
                return this.sectionClassifier.isMinimizedSection(section) && (Intrinsics.areEqual((Object) parent, (Object) GroupEntry.ROOT_ENTRY) || Intrinsics.areEqual((Object) parent.getSummary(), (Object) notificationEntry));
            }
            throw new IllegalStateException("Entry must have a parent to determine if minimized".toString());
        }
        throw new IllegalStateException("Entry must have a section to determine if minimized".toString());
    }

    public final NotifUiAdjustment calculateAdjustment(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        String key = notificationEntry.getKey();
        Intrinsics.checkNotNullExpressionValue(key, "entry.key");
        List<Notification.Action> smartActions = notificationEntry.getRanking().getSmartActions();
        Intrinsics.checkNotNullExpressionValue(smartActions, "entry.ranking.smartActions");
        List<CharSequence> smartReplies = notificationEntry.getRanking().getSmartReplies();
        Intrinsics.checkNotNullExpressionValue(smartReplies, "entry.ranking.smartReplies");
        return new NotifUiAdjustment(key, smartActions, smartReplies, notificationEntry.getRanking().isConversation(), isEntryMinimized(notificationEntry), this.lockscreenUserManager.needsRedaction(notificationEntry));
    }
}
