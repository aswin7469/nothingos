package android.app.usage;

import android.content.Context;
import android.net.INetworkStatsService;
import android.net.INetworkStatsSession;
import android.net.NetworkStats;
import android.net.NetworkStatsHistory;
import android.net.NetworkTemplate;
import android.net.connectivity.com.android.net.module.util.CollectionUtils;
import android.os.RemoteException;
import android.util.Log;
import dalvik.system.CloseGuard;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

public final class NetworkStats implements AutoCloseable {
    private static final String TAG = "NetworkStats";
    private final CloseGuard mCloseGuard;
    private final long mEndTimeStamp;
    private int mEnumerationIndex = 0;
    private NetworkStatsHistory mHistory = null;
    private NetworkStatsHistory.Entry mRecycledHistoryEntry = null;
    private NetworkStats.Entry mRecycledSummaryEntry = null;
    private INetworkStatsSession mSession;
    private final long mStartTimeStamp;
    private int mState = -1;
    private android.net.NetworkStats mSummary = null;
    private int mTag = 0;
    private NetworkTemplate mTemplate;
    private int mUidOrUidIndex;
    private int[] mUids;

    NetworkStats(Context context, NetworkTemplate networkTemplate, int i, long j, long j2, INetworkStatsService iNetworkStatsService) throws RemoteException, SecurityException {
        CloseGuard closeGuard = CloseGuard.get();
        this.mCloseGuard = closeGuard;
        this.mSession = iNetworkStatsService.openSessionForUsageStats(i, context.getOpPackageName());
        closeGuard.open("close");
        this.mTemplate = networkTemplate;
        this.mStartTimeStamp = j;
        this.mEndTimeStamp = j2;
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        try {
            CloseGuard closeGuard = this.mCloseGuard;
            if (closeGuard != null) {
                closeGuard.warnIfOpen();
            }
            close();
        } finally {
            super.finalize();
        }
    }

    public static class Bucket {
        public static final int DEFAULT_NETWORK_ALL = -1;
        public static final int DEFAULT_NETWORK_NO = 1;
        public static final int DEFAULT_NETWORK_YES = 2;
        public static final int METERED_ALL = -1;
        public static final int METERED_NO = 1;
        public static final int METERED_YES = 2;
        public static final int ROAMING_ALL = -1;
        public static final int ROAMING_NO = 1;
        public static final int ROAMING_YES = 2;
        public static final int STATE_ALL = -1;
        public static final int STATE_DEFAULT = 1;
        public static final int STATE_FOREGROUND = 2;
        public static final int TAG_NONE = 0;
        public static final int UID_ALL = -1;
        public static final int UID_REMOVED = -4;
        public static final int UID_TETHERING = -5;
        /* access modifiers changed from: private */
        public long mBeginTimeStamp;
        /* access modifiers changed from: private */
        public int mDefaultNetworkStatus;
        /* access modifiers changed from: private */
        public long mEndTimeStamp;
        /* access modifiers changed from: private */
        public int mMetered;
        /* access modifiers changed from: private */
        public int mRoaming;
        /* access modifiers changed from: private */
        public long mRxBytes;
        /* access modifiers changed from: private */
        public long mRxPackets;
        /* access modifiers changed from: private */
        public int mState;
        /* access modifiers changed from: private */
        public int mTag;
        /* access modifiers changed from: private */
        public long mTxBytes;
        /* access modifiers changed from: private */
        public long mTxPackets;
        /* access modifiers changed from: private */
        public int mUid;

        @Retention(RetentionPolicy.SOURCE)
        public @interface DefaultNetworkStatus {
        }

        @Retention(RetentionPolicy.SOURCE)
        public @interface Metered {
        }

        @Retention(RetentionPolicy.SOURCE)
        public @interface Roaming {
        }

        @Retention(RetentionPolicy.SOURCE)
        public @interface State {
        }

        /* access modifiers changed from: private */
        public static int convertDefaultNetworkStatus(int i) {
            int i2 = -1;
            if (i != -1) {
                i2 = 1;
                if (i != 0) {
                    return i != 1 ? 0 : 2;
                }
            }
            return i2;
        }

        /* access modifiers changed from: private */
        public static int convertMetered(int i) {
            int i2 = -1;
            if (i != -1) {
                i2 = 1;
                if (i != 0) {
                    return i != 1 ? 0 : 2;
                }
            }
            return i2;
        }

        /* access modifiers changed from: private */
        public static int convertRoaming(int i) {
            int i2 = -1;
            if (i != -1) {
                i2 = 1;
                if (i != 0) {
                    return i != 1 ? 0 : 2;
                }
            }
            return i2;
        }

        /* access modifiers changed from: private */
        public static int convertSet(int i) {
            if (i != -1) {
                return i != 2 ? 0 : 1;
            }
            return -1;
        }

        /* access modifiers changed from: private */
        public static int convertState(int i) {
            int i2 = -1;
            if (i != -1) {
                i2 = 1;
                if (i != 0) {
                    return i != 1 ? 0 : 2;
                }
            }
            return i2;
        }

        /* access modifiers changed from: private */
        public static int convertTag(int i) {
            if (i != 0) {
                return i;
            }
            return 0;
        }

