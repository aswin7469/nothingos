package android.bluetooth.le;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothUuid;
import android.bluetooth.IBluetoothGatt;
import android.bluetooth.IBluetoothManager;
import android.bluetooth.le.AdvertisingSetParameters;
import android.bluetooth.le.IAdvertisingSetCallback;
import android.content.AttributionSource;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelUuid;
import android.os.RemoteException;
import android.util.Log;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
/* loaded from: classes.dex */
public final class BluetoothLeAdvertiser {
    private static final int FLAGS_FIELD_BYTES = 3;
    private static final int MANUFACTURER_SPECIFIC_DATA_LENGTH = 2;
    private static final int MAX_ADVERTISING_DATA_BYTES = 1650;
    private static final int MAX_LEGACY_ADVERTISING_DATA_BYTES = 31;
    private static final int OVERHEAD_BYTES_PER_FIELD = 2;
    private static final String TAG = "BluetoothLeAdvertiser";
    private final AttributionSource mAttributionSource;
    private final BluetoothAdapter mBluetoothAdapter;
    private final IBluetoothManager mBluetoothManager;
    private final Map<AdvertiseCallback, AdvertisingSetCallback> mLegacyAdvertisers = new HashMap();
    private final Map<AdvertisingSetCallback, IAdvertisingSetCallback> mCallbackWrappers = Collections.synchronizedMap(new HashMap());
    private final Map<Integer, AdvertisingSet> mAdvertisingSets = Collections.synchronizedMap(new HashMap());
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    public BluetoothLeAdvertiser(BluetoothAdapter bluetoothAdapter) {
        Objects.requireNonNull(bluetoothAdapter);
        BluetoothAdapter bluetoothAdapter2 = bluetoothAdapter;
        this.mBluetoothAdapter = bluetoothAdapter2;
        this.mBluetoothManager = bluetoothAdapter2.getBluetoothManager();
        this.mAttributionSource = bluetoothAdapter2.getAttributionSource();
    }

    public void startAdvertising(AdvertiseSettings settings, AdvertiseData advertiseData, AdvertiseCallback callback) {
        startAdvertising(settings, advertiseData, null, callback);
    }

