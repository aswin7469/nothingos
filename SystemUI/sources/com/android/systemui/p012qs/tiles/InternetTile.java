package com.android.systemui.p012qs.tiles;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import com.android.internal.logging.MetricsLogger;
import com.android.settingslib.graph.SignalDrawable;
import com.android.settingslib.mobile.TelephonyIcons;
import com.android.settingslib.net.DataUsageController;
import com.android.systemui.C1894R;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.android.systemui.p012qs.AlphaControlledSignalTileView;
import com.android.systemui.p012qs.QSHost;
import com.android.systemui.p012qs.logging.QSLogger;
import com.android.systemui.p012qs.tileimpl.QSTileImpl;
import com.android.systemui.p012qs.tiles.dialog.InternetDialogFactory;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.p011qs.QSIconView;
import com.android.systemui.plugins.p011qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.connectivity.AccessPointController;
import com.android.systemui.statusbar.connectivity.IconState;
import com.android.systemui.statusbar.connectivity.MobileDataIndicators;
import com.android.systemui.statusbar.connectivity.NetworkController;
import com.android.systemui.statusbar.connectivity.SignalCallback;
import com.android.systemui.statusbar.connectivity.WifiIndicators;
import com.android.systemui.util.CarrierNameCustomization;
import java.p026io.PrintWriter;
import javax.inject.Inject;

/* renamed from: com.android.systemui.qs.tiles.InternetTile */
public class InternetTile extends QSTileImpl<QSTile.SignalState> {
    private static final Intent WIFI_SETTINGS = new Intent("android.settings.WIFI_SETTINGS");
    private final AccessPointController mAccessPointController;
    /* access modifiers changed from: private */
    public CarrierNameCustomization mCarrierNameCustomization;
    protected final NetworkController mController;
    private final DataUsageController mDataController;
    final Handler mHandler;
    private final InternetDialogFactory mInternetDialogFactory;
    private int mLastTileState = -1;
    protected final InternetSignalCallback mSignalCallback;

    public int getMetricsCategory() {
        return 126;
    }

