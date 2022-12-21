package com.android.systemui.p012qs.tiles;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.C1893R;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.android.systemui.p012qs.AlphaControlledSignalTileView;
import com.android.systemui.p012qs.QSHost;
import com.android.systemui.p012qs.logging.QSLogger;
import com.android.systemui.p012qs.tileimpl.QSTileImpl;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.p011qs.QSIconView;
import com.android.systemui.plugins.p011qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.connectivity.AccessPointController;
import com.android.systemui.statusbar.connectivity.NetworkController;
import com.android.systemui.statusbar.connectivity.SignalCallback;
import com.android.systemui.statusbar.connectivity.WifiIndicators;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.p024qs.QSTileHostEx;
import javax.inject.Inject;

/* renamed from: com.android.systemui.qs.tiles.WifiTile */
public class WifiTile extends QSTileImpl<QSTile.SignalState> {
    private static final Intent WIFI_SETTINGS = new Intent("android.settings.WIFI_SETTINGS");
    protected final NetworkController mController;
    private boolean mExpectDisabled;
    protected final WifiSignalCallback mSignalCallback;
    private final QSTile.SignalState mStateBeforeClick;
    private final AccessPointController mWifiController;

    public int getMetricsCategory() {
        return 126;
    }

    @Inject
    public WifiTile(QSHost qSHost, @Background Looper looper, @Main Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, NetworkController networkController, AccessPointController accessPointController) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        QSTile.SignalState newTileState = newTileState();
        this.mStateBeforeClick = newTileState;
        WifiSignalCallback wifiSignalCallback = new WifiSignalCallback();
        this.mSignalCallback = wifiSignalCallback;
        this.mController = networkController;
        this.mWifiController = accessPointController;
        networkController.observe(getLifecycle(), wifiSignalCallback);
        newTileState.spec = "wifi";
    }

    public QSTile.SignalState newTileState() {
        return new QSTile.SignalState();
    }

    public QSIconView createTileView(Context context) {
        AlphaControlledSignalTileView alphaControlledSignalTileView = new AlphaControlledSignalTileView(context);
        alphaControlledSignalTileView.setOnClickListener(new WifiTile$$ExternalSyntheticLambda1(this));
        alphaControlledSignalTileView.setOnLongClickListener(new WifiTile$$ExternalSyntheticLambda2(this));
        return alphaControlledSignalTileView;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$createTileView$1$com-android-systemui-qs-tiles-WifiTile  reason: not valid java name */
    public /* synthetic */ void m2983lambda$createTileView$1$comandroidsystemuiqstilesWifiTile(View view) {
        Object obj;
        ((QSTile.SignalState) this.mState).copyTo(this.mStateBeforeClick);
        boolean z = ((QSTile.SignalState) this.mState).value;
        if (z) {
            obj = null;
        } else {
            obj = ARG_SHOW_TRANSIENT_ENABLING;
        }
        refreshState(obj);
        this.mController.setWifiEnabled(!z);
        this.mExpectDisabled = z;
        if (z) {
            this.mHandler.postDelayed(new WifiTile$$ExternalSyntheticLambda0(this), 350);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$createTileView$0$com-android-systemui-qs-tiles-WifiTile  reason: not valid java name */
    public /* synthetic */ void m2982lambda$createTileView$0$comandroidsystemuiqstilesWifiTile() {
        if (this.mExpectDisabled) {
            this.mExpectDisabled = false;
            refreshState();
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$createTileView$2$com-android-systemui-qs-tiles-WifiTile  reason: not valid java name */
    public /* synthetic */ boolean m2984lambda$createTileView$2$comandroidsystemuiqstilesWifiTile(View view) {
        handleLongClick(view);
        return true;
    }

    public Intent getLongClickIntent() {
        if (!((QSTileHostEx) NTDependencyEx.get(QSTileHostEx.class)).isQsExpanded()) {
            return new Intent("android.settings.WIRELESS_SETTINGS");
        }
        return WIFI_SETTINGS;
    }

    /* access modifiers changed from: protected */
    public void handleClick(View view) {
        Object obj = null;
        if (this.mEx != null) {
            this.mEx.createInternetDialog((View) null, this.mActivityStarter);
            return;
        }
        ((QSTile.SignalState) this.mState).copyTo(this.mStateBeforeClick);
        boolean z = ((QSTile.SignalState) this.mState).value;
        if (!z) {
            obj = ARG_SHOW_TRANSIENT_ENABLING;
        }
        refreshState(obj);
        this.mController.setWifiEnabled(!z);
        this.mExpectDisabled = z;
        if (z) {
            this.mHandler.postDelayed(new WifiTile$$ExternalSyntheticLambda3(this), 350);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$handleClick$3$com-android-systemui-qs-tiles-WifiTile  reason: not valid java name */
    public /* synthetic */ void m2985lambda$handleClick$3$comandroidsystemuiqstilesWifiTile() {
        if (this.mExpectDisabled) {
            this.mExpectDisabled = false;
            refreshState();
        }
    }

    /* access modifiers changed from: protected */
    public void handleSecondaryClick(View view) {
        if (!this.mWifiController.canConfigWifi()) {
            this.mActivityStarter.postStartActivityDismissingKeyguard(new Intent("android.settings.WIFI_SETTINGS"), 0);
        } else if (!((QSTile.SignalState) this.mState).value) {
            this.mController.setWifiEnabled(true);
        }
    }

    public CharSequence getTileLabel() {
        return this.mContext.getString(C1893R.string.quick_settings_wifi_label);
    }

    /* access modifiers changed from: protected */
    public void handleUpdateState(QSTile.SignalState signalState, Object obj) {
        if (DEBUG) {
            Log.d(this.TAG, "handleUpdateState arg=" + obj);
        }
        CallbackInfo callbackInfo = this.mSignalCallback.mInfo;
        if (this.mExpectDisabled) {
            if (!callbackInfo.enabled) {
                this.mExpectDisabled = false;
            } else {
                return;
            }
        }
        boolean z = obj == ARG_SHOW_TRANSIENT_ENABLING;
        boolean z2 = callbackInfo.enabled && callbackInfo.wifiSignalIconId > 0 && !(callbackInfo.ssid == null && callbackInfo.wifiSignalIconId == 17302913);
        boolean z3 = callbackInfo.ssid == null && callbackInfo.wifiSignalIconId == 17302913;
        if (signalState.slash == null) {
            signalState.slash = new QSTile.SlashState();
            signalState.slash.rotation = 6.0f;
        }
        signalState.slash.isSlashed = false;
        boolean z4 = z || callbackInfo.isTransient;
        signalState.secondaryLabel = getSecondaryLabel(z4, callbackInfo.statusLabel);
        signalState.state = 2;
        signalState.dualTarget = true;
        signalState.value = z || callbackInfo.enabled;
        signalState.activityIn = callbackInfo.enabled && callbackInfo.activityIn;
        signalState.activityOut = callbackInfo.enabled && callbackInfo.activityOut;
        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer stringBuffer2 = new StringBuffer();
        Resources resources = this.mContext.getResources();
        if (z4) {
            signalState.icon = QSTileImpl.ResourceIcon.get(17302865);
            signalState.label = resources.getString(C1893R.string.quick_settings_wifi_label);
        } else if (!signalState.value) {
            signalState.slash.isSlashed = true;
            signalState.state = 1;
            signalState.icon = QSTileImpl.ResourceIcon.get(17302913);
            signalState.label = resources.getString(C1893R.string.quick_settings_wifi_label);
        } else if (z2) {
            signalState.icon = QSTileImpl.ResourceIcon.get(callbackInfo.wifiSignalIconId);
            signalState.label = callbackInfo.ssid != null ? removeDoubleQuotes(callbackInfo.ssid) : getTileLabel();
        } else if (z3) {
            signalState.icon = QSTileImpl.ResourceIcon.get(17302913);
            signalState.label = resources.getString(C1893R.string.quick_settings_wifi_label);
        } else {
            signalState.icon = QSTileImpl.ResourceIcon.get(17302913);
            signalState.label = resources.getString(C1893R.string.quick_settings_wifi_label);
        }
        stringBuffer.append(this.mContext.getString(C1893R.string.quick_settings_wifi_label)).append(NavigationBarInflaterView.BUTTON_SEPARATOR);
        if (signalState.value && z2) {
            stringBuffer2.append(callbackInfo.wifiSignalContentDescription);
            stringBuffer.append(removeDoubleQuotes(callbackInfo.ssid));
            if (!TextUtils.isEmpty(signalState.secondaryLabel)) {
                stringBuffer.append(NavigationBarInflaterView.BUTTON_SEPARATOR).append(signalState.secondaryLabel);
            }
        }
        signalState.stateDescription = stringBuffer2.toString();
        signalState.contentDescription = stringBuffer.toString();
        signalState.dualLabelContentDescription = resources.getString(C1893R.string.accessibility_quick_settings_open_settings, new Object[]{getTileLabel()});
        signalState.expandedAccessibilityClassName = Switch.class.getName();
    }

    private CharSequence getSecondaryLabel(boolean z, String str) {
        return z ? this.mContext.getString(C1893R.string.quick_settings_wifi_secondary_label_transient) : str;
    }

    public boolean isAvailable() {
        return this.mContext.getPackageManager().hasSystemFeature("android.hardware.wifi");
    }

    private static String removeDoubleQuotes(String str) {
        if (str == null) {
            return null;
        }
        int length = str.length();
        if (length <= 1 || str.charAt(0) != '\"') {
            return str;
        }
        int i = length - 1;
        return str.charAt(i) == '\"' ? str.substring(1, i) : str;
    }

    /* renamed from: com.android.systemui.qs.tiles.WifiTile$CallbackInfo */
    protected static final class CallbackInfo {
        boolean activityIn;
        boolean activityOut;
        boolean connected;
        boolean enabled;
        boolean isTransient;
        String ssid;
        public String statusLabel;
        String wifiSignalContentDescription;
        int wifiSignalIconId;

        protected CallbackInfo() {
        }

        public String toString() {
            return "CallbackInfo[enabled=" + this.enabled + ",connected=" + this.connected + ",wifiSignalIconId=" + this.wifiSignalIconId + ",ssid=" + this.ssid + ",activityIn=" + this.activityIn + ",activityOut=" + this.activityOut + ",wifiSignalContentDescription=" + this.wifiSignalContentDescription + ",isTransient=" + this.isTransient + ']';
        }
    }

    /* renamed from: com.android.systemui.qs.tiles.WifiTile$WifiSignalCallback */
    protected final class WifiSignalCallback implements SignalCallback {
        final CallbackInfo mInfo = new CallbackInfo();

        protected WifiSignalCallback() {
        }

        public void setWifiIndicators(WifiIndicators wifiIndicators) {
            if (WifiTile.DEBUG) {
                Log.d(WifiTile.this.TAG, "onWifiSignalChanged enabled=" + wifiIndicators.enabled);
            }
            if (wifiIndicators.qsIcon != null) {
                WifiTile.this.mEx.dumpWifiInfo(wifiIndicators);
                this.mInfo.enabled = wifiIndicators.enabled;
                this.mInfo.connected = wifiIndicators.qsIcon.visible;
                this.mInfo.wifiSignalIconId = wifiIndicators.qsIcon.icon;
                this.mInfo.ssid = wifiIndicators.description;
                this.mInfo.activityIn = wifiIndicators.activityIn;
                this.mInfo.activityOut = wifiIndicators.activityOut;
                this.mInfo.wifiSignalContentDescription = wifiIndicators.qsIcon.contentDescription;
                this.mInfo.isTransient = wifiIndicators.isTransient;
                this.mInfo.statusLabel = wifiIndicators.statusLabel;
                WifiTile.this.refreshState();
            }
        }
    }
}
