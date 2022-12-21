package android.net.wifi;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

@SystemApi
public final class WifiUsabilityStatsEntry implements Parcelable {
    public static final Parcelable.Creator<WifiUsabilityStatsEntry> CREATOR = new Parcelable.Creator<WifiUsabilityStatsEntry>() {
        public WifiUsabilityStatsEntry createFromParcel(Parcel parcel) {
            Parcel parcel2 = parcel;
            return new WifiUsabilityStatsEntry(parcel.readLong(), parcel.readInt(), parcel.readInt(), parcel.readLong(), parcel.readLong(), parcel.readLong(), parcel.readLong(), parcel.readLong(), parcel.readLong(), parcel.readLong(), parcel.readLong(), parcel.readLong(), parcel.readLong(), parcel.readLong(), parcel.readLong(), parcel.readLong(), parcel.readLong(), parcel.readLong(), parcel.readLong(), parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readInt(), (ContentionTimeStats[]) parcel2.createTypedArray(ContentionTimeStats.CREATOR), (RateStats[]) parcel2.createTypedArray(RateStats.CREATOR), (RadioStats[]) parcel2.createTypedArray(RadioStats.CREATOR), parcel.readInt(), parcel.readBoolean(), parcel.readBoolean(), parcel.readBoolean(), parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readBoolean());
        }

        public WifiUsabilityStatsEntry[] newArray(int i) {
            return new WifiUsabilityStatsEntry[i];
        }
    };
    public static final int NUM_WME_ACCESS_CATEGORIES = 4;
    public static final int PROBE_STATUS_FAILURE = 3;
    public static final int PROBE_STATUS_NO_PROBE = 1;
    public static final int PROBE_STATUS_SUCCESS = 2;
    public static final int PROBE_STATUS_UNKNOWN = 0;
    private static final String TAG = "WifiUsabilityStatsEntry";
    public static final int WIFI_BANDWIDTH_10_MHZ = 6;
    public static final int WIFI_BANDWIDTH_160_MHZ = 3;
    public static final int WIFI_BANDWIDTH_20_MHZ = 0;
    public static final int WIFI_BANDWIDTH_320_MHZ = 7;
    public static final int WIFI_BANDWIDTH_40_MHZ = 1;
    public static final int WIFI_BANDWIDTH_5_MHZ = 5;
    public static final int WIFI_BANDWIDTH_80P80_MHZ = 4;
    public static final int WIFI_BANDWIDTH_80_MHZ = 2;
    public static final int WIFI_BANDWIDTH_INVALID = -1;
    public static final int WIFI_PREAMBLE_CCK = 1;
    public static final int WIFI_PREAMBLE_EHT = 6;
    public static final int WIFI_PREAMBLE_HE = 5;
    public static final int WIFI_PREAMBLE_HT = 2;
    public static final int WIFI_PREAMBLE_INVALID = -1;
    public static final int WIFI_PREAMBLE_OFDM = 0;
    public static final int WIFI_PREAMBLE_VHT = 3;
    public static final int WIFI_SPATIAL_STREAMS_FOUR = 4;
    public static final int WIFI_SPATIAL_STREAMS_INVALID = -1;
    public static final int WIFI_SPATIAL_STREAMS_ONE = 1;
    public static final int WIFI_SPATIAL_STREAMS_THREE = 3;
    public static final int WIFI_SPATIAL_STREAMS_TWO = 2;
    public static final int WME_ACCESS_CATEGORY_BE = 0;
    public static final int WME_ACCESS_CATEGORY_BK = 1;
    public static final int WME_ACCESS_CATEGORY_VI = 2;
    public static final int WME_ACCESS_CATEGORY_VO = 3;
    private final int mCellularDataNetworkType;
    private final int mCellularSignalStrengthDb;
    private final int mCellularSignalStrengthDbm;
    private final int mChannelUtilizationRatio;
    private final ContentionTimeStats[] mContentionTimeStats;
    private final boolean mIsCellularDataAvailable;
    private final boolean mIsSameRegisteredCell;
    private final boolean mIsThroughputSufficient;
    private final boolean mIsWifiScoringEnabled;
    private final int mLinkSpeedMbps;
    private final int mProbeElapsedTimeSinceLastUpdateMillis;
    private final int mProbeMcsRateSinceLastUpdate;
    private final int mProbeStatusSinceLastUpdate;
    private final RadioStats[] mRadioStats;
    private final RateStats[] mRateStats;
    private final int mRssi;
    private final int mRxLinkSpeedMbps;
    private final int mTimeSliceDutyCycleInPercent;
    private final long mTimeStampMillis;
    private final long mTotalBackgroundScanTimeMillis;
    private final long mTotalBeaconRx;
    private final long mTotalCcaBusyFreqTimeMillis;
    private final long mTotalHotspot2ScanTimeMillis;
    private final long mTotalNanScanTimeMillis;
    private final long mTotalPnoScanTimeMillis;
    private final long mTotalRadioOnFreqTimeMillis;
    private final long mTotalRadioOnTimeMillis;
    private final long mTotalRadioRxTimeMillis;
    private final long mTotalRadioTxTimeMillis;
    private final long mTotalRoamScanTimeMillis;
    private final long mTotalRxSuccess;
    private final long mTotalScanTimeMillis;
    private final long mTotalTxBad;
    private final long mTotalTxRetries;
    private final long mTotalTxSuccess;

