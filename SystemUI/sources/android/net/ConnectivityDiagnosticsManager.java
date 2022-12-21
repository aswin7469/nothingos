package android.net;

import android.content.Context;
import android.net.IConnectivityDiagnosticsCallback;
import android.os.Binder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.os.RemoteException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class ConnectivityDiagnosticsManager {
    public static final Map<ConnectivityDiagnosticsCallback, ConnectivityDiagnosticsBinder> sCallbacks = new ConcurrentHashMap();
    private final Context mContext;
    private final IConnectivityManager mService;

    public static abstract class ConnectivityDiagnosticsCallback {
        public void onConnectivityReportAvailable(ConnectivityReport connectivityReport) {
        }

        public void onDataStallSuspected(DataStallReport dataStallReport) {
        }

        public void onNetworkConnectivityReported(Network network, boolean z) {
        }
    }

    public ConnectivityDiagnosticsManager(Context context, IConnectivityManager iConnectivityManager) {
        this.mContext = (Context) Objects.requireNonNull(context, "missing context");
        this.mService = (IConnectivityManager) Objects.requireNonNull(iConnectivityManager, "missing IConnectivityManager");
    }

    public static boolean persistableBundleEquals(PersistableBundle persistableBundle, PersistableBundle persistableBundle2) {
        if (persistableBundle == persistableBundle2) {
            return true;
        }
        if (persistableBundle == null || persistableBundle2 == null || !Objects.equals(persistableBundle.keySet(), persistableBundle2.keySet())) {
            return false;
        }
        for (String str : persistableBundle.keySet()) {
            if (!Objects.equals(persistableBundle.get(str), persistableBundle2.get(str))) {
                return false;
            }
        }
        return true;
    }

    public static final class ConnectivityReport implements Parcelable {
        public static final Parcelable.Creator<ConnectivityReport> CREATOR = new Parcelable.Creator<ConnectivityReport>() {
            public ConnectivityReport createFromParcel(Parcel parcel) {
                return new ConnectivityReport((Network) parcel.readParcelable((ClassLoader) null), parcel.readLong(), (LinkProperties) parcel.readParcelable((ClassLoader) null), (NetworkCapabilities) parcel.readParcelable((ClassLoader) null), (PersistableBundle) parcel.readParcelable((ClassLoader) null));
            }

            public ConnectivityReport[] newArray(int i) {
                return new ConnectivityReport[i];
            }
        };
        public static final String KEY_NETWORK_PROBES_ATTEMPTED_BITMASK = "networkProbesAttempted";
        public static final String KEY_NETWORK_PROBES_SUCCEEDED_BITMASK = "networkProbesSucceeded";
        public static final String KEY_NETWORK_VALIDATION_RESULT = "networkValidationResult";
        public static final int NETWORK_PROBE_DNS = 4;
        public static final int NETWORK_PROBE_FALLBACK = 32;
        public static final int NETWORK_PROBE_HTTP = 8;
        public static final int NETWORK_PROBE_HTTPS = 16;
        public static final int NETWORK_PROBE_PRIVATE_DNS = 64;
        public static final int NETWORK_VALIDATION_RESULT_INVALID = 0;
        public static final int NETWORK_VALIDATION_RESULT_PARTIALLY_VALID = 2;
        public static final int NETWORK_VALIDATION_RESULT_SKIPPED = 3;
        public static final int NETWORK_VALIDATION_RESULT_VALID = 1;
        private final PersistableBundle mAdditionalInfo;
        private final LinkProperties mLinkProperties;
        private final Network mNetwork;
        private final NetworkCapabilities mNetworkCapabilities;
        private final long mReportTimestamp;

        @Retention(RetentionPolicy.SOURCE)
        public @interface ConnectivityReportBundleKeys {
        }

        @Retention(RetentionPolicy.SOURCE)
        public @interface NetworkProbe {
        }

        @Retention(RetentionPolicy.SOURCE)
        public @interface NetworkValidationResult {
        }

        public int describeContents() {
            return 0;
        }

        public ConnectivityReport(Network network, long j, LinkProperties linkProperties, NetworkCapabilities networkCapabilities, PersistableBundle persistableBundle) {
            this.mNetwork = network;
            this.mReportTimestamp = j;
            this.mLinkProperties = new LinkProperties(linkProperties);
            this.mNetworkCapabilities = new NetworkCapabilities(networkCapabilities);
            this.mAdditionalInfo = persistableBundle;
        }

        public Network getNetwork() {
            return this.mNetwork;
        }

        public long getReportTimestamp() {
            return this.mReportTimestamp;
        }

        public LinkProperties getLinkProperties() {
            return new LinkProperties(this.mLinkProperties);
        }

        public NetworkCapabilities getNetworkCapabilities() {
            return new NetworkCapabilities(this.mNetworkCapabilities);
        }

        public PersistableBundle getAdditionalInfo() {
            return new PersistableBundle(this.mAdditionalInfo);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof ConnectivityReport)) {
                return false;
            }
            ConnectivityReport connectivityReport = (ConnectivityReport) obj;
            if (this.mReportTimestamp != connectivityReport.mReportTimestamp || !this.mNetwork.equals(connectivityReport.mNetwork) || !this.mLinkProperties.equals(connectivityReport.mLinkProperties) || !this.mNetworkCapabilities.equals(connectivityReport.mNetworkCapabilities) || !ConnectivityDiagnosticsManager.persistableBundleEquals(this.mAdditionalInfo, connectivityReport.mAdditionalInfo)) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return Objects.hash(this.mNetwork, Long.valueOf(this.mReportTimestamp), this.mLinkProperties, this.mNetworkCapabilities, this.mAdditionalInfo);
        }

        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeParcelable(this.mNetwork, i);
            parcel.writeLong(this.mReportTimestamp);
            parcel.writeParcelable(this.mLinkProperties, i);
            parcel.writeParcelable(this.mNetworkCapabilities, i);
            parcel.writeParcelable(this.mAdditionalInfo, i);
        }
    }

    public static final class DataStallReport implements Parcelable {
        public static final Parcelable.Creator<DataStallReport> CREATOR = new Parcelable.Creator<DataStallReport>() {
            public DataStallReport createFromParcel(Parcel parcel) {
                return new DataStallReport((Network) parcel.readParcelable((ClassLoader) null), parcel.readLong(), parcel.readInt(), (LinkProperties) parcel.readParcelable((ClassLoader) null), (NetworkCapabilities) parcel.readParcelable((ClassLoader) null), (PersistableBundle) parcel.readParcelable((ClassLoader) null));
            }

            public DataStallReport[] newArray(int i) {
                return new DataStallReport[i];
            }
        };
        public static final int DETECTION_METHOD_DNS_EVENTS = 1;
        public static final int DETECTION_METHOD_TCP_METRICS = 2;
        public static final String KEY_DNS_CONSECUTIVE_TIMEOUTS = "dnsConsecutiveTimeouts";
        public static final String KEY_TCP_METRICS_COLLECTION_PERIOD_MILLIS = "tcpMetricsCollectionPeriodMillis";
        public static final String KEY_TCP_PACKET_FAIL_RATE = "tcpPacketFailRate";
        private final int mDetectionMethod;
        private final LinkProperties mLinkProperties;
        private final Network mNetwork;
        private final NetworkCapabilities mNetworkCapabilities;
        private long mReportTimestamp;
        private final PersistableBundle mStallDetails;

        @Retention(RetentionPolicy.SOURCE)
        public @interface DataStallReportBundleKeys {
        }

        @Retention(RetentionPolicy.SOURCE)
        public @interface DetectionMethod {
        }

        public int describeContents() {
            return 0;
        }

        public DataStallReport(Network network, long j, int i, LinkProperties linkProperties, NetworkCapabilities networkCapabilities, PersistableBundle persistableBundle) {
            this.mNetwork = network;
            this.mReportTimestamp = j;
            this.mDetectionMethod = i;
            this.mLinkProperties = new LinkProperties(linkProperties);
            this.mNetworkCapabilities = new NetworkCapabilities(networkCapabilities);
            this.mStallDetails = persistableBundle;
        }

        public Network getNetwork() {
            return this.mNetwork;
        }

        public long getReportTimestamp() {
            return this.mReportTimestamp;
        }

        public int getDetectionMethod() {
            return this.mDetectionMethod;
        }

        public LinkProperties getLinkProperties() {
            return new LinkProperties(this.mLinkProperties);
        }

        public NetworkCapabilities getNetworkCapabilities() {
            return new NetworkCapabilities(this.mNetworkCapabilities);
        }

        public PersistableBundle getStallDetails() {
            return new PersistableBundle(this.mStallDetails);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof DataStallReport)) {
                return false;
            }
            DataStallReport dataStallReport = (DataStallReport) obj;
            if (this.mReportTimestamp != dataStallReport.mReportTimestamp || this.mDetectionMethod != dataStallReport.mDetectionMethod || !this.mNetwork.equals(dataStallReport.mNetwork) || !this.mLinkProperties.equals(dataStallReport.mLinkProperties) || !this.mNetworkCapabilities.equals(dataStallReport.mNetworkCapabilities) || !ConnectivityDiagnosticsManager.persistableBundleEquals(this.mStallDetails, dataStallReport.mStallDetails)) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return Objects.hash(this.mNetwork, Long.valueOf(this.mReportTimestamp), Integer.valueOf(this.mDetectionMethod), this.mLinkProperties, this.mNetworkCapabilities, this.mStallDetails);
        }

        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeParcelable(this.mNetwork, i);
            parcel.writeLong(this.mReportTimestamp);
            parcel.writeInt(this.mDetectionMethod);
            parcel.writeParcelable(this.mLinkProperties, i);
            parcel.writeParcelable(this.mNetworkCapabilities, i);
            parcel.writeParcelable(this.mStallDetails, i);
        }
    }

    public static class ConnectivityDiagnosticsBinder extends IConnectivityDiagnosticsCallback.Stub {
        private final ConnectivityDiagnosticsCallback mCb;
        private final Executor mExecutor;

        public ConnectivityDiagnosticsBinder(ConnectivityDiagnosticsCallback connectivityDiagnosticsCallback, Executor executor) {
            this.mCb = connectivityDiagnosticsCallback;
            this.mExecutor = executor;
        }

        public void onConnectivityReportAvailable(ConnectivityReport connectivityReport) {
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                this.mExecutor.execute(new C0035xad5a7b63(this, connectivityReport));
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onConnectivityReportAvailable$0$android-net-ConnectivityDiagnosticsManager$ConnectivityDiagnosticsBinder */
        public /* synthetic */ void mo1867xe6f06637(ConnectivityReport connectivityReport) {
            this.mCb.onConnectivityReportAvailable(connectivityReport);
        }

        public void onDataStallSuspected(DataStallReport dataStallReport) {
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                this.mExecutor.execute(new C0036xad5a7b64(this, dataStallReport));
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onDataStallSuspected$1$android-net-ConnectivityDiagnosticsManager$ConnectivityDiagnosticsBinder */
        public /* synthetic */ void mo1868x89b2b17c(DataStallReport dataStallReport) {
            this.mCb.onDataStallSuspected(dataStallReport);
        }

        public void onNetworkConnectivityReported(Network network, boolean z) {
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                this.mExecutor.execute(new C0034xad5a7b62(this, network, z));
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onNetworkConnectivityReported$2$android-net-ConnectivityDiagnosticsManager$ConnectivityDiagnosticsBinder */
        public /* synthetic */ void mo1869x912b327b(Network network, boolean z) {
            this.mCb.onNetworkConnectivityReported(network, z);
        }
    }

    public void registerConnectivityDiagnosticsCallback(NetworkRequest networkRequest, Executor executor, ConnectivityDiagnosticsCallback connectivityDiagnosticsCallback) {
        ConnectivityDiagnosticsBinder connectivityDiagnosticsBinder = new ConnectivityDiagnosticsBinder(connectivityDiagnosticsCallback, executor);
        if (sCallbacks.putIfAbsent(connectivityDiagnosticsCallback, connectivityDiagnosticsBinder) == null) {
            try {
                this.mService.registerConnectivityDiagnosticsCallback(connectivityDiagnosticsBinder, networkRequest, this.mContext.getOpPackageName());
            } catch (RemoteException e) {
                e.rethrowFromSystemServer();
            }
        } else {
            throw new IllegalArgumentException("Callback is currently registered");
        }
    }

    public void unregisterConnectivityDiagnosticsCallback(ConnectivityDiagnosticsCallback connectivityDiagnosticsCallback) {
        ConnectivityDiagnosticsBinder remove = sCallbacks.remove(connectivityDiagnosticsCallback);
        if (remove != null) {
            try {
                this.mService.unregisterConnectivityDiagnosticsCallback(remove);
            } catch (RemoteException e) {
                e.rethrowFromSystemServer();
            }
        }
    }
}
