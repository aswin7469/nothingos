package com.android.systemui.smartspace.preconditions;

import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import com.android.systemui.flags.ResourceBooleanFlag;
import com.android.systemui.smartspace.SmartspacePrecondition;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.util.concurrency.Execution;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000?\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010#\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005*\u0001\n\u0018\u00002\u00020\u0001B\u001f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\u0010\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0013H\u0016J\b\u0010\u0017\u001a\u00020\rH\u0016J\u0010\u0010\u0018\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0013H\u0016J\b\u0010\u0019\u001a\u00020\u0015H\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\t\u001a\u00020\nX\u0004¢\u0006\u0004\n\u0002\u0010\u000bR\u001e\u0010\u000e\u001a\u00020\r2\u0006\u0010\f\u001a\u00020\r@BX\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00130\u0012X\u000e¢\u0006\u0002\n\u0000¨\u0006\u001a"}, mo64987d2 = {"Lcom/android/systemui/smartspace/preconditions/LockscreenPrecondition;", "Lcom/android/systemui/smartspace/SmartspacePrecondition;", "featureFlags", "Lcom/android/systemui/flags/FeatureFlags;", "deviceProvisionedController", "Lcom/android/systemui/statusbar/policy/DeviceProvisionedController;", "execution", "Lcom/android/systemui/util/concurrency/Execution;", "(Lcom/android/systemui/flags/FeatureFlags;Lcom/android/systemui/statusbar/policy/DeviceProvisionedController;Lcom/android/systemui/util/concurrency/Execution;)V", "deviceProvisionedListener", "com/android/systemui/smartspace/preconditions/LockscreenPrecondition$deviceProvisionedListener$1", "Lcom/android/systemui/smartspace/preconditions/LockscreenPrecondition$deviceProvisionedListener$1;", "<set-?>", "", "deviceReady", "getDeviceReady", "()Z", "listeners", "", "Lcom/android/systemui/smartspace/SmartspacePrecondition$Listener;", "addListener", "", "listener", "conditionsMet", "removeListener", "updateDeviceReadiness", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: LockscreenPrecondition.kt */
public final class LockscreenPrecondition implements SmartspacePrecondition {
    private final DeviceProvisionedController deviceProvisionedController;
    private final LockscreenPrecondition$deviceProvisionedListener$1 deviceProvisionedListener;
    private boolean deviceReady;
    private final Execution execution;
    private final FeatureFlags featureFlags;
    private Set<SmartspacePrecondition.Listener> listeners = new LinkedHashSet();

    @Inject
    public LockscreenPrecondition(FeatureFlags featureFlags2, DeviceProvisionedController deviceProvisionedController2, Execution execution2) {
        Intrinsics.checkNotNullParameter(featureFlags2, "featureFlags");
        Intrinsics.checkNotNullParameter(deviceProvisionedController2, "deviceProvisionedController");
        Intrinsics.checkNotNullParameter(execution2, "execution");
        this.featureFlags = featureFlags2;
        this.deviceProvisionedController = deviceProvisionedController2;
        this.execution = execution2;
        LockscreenPrecondition$deviceProvisionedListener$1 lockscreenPrecondition$deviceProvisionedListener$1 = new LockscreenPrecondition$deviceProvisionedListener$1(this);
        this.deviceProvisionedListener = lockscreenPrecondition$deviceProvisionedListener$1;
        deviceProvisionedController2.addCallback(lockscreenPrecondition$deviceProvisionedListener$1);
        updateDeviceReadiness();
    }

    public final boolean getDeviceReady() {
        return this.deviceReady;
    }

    /* access modifiers changed from: private */
    public final void updateDeviceReadiness() {
        if (!this.deviceReady) {
            boolean z = this.deviceProvisionedController.isDeviceProvisioned() && this.deviceProvisionedController.isCurrentUserSetup();
            this.deviceReady = z;
            if (z) {
                this.deviceProvisionedController.removeCallback(this.deviceProvisionedListener);
                synchronized (this.listeners) {
                    for (SmartspacePrecondition.Listener onCriteriaChanged : this.listeners) {
                        onCriteriaChanged.onCriteriaChanged();
                    }
                    Unit unit = Unit.INSTANCE;
                }
            }
        }
    }

    public void addListener(SmartspacePrecondition.Listener listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        synchronized (this.listeners) {
            this.listeners.add(listener);
            Unit unit = Unit.INSTANCE;
        }
        listener.onCriteriaChanged();
    }

    public void removeListener(SmartspacePrecondition.Listener listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        synchronized (this.listeners) {
            this.listeners.remove(listener);
            Unit unit = Unit.INSTANCE;
        }
    }

    public boolean conditionsMet() {
        this.execution.assertIsMainThread();
        FeatureFlags featureFlags2 = this.featureFlags;
        ResourceBooleanFlag resourceBooleanFlag = Flags.SMARTSPACE;
        Intrinsics.checkNotNullExpressionValue(resourceBooleanFlag, "SMARTSPACE");
        return featureFlags2.isEnabled(resourceBooleanFlag) && this.deviceReady;
    }
}