    @Retention(RetentionPolicy.SOURCE)
    public @interface ProbeStatus {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface WifiChannelBandwidth {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface WifiPreambleType {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface WifiSpatialStreams {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface WmeAccessCategory {
    }

    public int describeContents() {
        return 0;
    }

    public static final class ContentionTimeStats implements Parcelable {
        public static final Parcelable.Creator<ContentionTimeStats> CREATOR = new Parcelable.Creator<ContentionTimeStats>() {
            public ContentionTimeStats createFromParcel(Parcel parcel) {
                ContentionTimeStats contentionTimeStats = new ContentionTimeStats();
                contentionTimeStats.mContentionTimeMinMicros = parcel.readLong();
                contentionTimeStats.mContentionTimeMaxMicros = parcel.readLong();
                contentionTimeStats.mContentionTimeAvgMicros = parcel.readLong();
                contentionTimeStats.mContentionNumSamples = parcel.readLong();
                return contentionTimeStats;
            }

            public ContentionTimeStats[] newArray(int i) {
                return new ContentionTimeStats[i];
            }
        };
        /* access modifiers changed from: private */
        public long mContentionNumSamples;
        /* access modifiers changed from: private */
        public long mContentionTimeAvgMicros;
        /* access modifiers changed from: private */
        public long mContentionTimeMaxMicros;
        /* access modifiers changed from: private */
        public long mContentionTimeMinMicros;

        public int describeContents() {
            return 0;
        }

        public ContentionTimeStats() {
        }

        public ContentionTimeStats(long j, long j2, long j3, long j4) {
            this.mContentionTimeMinMicros = j;
            this.mContentionTimeMaxMicros = j2;
            this.mContentionTimeAvgMicros = j3;
            this.mContentionNumSamples = j4;
        }

        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeLong(this.mContentionTimeMinMicros);
            parcel.writeLong(this.mContentionTimeMaxMicros);
            parcel.writeLong(this.mContentionTimeAvgMicros);
            parcel.writeLong(this.mContentionNumSamples);
        }

        public long getContentionTimeMinMicros() {
            return this.mContentionTimeMinMicros;
        }

        public long getContentionTimeMaxMicros() {
            return this.mContentionTimeMaxMicros;
        }

        public long getContentionTimeAvgMicros() {
            return this.mContentionTimeAvgMicros;
        }

        public long getContentionNumSamples() {
            return this.mContentionNumSamples;
        }
    }

