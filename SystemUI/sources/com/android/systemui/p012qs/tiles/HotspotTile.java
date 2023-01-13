package com.android.systemui.p012qs.tiles;

import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import androidx.lifecycle.LifecycleOwner;
import com.android.internal.logging.MetricsLogger;
import com.android.settingslib.wifi.WifiEnterpriseRestrictionUtils;
import com.android.systemui.C1894R;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.p012qs.QSHost;
import com.android.systemui.p012qs.logging.QSLogger;
import com.android.systemui.p012qs.tileimpl.QSTileImpl;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.p011qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.policy.DataSaverController;
import com.android.systemui.statusbar.policy.HotspotController;
import javax.inject.Inject;

/* renamed from: com.android.systemui.qs.tiles.HotspotTile */
public class HotspotTile extends QSTileImpl<QSTile.BooleanState> {
    private final HotspotAndDataSaverCallbacks mCallbacks;
    private final DataSaverController mDataSaverController;
    private final QSTile.Icon mEnabledStatic = QSTileImpl.ResourceIcon.get(C1894R.C1896drawable.ic_hotspot);
    private final HotspotController mHotspotController;
    private boolean mListening;
    private final QSTile.Icon mWifi4EnabledStatic = QSTileImpl.ResourceIcon.get(C1894R.C1896drawable.ic_wifi_4_hotspot);
    private final QSTile.Icon mWifi5EnabledStatic = QSTileImpl.ResourceIcon.get(C1894R.C1896drawable.ic_wifi_5_hotspot);
    private final QSTile.Icon mWifi6EnabledStatic = QSTileImpl.ResourceIcon.get(C1894R.C1896drawable.ic_wifi_6_hotspot);
    private WifiManager mWifiManager;

    public int getMetricsCategory() {
        return 120;
    }

