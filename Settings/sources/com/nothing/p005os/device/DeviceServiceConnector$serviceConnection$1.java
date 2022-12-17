package com.nothing.p005os.device;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import com.nothing.p005os.device.DeviceServiceConnector;
import com.nothing.p005os.device.IDeviceService;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.Nullable;

/* renamed from: com.nothing.os.device.DeviceServiceConnector$serviceConnection$1 */
/* compiled from: DeviceServiceConnector.kt */
public final class DeviceServiceConnector$serviceConnection$1 implements ServiceConnection {
    final /* synthetic */ DeviceServiceConnector this$0;

    DeviceServiceConnector$serviceConnection$1(DeviceServiceConnector deviceServiceConnector) {
        this.this$0 = deviceServiceConnector;
    }

    public void onBindingDied(@Nullable ComponentName componentName) {
        super.onBindingDied(componentName);
        Log.e("DeviceControlService", Intrinsics.stringPlus("onBindingDied.....", Integer.valueOf(this.this$0.getServiceStatus())));
        this.this$0.setServiceStatus(3);
    }

    public void onNullBinding(@Nullable ComponentName componentName) {
        super.onNullBinding(componentName);
        Log.e("DeviceControlService", "onNullBinding......");
        this.this$0.setServiceStatus(4);
    }

    public void onServiceConnected(@Nullable ComponentName componentName, @Nullable IBinder iBinder) {
        Log.i("DeviceControlService", "onServiceConnected......");
        this.this$0.setServiceStatus(1);
        this.this$0.deviceService = IDeviceService.Stub.asInterface(iBinder);
        try {
            IDeviceService access$getDeviceService$p = this.this$0.deviceService;
            if (access$getDeviceService$p != null) {
                access$getDeviceService$p.registerCallBack(this.this$0.deviceServiceCallback);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        DeviceServiceConnector.Callback callback = this.this$0.getCallback();
        if (callback != null) {
            callback.onDeviceServiceConnected();
        }
    }

    public void onServiceDisconnected(@Nullable ComponentName componentName) {
        this.this$0.setServiceStatus(2);
        Log.e("DeviceControlService", " onServiceDisconnected.....");
        try {
            IDeviceService access$getDeviceService$p = this.this$0.deviceService;
            if (access$getDeviceService$p != null) {
                access$getDeviceService$p.unRegisterCallBack(this.this$0.deviceServiceCallback);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.this$0.deviceService = null;
        DeviceServiceConnector.Callback callback = this.this$0.getCallback();
        if (callback != null) {
            callback.onDeviceServiceDisConnected();
        }
    }
}