    public static final class RateStats implements Parcelable {
        public static final Parcelable.Creator<RateStats> CREATOR = new Parcelable.Creator<RateStats>() {
            public RateStats createFromParcel(Parcel parcel) {
                RateStats rateStats = new RateStats();
                rateStats.mPreamble = parcel.readInt();
                rateStats.mNss = parcel.readInt();
                rateStats.mBw = parcel.readInt();
                rateStats.mRateMcsIdx = parcel.readInt();
                rateStats.mBitRateInKbps = parcel.readInt();
                rateStats.mTxMpdu = parcel.readInt();
                rateStats.mRxMpdu = parcel.readInt();
                rateStats.mMpduLost = parcel.readInt();
                rateStats.mRetries = parcel.readInt();
                return rateStats;
            }

            public RateStats[] newArray(int i) {
                return new RateStats[i];
            }
        };
        /* access modifiers changed from: private */
        public int mBitRateInKbps;
        /* access modifiers changed from: private */
        public int mBw;
        /* access modifiers changed from: private */
        public int mMpduLost;
        /* access modifiers changed from: private */
        public int mNss;
        /* access modifiers changed from: private */
        public int mPreamble;
        /* access modifiers changed from: private */
        public int mRateMcsIdx;
        /* access modifiers changed from: private */
        public int mRetries;
        /* access modifiers changed from: private */
        public int mRxMpdu;
        /* access modifiers changed from: private */
        public int mTxMpdu;

        public int describeContents() {
            return 0;
        }

        public RateStats() {
        }

        public RateStats(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9) {
            this.mPreamble = i;
            this.mNss = i2;
            this.mBw = i3;
            this.mRateMcsIdx = i4;
            this.mBitRateInKbps = i5;
            this.mTxMpdu = i6;
            this.mRxMpdu = i7;
            this.mMpduLost = i8;
            this.mRetries = i9;
        }

        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(this.mPreamble);
            parcel.writeInt(this.mNss);
            parcel.writeInt(this.mBw);
            parcel.writeInt(this.mRateMcsIdx);
            parcel.writeInt(this.mBitRateInKbps);
            parcel.writeInt(this.mTxMpdu);
            parcel.writeInt(this.mRxMpdu);
            parcel.writeInt(this.mMpduLost);
            parcel.writeInt(this.mRetries);
        }

        public int getPreamble() {
            return this.mPreamble;
        }

        public int getNumberOfSpatialStreams() {
            return this.mNss;
        }

        public int getBandwidthInMhz() {
            return this.mBw;
        }

        public int getRateMcsIdx() {
            return this.mRateMcsIdx;
        }

        public int getBitRateInKbps() {
            return this.mBitRateInKbps;
        }

        public int getTxMpdu() {
            return this.mTxMpdu;
        }

        public int getRxMpdu() {
            return this.mRxMpdu;
        }

        public int getMpduLost() {
            return this.mMpduLost;
        }

        public int getRetries() {
            return this.mRetries;
        }
    }

    public static final class RadioStats implements Parcelable {
        public static final Parcelable.Creator<RadioStats> CREATOR = new Parcelable.Creator<RadioStats>() {
            public RadioStats createFromParcel(Parcel parcel) {
                RadioStats radioStats = new RadioStats();
                radioStats.mRadioId = parcel.readInt();
                radioStats.mTotalRadioOnTimeMillis = parcel.readLong();
                radioStats.mTotalRadioTxTimeMillis = parcel.readLong();
                radioStats.mTotalRadioRxTimeMillis = parcel.readLong();
                radioStats.mTotalScanTimeMillis = parcel.readLong();
                radioStats.mTotalNanScanTimeMillis = parcel.readLong();
                radioStats.mTotalBackgroundScanTimeMillis = parcel.readLong();
                radioStats.mTotalRoamScanTimeMillis = parcel.readLong();
                radioStats.mTotalPnoScanTimeMillis = parcel.readLong();
                radioStats.mTotalHotspot2ScanTimeMillis = parcel.readLong();
                return radioStats;
            }

            public RadioStats[] newArray(int i) {
                return new RadioStats[i];
            }
        };
        /* access modifiers changed from: private */
        public int mRadioId;
        /* access modifiers changed from: private */
        public long mTotalBackgroundScanTimeMillis;
        /* access modifiers changed from: private */
        public long mTotalHotspot2ScanTimeMillis;
        /* access modifiers changed from: private */
        public long mTotalNanScanTimeMillis;
        /* access modifiers changed from: private */
        public long mTotalPnoScanTimeMillis;
        /* access modifiers changed from: private */
        public long mTotalRadioOnTimeMillis;
        /* access modifiers changed from: private */
        public long mTotalRadioRxTimeMillis;
        /* access modifiers changed from: private */
        public long mTotalRadioTxTimeMillis;
        /* access modifiers changed from: private */
        public long mTotalRoamScanTimeMillis;
        /* access modifiers changed from: private */
        public long mTotalScanTimeMillis;

