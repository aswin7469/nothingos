package com.android.wifitrackerlib;

import android.content.Context;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;
import com.android.wifitrackerlib.WifiEntry;
import java.util.StringJoiner;

public class MergedCarrierEntry extends WifiEntry {
    static final String KEY_PREFIX = "MergedCarrierEntry:";
    private final Context mContext;
    boolean mIsCellDefaultRoute;
    private final String mKey;
    private final int mSubscriptionId;

    MergedCarrierEntry(Handler handler, WifiManager wifiManager, boolean z, Context context, int i) throws IllegalArgumentException {
        super(handler, wifiManager, z);
        this.mContext = context;
        this.mSubscriptionId = i;
        this.mKey = KEY_PREFIX + i;
    }

    public String getKey() {
        return this.mKey;
    }

    public String getSummary(boolean z) {
        StringJoiner stringJoiner = new StringJoiner(this.mContext.getString(C3341R.string.wifitrackerlib_summary_separator));
        if (!z) {
            String verboseLoggingDescription = Utils.getVerboseLoggingDescription(this);
            if (!TextUtils.isEmpty(verboseLoggingDescription)) {
                stringJoiner.add(verboseLoggingDescription);
            }
        }
        return stringJoiner.toString();
    }

    public synchronized String getSsid() {
        if (this.mWifiInfo == null) {
            return null;
        }
        return WifiInfo.sanitizeSsid(this.mWifiInfo.getSSID());
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x001c, code lost:
        return null;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized java.lang.String getMacAddress() {
        /*
            r2 = this;
            monitor-enter(r2)
            android.net.wifi.WifiInfo r0 = r2.mWifiInfo     // Catch:{ all -> 0x001e }
            if (r0 == 0) goto L_0x001b
            android.net.wifi.WifiInfo r0 = r2.mWifiInfo     // Catch:{ all -> 0x001e }
            java.lang.String r0 = r0.getMacAddress()     // Catch:{ all -> 0x001e }
            boolean r1 = android.text.TextUtils.isEmpty(r0)     // Catch:{ all -> 0x001e }
            if (r1 != 0) goto L_0x001b
            java.lang.String r1 = "02:00:00:00:00:00"
            boolean r1 = android.text.TextUtils.equals(r0, r1)     // Catch:{ all -> 0x001e }
            if (r1 != 0) goto L_0x001b
            monitor-exit(r2)
            return r0
        L_0x001b:
            monitor-exit(r2)
            r2 = 0
            return r2
        L_0x001e:
            r0 = move-exception
            monitor-exit(r2)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wifitrackerlib.MergedCarrierEntry.getMacAddress():java.lang.String");
    }

    public synchronized boolean canConnect() {
        return getConnectedState() == 0 && !this.mIsCellDefaultRoute;
    }

    public synchronized void connect(WifiEntry.ConnectCallback connectCallback) {
        connect(connectCallback, true);
    }

    public synchronized void connect(WifiEntry.ConnectCallback connectCallback, boolean z) {
        this.mConnectCallback = connectCallback;
        this.mWifiManager.startRestrictingAutoJoinToSubscriptionId(this.mSubscriptionId);
        if (z) {
            Toast.makeText(this.mContext, C3341R.string.wifitrackerlib_wifi_wont_autoconnect_for_now, 0).show();
        }
        if (this.mConnectCallback != null) {
            this.mCallbackHandler.post(new MergedCarrierEntry$$ExternalSyntheticLambda1(this));
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$connect$0$com-android-wifitrackerlib-MergedCarrierEntry  reason: not valid java name */
    public /* synthetic */ void m3371lambda$connect$0$comandroidwifitrackerlibMergedCarrierEntry() {
        WifiEntry.ConnectCallback connectCallback = this.mConnectCallback;
        if (connectCallback != null) {
            connectCallback.onConnectResult(0);
        }
    }

    public boolean canDisconnect() {
        return getConnectedState() == 2;
    }

    public synchronized void disconnect(WifiEntry.DisconnectCallback disconnectCallback) {
        this.mDisconnectCallback = disconnectCallback;
        this.mWifiManager.stopRestrictingAutoJoinToSubscriptionId();
        this.mWifiManager.startScan();
        if (this.mDisconnectCallback != null) {
            this.mCallbackHandler.post(new MergedCarrierEntry$$ExternalSyntheticLambda0(this));
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$disconnect$1$com-android-wifitrackerlib-MergedCarrierEntry */
    public /* synthetic */ void mo47790xf1158ee9() {
        WifiEntry.DisconnectCallback disconnectCallback = this.mDisconnectCallback;
        if (disconnectCallback != null) {
            disconnectCallback.onDisconnectResult(0);
        }
    }

    /* access modifiers changed from: protected */
    public boolean connectionInfoMatches(WifiInfo wifiInfo, NetworkInfo networkInfo) {
        return wifiInfo.isCarrierMerged() && this.mSubscriptionId == wifiInfo.getSubscriptionId();
    }

    public boolean isEnabled() {
        return this.mWifiManager.isCarrierNetworkOffloadEnabled(this.mSubscriptionId, true);
    }

    public void setEnabled(boolean z) {
        this.mWifiManager.setCarrierNetworkOffloadEnabled(this.mSubscriptionId, true, z);
        if (!z) {
            this.mWifiManager.stopRestrictingAutoJoinToSubscriptionId();
            this.mWifiManager.startScan();
        }
    }

    /* access modifiers changed from: package-private */
    public int getSubscriptionId() {
        return this.mSubscriptionId;
    }

    /* access modifiers changed from: package-private */
    public synchronized void updateIsCellDefaultRoute(boolean z) {
        this.mIsCellDefaultRoute = z;
        notifyOnUpdated();
    }
}
