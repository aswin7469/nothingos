package android.net.wifi;

import android.annotation.SystemApi;
import android.content.Context;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Process;
import android.os.RemoteException;
import android.os.WorkSource;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import com.android.wifi.p018x.com.android.internal.util.AsyncChannel;
import com.android.wifi.p018x.com.android.modules.utils.build.SdkLevel;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;

@SystemApi
public class WifiScanner {
    private static final int BASE = 159744;
    public static final int CMD_DEREGISTER_SCAN_LISTENER = 159772;
    public static final int CMD_DISABLE = 159775;
    public static final int CMD_ENABLE = 159774;
    public static final int CMD_FULL_SCAN_RESULT = 159764;
    public static final int CMD_GET_SCAN_RESULTS = 159748;
    public static final int CMD_GET_SINGLE_SCAN_RESULTS = 159773;
    public static final int CMD_OP_FAILED = 159762;
    public static final int CMD_OP_SUCCEEDED = 159761;
    public static final int CMD_PNO_NETWORK_FOUND = 159770;
    public static final int CMD_REGISTER_SCAN_LISTENER = 159771;
    public static final int CMD_SCAN_RESULT = 159749;
    public static final int CMD_SINGLE_SCAN_COMPLETED = 159767;
    public static final int CMD_START_BACKGROUND_SCAN = 159746;
    public static final int CMD_START_PNO_SCAN = 159768;
    public static final int CMD_START_SINGLE_SCAN = 159765;
    public static final int CMD_STOP_BACKGROUND_SCAN = 159747;
    public static final int CMD_STOP_PNO_SCAN = 159769;
    public static final int CMD_STOP_SINGLE_SCAN = 159766;
    private static final boolean DBG = false;
    public static final String GET_AVAILABLE_CHANNELS_EXTRA = "Channels";
    private static final int INVALID_KEY = 0;
    public static final int MAX_SCAN_PERIOD_MS = 1024000;
    public static final int MIN_SCAN_PERIOD_MS = 1000;
    public static final String PNO_PARAMS_PNO_SETTINGS_KEY = "PnoSettings";
    public static final String PNO_PARAMS_SCAN_SETTINGS_KEY = "ScanSettings";
    public static final int REASON_DUPLICATE_REQEUST = -5;
    public static final int REASON_INVALID_LISTENER = -2;
    public static final int REASON_INVALID_REQUEST = -3;
    public static final int REASON_NOT_AUTHORIZED = -4;
    public static final int REASON_SUCCEEDED = 0;
    public static final int REASON_UNSPECIFIED = -1;
    @Deprecated
    public static final int REPORT_EVENT_AFTER_BUFFER_FULL = 0;
    public static final int REPORT_EVENT_AFTER_EACH_SCAN = 1;
    public static final int REPORT_EVENT_FULL_SCAN_RESULT = 2;
    public static final int REPORT_EVENT_NO_BATCH = 4;
    public static final String REQUEST_FEATURE_ID_KEY = "FeatureId";
    public static final String REQUEST_PACKAGE_NAME_KEY = "PackageName";
    public static final String SCAN_PARAMS_SCAN_SETTINGS_KEY = "ScanSettings";
    public static final String SCAN_PARAMS_WORK_SOURCE_KEY = "WorkSource";
    public static final int SCAN_TYPE_HIGH_ACCURACY = 2;
    public static final int SCAN_TYPE_LOW_LATENCY = 0;
    public static final int SCAN_TYPE_LOW_POWER = 1;
    public static final int SCAN_TYPE_MAX = 2;
    private static final String TAG = "WifiScanner";
    public static final int WIFI_BAND_24_5_6_60_GHZ = 27;
    public static final int WIFI_BAND_24_5_6_GHZ = 11;
    public static final int WIFI_BAND_24_5_WITH_DFS_6_60_GHZ = 31;
    public static final int WIFI_BAND_24_5_WITH_DFS_6_GHZ = 15;
    public static final int WIFI_BAND_24_GHZ = 1;
    public static final int WIFI_BAND_24_GHZ_WITH_5GHZ_DFS = 5;
    public static final int WIFI_BAND_5_GHZ = 2;
    public static final int WIFI_BAND_5_GHZ_DFS_ONLY = 4;
    public static final int WIFI_BAND_5_GHZ_WITH_DFS = 6;
    public static final int WIFI_BAND_60_GHZ = 16;
    public static final int WIFI_BAND_6_GHZ = 8;
    public static final int WIFI_BAND_ALL = 31;
    public static final int WIFI_BAND_BOTH = 3;
    public static final int WIFI_BAND_BOTH_WITH_DFS = 7;
    public static final int WIFI_BAND_COUNT = 5;
    public static final int WIFI_BAND_INDEX_24_GHZ = 0;
    public static final int WIFI_BAND_INDEX_5_GHZ = 1;
    public static final int WIFI_BAND_INDEX_5_GHZ_DFS_ONLY = 2;
    public static final int WIFI_BAND_INDEX_60_GHZ = 4;
    public static final int WIFI_BAND_INDEX_6_GHZ = 3;
    public static final int WIFI_BAND_UNSPECIFIED = 0;
    public static final int WIFI_RNR_ENABLED = 1;
    public static final int WIFI_RNR_ENABLED_IF_WIFI_BAND_6_GHZ_SCANNED = 0;
    public static final int WIFI_RNR_NOT_NEEDED = 2;
    /* access modifiers changed from: private */
    public AsyncChannel mAsyncChannel;
    private Context mContext;
    private final SparseArray<Executor> mExecutorMap = new SparseArray<>();
    private final Handler mInternalHandler;
    private int mListenerKey = 1;
    private final SparseArray mListenerMap = new SparseArray();
    private final Object mListenerMapLock = new Object();
    private IWifiScanner mService;

