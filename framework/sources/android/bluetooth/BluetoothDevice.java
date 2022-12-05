package android.bluetooth;

import android.annotation.SystemApi;
import android.app.PropertyInvalidatedCache;
import android.bluetooth.IBluetoothManagerCallback;
import android.content.Attributable;
import android.content.AttributionSource;
import android.content.Context;
import android.os.Handler;
import android.os.Parcel;
import android.os.ParcelUuid;
import android.os.Parcelable;
import android.os.Process;
import android.os.RemoteException;
import android.util.Log;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.UUID;
/* loaded from: classes.dex */
public final class BluetoothDevice implements Parcelable, Attributable {
    @SystemApi
    public static final int ACCESS_ALLOWED = 1;
    @SystemApi
    public static final int ACCESS_REJECTED = 2;
    @SystemApi
    public static final int ACCESS_UNKNOWN = 0;
    public static final String ACTION_ACL_CONNECTED = "android.bluetooth.device.action.ACL_CONNECTED";
    public static final String ACTION_ACL_DISCONNECTED = "android.bluetooth.device.action.ACL_DISCONNECTED";
    public static final String ACTION_ACL_DISCONNECT_REQUESTED = "android.bluetooth.device.action.ACL_DISCONNECT_REQUESTED";
    public static final String ACTION_ALIAS_CHANGED = "android.bluetooth.device.action.ALIAS_CHANGED";
    public static final String ACTION_BATTERY_LEVEL_CHANGED = "android.bluetooth.device.action.BATTERY_LEVEL_CHANGED";
    public static final String ACTION_BOND_STATE_CHANGED = "android.bluetooth.device.action.BOND_STATE_CHANGED";
    public static final String ACTION_CLASS_CHANGED = "android.bluetooth.device.action.CLASS_CHANGED";
    public static final String ACTION_CONNECTION_ACCESS_CANCEL = "android.bluetooth.device.action.CONNECTION_ACCESS_CANCEL";
    public static final String ACTION_CONNECTION_ACCESS_REPLY = "android.bluetooth.device.action.CONNECTION_ACCESS_REPLY";
    public static final String ACTION_CONNECTION_ACCESS_REQUEST = "android.bluetooth.device.action.CONNECTION_ACCESS_REQUEST";
    public static final String ACTION_FOUND = "android.bluetooth.device.action.FOUND";
    public static final String ACTION_MAS_INSTANCE = "android.bluetooth.device.action.MAS_INSTANCE";
    public static final String ACTION_NAME_CHANGED = "android.bluetooth.device.action.NAME_CHANGED";
    public static final String ACTION_NAME_FAILED = "android.bluetooth.device.action.NAME_FAILED";
    public static final String ACTION_PAIRING_CANCEL = "android.bluetooth.device.action.PAIRING_CANCEL";
    public static final String ACTION_PAIRING_REQUEST = "android.bluetooth.device.action.PAIRING_REQUEST";
    public static final String ACTION_REMOTE_ISSUE_OCCURRED = "org.codeaurora.intent.bluetooth.action.REMOTE_ISSUE_OCCURRED";
    public static final String ACTION_SDP_RECORD = "android.bluetooth.device.action.SDP_RECORD";
    @SystemApi
    public static final String ACTION_SILENCE_MODE_CHANGED = "android.bluetooth.device.action.SILENCE_MODE_CHANGED";
    public static final String ACTION_TWS_PLUS_DEVICE_PAIR = "android.bluetooth.device.action.TWS_PLUS_DEVICE_PAIR";
    public static final String ACTION_UUID = "android.bluetooth.device.action.UUID";
    public static final int ADDRESS_TYPE_PUBLIC = 0;
    public static final int ADDRESS_TYPE_RANDOM = 1;
    public static final int BATTERY_LEVEL_BLUETOOTH_OFF = -100;
    public static final int BATTERY_LEVEL_UNKNOWN = -1;
    private static final String BLUETOOTH_BONDING_CACHE_PROPERTY = "cache_key.bluetooth.get_bond_state";
    public static final int BOND_BONDED = 12;
    public static final int BOND_BONDING = 11;
    public static final int BOND_NONE = 10;
    public static final int BOND_SUCCESS = 0;
    public static final int CONNECTION_ACCESS_NO = 2;
    public static final int CONNECTION_ACCESS_YES = 1;
    private static final int CONNECTION_STATE_CONNECTED = 1;
    private static final int CONNECTION_STATE_DISCONNECTED = 0;
    private static final int CONNECTION_STATE_ENCRYPTED_BREDR = 2;
    private static final int CONNECTION_STATE_ENCRYPTED_LE = 4;
    private static final boolean DBG = false;
    public static final int DEVICE_TYPE_CLASSIC = 1;
    @SystemApi
    public static final String DEVICE_TYPE_DEFAULT = "Default";
    public static final int DEVICE_TYPE_DUAL = 3;
    public static final int DEVICE_TYPE_LE = 2;
    public static final int DEVICE_TYPE_UNKNOWN = 0;
    @SystemApi
    public static final String DEVICE_TYPE_UNTETHERED_HEADSET = "Untethered Headset";
    @SystemApi
    public static final String DEVICE_TYPE_WATCH = "Watch";
    public static final int ERROR = Integer.MIN_VALUE;
    public static final String EXTRA_ACCESS_REQUEST_TYPE = "android.bluetooth.device.extra.ACCESS_REQUEST_TYPE";
    public static final String EXTRA_ALWAYS_ALLOWED = "android.bluetooth.device.extra.ALWAYS_ALLOWED";
    public static final String EXTRA_BATTERY_LEVEL = "android.bluetooth.device.extra.BATTERY_LEVEL";
    public static final String EXTRA_BOND_STATE = "android.bluetooth.device.extra.BOND_STATE";
    public static final String EXTRA_BQR = "android.bluetooth.qti.extra.EXTRA_BQR";
    public static final String EXTRA_CLASS = "android.bluetooth.device.extra.CLASS";
    public static final String EXTRA_CLASS_NAME = "android.bluetooth.device.extra.CLASS_NAME";
    public static final String EXTRA_CONNECTION_ACCESS_RESULT = "android.bluetooth.device.extra.CONNECTION_ACCESS_RESULT";
    public static final String EXTRA_DEVICE = "android.bluetooth.device.extra.DEVICE";
    public static final String EXTRA_ERROR_CODE = "android.bluetooth.qti.extra.ERROR_CODE";
    public static final String EXTRA_ERROR_EVENT_MASK = "android.bluetooth.qti.extra.ERROR_EVENT_MASK";
    public static final String EXTRA_GLITCH_COUNT = "android.bluetooth.qti.extra.EXTRA_GLITCH_COUNT";
    public static final String EXTRA_GROUP_ID = "android.bluetooth.qti.extra.GROUP_ID";
    public static final String EXTRA_ISSUE_TYPE = "android.bluetooth.qti.extra.ERROR_TYPE";
    public static final String EXTRA_IS_PRIVATE_ADDRESS = "android.bluetooth.qti.extra.IS_PRIVATE_ADDRESS";
    public static final String EXTRA_LINK_QUALITY = "android.bluetooth.qti.extra.EXTRA_LINK_QUALITY";
    public static final String EXTRA_LMP_SUBVER = "android.bluetooth.qti.extra.EXTRA_LMP_SUBVER";
    public static final String EXTRA_LMP_VERSION = "android.bluetooth.qti.extra.EXTRA_LMP_VERSION";
    public static final String EXTRA_MANUFACTURER = "android.bluetooth.qti.extra.EXTRA_MANUFACTURER";
    public static final String EXTRA_MAS_INSTANCE = "android.bluetooth.device.extra.MAS_INSTANCE";
    public static final String EXTRA_NAME = "android.bluetooth.device.extra.NAME";
    public static final String EXTRA_PACKAGE_NAME = "android.bluetooth.device.extra.PACKAGE_NAME";
    public static final String EXTRA_PAIRING_INITIATOR = "android.bluetooth.device.extra.PAIRING_INITIATOR";
    public static final int EXTRA_PAIRING_INITIATOR_BACKGROUND = 2;
    public static final int EXTRA_PAIRING_INITIATOR_FOREGROUND = 1;
    public static final String EXTRA_PAIRING_KEY = "android.bluetooth.device.extra.PAIRING_KEY";
    public static final String EXTRA_PAIRING_VARIANT = "android.bluetooth.device.extra.PAIRING_VARIANT";
    public static final String EXTRA_POWER_LEVEL = "android.bluetooth.qti.extra.EXTRA_POWER_LEVEL";
    public static final String EXTRA_PREVIOUS_BOND_STATE = "android.bluetooth.device.extra.PREVIOUS_BOND_STATE";
    public static final String EXTRA_REASON = "android.bluetooth.device.extra.REASON";
    public static final String EXTRA_RSSI = "android.bluetooth.device.extra.RSSI";
    public static final String EXTRA_SDP_RECORD = "android.bluetooth.device.extra.SDP_RECORD";
    public static final String EXTRA_SDP_SEARCH_STATUS = "android.bluetooth.device.extra.SDP_SEARCH_STATUS";
    public static final String EXTRA_TWS_PLUS_DEVICE1 = "android.bluetooth.device.extra.EXTRA_TWS_PLUS_DEVICE1";
    public static final String EXTRA_TWS_PLUS_DEVICE2 = "android.bluetooth.device.extra.EXTRA_TWS_PLUS_DEVICE2";
    public static final String EXTRA_UUID = "android.bluetooth.device.extra.UUID";
    @SystemApi
    public static final int METADATA_COMPANION_APP = 4;
    @SystemApi
    public static final int METADATA_DEVICE_TYPE = 17;
    @SystemApi
    public static final int METADATA_ENHANCED_SETTINGS_UI_URI = 16;
    @SystemApi
    public static final int METADATA_HARDWARE_VERSION = 3;
    @SystemApi
    public static final int METADATA_IS_UNTETHERED_HEADSET = 6;
    @SystemApi
    public static final int METADATA_MAIN_BATTERY = 18;
    @SystemApi
    public static final int METADATA_MAIN_CHARGING = 19;
    @SystemApi
    public static final int METADATA_MAIN_ICON = 5;
    @SystemApi
    public static final int METADATA_MAIN_LOW_BATTERY_THRESHOLD = 20;
    @SystemApi
    public static final int METADATA_MANUFACTURER_NAME = 0;
    @SystemApi
    public static final int METADATA_MAX_LENGTH = 2048;
    @SystemApi
    public static final int METADATA_MODEL_NAME = 1;
    @SystemApi
    public static final int METADATA_SOFTWARE_VERSION = 2;
    @SystemApi
    public static final int METADATA_UNTETHERED_CASE_BATTERY = 12;
    @SystemApi
    public static final int METADATA_UNTETHERED_CASE_CHARGING = 15;
    @SystemApi
    public static final int METADATA_UNTETHERED_CASE_ICON = 9;
    @SystemApi
    public static final int METADATA_UNTETHERED_CASE_LOW_BATTERY_THRESHOLD = 23;
    @SystemApi
    public static final int METADATA_UNTETHERED_LEFT_BATTERY = 10;
    @SystemApi
    public static final int METADATA_UNTETHERED_LEFT_CHARGING = 13;
    @SystemApi
    public static final int METADATA_UNTETHERED_LEFT_ICON = 7;
    @SystemApi
    public static final int METADATA_UNTETHERED_LEFT_LOW_BATTERY_THRESHOLD = 21;
    @SystemApi
    public static final int METADATA_UNTETHERED_RIGHT_BATTERY = 11;
    @SystemApi
    public static final int METADATA_UNTETHERED_RIGHT_CHARGING = 14;
    @SystemApi
    public static final int METADATA_UNTETHERED_RIGHT_ICON = 8;
    @SystemApi
    public static final int METADATA_UNTETHERED_RIGHT_LOW_BATTERY_THRESHOLD = 22;
    public static final int PAIRING_VARIANT_CONSENT = 3;
    public static final int PAIRING_VARIANT_DISPLAY_PASSKEY = 4;
    public static final int PAIRING_VARIANT_DISPLAY_PIN = 5;
    public static final int PAIRING_VARIANT_OOB_CONSENT = 6;
    public static final int PAIRING_VARIANT_PASSKEY = 1;
    public static final int PAIRING_VARIANT_PASSKEY_CONFIRMATION = 2;
    public static final int PAIRING_VARIANT_PIN = 0;
    public static final int PAIRING_VARIANT_PIN_16_DIGITS = 7;
    public static final int PHY_LE_1M = 1;
    public static final int PHY_LE_1M_MASK = 1;
    public static final int PHY_LE_2M = 2;
    public static final int PHY_LE_2M_MASK = 2;
    public static final int PHY_LE_CODED = 3;
    public static final int PHY_LE_CODED_MASK = 4;
    public static final int PHY_OPTION_NO_PREFERRED = 0;
    public static final int PHY_OPTION_S2 = 1;
    public static final int PHY_OPTION_S8 = 2;
    public static final int REQUEST_TYPE_MESSAGE_ACCESS = 3;
    public static final int REQUEST_TYPE_PHONEBOOK_ACCESS = 2;
    public static final int REQUEST_TYPE_PROFILE_CONNECTION = 1;
    public static final int REQUEST_TYPE_SIM_ACCESS = 4;
    private static final String TAG = "BluetoothDevice";
    public static final int TRANSPORT_AUTO = 0;
    public static final int TRANSPORT_BREDR = 1;
    public static final int TRANSPORT_LE = 2;
    public static final int UNBOND_REASON_AUTH_CANCELED = 3;
    public static final int UNBOND_REASON_AUTH_FAILED = 1;
    public static final int UNBOND_REASON_AUTH_REJECTED = 2;
    public static final int UNBOND_REASON_AUTH_TIMEOUT = 6;
    public static final int UNBOND_REASON_DISCOVERY_IN_PROGRESS = 5;
    public static final int UNBOND_REASON_REMOTE_AUTH_CANCELED = 8;
    public static final int UNBOND_REASON_REMOTE_DEVICE_DOWN = 4;
    public static final int UNBOND_REASON_REMOVED = 9;
    public static final int UNBOND_REASON_REPEATED_ATTEMPTS = 7;
    private static volatile IBluetooth sService;
    private final String mAddress;
    private final int mAddressType;
    private AttributionSource mAttributionSource;
    private final PropertyInvalidatedCache<BluetoothDevice, Integer> mBluetoothBondCache = new PropertyInvalidatedCache<BluetoothDevice, Integer>(8, BLUETOOTH_BONDING_CACHE_PROPERTY) { // from class: android.bluetooth.BluetoothDevice.3
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.app.PropertyInvalidatedCache
        public Integer recompute(BluetoothDevice query) {
            try {
                return Integer.valueOf(BluetoothDevice.sService.getBondState(query, BluetoothDevice.this.mAttributionSource));
            } catch (RemoteException e) {
                throw e.rethrowAsRuntimeException();
            }
        }
    };
    static IBluetoothManagerCallback sStateChangeCallback = new IBluetoothManagerCallback.Stub() { // from class: android.bluetooth.BluetoothDevice.1
        @Override // android.bluetooth.IBluetoothManagerCallback
        public void onBluetoothServiceUp(IBluetooth bluetoothService) throws RemoteException {
            synchronized (BluetoothDevice.class) {
                if (BluetoothDevice.sService != null) {
                    Log.w(BluetoothDevice.TAG, "sService is not NULL");
                }
                IBluetooth unused = BluetoothDevice.sService = bluetoothService;
            }
        }

        @Override // android.bluetooth.IBluetoothManagerCallback
        public void onBluetoothServiceDown() throws RemoteException {
            synchronized (BluetoothDevice.class) {
                IBluetooth unused = BluetoothDevice.sService = null;
            }
        }

        @Override // android.bluetooth.IBluetoothManagerCallback
        public void onBrEdrDown() {
        }

        public void onOobData(int transport, OobData oobData) {
        }
    };
    public static final Parcelable.Creator<BluetoothDevice> CREATOR = new Parcelable.Creator<BluetoothDevice>() { // from class: android.bluetooth.BluetoothDevice.2
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public BluetoothDevice mo3559createFromParcel(Parcel in) {
            return new BluetoothDevice(in.readString());
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public BluetoothDevice[] mo3560newArray(int size) {
            return new BluetoothDevice[size];
        }
    };

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface AccessPermission {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface AddressType {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface MetadataKey {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface SetAliasReturnValues {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface Transport {
    }

    static IBluetooth getService() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        IBluetooth tService = adapter.getBluetoothService(sStateChangeCallback);
        synchronized (BluetoothDevice.class) {
            if (sService == null) {
                sService = tService;
            }
        }
        return sService;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BluetoothDevice(String address) {
        getService();
        if (!BluetoothAdapter.checkBluetoothAddress(address)) {
            throw new IllegalArgumentException(address + " is not a valid Bluetooth address");
        }
        this.mAddress = address;
        this.mAddressType = 0;
        this.mAttributionSource = BluetoothManager.resolveAttributionSource(null);
    }

    @Override // android.content.Attributable
    public void setAttributionSource(AttributionSource attributionSource) {
        this.mAttributionSource = attributionSource;
    }

    public void prepareToEnterProcess(AttributionSource attributionSource) {
        setAttributionSource(attributionSource);
    }

    public boolean equals(Object o) {
        if (o instanceof BluetoothDevice) {
            return this.mAddress.equals(((BluetoothDevice) o).getAddress());
        }
        return false;
    }

    public int hashCode() {
        return this.mAddress.hashCode();
    }

    public String toString() {
        return this.mAddress;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.mAddress);
    }

    public String getAddress() {
        return this.mAddress;
    }

    public String getAnonymizedAddress() {
        return "XX:XX:XX" + getAddress().substring(8);
    }

    public String getName() {
        IBluetooth service = sService;
        if (service == null) {
            Log.e(TAG, "BT not enabled. Cannot get Remote Device name");
            return null;
        }
        try {
            String name = service.getRemoteName(this, this.mAttributionSource);
            if (name == null) {
                return null;
            }
            return name.replace('\t', ' ').replace('\n', ' ').replace('\r', ' ');
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
            return null;
        }
    }

    public int getType() {
        IBluetooth service = sService;
        if (service == null) {
            Log.e(TAG, "BT not enabled. Cannot get Remote Device type");
            return 0;
        }
        try {
            return service.getRemoteType(this, this.mAttributionSource);
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
            return 0;
        }
    }

    public String getAlias() {
        IBluetooth service = sService;
        if (service == null) {
            Log.e(TAG, "BT not enabled. Cannot get Remote Device Alias");
            return null;
        }
        try {
            String alias = service.getRemoteAliasWithAttribution(this, this.mAttributionSource);
            if (alias == null) {
                return getName();
            }
            return alias.replace('\t', ' ').replace('\n', ' ').replace('\r', ' ');
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
            return null;
        }
    }

    public int setAlias(String alias) {
        if (alias != null && alias.isEmpty()) {
            throw new IllegalArgumentException("alias cannot be the empty string");
        }
        IBluetooth service = sService;
        if (service == null) {
            Log.e(TAG, "BT not enabled. Cannot set Remote Device name");
            return 1;
        }
        try {
            return service.setRemoteAlias(this, alias, this.mAttributionSource);
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
            throw e.rethrowFromSystemServer();
        }
    }

    public int getBatteryLevel() {
        IBluetooth service = sService;
        if (service == null) {
            Log.e(TAG, "Bluetooth disabled. Cannot get remote device battery level");
            return -100;
        }
        try {
            return service.getBatteryLevel(this, this.mAttributionSource);
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
            return -1;
        }
    }

    public boolean createBond() {
        return createBond(0);
    }

    public boolean createBond(int transport) {
        return createBondInternal(transport, null, null);
    }

    @SystemApi
    public boolean createBondOutOfBand(int transport, OobData remoteP192Data, OobData remoteP256Data) {
        if (remoteP192Data == null && remoteP256Data == null) {
            throw new IllegalArgumentException("One or both arguments for the OOB data types are required to not be null.  Please use createBond() instead if you do not have OOB data to pass.");
        }
        return createBondInternal(transport, remoteP192Data, remoteP256Data);
    }

    private boolean createBondInternal(int transport, OobData remoteP192Data, OobData remoteP256Data) {
        IBluetooth service = sService;
        if (service == null) {
            Log.w(TAG, "BT not enabled, createBondOutOfBand failed");
            return false;
        }
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter != null && (((transport == 2 || transport == 0) && !adapter.isLeEnabled()) || ((transport == 1 || transport == 0) && !isBluetoothEnabled()))) {
            Log.w(TAG, "creatBond() initiated in improper adapter state : " + adapter.getState() + " transport = " + transport);
            return false;
        }
        try {
            return service.createBond(this, transport, remoteP192Data, remoteP256Data, this.mAttributionSource);
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
            return false;
        }
    }

    public boolean isBondingInitiatedLocally() {
        IBluetooth service = sService;
        if (service == null) {
            Log.w(TAG, "BT not enabled, isBondingInitiatedLocally failed");
            return false;
        }
        try {
            return service.isBondingInitiatedLocally(this, this.mAttributionSource);
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
            return false;
        }
    }

    public void setBondingInitiatedLocally(boolean localInitiated) {
        IBluetooth service = sService;
        if (service == null) {
            Log.w(TAG, "BT not enabled, setBondingInitiatedLocally failed");
            return;
        }
        try {
            service.setBondingInitiatedLocally(this, localInitiated, this.mAttributionSource);
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
        }
    }

    @SystemApi
    public boolean cancelBondProcess() {
        IBluetooth service = sService;
        if (service == null) {
            Log.e(TAG, "BT not enabled. Cannot cancel Remote Device bond");
            return false;
        }
        try {
            Log.i(TAG, "cancelBondProcess() for device " + getAddress() + " called by pid: " + Process.myPid() + " tid: " + Process.myTid());
            return service.cancelBondProcess(this, this.mAttributionSource);
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
            return false;
        }
    }

    @SystemApi
    public boolean removeBond() {
        IBluetooth service = sService;
        if (service == null) {
            Log.e(TAG, "BT not enabled. Cannot remove Remote Device bond");
            return false;
        }
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter != null && !adapter.isLeEnabled()) {
            Log.w(TAG, "removeBond() initiated in improper adapter state : " + adapter.getState());
            return false;
        }
        try {
            Log.i(TAG, "removeBond() for device " + getAddress() + " called by pid: " + Process.myPid() + " tid: " + Process.myTid());
            return service.removeBond(this, this.mAttributionSource);
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
            return false;
        }
    }

    public void disableBluetoothGetBondStateCache() {
        this.mBluetoothBondCache.disableLocal();
    }

    public static void invalidateBluetoothGetBondStateCache() {
        PropertyInvalidatedCache.invalidateCache(BLUETOOTH_BONDING_CACHE_PROPERTY);
    }

    public int getBondState() {
        IBluetooth service = sService;
        if (service == null) {
            Log.e(TAG, "BT not enabled. Cannot get bond state");
            return 10;
        }
        try {
            return this.mBluetoothBondCache.query(this).intValue();
        } catch (RuntimeException e) {
            if (e.getCause() instanceof RemoteException) {
                Log.e(TAG, "", e);
                return 10;
            }
            throw e;
        }
    }

    @SystemApi
    public boolean canBondWithoutDialog() {
        IBluetooth service = sService;
        if (service == null) {
            Log.e(TAG, "BT not enabled. Cannot check if we can skip pairing dialog");
            return false;
        }
        try {
            return service.canBondWithoutDialog(this, this.mAttributionSource);
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
            return false;
        }
    }

    @SystemApi
    public boolean isConnected() {
        IBluetooth service = sService;
        if (service == null) {
            return false;
        }
        try {
            return service.getConnectionStateWithAttribution(this, this.mAttributionSource) != 0;
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
            return false;
        }
    }

    @SystemApi
    public boolean isEncrypted() {
        IBluetooth service = sService;
        if (service == null) {
            return false;
        }
        try {
            return service.getConnectionStateWithAttribution(this, this.mAttributionSource) > 1;
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
            return false;
        }
    }

    public BluetoothClass getBluetoothClass() {
        IBluetooth service = sService;
        if (service == null) {
            Log.e(TAG, "BT not enabled. Cannot get Bluetooth Class");
            return null;
        }
        try {
            int classInt = service.getRemoteClass(this, this.mAttributionSource);
            if (classInt != -16777216) {
                return new BluetoothClass(classInt);
            }
            return null;
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
            return null;
        }
    }

    public ParcelUuid[] getUuids() {
        IBluetooth service = sService;
        if (service == null || !isBluetoothEnabled()) {
            Log.e(TAG, "BT not enabled. Cannot get remote device Uuids");
            return null;
        }
        try {
            return service.getRemoteUuids(this, this.mAttributionSource);
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
            return null;
        }
    }

    public boolean fetchUuidsWithSdp() {
        IBluetooth service = sService;
        if (service == null || !isBluetoothEnabled()) {
            Log.e(TAG, "BT not enabled. Cannot fetchUuidsWithSdp");
            return false;
        }
        try {
            return service.fetchRemoteUuidsWithAttribution(this, this.mAttributionSource);
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
            return false;
        }
    }

    public boolean sdpSearch(ParcelUuid uuid) {
        IBluetooth service = sService;
        if (service == null) {
            Log.e(TAG, "BT not enabled. Cannot query remote device sdp records");
            return false;
        }
        try {
            return service.sdpSearch(this, uuid, this.mAttributionSource);
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
            return false;
        }
    }

    public boolean isTwsPlusDevice() {
        if (sService == null) {
            Log.e(TAG, "BT not enabled. Cannot query remote device sdp records");
            return false;
        }
        try {
            return sService.isTwsPlusDevice(this, this.mAttributionSource);
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
            return false;
        }
    }

    public String getTwsPlusPeerAddress() {
        if (sService == null) {
            Log.e(TAG, "BT not enabled. Cannot get Remote Device name");
            return null;
        }
        try {
            return sService.getTwsPlusPeerAddress(this, this.mAttributionSource);
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
            return null;
        }
    }

    public boolean setPin(byte[] pin) {
        IBluetooth service = sService;
        if (service == null) {
            Log.e(TAG, "BT not enabled. Cannot set Remote Device pin");
            return false;
        }
        try {
            return service.setPin(this, true, pin.length, pin, this.mAttributionSource);
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
            return false;
        }
    }

    public boolean setPin(String pin) {
        byte[] pinBytes = convertPinToBytes(pin);
        if (pinBytes == null) {
            return false;
        }
        return setPin(pinBytes);
    }

    public boolean setPairingConfirmation(boolean confirm) {
        IBluetooth service = sService;
        if (service == null) {
            Log.e(TAG, "BT not enabled. Cannot set pairing confirmation");
            return false;
        }
        try {
            return service.setPairingConfirmation(this, confirm, this.mAttributionSource);
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
            return false;
        }
    }

    public boolean cancelPairing() {
        IBluetooth service = sService;
        if (service == null) {
            Log.e(TAG, "BT not enabled. Cannot cancel pairing");
            return false;
        }
        try {
            return service.cancelBondProcess(this, this.mAttributionSource);
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
            return false;
        }
    }

    boolean isBluetoothEnabled() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null || !adapter.isEnabled()) {
            return false;
        }
        return true;
    }

