package com.android.systemui.p012qs.tiles;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.telephony.SubscriptionManager;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Switch;
import com.android.internal.logging.MetricsLogger;
import com.android.settingslib.net.DataUsageController;
import com.android.systemui.C1893R;
import com.android.systemui.Prefs;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.p012qs.QSHost;
import com.android.systemui.p012qs.SignalTileView;
import com.android.systemui.p012qs.logging.QSLogger;
import com.android.systemui.p012qs.tileimpl.QSTileImpl;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.p011qs.QSIconView;
import com.android.systemui.plugins.p011qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.connectivity.IconState;
import com.android.systemui.statusbar.connectivity.MobileDataIndicators;
import com.android.systemui.statusbar.connectivity.NetworkController;
import com.android.systemui.statusbar.connectivity.SignalCallback;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.p024qs.QSTileHostEx;
import java.sql.Types;
import javax.inject.Inject;

/* renamed from: com.android.systemui.qs.tiles.CellularTile */
public class CellularTile extends QSTileImpl<QSTile.SignalState> {
    private static final String ENABLE_SETTINGS_DATA_PLAN = "enable.settings.data.plan";
    /* access modifiers changed from: private */
    public final NetworkController mController;
    private final DataUsageController mDataController;
    private final KeyguardStateController mKeyguard;
    private final CellSignalCallback mSignalCallback;

    public int getMetricsCategory() {
        return 115;
    }