    public void startAdvertising(AdvertiseSettings settings, AdvertiseData advertiseData, AdvertiseData scanResponse, AdvertiseCallback callback) {
        int duration;
        synchronized (this.mLegacyAdvertisers) {
            try {
                try {
                    BluetoothLeUtils.checkAdapterStateOn(this.mBluetoothAdapter);
                    if (callback == null) {
                        throw new IllegalArgumentException("callback cannot be null");
                    }
                    boolean isConnectable = settings.isConnectable();
                    try {
                        int i = 1;
                        if (totalBytes(advertiseData, isConnectable) <= 31 && totalBytes(scanResponse, false) <= 31) {
                            if (this.mLegacyAdvertisers.containsKey(callback)) {
                                postStartFailure(callback, 3);
                                return;
                            }
                            AdvertisingSetParameters.Builder parameters = new AdvertisingSetParameters.Builder();
                            parameters.setLegacyMode(true);
                            parameters.setConnectable(isConnectable);
                            parameters.setScannable(true);
                            if (settings.getMode() == 0) {
                                parameters.setInterval(1600);
                            } else if (settings.getMode() == 1) {
                                parameters.setInterval(400);
                            } else if (settings.getMode() == 2) {
                                parameters.setInterval(160);
                            }
                            if (settings.getTxPowerLevel() == 0) {
                                parameters.setTxPowerLevel(-21);
                            } else if (settings.getTxPowerLevel() == 1) {
                                parameters.setTxPowerLevel(-15);
                            } else if (settings.getTxPowerLevel() == 2) {
                                parameters.setTxPowerLevel(-7);
                            } else if (settings.getTxPowerLevel() == 3) {
                                parameters.setTxPowerLevel(1);
                            }
                            int timeoutMillis = settings.getTimeout();
                            if (timeoutMillis <= 0) {
                                duration = 0;
                            } else {
                                if (timeoutMillis >= 10) {
                                    i = timeoutMillis / 10;
                                }
                                int duration2 = i;
                                duration = duration2;
                            }
                            AdvertisingSetCallback wrapped = wrapOldCallback(callback, settings);
                            this.mLegacyAdvertisers.put(callback, wrapped);
                            startAdvertisingSet(parameters.build(), advertiseData, scanResponse, null, null, duration, 0, wrapped);
                            return;
                        }
                        postStartFailure(callback, 1);
                    } catch (Throwable th) {
                        th = th;
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                }
            } catch (Throwable th3) {
                th = th3;
            }
        }
    }

    AdvertisingSetCallback wrapOldCallback(final AdvertiseCallback callback, final AdvertiseSettings settings) {
        return new AdvertisingSetCallback() { // from class: android.bluetooth.le.BluetoothLeAdvertiser.1
            @Override // android.bluetooth.le.AdvertisingSetCallback
            public void onAdvertisingSetStarted(AdvertisingSet advertisingSet, int txPower, int status) {
                if (status != 0) {
                    BluetoothLeAdvertiser.this.postStartFailure(callback, status);
                } else {
                    BluetoothLeAdvertiser.this.postStartSuccess(callback, settings);
                }
            }

            @Override // android.bluetooth.le.AdvertisingSetCallback
            public void onAdvertisingEnabled(AdvertisingSet advertisingSet, boolean enabled, int status) {
                if (enabled) {
                    Log.e(BluetoothLeAdvertiser.TAG, "Legacy advertiser should be only disabled on timeout, but was enabled!");
                } else {
                    BluetoothLeAdvertiser.this.stopAdvertising(callback);
                }
            }
        };
    }

    public void stopAdvertising(AdvertiseCallback callback) {
        synchronized (this.mLegacyAdvertisers) {
            try {
                if (callback == null) {
                    throw new IllegalArgumentException("callback cannot be null");
                }
                AdvertisingSetCallback wrapper = this.mLegacyAdvertisers.get(callback);
                if (wrapper == null) {
                    return;
                }
                stopAdvertisingSet(wrapper);
                this.mLegacyAdvertisers.remove(callback);
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public void startAdvertisingSet(AdvertisingSetParameters parameters, AdvertiseData advertiseData, AdvertiseData scanResponse, PeriodicAdvertisingParameters periodicParameters, AdvertiseData periodicData, AdvertisingSetCallback callback) {
        startAdvertisingSet(parameters, advertiseData, scanResponse, periodicParameters, periodicData, 0, 0, callback, new Handler(Looper.getMainLooper()));
    }

    public void startAdvertisingSet(AdvertisingSetParameters parameters, AdvertiseData advertiseData, AdvertiseData scanResponse, PeriodicAdvertisingParameters periodicParameters, AdvertiseData periodicData, AdvertisingSetCallback callback, Handler handler) {
        startAdvertisingSet(parameters, advertiseData, scanResponse, periodicParameters, periodicData, 0, 0, callback, handler);
    }

    public void startAdvertisingSet(AdvertisingSetParameters parameters, AdvertiseData advertiseData, AdvertiseData scanResponse, PeriodicAdvertisingParameters periodicParameters, AdvertiseData periodicData, int duration, int maxExtendedAdvertisingEvents, AdvertisingSetCallback callback) {
        startAdvertisingSet(parameters, advertiseData, scanResponse, periodicParameters, periodicData, duration, maxExtendedAdvertisingEvents, callback, new Handler(Looper.getMainLooper()));
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x00ad, code lost:
        if (r26 > 255) goto L52;
     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x00af, code lost:
        if (r26 == 0) goto L19;
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x00b7, code lost:
        if (r19.mBluetoothAdapter.isLePeriodicAdvertisingSupported() == false) goto L16;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x00c1, code lost:
        throw new java.lang.IllegalArgumentException("Can't use maxExtendedAdvertisingEvents with controller that don't support LE Extended Advertising");
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x00c2, code lost:
        if (r25 < 0) goto L50;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x00c7, code lost:
        if (r25 > 65535) goto L50;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x00ca, code lost:
        r0 = r19.mBluetoothManager.getBluetoothGatt();
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x00d3, code lost:
        if (r0 != null) goto L29;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x00d5, code lost:
        android.util.Log.e(android.bluetooth.le.BluetoothLeAdvertiser.TAG, "Bluetooth GATT is null");
        postStartSetFailure(r28, r27, 4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x00dd, code lost:
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x00de, code lost:
        r5 = wrap(r27, r28);
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x00e8, code lost:
        if (r19.mCallbackWrappers.putIfAbsent(r27, r5) != null) goto L31;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x0127, code lost:
        throw new java.lang.IllegalArgumentException("callback instance already associated with advertising");
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x00ea, code lost:
        r0 = r19.mAttributionSource;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x00ec, code lost:
        r12 = android.bluetooth.le.BluetoothLeAdvertiser.TAG;
        r13 = r28;
        r15 = r27;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x0107, code lost:
        r0.startAdvertisingSet(r20, r21, r22, r23, r24, r25, r26, r5, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x010b, code lost:
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x010c, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x0116, code lost:
        android.util.Log.e(r12, "Failed to start advertising set - ", r0);
        postStartSetFailure(r13, r15, 4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x011f, code lost:
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x010e, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x010f, code lost:
        r12 = android.bluetooth.le.BluetoothLeAdvertiser.TAG;
        r13 = r28;
        r15 = r27;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x0128, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x0129, code lost:
        android.util.Log.e(android.bluetooth.le.BluetoothLeAdvertiser.TAG, "Failed to get Bluetooth GATT - ", r0);
        postStartSetFailure(r28, r27, 4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x0137, code lost:
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x0152, code lost:
        throw new java.lang.IllegalArgumentException("duration out of range: " + r25);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void startAdvertisingSet(AdvertisingSetParameters parameters, AdvertiseData advertiseData, AdvertiseData scanResponse, PeriodicAdvertisingParameters periodicParameters, AdvertiseData periodicData, int duration, int maxExtendedAdvertisingEvents, AdvertisingSetCallback callback, Handler handler) {
        BluetoothLeUtils.checkAdapterStateOn(this.mBluetoothAdapter);
        if (callback != null) {
            boolean isConnectable = parameters.isConnectable();
            if (parameters.isLegacy()) {
                if (totalBytes(advertiseData, isConnectable) > 31) {
                    throw new IllegalArgumentException("Legacy advertising data too big");
                }
                if (totalBytes(scanResponse, false) > 31) {
                    throw new IllegalArgumentException("Legacy scan response data too big");
                }
            } else {
                boolean supportCodedPhy = this.mBluetoothAdapter.isLeCodedPhySupported();
                boolean support2MPhy = this.mBluetoothAdapter.isLe2MPhySupported();
                int pphy = parameters.getPrimaryPhy();
                int sphy = parameters.getSecondaryPhy();
                if (pphy == 3 && !supportCodedPhy) {
                    throw new IllegalArgumentException("Unsupported primary PHY selected");
                }
                if ((sphy == 3 && !supportCodedPhy) || (sphy == 2 && !support2MPhy)) {
                    throw new IllegalArgumentException("Unsupported secondary PHY selected");
                }
                int maxData = this.mBluetoothAdapter.getLeMaximumAdvertisingDataLength();
                if (totalBytes(advertiseData, isConnectable) > maxData) {
                    throw new IllegalArgumentException("Advertising data too big");
                }
                if (totalBytes(scanResponse, false) > maxData) {
                    throw new IllegalArgumentException("Scan response data too big");
                }
                if (totalBytes(periodicData, false) > maxData) {
                    throw new IllegalArgumentException("Periodic advertising data too big");
                }
                boolean supportPeriodic = this.mBluetoothAdapter.isLePeriodicAdvertisingSupported();
                if (periodicParameters != null && !supportPeriodic) {
                    throw new IllegalArgumentException("Controller does not support LE Periodic Advertising");
                }
            }
            throw new IllegalArgumentException("maxExtendedAdvertisingEvents out of range: " + maxExtendedAdvertisingEvents);
        }
        throw new IllegalArgumentException("callback cannot be null");
    }

    public void stopAdvertisingSet(AdvertisingSetCallback callback) {
        if (callback == null) {
            throw new IllegalArgumentException("callback cannot be null");
        }
        IAdvertisingSetCallback wrapped = this.mCallbackWrappers.remove(callback);
        if (wrapped == null) {
            return;
        }
        try {
            IBluetoothGatt gatt = this.mBluetoothManager.getBluetoothGatt();
            gatt.stopAdvertisingSet(wrapped, this.mAttributionSource);
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to stop advertising - ", e);
        }
    }

    public void cleanup() {
        this.mLegacyAdvertisers.clear();
        this.mCallbackWrappers.clear();
        this.mAdvertisingSets.clear();
    }

    private int totalBytes(AdvertiseData data, boolean isFlagsIncluded) {
        int length;
        int size = 0;
        if (data == null) {
            return 0;
        }
        if (isFlagsIncluded) {
            size = 3;
        }
        if (data.getServiceUuids() != null) {
            int num16BitUuids = 0;
            int num32BitUuids = 0;
            int num128BitUuids = 0;
            for (ParcelUuid uuid : data.getServiceUuids()) {
                if (BluetoothUuid.is16BitUuid(uuid)) {
                    num16BitUuids++;
                } else if (BluetoothUuid.is32BitUuid(uuid)) {
                    num32BitUuids++;
                } else {
                    num128BitUuids++;
                }
            }
            if (num16BitUuids != 0) {
                size += (num16BitUuids * 2) + 2;
            }
            if (num32BitUuids != 0) {
                size += (num32BitUuids * 4) + 2;
            }
            if (num128BitUuids != 0) {
                size += (num128BitUuids * 16) + 2;
            }
        }
        if (data.getServiceSolicitationUuids() != null) {
            int num16BitUuids2 = 0;
            int num32BitUuids2 = 0;
            int num128BitUuids2 = 0;
            for (ParcelUuid uuid2 : data.getServiceSolicitationUuids()) {
                if (BluetoothUuid.is16BitUuid(uuid2)) {
                    num16BitUuids2++;
                } else if (BluetoothUuid.is32BitUuid(uuid2)) {
                    num32BitUuids2++;
                } else {
                    num128BitUuids2++;
                }
            }
            if (num16BitUuids2 != 0) {
                size += (num16BitUuids2 * 2) + 2;
            }
            if (num32BitUuids2 != 0) {
                size += (num32BitUuids2 * 4) + 2;
            }
            if (num128BitUuids2 != 0) {
                size += (num128BitUuids2 * 16) + 2;
            }
        }
        for (ParcelUuid uuid3 : data.getServiceData().keySet()) {
            int uuidLen = BluetoothUuid.uuidToBytes(uuid3).length;
            size += uuidLen + 2 + byteLength(data.getServiceData().get(uuid3));
        }
        for (int i = 0; i < data.getManufacturerSpecificData().size(); i++) {
            size += byteLength(data.getManufacturerSpecificData().valueAt(i)) + 4;
        }
        if (data.getIncludeTxPowerLevel()) {
            size += 3;
        }
        if (data.getIncludeDeviceName() && (length = this.mBluetoothAdapter.getNameLengthForAdvertise()) >= 0) {
            return size + length + 2;
        }
        return size;
    }

    private int byteLength(byte[] array) {
        if (array == null) {
            return 0;
        }
        return array.length;
    }

    IAdvertisingSetCallback wrap(final AdvertisingSetCallback callback, final Handler handler) {
        return new IAdvertisingSetCallback.Stub() { // from class: android.bluetooth.le.BluetoothLeAdvertiser.2
            @Override // android.bluetooth.le.IAdvertisingSetCallback
            public void onAdvertisingSetStarted(final int advertiserId, final int txPower, final int status) {
                handler.post(new Runnable() { // from class: android.bluetooth.le.BluetoothLeAdvertiser.2.1
                    @Override // java.lang.Runnable
                    public void run() {
                        if (status != 0) {
                            callback.onAdvertisingSetStarted(null, 0, status);
                            BluetoothLeAdvertiser.this.mCallbackWrappers.remove(callback);
                            return;
                        }
                        AdvertisingSet advertisingSet = new AdvertisingSet(advertiserId, BluetoothLeAdvertiser.this.mBluetoothManager, BluetoothLeAdvertiser.this.mAttributionSource);
                        BluetoothLeAdvertiser.this.mAdvertisingSets.put(Integer.valueOf(advertiserId), advertisingSet);
                        callback.onAdvertisingSetStarted(advertisingSet, txPower, status);
                    }
                });
            }

            @Override // android.bluetooth.le.IAdvertisingSetCallback
            public void onOwnAddressRead(final int advertiserId, final int addressType, final String address) {
                handler.post(new Runnable() { // from class: android.bluetooth.le.BluetoothLeAdvertiser.2.2
                    @Override // java.lang.Runnable
                    public void run() {
                        AdvertisingSet advertisingSet = (AdvertisingSet) BluetoothLeAdvertiser.this.mAdvertisingSets.get(Integer.valueOf(advertiserId));
                        callback.onOwnAddressRead(advertisingSet, addressType, address);
                    }
                });
            }

            @Override // android.bluetooth.le.IAdvertisingSetCallback
            public void onAdvertisingSetStopped(final int advertiserId) {
                handler.post(new Runnable() { // from class: android.bluetooth.le.BluetoothLeAdvertiser.2.3
                    @Override // java.lang.Runnable
                    public void run() {
                        AdvertisingSet advertisingSet = (AdvertisingSet) BluetoothLeAdvertiser.this.mAdvertisingSets.get(Integer.valueOf(advertiserId));
                        callback.onAdvertisingSetStopped(advertisingSet);
                        BluetoothLeAdvertiser.this.mAdvertisingSets.remove(Integer.valueOf(advertiserId));
                        BluetoothLeAdvertiser.this.mCallbackWrappers.remove(callback);
                    }
                });
            }

            @Override // android.bluetooth.le.IAdvertisingSetCallback
            public void onAdvertisingEnabled(final int advertiserId, final boolean enabled, final int status) {
                handler.post(new Runnable() { // from class: android.bluetooth.le.BluetoothLeAdvertiser.2.4
                    @Override // java.lang.Runnable
                    public void run() {
                        AdvertisingSet advertisingSet = (AdvertisingSet) BluetoothLeAdvertiser.this.mAdvertisingSets.get(Integer.valueOf(advertiserId));
                        callback.onAdvertisingEnabled(advertisingSet, enabled, status);
                    }
                });
            }

            @Override // android.bluetooth.le.IAdvertisingSetCallback
            public void onAdvertisingDataSet(final int advertiserId, final int status) {
                handler.post(new Runnable() { // from class: android.bluetooth.le.BluetoothLeAdvertiser.2.5
                    @Override // java.lang.Runnable
                    public void run() {
                        AdvertisingSet advertisingSet = (AdvertisingSet) BluetoothLeAdvertiser.this.mAdvertisingSets.get(Integer.valueOf(advertiserId));
                        callback.onAdvertisingDataSet(advertisingSet, status);
                    }
                });
            }

            @Override // android.bluetooth.le.IAdvertisingSetCallback
            public void onScanResponseDataSet(final int advertiserId, final int status) {
                handler.post(new Runnable() { // from class: android.bluetooth.le.BluetoothLeAdvertiser.2.6
                    @Override // java.lang.Runnable
                    public void run() {
                        AdvertisingSet advertisingSet = (AdvertisingSet) BluetoothLeAdvertiser.this.mAdvertisingSets.get(Integer.valueOf(advertiserId));
                        callback.onScanResponseDataSet(advertisingSet, status);
                    }
                });
            }

            @Override // android.bluetooth.le.IAdvertisingSetCallback
            public void onAdvertisingParametersUpdated(final int advertiserId, final int txPower, final int status) {
                handler.post(new Runnable() { // from class: android.bluetooth.le.BluetoothLeAdvertiser.2.7
                    @Override // java.lang.Runnable
                    public void run() {
                        AdvertisingSet advertisingSet = (AdvertisingSet) BluetoothLeAdvertiser.this.mAdvertisingSets.get(Integer.valueOf(advertiserId));
                        callback.onAdvertisingParametersUpdated(advertisingSet, txPower, status);
                    }
                });
            }

            @Override // android.bluetooth.le.IAdvertisingSetCallback
            public void onPeriodicAdvertisingParametersUpdated(final int advertiserId, final int status) {
                handler.post(new Runnable() { // from class: android.bluetooth.le.BluetoothLeAdvertiser.2.8
                    @Override // java.lang.Runnable
                    public void run() {
                        AdvertisingSet advertisingSet = (AdvertisingSet) BluetoothLeAdvertiser.this.mAdvertisingSets.get(Integer.valueOf(advertiserId));
                        callback.onPeriodicAdvertisingParametersUpdated(advertisingSet, status);
                    }
                });
            }

            @Override // android.bluetooth.le.IAdvertisingSetCallback
            public void onPeriodicAdvertisingDataSet(final int advertiserId, final int status) {
                handler.post(new Runnable() { // from class: android.bluetooth.le.BluetoothLeAdvertiser.2.9
                    @Override // java.lang.Runnable
                    public void run() {
                        AdvertisingSet advertisingSet = (AdvertisingSet) BluetoothLeAdvertiser.this.mAdvertisingSets.get(Integer.valueOf(advertiserId));
                        callback.onPeriodicAdvertisingDataSet(advertisingSet, status);
                    }
                });
            }

            @Override // android.bluetooth.le.IAdvertisingSetCallback
            public void onPeriodicAdvertisingEnabled(final int advertiserId, final boolean enable, final int status) {
                handler.post(new Runnable() { // from class: android.bluetooth.le.BluetoothLeAdvertiser.2.10
                    @Override // java.lang.Runnable
                    public void run() {
                        AdvertisingSet advertisingSet = (AdvertisingSet) BluetoothLeAdvertiser.this.mAdvertisingSets.get(Integer.valueOf(advertiserId));
                        callback.onPeriodicAdvertisingEnabled(advertisingSet, enable, status);
                    }
                });
            }
        };
    }

    private void postStartSetFailure(Handler handler, final AdvertisingSetCallback callback, final int error) {
        handler.post(new Runnable() { // from class: android.bluetooth.le.BluetoothLeAdvertiser.3
            @Override // java.lang.Runnable
            public void run() {
                callback.onAdvertisingSetStarted(null, 0, error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void postStartFailure(final AdvertiseCallback callback, final int error) {
        this.mHandler.post(new Runnable() { // from class: android.bluetooth.le.BluetoothLeAdvertiser.4
            @Override // java.lang.Runnable
            public void run() {
                callback.onStartFailure(error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void postStartSuccess(final AdvertiseCallback callback, final AdvertiseSettings settings) {
        this.mHandler.post(new Runnable() { // from class: android.bluetooth.le.BluetoothLeAdvertiser.5
            @Override // java.lang.Runnable
            public void run() {
                callback.onStartSuccess(settings);
            }
        });
    }
}
