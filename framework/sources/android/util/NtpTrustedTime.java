package android.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.SntpClient;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.provider.Settings;
import android.text.TextUtils;
import com.android.internal.R;
import java.util.Objects;
import java.util.function.Supplier;
/* loaded from: classes3.dex */
public class NtpTrustedTime implements TrustedTime {
    private static final String BACKUP_SERVER = "persist.backup.ntpServer";
    private static final boolean LOGD = false;
    private static final String TAG = "NtpTrustedTime";
    private static String mBackupServer = "";
    private static int mNtpRetries = 0;
    private static int mNtpRetriesMax = 0;
    private static NtpTrustedTime sSingleton;
    private final Context mContext;
    private volatile TimeResult mTimeResult;
    private final Supplier<ConnectivityManager> mConnectivityManagerSupplier = new Supplier<ConnectivityManager>() { // from class: android.util.NtpTrustedTime.1
        private ConnectivityManager mConnectivityManager;

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.function.Supplier
        /* renamed from: get */
        public synchronized ConnectivityManager mo2775get() {
            if (this.mConnectivityManager == null) {
                this.mConnectivityManager = (ConnectivityManager) NtpTrustedTime.this.mContext.getSystemService(ConnectivityManager.class);
            }
            return this.mConnectivityManager;
        }
    };
    private boolean mBackupmode = false;

    /* loaded from: classes3.dex */
    public static class TimeResult {
        private final long mCertaintyMillis;
        private final long mElapsedRealtimeMillis;
        private final long mTimeMillis;

        public TimeResult(long timeMillis, long elapsedRealtimeMillis, long certaintyMillis) {
            this.mTimeMillis = timeMillis;
            this.mElapsedRealtimeMillis = elapsedRealtimeMillis;
            this.mCertaintyMillis = certaintyMillis;
        }

        public long getTimeMillis() {
            return this.mTimeMillis;
        }

        public long getElapsedRealtimeMillis() {
            return this.mElapsedRealtimeMillis;
        }

        public long getCertaintyMillis() {
            return this.mCertaintyMillis;
        }

        public long currentTimeMillis() {
            return this.mTimeMillis + getAgeMillis();
        }

        public long getAgeMillis() {
            return SystemClock.elapsedRealtime() - this.mElapsedRealtimeMillis;
        }

        public String toString() {
            return "TimeResult{mTimeMillis=" + this.mTimeMillis + ", mElapsedRealtimeMillis=" + this.mElapsedRealtimeMillis + ", mCertaintyMillis=" + this.mCertaintyMillis + '}';
        }
    }

    private NtpTrustedTime(Context context) {
        Objects.requireNonNull(context);
        this.mContext = context;
    }

    public static synchronized NtpTrustedTime getInstance(Context context) {
        NtpTrustedTime ntpTrustedTime;
        int retryMax;
        synchronized (NtpTrustedTime.class) {
            if (sSingleton == null) {
                Resources res = context.getResources();
                ContentResolver resolver = context.getContentResolver();
                Context appContext = context.getApplicationContext();
                sSingleton = new NtpTrustedTime(appContext);
                String sserver_prop = Settings.Global.getString(resolver, Settings.Global.NTP_SERVER_2);
                String secondServer_prop = (sserver_prop == null || sserver_prop.length() <= 0) ? BACKUP_SERVER : sserver_prop;
                String backupServer = SystemProperties.get(secondServer_prop);
                if (backupServer != null && backupServer.length() > 0 && (retryMax = res.getInteger(R.integer.config_ntpRetry)) > 0) {
                    mNtpRetriesMax = retryMax;
                    mBackupServer = backupServer.trim().replace("\"", "");
                }
            }
            ntpTrustedTime = sSingleton;
        }
        return ntpTrustedTime;
    }

    @Override // android.util.TrustedTime
    public boolean forceRefresh() {
        if (hasCache()) {
            return forceSync();
        }
        return false;
    }

