package com.android.settingslib.bluetooth;

import android.bluetooth.BluetoothCodecStatus;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.UserHandle;
import android.util.Log;
import androidx.constraintlayout.widget.R$styleable;
import com.android.settingslib.R$string;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
/* loaded from: classes.dex */
public class BluetoothEventManager {
    private static final boolean DEBUG = Log.isLoggable("BluetoothEventManager", 3);
    private final Context mContext;
    private final CachedBluetoothDeviceManager mDeviceManager;
    private final LocalBluetoothAdapter mLocalAdapter;
    private final android.os.Handler mReceiverHandler;
    private final UserHandle mUserHandle;
    private final BroadcastReceiver mBroadcastReceiver = new BluetoothBroadcastReceiver();
    private final BroadcastReceiver mProfileBroadcastReceiver = new BluetoothBroadcastReceiver();
    private final Collection<BluetoothCallback> mCallbacks = new CopyOnWriteArrayList();
    private final String ACT_BROADCAST_SOURCE_INFO = "android.bluetooth.BroadcastAudioSAManager.action.BROADCAST_SOURCE_INFO";
    private final IntentFilter mAdapterIntentFilter = new IntentFilter();
    private final IntentFilter mProfileIntentFilter = new IntentFilter();
    private final Map<String, Handler> mHandlerMap = new HashMap();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface Handler {
        void onReceive(Context context, Intent intent, BluetoothDevice bluetoothDevice);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BluetoothEventManager(LocalBluetoothAdapter localBluetoothAdapter, CachedBluetoothDeviceManager cachedBluetoothDeviceManager, Context context, android.os.Handler handler, UserHandle userHandle) {
        Object obj;
        this.mLocalAdapter = localBluetoothAdapter;
        this.mDeviceManager = cachedBluetoothDeviceManager;
        this.mContext = context;
        this.mUserHandle = userHandle;
        this.mReceiverHandler = handler;
        addHandler("android.bluetooth.adapter.action.STATE_CHANGED", new AdapterStateChangedHandler());
        addHandler("android.bluetooth.adapter.action.CONNECTION_STATE_CHANGED", new ConnectionStateChangedHandler());
        addHandler("android.bluetooth.adapter.action.DISCOVERY_STARTED", new ScanningStateChangedHandler(true));
        addHandler("android.bluetooth.adapter.action.DISCOVERY_FINISHED", new ScanningStateChangedHandler(false));
        addHandler("android.bluetooth.device.action.FOUND", new DeviceFoundHandler());
        addHandler("android.bluetooth.device.action.NAME_CHANGED", new NameChangedHandler());
        addHandler("android.bluetooth.device.action.ALIAS_CHANGED", new NameChangedHandler());
        addHandler("android.bluetooth.device.action.BOND_STATE_CHANGED", new BondStateChangedHandler());
        addHandler("android.bluetooth.device.action.CLASS_CHANGED", new ClassChangedHandler());
        addHandler("android.bluetooth.device.action.UUID", new UuidChangedHandler());
        addHandler("android.bluetooth.device.action.BATTERY_LEVEL_CHANGED", new BatteryLevelChangedHandler());
        addHandler("android.bluetooth.headset.action.HF_TWSP_BATTERY_STATE_CHANGED", new TwspBatteryLevelChangedHandler());
        addHandler("android.bluetooth.a2dp.profile.action.ACTIVE_DEVICE_CHANGED", new ActiveDeviceChangedHandler());
        addHandler("android.bluetooth.headset.profile.action.ACTIVE_DEVICE_CHANGED", new ActiveDeviceChangedHandler());
        addHandler("android.bluetooth.hearingaid.profile.action.ACTIVE_DEVICE_CHANGED", new ActiveDeviceChangedHandler());
        addHandler("android.bluetooth.headset.profile.action.AUDIO_STATE_CHANGED", new AudioModeChangedHandler());
        addHandler("android.intent.action.PHONE_STATE", new AudioModeChangedHandler());
        addHandler("android.bluetooth.device.action.ACL_CONNECTED", new AclStateChangedHandler());
        addHandler("android.bluetooth.device.action.ACL_DISCONNECTED", new AclStateChangedHandler());
        addHandler("android.bluetooth.a2dp.profile.action.CODEC_CONFIG_CHANGED", new A2dpCodecConfigChangedHandler());
        try {
            int i = BroadcastSourceInfoHandler.$r8$clinit;
            obj = BroadcastSourceInfoHandler.class.getDeclaredConstructor(CachedBluetoothDeviceManager.class).newInstance(cachedBluetoothDeviceManager);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            obj = null;
        }
        if (obj != null) {
            Log.d("BluetoothEventManager", "adding SourceInfo Handler");
            addHandler("android.bluetooth.BroadcastAudioSAManager.action.BROADCAST_SOURCE_INFO", (Handler) obj);
        }
        addHandler("android.bluetooth.broadcast.profile.action.BROADCAST_STATE_CHANGED", new BroadcastStateChangedHandler());
        addHandler("android.bluetooth.broadcast.profile.action.BROADCAST_ENCRYPTION_KEY_GENERATED", new BroadcastKeyGeneratedHandler());
        addHandler("android.bluetooth.vcp.profile.action.CONNECTION_MODE_CHANGED", new VcpModeChangedHandler());
        addHandler("android.bluetooth.vcp.profile.action.VOLUME_CHANGED", new VcpVolumeChangedHandler());
        registerAdapterIntentReceiver();
    }

    public void registerCallback(BluetoothCallback bluetoothCallback) {
        this.mCallbacks.add(bluetoothCallback);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void registerProfileIntentReceiver() {
        registerIntentReceiver(this.mProfileBroadcastReceiver, this.mProfileIntentFilter);
    }

    void registerAdapterIntentReceiver() {
        registerIntentReceiver(this.mBroadcastReceiver, this.mAdapterIntentFilter);
    }

    private void registerIntentReceiver(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter) {
        UserHandle userHandle = this.mUserHandle;
        if (userHandle == null) {
            this.mContext.registerReceiver(broadcastReceiver, intentFilter, null, this.mReceiverHandler);
        } else {
            this.mContext.registerReceiverAsUser(broadcastReceiver, userHandle, intentFilter, null, this.mReceiverHandler);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addProfileHandler(String str, Handler handler) {
        this.mHandlerMap.put(str, handler);
        this.mProfileIntentFilter.addAction(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean readPairedDevices() {
        Set<BluetoothDevice> bondedDevices = this.mLocalAdapter.getBondedDevices();
        boolean z = false;
        if (bondedDevices == null) {
            return false;
        }
        for (BluetoothDevice bluetoothDevice : bondedDevices) {
            if (this.mDeviceManager.findDevice(bluetoothDevice) == null) {
                this.mDeviceManager.addDevice(bluetoothDevice);
                z = true;
            }
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dispatchDeviceAdded(CachedBluetoothDevice cachedBluetoothDevice) {
        for (BluetoothCallback bluetoothCallback : this.mCallbacks) {
            bluetoothCallback.onDeviceAdded(cachedBluetoothDevice);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dispatchDeviceRemoved(CachedBluetoothDevice cachedBluetoothDevice) {
        for (BluetoothCallback bluetoothCallback : this.mCallbacks) {
            bluetoothCallback.onDeviceDeleted(cachedBluetoothDevice);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dispatchProfileConnectionStateChanged(CachedBluetoothDevice cachedBluetoothDevice, int i, int i2) {
        for (BluetoothCallback bluetoothCallback : this.mCallbacks) {
            bluetoothCallback.onProfileConnectionStateChanged(cachedBluetoothDevice, i, i2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchConnectionStateChanged(CachedBluetoothDevice cachedBluetoothDevice, int i) {
        for (BluetoothCallback bluetoothCallback : this.mCallbacks) {
            bluetoothCallback.onConnectionStateChanged(cachedBluetoothDevice, i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchAudioModeChanged() {
        for (CachedBluetoothDevice cachedBluetoothDevice : this.mDeviceManager.getCachedDevicesCopy()) {
            cachedBluetoothDevice.onAudioModeChanged();
        }
        for (BluetoothCallback bluetoothCallback : this.mCallbacks) {
            bluetoothCallback.onAudioModeChanged();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchBroadcastStateChanged(int i) {
        for (BluetoothCallback bluetoothCallback : this.mCallbacks) {
            bluetoothCallback.onBroadcastStateChanged(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchBroadcastKeyGenerated() {
        for (BluetoothCallback bluetoothCallback : this.mCallbacks) {
            bluetoothCallback.onBroadcastKeyGenerated();
        }
    }

    void dispatchActiveDeviceChanged(CachedBluetoothDevice cachedBluetoothDevice, int i) {
        for (CachedBluetoothDevice cachedBluetoothDevice2 : this.mDeviceManager.getCachedDevicesCopy()) {
            cachedBluetoothDevice2.onActiveDeviceChanged(Objects.equals(cachedBluetoothDevice2, cachedBluetoothDevice), i);
        }
        for (BluetoothCallback bluetoothCallback : this.mCallbacks) {
            bluetoothCallback.onActiveDeviceChanged(cachedBluetoothDevice, i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchAclStateChanged(CachedBluetoothDevice cachedBluetoothDevice, int i) {
        for (BluetoothCallback bluetoothCallback : this.mCallbacks) {
            bluetoothCallback.onAclConnectionStateChanged(cachedBluetoothDevice, i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchA2dpCodecConfigChanged(CachedBluetoothDevice cachedBluetoothDevice, BluetoothCodecStatus bluetoothCodecStatus) {
        for (BluetoothCallback bluetoothCallback : this.mCallbacks) {
            bluetoothCallback.onA2dpCodecConfigChanged(cachedBluetoothDevice, bluetoothCodecStatus);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void dispatchNewGroupFound(CachedBluetoothDevice cachedBluetoothDevice, int i, UUID uuid) {
        synchronized (this.mCallbacks) {
            updateCacheDeviceInfo(i, cachedBluetoothDevice);
            for (BluetoothCallback bluetoothCallback : this.mCallbacks) {
                bluetoothCallback.onNewGroupFound(cachedBluetoothDevice, i, uuid);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void dispatchGroupDiscoveryStatusChanged(int i, int i2, int i3) {
        synchronized (this.mCallbacks) {
            for (BluetoothCallback bluetoothCallback : this.mCallbacks) {
                bluetoothCallback.onGroupDiscoveryStatusChanged(i, i2, i3);
            }
        }
    }

    void addHandler(String str, Handler handler) {
        this.mHandlerMap.put(str, handler);
        this.mAdapterIntentFilter.addAction(str);
    }

    /* loaded from: classes.dex */
    private class BluetoothBroadcastReceiver extends BroadcastReceiver {
        private BluetoothBroadcastReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            BluetoothDevice bluetoothDevice = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
            Handler handler = (Handler) BluetoothEventManager.this.mHandlerMap.get(action);
            if (handler != null) {
                handler.onReceive(context, intent, bluetoothDevice);
            }
        }
    }

    /* loaded from: classes.dex */
    private class AdapterStateChangedHandler implements Handler {
        private AdapterStateChangedHandler() {
        }

        @Override // com.android.settingslib.bluetooth.BluetoothEventManager.Handler
        public void onReceive(Context context, Intent intent, BluetoothDevice bluetoothDevice) {
            int intExtra = intent.getIntExtra("android.bluetooth.adapter.extra.STATE", Integer.MIN_VALUE);
            BluetoothEventManager.this.mLocalAdapter.setBluetoothStateInt(intExtra);
            for (BluetoothCallback bluetoothCallback : BluetoothEventManager.this.mCallbacks) {
                bluetoothCallback.onBluetoothStateChanged(intExtra);
            }
            BluetoothEventManager.this.mDeviceManager.onBluetoothStateChanged(intExtra);
        }
    }

    /* loaded from: classes.dex */
    private class ScanningStateChangedHandler implements Handler {
        private final boolean mStarted;

        ScanningStateChangedHandler(boolean z) {
            this.mStarted = z;
        }

        @Override // com.android.settingslib.bluetooth.BluetoothEventManager.Handler
        public void onReceive(Context context, Intent intent, BluetoothDevice bluetoothDevice) {
            for (BluetoothCallback bluetoothCallback : BluetoothEventManager.this.mCallbacks) {
                bluetoothCallback.onScanningStateChanged(this.mStarted);
            }
            BluetoothEventManager.this.mDeviceManager.onScanningStateChanged(this.mStarted);
        }
    }

    /* loaded from: classes.dex */
    private class DeviceFoundHandler implements Handler {
        private DeviceFoundHandler() {
        }

        @Override // com.android.settingslib.bluetooth.BluetoothEventManager.Handler
        public void onReceive(Context context, Intent intent, BluetoothDevice bluetoothDevice) {
            short shortExtra = intent.getShortExtra("android.bluetooth.device.extra.RSSI", Short.MIN_VALUE);
            intent.getStringExtra("android.bluetooth.device.extra.NAME");
            CachedBluetoothDevice findDevice = BluetoothEventManager.this.mDeviceManager.findDevice(bluetoothDevice);
            if (findDevice == null) {
                findDevice = BluetoothEventManager.this.mDeviceManager.addDevice(bluetoothDevice);
                Log.d("BluetoothEventManager", "DeviceFoundHandler created new CachedBluetoothDevice");
            } else if (findDevice.getBondState() == 12 && !findDevice.getDevice().isConnected()) {
                BluetoothEventManager.this.dispatchDeviceAdded(findDevice);
            }
            findDevice.setRssi(shortExtra);
            findDevice.setJustDiscovered(true);
        }
    }

    /* loaded from: classes.dex */
    private class ConnectionStateChangedHandler implements Handler {
        private ConnectionStateChangedHandler() {
        }

        @Override // com.android.settingslib.bluetooth.BluetoothEventManager.Handler
        public void onReceive(Context context, Intent intent, BluetoothDevice bluetoothDevice) {
            BluetoothEventManager.this.dispatchConnectionStateChanged(BluetoothEventManager.this.mDeviceManager.findDevice(bluetoothDevice), intent.getIntExtra("android.bluetooth.adapter.extra.CONNECTION_STATE", Integer.MIN_VALUE));
        }
    }

    /* loaded from: classes.dex */
    private class BroadcastStateChangedHandler implements Handler {
        private BroadcastStateChangedHandler() {
        }

        @Override // com.android.settingslib.bluetooth.BluetoothEventManager.Handler
        public void onReceive(Context context, Intent intent, BluetoothDevice bluetoothDevice) {
            BluetoothEventManager.this.dispatchBroadcastStateChanged(intent.getIntExtra("android.bluetooth.profile.extra.STATE", Integer.MIN_VALUE));
        }
    }

    /* loaded from: classes.dex */
    private class BroadcastKeyGeneratedHandler implements Handler {
        private BroadcastKeyGeneratedHandler() {
        }

        @Override // com.android.settingslib.bluetooth.BluetoothEventManager.Handler
        public void onReceive(Context context, Intent intent, BluetoothDevice bluetoothDevice) {
            BluetoothEventManager.this.dispatchBroadcastKeyGenerated();
        }
    }

    /* loaded from: classes.dex */
    private class NameChangedHandler implements Handler {
        private NameChangedHandler() {
        }

        @Override // com.android.settingslib.bluetooth.BluetoothEventManager.Handler
        public void onReceive(Context context, Intent intent, BluetoothDevice bluetoothDevice) {
            BluetoothEventManager.this.mDeviceManager.onDeviceNameUpdated(bluetoothDevice);
        }
    }

    /* loaded from: classes.dex */
    private class BondStateChangedHandler implements Handler {
        private BondStateChangedHandler() {
        }

        @Override // com.android.settingslib.bluetooth.BluetoothEventManager.Handler
        public void onReceive(Context context, Intent intent, BluetoothDevice bluetoothDevice) {
            if (bluetoothDevice == null) {
                Log.e("BluetoothEventManager", "ACTION_BOND_STATE_CHANGED with no EXTRA_DEVICE");
                return;
            }
            int intExtra = intent.getIntExtra("android.bluetooth.device.extra.BOND_STATE", Integer.MIN_VALUE);
            CachedBluetoothDevice findDevice = BluetoothEventManager.this.mDeviceManager.findDevice(bluetoothDevice);
            if (findDevice == null) {
                Log.w("BluetoothEventManager", "Got bonding state changed for " + bluetoothDevice + ", but we have no record of that device.");
                findDevice = BluetoothEventManager.this.mDeviceManager.addDevice(bluetoothDevice);
            }
            if (intExtra == 12) {
                int intExtra2 = intent.getIntExtra("android.bluetooth.qti.extra.GROUP_ID", Integer.MIN_VALUE);
                if (intExtra2 != Integer.MIN_VALUE && intExtra2 >= 0) {
                    BluetoothEventManager.this.updateCacheDeviceInfo(intExtra2, findDevice);
                } else if (intent.getBooleanExtra("android.bluetooth.qti.extra.IS_PRIVATE_ADDRESS", false)) {
                    Log.d("BluetoothEventManager", "Hide showing private address in UI  " + findDevice);
                    BluetoothEventManager.this.updateIgnoreDeviceFlag(findDevice);
                }
            }
            for (BluetoothCallback bluetoothCallback : BluetoothEventManager.this.mCallbacks) {
                bluetoothCallback.onDeviceBondStateChanged(findDevice, intExtra);
            }
            findDevice.onBondingStateChanged(intExtra);
            if (intExtra != 10) {
                return;
            }
            if (findDevice.getHiSyncId() != 0) {
                BluetoothEventManager.this.mDeviceManager.onDeviceUnpaired(findDevice);
            }
            showUnbondMessage(context, findDevice.getName(), intent.getIntExtra("android.bluetooth.device.extra.REASON", Integer.MIN_VALUE));
        }

        private void showUnbondMessage(Context context, String str, int i) {
            int i2;
            if (BluetoothEventManager.DEBUG) {
                Log.d("BluetoothEventManager", "showUnbondMessage() name : " + str + ", reason : " + i);
            }
            switch (i) {
                case 1:
                    i2 = R$string.bluetooth_pairing_pin_error_message;
                    break;
                case 2:
                    i2 = R$string.bluetooth_pairing_rejected_error_message;
                    break;
                case 3:
                default:
                    Log.w("BluetoothEventManager", "showUnbondMessage: Not displaying any message for reason: " + i);
                    return;
                case 4:
                    i2 = R$string.bluetooth_pairing_device_down_error_message;
                    break;
                case 5:
                case 6:
                case 7:
                case 8:
                    i2 = R$string.bluetooth_pairing_error_message;
                    break;
            }
            BluetoothUtils.showError(context, str, i2);
        }
    }

    /* loaded from: classes.dex */
    private class ClassChangedHandler implements Handler {
        private ClassChangedHandler() {
        }

        @Override // com.android.settingslib.bluetooth.BluetoothEventManager.Handler
        public void onReceive(Context context, Intent intent, BluetoothDevice bluetoothDevice) {
            CachedBluetoothDevice findDevice = BluetoothEventManager.this.mDeviceManager.findDevice(bluetoothDevice);
            if (findDevice != null) {
                findDevice.refresh();
            }
        }
    }

    /* loaded from: classes.dex */
    private class UuidChangedHandler implements Handler {
        private UuidChangedHandler() {
        }

        @Override // com.android.settingslib.bluetooth.BluetoothEventManager.Handler
        public void onReceive(Context context, Intent intent, BluetoothDevice bluetoothDevice) {
            CachedBluetoothDevice findDevice = BluetoothEventManager.this.mDeviceManager.findDevice(bluetoothDevice);
            if (findDevice != null) {
                findDevice.onUuidChanged();
            }
        }
    }

    /* loaded from: classes.dex */
    private class BatteryLevelChangedHandler implements Handler {
        private BatteryLevelChangedHandler() {
        }

        @Override // com.android.settingslib.bluetooth.BluetoothEventManager.Handler
        public void onReceive(Context context, Intent intent, BluetoothDevice bluetoothDevice) {
            CachedBluetoothDevice findDevice = BluetoothEventManager.this.mDeviceManager.findDevice(bluetoothDevice);
            if (findDevice != null) {
                findDevice.refresh();
            }
        }
    }

    /* loaded from: classes.dex */
    private class TwspBatteryLevelChangedHandler implements Handler {
        private TwspBatteryLevelChangedHandler() {
        }

        @Override // com.android.settingslib.bluetooth.BluetoothEventManager.Handler
        public void onReceive(Context context, Intent intent, BluetoothDevice bluetoothDevice) {
            CachedBluetoothDevice findDevice = BluetoothEventManager.this.mDeviceManager.findDevice(bluetoothDevice);
            if (findDevice != null) {
                findDevice.mTwspBatteryState = intent.getIntExtra("android.bluetooth.headset.extra.HF_TWSP_BATTERY_STATE", -1);
                findDevice.mTwspBatteryLevel = intent.getIntExtra("android.bluetooth.headset.extra.HF_TWSP_BATTERY_LEVEL", -1);
                Log.i("BluetoothEventManager", findDevice + ": mTwspBatteryState: " + findDevice.mTwspBatteryState + "mTwspBatteryLevel: " + findDevice.mTwspBatteryLevel);
                findDevice.refresh();
            }
        }
    }

    /* loaded from: classes.dex */
    private class ActiveDeviceChangedHandler implements Handler {
        private ActiveDeviceChangedHandler() {
        }

        @Override // com.android.settingslib.bluetooth.BluetoothEventManager.Handler
        public void onReceive(Context context, Intent intent, BluetoothDevice bluetoothDevice) {
            int i;
            String action = intent.getAction();
            if (action != null) {
                CachedBluetoothDevice findDevice = BluetoothEventManager.this.mDeviceManager.findDevice(bluetoothDevice);
                if (action.equals("android.bluetooth.a2dp.profile.action.ACTIVE_DEVICE_CHANGED")) {
                    i = 2;
                } else if (action.equals("android.bluetooth.headset.profile.action.ACTIVE_DEVICE_CHANGED")) {
                    i = 1;
                } else if (!action.equals("android.bluetooth.hearingaid.profile.action.ACTIVE_DEVICE_CHANGED")) {
                    Log.w("BluetoothEventManager", "ActiveDeviceChangedHandler: unknown action " + action);
                    return;
                } else {
                    i = 21;
                }
                BluetoothEventManager.this.dispatchActiveDeviceChanged(findDevice, i);
                return;
            }
            Log.w("BluetoothEventManager", "ActiveDeviceChangedHandler: action is null");
        }
    }

    /* loaded from: classes.dex */
    private class AclStateChangedHandler implements Handler {
        private AclStateChangedHandler() {
        }

        @Override // com.android.settingslib.bluetooth.BluetoothEventManager.Handler
        public void onReceive(Context context, Intent intent, BluetoothDevice bluetoothDevice) {
            int i;
            if (bluetoothDevice != null) {
                if (BluetoothEventManager.this.mDeviceManager.isSubDevice(bluetoothDevice)) {
                    return;
                }
                String action = intent.getAction();
                if (action != null) {
                    CachedBluetoothDevice findDevice = BluetoothEventManager.this.mDeviceManager.findDevice(bluetoothDevice);
                    if (findDevice == null) {
                        Log.w("BluetoothEventManager", "AclStateChangedHandler: activeDevice is null");
                        return;
                    }
                    if (action.equals("android.bluetooth.device.action.ACL_CONNECTED")) {
                        i = 2;
                    } else if (!action.equals("android.bluetooth.device.action.ACL_DISCONNECTED")) {
                        Log.w("BluetoothEventManager", "ActiveDeviceChangedHandler: unknown action " + action);
                        return;
                    } else {
                        i = 0;
                    }
                    BluetoothEventManager.this.dispatchAclStateChanged(findDevice, i);
                    return;
                }
                Log.w("BluetoothEventManager", "AclStateChangedHandler: action is null");
                return;
            }
            Log.w("BluetoothEventManager", "AclStateChangedHandler: device is null");
        }
    }

    /* loaded from: classes.dex */
    private class AudioModeChangedHandler implements Handler {
        private AudioModeChangedHandler() {
        }

        @Override // com.android.settingslib.bluetooth.BluetoothEventManager.Handler
        public void onReceive(Context context, Intent intent, BluetoothDevice bluetoothDevice) {
            if (intent.getAction() != null) {
                BluetoothEventManager.this.dispatchAudioModeChanged();
            } else {
                Log.w("BluetoothEventManager", "AudioModeChangedHandler() action is null");
            }
        }
    }

    /* loaded from: classes.dex */
    private class A2dpCodecConfigChangedHandler implements Handler {
        private A2dpCodecConfigChangedHandler() {
        }

        @Override // com.android.settingslib.bluetooth.BluetoothEventManager.Handler
        public void onReceive(Context context, Intent intent, BluetoothDevice bluetoothDevice) {
            if (intent.getAction() != null) {
                CachedBluetoothDevice findDevice = BluetoothEventManager.this.mDeviceManager.findDevice(bluetoothDevice);
                if (findDevice == null) {
                    Log.w("BluetoothEventManager", "A2dpCodecConfigChangedHandler: device is null");
                    return;
                }
                BluetoothCodecStatus parcelableExtra = intent.getParcelableExtra("android.bluetooth.extra.CODEC_STATUS");
                Log.d("BluetoothEventManager", "A2dpCodecConfigChangedHandler: device=" + bluetoothDevice + ", codecStatus=" + parcelableExtra);
                BluetoothEventManager.this.dispatchA2dpCodecConfigChanged(findDevice, parcelableExtra);
                return;
            }
            Log.w("BluetoothEventManager", "A2dpCodecConfigChangedHandler: action is null");
        }
    }

    /* loaded from: classes.dex */
    private class VcpModeChangedHandler implements Handler {
        private VcpModeChangedHandler() {
        }

        @Override // com.android.settingslib.bluetooth.BluetoothEventManager.Handler
        public void onReceive(Context context, Intent intent, BluetoothDevice bluetoothDevice) {
            CachedBluetoothDevice findDevice = BluetoothEventManager.this.mDeviceManager.findDevice(bluetoothDevice);
            int intExtra = intent.getIntExtra("android.bluetooth.vcp.profile.extra.MODE", 0);
            if (findDevice != null) {
                Log.i("BluetoothEventManager", findDevice + " Vcp connection mode change to " + intExtra);
                findDevice.refresh();
            }
        }
    }

    /* loaded from: classes.dex */
    private class VcpVolumeChangedHandler implements Handler {
        private VcpVolumeChangedHandler() {
        }

        @Override // com.android.settingslib.bluetooth.BluetoothEventManager.Handler
        public void onReceive(Context context, Intent intent, BluetoothDevice bluetoothDevice) {
            CachedBluetoothDevice findDevice = BluetoothEventManager.this.mDeviceManager.findDevice(bluetoothDevice);
            if (findDevice != null) {
                Log.i("BluetoothEventManager", findDevice + " Vcp volume change");
                findDevice.refresh();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateCacheDeviceInfo(int i, CachedBluetoothDevice cachedBluetoothDevice) {
        cachedBluetoothDevice.getDevice();
        boolean isGroupDevice = cachedBluetoothDevice.isGroupDevice();
        Log.d("BluetoothEventManager", "updateCacheDeviceInfo groupId " + i + ", cachedDevice :" + cachedBluetoothDevice + ", name :" + cachedBluetoothDevice.getName() + " isGroup :" + isGroupDevice + " groupId " + cachedBluetoothDevice.getGroupId());
        if (isGroupDevice) {
            if (i != cachedBluetoothDevice.getGroupId()) {
                Log.d("BluetoothEventManager", "groupId mismatch ignore" + cachedBluetoothDevice.getGroupId());
                return;
            }
            Log.d("BluetoothEventManager", "updateCacheDeviceInfo update ignored ");
            return;
        }
        cachedBluetoothDevice.setDeviceType(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateIgnoreDeviceFlag(CachedBluetoothDevice cachedBluetoothDevice) {
        cachedBluetoothDevice.setDeviceType(R$styleable.Constraint_layout_goneMarginRight);
    }
}
