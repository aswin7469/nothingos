package com.nothing.p023os.device;

import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000Z\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\b\u0003\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004*\u0002\u000e\u0011\u0018\u00002\u00020\u0001:\u0002+,B\u000f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0006\u0010\u0019\u001a\u00020\u001aJ\u0006\u0010\u001b\u001a\u00020\u001aJ\u0018\u0010\u001c\u001a\u00020\u001a2\u0006\u0010\u001d\u001a\u00020\u00142\b\u0010\u001e\u001a\u0004\u0018\u00010\u001fJ\u0018\u0010 \u001a\u00020\u001a2\u0006\u0010\u001d\u001a\u00020\u00142\b\u0010\u001e\u001a\u0004\u0018\u00010\u001fJ\u0018\u0010!\u001a\u00020\u001a2\u0006\u0010\u001d\u001a\u00020\u00142\b\u0010\u001e\u001a\u0004\u0018\u00010\u001fJ\u0018\u0010\"\u001a\u00020\u001a2\u0006\u0010#\u001a\u00020$2\b\u0010%\u001a\u0004\u0018\u00010&J \u0010'\u001a\u00020\u001a2\u0006\u0010#\u001a\u00020$2\b\u0010%\u001a\u0004\u0018\u00010&2\u0006\u0010(\u001a\u00020)J\u0018\u0010*\u001a\u00020\u001f2\u0006\u0010\u001d\u001a\u00020\u00142\b\u0010\u001e\u001a\u0004\u0018\u00010\u001fR\u001c\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fX\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u00020\u000eX\u0004¢\u0006\u0004\n\u0002\u0010\u000fR\u0010\u0010\u0010\u001a\u00020\u0011X\u0004¢\u0006\u0004\n\u0002\u0010\u0012R\u001a\u0010\u0013\u001a\u00020\u0014X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\u0016\"\u0004\b\u0017\u0010\u0018¨\u0006-"}, mo64987d2 = {"Lcom/nothing/os/device/DeviceServiceConnector;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "callback", "Lcom/nothing/os/device/DeviceServiceConnector$Callback;", "getCallback", "()Lcom/nothing/os/device/DeviceServiceConnector$Callback;", "setCallback", "(Lcom/nothing/os/device/DeviceServiceConnector$Callback;)V", "deviceService", "Lcom/nothing/os/device/IDeviceService;", "deviceServiceCallback", "com/nothing/os/device/DeviceServiceConnector$deviceServiceCallback$1", "Lcom/nothing/os/device/DeviceServiceConnector$deviceServiceCallback$1;", "serviceConnection", "com/nothing/os/device/DeviceServiceConnector$serviceConnection$1", "Lcom/nothing/os/device/DeviceServiceConnector$serviceConnection$1;", "serviceStatus", "", "getServiceStatus", "()I", "setServiceStatus", "(I)V", "connectService", "", "disconnectService", "getCommand", "command", "extras", "Landroid/os/Bundle;", "sendCommand", "setCommand", "setModelIdAndDevice", "modelId", "", "device", "Landroid/bluetooth/BluetoothDevice;", "setModelIdAndDeviceConnected", "autoConnected", "", "syncCommand", "Builder", "Callback", "osConnect_SnapshotRelease"}, mo64988k = 1, mo64989mv = {1, 5, 1}, mo64991xi = 48)
/* renamed from: com.nothing.os.device.DeviceServiceConnector */
/* compiled from: DeviceServiceConnector.kt */
public final class DeviceServiceConnector {
    private Callback callback;
    private final Context context;
    /* access modifiers changed from: private */
    public IDeviceService deviceService;
    /* access modifiers changed from: private */
    public final DeviceServiceConnector$deviceServiceCallback$1 deviceServiceCallback;
    private final DeviceServiceConnector$serviceConnection$1 serviceConnection;
    private int serviceStatus;

    @Metadata(mo64986d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\u0016J\b\u0010\u0004\u001a\u00020\u0003H\u0016J\u0018\u0010\u0005\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u0007H\u0016J\u001a\u0010\t\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u00072\b\u0010\n\u001a\u0004\u0018\u00010\u000bH\u0016¨\u0006\f"}, mo64987d2 = {"Lcom/nothing/os/device/DeviceServiceConnector$Callback;", "", "onDeviceServiceConnected", "", "onDeviceServiceDisConnected", "onFail", "command", "", "code", "onSuccess", "extras", "Landroid/os/Bundle;", "osConnect_SnapshotRelease"}, mo64988k = 1, mo64989mv = {1, 5, 1}, mo64991xi = 48)
    /* renamed from: com.nothing.os.device.DeviceServiceConnector$Callback */
    /* compiled from: DeviceServiceConnector.kt */
    public interface Callback {

        @Metadata(mo64988k = 3, mo64989mv = {1, 5, 1}, mo64991xi = 48)
        /* renamed from: com.nothing.os.device.DeviceServiceConnector$Callback$DefaultImpls */
        /* compiled from: DeviceServiceConnector.kt */
        public static final class DefaultImpls {
            public static void onDeviceServiceConnected(Callback callback) {
                Intrinsics.checkNotNullParameter(callback, "this");
            }

            public static void onDeviceServiceDisConnected(Callback callback) {
                Intrinsics.checkNotNullParameter(callback, "this");
            }

            public static void onFail(Callback callback, int i, int i2) {
                Intrinsics.checkNotNullParameter(callback, "this");
            }

            public static void onSuccess(Callback callback, int i, Bundle bundle) {
                Intrinsics.checkNotNullParameter(callback, "this");
            }
        }

        void onDeviceServiceConnected();

        void onDeviceServiceDisConnected();

        void onFail(int i, int i2);

        void onSuccess(int i, Bundle bundle);
    }

    public /* synthetic */ DeviceServiceConnector(Context context2, DefaultConstructorMarker defaultConstructorMarker) {
        this(context2);
    }

    private DeviceServiceConnector(Context context2) {
        this.context = context2;
        this.serviceConnection = new DeviceServiceConnector$serviceConnection$1(this);
        this.deviceServiceCallback = new DeviceServiceConnector$deviceServiceCallback$1(this);
    }

    public final Callback getCallback() {
        return this.callback;
    }

    public final void setCallback(Callback callback2) {
        this.callback = callback2;
    }

    public final int getServiceStatus() {
        return this.serviceStatus;
    }

    public final void setServiceStatus(int i) {
        this.serviceStatus = i;
    }

    public final void sendCommand(int i, Bundle bundle) {
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

    public final Bundle syncCommand(int i, Bundle bundle) {
        Log.e("DeviceControlService", " syncCommand: " + i + ". " + bundle + "... " + this.deviceService + ' ');
        try {
            IDeviceService iDeviceService = this.deviceService;
            Bundle syncCommand = iDeviceService == null ? null : iDeviceService.syncCommand(i, bundle);
            if (syncCommand == null) {
                return new Bundle();
            }
            return syncCommand;
        } catch (Exception unused) {
            return new Bundle();
        }
    }

    public final void setCommand(int i, Bundle bundle) {
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

    public final void getCommand(int i, Bundle bundle) {
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

    public final void setModelIdAndDevice(String str, BluetoothDevice bluetoothDevice) {
        Intrinsics.checkNotNullParameter(str, "modelId");
        Log.e("DeviceControlService", " setModelIdAndDevice:   " + str + ' ' + this.deviceService + ' ');
        try {
            IDeviceService iDeviceService = this.deviceService;
            if (iDeviceService != null) {
                iDeviceService.setModelIdAndDevice(str, bluetoothDevice);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final void setModelIdAndDeviceConnected(String str, BluetoothDevice bluetoothDevice, boolean z) {
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
            intent.setComponent(new ComponentName("com.nothing.smartcenter", DeviceConstant.CONTROL_CLS_NAME));
            intent.setAction(DeviceConstant.CONTROL_ACTION);
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

    @Metadata(mo64986d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0006\u0010\u0007\u001a\u00020\u0006J\u000e\u0010\b\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\nR\u000e\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006\u000b"}, mo64987d2 = {"Lcom/nothing/os/device/DeviceServiceConnector$Builder;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "connectorClient", "Lcom/nothing/os/device/DeviceServiceConnector;", "build", "setCallback", "cb", "Lcom/nothing/os/device/DeviceServiceConnector$Callback;", "osConnect_SnapshotRelease"}, mo64988k = 1, mo64989mv = {1, 5, 1}, mo64991xi = 48)
    /* renamed from: com.nothing.os.device.DeviceServiceConnector$Builder */
    /* compiled from: DeviceServiceConnector.kt */
    public static final class Builder {
        private final DeviceServiceConnector connectorClient;

        public Builder(Context context) {
            Intrinsics.checkNotNullParameter(context, "context");
            this.connectorClient = new DeviceServiceConnector(context, (DefaultConstructorMarker) null);
        }

        public final Builder setCallback(Callback callback) {
            Intrinsics.checkNotNullParameter(callback, "cb");
            this.connectorClient.setCallback(callback);
            return this;
        }

        public final DeviceServiceConnector build() {
            return this.connectorClient;
        }
    }
}