    @Inject
    public InternetTile(QSHost qSHost, @Background Looper looper, @Main Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, NetworkController networkController, AccessPointController accessPointController, InternetDialogFactory internetDialogFactory, CarrierNameCustomization carrierNameCustomization) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        InternetSignalCallback internetSignalCallback = new InternetSignalCallback();
        this.mSignalCallback = internetSignalCallback;
        this.mInternetDialogFactory = internetDialogFactory;
        this.mHandler = handler;
        this.mController = networkController;
        this.mAccessPointController = accessPointController;
        this.mDataController = networkController.getMobileDataController();
        networkController.observe(getLifecycle(), internetSignalCallback);
        this.mCarrierNameCustomization = carrierNameCustomization;
    }

    public QSTile.SignalState newTileState() {
        QSTile.SignalState signalState = new QSTile.SignalState();
        signalState.forceExpandIcon = true;
        return signalState;
    }

    public QSIconView createTileView(Context context) {
        return new AlphaControlledSignalTileView(context);
    }

    public Intent getLongClickIntent() {
        return WIFI_SETTINGS;
    }

    /* access modifiers changed from: protected */
    public void handleClick(View view) {
        this.mHandler.post(new InternetTile$$ExternalSyntheticLambda0(this, view));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$handleClick$0$com-android-systemui-qs-tiles-InternetTile  reason: not valid java name */
    public /* synthetic */ void m2982lambda$handleClick$0$comandroidsystemuiqstilesInternetTile(View view) {
        this.mInternetDialogFactory.create(true, this.mAccessPointController.canConfigMobileData(), this.mAccessPointController.canConfigWifi(), view);
    }

    public CharSequence getTileLabel() {
        return this.mContext.getString(C1894R.string.quick_settings_internet_label);
    }

    public boolean isAvailable() {
        return this.mContext.getPackageManager().hasSystemFeature("android.hardware.wifi") || (this.mController.hasMobileDataFeature() && this.mHost.getUserContext().getUserId() == 0);
    }

    private CharSequence getSecondaryLabel(boolean z, String str) {
        return z ? this.mContext.getString(C1894R.string.quick_settings_wifi_secondary_label_transient) : str;
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

    /* renamed from: com.android.systemui.qs.tiles.InternetTile$EthernetCallbackInfo */
    private static final class EthernetCallbackInfo {
        boolean mConnected;
        String mEthernetContentDescription;
        int mEthernetSignalIconId;

        private EthernetCallbackInfo() {
        }

        public String toString() {
            return "EthernetCallbackInfo[mConnected=" + this.mConnected + ",mEthernetSignalIconId=" + this.mEthernetSignalIconId + ",mEthernetContentDescription=" + this.mEthernetContentDescription + ']';
        }
    }

    /* renamed from: com.android.systemui.qs.tiles.InternetTile$WifiCallbackInfo */
    private static final class WifiCallbackInfo {
        boolean mActivityIn;
        boolean mActivityOut;
        boolean mAirplaneModeEnabled;
        boolean mConnected;
        boolean mEnabled;
        boolean mIsTransient;
        boolean mNoDefaultNetwork;
        boolean mNoNetworksAvailable;
        boolean mNoValidatedNetwork;
        String mSsid;
        public String mStatusLabel;
        String mWifiSignalContentDescription;
        int mWifiSignalIconId;

        private WifiCallbackInfo() {
        }

        public String toString() {
            return "WifiCallbackInfo[mAirplaneModeEnabled=" + this.mAirplaneModeEnabled + ",mEnabled=" + this.mEnabled + ",mConnected=" + this.mConnected + ",mWifiSignalIconId=" + this.mWifiSignalIconId + ",mSsid=" + this.mSsid + ",mActivityIn=" + this.mActivityIn + ",mActivityOut=" + this.mActivityOut + ",mWifiSignalContentDescription=" + this.mWifiSignalContentDescription + ",mIsTransient=" + this.mIsTransient + ",mNoDefaultNetwork=" + this.mNoDefaultNetwork + ",mNoValidatedNetwork=" + this.mNoValidatedNetwork + ",mNoNetworksAvailable=" + this.mNoNetworksAvailable + ']';
        }
    }

    /* renamed from: com.android.systemui.qs.tiles.InternetTile$CellularCallbackInfo */
    private static final class CellularCallbackInfo {
        boolean mActivityIn;
        boolean mActivityOut;
        boolean mAirplaneModeEnabled;
        CharSequence mDataContentDescription;
        CharSequence mDataSubscriptionName;
        int mMobileSignalIconId;
        boolean mMultipleSubs;
        boolean mNoDefaultNetwork;
        boolean mNoNetworksAvailable;
        boolean mNoSim;
        boolean mNoValidatedNetwork;
        int mQsTypeIcon;
        boolean mRoaming;

        private CellularCallbackInfo() {
        }

        public String toString() {
            return "CellularCallbackInfo[mAirplaneModeEnabled=" + this.mAirplaneModeEnabled + ",mDataSubscriptionName=" + this.mDataSubscriptionName + ",mDataContentDescription=" + this.mDataContentDescription + ",mMobileSignalIconId=" + this.mMobileSignalIconId + ",mQsTypeIcon=" + this.mQsTypeIcon + ",mActivityIn=" + this.mActivityIn + ",mActivityOut=" + this.mActivityOut + ",mNoSim=" + this.mNoSim + ",mRoaming=" + this.mRoaming + ",mMultipleSubs=" + this.mMultipleSubs + ",mNoDefaultNetwork=" + this.mNoDefaultNetwork + ",mNoValidatedNetwork=" + this.mNoValidatedNetwork + ",mNoNetworksAvailable=" + this.mNoNetworksAvailable + ']';
        }
    }

    /* renamed from: com.android.systemui.qs.tiles.InternetTile$InternetSignalCallback */
    protected final class InternetSignalCallback implements SignalCallback {
        final CellularCallbackInfo mCellularInfo = new CellularCallbackInfo();
        final EthernetCallbackInfo mEthernetInfo = new EthernetCallbackInfo();
        final WifiCallbackInfo mWifiInfo = new WifiCallbackInfo();

        protected InternetSignalCallback() {
        }

        public void setWifiIndicators(WifiIndicators wifiIndicators) {
            if (InternetTile.DEBUG) {
                Log.d(InternetTile.this.TAG, "setWifiIndicators: " + wifiIndicators);
            }
            this.mWifiInfo.mEnabled = wifiIndicators.enabled;
            if (wifiIndicators.qsIcon != null) {
                this.mWifiInfo.mConnected = wifiIndicators.qsIcon.visible;
                this.mWifiInfo.mWifiSignalIconId = wifiIndicators.qsIcon.icon;
                this.mWifiInfo.mWifiSignalContentDescription = wifiIndicators.qsIcon.contentDescription;
                this.mWifiInfo.mEnabled = wifiIndicators.enabled;
                this.mWifiInfo.mSsid = wifiIndicators.description;
                this.mWifiInfo.mActivityIn = wifiIndicators.activityIn;
                this.mWifiInfo.mActivityOut = wifiIndicators.activityOut;
                this.mWifiInfo.mIsTransient = wifiIndicators.isTransient;
                this.mWifiInfo.mStatusLabel = wifiIndicators.statusLabel;
                InternetTile.this.refreshState(this.mWifiInfo);
            }
        }

        public void setMobileDataIndicators(MobileDataIndicators mobileDataIndicators) {
            if (InternetTile.DEBUG) {
                Log.d(InternetTile.this.TAG, "setMobileDataIndicators: " + mobileDataIndicators);
            }
            if (mobileDataIndicators.qsIcon != null) {
                if (!InternetTile.this.mCarrierNameCustomization.isRoamingCustomizationEnabled() || !InternetTile.this.mCarrierNameCustomization.isRoaming(mobileDataIndicators.subId)) {
                    this.mCellularInfo.mDataSubscriptionName = mobileDataIndicators.qsDescription == null ? InternetTile.this.mController.getMobileDataNetworkName() : mobileDataIndicators.qsDescription;
                } else {
                    this.mCellularInfo.mDataSubscriptionName = InternetTile.this.mCarrierNameCustomization.getRoamingCarrierName(mobileDataIndicators.subId);
                }
                this.mCellularInfo.mDataContentDescription = mobileDataIndicators.qsDescription != null ? mobileDataIndicators.typeContentDescriptionHtml : null;
                this.mCellularInfo.mMobileSignalIconId = mobileDataIndicators.qsIcon.icon;
                this.mCellularInfo.mQsTypeIcon = mobileDataIndicators.qsType;
                this.mCellularInfo.mActivityIn = mobileDataIndicators.activityIn;
                this.mCellularInfo.mActivityOut = mobileDataIndicators.activityOut;
                this.mCellularInfo.mRoaming = mobileDataIndicators.roaming;
                CellularCallbackInfo cellularCallbackInfo = this.mCellularInfo;
                boolean z = true;
                if (InternetTile.this.mController.getNumberSubscriptions() <= 1) {
                    z = false;
                }
                cellularCallbackInfo.mMultipleSubs = z;
                InternetTile.this.refreshState(this.mCellularInfo);
            }
        }

        public void setEthernetIndicators(IconState iconState) {
            String str;
            if (InternetTile.DEBUG) {
                String access$1100 = InternetTile.this.TAG;
                StringBuilder sb = new StringBuilder("setEthernetIndicators: icon = ");
                if (iconState == null) {
                    str = "";
                } else {
                    str = iconState.toString();
                }
                Log.d(access$1100, sb.append(str).toString());
            }
            this.mEthernetInfo.mConnected = iconState.visible;
            this.mEthernetInfo.mEthernetSignalIconId = iconState.icon;
            this.mEthernetInfo.mEthernetContentDescription = iconState.contentDescription;
            if (iconState.visible) {
                InternetTile.this.refreshState(this.mEthernetInfo);
            }
        }

        public void setNoSims(boolean z, boolean z2) {
            if (InternetTile.DEBUG) {
                Log.d(InternetTile.this.TAG, "setNoSims: show = " + z + ",simDetected = " + z2);
            }
            this.mCellularInfo.mNoSim = z;
            if (this.mCellularInfo.mNoSim) {
                this.mCellularInfo.mMobileSignalIconId = 0;
                this.mCellularInfo.mQsTypeIcon = 0;
            }
        }

        public void setIsAirplaneMode(IconState iconState) {
            String str;
            if (InternetTile.DEBUG) {
                String access$1600 = InternetTile.this.TAG;
                StringBuilder sb = new StringBuilder("setIsAirplaneMode: icon = ");
                if (iconState == null) {
                    str = "";
                } else {
                    str = iconState.toString();
                }
                Log.d(access$1600, sb.append(str).toString());
            }
            if (this.mCellularInfo.mAirplaneModeEnabled != iconState.visible) {
                this.mCellularInfo.mAirplaneModeEnabled = iconState.visible;
                this.mWifiInfo.mAirplaneModeEnabled = iconState.visible;
                if (InternetTile.this.mSignalCallback.mEthernetInfo.mConnected) {
                    return;
                }
                if (!this.mWifiInfo.mEnabled || this.mWifiInfo.mWifiSignalIconId <= 0 || this.mWifiInfo.mSsid == null) {
                    InternetTile.this.refreshState(this.mCellularInfo);
                } else {
                    InternetTile.this.refreshState(this.mWifiInfo);
                }
            }
        }

        public void setConnectivityStatus(boolean z, boolean z2, boolean z3) {
            if (InternetTile.DEBUG) {
                Log.d(InternetTile.this.TAG, "setConnectivityStatus: noDefaultNetwork = " + z + ",noValidatedNetwork = " + z2 + ",noNetworksAvailable = " + z3);
            }
            this.mCellularInfo.mNoDefaultNetwork = z;
            this.mCellularInfo.mNoValidatedNetwork = z2;
            this.mCellularInfo.mNoNetworksAvailable = z3;
            this.mWifiInfo.mNoDefaultNetwork = z;
            this.mWifiInfo.mNoValidatedNetwork = z2;
            this.mWifiInfo.mNoNetworksAvailable = z3;
            InternetTile.this.refreshState(this.mWifiInfo);
        }

        public String toString() {
            return "InternetSignalCallback[mWifiInfo=" + this.mWifiInfo + ",mCellularInfo=" + this.mCellularInfo + ",mEthernetInfo=" + this.mEthernetInfo + ']';
        }
    }

    /* access modifiers changed from: protected */
    public void handleUpdateState(QSTile.SignalState signalState, Object obj) {
        if (obj instanceof CellularCallbackInfo) {
            this.mLastTileState = 0;
            handleUpdateCellularState(signalState, obj);
        } else if (obj instanceof WifiCallbackInfo) {
            this.mLastTileState = 1;
            handleUpdateWifiState(signalState, obj);
        } else if (obj instanceof EthernetCallbackInfo) {
            this.mLastTileState = 2;
            handleUpdateEthernetState(signalState, obj);
        } else {
            int i = this.mLastTileState;
            if (i == 0) {
                handleUpdateCellularState(signalState, this.mSignalCallback.mCellularInfo);
            } else if (i == 1) {
                handleUpdateWifiState(signalState, this.mSignalCallback.mWifiInfo);
            } else if (i == 2) {
                handleUpdateEthernetState(signalState, this.mSignalCallback.mEthernetInfo);
            }
        }
    }

    private void handleUpdateWifiState(QSTile.SignalState signalState, Object obj) {
        WifiCallbackInfo wifiCallbackInfo = (WifiCallbackInfo) obj;
        if (DEBUG) {
            Log.d(this.TAG, "handleUpdateWifiState: WifiCallbackInfo = " + wifiCallbackInfo.toString());
        }
        boolean z = wifiCallbackInfo.mEnabled && wifiCallbackInfo.mWifiSignalIconId > 0 && wifiCallbackInfo.mSsid != null;
        boolean z2 = wifiCallbackInfo.mWifiSignalIconId > 0 && wifiCallbackInfo.mSsid == null;
        if (signalState.slash == null) {
            signalState.slash = new QSTile.SlashState();
            signalState.slash.rotation = 6.0f;
        }
        signalState.slash.isSlashed = false;
        signalState.secondaryLabel = getSecondaryLabel(wifiCallbackInfo.mIsTransient, removeDoubleQuotes(wifiCallbackInfo.mSsid));
        signalState.state = 2;
        signalState.dualTarget = true;
        signalState.value = wifiCallbackInfo.mEnabled;
        signalState.activityIn = wifiCallbackInfo.mEnabled && wifiCallbackInfo.mActivityIn;
        signalState.activityOut = wifiCallbackInfo.mEnabled && wifiCallbackInfo.mActivityOut;
        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer stringBuffer2 = new StringBuffer();
        Resources resources = this.mContext.getResources();
        signalState.label = resources.getString(C1894R.string.quick_settings_internet_label);
        if (wifiCallbackInfo.mAirplaneModeEnabled) {
            if (!signalState.value) {
                signalState.state = 1;
                signalState.icon = QSTileImpl.ResourceIcon.get(C1894R.C1896drawable.ic_qs_no_internet_unavailable);
                signalState.secondaryLabel = resources.getString(C1894R.string.status_bar_airplane);
            } else if (!z) {
                signalState.icon = QSTileImpl.ResourceIcon.get(C1894R.C1896drawable.ic_qs_no_internet_unavailable);
                if (wifiCallbackInfo.mNoNetworksAvailable) {
                    signalState.secondaryLabel = resources.getString(C1894R.string.quick_settings_networks_unavailable);
                } else {
                    signalState.secondaryLabel = resources.getString(C1894R.string.quick_settings_networks_available);
                }
            } else {
                signalState.icon = QSTileImpl.ResourceIcon.get(wifiCallbackInfo.mWifiSignalIconId);
            }
        } else if (wifiCallbackInfo.mNoDefaultNetwork) {
            if (wifiCallbackInfo.mNoNetworksAvailable || !wifiCallbackInfo.mEnabled) {
                signalState.icon = QSTileImpl.ResourceIcon.get(C1894R.C1896drawable.ic_qs_no_internet_unavailable);
                signalState.secondaryLabel = resources.getString(C1894R.string.quick_settings_networks_unavailable);
            } else {
                signalState.icon = QSTileImpl.ResourceIcon.get(C1894R.C1896drawable.ic_qs_no_internet_available);
                signalState.secondaryLabel = resources.getString(C1894R.string.quick_settings_networks_available);
            }
        } else if (wifiCallbackInfo.mIsTransient) {
            signalState.icon = QSTileImpl.ResourceIcon.get(17302865);
        } else if (!signalState.value) {
            signalState.slash.isSlashed = true;
            signalState.state = 1;
            signalState.icon = QSTileImpl.ResourceIcon.get(17302913);
        } else if (z) {
            signalState.icon = QSTileImpl.ResourceIcon.get(wifiCallbackInfo.mWifiSignalIconId);
        } else if (z2) {
            signalState.icon = QSTileImpl.ResourceIcon.get(17302913);
        } else {
            signalState.icon = QSTileImpl.ResourceIcon.get(17302913);
        }
        stringBuffer.append(this.mContext.getString(C1894R.string.quick_settings_internet_label)).append(NavigationBarInflaterView.BUTTON_SEPARATOR);
        if (signalState.value && z) {
            stringBuffer2.append(wifiCallbackInfo.mWifiSignalContentDescription);
            stringBuffer.append(removeDoubleQuotes(wifiCallbackInfo.mSsid));
        } else if (!TextUtils.isEmpty(signalState.secondaryLabel)) {
            stringBuffer.append(NavigationBarInflaterView.BUTTON_SEPARATOR).append(signalState.secondaryLabel);
        }
        signalState.stateDescription = stringBuffer2.toString();
        signalState.contentDescription = stringBuffer.toString();
        signalState.dualLabelContentDescription = resources.getString(C1894R.string.accessibility_quick_settings_open_settings, new Object[]{getTileLabel()});
        signalState.expandedAccessibilityClassName = Switch.class.getName();
        if (DEBUG) {
            Log.d(this.TAG, "handleUpdateWifiState: SignalState = " + signalState.toString());
        }
    }

    private void handleUpdateCellularState(QSTile.SignalState signalState, Object obj) {
        CellularCallbackInfo cellularCallbackInfo = (CellularCallbackInfo) obj;
        if (DEBUG) {
            Log.d(this.TAG, "handleUpdateCellularState: CellularCallbackInfo = " + cellularCallbackInfo.toString());
        }
        Resources resources = this.mContext.getResources();
        signalState.label = resources.getString(C1894R.string.quick_settings_internet_label);
        signalState.state = 2;
        boolean z = false;
        boolean z2 = this.mDataController.isMobileDataSupported() && this.mDataController.isMobileDataEnabled();
        signalState.value = z2;
        signalState.activityIn = z2 && cellularCallbackInfo.mActivityIn;
        if (z2 && cellularCallbackInfo.mActivityOut) {
            z = true;
        }
        signalState.activityOut = z;
        signalState.expandedAccessibilityClassName = Switch.class.getName();
        if (cellularCallbackInfo.mAirplaneModeEnabled && cellularCallbackInfo.mQsTypeIcon != TelephonyIcons.ICON_CWF) {
            signalState.state = 1;
            signalState.icon = QSTileImpl.ResourceIcon.get(C1894R.C1896drawable.ic_qs_no_internet_unavailable);
            signalState.secondaryLabel = resources.getString(C1894R.string.status_bar_airplane);
        } else if (!cellularCallbackInfo.mNoDefaultNetwork) {
            signalState.icon = new SignalIcon(cellularCallbackInfo.mMobileSignalIconId);
            signalState.secondaryLabel = appendMobileDataType(cellularCallbackInfo.mDataSubscriptionName, getMobileDataContentName(cellularCallbackInfo));
        } else if (cellularCallbackInfo.mNoNetworksAvailable || !this.mSignalCallback.mWifiInfo.mEnabled) {
            signalState.icon = QSTileImpl.ResourceIcon.get(C1894R.C1896drawable.ic_qs_no_internet_unavailable);
            signalState.secondaryLabel = resources.getString(C1894R.string.quick_settings_networks_unavailable);
        } else {
            signalState.icon = QSTileImpl.ResourceIcon.get(C1894R.C1896drawable.ic_qs_no_internet_available);
            signalState.secondaryLabel = resources.getString(C1894R.string.quick_settings_networks_available);
        }
        signalState.contentDescription = signalState.label;
        if (signalState.state == 1) {
            signalState.stateDescription = "";
        } else {
            signalState.stateDescription = signalState.secondaryLabel;
        }
        if (DEBUG) {
            Log.d(this.TAG, "handleUpdateCellularState: SignalState = " + signalState.toString());
        }
    }

    private void handleUpdateEthernetState(QSTile.SignalState signalState, Object obj) {
        EthernetCallbackInfo ethernetCallbackInfo = (EthernetCallbackInfo) obj;
        if (DEBUG) {
            Log.d(this.TAG, "handleUpdateEthernetState: EthernetCallbackInfo = " + ethernetCallbackInfo.toString());
        }
        signalState.label = this.mContext.getResources().getString(C1894R.string.quick_settings_internet_label);
        signalState.state = 2;
        signalState.icon = QSTileImpl.ResourceIcon.get(ethernetCallbackInfo.mEthernetSignalIconId);
        signalState.secondaryLabel = ethernetCallbackInfo.mEthernetContentDescription;
        if (DEBUG) {
            Log.d(this.TAG, "handleUpdateEthernetState: SignalState = " + signalState.toString());
        }
    }

    private CharSequence appendMobileDataType(CharSequence charSequence, CharSequence charSequence2) {
        String str = "";
        if (TextUtils.isEmpty(charSequence2)) {
            if (charSequence != null) {
                str = charSequence.toString();
            }
            return Html.fromHtml(str, 0);
        } else if (TextUtils.isEmpty(charSequence)) {
            if (charSequence2 != null) {
                str = charSequence2.toString();
            }
            return Html.fromHtml(str, 0);
        } else {
            return Html.fromHtml(this.mContext.getString(C1894R.string.mobile_carrier_text_format, new Object[]{charSequence, charSequence2}), 0);
        }
    }

    private CharSequence getMobileDataContentName(CellularCallbackInfo cellularCallbackInfo) {
        String str;
        if (cellularCallbackInfo.mRoaming && !TextUtils.isEmpty(cellularCallbackInfo.mDataContentDescription)) {
            String string = this.mContext.getString(C1894R.string.data_connection_roaming);
            if (cellularCallbackInfo.mDataContentDescription == null) {
                str = "";
            } else {
                str = cellularCallbackInfo.mDataContentDescription.toString();
            }
            return this.mContext.getString(C1894R.string.mobile_data_text_format, new Object[]{string, str});
        } else if (cellularCallbackInfo.mRoaming) {
            return this.mContext.getString(C1894R.string.data_connection_roaming);
        } else {
            return cellularCallbackInfo.mDataContentDescription;
        }
    }

    /* renamed from: com.android.systemui.qs.tiles.InternetTile$SignalIcon */
    private static class SignalIcon extends QSTile.Icon {
        private final int mState;

        SignalIcon(int i) {
            this.mState = i;
        }

        public int getState() {
            return this.mState;
        }

        public Drawable getDrawable(Context context) {
            SignalDrawable signalDrawable = new SignalDrawable(context);
            signalDrawable.setLevel(getState());
            return signalDrawable;
        }
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println(getClass().getSimpleName() + ":");
        printWriter.print("    ");
        printWriter.println(((QSTile.SignalState) getState()).toString());
        printWriter.print("    ");
        printWriter.println("mLastTileState=" + this.mLastTileState);
        printWriter.print("    ");
        printWriter.println("mSignalCallback=" + this.mSignalCallback.toString());
    }
}
