package com.android.systemui.statusbar.policy;

import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: DeviceProvisionedControllerImpl.kt */
/* synthetic */ class DeviceProvisionedControllerImpl$onUserSetupChanged$1 extends FunctionReferenceImpl implements Function1<DeviceProvisionedController.DeviceProvisionedListener, Unit> {
    public static final DeviceProvisionedControllerImpl$onUserSetupChanged$1 INSTANCE = new DeviceProvisionedControllerImpl$onUserSetupChanged$1();

    DeviceProvisionedControllerImpl$onUserSetupChanged$1() {
        super(1, DeviceProvisionedController.DeviceProvisionedListener.class, "onUserSetupChanged", "onUserSetupChanged()V", 0);
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        invoke((DeviceProvisionedController.DeviceProvisionedListener) obj);
        return Unit.INSTANCE;
    }

    public final void invoke(DeviceProvisionedController.DeviceProvisionedListener deviceProvisionedListener) {
        Intrinsics.checkNotNullParameter(deviceProvisionedListener, "p0");
        deviceProvisionedListener.onUserSetupChanged();
    }
}
