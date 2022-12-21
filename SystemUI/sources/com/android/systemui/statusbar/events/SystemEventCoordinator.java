package com.android.systemui.statusbar.events;

import android.icu.text.DateFormat;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.privacy.PrivacyItemController;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.util.time.SystemClock;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo64986d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\b\u0003\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0004*\u0002\n\r\b\u0007\u0018\u00002\u00020\u0001B\u001f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\u000e\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0010J\u0006\u0010\u0014\u001a\u00020\u0012J\u0010\u0010\u0015\u001a\u00020\u00122\b\b\u0002\u0010\u0016\u001a\u00020\u0017J\u0006\u0010\u0018\u001a\u00020\u0012J\u0006\u0010\u0019\u001a\u00020\u0012J\u0006\u0010\u001a\u001a\u00020\u0012R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\t\u001a\u00020\nX\u0004¢\u0006\u0004\n\u0002\u0010\u000bR\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\f\u001a\u00020\rX\u0004¢\u0006\u0004\n\u0002\u0010\u000eR\u000e\u0010\u000f\u001a\u00020\u0010X.¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\u001b"}, mo64987d2 = {"Lcom/android/systemui/statusbar/events/SystemEventCoordinator;", "", "systemClock", "Lcom/android/systemui/util/time/SystemClock;", "batteryController", "Lcom/android/systemui/statusbar/policy/BatteryController;", "privacyController", "Lcom/android/systemui/privacy/PrivacyItemController;", "(Lcom/android/systemui/util/time/SystemClock;Lcom/android/systemui/statusbar/policy/BatteryController;Lcom/android/systemui/privacy/PrivacyItemController;)V", "batteryStateListener", "com/android/systemui/statusbar/events/SystemEventCoordinator$batteryStateListener$1", "Lcom/android/systemui/statusbar/events/SystemEventCoordinator$batteryStateListener$1;", "privacyStateListener", "com/android/systemui/statusbar/events/SystemEventCoordinator$privacyStateListener$1", "Lcom/android/systemui/statusbar/events/SystemEventCoordinator$privacyStateListener$1;", "scheduler", "Lcom/android/systemui/statusbar/events/SystemStatusAnimationScheduler;", "attachScheduler", "", "s", "notifyPluggedIn", "notifyPrivacyItemsChanged", "showAnimation", "", "notifyPrivacyItemsEmpty", "startObserving", "stopObserving", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: SystemEventCoordinator.kt */
public final class SystemEventCoordinator {
    private final BatteryController batteryController;
    private final SystemEventCoordinator$batteryStateListener$1 batteryStateListener = new SystemEventCoordinator$batteryStateListener$1(this);
    private final PrivacyItemController privacyController;
    private final SystemEventCoordinator$privacyStateListener$1 privacyStateListener = new SystemEventCoordinator$privacyStateListener$1(this);
    private SystemStatusAnimationScheduler scheduler;
    /* access modifiers changed from: private */
    public final SystemClock systemClock;

    @Inject
    public SystemEventCoordinator(SystemClock systemClock2, BatteryController batteryController2, PrivacyItemController privacyItemController) {
        Intrinsics.checkNotNullParameter(systemClock2, "systemClock");
        Intrinsics.checkNotNullParameter(batteryController2, "batteryController");
        Intrinsics.checkNotNullParameter(privacyItemController, "privacyController");
        this.systemClock = systemClock2;
        this.batteryController = batteryController2;
        this.privacyController = privacyItemController;
    }

    public final void startObserving() {
        this.privacyController.addCallback((PrivacyItemController.Callback) this.privacyStateListener);
    }

    public final void stopObserving() {
        this.privacyController.removeCallback((PrivacyItemController.Callback) this.privacyStateListener);
    }

    public final void attachScheduler(SystemStatusAnimationScheduler systemStatusAnimationScheduler) {
        Intrinsics.checkNotNullParameter(systemStatusAnimationScheduler, DateFormat.SECOND);
        this.scheduler = systemStatusAnimationScheduler;
    }

    public final void notifyPluggedIn() {
        SystemStatusAnimationScheduler systemStatusAnimationScheduler = this.scheduler;
        if (systemStatusAnimationScheduler == null) {
            Intrinsics.throwUninitializedPropertyAccessException("scheduler");
            systemStatusAnimationScheduler = null;
        }
        systemStatusAnimationScheduler.onStatusEvent(new BatteryEvent());
    }

    public final void notifyPrivacyItemsEmpty() {
        SystemStatusAnimationScheduler systemStatusAnimationScheduler = this.scheduler;
        if (systemStatusAnimationScheduler == null) {
            Intrinsics.throwUninitializedPropertyAccessException("scheduler");
            systemStatusAnimationScheduler = null;
        }
        systemStatusAnimationScheduler.setShouldShowPersistentPrivacyIndicator(false);
    }

    public static /* synthetic */ void notifyPrivacyItemsChanged$default(SystemEventCoordinator systemEventCoordinator, boolean z, int i, Object obj) {
        if ((i & 1) != 0) {
            z = true;
        }
        systemEventCoordinator.notifyPrivacyItemsChanged(z);
    }

    public final void notifyPrivacyItemsChanged(boolean z) {
        PrivacyEvent privacyEvent = new PrivacyEvent(z);
        privacyEvent.setPrivacyItems(this.privacyStateListener.getCurrentPrivacyItems());
        SystemStatusAnimationScheduler systemStatusAnimationScheduler = this.scheduler;
        if (systemStatusAnimationScheduler == null) {
            Intrinsics.throwUninitializedPropertyAccessException("scheduler");
            systemStatusAnimationScheduler = null;
        }
        systemStatusAnimationScheduler.onStatusEvent(privacyEvent);
    }
}
