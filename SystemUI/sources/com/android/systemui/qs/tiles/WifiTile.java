package com.android.systemui.qs.tiles;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import androidx.appcompat.R$styleable;
import androidx.lifecycle.Lifecycle;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.R$drawable;
import com.android.systemui.R$string;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.DetailAdapter;
import com.android.systemui.plugins.qs.QSIconView;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.AlphaControlledSignalTileView;
import com.android.systemui.qs.QSDetailItems;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.statusbar.policy.NetworkController;
import com.android.wifitrackerlib.WifiEntry;
import java.util.List;
/* loaded from: classes.dex */
public class WifiTile extends QSTileImpl<QSTile.SignalState> {
    private static final Intent WIFI_SETTINGS = new Intent("android.settings.WIFI_SETTINGS");
    protected final NetworkController mController;
    private final WifiDetailAdapter mDetailAdapter = (WifiDetailAdapter) createDetailAdapter();
    private boolean mExpectDisabled;
    protected final WifiSignalCallback mSignalCallback;
    private final QSTile.SignalState mStateBeforeClick;
    private final NetworkController.AccessPointController mWifiController;

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public int getMetricsCategory() {
        return R$styleable.AppCompatTheme_windowNoTitle;
    }

    public WifiTile(QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, NetworkController networkController, NetworkController.AccessPointController accessPointController) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        QSTile.SignalState mo1926newTileState = mo1926newTileState();
        this.mStateBeforeClick = mo1926newTileState;
        WifiSignalCallback wifiSignalCallback = new WifiSignalCallback();
        this.mSignalCallback = wifiSignalCallback;
        this.mController = networkController;
        this.mWifiController = accessPointController;
        networkController.observe(mo1437getLifecycle(), (Lifecycle) wifiSignalCallback);
        mo1926newTileState.spec = "wifi";
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    /* renamed from: newTileState */
    public QSTile.SignalState mo1926newTileState() {
        return new QSTile.SignalState();
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public void setDetailListening(boolean z) {
        if (z) {
            this.mWifiController.addAccessPointCallback(this.mDetailAdapter);
        } else {
            this.mWifiController.removeAccessPointCallback(this.mDetailAdapter);
        }
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public DetailAdapter getDetailAdapter() {
        return this.mDetailAdapter;
    }

    protected DetailAdapter createDetailAdapter() {
        return new WifiDetailAdapter();
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public QSIconView createTileView(Context context) {
        return new AlphaControlledSignalTileView(context);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public Intent getLongClickIntent() {
        return WIFI_SETTINGS;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    protected void handleClick(View view) {
        this.mActivityStarter.postStartActivityDismissingKeyguard(QSTileImpl.INTERNET_PANEL, 0);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    protected void handleSecondaryClick(View view) {
        if (!this.mWifiController.canConfigWifi()) {
            this.mActivityStarter.postStartActivityDismissingKeyguard(new Intent("android.settings.WIFI_SETTINGS"), 0);
            return;
        }
        showDetail(true);
        if (((QSTile.SignalState) this.mState).value) {
            return;
        }
        this.mController.setWifiEnabled(true);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public CharSequence getTileLabel() {
        return this.mContext.getString(R$string.quick_settings_wifi_label);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public void handleUpdateState(QSTile.SignalState signalState, Object obj) {
        int i;
        if (QSTileImpl.DEBUG) {
            String str = this.TAG;
            Log.d(str, "handleUpdateState arg=" + obj);
        }
        CallbackInfo callbackInfo = this.mSignalCallback.mInfo;
        if (this.mExpectDisabled) {
            if (callbackInfo.enabled) {
                return;
            }
            this.mExpectDisabled = false;
        }
        boolean z = obj == QSTileImpl.ARG_SHOW_TRANSIENT_ENABLING;
        boolean z2 = callbackInfo.enabled;
        boolean z3 = z2 && (i = callbackInfo.wifiSignalIconId) > 0 && !(callbackInfo.ssid == null && i == 17302902);
        boolean z4 = callbackInfo.ssid == null && callbackInfo.wifiSignalIconId == 17302902;
        if (signalState.value != z2) {
            this.mDetailAdapter.setItemsVisible(z2);
            fireToggleStateChanged(callbackInfo.enabled);
        }
        if (signalState.slash == null) {
            QSTile.SlashState slashState = new QSTile.SlashState();
            signalState.slash = slashState;
            slashState.rotation = 6.0f;
        }
        signalState.slash.isSlashed = false;
        boolean z5 = z || callbackInfo.isTransient;
        signalState.secondaryLabel = getSecondaryLabel(z5, callbackInfo.statusLabel);
        signalState.state = 2;
        signalState.dualTarget = true;
        signalState.value = z || callbackInfo.enabled;
        boolean z6 = callbackInfo.enabled;
        signalState.activityIn = z6 && callbackInfo.activityIn;
        signalState.activityOut = z6 && callbackInfo.activityOut;
        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer stringBuffer2 = new StringBuffer();
        Resources resources = this.mContext.getResources();
        if (z5) {
            signalState.icon = QSTileImpl.ResourceIcon.get(17302855);
            signalState.label = resources.getString(R$string.quick_settings_wifi_label);
        } else if (!signalState.value) {
            signalState.slash.isSlashed = true;
            signalState.state = 1;
            signalState.icon = QSTileImpl.ResourceIcon.get(17302902);
            signalState.label = resources.getString(R$string.quick_settings_wifi_label);
        } else if (z3) {
            signalState.icon = QSTileImpl.ResourceIcon.get(callbackInfo.wifiSignalIconId);
            String str2 = callbackInfo.ssid;
            signalState.label = str2 != null ? removeDoubleQuotes(str2) : getTileLabel();
        } else if (z4) {
            signalState.icon = QSTileImpl.ResourceIcon.get(17302902);
            signalState.label = resources.getString(R$string.quick_settings_wifi_label);
        } else {
            signalState.icon = QSTileImpl.ResourceIcon.get(17302902);
            signalState.label = resources.getString(R$string.quick_settings_wifi_label);
        }
        stringBuffer.append(this.mContext.getString(R$string.quick_settings_wifi_label));
        stringBuffer.append(",");
        if (signalState.value && z3) {
            stringBuffer2.append(callbackInfo.wifiSignalContentDescription);
            stringBuffer.append(removeDoubleQuotes(callbackInfo.ssid));
            if (!TextUtils.isEmpty(signalState.secondaryLabel)) {
                stringBuffer.append(",");
                stringBuffer.append(signalState.secondaryLabel);
            }
        }
        signalState.stateDescription = stringBuffer2.toString();
        signalState.contentDescription = stringBuffer.toString();
        signalState.dualLabelContentDescription = resources.getString(R$string.accessibility_quick_settings_open_settings, getTileLabel());
        signalState.expandedAccessibilityClassName = Switch.class.getName();
    }

    private CharSequence getSecondaryLabel(boolean z, String str) {
        return z ? this.mContext.getString(R$string.quick_settings_wifi_secondary_label_transient) : str;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    protected boolean shouldAnnouncementBeDelayed() {
        return this.mStateBeforeClick.value == ((QSTile.SignalState) this.mState).value;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    protected String composeChangeAnnouncement() {
        if (((QSTile.SignalState) this.mState).value) {
            return this.mContext.getString(R$string.accessibility_quick_settings_wifi_changed_on);
        }
        return this.mContext.getString(R$string.accessibility_quick_settings_wifi_changed_off);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
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

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public static final class CallbackInfo {
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

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public final class WifiSignalCallback implements NetworkController.SignalCallback {
        final CallbackInfo mInfo = new CallbackInfo();

        protected WifiSignalCallback() {
        }

        @Override // com.android.systemui.statusbar.policy.NetworkController.SignalCallback
        public void setWifiIndicators(NetworkController.WifiIndicators wifiIndicators) {
            if (QSTileImpl.DEBUG) {
                String str = ((QSTileImpl) WifiTile.this).TAG;
                Log.d(str, "onWifiSignalChanged enabled=" + wifiIndicators.enabled);
            }
            NetworkController.IconState iconState = wifiIndicators.qsIcon;
            if (iconState == null) {
                return;
            }
            CallbackInfo callbackInfo = this.mInfo;
            callbackInfo.enabled = wifiIndicators.enabled;
            callbackInfo.connected = iconState.visible;
            callbackInfo.wifiSignalIconId = iconState.icon;
            callbackInfo.ssid = wifiIndicators.description;
            callbackInfo.activityIn = wifiIndicators.activityIn;
            callbackInfo.activityOut = wifiIndicators.activityOut;
            callbackInfo.wifiSignalContentDescription = iconState.contentDescription;
            callbackInfo.isTransient = wifiIndicators.isTransient;
            callbackInfo.statusLabel = wifiIndicators.statusLabel;
            if (WifiTile.this.isShowingDetail()) {
                WifiTile.this.mDetailAdapter.updateItems();
            }
            WifiTile.this.refreshState();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public class WifiDetailAdapter implements DetailAdapter, NetworkController.AccessPointController.AccessPointCallback, QSDetailItems.Callback {
        private WifiEntry[] mAccessPoints;
        private QSDetailItems mItems;

        @Override // com.android.systemui.plugins.qs.DetailAdapter
        public int getMetricsCategory() {
            return 152;
        }

        @Override // com.android.systemui.qs.QSDetailItems.Callback
        public void onDetailItemDisconnect(QSDetailItems.Item item) {
        }

        protected WifiDetailAdapter() {
        }

        @Override // com.android.systemui.plugins.qs.DetailAdapter
        public CharSequence getTitle() {
            return ((QSTileImpl) WifiTile.this).mContext.getString(R$string.quick_settings_wifi_label);
        }

        @Override // com.android.systemui.plugins.qs.DetailAdapter
        public Intent getSettingsIntent() {
            return WifiTile.WIFI_SETTINGS;
        }

        @Override // com.android.systemui.plugins.qs.DetailAdapter
        public Boolean getToggleState() {
            return Boolean.valueOf(((QSTile.SignalState) ((QSTileImpl) WifiTile.this).mState).value);
        }

        @Override // com.android.systemui.plugins.qs.DetailAdapter
        public void setToggleState(boolean z) {
            if (QSTileImpl.DEBUG) {
                String str = ((QSTileImpl) WifiTile.this).TAG;
                Log.d(str, "setToggleState " + z);
            }
            MetricsLogger.action(((QSTileImpl) WifiTile.this).mContext, 153, z);
            WifiTile.this.mController.setWifiEnabled(z);
        }

        @Override // com.android.systemui.plugins.qs.DetailAdapter
        public View createDetailView(Context context, View view, ViewGroup viewGroup) {
            if (QSTileImpl.DEBUG) {
                String str = ((QSTileImpl) WifiTile.this).TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("createDetailView convertView=");
                sb.append(view != null);
                Log.d(str, sb.toString());
            }
            this.mAccessPoints = null;
            QSDetailItems convertOrInflate = QSDetailItems.convertOrInflate(context, view, viewGroup);
            this.mItems = convertOrInflate;
            convertOrInflate.setTagSuffix("Wifi");
            this.mItems.setCallback(this);
            WifiTile.this.mWifiController.scanForAccessPoints();
            setItemsVisible(((QSTile.SignalState) ((QSTileImpl) WifiTile.this).mState).value);
            return this.mItems;
        }

        @Override // com.android.systemui.statusbar.policy.NetworkController.AccessPointController.AccessPointCallback
        public void onAccessPointsChanged(List<WifiEntry> list) {
            this.mAccessPoints = (WifiEntry[]) list.toArray(new WifiEntry[list.size()]);
            filterUnreachableAPs();
            updateItems();
        }

        private void filterUnreachableAPs() {
            int i = 0;
            for (WifiEntry wifiEntry : this.mAccessPoints) {
                if (WifiTile.isWifiEntryReachable(wifiEntry)) {
                    i++;
                }
            }
            WifiEntry[] wifiEntryArr = this.mAccessPoints;
            if (i != wifiEntryArr.length) {
                this.mAccessPoints = new WifiEntry[i];
                int i2 = 0;
                for (WifiEntry wifiEntry2 : wifiEntryArr) {
                    if (WifiTile.isWifiEntryReachable(wifiEntry2)) {
                        this.mAccessPoints[i2] = wifiEntry2;
                        i2++;
                    }
                }
            }
        }

        @Override // com.android.systemui.statusbar.policy.NetworkController.AccessPointController.AccessPointCallback
        public void onSettingsActivityTriggered(Intent intent) {
            ((QSTileImpl) WifiTile.this).mActivityStarter.postStartActivityDismissingKeyguard(intent, 0);
        }

        @Override // com.android.systemui.qs.QSDetailItems.Callback
        public void onDetailItemClick(QSDetailItems.Item item) {
            Object obj;
            if (item == null || (obj = item.tag) == null) {
                return;
            }
            WifiEntry wifiEntry = (WifiEntry) obj;
            if (wifiEntry.getConnectedState() == 0 && WifiTile.this.mWifiController.connect(wifiEntry)) {
                ((QSTileImpl) WifiTile.this).mHost.collapsePanels();
            }
            WifiTile.this.showDetail(false);
        }

        public void setItemsVisible(boolean z) {
            QSDetailItems qSDetailItems = this.mItems;
            if (qSDetailItems == null) {
                return;
            }
            qSDetailItems.setItemsVisible(z);
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Removed duplicated region for block: B:12:0x002f  */
        /* JADX WARN: Removed duplicated region for block: B:14:0x003c  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void updateItems() {
            if (this.mItems == null) {
                return;
            }
            WifiEntry[] wifiEntryArr = this.mAccessPoints;
            int i = 0;
            if (wifiEntryArr == null || wifiEntryArr.length <= 0) {
                WifiTile wifiTile = WifiTile.this;
                if (wifiTile.mSignalCallback.mInfo.enabled) {
                    wifiTile.fireScanStateChanged(true);
                    QSDetailItems.Item[] itemArr = null;
                    if (WifiTile.this.mSignalCallback.mInfo.enabled) {
                        this.mItems.setEmptyState(17302902, R$string.wifi_is_off);
                        this.mItems.setItems(null);
                        return;
                    }
                    this.mItems.setEmptyState(17302902, R$string.quick_settings_wifi_detail_empty_text);
                    WifiEntry[] wifiEntryArr2 = this.mAccessPoints;
                    if (wifiEntryArr2 != null) {
                        itemArr = new QSDetailItems.Item[wifiEntryArr2.length];
                        while (true) {
                            WifiEntry[] wifiEntryArr3 = this.mAccessPoints;
                            if (i >= wifiEntryArr3.length) {
                                break;
                            }
                            WifiEntry wifiEntry = wifiEntryArr3[i];
                            QSDetailItems.Item item = new QSDetailItems.Item();
                            item.tag = wifiEntry;
                            item.iconResId = WifiTile.this.mWifiController.getIcon(wifiEntry);
                            item.line1 = wifiEntry.getSsid();
                            item.line2 = wifiEntry.getSummary();
                            item.icon2 = wifiEntry.getSecurity() != 0 ? R$drawable.qs_ic_wifi_lock : -1;
                            itemArr[i] = item;
                            i++;
                        }
                    }
                    this.mItems.setItems(itemArr);
                    return;
                }
            }
            WifiTile.this.fireScanStateChanged(false);
            QSDetailItems.Item[] itemArr2 = null;
            if (WifiTile.this.mSignalCallback.mInfo.enabled) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isWifiEntryReachable(WifiEntry wifiEntry) {
        return wifiEntry.getLevel() != -1;
    }
}
