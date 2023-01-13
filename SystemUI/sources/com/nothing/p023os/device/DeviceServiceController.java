package com.nothing.p023os.device;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.nothing.p023os.device.DeviceServiceConnector;
import java.util.ArrayList;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u000e\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0001J\u000e\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0014J\u001a\u0010\u0015\u001a\u00020\u000f2\u0006\u0010\u0016\u001a\u00020\u00122\n\b\u0002\u0010\u0017\u001a\u0004\u0018\u00010\u0018J\b\u0010\u0019\u001a\u00020\u000fH\u0016J\b\u0010\u001a\u001a\u00020\u000fH\u0016J\u0018\u0010\u001b\u001a\u00020\u000f2\u0006\u0010\u0016\u001a\u00020\u00122\u0006\u0010\u001c\u001a\u00020\u0012H\u0016J\u001a\u0010\u001d\u001a\u00020\u000f2\u0006\u0010\u0016\u001a\u00020\u00122\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018H\u0016J\u000e\u0010\u001e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0001J\u001a\u0010\u001f\u001a\u00020\u000f2\u0006\u0010\u0016\u001a\u00020\u00122\n\b\u0002\u0010\u0017\u001a\u0004\u0018\u00010\u0018J\u001a\u0010 \u001a\u00020\u000f2\u0006\u0010\u0016\u001a\u00020\u00122\n\b\u0002\u0010\u0017\u001a\u0004\u0018\u00010\u0018J\u0018\u0010!\u001a\u00020\u000f2\u0006\u0010\"\u001a\u00020#2\b\u0010\u0017\u001a\u0004\u0018\u00010$J \u0010%\u001a\u00020\u000f2\u0006\u0010\"\u001a\u00020#2\b\u0010\u0017\u001a\u0004\u0018\u00010$2\u0006\u0010&\u001a\u00020\u0014J\u001a\u0010'\u001a\u00020\u00182\u0006\u0010\u0016\u001a\u00020\u00122\n\b\u0002\u0010\u0017\u001a\u0004\u0018\u00010\u0018R\u001e\u0010\u0005\u001a\u0012\u0012\u0004\u0012\u00020\u00010\u0006j\b\u0012\u0004\u0012\u00020\u0001`\u0007X\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u000e\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0004¢\u0006\u0002\n\u0000¨\u0006("}, mo65043d2 = {"Lcom/nothing/os/device/DeviceServiceController;", "Lcom/nothing/os/device/DeviceServiceConnector$Callback;", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "callbacks", "Ljava/util/ArrayList;", "Lkotlin/collections/ArrayList;", "getContext", "()Landroid/content/Context;", "mainHandler", "Landroid/os/Handler;", "serviceProvider", "Lcom/nothing/os/device/DeviceServiceConnector;", "addCallback", "", "cb", "checkBindStatus", "", "isRebind", "", "getCommand", "command", "extras", "Landroid/os/Bundle;", "onDeviceServiceConnected", "onDeviceServiceDisConnected", "onFail", "code", "onSuccess", "removeCallback", "sendCommand", "setCommand", "setModelIdAndDevice", "modelId", "", "Landroid/bluetooth/BluetoothDevice;", "setModelIdAndDeviceConnected", "autoConnected", "syncCommand", "osConnect_SnapshotRelease"}, mo65044k = 1, mo65045mv = {1, 5, 1}, mo65047xi = 48)
/* renamed from: com.nothing.os.device.DeviceServiceController */
/* compiled from: DeviceServiceController.kt */
public final class DeviceServiceController implements DeviceServiceConnector.Callback {
    private final ArrayList<DeviceServiceConnector.Callback> callbacks;
    private final Context context;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final DeviceServiceConnector serviceProvider;

    public DeviceServiceController(Context context2) {
        Intrinsics.checkNotNullParameter(context2, "context");
        this.context = context2;
        this.serviceProvider = new DeviceServiceConnector.Builder(context2).setCallback(this).build();
        this.callbacks = new ArrayList<>();
    }

    public final Context getContext() {
        return this.context;
    }

