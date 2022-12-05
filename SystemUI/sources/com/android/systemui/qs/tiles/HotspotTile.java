package com.android.systemui.qs.tiles;

import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import androidx.appcompat.R$styleable;
import androidx.lifecycle.LifecycleOwner;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.R$drawable;
import com.android.systemui.R$plurals;
import com.android.systemui.R$string;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.statusbar.policy.DataSaverController;
import com.android.systemui.statusbar.policy.HotspotController;
/* loaded from: classes.dex */
public class HotspotTile extends QSTileImpl<QSTile.BooleanState> {
    private final HotspotAndDataSaverCallbacks mCallbacks;
    private final DataSaverController mDataSaverController;
    private final HotspotController mHotspotController;
    private boolean mListening;
    private final QSTile.Icon mEnabledStatic = QSTileImpl.ResourceIcon.get(R$drawable.ic_hotspot);
    private final QSTile.Icon mWifi4EnabledStatic = QSTileImpl.ResourceIcon.get(R$drawable.ic_wifi_4_hotspot);
    private final QSTile.Icon mWifi5EnabledStatic = QSTileImpl.ResourceIcon.get(R$drawable.ic_wifi_5_hotspot);
    private final QSTile.Icon mWifi6EnabledStatic = QSTileImpl.ResourceIcon.get(R$drawable.ic_wifi_6_hotspot);
    private WifiManager mWifiManager = (WifiManager) this.mContext.getSystemService("wifi");

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public int getMetricsCategory() {
        return R$styleable.AppCompatTheme_windowFixedHeightMajor;
    }

