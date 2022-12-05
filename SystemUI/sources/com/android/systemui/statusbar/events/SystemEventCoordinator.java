package com.android.systemui.statusbar.events;

import android.content.Context;
import com.android.systemui.privacy.PrivacyItemController;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.util.time.SystemClock;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: SystemEventCoordinator.kt */
/* loaded from: classes.dex */
public final class SystemEventCoordinator {
    @NotNull
    private final BatteryController batteryController;
    @NotNull
    private final Context context;
    @NotNull
    private final PrivacyItemController privacyController;
    private SystemStatusAnimationScheduler scheduler;
    @NotNull
    private final SystemClock systemClock;
    @NotNull
    private final SystemEventCoordinator$batteryStateListener$1 batteryStateListener = new BatteryController.BatteryStateChangeCallback() { // from class: com.android.systemui.statusbar.events.SystemEventCoordinator$batteryStateListener$1
        private boolean plugged;
        private boolean stateKnown;

        @Override // com.android.systemui.statusbar.policy.BatteryController.BatteryStateChangeCallback
        public void onBatteryLevelChanged(int i, boolean z, boolean z2) {
            if (!this.stateKnown) {
                this.stateKnown = true;
                this.plugged = z;
                notifyListeners();
            } else if (this.plugged == z) {
            } else {
                this.plugged = z;
                notifyListeners();
            }
        }

        private final void notifyListeners() {
            if (this.plugged) {
                SystemEventCoordinator.this.notifyPluggedIn();
            }
        }
    };
    @NotNull
    private final SystemEventCoordinator$privacyStateListener$1 privacyStateListener = new SystemEventCoordinator$privacyStateListener$1(this);

    /* JADX WARN: Type inference failed for: r2v1, types: [com.android.systemui.statusbar.events.SystemEventCoordinator$batteryStateListener$1] */
    public SystemEventCoordinator(@NotNull SystemClock systemClock, @NotNull BatteryController batteryController, @NotNull PrivacyItemController privacyController, @NotNull Context context) {
        Intrinsics.checkNotNullParameter(systemClock, "systemClock");
        Intrinsics.checkNotNullParameter(batteryController, "batteryController");
        Intrinsics.checkNotNullParameter(privacyController, "privacyController");
        Intrinsics.checkNotNullParameter(context, "context");
        this.systemClock = systemClock;
        this.batteryController = batteryController;
        this.privacyController = privacyController;
        this.context = context;
    }

    public final void startObserving() {
        this.privacyController.addCallback(this.privacyStateListener);
    }

    public final void stopObserving() {
        this.privacyController.removeCallback(this.privacyStateListener);
    }

    public final void attachScheduler(@NotNull SystemStatusAnimationScheduler s) {
        Intrinsics.checkNotNullParameter(s, "s");
        this.scheduler = s;
    }

    public final void notifyPluggedIn() {
        SystemStatusAnimationScheduler systemStatusAnimationScheduler = this.scheduler;
        if (systemStatusAnimationScheduler != null) {
            systemStatusAnimationScheduler.onStatusEvent(new BatteryEvent());
        } else {
            Intrinsics.throwUninitializedPropertyAccessException("scheduler");
            throw null;
        }
    }

    public final void notifyPrivacyItemsEmpty() {
        SystemStatusAnimationScheduler systemStatusAnimationScheduler = this.scheduler;
        if (systemStatusAnimationScheduler != null) {
            systemStatusAnimationScheduler.setShouldShowPersistentPrivacyIndicator(false);
        } else {
            Intrinsics.throwUninitializedPropertyAccessException("scheduler");
            throw null;
        }
    }

    public final void notifyPrivacyItemsChanged(boolean z) {
        PrivacyEvent privacyEvent = new PrivacyEvent(z);
        privacyEvent.setPrivacyItems(this.privacyStateListener.getCurrentPrivacyItems());
        privacyEvent.setContentDescription((String) new SystemEventCoordinator$notifyPrivacyItemsChanged$1(this, privacyEvent).mo1951invoke());
        SystemStatusAnimationScheduler systemStatusAnimationScheduler = this.scheduler;
        if (systemStatusAnimationScheduler != null) {
            systemStatusAnimationScheduler.onStatusEvent(privacyEvent);
        } else {
            Intrinsics.throwUninitializedPropertyAccessException("scheduler");
            throw null;
        }
    }
}
