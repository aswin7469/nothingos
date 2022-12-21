package com.android.systemui.statusbar.policy;

import android.app.ActivityManager;
import android.content.Context;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.Log;
import com.android.settingslib.bluetooth.BluetoothCallback;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.settingslib.bluetooth.LocalBluetoothProfile;
import com.android.settingslib.bluetooth.LocalBluetoothProfileManager;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.android.systemui.statusbar.policy.BluetoothController;
import java.lang.ref.WeakReference;
import java.p026io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.WeakHashMap;
import javax.inject.Inject;

@SysUISingleton
public class BluetoothControllerImpl implements BluetoothController, BluetoothCallback, CachedBluetoothDevice.Callback, LocalBluetoothProfileManager.ServiceListener {
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);
    private static final String TAG = "BluetoothController";
    private CachedBluetoothDevice mActiveDevice;
    private boolean mAudioProfileOnly;
    private final Handler mBgHandler;
    private final WeakHashMap<CachedBluetoothDevice, ActuallyCachedState> mCachedState = new WeakHashMap<>();
    private final List<CachedBluetoothDevice> mConnectedDevices = new ArrayList();
    private int mConnectionState = 0;
    private final int mCurrentUser;
    private final DumpManager mDumpManager;
    /* access modifiers changed from: private */
    public boolean mEnabled;
    private final C3136H mHandler;
    private boolean mIsActive;
    private final LocalBluetoothManager mLocalBluetoothManager;
    private int mState;
    private final UserManager mUserManager;

    public void onServiceDisconnected() {
    }

    @Inject
    public BluetoothControllerImpl(Context context, DumpManager dumpManager, @Background Looper looper, @Main Looper looper2, LocalBluetoothManager localBluetoothManager) {
        this.mDumpManager = dumpManager;
        this.mLocalBluetoothManager = localBluetoothManager;
        this.mBgHandler = new Handler(looper);
        this.mHandler = new C3136H(looper2);
        if (localBluetoothManager != null) {
            localBluetoothManager.getEventManager().registerCallback(this);
            localBluetoothManager.getProfileManager().addServiceListener(this);
            onBluetoothStateChanged(localBluetoothManager.getBluetoothAdapter().getBluetoothState());
        }
        this.mUserManager = (UserManager) context.getSystemService("user");
        this.mCurrentUser = ActivityManager.getCurrentUser();
        dumpManager.registerDumpable(TAG, this);
    }

    public boolean canConfigBluetooth() {
        return !this.mUserManager.hasUserRestriction("no_config_bluetooth", UserHandle.of(this.mCurrentUser)) && !this.mUserManager.hasUserRestriction("no_bluetooth", UserHandle.of(this.mCurrentUser));
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("BluetoothController state:");
        printWriter.print("  mLocalBluetoothManager=");
        printWriter.println((Object) this.mLocalBluetoothManager);
        if (this.mLocalBluetoothManager != null) {
            printWriter.print("  mEnabled=");
            printWriter.println(this.mEnabled);
            printWriter.print("  mConnectionState=");
            printWriter.println(stateToString(this.mConnectionState));
            printWriter.print("  mAudioProfileOnly=");
            printWriter.println(this.mAudioProfileOnly);
            printWriter.print("  mIsActive=");
            printWriter.println(this.mIsActive);
            printWriter.print("  mConnectedDevices=");
            printWriter.println((Object) getConnectedDevices());
            printWriter.print("  mCallbacks.size=");
            printWriter.println(this.mHandler.mCallbacks.size());
            printWriter.println("  Bluetooth Devices:");
            for (CachedBluetoothDevice deviceString : getDevices()) {
                printWriter.println("    " + getDeviceString(deviceString));
            }
        }
    }

    private static String stateToString(int i) {
        if (i == 0) {
            return "DISCONNECTED";
        }
        if (i == 1) {
            return "CONNECTING";
        }
        if (i != 2) {
            return i != 3 ? "UNKNOWN(" + i + NavigationBarInflaterView.KEY_CODE_END : "DISCONNECTING";
        }
        return "CONNECTED";
    }

    private String getDeviceString(CachedBluetoothDevice cachedBluetoothDevice) {
        return cachedBluetoothDevice.getName() + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + cachedBluetoothDevice.getBondState() + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + cachedBluetoothDevice.isConnected();
    }

    public int getBondState(CachedBluetoothDevice cachedBluetoothDevice) {
        return getCachedState(cachedBluetoothDevice).mBondState;
    }

    public List<CachedBluetoothDevice> getConnectedDevices() {
        ArrayList arrayList;
        synchronized (this.mConnectedDevices) {
            arrayList = new ArrayList(this.mConnectedDevices);
        }
        return arrayList;
    }

    public int getMaxConnectionState(CachedBluetoothDevice cachedBluetoothDevice) {
        return getCachedState(cachedBluetoothDevice).mMaxConnectionState;
    }

    public void addCallback(BluetoothController.Callback callback) {
        this.mHandler.obtainMessage(3, callback).sendToTarget();
        this.mHandler.sendEmptyMessage(2);
    }

    public void removeCallback(BluetoothController.Callback callback) {
        this.mHandler.obtainMessage(4, callback).sendToTarget();
    }

    public boolean isBluetoothEnabled() {
        return this.mEnabled;
    }

    public int getBluetoothState() {
        return this.mState;
    }

    public boolean isBluetoothConnected() {
        return this.mConnectionState == 2;
    }

    public boolean isBluetoothConnecting() {
        return this.mConnectionState == 1;
    }

    public boolean isBluetoothAudioProfileOnly() {
        return this.mAudioProfileOnly;
    }

    public boolean isBluetoothAudioActive() {
        return this.mIsActive;
    }

    public void setBluetoothEnabled(boolean z) {
        LocalBluetoothManager localBluetoothManager = this.mLocalBluetoothManager;
        if (localBluetoothManager != null) {
            localBluetoothManager.getBluetoothAdapter().setBluetoothEnabled(z);
        }
    }

    public boolean isBluetoothSupported() {
        return this.mLocalBluetoothManager != null;
    }

    public void connect(CachedBluetoothDevice cachedBluetoothDevice) {
        if (this.mLocalBluetoothManager != null && cachedBluetoothDevice != null) {
            cachedBluetoothDevice.connect(true);
        }
    }

    public void disconnect(CachedBluetoothDevice cachedBluetoothDevice) {
        if (this.mLocalBluetoothManager != null && cachedBluetoothDevice != null) {
            cachedBluetoothDevice.disconnect();
        }
    }

    public String getConnectedDeviceName() {
        synchronized (this.mConnectedDevices) {
            if (this.mConnectedDevices.size() != 1) {
                return null;
            }
            String name = this.mConnectedDevices.get(0).getName();
            return name;
        }
    }

    public Collection<CachedBluetoothDevice> getDevices() {
        LocalBluetoothManager localBluetoothManager = this.mLocalBluetoothManager;
        if (localBluetoothManager != null) {
            return localBluetoothManager.getCachedDeviceManager().getCachedDevicesCopy();
        }
        return null;
    }

    private void updateConnected() {
        int connectionState = this.mLocalBluetoothManager.getBluetoothAdapter().getConnectionState();
        ArrayList arrayList = new ArrayList();
        for (CachedBluetoothDevice next : getDevices()) {
            int maxConnectionState = next.getMaxConnectionState();
            if (maxConnectionState > connectionState) {
                connectionState = maxConnectionState;
            }
            if (next.isConnected()) {
                arrayList.add(next);
            }
        }
        if (arrayList.isEmpty() && connectionState == 2) {
            connectionState = 0;
        }
        synchronized (this.mConnectedDevices) {
            this.mConnectedDevices.clear();
            this.mConnectedDevices.addAll(arrayList);
        }
        if (connectionState != this.mConnectionState) {
            this.mConnectionState = connectionState;
            this.mHandler.sendEmptyMessage(2);
        }
        updateAudioProfile();
    }

    private void updateActive() {
        boolean z = false;
        for (CachedBluetoothDevice next : getDevices()) {
            boolean z2 = true;
            if (!next.isActiveDevice(1) && !next.isActiveDevice(2) && !next.isActiveDevice(21)) {
                z2 = false;
            }
            z |= z2;
        }
        if (this.mIsActive != z) {
            this.mIsActive = z;
            this.mHandler.sendEmptyMessage(2);
        }
    }

    private void updateAudioProfile() {
        boolean z = false;
        boolean z2 = false;
        boolean z3 = false;
        for (CachedBluetoothDevice next : getDevices()) {
            for (LocalBluetoothProfile next2 : next.getProfiles()) {
                int profileId = next2.getProfileId();
                boolean isConnectedProfile = next.isConnectedProfile(next2);
                if (profileId == 1 || profileId == 2 || profileId == 21) {
                    z2 |= isConnectedProfile;
                } else {
                    z3 |= isConnectedProfile;
                }
            }
        }
        if (z2 && !z3) {
            z = true;
        }
        if (z != this.mAudioProfileOnly) {
            this.mAudioProfileOnly = z;
            this.mHandler.sendEmptyMessage(2);
        }
    }

    public void onBluetoothStateChanged(int i) {
        if (DEBUG) {
            Log.d(TAG, "BluetoothStateChanged=" + stateToString(i));
        }
        this.mEnabled = i == 12 || i == 11;
        this.mState = i;
        updateConnected();
        this.mHandler.sendEmptyMessage(2);
    }

    public void onDeviceAdded(CachedBluetoothDevice cachedBluetoothDevice) {
        if (DEBUG) {
            Log.d(TAG, "DeviceAdded=" + cachedBluetoothDevice.getAddress());
        }
        cachedBluetoothDevice.registerCallback(this);
        updateConnected();
        this.mHandler.sendEmptyMessage(1);
    }

    public void onDeviceDeleted(CachedBluetoothDevice cachedBluetoothDevice) {
        if (DEBUG) {
            Log.d(TAG, "DeviceDeleted=" + cachedBluetoothDevice.getAddress());
        }
        this.mCachedState.remove(cachedBluetoothDevice);
        updateConnected();
        this.mHandler.sendEmptyMessage(1);
    }

    public void onDeviceBondStateChanged(CachedBluetoothDevice cachedBluetoothDevice, int i) {
        if (DEBUG) {
            Log.d(TAG, "DeviceBondStateChanged=" + cachedBluetoothDevice.getAddress());
        }
        this.mCachedState.remove(cachedBluetoothDevice);
        updateConnected();
        this.mHandler.sendEmptyMessage(1);
    }

    public void onDeviceAttributesChanged() {
        if (DEBUG) {
            Log.d(TAG, "DeviceAttributesChanged");
        }
        updateConnected();
        this.mHandler.sendEmptyMessage(1);
    }

    public void onConnectionStateChanged(CachedBluetoothDevice cachedBluetoothDevice, int i) {
        if (DEBUG) {
            Log.d(TAG, "ConnectionStateChanged=" + cachedBluetoothDevice.getAddress() + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + stateToString(i));
        }
        this.mCachedState.remove(cachedBluetoothDevice);
        updateConnected();
        this.mHandler.sendEmptyMessage(2);
    }

    public void onProfileConnectionStateChanged(CachedBluetoothDevice cachedBluetoothDevice, int i, int i2) {
        if (DEBUG) {
            Log.d(TAG, "ProfileConnectionStateChanged=" + cachedBluetoothDevice.getAddress() + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + stateToString(i) + " profileId=" + i2);
        }
        this.mCachedState.remove(cachedBluetoothDevice);
        updateConnected();
        this.mHandler.sendEmptyMessage(2);
    }

    public void onActiveDeviceChanged(CachedBluetoothDevice cachedBluetoothDevice, int i) {
        if (DEBUG) {
            Log.d(TAG, "ActiveDeviceChanged=" + cachedBluetoothDevice.getAddress() + " profileId=" + i);
        }
        this.mActiveDevice = cachedBluetoothDevice;
        updateActive();
        this.mHandler.sendEmptyMessage(2);
    }

    public void onAclConnectionStateChanged(CachedBluetoothDevice cachedBluetoothDevice, int i) {
        if (DEBUG) {
            Log.d(TAG, "ACLConnectionStateChanged=" + cachedBluetoothDevice.getAddress() + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + stateToString(i));
        }
        this.mCachedState.remove(cachedBluetoothDevice);
        updateConnected();
        this.mHandler.sendEmptyMessage(2);
    }

    private ActuallyCachedState getCachedState(CachedBluetoothDevice cachedBluetoothDevice) {
        ActuallyCachedState actuallyCachedState = this.mCachedState.get(cachedBluetoothDevice);
        if (actuallyCachedState != null) {
            return actuallyCachedState;
        }
        ActuallyCachedState actuallyCachedState2 = new ActuallyCachedState(cachedBluetoothDevice, this.mHandler);
        this.mBgHandler.post(actuallyCachedState2);
        this.mCachedState.put(cachedBluetoothDevice, actuallyCachedState2);
        return actuallyCachedState2;
    }

    public void onServiceConnected() {
        updateConnected();
        this.mHandler.sendEmptyMessage(1);
    }

    private static class ActuallyCachedState implements Runnable {
        /* access modifiers changed from: private */
        public int mBondState;
        private final WeakReference<CachedBluetoothDevice> mDevice;
        /* access modifiers changed from: private */
        public int mMaxConnectionState;
        private final Handler mUiHandler;

        private ActuallyCachedState(CachedBluetoothDevice cachedBluetoothDevice, Handler handler) {
            this.mBondState = 10;
            this.mMaxConnectionState = 0;
            this.mDevice = new WeakReference<>(cachedBluetoothDevice);
            this.mUiHandler = handler;
        }

        public void run() {
            CachedBluetoothDevice cachedBluetoothDevice = this.mDevice.get();
            if (cachedBluetoothDevice != null) {
                this.mBondState = cachedBluetoothDevice.getBondState();
                this.mMaxConnectionState = cachedBluetoothDevice.getMaxConnectionState();
                this.mUiHandler.removeMessages(1);
                this.mUiHandler.sendEmptyMessage(1);
            }
        }
    }

    /* renamed from: com.android.systemui.statusbar.policy.BluetoothControllerImpl$H */
    private final class C3136H extends Handler {
        private static final int MSG_ADD_CALLBACK = 3;
        private static final int MSG_PAIRED_DEVICES_CHANGED = 1;
        private static final int MSG_REMOVE_CALLBACK = 4;
        private static final int MSG_STATE_CHANGED = 2;
        /* access modifiers changed from: private */
        public final ArrayList<BluetoothController.Callback> mCallbacks = new ArrayList<>();

        public C3136H(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                firePairedDevicesChanged();
            } else if (i == 2) {
                fireStateChange();
            } else if (i == 3) {
                this.mCallbacks.add((BluetoothController.Callback) message.obj);
            } else if (i == 4) {
                this.mCallbacks.remove((Object) (BluetoothController.Callback) message.obj);
            }
        }

        private void firePairedDevicesChanged() {
            Iterator<BluetoothController.Callback> it = this.mCallbacks.iterator();
            while (it.hasNext()) {
                it.next().onBluetoothDevicesChanged();
            }
        }

        private void fireStateChange() {
            Iterator<BluetoothController.Callback> it = this.mCallbacks.iterator();
            while (it.hasNext()) {
                fireStateChange(it.next());
            }
        }

        private void fireStateChange(BluetoothController.Callback callback) {
            callback.onBluetoothStateChange(BluetoothControllerImpl.this.mEnabled);
        }
    }

    public CachedBluetoothDevice getActiveDevice() {
        return this.mActiveDevice;
    }
}