        public int describeContents() {
            return 0;
        }

        public RadioStats() {
        }

        public RadioStats(int i, long j, long j2, long j3, long j4, long j5, long j6, long j7, long j8, long j9) {
            this.mRadioId = i;
            this.mTotalRadioOnTimeMillis = j;
            this.mTotalRadioTxTimeMillis = j2;
            this.mTotalRadioRxTimeMillis = j3;
            this.mTotalScanTimeMillis = j4;
            this.mTotalNanScanTimeMillis = j5;
            this.mTotalBackgroundScanTimeMillis = j6;
            this.mTotalRoamScanTimeMillis = j7;
            this.mTotalPnoScanTimeMillis = j8;
            this.mTotalHotspot2ScanTimeMillis = j9;
        }

        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(this.mRadioId);
            parcel.writeLong(this.mTotalRadioOnTimeMillis);
            parcel.writeLong(this.mTotalRadioTxTimeMillis);
            parcel.writeLong(this.mTotalRadioRxTimeMillis);
            parcel.writeLong(this.mTotalScanTimeMillis);
            parcel.writeLong(this.mTotalNanScanTimeMillis);
            parcel.writeLong(this.mTotalBackgroundScanTimeMillis);
            parcel.writeLong(this.mTotalRoamScanTimeMillis);
            parcel.writeLong(this.mTotalPnoScanTimeMillis);
            parcel.writeLong(this.mTotalHotspot2ScanTimeMillis);
        }

        public long getRadioId() {
            return (long) this.mRadioId;
        }

        public long getTotalRadioOnTimeMillis() {
            return this.mTotalRadioOnTimeMillis;
        }

        public long getTotalRadioTxTimeMillis() {
            return this.mTotalRadioTxTimeMillis;
        }

        public long getTotalRadioRxTimeMillis() {
            return this.mTotalRadioRxTimeMillis;
        }

        public long getTotalScanTimeMillis() {
            return this.mTotalScanTimeMillis;
        }

        public long getTotalNanScanTimeMillis() {
            return this.mTotalNanScanTimeMillis;
        }

        public long getTotalBackgroundScanTimeMillis() {
            return this.mTotalBackgroundScanTimeMillis;
        }

        public long getTotalRoamScanTimeMillis() {
            return this.mTotalRoamScanTimeMillis;
        }

        public long getTotalPnoScanTimeMillis() {
            return this.mTotalPnoScanTimeMillis;
        }

        public long getTotalHotspot2ScanTimeMillis() {
            return this.mTotalHotspot2ScanTimeMillis;
        }
    }