    @Override // android.util.TrustedTime
    public boolean forceSync() {
        synchronized (this) {
            NtpConnectionInfo connectionInfo = getNtpConnectionInfo();
            if (connectionInfo == null) {
                return false;
            }
            ConnectivityManager connectivityManager = this.mConnectivityManagerSupplier.get();
            if (connectivityManager == null) {
                return false;
            }
            Network network = connectivityManager.getActiveNetwork();
            NetworkInfo ni = connectivityManager.getNetworkInfo(network);
            if (ni != null && ni.isConnected()) {
                SntpClient client = new SntpClient();
                String serverName = connectionInfo.getServer();
                int timeoutMillis = connectionInfo.getTimeoutMillis();
                if (getBackupmode()) {
                    setBackupmode(false);
                    serverName = mBackupServer;
                }
                if (client.requestTime(serverName, timeoutMillis, network)) {
                    long ntpCertainty = client.getRoundTripTime() / 2;
                    this.mTimeResult = new TimeResult(client.getNtpTime(), client.getNtpTimeReference(), ntpCertainty);
                    return true;
                }
                countInBackupmode();
                return false;
            }
            return false;
        }
    }

    @Override // android.util.TrustedTime
    @Deprecated
    public boolean hasCache() {
        return this.mTimeResult != null;
    }

    @Override // android.util.TrustedTime
    @Deprecated
    public long getCacheAge() {
        TimeResult timeResult = this.mTimeResult;
        if (timeResult != null) {
            return SystemClock.elapsedRealtime() - timeResult.getElapsedRealtimeMillis();
        }
        return Long.MAX_VALUE;
    }

    @Override // android.util.TrustedTime
    @Deprecated
    public long currentTimeMillis() {
        TimeResult timeResult = this.mTimeResult;
        if (timeResult == null) {
            throw new IllegalStateException("Missing authoritative time source");
        }
        return timeResult.currentTimeMillis();
    }

    @Deprecated
    public long getCachedNtpTime() {
        TimeResult timeResult = this.mTimeResult;
        if (timeResult == null) {
            return 0L;
        }
        return timeResult.getTimeMillis();
    }

    @Deprecated
    public long getCachedNtpTimeReference() {
        TimeResult timeResult = this.mTimeResult;
        if (timeResult == null) {
            return 0L;
        }
        return timeResult.getElapsedRealtimeMillis();
    }

    public TimeResult getCachedTimeResult() {
        return this.mTimeResult;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class NtpConnectionInfo {
        private final String mServer;
        private final int mTimeoutMillis;

        NtpConnectionInfo(String server, int timeoutMillis) {
            Objects.requireNonNull(server);
            this.mServer = server;
            this.mTimeoutMillis = timeoutMillis;
        }

        public String getServer() {
            return this.mServer;
        }

        int getTimeoutMillis() {
            return this.mTimeoutMillis;
        }
    }

    private NtpConnectionInfo getNtpConnectionInfo() {
        ContentResolver resolver = this.mContext.getContentResolver();
        Resources res = this.mContext.getResources();
        String defaultServer = res.getString(R.string.config_ntpServer);
        int defaultTimeoutMillis = res.getInteger(R.integer.config_ntpTimeout);
        String secureServer = Settings.Global.getString(resolver, Settings.Global.NTP_SERVER);
        int timeoutMillis = Settings.Global.getInt(resolver, Settings.Global.NTP_TIMEOUT, defaultTimeoutMillis);
        String server = secureServer != null ? secureServer : defaultServer;
        if (TextUtils.isEmpty(server)) {
            return null;
        }
        return new NtpConnectionInfo(server, timeoutMillis);
    }

    public void setBackupmode(boolean mode) {
        if (isBackupSupported()) {
            this.mBackupmode = mode;
        }
    }

    private boolean getBackupmode() {
        return this.mBackupmode;
    }

    private boolean isBackupSupported() {
        String str;
        return (mNtpRetriesMax <= 0 || (str = mBackupServer) == null || str.length() == 0) ? false : true;
    }

    private void countInBackupmode() {
        if (isBackupSupported()) {
            int i = mNtpRetries + 1;
            mNtpRetries = i;
            if (i >= mNtpRetriesMax) {
                mNtpRetries = 0;
                setBackupmode(true);
            }
        }
    }
}
