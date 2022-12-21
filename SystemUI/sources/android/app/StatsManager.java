package android.app;

import android.annotation.SystemApi;
import android.content.Context;
import android.os.Binder;
import android.os.RemoteException;
import android.p000os.IPullAtomCallback;
import android.p000os.IPullAtomResultReceiver;
import android.p000os.IStatsManagerService;
import android.p000os.StatsFrameworkInitializer;
import android.util.AndroidException;
import android.util.Log;
import android.util.StatsEvent;
import android.util.StatsEventParcel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

@SystemApi
public final class StatsManager {
    public static final String ACTION_STATSD_STARTED = "android.app.action.STATSD_STARTED";
    private static final boolean DEBUG = false;
    public static final long DEFAULT_COOL_DOWN_MILLIS = 1000;
    public static final long DEFAULT_TIMEOUT_MILLIS = 2000;
    public static final String EXTRA_STATS_ACTIVE_CONFIG_KEYS = "android.app.extra.STATS_ACTIVE_CONFIG_KEYS";
    public static final String EXTRA_STATS_BROADCAST_SUBSCRIBER_COOKIES = "android.app.extra.STATS_BROADCAST_SUBSCRIBER_COOKIES";
    public static final String EXTRA_STATS_CONFIG_KEY = "android.app.extra.STATS_CONFIG_KEY";
    public static final String EXTRA_STATS_CONFIG_UID = "android.app.extra.STATS_CONFIG_UID";
    public static final String EXTRA_STATS_DIMENSIONS_VALUE = "android.app.extra.STATS_DIMENSIONS_VALUE";
    public static final String EXTRA_STATS_SUBSCRIPTION_ID = "android.app.extra.STATS_SUBSCRIPTION_ID";
    public static final String EXTRA_STATS_SUBSCRIPTION_RULE_ID = "android.app.extra.STATS_SUBSCRIPTION_RULE_ID";
    public static final int PULL_SKIP = 1;
    public static final int PULL_SUCCESS = 0;
    private static final String TAG = "StatsManager";
    private static final Object sLock = new Object();
    private final Context mContext;
    private IStatsManagerService mStatsManagerService;

    public interface StatsPullAtomCallback {
        int onPullAtom(int i, List<StatsEvent> list);
    }

    public StatsManager(Context context) {
        this.mContext = context;
    }

