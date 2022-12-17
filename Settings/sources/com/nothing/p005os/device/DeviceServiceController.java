package com.nothing.p005os.device;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.nothing.p005os.device.DeviceServiceConnector;
import java.util.ArrayList;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* renamed from: com.nothing.os.device.DeviceServiceController */
/* compiled from: DeviceServiceController.kt */
public final class DeviceServiceController implements DeviceServiceConnector.Callback {
    @NotNull
    private final ArrayList<DeviceServiceConnector.Callback> callbacks;
    @NotNull
    private final Context context;
    @NotNull
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    @NotNull
    private final DeviceServiceConnector serviceProvider;

    public DeviceServiceController(@NotNull Context context2) {
        Intrinsics.checkNotNullParameter(context2, "context");
        this.context = context2;
        this.serviceProvider = new DeviceServiceConnector.Builder(context2).setCallback(this).build();
        this.callbacks = new ArrayList<>();
    }

    public final void setCommand(int i, @Nullable Bundle bundle) {
        this.serviceProvider.setCommand(i, bundle);
    }

    public final void getCommand(int i, @Nullable Bundle bundle) {
        this.serviceProvider.getCommand(i, bundle);
    }

    public final void sendCommand(int i, @Nullable Bundle bundle) {
        this.serviceProvider.sendCommand(i, bundle);
    }

    public final void setModelIdAndDeviceConnected(@NotNull String str, @Nullable BluetoothDevice bluetoothDevice, boolean z) {
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

    public void onSuccess(int i, @Nullable Bundle bundle) {
        this.mainHandler.post(new DeviceServiceController$$ExternalSyntheticLambda3(this, i, bundle));
    }

    /* access modifiers changed from: private */
    /* renamed from: onSuccess$lambda-1  reason: not valid java name */
    public static final void m2210onSuccess$lambda1(DeviceServiceController deviceServiceController, int i, Bundle bundle) {
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
    public static final void m2207onDeviceServiceConnected$lambda3(DeviceServiceController deviceServiceController) {
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
    public static final void m2208onDeviceServiceDisConnected$lambda5(DeviceServiceController deviceServiceController) {
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
    public static final void m2209onFail$lambda7(DeviceServiceController deviceServiceController, int i, int i2) {
        Intrinsics.checkNotNullParameter(deviceServiceController, "this$0");
        for (DeviceServiceConnector.Callback onFail : deviceServiceController.callbacks) {
            onFail.onFail(i, i2);
        }
    }

    public final void addCallback(@NotNull DeviceServiceConnector.Callback callback) {
        Intrinsics.checkNotNullParameter(callback, "cb");
        Log.i("DeviceControlService", Intrinsics.stringPlus("addCallback  ", callback));
        this.callbacks.add(callback);
        if (this.callbacks.size() > 0) {
            Log.i("DeviceControlService", "addCallback connectService");
            this.serviceProvider.connectService();
        }
    }

    public final void removeCallback(@NotNull DeviceServiceConnector.Callback callback) {
        Intrinsics.checkNotNullParameter(callback, "cb");
        Log.i("DeviceControlService", Intrinsics.stringPlus("remove call back ", callback));
        this.callbacks.remove(callback);
        if (this.callbacks.size() == 0) {
            Log.i("DeviceControlService", "removeCallback disconnectService");
            this.serviceProvider.disconnectService();
        }
    }
}