        /* access modifiers changed from: private */
        public static int convertUid(int i) {
            int i2 = -5;
            if (i != -5) {
                i2 = -4;
                if (i != -4) {
                    return i;
                }
            }
            return i2;
        }

        public int getUid() {
            return this.mUid;
        }

        public int getTag() {
            return this.mTag;
        }

        public int getState() {
            return this.mState;
        }

        public int getMetered() {
            return this.mMetered;
        }

        public int getRoaming() {
            return this.mRoaming;
        }

        public int getDefaultNetworkStatus() {
            return this.mDefaultNetworkStatus;
        }

        public long getStartTimeStamp() {
            return this.mBeginTimeStamp;
        }

        public long getEndTimeStamp() {
            return this.mEndTimeStamp;
        }

        public long getRxBytes() {
            return this.mRxBytes;
        }

        public long getTxBytes() {
            return this.mTxBytes;
        }

        public long getRxPackets() {
            return this.mRxPackets;
        }

        public long getTxPackets() {
            return this.mTxPackets;
        }
    }

    public boolean getNextBucket(Bucket bucket) {
        if (this.mSummary != null) {
            return getNextSummaryBucket(bucket);
        }
        return getNextHistoryBucket(bucket);
    }

    public boolean hasNextBucket() {
        android.net.NetworkStats networkStats = this.mSummary;
        if (networkStats == null) {
            NetworkStatsHistory networkStatsHistory = this.mHistory;
            if (networkStatsHistory == null) {
                return false;
            }
            if (this.mEnumerationIndex < networkStatsHistory.size() || hasNextUid()) {
                return true;
            }
            return false;
        } else if (this.mEnumerationIndex < networkStats.size()) {
            return true;
        } else {
            return false;
        }
    }

    public void close() {
        INetworkStatsSession iNetworkStatsSession = this.mSession;
        if (iNetworkStatsSession != null) {
            try {
                iNetworkStatsSession.close();
            } catch (RemoteException e) {
                Log.w(TAG, e);
            }
        }
        this.mSession = null;
        CloseGuard closeGuard = this.mCloseGuard;
        if (closeGuard != null) {
            closeGuard.close();
        }
    }

    /* access modifiers changed from: package-private */
    public Bucket getDeviceSummaryForNetwork() throws RemoteException {
        android.net.NetworkStats deviceSummaryForNetwork = this.mSession.getDeviceSummaryForNetwork(this.mTemplate, this.mStartTimeStamp, this.mEndTimeStamp);
        this.mSummary = deviceSummaryForNetwork;
        this.mEnumerationIndex = deviceSummaryForNetwork.size();
        return getSummaryAggregate();
    }

    /* access modifiers changed from: package-private */
    public void startSummaryEnumeration() throws RemoteException {
        this.mSummary = this.mSession.getSummaryForAllUid(this.mTemplate, this.mStartTimeStamp, this.mEndTimeStamp, false);
        this.mEnumerationIndex = 0;
    }

    /* access modifiers changed from: package-private */
    public void startTaggedSummaryEnumeration() throws RemoteException {
        this.mSummary = this.mSession.getTaggedSummaryForAllUid(this.mTemplate, this.mStartTimeStamp, this.mEndTimeStamp);
        this.mEnumerationIndex = 0;
    }

    /* access modifiers changed from: package-private */
    public void startHistoryUidEnumeration(int i, int i2, int i3) {
        this.mHistory = null;
        try {
            this.mHistory = this.mSession.getHistoryIntervalForUid(this.mTemplate, i, Bucket.convertSet(i3), i2, -1, this.mStartTimeStamp, this.mEndTimeStamp);
            setSingleUidTagState(i, i2, i3);
        } catch (RemoteException e) {
            Log.w(TAG, e);
        }
        this.mEnumerationIndex = 0;
    }

    /* access modifiers changed from: package-private */
    public void startHistoryDeviceEnumeration() {
        try {
            this.mHistory = this.mSession.getHistoryIntervalForNetwork(this.mTemplate, -1, this.mStartTimeStamp, this.mEndTimeStamp);
        } catch (RemoteException e) {
            Log.w(TAG, e);
            this.mHistory = null;
        }
        this.mEnumerationIndex = 0;
    }

    /* access modifiers changed from: package-private */
    public void startUserUidEnumeration() throws RemoteException {
        int[] relevantUids = this.mSession.getRelevantUids();
        ArrayList arrayList = new ArrayList();
        for (int i : relevantUids) {
            try {
                NetworkStatsHistory historyIntervalForUid = this.mSession.getHistoryIntervalForUid(this.mTemplate, i, -1, 0, -1, this.mStartTimeStamp, this.mEndTimeStamp);
                if (historyIntervalForUid != null && historyIntervalForUid.size() > 0) {
                    arrayList.add(Integer.valueOf(i));
                }
            } catch (RemoteException e) {
                Log.w(TAG, "Error while getting history of uid " + i, e);
            }
        }
        this.mUids = CollectionUtils.toIntArray(arrayList);
        this.mUidOrUidIndex = -1;
        stepHistory();
    }

