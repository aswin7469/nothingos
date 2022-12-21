package com.nothing.p023os.device;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import com.nothing.p023os.device.DeviceServiceConnector;
import com.nothing.p023os.device.IDeviceService;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000!\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0012\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u0016J\u0012\u0010\u0006\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u0016J\u001c\u0010\u0007\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u00052\b\u0010\b\u001a\u0004\u0018\u00010\tH\u0016J\u0012\u0010\n\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u0016¨\u0006\u000b"}, mo64987d2 = {"com/nothing/os/device/DeviceServiceConnector$serviceConnection$1", "Landroid/content/ServiceConnection;", "onBindingDied", "", "name", "Landroid/content/ComponentName;", "onNullBinding", "onServiceConnected", "service", "Landroid/os/IBinder;", "onServiceDisconnected", "osConnect_SnapshotRelease"}, mo64988k = 1, mo64989mv = {1, 5, 1}, mo64991xi = 48)
/* renamed from: com.nothing.os.device.DeviceServiceConnector$serviceConnection$1 */
/* compiled from: DeviceServiceConnector.kt */
public final class DeviceServiceConnector$serviceConnection$1 implements ServiceConnection {
    final /* synthetic */ DeviceServiceConnector this$0;

    DeviceServiceConnector$serviceConnection$1(DeviceServiceConnector deviceServiceConnector) {
        this.this$0 = deviceServiceConnector;
    }

    public void onBindingDied(ComponentName componentName) {
        super.onBindingDied(componentName);
        Log.e("DeviceControlService", Intrinsics.stringPlus("onBindingDied.....", Integer.valueOf(this.this$0.getServiceStatus())));
        this.this$0.setServiceStatus(3);
    }

    public void onNullBinding(ComponentName componentName) {
        super.onNullBinding(componentName);
        Log.e("DeviceControlService", "onNullBinding......");
        this.this$0.setServiceStatus(4);
    }

    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
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

    public void onServiceDisconnected(ComponentName componentName) {
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