    @Inject
    public HotspotTile(QSHost qSHost, @Background Looper looper, @Main Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, HotspotController hotspotController, DataSaverController dataSaverController) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        HotspotAndDataSaverCallbacks hotspotAndDataSaverCallbacks = new HotspotAndDataSaverCallbacks();
        this.mCallbacks = hotspotAndDataSaverCallbacks;
        this.mHotspotController = hotspotController;
        this.mDataSaverController = dataSaverController;
        hotspotController.observe((LifecycleOwner) this, hotspotAndDataSaverCallbacks);
        dataSaverController.observe((LifecycleOwner) this, hotspotAndDataSaverCallbacks);
        this.mWifiManager = (WifiManager) this.mContext.getSystemService("wifi");
    }

    public boolean isAvailable() {
        return this.mHotspotController.isHotspotSupported();
    }

    /* access modifiers changed from: protected */
    public void handleDestroy() {
        super.handleDestroy();
    }

    public void handleSetListening(boolean z) {
        super.handleSetListening(z);
        if (this.mListening != z) {
            this.mListening = z;
            if (z) {
                refreshState();
            }
        }
    }

    public Intent getLongClickIntent() {
        return new Intent("com.android.settings.WIFI_TETHER_SETTINGS");
    }

    public QSTile.BooleanState newTileState() {
        return new QSTile.BooleanState();
    }

    /* access modifiers changed from: protected */
    public void handleClick(View view) {
        Object obj;
        boolean z = ((QSTile.BooleanState) this.mState).value;
        if (z || !this.mDataSaverController.isDataSaverEnabled()) {
            if (z) {
                obj = null;
            } else {
                obj = ARG_SHOW_TRANSIENT_ENABLING;
            }
            refreshState(obj);
            this.mHotspotController.setHotspotEnabled(!z);
        }
    }

    public CharSequence getTileLabel() {
        return this.mContext.getString(C1894R.string.quick_settings_hotspot_label);
    }

    /* access modifiers changed from: protected */
    public void handleUpdateState(QSTile.BooleanState booleanState, Object obj) {
        int i;
        boolean z;
        int i2 = 1;
        boolean z2 = obj == ARG_SHOW_TRANSIENT_ENABLING;
        if (booleanState.slash == null) {
            booleanState.slash = new QSTile.SlashState();
        }
        boolean z3 = z2 || this.mHotspotController.isHotspotTransient();
        checkIfRestrictionEnforcedByAdminOnly(booleanState, "no_config_tethering");
        if (obj instanceof CallbackInfo) {
            CallbackInfo callbackInfo = (CallbackInfo) obj;
            booleanState.value = z2 || callbackInfo.isHotspotEnabled;
            int i3 = callbackInfo.numConnectedDevices;
            z = callbackInfo.isDataSaverEnabled;
            i = i3;
        } else {
            booleanState.value = z2 || this.mHotspotController.isHotspotEnabled();
            i = this.mHotspotController.getNumConnectedDevices();
            z = this.mDataSaverController.isDataSaverEnabled();
        }
        booleanState.icon = this.mEnabledStatic;
        booleanState.label = this.mContext.getString(C1894R.string.quick_settings_hotspot_label);
        booleanState.isTransient = z3;
        booleanState.slash.isSlashed = !booleanState.value && !booleanState.isTransient;
        if (booleanState.isTransient) {
            booleanState.icon = QSTileImpl.ResourceIcon.get(17302469);
        }
        booleanState.expandedAccessibilityClassName = Switch.class.getName();
        booleanState.contentDescription = booleanState.label;
        boolean isWifiTetheringAllowed = WifiEnterpriseRestrictionUtils.isWifiTetheringAllowed(this.mHost.getUserContext());
        boolean z4 = z || !isWifiTetheringAllowed;
        boolean z5 = booleanState.value || booleanState.isTransient;
        if (z4) {
            booleanState.state = 0;
        } else {
            if (z5) {
                i2 = 2;
            }
            booleanState.state = i2;
        }
        booleanState.secondaryLabel = getSecondaryLabel(z5, z3, z, i, isWifiTetheringAllowed);
        booleanState.stateDescription = booleanState.secondaryLabel;
    }

    private String getSecondaryLabel(boolean z, boolean z2, boolean z3, int i, boolean z4) {
        if (!z4) {
            return this.mContext.getString(C1894R.string.wifitrackerlib_admin_restricted_network);
        }
        if (z2) {
            return this.mContext.getString(C1894R.string.quick_settings_hotspot_secondary_label_transient);
        }
        if (z3) {
            return this.mContext.getString(C1894R.string.quick_settings_hotspot_secondary_label_data_saver_enabled);
        }
        if (i <= 0 || !z) {
            return null;
        }
        return this.mContext.getResources().getQuantityString(C1894R.plurals.quick_settings_hotspot_secondary_label_num_devices, i, new Object[]{Integer.valueOf(i)});
    }

    /* renamed from: com.android.systemui.qs.tiles.HotspotTile$HotspotAndDataSaverCallbacks */
    private final class HotspotAndDataSaverCallbacks implements HotspotController.Callback, DataSaverController.Listener {
        CallbackInfo mCallbackInfo;

        private HotspotAndDataSaverCallbacks() {
            this.mCallbackInfo = new CallbackInfo();
        }

        public void onDataSaverChanged(boolean z) {
            this.mCallbackInfo.isDataSaverEnabled = z;
            HotspotTile.this.refreshState(this.mCallbackInfo);
        }

        public void onHotspotChanged(boolean z, int i) {
            this.mCallbackInfo.isHotspotEnabled = z;
            this.mCallbackInfo.numConnectedDevices = i;
            HotspotTile.this.refreshState(this.mCallbackInfo);
        }

        public void onHotspotAvailabilityChanged(boolean z) {
            if (!z) {
                Log.d(HotspotTile.this.TAG, "Tile removed. Hotspot no longer available");
                HotspotTile.this.mHost.removeTile(HotspotTile.this.getTileSpec());
            }
        }
    }

    /* renamed from: com.android.systemui.qs.tiles.HotspotTile$CallbackInfo */
    protected static final class CallbackInfo {
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