    @Inject
    public CellularTile(QSHost qSHost, @Background Looper looper, @Main Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, NetworkController networkController, KeyguardStateController keyguardStateController) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        CellSignalCallback cellSignalCallback = new CellSignalCallback();
        this.mSignalCallback = cellSignalCallback;
        this.mController = networkController;
        this.mKeyguard = keyguardStateController;
        this.mDataController = networkController.getMobileDataController();
        networkController.observe(getLifecycle(), cellSignalCallback);
    }

    public QSTile.SignalState newTileState() {
        return new QSTile.SignalState();
    }

    public QSIconView createTileView(Context context) {
        SignalTileView signalTileView = new SignalTileView(context);
        signalTileView.setOnClickListener(new CellularTile$$ExternalSyntheticLambda0(this));
        signalTileView.setOnLongClickListener(new CellularTile$$ExternalSyntheticLambda1(this));
        return signalTileView;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$createTileView$0$com-android-systemui-qs-tiles-CellularTile */
    public /* synthetic */ void mo36859xa5811257(View view) {
        if (((QSTile.SignalState) getState()).state != 0) {
            if (this.mDataController.isMobileDataEnabled()) {
                maybeShowDisableDialog();
            } else {
                this.mDataController.setMobileDataEnabled(true);
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$createTileView$1$com-android-systemui-qs-tiles-CellularTile */
    public /* synthetic */ boolean mo36860x686d7bb6(View view) {
        handleLongClick(view);
        return true;
    }

    public Intent getLongClickIntent() {
        if (!((QSTileHostEx) NTDependencyEx.get(QSTileHostEx.class)).isQsExpanded() || ((QSTile.SignalState) getState()).state == 0) {
            return new Intent("android.settings.WIRELESS_SETTINGS");
        }
        return getCellularSettingIntent();
    }

    /* access modifiers changed from: protected */
    public void handleClick(View view) {
        if (this.mEx != null) {
            this.mEx.createInternetDialog((View) null, this.mActivityStarter);
        } else if (((QSTile.SignalState) getState()).state != 0) {
            if (this.mDataController.isMobileDataEnabled()) {
                maybeShowDisableDialog();
            } else {
                this.mDataController.setMobileDataEnabled(true);
            }
        }
    }

    private void maybeShowDisableDialog() {
        if (Prefs.getBoolean(this.mContext, Prefs.Key.QS_HAS_TURNED_OFF_MOBILE_DATA, false)) {
            this.mDataController.setMobileDataEnabled(false);
            return;
        }
        String mobileDataNetworkName = this.mController.getMobileDataNetworkName();
        boolean isMobileDataNetworkInService = this.mController.isMobileDataNetworkInService();
        if (TextUtils.isEmpty(mobileDataNetworkName) || !isMobileDataNetworkInService) {
            mobileDataNetworkName = this.mContext.getString(C1893R.string.mobile_data_disable_message_default_carrier);
        }
        AlertDialog create = new AlertDialog.Builder(this.mContext).setTitle(C1893R.string.mobile_data_disable_title).setMessage(this.mContext.getString(C1893R.string.mobile_data_disable_message, new Object[]{mobileDataNetworkName})).setNegativeButton(17039360, (DialogInterface.OnClickListener) null).setPositiveButton(17039659, new CellularTile$$ExternalSyntheticLambda2(this)).create();
        create.getWindow().setType(Types.SQLXML);
        SystemUIDialog.setShowForAllUsers(create, true);
        SystemUIDialog.registerDismissListener(create);
        SystemUIDialog.setWindowOnTop(create, this.mKeyguard.isShowing());
        create.show();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$maybeShowDisableDialog$2$com-android-systemui-qs-tiles-CellularTile */
    public /* synthetic */ void mo36861x40d52059(DialogInterface dialogInterface, int i) {
        this.mDataController.setMobileDataEnabled(false);
        Prefs.putBoolean(this.mContext, Prefs.Key.QS_HAS_TURNED_OFF_MOBILE_DATA, true);
    }

    /* access modifiers changed from: protected */
    public void handleSecondaryClick(View view) {
        handleLongClick(view);
    }

    public CharSequence getTileLabel() {
        return this.mContext.getString(C1893R.string.quick_settings_cellular_detail_title);
    }

    /* access modifiers changed from: protected */
    public void handleUpdateState(QSTile.SignalState signalState, Object obj) {
        CharSequence charSequence;
        CallbackInfo callbackInfo = (CallbackInfo) obj;
        if (callbackInfo == null) {
            callbackInfo = this.mSignalCallback.mInfo;
        }
        Resources resources = this.mContext.getResources();
        signalState.label = resources.getString(C1893R.string.mobile_data);
        boolean z = this.mDataController.isMobileDataSupported() && this.mDataController.isMobileDataEnabled();
        signalState.value = z;
        signalState.activityIn = z && callbackInfo.activityIn;
        signalState.activityOut = z && callbackInfo.activityOut;
        signalState.expandedAccessibilityClassName = Switch.class.getName();
        if (callbackInfo.noSim) {
            signalState.icon = QSTileImpl.ResourceIcon.get(C1893R.C1895drawable.ic_qs_no_sim);
        } else {
            signalState.icon = QSTileImpl.ResourceIcon.get(C1893R.C1895drawable.ic_swap_vert);
        }
        if (callbackInfo.noSim) {
            signalState.state = 0;
            signalState.secondaryLabel = resources.getString(C1893R.string.keyguard_missing_sim_message_short);
        } else if (callbackInfo.airplaneModeEnabled) {
            signalState.state = 0;
            signalState.secondaryLabel = resources.getString(C1893R.string.status_bar_airplane);
        } else if (z) {
            signalState.state = 2;
            CharSequence charSequence2 = callbackInfo.dataSubscriptionName;
            if (callbackInfo.showRat) {
                charSequence = getMobileDataContentName(callbackInfo);
            } else {
                charSequence = "";
            }
            signalState.secondaryLabel = appendMobileDataType(charSequence2, charSequence);
        } else {
            signalState.state = 1;
            signalState.secondaryLabel = resources.getString(C1893R.string.cell_data_off);
        }
        signalState.contentDescription = signalState.label;
        if (signalState.state == 1) {
            signalState.stateDescription = "";
        } else {
            signalState.stateDescription = signalState.secondaryLabel;
        }
    }

    private CharSequence appendMobileDataType(CharSequence charSequence, CharSequence charSequence2) {
        if (TextUtils.isEmpty(charSequence2)) {
            return Html.fromHtml(charSequence.toString(), 0);
        }
        if (TextUtils.isEmpty(charSequence)) {
            return Html.fromHtml(charSequence2.toString(), 0);
        }
        return Html.fromHtml(this.mContext.getString(C1893R.string.mobile_carrier_text_format, new Object[]{charSequence, charSequence2}), 0);
    }

    private CharSequence getMobileDataContentName(CallbackInfo callbackInfo) {
        if (callbackInfo.roaming && !TextUtils.isEmpty(callbackInfo.dataContentDescription)) {
            String string = this.mContext.getString(C1893R.string.data_connection_roaming);
            String charSequence = callbackInfo.dataContentDescription.toString();
            return this.mContext.getString(C1893R.string.mobile_data_text_format, new Object[]{string, charSequence});
        } else if (callbackInfo.roaming) {
            return this.mContext.getString(C1893R.string.data_connection_roaming);
        } else {
            return callbackInfo.dataContentDescription;
        }
    }

    public boolean isAvailable() {
        return this.mController.hasMobileDataFeature();
    }

    /* renamed from: com.android.systemui.qs.tiles.CellularTile$CallbackInfo */
    private static final class CallbackInfo {
        boolean activityIn;
        boolean activityOut;
        boolean airplaneModeEnabled;
        CharSequence dataContentDescription;
        CharSequence dataSubscriptionName;
        boolean multipleSubs;
        boolean noSim;
        boolean roaming;
        boolean showRat;

        private CallbackInfo() {
        }
    }

    /* renamed from: com.android.systemui.qs.tiles.CellularTile$CellSignalCallback */
    private final class CellSignalCallback implements SignalCallback {
        /* access modifiers changed from: private */
        public final CallbackInfo mInfo;

        private CellSignalCallback() {
            this.mInfo = new CallbackInfo();
        }

        public void setMobileDataIndicators(MobileDataIndicators mobileDataIndicators) {
            if (mobileDataIndicators.qsIcon != null) {
                this.mInfo.dataSubscriptionName = CellularTile.this.mController.getMobileDataNetworkName();
                this.mInfo.dataContentDescription = mobileDataIndicators.qsDescription != null ? mobileDataIndicators.typeContentDescriptionHtml : null;
                this.mInfo.activityIn = mobileDataIndicators.activityIn;
                this.mInfo.activityOut = mobileDataIndicators.activityOut;
                this.mInfo.roaming = mobileDataIndicators.roaming;
                CallbackInfo callbackInfo = this.mInfo;
                boolean z = true;
                if (CellularTile.this.mController.getNumberSubscriptions() <= 1) {
                    z = false;
                }
                callbackInfo.multipleSubs = z;
                this.mInfo.showRat = mobileDataIndicators.showRat;
                CellularTile.this.refreshState(this.mInfo);
            }
        }

        public void setNoSims(boolean z, boolean z2) {
            this.mInfo.noSim = z;
            CellularTile.this.refreshState(this.mInfo);
        }

        public void setIsAirplaneMode(IconState iconState) {
            this.mInfo.airplaneModeEnabled = iconState.visible;
            CellularTile.this.refreshState(this.mInfo);
        }
    }

    public static Intent getCellularSettingIntent() {
        Intent intent = new Intent("android.settings.NETWORK_OPERATOR_SETTINGS");
        if (SubscriptionManager.getDefaultDataSubscriptionId() != -1) {
            intent.putExtra("android.provider.extra.SUB_ID", SubscriptionManager.getDefaultDataSubscriptionId());
        }
        return intent;
    }
}