    public WifiUsabilityStatsEntry(long j, int i, int i2, long j2, long j3, long j4, long j5, long j6, long j7, long j8, long j9, long j10, long j11, long j12, long j13, long j14, long j15, long j16, long j17, int i3, int i4, int i5, int i6, int i7, ContentionTimeStats[] contentionTimeStatsArr, RateStats[] rateStatsArr, RadioStats[] radioStatsArr, int i8, boolean z, boolean z2, boolean z3, int i9, int i10, int i11, boolean z4) {
        this.mTimeStampMillis = j;
        this.mRssi = i;
        this.mLinkSpeedMbps = i2;
        this.mTotalTxSuccess = j2;
        this.mTotalTxRetries = j3;
        this.mTotalTxBad = j4;
        this.mTotalRxSuccess = j5;
        this.mTotalRadioOnTimeMillis = j6;
        this.mTotalRadioTxTimeMillis = j7;
        this.mTotalRadioRxTimeMillis = j8;
        this.mTotalScanTimeMillis = j9;
        this.mTotalNanScanTimeMillis = j10;
        this.mTotalBackgroundScanTimeMillis = j11;
        this.mTotalRoamScanTimeMillis = j12;
        this.mTotalPnoScanTimeMillis = j13;
        this.mTotalHotspot2ScanTimeMillis = j14;
        this.mTotalCcaBusyFreqTimeMillis = j15;
        this.mTotalRadioOnFreqTimeMillis = j16;
        this.mTotalBeaconRx = j17;
        this.mProbeStatusSinceLastUpdate = i3;
        this.mProbeElapsedTimeSinceLastUpdateMillis = i4;
        this.mProbeMcsRateSinceLastUpdate = i5;
        this.mRxLinkSpeedMbps = i6;
        this.mTimeSliceDutyCycleInPercent = i7;
        this.mContentionTimeStats = contentionTimeStatsArr;
        this.mRateStats = rateStatsArr;
        this.mRadioStats = radioStatsArr;
        this.mChannelUtilizationRatio = i8;
        this.mIsThroughputSufficient = z;
        this.mIsWifiScoringEnabled = z2;
        this.mIsCellularDataAvailable = z3;
        this.mCellularDataNetworkType = i9;
        this.mCellularSignalStrengthDbm = i10;
        this.mCellularSignalStrengthDb = i11;
        this.mIsSameRegisteredCell = z4;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.mTimeStampMillis);
        parcel.writeInt(this.mRssi);
        parcel.writeInt(this.mLinkSpeedMbps);
        parcel.writeLong(this.mTotalTxSuccess);
        parcel.writeLong(this.mTotalTxRetries);
        parcel.writeLong(this.mTotalTxBad);
        parcel.writeLong(this.mTotalRxSuccess);
        parcel.writeLong(this.mTotalRadioOnTimeMillis);
        parcel.writeLong(this.mTotalRadioTxTimeMillis);
        parcel.writeLong(this.mTotalRadioRxTimeMillis);
        parcel.writeLong(this.mTotalScanTimeMillis);
        parcel.writeLong(this.mTotalNanScanTimeMillis);
        parcel.writeLong(this.mTotalBackgroundScanTimeMillis);
        parcel.writeLong(this.mTotalRoamScanTimeMillis);
        parcel.writeLong(this.mTotalPnoScanTimeMillis);
        parcel.writeLong(this.mTotalHotspot2ScanTimeMillis);
        parcel.writeLong(this.mTotalCcaBusyFreqTimeMillis);
        parcel.writeLong(this.mTotalRadioOnFreqTimeMillis);
        parcel.writeLong(this.mTotalBeaconRx);
        parcel.writeInt(this.mProbeStatusSinceLastUpdate);
        parcel.writeInt(this.mProbeElapsedTimeSinceLastUpdateMillis);
        parcel.writeInt(this.mProbeMcsRateSinceLastUpdate);
        parcel.writeInt(this.mRxLinkSpeedMbps);
        parcel.writeInt(this.mTimeSliceDutyCycleInPercent);
        parcel.writeTypedArray(this.mContentionTimeStats, i);
        parcel.writeTypedArray(this.mRateStats, i);
        parcel.writeTypedArray(this.mRadioStats, i);
        parcel.writeInt(this.mChannelUtilizationRatio);
        parcel.writeBoolean(this.mIsThroughputSufficient);
        parcel.writeBoolean(this.mIsWifiScoringEnabled);
        parcel.writeBoolean(this.mIsCellularDataAvailable);
        parcel.writeInt(this.mCellularDataNetworkType);
        parcel.writeInt(this.mCellularSignalStrengthDbm);
        parcel.writeInt(this.mCellularSignalStrengthDb);
        parcel.writeBoolean(this.mIsSameRegisteredCell);
    }

    public long getTimeStampMillis() {
        return this.mTimeStampMillis;
    }

    public int getRssi() {
        return this.mRssi;
    }

    public int getLinkSpeedMbps() {
        return this.mLinkSpeedMbps;
    }

    public long getTotalTxSuccess() {
        return this.mTotalTxSuccess;
    }

