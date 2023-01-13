package com.nothing.systemui.p024qs.tiles;

import android.content.Intent;
import android.os.BatteryManager;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.widget.Switch;
import com.android.internal.logging.MetricsLogger;
import com.android.settingslib.media.MediaOutputConstants;
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
import com.nothing.systemui.statusbar.policy.BatteryShareController;
import javax.inject.Inject;

/* renamed from: com.nothing.systemui.qs.tiles.BatteryShareTile */
public class BatteryShareTile extends QSTileImpl<QSTile.BooleanState> {
    private static final String NT_REVERSE_CHARGING_LIMITING_LEVEL_KEY = "nt_reverse_charging_limiting_level";
    private final BatteryShareController mBatteryShareController;
    private final BatteryShareController.CallBack mCallBack;
    private final QSTile.Icon mIcon = QSTileImpl.ResourceIcon.get(C1894R.C1896drawable.ic_qs_battery_share);

    public int getMetricsCategory() {
        return 0;
    }

    @Inject
    public BatteryShareTile(QSHost qSHost, @Background Looper looper, @Main Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, BatteryShareController batteryShareController) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        C42041 r1 = new BatteryShareController.CallBack() {
            public void onBatteryShareChange() {
                BatteryShareTile.this.refreshState();
            }
        };
        this.mCallBack = r1;
        this.mBatteryShareController = batteryShareController;
        batteryShareController.observe(getLifecycle(), r1);
    }

    public QSTile.BooleanState newTileState() {
        return new QSTile.BooleanState();
    }

    /* access modifiers changed from: protected */
    public void handleClick(View view) {
        if (((QSTile.BooleanState) this.mState).state != 0) {
            this.mBatteryShareController.setBatteryShareEnable();
        }
    }

    /* access modifiers changed from: protected */
    public void handleUpdateState(QSTile.BooleanState booleanState, Object obj) {
        booleanState.icon = this.mIcon;
        booleanState.label = this.mContext.getString(C1894R.string.quick_settings_batteryshare_label);
        booleanState.expandedAccessibilityClassName = Switch.class.getName();
        booleanState.contentDescription = booleanState.label;
        booleanState.value = this.mBatteryShareController.getBatteryShareEnabled();
        int i = 1;
        boolean z = Settings.Global.getInt(this.mContext.getContentResolver(), NT_REVERSE_CHARGING_LIMITING_LEVEL_KEY, 15) > ((BatteryManager) this.mContext.getSystemService("batterymanager")).getIntProperty(4);
        if (this.mBatteryShareController.isWirelessCharging() || z) {
            i = 0;
        } else if (booleanState.value) {
            i = 2;
        }
        booleanState.state = i;
    }

    public Intent getLongClickIntent() {
        Intent intent = new Intent();
        intent.setAction("android.settings.ACTION_BATTERY_SHARE_SETTINGS");
        intent.setPackage(MediaOutputConstants.SETTINGS_PACKAGE_NAME);
        return intent;
    }

    public CharSequence getTileLabel() {
        return this.mContext.getString(C1894R.string.quick_settings_batteryshare_label);
    }
}