    public int getPhonebookAccessPermission() {
        IBluetooth service = sService;
        if (service == null) {
            return 0;
        }
        try {
            return service.getPhonebookAccessPermission(this, this.mAttributionSource);
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
            return 0;
        }
    }

    @SystemApi
    public boolean setSilenceMode(boolean silence) {
        IBluetooth service = sService;
        if (service == null) {
            throw new IllegalStateException("Bluetooth is not turned ON");
        }
        try {
            return service.setSilenceMode(this, silence, this.mAttributionSource);
        } catch (RemoteException e) {
            Log.e(TAG, "setSilenceMode fail", e);
            return false;
        }
    }

    @SystemApi
    public boolean isInSilenceMode() {
        IBluetooth service = sService;
        if (service == null) {
            throw new IllegalStateException("Bluetooth is not turned ON");
        }
        try {
            return service.getSilenceMode(this, this.mAttributionSource);
        } catch (RemoteException e) {
            Log.e(TAG, "isInSilenceMode fail", e);
            return false;
        }
    }

    @SystemApi
    public boolean setPhonebookAccessPermission(int value) {
        IBluetooth service = sService;
        if (service == null) {
            return false;
        }
        try {
            return service.setPhonebookAccessPermission(this, value, this.mAttributionSource);
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
            return false;
        }
    }

