package android.net.util;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.net.ConnectivityResources;
import android.net.ConnectivitySettingsManager;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import android.util.Log;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;

public class MultinetworkPolicyTracker {
    /* access modifiers changed from: private */
    public static String TAG = "MultinetworkPolicyTracker";
    /* access modifiers changed from: private */
    public int mActiveSubId;
    private volatile boolean mAvoidBadWifi;
    private final Runnable mAvoidBadWifiCallback;
    private final BroadcastReceiver mBroadcastReceiver;
    private final Context mContext;
    private final Handler mHandler;
    private volatile int mMeteredMultipathPreference;
    private final ContentResolver mResolver;
    private final ConnectivityResources mResources;
    private final SettingObserver mSettingObserver;
    /* access modifiers changed from: private */
    public final List<Uri> mSettingsUris;
    private volatile long mTestAllowBadWifiUntilMs;

    private static class HandlerExecutor implements Executor {
        private final Handler mHandler;

        HandlerExecutor(Handler handler) {
            this.mHandler = handler;
        }

        public void execute(Runnable runnable) {
            if (!this.mHandler.post(runnable)) {
                throw new RejectedExecutionException(this.mHandler + " is shutting down");
            }
        }
    }

    protected class ActiveDataSubscriptionIdListener extends TelephonyCallback implements TelephonyCallback.ActiveDataSubscriptionIdListener {
        protected ActiveDataSubscriptionIdListener() {
        }

        public void onActiveDataSubscriptionIdChanged(int i) {
            MultinetworkPolicyTracker.this.mActiveSubId = i;
            MultinetworkPolicyTracker.this.reevaluateInternal();
        }
    }

    public MultinetworkPolicyTracker(Context context, Handler handler) {
        this(context, handler, (Runnable) null);
    }

    public MultinetworkPolicyTracker(Context context, Handler handler, Runnable runnable) {
        this.mAvoidBadWifi = true;
        this.mActiveSubId = -1;
        this.mTestAllowBadWifiUntilMs = 0;
        this.mContext = context;
        this.mResources = new ConnectivityResources(context);
        this.mHandler = handler;
        this.mAvoidBadWifiCallback = runnable;
        this.mSettingsUris = Arrays.asList(Settings.Global.getUriFor(ConnectivitySettingsManager.NETWORK_AVOID_BAD_WIFI), Settings.Global.getUriFor(ConnectivitySettingsManager.NETWORK_METERED_MULTIPATH_PREFERENCE));
        this.mResolver = context.getContentResolver();
        this.mSettingObserver = new SettingObserver();
        this.mBroadcastReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                MultinetworkPolicyTracker.this.reevaluateInternal();
            }
        };
        ((TelephonyManager) context.getSystemService(TelephonyManager.class)).registerTelephonyCallback(new HandlerExecutor(handler), new ActiveDataSubscriptionIdListener());
        updateAvoidBadWifi();
        updateMeteredMultipathPreference();
    }

    public void start() {
        for (Uri registerContentObserver : this.mSettingsUris) {
            this.mResolver.registerContentObserver(registerContentObserver, false, this.mSettingObserver);
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.CONFIGURATION_CHANGED");
        this.mContext.registerReceiverForAllUsers(this.mBroadcastReceiver, intentFilter, (String) null, this.mHandler);
        reevaluate();
    }

    public void shutdown() {
        this.mResolver.unregisterContentObserver(this.mSettingObserver);
        this.mContext.unregisterReceiver(this.mBroadcastReceiver);
    }

    public boolean getAvoidBadWifi() {
        return this.mAvoidBadWifi;
    }

    public int getMeteredMultipathPreference() {
        return this.mMeteredMultipathPreference;
    }

    public boolean configRestrictsAvoidBadWifi() {
        if (this.mTestAllowBadWifiUntilMs > 0 && this.mTestAllowBadWifiUntilMs > System.currentTimeMillis()) {
            return true;
        }
        if (getResourcesForActiveSubId().getInteger(this.mResources.get().getIdentifier("config_networkAvoidBadWifi", "integer", this.mResources.getResourcesContext().getPackageName())) == 0) {
            return true;
        }
        return false;
    }

    public void setTestAllowBadWifiUntil(long j) {
        String str = TAG;
        Log.d(str, "setTestAllowBadWifiUntil: " + j);
        this.mTestAllowBadWifiUntilMs = j;
        reevaluateInternal();
    }

    /* access modifiers changed from: protected */
    public Resources getResourcesForActiveSubId() {
        return SubscriptionManager.getResourcesForSubId(this.mResources.getResourcesContext(), this.mActiveSubId);
    }

    public boolean shouldNotifyWifiUnvalidated() {
        return configRestrictsAvoidBadWifi() && getAvoidBadWifiSetting() == null;
    }

    public String getAvoidBadWifiSetting() {
        return Settings.Global.getString(this.mResolver, ConnectivitySettingsManager.NETWORK_AVOID_BAD_WIFI);
    }

    public void reevaluate() {
        this.mHandler.post(new MultinetworkPolicyTracker$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    public void reevaluateInternal() {
        Runnable runnable;
        if (updateAvoidBadWifi() && (runnable = this.mAvoidBadWifiCallback) != null) {
            runnable.run();
        }
        updateMeteredMultipathPreference();
    }

    public boolean updateAvoidBadWifi() {
        boolean equals = "1".equals(getAvoidBadWifiSetting());
        boolean z = this.mAvoidBadWifi;
        this.mAvoidBadWifi = equals || !configRestrictsAvoidBadWifi();
        if (this.mAvoidBadWifi != z) {
            return true;
        }
        return false;
    }

    public int configMeteredMultipathPreference() {
        return this.mResources.get().getInteger(this.mResources.get().getIdentifier("config_networkMeteredMultipathPreference", "integer", this.mResources.getResourcesContext().getPackageName()));
    }

    public void updateMeteredMultipathPreference() {
        try {
            this.mMeteredMultipathPreference = Integer.parseInt(Settings.Global.getString(this.mResolver, ConnectivitySettingsManager.NETWORK_METERED_MULTIPATH_PREFERENCE));
        } catch (NumberFormatException unused) {
            this.mMeteredMultipathPreference = configMeteredMultipathPreference();
        }
    }

    private class SettingObserver extends ContentObserver {
        public SettingObserver() {
            super((Handler) null);
        }

        public void onChange(boolean z) {
            Log.wtf(MultinetworkPolicyTracker.TAG, "Should never be reached.");
        }

        public void onChange(boolean z, Uri uri) {
            if (!MultinetworkPolicyTracker.this.mSettingsUris.contains(uri)) {
                String r3 = MultinetworkPolicyTracker.TAG;
                Log.wtf(r3, "Unexpected settings observation: " + uri);
            }
            MultinetworkPolicyTracker.this.reevaluate();
        }
    }
}
