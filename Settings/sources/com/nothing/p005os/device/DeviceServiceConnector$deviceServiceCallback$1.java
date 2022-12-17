package com.nothing.p005os.device;

import android.os.Bundle;
import com.nothing.p005os.device.DeviceServiceConnector;
import com.nothing.p005os.device.IDeviceServiceCallBack;
import org.jetbrains.annotations.Nullable;

/* renamed from: com.nothing.os.device.DeviceServiceConnector$deviceServiceCallback$1 */
/* compiled from: DeviceServiceConnector.kt */
public final class DeviceServiceConnector$deviceServiceCallback$1 extends IDeviceServiceCallBack.Stub {
    final /* synthetic */ DeviceServiceConnector this$0;

    DeviceServiceConnector$deviceServiceCallback$1(DeviceServiceConnector deviceServiceConnector) {
        this.this$0 = deviceServiceConnector;
    }

    public void onSuccess(int i, @Nullable Bundle bundle) {
        if (bundle != null) {
            DeviceServiceConnector.Callback callback = this.this$0.getCallback();
            bundle.setClassLoader(callback == null ? null : callback.getClass().getClassLoader());
        }
        DeviceServiceConnector.Callback callback2 = this.this$0.getCallback();
        if (callback2 != null) {
            callback2.onSuccess(i, bundle);
        }
    }

    public void onFail(int i, int i2) {
        DeviceServiceConnector.Callback callback = this.this$0.getCallback();
        if (callback != null) {
            callback.onFail(i, i2);
        }
    }
}