    public int getMessageAccessPermission() {
        IBluetooth service = sService;
        if (service == null) {
            return 0;
        }
        try {
            return service.getMessageAccessPermission(this, this.mAttributionSource);
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
            return 0;
        }
    }

    @SystemApi
    public boolean setMessageAccessPermission(int value) {
        if (value != 1 && value != 2 && value != 0) {
            throw new IllegalArgumentException(value + "is not a valid AccessPermission value");
        }
        IBluetooth service = sService;
        if (service == null) {
            return false;
        }
        try {
            return service.setMessageAccessPermission(this, value, this.mAttributionSource);
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
            return false;
        }
    }

    @SystemApi
    public int getSimAccessPermission() {
        IBluetooth service = sService;
        if (service == null) {
            return 0;
        }
        try {
            return service.getSimAccessPermission(this, this.mAttributionSource);
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
            return 0;
        }
    }

    @SystemApi
    public boolean setSimAccessPermission(int value) {
        IBluetooth service = sService;
        if (service == null) {
            return false;
        }
        try {
            return service.setSimAccessPermission(this, value, this.mAttributionSource);
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
            return false;
        }
    }

    public BluetoothSocket createRfcommSocket(int channel) throws IOException {
        if (!isBluetoothEnabled()) {
            Log.e(TAG, "Bluetooth is not enabled");
            throw new IOException();
        }
        return new BluetoothSocket(1, -1, true, true, this, channel, null);
    }

