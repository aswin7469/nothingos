package com.android.systemui.statusbar.policy;

import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.systemui.Dumpable;
import java.util.Collection;
import java.util.List;
/* loaded from: classes2.dex */
public interface BluetoothController extends CallbackController<Callback>, Dumpable {

    /* loaded from: classes2.dex */
    public interface Callback {
        void onBluetoothDevicesChanged();

        void onBluetoothStateChange(boolean z);
    }

    boolean canConfigBluetooth();

    void connect(CachedBluetoothDevice cachedBluetoothDevice);

    void disconnect(CachedBluetoothDevice cachedBluetoothDevice);

    CachedBluetoothDevice getActiveDevice();

    int getBluetoothState();

    int getBondState(CachedBluetoothDevice cachedBluetoothDevice);

    List<CachedBluetoothDevice> getConnectedDevices();

    Collection<CachedBluetoothDevice> getDevices();

    boolean isBluetoothAudioActive();

    boolean isBluetoothAudioProfileOnly();

    boolean isBluetoothConnected();

    boolean isBluetoothConnecting();

    boolean isBluetoothEnabled();

    boolean isBluetoothSupported();

    void setBluetoothEnabled(boolean z);
}
