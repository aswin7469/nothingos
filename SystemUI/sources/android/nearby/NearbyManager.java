package android.nearby;

import android.annotation.SystemApi;
import android.content.Context;
import android.nearby.FastPairDevice;
import android.nearby.IBroadcastListener;
import android.nearby.IScanListener;
import android.nearby.PresenceDevice;
import android.net.connectivity.com.android.internal.util.Preconditions;
import android.os.RemoteException;
import android.provider.Settings;
import android.util.Log;
import java.lang.ref.WeakReference;
import java.util.Objects;
import java.util.WeakHashMap;
import java.util.concurrent.Executor;

@SystemApi
public class NearbyManager {
    public static final String FAST_PAIR_SCAN_ENABLED = "fast_pair_scan_enabled";
    private static final String TAG = "NearbyManager";
    private static final WeakHashMap<BroadcastCallback, WeakReference<BroadcastListenerTransport>> sBroadcastListeners = new WeakHashMap<>();
    private static final WeakHashMap<ScanCallback, WeakReference<ScanListenerTransport>> sScanListeners = new WeakHashMap<>();
    private final Context mContext;
    private final INearbyManager mService;

    public @interface ScanStatus {
        public static final int ERROR = 2;
        public static final int SUCCESS = 1;
        public static final int UNKNOWN = 0;
    }

    NearbyManager(Context context, INearbyManager iNearbyManager) {
        Objects.requireNonNull(context);
        Objects.requireNonNull(iNearbyManager);
        this.mContext = context;
        this.mService = iNearbyManager;
    }

    /* access modifiers changed from: private */
    public static NearbyDevice toClientNearbyDevice(NearbyDeviceParcelable nearbyDeviceParcelable, int i) {
        PublicCredential publicCredential;
        if (i == 1) {
            return new FastPairDevice.Builder().setName(nearbyDeviceParcelable.getName()).addMedium(nearbyDeviceParcelable.getMedium()).setRssi(nearbyDeviceParcelable.getRssi()).setTxPower(nearbyDeviceParcelable.getTxPower()).setModelId(nearbyDeviceParcelable.getFastPairModelId()).setBluetoothAddress(nearbyDeviceParcelable.getBluetoothAddress()).setData(nearbyDeviceParcelable.getData()).build();
        }
        if (i != 2 || (publicCredential = nearbyDeviceParcelable.getPublicCredential()) == null) {
            return null;
        }
        byte[] salt = nearbyDeviceParcelable.getSalt();
        if (salt == null) {
            salt = new byte[0];
        }
        return new PresenceDevice.Builder(String.valueOf(publicCredential.hashCode()), salt, publicCredential.getSecretId(), publicCredential.getEncryptedMetadata()).setRssi(nearbyDeviceParcelable.getRssi()).addMedium(nearbyDeviceParcelable.getMedium()).build();
    }