    public HotspotTile(QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, HotspotController hotspotController, DataSaverController dataSaverController) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        HotspotAndDataSaverCallbacks hotspotAndDataSaverCallbacks = new HotspotAndDataSaverCallbacks();
        this.mCallbacks = hotspotAndDataSaverCallbacks;
        this.mHotspotController = hotspotController;
        this.mDataSaverController = dataSaverController;
        hotspotController.observe((LifecycleOwner) this, (HotspotTile) hotspotAndDataSaverCallbacks);
        dataSaverController.observe((LifecycleOwner) this, (HotspotTile) hotspotAndDataSaverCallbacks);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public boolean isAvailable() {
        return this.mHotspotController.isHotspotSupported();
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    protected void handleDestroy() {
        super.handleDestroy();
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public void handleSetListening(boolean z) {
        super.handleSetListening(z);
        if (this.mListening == z) {
            return;
        }
        this.mListening = z;
        if (!z) {
            return;
        }
        refreshState();
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public Intent getLongClickIntent() {
        return new Intent("com.android.settings.WIFI_TETHER_SETTINGS");
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    /* renamed from: newTileState */
    public QSTile.BooleanState mo1926newTileState() {
        return new QSTile.BooleanState();
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    protected void handleClick(View view) {
        this.mActivityStarter.postStartActivityDismissingKeyguard(QSTileImpl.INTERNET_PANEL, 0);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public CharSequence getTileLabel() {
        return this.mContext.getString(R$string.quick_settings_hotspot_label);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public void handleUpdateState(QSTile.BooleanState booleanState, Object obj) {
        int numConnectedDevices;
        boolean isDataSaverEnabled;
        int i = 1;
        boolean z = obj == QSTileImpl.ARG_SHOW_TRANSIENT_ENABLING;
        if (booleanState.slash == null) {
            booleanState.slash = new QSTile.SlashState();
        }
        boolean z2 = z || this.mHotspotController.isHotspotTransient();
        checkIfRestrictionEnforcedByAdminOnly(booleanState, "no_config_tethering");
        if (obj instanceof CallbackInfo) {
            CallbackInfo callbackInfo = (CallbackInfo) obj;
            booleanState.value = z || callbackInfo.isHotspotEnabled;
            numConnectedDevices = callbackInfo.numConnectedDevices;
            isDataSaverEnabled = callbackInfo.isDataSaverEnabled;
        } else {
            booleanState.value = z || this.mHotspotController.isHotspotEnabled();
            numConnectedDevices = this.mHotspotController.getNumConnectedDevices();
            isDataSaverEnabled = this.mDataSaverController.isDataSaverEnabled();
        }
        booleanState.icon = this.mEnabledStatic;
        booleanState.label = this.mContext.getString(R$string.quick_settings_hotspot_label);
        booleanState.isTransient = z2;
        QSTile.SlashState slashState = booleanState.slash;
        boolean z3 = booleanState.value;
        slashState.isSlashed = !z3 && !z2;
        if (z2) {
            booleanState.icon = QSTileImpl.ResourceIcon.get(17302460);
        } else if (z3) {
            int softApWifiStandard = this.mWifiManager.getSoftApWifiStandard();
            if (softApWifiStandard == 6) {
                booleanState.icon = this.mWifi6EnabledStatic;
            } else if (softApWifiStandard == 5) {
                booleanState.icon = this.mWifi5EnabledStatic;
            } else if (softApWifiStandard == 4) {
                booleanState.icon = this.mWifi4EnabledStatic;
            }
        }
        booleanState.expandedAccessibilityClassName = Switch.class.getName();
        booleanState.contentDescription = booleanState.label;
        boolean z4 = booleanState.value || booleanState.isTransient;
        if (isDataSaverEnabled) {
            booleanState.state = 0;
        } else {
            if (z4) {
                i = 2;
            }
            booleanState.state = i;
        }
        String secondaryLabel = getSecondaryLabel(z4, z2, isDataSaverEnabled, numConnectedDevices);
        booleanState.secondaryLabel = secondaryLabel;
        booleanState.stateDescription = secondaryLabel;
    }

    private String getSecondaryLabel(boolean z, boolean z2, boolean z3, int i) {
        if (z2) {
            return this.mContext.getString(R$string.quick_settings_hotspot_secondary_label_transient);
        }
        if (z3) {
            return this.mContext.getString(R$string.quick_settings_hotspot_secondary_label_data_saver_enabled);
        }
        if (i > 0 && z) {
            return this.mContext.getResources().getQuantityString(R$plurals.quick_settings_hotspot_secondary_label_num_devices, i, Integer.valueOf(i));
        }
        return null;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    protected String composeChangeAnnouncement() {
        if (((QSTile.BooleanState) this.mState).value) {
            return this.mContext.getString(R$string.accessibility_quick_settings_hotspot_changed_on);
        }
        return this.mContext.getString(R$string.accessibility_quick_settings_hotspot_changed_off);
    }

    /* loaded from: classes.dex */
    private final class HotspotAndDataSaverCallbacks implements HotspotController.Callback, DataSaverController.Listener {
        CallbackInfo mCallbackInfo;

        private HotspotAndDataSaverCallbacks() {
            this.mCallbackInfo = new CallbackInfo();
        }

        @Override // com.android.systemui.statusbar.policy.DataSaverController.Listener
        public void onDataSaverChanged(boolean z) {
            CallbackInfo callbackInfo = this.mCallbackInfo;
            callbackInfo.isDataSaverEnabled = z;
            HotspotTile.this.refreshState(callbackInfo);
        }

        @Override // com.android.systemui.statusbar.policy.HotspotController.Callback
        public void onHotspotChanged(boolean z, int i) {
            CallbackInfo callbackInfo = this.mCallbackInfo;
            callbackInfo.isHotspotEnabled = z;
            callbackInfo.numConnectedDevices = i;
            HotspotTile.this.refreshState(callbackInfo);
        }

        @Override // com.android.systemui.statusbar.policy.HotspotController.Callback
        public void onHotspotAvailabilityChanged(boolean z) {
            if (!z) {
                Log.d(((QSTileImpl) HotspotTile.this).TAG, "Tile removed. Hotspot no longer available");
                ((QSTileImpl) HotspotTile.this).mHost.removeTile(HotspotTile.this.getTileSpec());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public static final class CallbackInfo {
        boolean isDataSaverEnabled;
        boolean isHotspotEnabled;
        int numConnectedDevices;

        protected CallbackInfo() {
        }

        public String toString() {
            return "CallbackInfo[isHotspotEnabled=" + this.isHotspotEnabled + ",numConnectedDevices=" + this.numConnectedDevices + ",isDataSaverEnabled=" + this.isDataSaverEnabled + ']';
        }
    }
}
