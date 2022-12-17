package com.nothing.p005os.device;

import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* renamed from: com.nothing.os.device.DeviceServiceConnector */
/* compiled from: DeviceServiceConnector.kt */
public final class DeviceServiceConnector {
    @Nullable
    private Callback callback;
    @NotNull
    private final Context context;
    /* access modifiers changed from: private */
    @Nullable
    public IDeviceService deviceService;
    /* access modifiers changed from: private */
    @NotNull
    public final DeviceServiceConnector$deviceServiceCallback$1 deviceServiceCallback;
    @NotNull
    private final DeviceServiceConnector$serviceConnection$1 serviceConnection;
    private int serviceStatus;

    /* renamed from: com.nothing.os.device.DeviceServiceConnector$Callback */
    /* compiled from: DeviceServiceConnector.kt */
    public interface Callback {
        void onDeviceServiceConnected();

        void onDeviceServiceDisConnected();

        void onFail(int i, int i2);

        void onSuccess(int i, @Nullable Bundle bundle);
    }

    public /* synthetic */ DeviceServiceConnector(Context context2, DefaultConstructorMarker defaultConstructorMarker) {
        this(context2);
    }

    private DeviceServiceConnector(Context context2) {
        this.context = context2;
        this.serviceConnection = new DeviceServiceConnector$serviceConnection$1(this);
        this.deviceServiceCallback = new DeviceServiceConnector$deviceServiceCallback$1(this);
    }

    @Nullable
    public final Callback getCallback() {
        return this.callback;
    }

    public final void setCallback(@Nullable Callback callback2) {
        this.callback = callback2;
    }

    public final int getServiceStatus() {
        return this.serviceStatus;
    }

    public final void setServiceStatus(int i) {
        this.serviceStatus = i;
    }

    public final void sendCommand(int i, @Nullable Bundle bundle) {
        Log.e("DeviceControlService", " sendCommand: " + i + ". " + bundle + "... " + this.deviceService + ' ');
        try {
            IDeviceService iDeviceService = this.deviceService;
            if (iDeviceService != null) {
                iDeviceService.sendCommand(i, bundle);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final void setCommand(int i, @Nullable Bundle bundle) {
        Log.e("DeviceControlService", " setCommand: " + i + ". " + bundle + "... " + this.deviceService + ' ');
        try {
            IDeviceService iDeviceService = this.deviceService;
            if (iDeviceService != null) {
                iDeviceService.setCommand(i, bundle);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final void getCommand(int i, @Nullable Bundle bundle) {
        Log.e("DeviceControlService", " getCommand: " + i + ". " + bundle + "... " + this.deviceService + ' ');
        try {
            IDeviceService iDeviceService = this.deviceService;
            if (iDeviceService != null) {
                iDeviceService.getCommand(i, bundle);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final void setModelIdAndDeviceConnected(@NotNull String str, @Nullable BluetoothDevice bluetoothDevice, boolean z) {
        Intrinsics.checkNotNullParameter(str, "modelId");
        Log.e("DeviceControlService", " setModelIdAndDevice:   " + str + ' ' + this.deviceService + ' ' + z);
        try {
            IDeviceService iDeviceService = this.deviceService;
            if (iDeviceService != null) {
                iDeviceService.setModelIdAndDeviceConnected(str, bluetoothDevice, z);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final void connectService() {
        try {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.nothing.smartcenter", "com.nothing.os.device.DeviceControlService"));
            intent.setAction("com.nothing.os.device.intent.action.BIND_DEVICE_SERVICE");
            boolean bindService = this.context.bindService(intent, this.serviceConnection, 1);
            Log.e("DeviceControlService", " bindService  result " + bindService + "  " + this.context + ' ');
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final void disconnectService() {
        Log.e("DeviceControlService", " disconnectService ");
        try {
            IDeviceService iDeviceService = this.deviceService;
            if (iDeviceService != null) {
                iDeviceService.unRegisterCallBack(this.deviceServiceCallback);
                this.context.unbindService(this.serviceConnection);
            }
        } catch (Exception e) {
            Log.e("DeviceControlService", Intrinsics.stringPlus(" disconnectService: unRegisterCallBack ", e));
        }
    }

    /* renamed from: com.nothing.os.device.DeviceServiceConnector$Builder */
    /* compiled from: DeviceServiceConnector.kt */
    public static final class Builder {
        @NotNull
        private final DeviceServiceConnector connectorClient;

        public Builder(@NotNull Context context) {
            Intrinsics.checkNotNullParameter(context, "context");
            this.connectorClient = new DeviceServiceConnector(context, (DefaultConstructorMarker) null);
        }

        @NotNull
        public final Builder setCallback(@NotNull Callback callback) {
            Intrinsics.checkNotNullParameter(callback, "cb");
            this.connectorClient.setCallback(callback);
            return this;
        }

        @NotNull
        public final DeviceServiceConnector build() {
            return this.connectorClient;
        }
    }
}