    public int startScan(ScanRequest scanRequest, Executor executor, ScanCallback scanCallback) {
        Objects.requireNonNull(scanRequest, "scanRequest must not be null");
        Objects.requireNonNull(scanCallback, "scanCallback must not be null");
        Objects.requireNonNull(executor, "executor must not be null");
        try {
            WeakHashMap<ScanCallback, WeakReference<ScanListenerTransport>> weakHashMap = sScanListeners;
            synchronized (weakHashMap) {
                WeakReference weakReference = weakHashMap.get(scanCallback);
                ScanListenerTransport scanListenerTransport = weakReference != null ? (ScanListenerTransport) weakReference.get() : null;
                if (scanListenerTransport == null) {
                    scanListenerTransport = new ScanListenerTransport(scanRequest.getScanType(), scanCallback, executor);
                } else {
                    Preconditions.checkState(scanListenerTransport.isRegistered());
                    scanListenerTransport.setExecutor(executor);
                }
                int registerScanListener = this.mService.registerScanListener(scanRequest, scanListenerTransport, this.mContext.getPackageName(), this.mContext.getAttributionTag());
                if (registerScanListener != 1) {
                    return registerScanListener;
                }
                weakHashMap.put(scanCallback, new WeakReference(scanListenerTransport));
                return 1;
            }
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void stopScan(ScanCallback scanCallback) {
        Preconditions.checkArgument(scanCallback != null, "invalid null scanCallback");
        try {
            WeakHashMap<ScanCallback, WeakReference<ScanListenerTransport>> weakHashMap = sScanListeners;
            synchronized (weakHashMap) {
                WeakReference remove = weakHashMap.remove(scanCallback);
                ScanListenerTransport scanListenerTransport = remove != null ? (ScanListenerTransport) remove.get() : null;
                if (scanListenerTransport != null) {
                    scanListenerTransport.unregister();
                    this.mService.unregisterScanListener(scanListenerTransport, this.mContext.getPackageName(), this.mContext.getAttributionTag());
                } else {
                    Log.e(TAG, "Cannot stop scan with this callback because it is never registered.");
                }
            }
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void startBroadcast(BroadcastRequest broadcastRequest, Executor executor, BroadcastCallback broadcastCallback) {
        try {
            WeakHashMap<BroadcastCallback, WeakReference<BroadcastListenerTransport>> weakHashMap = sBroadcastListeners;
            synchronized (weakHashMap) {
                WeakReference weakReference = weakHashMap.get(broadcastCallback);
                BroadcastListenerTransport broadcastListenerTransport = weakReference != null ? (BroadcastListenerTransport) weakReference.get() : null;
                if (broadcastListenerTransport == null) {
                    broadcastListenerTransport = new BroadcastListenerTransport(broadcastCallback, executor);
                } else {
                    Preconditions.checkState(broadcastListenerTransport.isRegistered());
                    broadcastListenerTransport.setExecutor(executor);
                }
                this.mService.startBroadcast(new BroadcastRequestParcelable(broadcastRequest), broadcastListenerTransport, this.mContext.getPackageName(), this.mContext.getAttributionTag());
                weakHashMap.put(broadcastCallback, new WeakReference(broadcastListenerTransport));
            }
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void stopBroadcast(BroadcastCallback broadcastCallback) {
        try {
            WeakHashMap<BroadcastCallback, WeakReference<BroadcastListenerTransport>> weakHashMap = sBroadcastListeners;
            synchronized (weakHashMap) {
                WeakReference remove = weakHashMap.remove(broadcastCallback);
                BroadcastListenerTransport broadcastListenerTransport = remove != null ? (BroadcastListenerTransport) remove.get() : null;
                if (broadcastListenerTransport != null) {
                    broadcastListenerTransport.unregister();
                    this.mService.stopBroadcast(broadcastListenerTransport, this.mContext.getPackageName(), this.mContext.getAttributionTag());
                } else {
                    Log.e(TAG, "Cannot stop broadcast with this callback because it is never registered.");
                }
            }
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static boolean getFastPairScanEnabled(Context context) {
        if (Settings.Secure.getInt(context.getContentResolver(), FAST_PAIR_SCAN_ENABLED, 0) != 0) {
            return true;
        }
        return false;
    }

    public static void setFastPairScanEnabled(Context context, boolean z) {
        Settings.Secure.putInt(context.getContentResolver(), FAST_PAIR_SCAN_ENABLED, z ? 1 : 0);
    }

    private static class ScanListenerTransport extends IScanListener.Stub {
        private Executor mExecutor;
        private volatile ScanCallback mScanCallback;
        private int mScanType;

        ScanListenerTransport(int i, ScanCallback scanCallback, Executor executor) {
            Preconditions.checkArgument(scanCallback != null, "invalid null callback");
            boolean isValidScanType = ScanRequest.isValidScanType(i);
            Preconditions.checkState(isValidScanType, "invalid scan type : " + i + ", scan type must be one of ScanRequest#SCAN_TYPE_");
            this.mScanType = i;
            this.mScanCallback = scanCallback;
            this.mExecutor = executor;
        }

        /* access modifiers changed from: package-private */
        public void setExecutor(Executor executor) {
            Preconditions.checkArgument(executor != null, "invalid null executor");
            this.mExecutor = executor;
        }

        /* access modifiers changed from: package-private */
        public boolean isRegistered() {
            return this.mScanCallback != null;
        }

        /* access modifiers changed from: package-private */
        public void unregister() {
            this.mScanCallback = null;
        }

        public void onDiscovered(NearbyDeviceParcelable nearbyDeviceParcelable) throws RemoteException {
            this.mExecutor.execute(new NearbyManager$ScanListenerTransport$$ExternalSyntheticLambda1(this, nearbyDeviceParcelable));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onDiscovered$0$android-nearby-NearbyManager$ScanListenerTransport */
        public /* synthetic */ void mo1602xd0912bc6(NearbyDeviceParcelable nearbyDeviceParcelable) {
            if (this.mScanCallback != null) {
                this.mScanCallback.onDiscovered(NearbyManager.toClientNearbyDevice(nearbyDeviceParcelable, this.mScanType));
            }
        }

        public void onUpdated(NearbyDeviceParcelable nearbyDeviceParcelable) throws RemoteException {
            this.mExecutor.execute(new NearbyManager$ScanListenerTransport$$ExternalSyntheticLambda3(this, nearbyDeviceParcelable));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onUpdated$1$android-nearby-NearbyManager$ScanListenerTransport */
        public /* synthetic */ void mo1605x394bfb0a(NearbyDeviceParcelable nearbyDeviceParcelable) {
            if (this.mScanCallback != null) {
                this.mScanCallback.onUpdated(NearbyManager.toClientNearbyDevice(nearbyDeviceParcelable, this.mScanType));
            }
        }

        public void onLost(NearbyDeviceParcelable nearbyDeviceParcelable) throws RemoteException {
            this.mExecutor.execute(new NearbyManager$ScanListenerTransport$$ExternalSyntheticLambda0(this, nearbyDeviceParcelable));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onLost$2$android-nearby-NearbyManager$ScanListenerTransport */
        public /* synthetic */ void mo1604x23e44c28(NearbyDeviceParcelable nearbyDeviceParcelable) {
            if (this.mScanCallback != null) {
                this.mScanCallback.onLost(NearbyManager.toClientNearbyDevice(nearbyDeviceParcelable, this.mScanType));
            }
        }

        public void onError() {
            this.mExecutor.execute(new NearbyManager$ScanListenerTransport$$ExternalSyntheticLambda2(this));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onError$3$android-nearby-NearbyManager$ScanListenerTransport */
        public /* synthetic */ void mo1603x48ff439b() {
            if (this.mScanCallback != null) {
                Log.e(NearbyManager.TAG, "onError: There is an error in scan.");
            }
        }
    }

    private static class BroadcastListenerTransport extends IBroadcastListener.Stub {
        private volatile BroadcastCallback mBroadcastCallback;
        private Executor mExecutor;

        BroadcastListenerTransport(BroadcastCallback broadcastCallback, Executor executor) {
            this.mBroadcastCallback = broadcastCallback;
            this.mExecutor = executor;
        }

        /* access modifiers changed from: package-private */
        public void setExecutor(Executor executor) {
            Preconditions.checkArgument(executor != null, "invalid null executor");
            this.mExecutor = executor;
        }

        /* access modifiers changed from: package-private */
        public boolean isRegistered() {
            return this.mBroadcastCallback != null;
        }

        /* access modifiers changed from: package-private */
        public void unregister() {
            this.mBroadcastCallback = null;
        }

        public void onStatusChanged(int i) {
            this.mExecutor.execute(new C0010x90124cd9(this, i));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onStatusChanged$0$android-nearby-NearbyManager$BroadcastListenerTransport */
        public /* synthetic */ void mo1598x3db2cd08(int i) {
            if (this.mBroadcastCallback != null) {
                this.mBroadcastCallback.onStatusChanged(i);
            }
        }
    }
}