    public BluetoothSocket createL2capSocket(int channel) throws IOException {
        return new BluetoothSocket(3, -1, true, true, this, channel, null);
    }

    public BluetoothSocket createInsecureL2capSocket(int channel) throws IOException {
        return new BluetoothSocket(3, -1, false, false, this, channel, null);
    }

    public BluetoothSocket createRfcommSocketToServiceRecord(UUID uuid) throws IOException {
        if (!isBluetoothEnabled()) {
            Log.e(TAG, "Bluetooth is not enabled");
            throw new IOException();
        }
        return new BluetoothSocket(1, -1, true, true, this, -1, new ParcelUuid(uuid));
    }

    public BluetoothSocket createInsecureRfcommSocketToServiceRecord(UUID uuid) throws IOException {
        if (!isBluetoothEnabled()) {
            Log.e(TAG, "Bluetooth is not enabled");
            throw new IOException();
        }
        return new BluetoothSocket(1, -1, false, false, this, -1, new ParcelUuid(uuid));
    }

    public BluetoothSocket createInsecureRfcommSocket(int port) throws IOException {
        if (!isBluetoothEnabled()) {
            Log.e(TAG, "Bluetooth is not enabled");
            throw new IOException();
        }
        return new BluetoothSocket(1, -1, false, false, this, port, null);
    }