    private void stepHistory() {
        if (hasNextUid()) {
            stepUid();
            this.mHistory = null;
            try {
                this.mHistory = this.mSession.getHistoryIntervalForUid(this.mTemplate, getUid(), -1, 0, -1, this.mStartTimeStamp, this.mEndTimeStamp);
            } catch (RemoteException e) {
                Log.w(TAG, e);
            }
            this.mEnumerationIndex = 0;
        }
    }

    private void fillBucketFromSummaryEntry(Bucket bucket) {
        bucket.mUid = Bucket.convertUid(this.mRecycledSummaryEntry.uid);
        bucket.mTag = Bucket.convertTag(this.mRecycledSummaryEntry.tag);
        bucket.mState = Bucket.convertState(this.mRecycledSummaryEntry.set);
        bucket.mDefaultNetworkStatus = Bucket.convertDefaultNetworkStatus(this.mRecycledSummaryEntry.defaultNetwork);
        bucket.mMetered = Bucket.convertMetered(this.mRecycledSummaryEntry.metered);
        bucket.mRoaming = Bucket.convertRoaming(this.mRecycledSummaryEntry.roaming);
        bucket.mBeginTimeStamp = this.mStartTimeStamp;
        bucket.mEndTimeStamp = this.mEndTimeStamp;
        bucket.mRxBytes = this.mRecycledSummaryEntry.rxBytes;
        bucket.mRxPackets = this.mRecycledSummaryEntry.rxPackets;
        bucket.mTxBytes = this.mRecycledSummaryEntry.txBytes;
        bucket.mTxPackets = this.mRecycledSummaryEntry.txPackets;
    }

    private boolean getNextSummaryBucket(Bucket bucket) {
        if (bucket == null || this.mEnumerationIndex >= this.mSummary.size()) {
            return false;
        }
        android.net.NetworkStats networkStats = this.mSummary;
        int i = this.mEnumerationIndex;
        this.mEnumerationIndex = i + 1;
        this.mRecycledSummaryEntry = networkStats.getValues(i, this.mRecycledSummaryEntry);
        fillBucketFromSummaryEntry(bucket);
        return true;
    }

    /* access modifiers changed from: package-private */
    public Bucket getSummaryAggregate() {
        if (this.mSummary == null) {
            return null;
        }
        Bucket bucket = new Bucket();
        if (this.mRecycledSummaryEntry == null) {
            this.mRecycledSummaryEntry = new NetworkStats.Entry();
        }
        this.mSummary.getTotal(this.mRecycledSummaryEntry);
        fillBucketFromSummaryEntry(bucket);
        return bucket;
    }

    private boolean getNextHistoryBucket(Bucket bucket) {
        NetworkStatsHistory networkStatsHistory;
        if (bucket == null || (networkStatsHistory = this.mHistory) == null) {
            return false;
        }
        if (this.mEnumerationIndex < networkStatsHistory.size()) {
            NetworkStatsHistory networkStatsHistory2 = this.mHistory;
            int i = this.mEnumerationIndex;
            this.mEnumerationIndex = i + 1;
            this.mRecycledHistoryEntry = networkStatsHistory2.getValues(i, this.mRecycledHistoryEntry);
            bucket.mUid = Bucket.convertUid(getUid());
            bucket.mTag = Bucket.convertTag(this.mTag);
            bucket.mState = this.mState;
            bucket.mDefaultNetworkStatus = -1;
            bucket.mMetered = -1;
            bucket.mRoaming = -1;
            bucket.mBeginTimeStamp = this.mRecycledHistoryEntry.bucketStart;
            bucket.mEndTimeStamp = this.mRecycledHistoryEntry.bucketStart + this.mRecycledHistoryEntry.bucketDuration;
            bucket.mRxBytes = this.mRecycledHistoryEntry.rxBytes;
            bucket.mRxPackets = this.mRecycledHistoryEntry.rxPackets;
            bucket.mTxBytes = this.mRecycledHistoryEntry.txBytes;
            bucket.mTxPackets = this.mRecycledHistoryEntry.txPackets;
            return true;
        } else if (!hasNextUid()) {
            return false;
        } else {
            stepHistory();
            return getNextHistoryBucket(bucket);
        }
    }

    private boolean isUidEnumeration() {
        return this.mUids != null;
    }

    private boolean hasNextUid() {
        return isUidEnumeration() && this.mUidOrUidIndex + 1 < this.mUids.length;
    }

    private int getUid() {
        if (!isUidEnumeration()) {
            return this.mUidOrUidIndex;
        }
        int i = this.mUidOrUidIndex;
        if (i >= 0) {
            int[] iArr = this.mUids;
            if (i < iArr.length) {
                return iArr[i];
            }
        }
        throw new IndexOutOfBoundsException("Index=" + this.mUidOrUidIndex + " mUids.length=" + this.mUids.length);
    }

    private void setSingleUidTagState(int i, int i2, int i3) {
        this.mUidOrUidIndex = i;
        this.mTag = i2;
        this.mState = i3;
    }

    private void stepUid() {
        if (this.mUids != null) {
            this.mUidOrUidIndex++;
        }
    }
}
