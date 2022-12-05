package com.android.systemui.statusbar.notification.interruption;

import android.content.Context;
import android.media.MediaMetadata;
import android.provider.Settings;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.phone.HeadsUpManagerPhone;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.tuner.TunerService;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: BypassHeadsUpNotifier.kt */
/* loaded from: classes.dex */
public final class BypassHeadsUpNotifier implements StatusBarStateController.StateListener, NotificationMediaManager.MediaListener {
    @NotNull
    private final KeyguardBypassController bypassController;
    @NotNull
    private final Context context;
    @Nullable
    private NotificationEntry currentMediaEntry;
    private boolean enabled = true;
    @NotNull
    private final NotificationEntryManager entryManager;
    private boolean fullyAwake;
    @NotNull
    private final HeadsUpManagerPhone headsUpManager;
    @NotNull
    private final NotificationMediaManager mediaManager;
    @NotNull
    private final NotificationLockscreenUserManager notificationLockscreenUserManager;
    @NotNull
    private final StatusBarStateController statusBarStateController;

    public BypassHeadsUpNotifier(@NotNull Context context, @NotNull KeyguardBypassController bypassController, @NotNull StatusBarStateController statusBarStateController, @NotNull HeadsUpManagerPhone headsUpManager, @NotNull NotificationLockscreenUserManager notificationLockscreenUserManager, @NotNull NotificationMediaManager mediaManager, @NotNull NotificationEntryManager entryManager, @NotNull TunerService tunerService) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(bypassController, "bypassController");
        Intrinsics.checkNotNullParameter(statusBarStateController, "statusBarStateController");
        Intrinsics.checkNotNullParameter(headsUpManager, "headsUpManager");
        Intrinsics.checkNotNullParameter(notificationLockscreenUserManager, "notificationLockscreenUserManager");
        Intrinsics.checkNotNullParameter(mediaManager, "mediaManager");
        Intrinsics.checkNotNullParameter(entryManager, "entryManager");
        Intrinsics.checkNotNullParameter(tunerService, "tunerService");
        this.context = context;
        this.bypassController = bypassController;
        this.statusBarStateController = statusBarStateController;
        this.headsUpManager = headsUpManager;
        this.notificationLockscreenUserManager = notificationLockscreenUserManager;
        this.mediaManager = mediaManager;
        this.entryManager = entryManager;
        statusBarStateController.addCallback(this);
        tunerService.addTunable(new TunerService.Tunable() { // from class: com.android.systemui.statusbar.notification.interruption.BypassHeadsUpNotifier.1
            @Override // com.android.systemui.tuner.TunerService.Tunable
            public final void onTuningChanged(String str, String str2) {
                BypassHeadsUpNotifier bypassHeadsUpNotifier = BypassHeadsUpNotifier.this;
                boolean z = false;
                if (Settings.Secure.getIntForUser(bypassHeadsUpNotifier.context.getContentResolver(), "show_media_when_bypassing", 0, KeyguardUpdateMonitor.getCurrentUser()) != 0) {
                    z = true;
                }
                bypassHeadsUpNotifier.enabled = z;
            }
        }, "show_media_when_bypassing");
    }

    public final void setFullyAwake(boolean z) {
        this.fullyAwake = z;
        if (z) {
            updateAutoHeadsUp(this.currentMediaEntry);
        }
    }

    public final void setUp() {
        this.mediaManager.addCallback(this);
    }

    @Override // com.android.systemui.statusbar.NotificationMediaManager.MediaListener
    public void onPrimaryMetadataOrStateChanged(@Nullable MediaMetadata mediaMetadata, int i) {
        NotificationEntry notificationEntry = this.currentMediaEntry;
        NotificationEntry activeNotificationUnfiltered = this.entryManager.getActiveNotificationUnfiltered(this.mediaManager.getMediaNotificationKey());
        if (!NotificationMediaManager.isPlayingState(i)) {
            activeNotificationUnfiltered = null;
        }
        this.currentMediaEntry = activeNotificationUnfiltered;
        updateAutoHeadsUp(notificationEntry);
        updateAutoHeadsUp(this.currentMediaEntry);
    }

    private final void updateAutoHeadsUp(NotificationEntry notificationEntry) {
        if (notificationEntry == null) {
            return;
        }
        boolean z = Intrinsics.areEqual(notificationEntry, this.currentMediaEntry) && canAutoHeadsUp(notificationEntry);
        notificationEntry.setAutoHeadsUp(z);
        if (!z) {
            return;
        }
        this.headsUpManager.showNotification(notificationEntry);
    }

    private final boolean canAutoHeadsUp(NotificationEntry notificationEntry) {
        return isAutoHeadsUpAllowed() && !notificationEntry.isSensitive() && this.notificationLockscreenUserManager.shouldShowOnKeyguard(notificationEntry) && this.entryManager.getActiveNotificationUnfiltered(notificationEntry.getKey()) == null;
    }

    @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
    public void onStatePostChange() {
        updateAutoHeadsUp(this.currentMediaEntry);
    }

    private final boolean isAutoHeadsUpAllowed() {
        return this.enabled && this.bypassController.getBypassEnabled() && this.statusBarStateController.getState() == 1 && this.fullyAwake;
    }
}