    public BluetoothSocket createScoSocket() throws IOException {
        if (!isBluetoothEnabled()) {
            Log.e(TAG, "Bluetooth is not enabled");
            throw new IOException();
        }
        return new BluetoothSocket(2, -1, true, true, this, -1, null);
    }

    public static byte[] convertPinToBytes(String pin) {
        if (pin == null) {
            return null;
        }
        try {
            byte[] pinBytes = pin.getBytes("UTF-8");
            if (pinBytes.length <= 0 || pinBytes.length > 16) {
                return null;
            }
            return pinBytes;
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "UTF-8 not supported?!?");
            return null;
        }
    }

    public BluetoothGatt connectGatt(Context context, boolean autoConnect, BluetoothGattCallback callback) {
        return connectGatt(context, autoConnect, callback, 0);
    }

    public BluetoothGatt connectGatt(Context context, boolean autoConnect, BluetoothGattCallback callback, int transport) {
        return connectGatt(context, autoConnect, callback, transport, 1);
    }

    public BluetoothGatt connectGatt(Context context, boolean autoConnect, BluetoothGattCallback callback, int transport, int phy) {
        return connectGatt(context, autoConnect, callback, transport, phy, null);
    }

    public BluetoothGatt connectGatt(Context context, boolean autoConnect, BluetoothGattCallback callback, int transport, int phy, Handler handler) {
        return connectGatt(context, autoConnect, callback, transport, false, phy, handler);
    }

    public BluetoothGatt connectGatt(Context context, boolean autoConnect, BluetoothGattCallback callback, int transport, boolean opportunistic, int phy, Handler handler) {
        return connectGatt(context, autoConnect, callback, transport, opportunistic, phy, handler, false);
    }

    public BluetoothGatt connectGatt(Context context, boolean autoConnect, BluetoothGattCallback callback, int transport, boolean opportunistic, int phy, Handler handler, boolean eattSupport) {
        if (callback != null) {
            BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
            IBluetoothManager managerService = adapter.getBluetoothManager();
            try {
                IBluetoothGatt iGatt = managerService.getBluetoothGatt();
                if (iGatt == null) {
                    return null;
                }
                try {
                    BluetoothGatt gatt = new BluetoothGatt(iGatt, this, transport, opportunistic, phy, this.mAttributionSource);
                    try {
                        gatt.connect(Boolean.valueOf(autoConnect), callback, handler, eattSupport);
                        return gatt;
                    } catch (RemoteException e) {
                        e = e;
                        Log.e(TAG, "", e);
                        return null;
                    }
                } catch (RemoteException e2) {
                    e = e2;
                    Log.e(TAG, "", e);
                    return null;
                }
            } catch (RemoteException e3) {
                e = e3;
            }
        } else {
            throw new NullPointerException("callback is null");
        }
    }

    public BluetoothSocket createL2capChannel(int psm) throws IOException {
        if (!isBluetoothEnabled()) {
            Log.e(TAG, "createL2capChannel: Bluetooth is not enabled");
            throw new IOException();
        }
        return new BluetoothSocket(4, -1, true, true, this, psm, null);
    }

    public BluetoothSocket createInsecureL2capChannel(int psm) throws IOException {
        if (!isBluetoothEnabled()) {
            Log.e(TAG, "createInsecureL2capChannel: Bluetooth is not enabled");
            throw new IOException();
        }
        return new BluetoothSocket(4, -1, false, false, this, psm, null);
    }

    @SystemApi
    public boolean setMetadata(int key, byte[] value) {
        IBluetooth service = sService;
        if (service == null) {
            Log.e(TAG, "Bluetooth is not enabled. Cannot set metadata");
            return false;
        } else if (value.length > 2048) {
            throw new IllegalArgumentException("value length is " + value.length + ", should not over 2048");
        } else {
            try {
                return service.setMetadata(this, key, value, this.mAttributionSource);
            } catch (RemoteException e) {
                Log.e(TAG, "setMetadata fail", e);
                return false;
            }
        }
    }

    @SystemApi
    public byte[] getMetadata(int key) {
        IBluetooth service = sService;
        if (service == null) {
            Log.e(TAG, "Bluetooth is not enabled. Cannot get metadata");
            return null;
        }
        try {
            return service.getMetadata(this, key, this.mAttributionSource);
        } catch (RemoteException e) {
            Log.e(TAG, "getMetadata fail", e);
            return null;
        }
    }

    public static int getMaxMetadataKey() {
        return 23;
    }

    public int getDeviceType() {
        if (sService == null) {
            Log.e(TAG, "getDeviceType query remote device info failed");
            return -1;
        }
        try {
            return sService.getDeviceType(this, this.mAttributionSource);
        } catch (RemoteException e) {
            Log.e(TAG, "getDeviceType fail ", e);
            return -1;
        }
    }
}