    @SystemApi
    public interface ActionListener {
        void onFailure(int i, String str);

        void onSuccess();
    }

    @Deprecated
    public static class BssidInfo {
        public String bssid;
        public int frequencyHint;
        public int high;
        public int low;
    }

    @Deprecated
    public interface BssidListener extends ActionListener {
        void onFound(ScanResult[] scanResultArr);

        void onLost(ScanResult[] scanResultArr);
    }

    @SystemApi
    @Deprecated
    public static class HotlistSettings implements Parcelable {
        public static final Parcelable.Creator<HotlistSettings> CREATOR = new Parcelable.Creator<HotlistSettings>() {
            public HotlistSettings createFromParcel(Parcel parcel) {
                return new HotlistSettings();
            }

            public HotlistSettings[] newArray(int i) {
                return new HotlistSettings[i];
            }
        };
        public int apLostThreshold;
        public BssidInfo[] bssidInfos;

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel parcel, int i) {
        }
    }

    public interface PnoScanListener extends ScanListener {
        void onPnoNetworkFound(ScanResult[] scanResultArr);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface RnrSetting {
    }

    public interface ScanListener extends ActionListener {
        void onFullResult(ScanResult scanResult);

        @Deprecated
        void onPeriodChanged(int i);

        void onResults(ScanData[] scanDataArr);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface WifiBand {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface WifiBandIndex {
    }

    @Deprecated
    public interface WifiChangeListener extends ActionListener {
        void onChanging(ScanResult[] scanResultArr);

        void onQuiescence(ScanResult[] scanResultArr);
    }

    @SystemApi
    @Deprecated
    public static class WifiChangeSettings implements Parcelable {
        public static final Parcelable.Creator<WifiChangeSettings> CREATOR = new Parcelable.Creator<WifiChangeSettings>() {
            public WifiChangeSettings createFromParcel(Parcel parcel) {
                return new WifiChangeSettings();
            }

            public WifiChangeSettings[] newArray(int i) {
                return new WifiChangeSettings[i];
            }
        };
        public BssidInfo[] bssidInfos;
        public int lostApSampleSize;
        public int minApsBreachingThreshold;
        public int periodInMs;
        public int rssiSampleSize;
        public int unchangedSampleSize;

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel parcel, int i) {
        }
    }

    public static boolean isFullBandScan(int i, boolean z) {
        return (((i | 8) | 16) | (z ? 4 : 0)) == 31;
    }

    @SystemApi
    public List<Integer> getAvailableChannels(int i) {
        try {
            ArrayList<Integer> integerArrayList = this.mService.getAvailableChannels(i, this.mContext.getOpPackageName(), this.mContext.getAttributionTag()).getIntegerArrayList(GET_AVAILABLE_CHANNELS_EXTRA);
            return integerArrayList == null ? new ArrayList() : integerArrayList;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static class ChannelSpec {
        public int dwellTimeMS = 0;
        public int frequency;
        public boolean passive = false;

        public ChannelSpec(int i) {
            this.frequency = i;
        }
    }

    public static class ScanSettings implements Parcelable {
        public static final Parcelable.Creator<ScanSettings> CREATOR = new Parcelable.Creator<ScanSettings>() {
            public ScanSettings createFromParcel(Parcel parcel) {
                ScanSettings scanSettings = new ScanSettings();
                scanSettings.band = parcel.readInt();
                scanSettings.periodInMs = parcel.readInt();
                scanSettings.reportEvents = parcel.readInt();
                scanSettings.numBssidsPerScan = parcel.readInt();
                scanSettings.maxScansToCache = parcel.readInt();
                scanSettings.maxPeriodInMs = parcel.readInt();
                scanSettings.stepCount = parcel.readInt();
                scanSettings.isPnoScan = parcel.readInt() == 1;
                scanSettings.type = parcel.readInt();
                scanSettings.ignoreLocationSettings = parcel.readInt() == 1;
                scanSettings.hideFromAppOps = parcel.readInt() == 1;
                scanSettings.mRnrSetting = parcel.readInt();
                scanSettings.mEnable6GhzPsc = parcel.readBoolean();
                int readInt = parcel.readInt();
                scanSettings.channels = new ChannelSpec[readInt];
                for (int i = 0; i < readInt; i++) {
                    ChannelSpec channelSpec = new ChannelSpec(parcel.readInt());
                    channelSpec.dwellTimeMS = parcel.readInt();
                    channelSpec.passive = parcel.readInt() == 1;
                    scanSettings.channels[i] = channelSpec;
                }
                int readInt2 = parcel.readInt();
                scanSettings.hiddenNetworks.clear();
                for (int i2 = 0; i2 < readInt2; i2++) {
                    scanSettings.hiddenNetworks.add(new HiddenNetwork(parcel.readString()));
                }
                return scanSettings;
            }

            public ScanSettings[] newArray(int i) {
                return new ScanSettings[i];
            }
        };
        public int band;
        public ChannelSpec[] channels;
        public final List<HiddenNetwork> hiddenNetworks = new ArrayList();
        @SystemApi
        public boolean hideFromAppOps;
        @SystemApi
        public boolean ignoreLocationSettings;
        public boolean isPnoScan;
        /* access modifiers changed from: private */
        public boolean mEnable6GhzPsc = false;
        /* access modifiers changed from: private */
        public int mRnrSetting = 0;
        @Deprecated
        public int maxPeriodInMs;
        @Deprecated
        public int maxScansToCache;
        @Deprecated
        public int numBssidsPerScan;
        @Deprecated
        public int periodInMs;
        @Deprecated
        public int reportEvents;
        @Deprecated
        public int stepCount;
        public int type = 0;

        public int describeContents() {
            return 0;
        }

        public static class HiddenNetwork {
            public final String ssid;

            public HiddenNetwork(String str) {
                this.ssid = str;
            }
        }

        public void set6GhzPscOnlyEnabled(boolean z) {
            if (SdkLevel.isAtLeastS()) {
                this.mEnable6GhzPsc = z;
                return;
            }
            throw new UnsupportedOperationException();
        }

        public boolean is6GhzPscOnlyEnabled() {
            if (SdkLevel.isAtLeastS()) {
                return this.mEnable6GhzPsc;
            }
            throw new UnsupportedOperationException();
        }

        public void setRnrSetting(int i) {
            if (!SdkLevel.isAtLeastS()) {
                throw new UnsupportedOperationException();
            } else if (i < 0 || i > 2) {
                throw new IllegalArgumentException("Invalid rnrSetting");
            } else {
                this.mRnrSetting = i;
            }
        }

        public int getRnrSetting() {
            if (SdkLevel.isAtLeastS()) {
                return this.mRnrSetting;
            }
            throw new UnsupportedOperationException();
        }

        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(this.band);
            parcel.writeInt(this.periodInMs);
            parcel.writeInt(this.reportEvents);
            parcel.writeInt(this.numBssidsPerScan);
            parcel.writeInt(this.maxScansToCache);
            parcel.writeInt(this.maxPeriodInMs);
            parcel.writeInt(this.stepCount);
            parcel.writeInt(this.isPnoScan ? 1 : 0);
            parcel.writeInt(this.type);
            parcel.writeInt(this.ignoreLocationSettings ? 1 : 0);
            parcel.writeInt(this.hideFromAppOps ? 1 : 0);
            parcel.writeInt(this.mRnrSetting);
            parcel.writeBoolean(this.mEnable6GhzPsc);
            ChannelSpec[] channelSpecArr = this.channels;
            int i2 = 0;
            if (channelSpecArr != null) {
                parcel.writeInt(channelSpecArr.length);
                while (true) {
                    ChannelSpec[] channelSpecArr2 = this.channels;
                    if (i2 >= channelSpecArr2.length) {
                        break;
                    }
                    parcel.writeInt(channelSpecArr2[i2].frequency);
                    parcel.writeInt(this.channels[i2].dwellTimeMS);
                    parcel.writeInt(this.channels[i2].passive ? 1 : 0);
                    i2++;
                }
            } else {
                parcel.writeInt(0);
            }
            parcel.writeInt(this.hiddenNetworks.size());
            for (HiddenNetwork hiddenNetwork : this.hiddenNetworks) {
                parcel.writeString(hiddenNetwork.ssid);
            }
        }
    }

    public static class ScanData implements Parcelable {
        public static final Parcelable.Creator<ScanData> CREATOR = new Parcelable.Creator<ScanData>() {
            public ScanData createFromParcel(Parcel parcel) {
                int readInt = parcel.readInt();
                int readInt2 = parcel.readInt();
                int readInt3 = parcel.readInt();
                int readInt4 = parcel.readInt();
                ArrayList arrayList = new ArrayList();
                parcel.readParcelableList(arrayList, ScanResult.class.getClassLoader());
                return new ScanData(readInt, readInt2, readInt3, readInt4, (List<ScanResult>) arrayList);
            }

            public ScanData[] newArray(int i) {
                return new ScanData[i];
            }
        };
        private int mBucketsScanned;
        private int mFlags;
        private int mId;
        private final List<ScanResult> mResults;
        private int mScannedBands;

        public int describeContents() {
            return 0;
        }

        ScanData() {
            this.mResults = new ArrayList();
        }

        public ScanData(int i, int i2, ScanResult[] scanResultArr) {
            this.mId = i;
            this.mFlags = i2;
            this.mResults = new ArrayList(Arrays.asList(scanResultArr));
        }

        public ScanData(int i, int i2, int i3, int i4, ScanResult[] scanResultArr) {
            this(i, i2, i3, i4, (List<ScanResult>) new ArrayList(Arrays.asList(scanResultArr)));
        }

        public ScanData(int i, int i2, int i3, int i4, List<ScanResult> list) {
            this.mId = i;
            this.mFlags = i2;
            this.mBucketsScanned = i3;
            this.mScannedBands = i4;
            this.mResults = list;
        }

        public ScanData(ScanData scanData) {
            this.mId = scanData.mId;
            this.mFlags = scanData.mFlags;
            this.mBucketsScanned = scanData.mBucketsScanned;
            this.mScannedBands = scanData.mScannedBands;
            this.mResults = new ArrayList();
            for (ScanResult scanResult : scanData.mResults) {
                this.mResults.add(new ScanResult(scanResult));
            }
        }

        public int getId() {
            return this.mId;
        }

        public int getFlags() {
            return this.mFlags;
        }

        public int getBucketsScanned() {
            return this.mBucketsScanned;
        }

        public int getScannedBands() {
            return getScannedBandsInternal();
        }

        public int getScannedBandsInternal() {
            return this.mScannedBands;
        }

        public ScanResult[] getResults() {
            return (ScanResult[]) this.mResults.toArray(new ScanResult[0]);
        }

        public void addResults(ScanResult[] scanResultArr) {
            for (ScanResult scanResult : scanResultArr) {
                this.mResults.add(new ScanResult(scanResult));
            }
        }

        public void addResults(ScanData scanData) {
            this.mScannedBands |= scanData.mScannedBands;
            this.mFlags |= scanData.mFlags;
            addResults(scanData.getResults());
        }

        public boolean isFullBandScanResults() {
            int i = this.mScannedBands;
            return ((i & 1) == 0 || (i & 2) == 0) ? false : true;
        }

        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(this.mId);
            parcel.writeInt(this.mFlags);
            parcel.writeInt(this.mBucketsScanned);
            parcel.writeInt(this.mScannedBands);
            parcel.writeParcelableList(this.mResults, 0);
        }
    }

    public static class ParcelableScanData implements Parcelable {
        public static final Parcelable.Creator<ParcelableScanData> CREATOR = new Parcelable.Creator<ParcelableScanData>() {
            public ParcelableScanData createFromParcel(Parcel parcel) {
                int readInt = parcel.readInt();
                ScanData[] scanDataArr = new ScanData[readInt];
                for (int i = 0; i < readInt; i++) {
                    scanDataArr[i] = ScanData.CREATOR.createFromParcel(parcel);
                }
                return new ParcelableScanData(scanDataArr);
            }

            public ParcelableScanData[] newArray(int i) {
                return new ParcelableScanData[i];
            }
        };
        public ScanData[] mResults;

        public int describeContents() {
            return 0;
        }

        public ParcelableScanData(ScanData[] scanDataArr) {
            this.mResults = scanDataArr;
        }

        public ScanData[] getResults() {
            return this.mResults;
        }

        public void writeToParcel(Parcel parcel, int i) {
            ScanData[] scanDataArr = this.mResults;
            int i2 = 0;
            if (scanDataArr != null) {
                parcel.writeInt(scanDataArr.length);
                while (true) {
                    ScanData[] scanDataArr2 = this.mResults;
                    if (i2 < scanDataArr2.length) {
                        scanDataArr2[i2].writeToParcel(parcel, i);
                        i2++;
                    } else {
                        return;
                    }
                }
            } else {
                parcel.writeInt(0);
            }
        }
    }

    public static class ParcelableScanResults implements Parcelable {
        public static final Parcelable.Creator<ParcelableScanResults> CREATOR = new Parcelable.Creator<ParcelableScanResults>() {
            public ParcelableScanResults createFromParcel(Parcel parcel) {
                int readInt = parcel.readInt();
                ScanResult[] scanResultArr = new ScanResult[readInt];
                for (int i = 0; i < readInt; i++) {
                    scanResultArr[i] = ScanResult.CREATOR.createFromParcel(parcel);
                }
                return new ParcelableScanResults(scanResultArr);
            }

            public ParcelableScanResults[] newArray(int i) {
                return new ParcelableScanResults[i];
            }
        };
        public ScanResult[] mResults;

        public int describeContents() {
            return 0;
        }

        public ParcelableScanResults(ScanResult[] scanResultArr) {
            this.mResults = scanResultArr;
        }

        public ScanResult[] getResults() {
            return this.mResults;
        }

        public void writeToParcel(Parcel parcel, int i) {
            ScanResult[] scanResultArr = this.mResults;
            int i2 = 0;
            if (scanResultArr != null) {
                parcel.writeInt(scanResultArr.length);
                while (true) {
                    ScanResult[] scanResultArr2 = this.mResults;
                    if (i2 < scanResultArr2.length) {
                        scanResultArr2[i2].writeToParcel(parcel, i);
                        i2++;
                    } else {
                        return;
                    }
                }
            } else {
                parcel.writeInt(0);
            }
        }
    }

    public static class PnoSettings implements Parcelable {
        public static final Parcelable.Creator<PnoSettings> CREATOR = new Parcelable.Creator<PnoSettings>() {
            public PnoSettings createFromParcel(Parcel parcel) {
                PnoSettings pnoSettings = new PnoSettings();
                boolean z = true;
                if (parcel.readInt() != 1) {
                    z = false;
                }
                pnoSettings.isConnected = z;
                pnoSettings.min5GHzRssi = parcel.readInt();
                pnoSettings.min24GHzRssi = parcel.readInt();
                pnoSettings.min6GHzRssi = parcel.readInt();
                int readInt = parcel.readInt();
                pnoSettings.networkList = new PnoNetwork[readInt];
                for (int i = 0; i < readInt; i++) {
                    PnoNetwork pnoNetwork = new PnoNetwork(parcel.readString());
                    pnoNetwork.flags = parcel.readByte();
                    pnoNetwork.authBitField = parcel.readByte();
                    pnoNetwork.frequencies = parcel.createIntArray();
                    pnoSettings.networkList[i] = pnoNetwork;
                }
                return pnoSettings;
            }

            public PnoSettings[] newArray(int i) {
                return new PnoSettings[i];
            }
        };
        public boolean isConnected;
        public int min24GHzRssi;
        public int min5GHzRssi;
        public int min6GHzRssi;
        public PnoNetwork[] networkList;

        public int describeContents() {
            return 0;
        }

        public static class PnoNetwork {
            public static final byte AUTH_CODE_EAPOL = 4;
            public static final byte AUTH_CODE_OPEN = 1;
            public static final byte AUTH_CODE_PSK = 2;
            public static final byte FLAG_A_BAND = 2;
            public static final byte FLAG_DIRECTED_SCAN = 1;
            public static final byte FLAG_G_BAND = 4;
            public static final byte FLAG_SAME_NETWORK = 16;
            public static final byte FLAG_STRICT_MATCH = 8;
            public byte authBitField = 0;
            public byte flags = 0;
            public int[] frequencies = new int[0];
            public String ssid;

            public PnoNetwork(String str) {
                this.ssid = str;
            }

            public int hashCode() {
                return Objects.hash(this.ssid, Byte.valueOf(this.flags), Byte.valueOf(this.authBitField));
            }

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                if (!(obj instanceof PnoNetwork)) {
                    return false;
                }
                PnoNetwork pnoNetwork = (PnoNetwork) obj;
                if (TextUtils.equals(this.ssid, pnoNetwork.ssid) && this.flags == pnoNetwork.flags && this.authBitField == pnoNetwork.authBitField) {
                    return true;
                }
                return false;
            }
        }

        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(this.isConnected ? 1 : 0);
            parcel.writeInt(this.min5GHzRssi);
            parcel.writeInt(this.min24GHzRssi);
            parcel.writeInt(this.min6GHzRssi);
            PnoNetwork[] pnoNetworkArr = this.networkList;
            int i2 = 0;
            if (pnoNetworkArr != null) {
                parcel.writeInt(pnoNetworkArr.length);
                while (true) {
                    PnoNetwork[] pnoNetworkArr2 = this.networkList;
                    if (i2 < pnoNetworkArr2.length) {
                        parcel.writeString(pnoNetworkArr2[i2].ssid);
                        parcel.writeByte(this.networkList[i2].flags);
                        parcel.writeByte(this.networkList[i2].authBitField);
                        parcel.writeIntArray(this.networkList[i2].frequencies);
                        i2++;
                    } else {
                        return;
                    }
                }
            } else {
                parcel.writeInt(0);
            }
        }
    }

    @SystemApi
    public void setScanningEnabled(boolean z) {
        validateChannel();
        this.mAsyncChannel.sendMessage(z ? CMD_ENABLE : CMD_DISABLE, Process.myTid(), Binder.getCallingPid(), this.mContext.getOpPackageName());
    }

    public void registerScanListener(Executor executor, ScanListener scanListener) {
        Objects.requireNonNull(executor, "executor cannot be null");
        Objects.requireNonNull(scanListener, "listener cannot be null");
        int addListener = addListener(scanListener, executor);
        if (addListener != 0) {
            validateChannel();
            this.mAsyncChannel.sendMessage(CMD_REGISTER_SCAN_LISTENER, 0, addListener);
        }
    }

    public void registerScanListener(ScanListener scanListener) {
        registerScanListener(new SynchronousExecutor(), scanListener);
    }

    public void unregisterScanListener(ScanListener scanListener) {
        Objects.requireNonNull(scanListener, "listener cannot be null");
        int removeListener = removeListener((Object) scanListener);
        if (removeListener != 0) {
            validateChannel();
            this.mAsyncChannel.sendMessage(CMD_DEREGISTER_SCAN_LISTENER, 0, removeListener);
        }
    }

    @SystemApi
    public boolean isScanning() {
        try {
            return this.mService.isScanning();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void startBackgroundScan(ScanSettings scanSettings, ScanListener scanListener) {
        startBackgroundScan(scanSettings, scanListener, (WorkSource) null);
    }

    @Deprecated
    public void startBackgroundScan(ScanSettings scanSettings, ScanListener scanListener, WorkSource workSource) {
        Objects.requireNonNull(scanListener, "listener cannot be null");
        int addListener = addListener(scanListener);
        if (addListener != 0) {
            validateChannel();
            Bundle bundle = new Bundle();
            bundle.putParcelable("ScanSettings", scanSettings);
            bundle.putParcelable(SCAN_PARAMS_WORK_SOURCE_KEY, workSource);
            bundle.putString(REQUEST_PACKAGE_NAME_KEY, this.mContext.getOpPackageName());
            bundle.putString(REQUEST_FEATURE_ID_KEY, this.mContext.getAttributionTag());
            this.mAsyncChannel.sendMessage(CMD_START_BACKGROUND_SCAN, 0, addListener, bundle);
        }
    }

    @Deprecated
    public void stopBackgroundScan(ScanListener scanListener) {
        Objects.requireNonNull(scanListener, "listener cannot be null");
        int removeListener = removeListener((Object) scanListener);
        if (removeListener != 0) {
            validateChannel();
            Bundle bundle = new Bundle();
            bundle.putString(REQUEST_PACKAGE_NAME_KEY, this.mContext.getOpPackageName());
            bundle.putString(REQUEST_FEATURE_ID_KEY, this.mContext.getAttributionTag());
            this.mAsyncChannel.sendMessage(CMD_STOP_BACKGROUND_SCAN, 0, removeListener, bundle);
        }
    }

    @Deprecated
    public boolean getScanResults() {
        validateChannel();
        Bundle bundle = new Bundle();
        bundle.putString(REQUEST_PACKAGE_NAME_KEY, this.mContext.getOpPackageName());
        bundle.putString(REQUEST_FEATURE_ID_KEY, this.mContext.getAttributionTag());
        if (this.mAsyncChannel.sendMessageSynchronously(CMD_GET_SCAN_RESULTS, 0, 0, bundle).what == 159761) {
            return true;
        }
        return false;
    }

    public void startScan(ScanSettings scanSettings, ScanListener scanListener) {
        startScan(scanSettings, scanListener, (WorkSource) null);
    }

    public void startScan(ScanSettings scanSettings, ScanListener scanListener, WorkSource workSource) {
        startScan(scanSettings, (Executor) null, scanListener, workSource);
    }

    public void startScan(ScanSettings scanSettings, Executor executor, ScanListener scanListener, WorkSource workSource) {
        Objects.requireNonNull(scanListener, "listener cannot be null");
        int addListener = addListener(scanListener, executor);
        if (addListener != 0) {
            validateChannel();
            Bundle bundle = new Bundle();
            bundle.putParcelable("ScanSettings", scanSettings);
            bundle.putParcelable(SCAN_PARAMS_WORK_SOURCE_KEY, workSource);
            bundle.putString(REQUEST_PACKAGE_NAME_KEY, this.mContext.getOpPackageName());
            bundle.putString(REQUEST_FEATURE_ID_KEY, this.mContext.getAttributionTag());
            this.mAsyncChannel.sendMessage(CMD_START_SINGLE_SCAN, 0, addListener, bundle);
        }
    }

    public void stopScan(ScanListener scanListener) {
        Objects.requireNonNull(scanListener, "listener cannot be null");
        int removeListener = removeListener((Object) scanListener);
        if (removeListener != 0) {
            validateChannel();
            Bundle bundle = new Bundle();
            bundle.putString(REQUEST_PACKAGE_NAME_KEY, this.mContext.getOpPackageName());
            bundle.putString(REQUEST_FEATURE_ID_KEY, this.mContext.getAttributionTag());
            this.mAsyncChannel.sendMessage(CMD_STOP_SINGLE_SCAN, 0, removeListener, bundle);
        }
    }

    public List<ScanResult> getSingleScanResults() {
        validateChannel();
        Bundle bundle = new Bundle();
        bundle.putString(REQUEST_PACKAGE_NAME_KEY, this.mContext.getOpPackageName());
        bundle.putString(REQUEST_FEATURE_ID_KEY, this.mContext.getAttributionTag());
        Message sendMessageSynchronously = this.mAsyncChannel.sendMessageSynchronously(CMD_GET_SINGLE_SCAN_RESULTS, 0, 0, bundle);
        if (sendMessageSynchronously.what == 159761) {
            return Arrays.asList(((ParcelableScanResults) sendMessageSynchronously.obj).getResults());
        }
        OperationResult operationResult = (OperationResult) sendMessageSynchronously.obj;
        Log.e(TAG, "Error retrieving SingleScan results reason: " + operationResult.reason + " description: " + operationResult.description);
        return new ArrayList();
    }

    private void startPnoScan(ScanSettings scanSettings, PnoSettings pnoSettings, int i) {
        Bundle bundle = new Bundle();
        scanSettings.isPnoScan = true;
        bundle.putParcelable("ScanSettings", scanSettings);
        bundle.putParcelable(PNO_PARAMS_PNO_SETTINGS_KEY, pnoSettings);
        this.mAsyncChannel.sendMessage(CMD_START_PNO_SCAN, 0, i, bundle);
    }

    public void startConnectedPnoScan(ScanSettings scanSettings, PnoSettings pnoSettings, Executor executor, PnoScanListener pnoScanListener) {
        Objects.requireNonNull(pnoScanListener, "listener cannot be null");
        Objects.requireNonNull(pnoSettings, "pnoSettings cannot be null");
        int addListener = addListener(pnoScanListener, executor);
        if (addListener != 0) {
            validateChannel();
            pnoSettings.isConnected = true;
            startPnoScan(scanSettings, pnoSettings, addListener);
        }
    }

    public void startDisconnectedPnoScan(ScanSettings scanSettings, PnoSettings pnoSettings, Executor executor, PnoScanListener pnoScanListener) {
        Objects.requireNonNull(pnoScanListener, "listener cannot be null");
        Objects.requireNonNull(pnoSettings, "pnoSettings cannot be null");
        int addListener = addListener(pnoScanListener, executor);
        if (addListener != 0) {
            validateChannel();
            pnoSettings.isConnected = false;
            startPnoScan(scanSettings, pnoSettings, addListener);
        }
    }

    public void stopPnoScan(ScanListener scanListener) {
        Objects.requireNonNull(scanListener, "listener cannot be null");
        int removeListener = removeListener((Object) scanListener);
        if (removeListener != 0) {
            validateChannel();
            this.mAsyncChannel.sendMessage(CMD_STOP_PNO_SCAN, 0, removeListener);
        }
    }

    @Deprecated
    public void configureWifiChange(int i, int i2, int i3, int i4, int i5, BssidInfo[] bssidInfoArr) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public void startTrackingWifiChange(WifiChangeListener wifiChangeListener) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public void stopTrackingWifiChange(WifiChangeListener wifiChangeListener) {
        throw new UnsupportedOperationException();
    }

    @SystemApi
    @Deprecated
    public void configureWifiChange(WifiChangeSettings wifiChangeSettings) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public void startTrackingBssids(BssidInfo[] bssidInfoArr, int i, BssidListener bssidListener) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public void stopTrackingBssids(BssidListener bssidListener) {
        throw new UnsupportedOperationException();
    }

    public WifiScanner(Context context, IWifiScanner iWifiScanner, Looper looper) {
        this.mContext = context;
        this.mService = iWifiScanner;
        try {
            Messenger messenger = iWifiScanner.getMessenger();
            if (messenger != null) {
                this.mAsyncChannel = new AsyncChannel();
                ServiceHandler serviceHandler = new ServiceHandler(looper);
                this.mInternalHandler = serviceHandler;
                this.mAsyncChannel.connectSync(this.mContext, (Handler) serviceHandler, messenger);
                this.mAsyncChannel.sendMessage((int) AsyncChannel.CMD_CHANNEL_FULL_CONNECTION);
                return;
            }
            throw new IllegalStateException("getMessenger() returned null!  This is invalid.");
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private void validateChannel() {
        if (this.mAsyncChannel == null) {
            throw new IllegalStateException("No permission to access and change wifi or a bad initialization");
        }
    }

    private int addListener(ActionListener actionListener) {
        return addListener(actionListener, (Executor) null);
    }

    private int addListener(ActionListener actionListener, Executor executor) {
        synchronized (this.mListenerMapLock) {
            boolean z = getListenerKey(actionListener) != 0;
            int putListener = putListener(actionListener);
            if (z) {
                Message.obtain(this.mInternalHandler, CMD_OP_FAILED, 0, putListener, new OperationResult(-5, "Outstanding request with same key not stopped yet")).sendToTarget();
                return 0;
            }
            this.mExecutorMap.put(putListener, executor);
            return putListener;
        }
    }

    private int putListener(Object obj) {
        int i;
        if (obj == null) {
            return 0;
        }
        synchronized (this.mListenerMapLock) {
            do {
                i = this.mListenerKey;
                this.mListenerKey = i + 1;
            } while (i == 0);
            this.mListenerMap.put(i, obj);
        }
        return i;
    }

    private static class ListenerWithExecutor {
        final Executor mExecutor;
        final Object mListener;

        ListenerWithExecutor(Object obj, Executor executor) {
            this.mListener = obj;
            this.mExecutor = executor;
        }
    }

    /* access modifiers changed from: private */
    public ListenerWithExecutor getListenerWithExecutor(int i) {
        ListenerWithExecutor listenerWithExecutor;
        if (i == 0) {
            return new ListenerWithExecutor((Object) null, (Executor) null);
        }
        synchronized (this.mListenerMapLock) {
            listenerWithExecutor = new ListenerWithExecutor(this.mListenerMap.get(i), this.mExecutorMap.get(i));
        }
        return listenerWithExecutor;
    }

    private int getListenerKey(Object obj) {
        if (obj == null) {
            return 0;
        }
        synchronized (this.mListenerMapLock) {
            int indexOfValue = this.mListenerMap.indexOfValue(obj);
            if (indexOfValue == -1) {
                return 0;
            }
            int keyAt = this.mListenerMap.keyAt(indexOfValue);
            return keyAt;
        }
    }

    /* access modifiers changed from: private */
    public Object removeListener(int i) {
        Object obj;
        if (i == 0) {
            return null;
        }
        synchronized (this.mListenerMapLock) {
            obj = this.mListenerMap.get(i);
            this.mListenerMap.remove(i);
            this.mExecutorMap.remove(i);
        }
        return obj;
    }

    private int removeListener(Object obj) {
        int listenerKey = getListenerKey(obj);
        if (listenerKey == 0) {
            Log.e(TAG, "listener cannot be found");
            return listenerKey;
        }
        synchronized (this.mListenerMapLock) {
            this.mListenerMap.remove(listenerKey);
            this.mExecutorMap.remove(listenerKey);
        }
        return listenerKey;
    }

    public Handler getInternalHandler() {
        return this.mInternalHandler;
    }

    public static class OperationResult implements Parcelable {
        public static final Parcelable.Creator<OperationResult> CREATOR = new Parcelable.Creator<OperationResult>() {
            public OperationResult createFromParcel(Parcel parcel) {
                return new OperationResult(parcel.readInt(), parcel.readString());
            }

            public OperationResult[] newArray(int i) {
                return new OperationResult[i];
            }
        };
        public String description;
        public int reason;

        public int describeContents() {
            return 0;
        }

        public OperationResult(int i, String str) {
            this.reason = i;
            this.description = str;
        }

        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(this.reason);
            parcel.writeString(this.description);
        }
    }

    private class ServiceHandler extends Handler {
        ServiceHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 69634) {
                return;
            }
            if (i == 69636) {
                Log.e(WifiScanner.TAG, "Channel connection lost");
                WifiScanner.this.mAsyncChannel = null;
            } else if (WifiScanner.this.mAsyncChannel == null) {
                Log.e(WifiScanner.TAG, "Channel was already disconnected!");
            } else {
                ListenerWithExecutor r0 = WifiScanner.this.getListenerWithExecutor(message.arg2);
                Object obj = r0.mListener;
                if (obj != null) {
                    Executor executor = r0.mExecutor;
                    if (executor == null) {
                        executor = new SynchronousExecutor();
                    }
                    switch (message.what) {
                        case WifiScanner.CMD_SCAN_RESULT /*159749*/:
                            Binder.clearCallingIdentity();
                            executor.execute(new WifiScanner$ServiceHandler$$ExternalSyntheticLambda2((ScanListener) obj, (ParcelableScanData) message.obj));
                            return;
                        case WifiScanner.CMD_OP_SUCCEEDED /*159761*/:
                            ActionListener actionListener = (ActionListener) obj;
                            Binder.clearCallingIdentity();
                            Objects.requireNonNull(actionListener);
                            executor.execute(new WifiScanner$ServiceHandler$$ExternalSyntheticLambda0(actionListener));
                            return;
                        case WifiScanner.CMD_OP_FAILED /*159762*/:
                            Object unused = WifiScanner.this.removeListener(message.arg2);
                            Binder.clearCallingIdentity();
                            executor.execute(new WifiScanner$ServiceHandler$$ExternalSyntheticLambda1((ActionListener) obj, (OperationResult) message.obj));
                            return;
                        case WifiScanner.CMD_FULL_SCAN_RESULT /*159764*/:
                            Binder.clearCallingIdentity();
                            executor.execute(new WifiScanner$ServiceHandler$$ExternalSyntheticLambda3((ScanListener) obj, (ScanResult) message.obj));
                            return;
                        case WifiScanner.CMD_SINGLE_SCAN_COMPLETED /*159767*/:
                            Object unused2 = WifiScanner.this.removeListener(message.arg2);
                            return;
                        case WifiScanner.CMD_PNO_NETWORK_FOUND /*159770*/:
                            Binder.clearCallingIdentity();
                            executor.execute(new WifiScanner$ServiceHandler$$ExternalSyntheticLambda4((PnoScanListener) obj, (ParcelableScanResults) message.obj));
                            return;
                        default:
                            return;
                    }
                }
            }
        }
    }
}
