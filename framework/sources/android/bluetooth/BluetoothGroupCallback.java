package android.bluetooth;

import java.util.List;
import java.util.UUID;
/* loaded from: classes.dex */
public abstract class BluetoothGroupCallback {
    public void onConnectionStateChanged(int state, BluetoothDevice device) {
    }

    public void onGroupClientAppRegistered(int status, int appId) {
    }

    public void onNewGroupFound(int groupId, BluetoothDevice device, UUID uuid) {
    }

    public void onGroupDiscoveryStatusChanged(int groupId, int status, int reason) {
    }

    public void onGroupDeviceFound(int groupId, BluetoothDevice device) {
    }

    public void onExclusiveAccessChanged(int groupId, int value, int status, List<BluetoothDevice> devices) {
    }

    public void onExclusiveAccessStatusFetched(int groupId, int accessStatus) {
    }

    public void onExclusiveAccessAvailable(int groupId, BluetoothDevice device) {
    }
}