    public void addConfig(long j, byte[] bArr) throws StatsUnavailableException {
        synchronized (sLock) {
            try {
                getIStatsManagerServiceLocked().addConfiguration(j, bArr, this.mContext.getOpPackageName());
            } catch (RemoteException e) {
                Log.e(TAG, "Failed to connect to statsmanager when adding configuration");
                throw new StatsUnavailableException("could not connect", e);
            } catch (SecurityException e2) {
                throw new StatsUnavailableException(e2.getMessage(), e2);
            } catch (IllegalStateException e3) {
                Log.e(TAG, "Failed to addConfig in statsmanager");
                throw new StatsUnavailableException(e3.getMessage(), e3);
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    @Deprecated
    public boolean addConfiguration(long j, byte[] bArr) {
        try {
            addConfig(j, bArr);
            return true;
        } catch (StatsUnavailableException | IllegalArgumentException unused) {
            return false;
        }
    }

    public void removeConfig(long j) throws StatsUnavailableException {
        synchronized (sLock) {
            try {
                getIStatsManagerServiceLocked().removeConfiguration(j, this.mContext.getOpPackageName());
            } catch (RemoteException e) {
                Log.e(TAG, "Failed to connect to statsmanager when removing configuration");
                throw new StatsUnavailableException("could not connect", e);
            } catch (SecurityException e2) {
                throw new StatsUnavailableException(e2.getMessage(), e2);
            } catch (IllegalStateException e3) {
                Log.e(TAG, "Failed to removeConfig in statsmanager");
                throw new StatsUnavailableException(e3.getMessage(), e3);
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    @Deprecated
    public boolean removeConfiguration(long j) {
        try {
            removeConfig(j);
            return true;
        } catch (StatsUnavailableException unused) {
            return false;
        }
    }

    public void setBroadcastSubscriber(PendingIntent pendingIntent, long j, long j2) throws StatsUnavailableException {
        synchronized (sLock) {
            try {
                IStatsManagerService iStatsManagerServiceLocked = getIStatsManagerServiceLocked();
                if (pendingIntent != null) {
                    iStatsManagerServiceLocked.setBroadcastSubscriber(j, j2, pendingIntent, this.mContext.getOpPackageName());
                } else {
                    iStatsManagerServiceLocked.unsetBroadcastSubscriber(j, j2, this.mContext.getOpPackageName());
                }
            } catch (RemoteException e) {
                Log.e(TAG, "Failed to connect to statsmanager when adding broadcast subscriber", e);
                throw new StatsUnavailableException("could not connect", e);
            } catch (SecurityException e2) {
                throw new StatsUnavailableException(e2.getMessage(), e2);
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    @Deprecated
    public boolean setBroadcastSubscriber(long j, long j2, PendingIntent pendingIntent) {
        try {
            setBroadcastSubscriber(pendingIntent, j, j2);
            return true;
        } catch (StatsUnavailableException unused) {
            return false;
        }
    }

    public void setFetchReportsOperation(PendingIntent pendingIntent, long j) throws StatsUnavailableException {
        synchronized (sLock) {
            try {
                IStatsManagerService iStatsManagerServiceLocked = getIStatsManagerServiceLocked();
                if (pendingIntent == null) {
                    iStatsManagerServiceLocked.removeDataFetchOperation(j, this.mContext.getOpPackageName());
                } else {
                    iStatsManagerServiceLocked.setDataFetchOperation(j, pendingIntent, this.mContext.getOpPackageName());
                }
            } catch (RemoteException e) {
                Log.e(TAG, "Failed to connect to statsmanager when registering data listener.");
                throw new StatsUnavailableException("could not connect", e);
            } catch (SecurityException e2) {
                throw new StatsUnavailableException(e2.getMessage(), e2);
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public long[] setActiveConfigsChangedOperation(PendingIntent pendingIntent) throws StatsUnavailableException {
        synchronized (sLock) {
            try {
                IStatsManagerService iStatsManagerServiceLocked = getIStatsManagerServiceLocked();
                if (pendingIntent == null) {
                    iStatsManagerServiceLocked.removeActiveConfigsChangedOperation(this.mContext.getOpPackageName());
                    long[] jArr = new long[0];
                    return jArr;
                }
                long[] activeConfigsChangedOperation = iStatsManagerServiceLocked.setActiveConfigsChangedOperation(pendingIntent, this.mContext.getOpPackageName());
                return activeConfigsChangedOperation;
            } catch (RemoteException e) {
                Log.e(TAG, "Failed to connect to statsmanager when registering active configs listener.");
                throw new StatsUnavailableException("could not connect", e);
            } catch (SecurityException e2) {
                throw new StatsUnavailableException(e2.getMessage(), e2);
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    @Deprecated
    public boolean setDataFetchOperation(long j, PendingIntent pendingIntent) {
        try {
            setFetchReportsOperation(pendingIntent, j);
            return true;
        } catch (StatsUnavailableException unused) {
            return false;
        }
    }

    public byte[] getReports(long j) throws StatsUnavailableException {
        byte[] data;
        synchronized (sLock) {
            try {
                data = getIStatsManagerServiceLocked().getData(j, this.mContext.getOpPackageName());
            } catch (RemoteException e) {
                Log.e(TAG, "Failed to connect to statsmanager when getting data");
                throw new StatsUnavailableException("could not connect", e);
            } catch (SecurityException e2) {
                throw new StatsUnavailableException(e2.getMessage(), e2);
            } catch (IllegalStateException e3) {
                Log.e(TAG, "Failed to getReports in statsmanager");
                throw new StatsUnavailableException(e3.getMessage(), e3);
            } catch (Throwable th) {
                throw th;
            }
        }
        return data;
    }

    @Deprecated
    public byte[] getData(long j) {
        try {
            return getReports(j);
        } catch (StatsUnavailableException unused) {
            return null;
        }
    }

    public byte[] getStatsMetadata() throws StatsUnavailableException {
        byte[] metadata;
        synchronized (sLock) {
            try {
                metadata = getIStatsManagerServiceLocked().getMetadata(this.mContext.getOpPackageName());
            } catch (RemoteException e) {
                Log.e(TAG, "Failed to connect to statsmanager when getting metadata");
                throw new StatsUnavailableException("could not connect", e);
            } catch (SecurityException e2) {
                throw new StatsUnavailableException(e2.getMessage(), e2);
            } catch (IllegalStateException e3) {
                Log.e(TAG, "Failed to getStatsMetadata in statsmanager");
                throw new StatsUnavailableException(e3.getMessage(), e3);
            } catch (Throwable th) {
                throw th;
            }
        }
        return metadata;
    }

    @Deprecated
    public byte[] getMetadata() {
        try {
            return getStatsMetadata();
        } catch (StatsUnavailableException unused) {
            return null;
        }
    }

    public long[] getRegisteredExperimentIds() throws StatsUnavailableException {
        long[] registeredExperimentIds;
        synchronized (sLock) {
            try {
                registeredExperimentIds = getIStatsManagerServiceLocked().getRegisteredExperimentIds();
            } catch (RemoteException e) {
                throw new StatsUnavailableException("could not connect", e);
            } catch (SecurityException e2) {
                throw new StatsUnavailableException(e2.getMessage(), e2);
            } catch (IllegalStateException e3) {
                Log.e(TAG, "Failed to getRegisteredExperimentIds in statsmanager");
                throw new StatsUnavailableException(e3.getMessage(), e3);
            } catch (Throwable th) {
                throw th;
            }
        }
        return registeredExperimentIds;
    }

    public void setPullAtomCallback(int i, PullAtomMetadata pullAtomMetadata, Executor executor, StatsPullAtomCallback statsPullAtomCallback) {
        long j;
        int[] iArr;
        long r4 = pullAtomMetadata == null ? 1000 : pullAtomMetadata.mCoolDownMillis;
        if (pullAtomMetadata == null) {
            j = DEFAULT_TIMEOUT_MILLIS;
        } else {
            j = pullAtomMetadata.mTimeoutMillis;
        }
        long j2 = j;
        if (pullAtomMetadata == null) {
            iArr = new int[0];
        } else {
            iArr = pullAtomMetadata.mAdditiveFields;
        }
        if (iArr == null) {
            iArr = new int[0];
        }
        int[] iArr2 = iArr;
        synchronized (sLock) {
            try {
                getIStatsManagerServiceLocked().registerPullAtomCallback(i, r4, j2, iArr2, new PullAtomCallbackInternal(i, statsPullAtomCallback, executor));
            } catch (RemoteException e) {
                throw new RuntimeException("Unable to register pull callback", e);
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public void clearPullAtomCallback(int i) {
        synchronized (sLock) {
            try {
                getIStatsManagerServiceLocked().unregisterPullAtomCallback(i);
            } catch (RemoteException unused) {
                throw new RuntimeException("Unable to unregister pull atom callback");
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private static class PullAtomCallbackInternal extends IPullAtomCallback.Stub {
        public final int mAtomId;
        public final StatsPullAtomCallback mCallback;
        public final Executor mExecutor;

        PullAtomCallbackInternal(int i, StatsPullAtomCallback statsPullAtomCallback, Executor executor) {
            this.mAtomId = i;
            this.mCallback = statsPullAtomCallback;
            this.mExecutor = executor;
        }

        public void onPullAtom(int i, IPullAtomResultReceiver iPullAtomResultReceiver) {
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                this.mExecutor.execute(new StatsManager$PullAtomCallbackInternal$$ExternalSyntheticLambda0(this, i, iPullAtomResultReceiver));
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onPullAtom$0$android-app-StatsManager$PullAtomCallbackInternal */
        public /* synthetic */ void mo20x46f7e6de(int i, IPullAtomResultReceiver iPullAtomResultReceiver) {
            ArrayList arrayList = new ArrayList();
            boolean z = this.mCallback.onPullAtom(i, arrayList) == 0;
            StatsEventParcel[] statsEventParcelArr = new StatsEventParcel[arrayList.size()];
            for (int i2 = 0; i2 < arrayList.size(); i2++) {
                StatsEventParcel statsEventParcel = new StatsEventParcel();
                statsEventParcelArr[i2] = statsEventParcel;
                statsEventParcel.buffer = ((StatsEvent) arrayList.get(i2)).getBytes();
            }
            try {
                iPullAtomResultReceiver.pullFinished(i, z, statsEventParcelArr);
            } catch (RemoteException unused) {
                Log.w(StatsManager.TAG, "StatsPullResultReceiver failed for tag " + this.mAtomId + " due to TransactionTooLarge. Calling pullFinish with no data");
                try {
                    iPullAtomResultReceiver.pullFinished(i, false, new StatsEventParcel[0]);
                } catch (RemoteException unused2) {
                    Log.w(StatsManager.TAG, "StatsPullResultReceiver failed for tag " + this.mAtomId + " with empty payload");
                }
            }
        }
    }

    public static class PullAtomMetadata {
        /* access modifiers changed from: private */
        public final int[] mAdditiveFields;
        /* access modifiers changed from: private */
        public final long mCoolDownMillis;
        /* access modifiers changed from: private */
        public final long mTimeoutMillis;

        private PullAtomMetadata(long j, long j2, int[] iArr) {
            this.mCoolDownMillis = j;
            this.mTimeoutMillis = j2;
            this.mAdditiveFields = iArr;
        }

        public static class Builder {
            private int[] mAdditiveFields = null;
            private long mCoolDownMillis = 1000;
            private long mTimeoutMillis = StatsManager.DEFAULT_TIMEOUT_MILLIS;

            public Builder setCoolDownMillis(long j) {
                this.mCoolDownMillis = j;
                return this;
            }

            public Builder setTimeoutMillis(long j) {
                this.mTimeoutMillis = j;
                return this;
            }

            public Builder setAdditiveFields(int[] iArr) {
                this.mAdditiveFields = iArr;
                return this;
            }

            public PullAtomMetadata build() {
                return new PullAtomMetadata(this.mCoolDownMillis, this.mTimeoutMillis, this.mAdditiveFields);
            }
        }

        public long getCoolDownMillis() {
            return this.mCoolDownMillis;
        }

        public long getTimeoutMillis() {
            return this.mTimeoutMillis;
        }

        public int[] getAdditiveFields() {
            return this.mAdditiveFields;
        }
    }

    private IStatsManagerService getIStatsManagerServiceLocked() {
        IStatsManagerService iStatsManagerService = this.mStatsManagerService;
        if (iStatsManagerService != null) {
            return iStatsManagerService;
        }
        IStatsManagerService asInterface = IStatsManagerService.Stub.asInterface(StatsFrameworkInitializer.getStatsServiceManager().getStatsManagerServiceRegisterer().get());
        this.mStatsManagerService = asInterface;
        return asInterface;
    }

    public static class StatsUnavailableException extends AndroidException {
        public StatsUnavailableException(String str) {
            super("Failed to connect to statsd: " + str);
        }

        public StatsUnavailableException(String str, Throwable th) {
            super("Failed to connect to statsd: " + str, th);
        }
    }
}
