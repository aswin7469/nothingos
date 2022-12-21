package com.android.systemui.statusbar.connectivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkScoreManager;
import android.net.wifi.WifiManager;
import android.os.Handler;
import com.android.settingslib.wifi.WifiStatusTracker;
import com.android.systemui.dagger.qualifiers.Main;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0000\u0018\u00002\u00020\u0001B3\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\b\b\u0001\u0010\n\u001a\u00020\u000b¢\u0006\u0002\u0010\fJ\u001a\u0010\r\u001a\u00020\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u000bR\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0004\u001a\u0004\u0018\u00010\u0005X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0012"}, mo64987d2 = {"Lcom/android/systemui/statusbar/connectivity/WifiStatusTrackerFactory;", "", "mContext", "Landroid/content/Context;", "mWifiManager", "Landroid/net/wifi/WifiManager;", "mNetworkScoreManager", "Landroid/net/NetworkScoreManager;", "mConnectivityManager", "Landroid/net/ConnectivityManager;", "mMainHandler", "Landroid/os/Handler;", "(Landroid/content/Context;Landroid/net/wifi/WifiManager;Landroid/net/NetworkScoreManager;Landroid/net/ConnectivityManager;Landroid/os/Handler;)V", "createTracker", "Lcom/android/settingslib/wifi/WifiStatusTracker;", "callback", "Ljava/lang/Runnable;", "bgHandler", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: WifiStatusTrackerFactory.kt */
public final class WifiStatusTrackerFactory {
    private final ConnectivityManager mConnectivityManager;
    private final Context mContext;
    private final Handler mMainHandler;
    private final NetworkScoreManager mNetworkScoreManager;
    private final WifiManager mWifiManager;

    @Inject
    public WifiStatusTrackerFactory(Context context, WifiManager wifiManager, NetworkScoreManager networkScoreManager, ConnectivityManager connectivityManager, @Main Handler handler) {
        Intrinsics.checkNotNullParameter(context, "mContext");
        Intrinsics.checkNotNullParameter(networkScoreManager, "mNetworkScoreManager");
        Intrinsics.checkNotNullParameter(connectivityManager, "mConnectivityManager");
        Intrinsics.checkNotNullParameter(handler, "mMainHandler");
        this.mContext = context;
        this.mWifiManager = wifiManager;
        this.mNetworkScoreManager = networkScoreManager;
        this.mConnectivityManager = connectivityManager;
        this.mMainHandler = handler;
    }

    public final WifiStatusTracker createTracker(Runnable runnable, Handler handler) {
        return new WifiStatusTracker(this.mContext, this.mWifiManager, this.mNetworkScoreManager, this.mConnectivityManager, runnable, this.mMainHandler, handler);
    }
}