    public long getTotalTxRetries() {
        return this.mTotalTxRetries;
    }

    public long getTotalTxBad() {
        return this.mTotalTxBad;
    }

    public long getTotalRxSuccess() {
        return this.mTotalRxSuccess;
    }

    public long getTotalRadioOnTimeMillis() {
        return this.mTotalRadioOnTimeMillis;
    }

    public long getTotalRadioTxTimeMillis() {
        return this.mTotalRadioTxTimeMillis;
    }

    public long getTotalRadioRxTimeMillis() {
        return this.mTotalRadioRxTimeMillis;
    }

    public long getTotalScanTimeMillis() {
        return this.mTotalScanTimeMillis;
    }

    public long getTotalNanScanTimeMillis() {
        return this.mTotalNanScanTimeMillis;
    }

    public long getTotalBackgroundScanTimeMillis() {
        return this.mTotalBackgroundScanTimeMillis;
    }

    public long getTotalRoamScanTimeMillis() {
        return this.mTotalRoamScanTimeMillis;
    }

    public long getTotalPnoScanTimeMillis() {
        return this.mTotalPnoScanTimeMillis;
    }

    public long getTotalHotspot2ScanTimeMillis() {
        return this.mTotalHotspot2ScanTimeMillis;
    }

    public long getTotalCcaBusyFreqTimeMillis() {
        return this.mTotalCcaBusyFreqTimeMillis;
    }

    public long getTotalRadioOnFreqTimeMillis() {
        return this.mTotalRadioOnFreqTimeMillis;
    }

    public long getTotalBeaconRx() {
        return this.mTotalBeaconRx;
    }

    public int getProbeStatusSinceLastUpdate() {
        return this.mProbeStatusSinceLastUpdate;
    }

    public int getProbeElapsedTimeSinceLastUpdateMillis() {
        return this.mProbeElapsedTimeSinceLastUpdateMillis;
    }

    public int getProbeMcsRateSinceLastUpdate() {
        return this.mProbeMcsRateSinceLastUpdate;
    }

    public int getRxLinkSpeedMbps() {
        return this.mRxLinkSpeedMbps;
    }

    public int getTimeSliceDutyCycleInPercent() {
        int i = this.mTimeSliceDutyCycleInPercent;
        if (i != -1) {
            return i;
        }
        throw new NoSuchElementException("Unknown value");
    }

    public ContentionTimeStats getContentionTimeStats(int i) {
        ContentionTimeStats[] contentionTimeStatsArr = this.mContentionTimeStats;
        if (contentionTimeStatsArr != null && contentionTimeStatsArr.length == 4) {
            return contentionTimeStatsArr[i];
        }
        Log.e(TAG, "The ContentionTimeStats is not filled out correctly: " + this.mContentionTimeStats);
        return new ContentionTimeStats();
    }

    public List<RateStats> getRateStats() {
        RateStats[] rateStatsArr = this.mRateStats;
        if (rateStatsArr != null) {
            return Arrays.asList(rateStatsArr);
        }
        return Collections.emptyList();
    }

    public List<RadioStats> getWifiLinkLayerRadioStats() {
        RadioStats[] radioStatsArr = this.mRadioStats;
        if (radioStatsArr != null) {
            return Arrays.asList(radioStatsArr);
        }
        return Collections.emptyList();
    }

    public int getChannelUtilizationRatio() {
        return this.mChannelUtilizationRatio;
    }

    public boolean isThroughputSufficient() {
        return this.mIsThroughputSufficient;
    }

    public boolean isWifiScoringEnabled() {
        return this.mIsWifiScoringEnabled;
    }

    public boolean isCellularDataAvailable() {
        return this.mIsCellularDataAvailable;
    }

    public int getCellularDataNetworkType() {
        return this.mCellularDataNetworkType;
    }

    public int getCellularSignalStrengthDbm() {
        return this.mCellularSignalStrengthDbm;
    }

    public int getCellularSignalStrengthDb() {
        return this.mCellularSignalStrengthDb;
    }

    public boolean isSameRegisteredCell() {
        return this.mIsSameRegisteredCell;
    }
}
