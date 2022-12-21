package com.android.systemui.p012qs.tiles;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Looper;
import android.sysprop.TelephonyProperties;
import android.view.View;
import android.widget.Switch;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.C1893R;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.p012qs.QSHost;
import com.android.systemui.p012qs.SettingObserver;
import com.android.systemui.p012qs.logging.QSLogger;
import com.android.systemui.p012qs.tileimpl.QSTileImpl;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.p011qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.util.settings.GlobalSettings;
import com.qti.extphone.ExtTelephonyManager;
import dagger.Lazy;
import javax.inject.Inject;

/* renamed from: com.android.systemui.qs.tiles.AirplaneModeTile */
public class AirplaneModeTile extends QSTileImpl<QSTile.BooleanState> {
    private final BroadcastDispatcher mBroadcastDispatcher;
    private final QSTile.Icon mIcon = QSTileImpl.ResourceIcon.get(17302827);
    private final Lazy<ConnectivityManager> mLazyConnectivityManager;
    private boolean mListening;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.AIRPLANE_MODE".equals(intent.getAction())) {
                AirplaneModeTile.this.refreshState();
            }
        }
    };
    private final SettingObserver mSetting;

    public int getMetricsCategory() {
        return 112;
    }

    @Inject
    public AirplaneModeTile(QSHost qSHost, @Background Looper looper, @Main Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, BroadcastDispatcher broadcastDispatcher, Lazy<ConnectivityManager> lazy, GlobalSettings globalSettings) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mLazyConnectivityManager = lazy;
        this.mSetting = new SettingObserver(globalSettings, this.mHandler, "airplane_mode_on") {
            /* access modifiers changed from: protected */
            public void handleValueChanged(int i, boolean z) {
                AirplaneModeTile.this.handleRefreshState(Integer.valueOf(i));
            }
        };
    }

    public QSTile.BooleanState newTileState() {
        return new QSTile.BooleanState();
    }

    public void handleClick(View view) {
        boolean z = ((QSTile.BooleanState) this.mState).value;
        MetricsLogger.action(this.mContext, getMetricsCategory(), !z);
        if (!z && ((Boolean) TelephonyProperties.in_ecm_mode().orElse(false)).booleanValue()) {
            this.mActivityStarter.postStartActivityDismissingKeyguard(new Intent("android.telephony.action.SHOW_NOTICE_ECM_BLOCK_OTHERS"), 0);
        } else if (z || !((Boolean) TelephonyProperties.in_scbm().orElse(false)).booleanValue()) {
            setEnabled(!z);
        } else {
            this.mActivityStarter.postStartActivityDismissingKeyguard(new Intent(ExtTelephonyManager.ACTION_SHOW_NOTICE_SCM_BLOCK_OTHERS), 0);
        }
    }

    private void setEnabled(boolean z) {
        this.mLazyConnectivityManager.get().setAirplaneMode(z);
    }

    public Intent getLongClickIntent() {
        return new Intent("android.settings.AIRPLANE_MODE_SETTINGS");
    }

    public CharSequence getTileLabel() {
        return this.mContext.getString(C1893R.string.airplane_mode);
    }

    /* access modifiers changed from: protected */
    public void handleUpdateState(QSTile.BooleanState booleanState, Object obj) {
        checkIfRestrictionEnforcedByAdminOnly(booleanState, "no_airplane_mode");
        int i = 1;
        boolean z = (obj instanceof Integer ? ((Integer) obj).intValue() : this.mSetting.getValue()) != 0;
        booleanState.value = z;
        booleanState.label = this.mContext.getString(C1893R.string.airplane_mode);
        booleanState.icon = this.mIcon;
        if (booleanState.slash == null) {
            booleanState.slash = new QSTile.SlashState();
        }
        booleanState.slash.isSlashed = !z;
        if (z) {
            i = 2;
        }
        booleanState.state = i;
        booleanState.contentDescription = booleanState.label;
        booleanState.expandedAccessibilityClassName = Switch.class.getName();
    }

    public void handleSetListening(boolean z) {
        super.handleSetListening(z);
        if (this.mListening != z) {
            this.mListening = z;
            if (z) {
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction("android.intent.action.AIRPLANE_MODE");
                this.mBroadcastDispatcher.registerReceiver(this.mReceiver, intentFilter);
            } else {
                this.mBroadcastDispatcher.unregisterReceiver(this.mReceiver);
            }
            this.mSetting.setListening(z);
        }
    }
}
