package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.notification.DynamicPrivacyController;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.coordinator.dagger.CoordinatorScope;
import com.android.systemui.statusbar.notification.collection.listbuilder.OnBeforeRenderListListener;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.Invalidator;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import java.util.List;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.SequencesKt;

@Metadata(mo65042d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0003\u0018\u00002\u00020\u00012\u00020\u00022\u00020\u00032\u00020\u0004B/\b\u0007\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\n\u0012\u0006\u0010\u000b\u001a\u00020\f\u0012\u0006\u0010\r\u001a\u00020\u000e¢\u0006\u0002\u0010\u000fJ\u0010\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013H\u0016J\u0016\u0010\u0014\u001a\u00020\u00112\f\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00170\u0016H\u0016J\b\u0010\u0018\u001a\u00020\u0011H\u0016R\u000e\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0004¢\u0006\u0002\n\u0000¨\u0006\u0019"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/collection/coordinator/SensitiveContentCoordinatorImpl;", "Lcom/android/systemui/statusbar/notification/collection/listbuilder/pluggable/Invalidator;", "Lcom/android/systemui/statusbar/notification/collection/coordinator/SensitiveContentCoordinator;", "Lcom/android/systemui/statusbar/notification/DynamicPrivacyController$Listener;", "Lcom/android/systemui/statusbar/notification/collection/listbuilder/OnBeforeRenderListListener;", "dynamicPrivacyController", "Lcom/android/systemui/statusbar/notification/DynamicPrivacyController;", "lockscreenUserManager", "Lcom/android/systemui/statusbar/NotificationLockscreenUserManager;", "keyguardUpdateMonitor", "Lcom/android/keyguard/KeyguardUpdateMonitor;", "statusBarStateController", "Lcom/android/systemui/plugins/statusbar/StatusBarStateController;", "keyguardStateController", "Lcom/android/systemui/statusbar/policy/KeyguardStateController;", "(Lcom/android/systemui/statusbar/notification/DynamicPrivacyController;Lcom/android/systemui/statusbar/NotificationLockscreenUserManager;Lcom/android/keyguard/KeyguardUpdateMonitor;Lcom/android/systemui/plugins/statusbar/StatusBarStateController;Lcom/android/systemui/statusbar/policy/KeyguardStateController;)V", "attach", "", "pipeline", "Lcom/android/systemui/statusbar/notification/collection/NotifPipeline;", "onBeforeRenderList", "entries", "", "Lcom/android/systemui/statusbar/notification/collection/ListEntry;", "onDynamicPrivacyChanged", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
@CoordinatorScope
/* compiled from: SensitiveContentCoordinator.kt */
final class SensitiveContentCoordinatorImpl extends Invalidator implements SensitiveContentCoordinator, DynamicPrivacyController.Listener, OnBeforeRenderListListener {
    private final DynamicPrivacyController dynamicPrivacyController;
    private final KeyguardStateController keyguardStateController;
    private final KeyguardUpdateMonitor keyguardUpdateMonitor;
    private final NotificationLockscreenUserManager lockscreenUserManager;
    private final StatusBarStateController statusBarStateController;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    @Inject
    public SensitiveContentCoordinatorImpl(DynamicPrivacyController dynamicPrivacyController2, NotificationLockscreenUserManager notificationLockscreenUserManager, KeyguardUpdateMonitor keyguardUpdateMonitor2, StatusBarStateController statusBarStateController2, KeyguardStateController keyguardStateController2) {
        super("SensitiveContentInvalidator");
        Intrinsics.checkNotNullParameter(dynamicPrivacyController2, "dynamicPrivacyController");
        Intrinsics.checkNotNullParameter(notificationLockscreenUserManager, "lockscreenUserManager");
        Intrinsics.checkNotNullParameter(keyguardUpdateMonitor2, "keyguardUpdateMonitor");
        Intrinsics.checkNotNullParameter(statusBarStateController2, "statusBarStateController");
        Intrinsics.checkNotNullParameter(keyguardStateController2, "keyguardStateController");
        this.dynamicPrivacyController = dynamicPrivacyController2;
        this.lockscreenUserManager = notificationLockscreenUserManager;
        this.keyguardUpdateMonitor = keyguardUpdateMonitor2;
        this.statusBarStateController = statusBarStateController2;
        this.keyguardStateController = keyguardStateController2;
    }

    public void attach(NotifPipeline notifPipeline) {
        Intrinsics.checkNotNullParameter(notifPipeline, "pipeline");
        this.dynamicPrivacyController.addListener(this);
        notifPipeline.addOnBeforeRenderListListener(this);
        notifPipeline.addPreRenderInvalidator(this);
    }

    public void onDynamicPrivacyChanged() {
        invalidateList();
    }

    public void onBeforeRenderList(List<? extends ListEntry> list) {
        boolean z;
        Intrinsics.checkNotNullParameter(list, "entries");
        if (this.keyguardStateController.isKeyguardGoingAway()) {
            return;
        }
        if (this.statusBarStateController.getState() != 1 || !this.keyguardUpdateMonitor.getUserUnlockedWithBiometricAndIsBypassing(KeyguardUpdateMonitor.getCurrentUser())) {
            int currentUserId = this.lockscreenUserManager.getCurrentUserId();
            boolean isLockscreenPublicMode = this.lockscreenUserManager.isLockscreenPublicMode(currentUserId);
            boolean z2 = isLockscreenPublicMode && !this.lockscreenUserManager.userAllowsPrivateNotificationsInPublic(currentUserId);
            boolean isDynamicallyUnlocked = this.dynamicPrivacyController.isDynamicallyUnlocked();
            for (NotificationEntry notificationEntry : SequencesKt.filter(SensitiveContentCoordinatorKt.extractAllRepresentativeEntries(list), SensitiveContentCoordinatorImpl$onBeforeRenderList$1.INSTANCE)) {
                int identifier = notificationEntry.getSbn().getUser().getIdentifier();
                if (isLockscreenPublicMode || this.lockscreenUserManager.isLockscreenPublicMode(identifier)) {
                    if (!isDynamicallyUnlocked) {
                        z = true;
                    } else if (!(identifier == currentUserId || identifier == -1)) {
                        z = this.lockscreenUserManager.needsSeparateWorkChallenge(identifier);
                    }
                    notificationEntry.setSensitive(!z && this.lockscreenUserManager.needsRedaction(notificationEntry), z2);
                }
                z = false;
                notificationEntry.setSensitive(!z && this.lockscreenUserManager.needsRedaction(notificationEntry), z2);
            }
        }
    }
}
