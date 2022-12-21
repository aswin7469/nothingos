package com.android.systemui.statusbar.policy;

import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: DeviceProvisionedControllerImpl.kt */
/* synthetic */ class DeviceProvisionedControllerImpl$onUserSwitched$1 extends FunctionReferenceImpl implements Function1<DeviceProvisionedController.DeviceProvisionedListener, Unit> {
    public static final DeviceProvisionedControllerImpl$onUserSwitched$1 INSTANCE = new DeviceProvisionedControllerImpl$onUserSwitched$1();

    DeviceProvisionedControllerImpl$onUserSwitched$1() {
        super(1, DeviceProvisionedController.DeviceProvisionedListener.class, "onUserSwitched", "onUserSwitched()V", 0);
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        invoke((DeviceProvisionedController.DeviceProvisionedListener) obj);
        return Unit.INSTANCE;
    }

    public final void invoke(DeviceProvisionedController.DeviceProvisionedListener deviceProvisionedListener) {
        Intrinsics.checkNotNullParameter(deviceProvisionedListener, "p0");
        deviceProvisionedListener.onUserSwitched();
    }
}