    public static /* synthetic */ Bundle syncCommand$default(DeviceServiceController deviceServiceController, int i, Bundle bundle, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            bundle = new Bundle();
        }
        return deviceServiceController.syncCommand(i, bundle);
    }

    public final Bundle syncCommand(int i, Bundle bundle) {
        return this.serviceProvider.syncCommand(i, bundle);
    }

    public static /* synthetic */ void setCommand$default(DeviceServiceController deviceServiceController, int i, Bundle bundle, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            bundle = new Bundle();
        }
        deviceServiceController.setCommand(i, bundle);
    }

    public final void setCommand(int i, Bundle bundle) {
        this.serviceProvider.setCommand(i, bundle);
    }

    public static /* synthetic */ void getCommand$default(DeviceServiceController deviceServiceController, int i, Bundle bundle, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            bundle = new Bundle();
        }
        deviceServiceController.getCommand(i, bundle);
    }

    public final void getCommand(int i, Bundle bundle) {
        this.serviceProvider.getCommand(i, bundle);
    }

    public static /* synthetic */ void sendCommand$default(DeviceServiceController deviceServiceController, int i, Bundle bundle, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            bundle = new Bundle();
        }
        deviceServiceController.sendCommand(i, bundle);
    }

    public final void sendCommand(int i, Bundle bundle) {
        this.serviceProvider.sendCommand(i, bundle);
    }

    public final void setModelIdAndDevice(String str, BluetoothDevice bluetoothDevice) {
        Intrinsics.checkNotNullParameter(str, "modelId");
        this.serviceProvider.setModelIdAndDevice(str, bluetoothDevice);
    }

    public final void setModelIdAndDeviceConnected(String str, BluetoothDevice bluetoothDevice, boolean z) {
        Intrinsics.checkNotNullParameter(str, "modelId");
        this.serviceProvider.setModelIdAndDeviceConnected(str, bluetoothDevice, z);
    }

    public final int checkBindStatus(boolean z) {
        int serviceStatus = this.serviceProvider.getServiceStatus();
        if (z && serviceStatus != 1) {
            this.serviceProvider.connectService();
        }
        return serviceStatus;
    }

    public void onSuccess(int i, Bundle bundle) {
        this.mainHandler.post(new DeviceServiceController$$ExternalSyntheticLambda3(this, i, bundle));
    }

    /* access modifiers changed from: private */
    /* renamed from: onSuccess$lambda-1  reason: not valid java name */
    public static final void m3497onSuccess$lambda1(DeviceServiceController deviceServiceController, int i, Bundle bundle) {
        Intrinsics.checkNotNullParameter(deviceServiceController, "this$0");
        for (DeviceServiceConnector.Callback onSuccess : deviceServiceController.callbacks) {
            onSuccess.onSuccess(i, bundle);
        }
    }

    public void onDeviceServiceConnected() {
        this.mainHandler.post(new DeviceServiceController$$ExternalSyntheticLambda2(this));
    }

    /* access modifiers changed from: private */
    /* renamed from: onDeviceServiceConnected$lambda-3  reason: not valid java name */
    public static final void m3494onDeviceServiceConnected$lambda3(DeviceServiceController deviceServiceController) {
        Intrinsics.checkNotNullParameter(deviceServiceController, "this$0");
        for (DeviceServiceConnector.Callback onDeviceServiceConnected : deviceServiceController.callbacks) {
            onDeviceServiceConnected.onDeviceServiceConnected();
        }
    }

    public void onDeviceServiceDisConnected() {
        this.mainHandler.post(new DeviceServiceController$$ExternalSyntheticLambda1(this));
    }

    /* access modifiers changed from: private */
    /* renamed from: onDeviceServiceDisConnected$lambda-5  reason: not valid java name */
    public static final void m3495onDeviceServiceDisConnected$lambda5(DeviceServiceController deviceServiceController) {
        Intrinsics.checkNotNullParameter(deviceServiceController, "this$0");
        for (DeviceServiceConnector.Callback onDeviceServiceDisConnected : deviceServiceController.callbacks) {
            onDeviceServiceDisConnected.onDeviceServiceDisConnected();
        }
    }

    public void onFail(int i, int i2) {
        this.mainHandler.post(new DeviceServiceController$$ExternalSyntheticLambda0(this, i, i2));
    }

    /* access modifiers changed from: private */
    /* renamed from: onFail$lambda-7  reason: not valid java name */
    public static final void m3496onFail$lambda7(DeviceServiceController deviceServiceController, int i, int i2) {
        Intrinsics.checkNotNullParameter(deviceServiceController, "this$0");
        for (DeviceServiceConnector.Callback onFail : deviceServiceController.callbacks) {
            onFail.onFail(i, i2);
        }
    }

    public final void addCallback(DeviceServiceConnector.Callback callback) {
        Intrinsics.checkNotNullParameter(callback, "cb");
        Log.i("DeviceControlService", Intrinsics.stringPlus("addCallback  ", callback));
        this.callbacks.add(callback);
        if (this.callbacks.size() > 0) {
            Log.i("DeviceControlService", "addCallback connectService");
            this.serviceProvider.connectService();
        }
    }

    public final void removeCallback(DeviceServiceConnector.Callback callback) {
        Intrinsics.checkNotNullParameter(callback, "cb");
        Log.i("DeviceControlService", Intrinsics.stringPlus("remove call back ", callback));
        this.callbacks.remove((Object) callback);
        if (this.callbacks.size() == 0) {
            Log.i("DeviceControlService", "removeCallback disconnectService");
            this.serviceProvider.disconnectService();
        }
    }
}
