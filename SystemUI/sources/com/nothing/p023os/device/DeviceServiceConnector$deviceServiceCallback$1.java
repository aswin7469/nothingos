package com.nothing.p023os.device;

import android.os.Bundle;
import com.nothing.p023os.device.DeviceServiceConnector;
import com.nothing.p023os.device.IDeviceServiceCallBack;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000\u001f\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0005H\u0016J\u001a\u0010\u0007\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\b\u0010\b\u001a\u0004\u0018\u00010\tH\u0016¨\u0006\n"}, mo65043d2 = {"com/nothing/os/device/DeviceServiceConnector$deviceServiceCallback$1", "Lcom/nothing/os/device/IDeviceServiceCallBack$Stub;", "onFail", "", "command", "", "code", "onSuccess", "extras", "Landroid/os/Bundle;", "osConnect_SnapshotRelease"}, mo65044k = 1, mo65045mv = {1, 5, 1}, mo65047xi = 48)
/* renamed from: com.nothing.os.device.DeviceServiceConnector$deviceServiceCallback$1 */
/* compiled from: DeviceServiceConnector.kt */
public final class DeviceServiceConnector$deviceServiceCallback$1 extends IDeviceServiceCallBack.Stub {
    final /* synthetic */ DeviceServiceConnector this$0;

    DeviceServiceConnector$deviceServiceCallback$1(DeviceServiceConnector deviceServiceConnector) {
        this.this$0 = deviceServiceConnector;
    }

    public void onSuccess(int i, Bundle bundle) {
        Class<?> cls;
        if (bundle != null) {
            DeviceServiceConnector.Callback callback = this.this$0.getCallback();
            ClassLoader classLoader = null;
            if (!(callback == null || (cls = callback.getClass()) == null)) {
                classLoader = cls.getClassLoader();
            }
            bundle.setClassLoader(classLoader);
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
