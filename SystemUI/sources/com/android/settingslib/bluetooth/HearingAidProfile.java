package com.android.settingslib.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHearingAid;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.util.Log;
import com.android.settingslib.C1757R;
import com.android.settingslib.Utils;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class HearingAidProfile implements LocalBluetoothProfile {
    static final String NAME = "HearingAid";
    private static final int ORDINAL = 1;
    private static final String TAG = "HearingAidProfile";
    /* access modifiers changed from: private */

    /* renamed from: V */
    public static boolean f242V = true;
    private final BluetoothAdapter mBluetoothAdapter;
    private Context mContext;
    /* access modifiers changed from: private */
    public final CachedBluetoothDeviceManager mDeviceManager;
    /* access modifiers changed from: private */
    public boolean mIsProfileReady;
    /* access modifiers changed from: private */
    public final LocalBluetoothProfileManager mProfileManager;
    /* access modifiers changed from: private */
    public BluetoothHearingAid mService;

    @Retention(RetentionPolicy.SOURCE)
    public @interface DeviceMode {
        public static final int MODE_BINAURAL = 1;
        public static final int MODE_INVALID = -1;
        public static final int MODE_MONAURAL = 0;
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface DeviceSide {
        public static final int SIDE_INVALID = -1;
        public static final int SIDE_LEFT = 0;
        public static final int SIDE_RIGHT = 1;
    }

    public boolean accessProfileEnabled() {
        return false;
    }

    public int getDrawableResource(BluetoothClass bluetoothClass) {
        return 17302338;
    }

    public int getOrdinal() {
        return 1;
    }

    public int getProfileId() {
        return 21;
    }

    public boolean isAutoConnectable() {
        return true;
    }

    public String toString() {
        return NAME;
    }

    private final class HearingAidServiceListener implements BluetoothProfile.ServiceListener {
        private HearingAidServiceListener() {
        }

        public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
            BluetoothHearingAid unused = HearingAidProfile.this.mService = (BluetoothHearingAid) bluetoothProfile;
            List<BluetoothDevice> connectedDevices = HearingAidProfile.this.mService.getConnectedDevices();
            while (!connectedDevices.isEmpty()) {
                BluetoothDevice remove = connectedDevices.remove(0);
                CachedBluetoothDevice findDevice = HearingAidProfile.this.mDeviceManager.findDevice(remove);
                if (findDevice == null) {
                    if (HearingAidProfile.f242V) {
                        Log.d(HearingAidProfile.TAG, "HearingAidProfile found new device: " + remove);
                    }
                    findDevice = HearingAidProfile.this.mDeviceManager.addDevice(remove);
                }
                findDevice.onProfileStateChanged(HearingAidProfile.this, 2);
                findDevice.refresh();
            }
            HearingAidProfile.this.mDeviceManager.updateHearingAidsDevices();
            boolean unused2 = HearingAidProfile.this.mIsProfileReady = true;
            HearingAidProfile.this.mProfileManager.callServiceConnectedListeners();
        }

        public void onServiceDisconnected(int i) {
            boolean unused = HearingAidProfile.this.mIsProfileReady = false;
        }
    }

    public boolean isProfileReady() {
        return this.mIsProfileReady;
    }

    HearingAidProfile(Context context, CachedBluetoothDeviceManager cachedBluetoothDeviceManager, LocalBluetoothProfileManager localBluetoothProfileManager) {
        this.mContext = context;
        this.mDeviceManager = cachedBluetoothDeviceManager;
        this.mProfileManager = localBluetoothProfileManager;
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        this.mBluetoothAdapter = defaultAdapter;
        defaultAdapter.getProfileProxy(context, new HearingAidServiceListener(), 21);
    }

    public List<BluetoothDevice> getConnectedDevices() {
        return getDevicesByStates(new int[]{2, 1, 3});
    }

    public List<BluetoothDevice> getConnectableDevices() {
        return getDevicesByStates(new int[]{0, 2, 1, 3});
    }

    private List<BluetoothDevice> getDevicesByStates(int[] iArr) {
        BluetoothHearingAid bluetoothHearingAid = this.mService;
        if (bluetoothHearingAid == null) {
            return new ArrayList(0);
        }
        return bluetoothHearingAid.getDevicesMatchingConnectionStates(iArr);
    }

    public int getConnectionStatus(BluetoothDevice bluetoothDevice) {
        BluetoothHearingAid bluetoothHearingAid = this.mService;
        if (bluetoothHearingAid == null) {
            return 0;
        }
        return bluetoothHearingAid.getConnectionState(bluetoothDevice);
    }

    public boolean setActiveDevice(BluetoothDevice bluetoothDevice) {
        if (this.mBluetoothAdapter == null) {
            return false;
        }
        boolean isAudioModeOngoingCall = Utils.isAudioModeOngoingCall(this.mContext);
        if (bluetoothDevice == null) {
            return this.mBluetoothAdapter.removeActiveDevice(isAudioModeOngoingCall);
        }
        return this.mBluetoothAdapter.setActiveDevice(bluetoothDevice, isAudioModeOngoingCall ? 1 : 0);
    }

    public List<BluetoothDevice> getActiveDevices() {
        BluetoothAdapter bluetoothAdapter = this.mBluetoothAdapter;
        if (bluetoothAdapter == null) {
            return new ArrayList();
        }
        return bluetoothAdapter.getActiveDevices(21);
    }

    public boolean isEnabled(BluetoothDevice bluetoothDevice) {
        BluetoothHearingAid bluetoothHearingAid = this.mService;
        if (bluetoothHearingAid == null || bluetoothDevice == null || bluetoothHearingAid.getConnectionPolicy(bluetoothDevice) <= 0) {
            return false;
        }
        return true;
    }

    public int getConnectionPolicy(BluetoothDevice bluetoothDevice) {
        BluetoothHearingAid bluetoothHearingAid = this.mService;
        if (bluetoothHearingAid == null || bluetoothDevice == null) {
            return 0;
        }
        return bluetoothHearingAid.getConnectionPolicy(bluetoothDevice);
    }

    public boolean setEnabled(BluetoothDevice bluetoothDevice, boolean z) {
        BluetoothHearingAid bluetoothHearingAid = this.mService;
        if (bluetoothHearingAid == null || bluetoothDevice == null) {
            return false;
        }
        if (!z) {
            return bluetoothHearingAid.setConnectionPolicy(bluetoothDevice, 0);
        }
        if (bluetoothHearingAid.getConnectionPolicy(bluetoothDevice) < 100) {
            return this.mService.setConnectionPolicy(bluetoothDevice, 100);
        }
        return false;
    }

    public void setVolume(int i) {
        BluetoothHearingAid bluetoothHearingAid = this.mService;
        if (bluetoothHearingAid != null) {
            bluetoothHearingAid.setVolume(i);
        }
    }

    public long getHiSyncId(BluetoothDevice bluetoothDevice) {
        BluetoothHearingAid bluetoothHearingAid = this.mService;
        if (bluetoothHearingAid == null || bluetoothDevice == null) {
            return 0;
        }
        return bluetoothHearingAid.getHiSyncId(bluetoothDevice);
    }

    public int getDeviceSide(BluetoothDevice bluetoothDevice) {
        BluetoothHearingAid bluetoothHearingAid = this.mService;
        if (bluetoothHearingAid == null) {
            Log.w(TAG, "Proxy not attached to HearingAidService");
            return -1;
        }
        try {
            Method declaredMethod = bluetoothHearingAid.getClass().getDeclaredMethod("getDeviceSideInternal", BluetoothDevice.class);
            declaredMethod.setAccessible(true);
            return ((Integer) declaredMethod.invoke(this.mService, bluetoothDevice)).intValue();
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            Log.e(TAG, "fail to get getDeviceSideInternal\n" + e.toString() + "\n" + Log.getStackTraceString(new Throwable()));
            return -1;
        }
    }

    public int getDeviceMode(BluetoothDevice bluetoothDevice) {
        BluetoothHearingAid bluetoothHearingAid = this.mService;
        if (bluetoothHearingAid == null) {
            Log.w(TAG, "Proxy not attached to HearingAidService");
            return -1;
        }
        try {
            Method declaredMethod = bluetoothHearingAid.getClass().getDeclaredMethod("getDeviceModeInternal", BluetoothDevice.class);
            declaredMethod.setAccessible(true);
            return ((Integer) declaredMethod.invoke(this.mService, bluetoothDevice)).intValue();
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            Log.e(TAG, "fail to get getDeviceModeInternal\n" + e.toString() + "\n" + Log.getStackTraceString(new Throwable()));
            return -1;
        }
    }

    public int getNameResource(BluetoothDevice bluetoothDevice) {
        return C1757R.string.bluetooth_profile_hearing_aid;
    }

    public int getSummaryResourceForDevice(BluetoothDevice bluetoothDevice) {
        int connectionStatus = getConnectionStatus(bluetoothDevice);
        if (connectionStatus == 0) {
            return C1757R.string.bluetooth_hearing_aid_profile_summary_use_for;
        }
        if (connectionStatus != 2) {
            return BluetoothUtils.getConnectionStateSummary(connectionStatus);
        }
        return C1757R.string.bluetooth_hearing_aid_profile_summary_connected;
    }

    /* access modifiers changed from: protected */
    public void finalize() {
        Log.d(TAG, "finalize()");
        if (this.mService != null) {
            try {
                BluetoothAdapter.getDefaultAdapter().closeProfileProxy(21, this.mService);
                this.mService = null;
            } catch (Throwable th) {
                Log.w(TAG, "Error cleaning up Hearing Aid proxy", th);
            }
        }
    }
}
