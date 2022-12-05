package com.nothingos.systemui.qs.tiles;

import android.content.Intent;
import android.os.BatteryManager;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.widget.Switch;
import androidx.lifecycle.Lifecycle;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.R$drawable;
import com.android.systemui.R$string;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.nothingos.systemui.statusbar.policy.BatteryShareController;
/* loaded from: classes2.dex */
public class BatteryShareTile extends QSTileImpl<QSTile.BooleanState> {
    private final BatteryShareController mBatteryShareController;
    private final BatteryShareController.CallBack mCallBack;
    private final QSTile.Icon mIcon = QSTileImpl.ResourceIcon.get(R$drawable.ic_qs_battery_share);

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public int getMetricsCategory() {
        return 0;
    }

    public BatteryShareTile(QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, BatteryShareController batteryShareController) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        BatteryShareController.CallBack callBack = new BatteryShareController.CallBack() { // from class: com.nothingos.systemui.qs.tiles.BatteryShareTile.1
            @Override // com.nothingos.systemui.statusbar.policy.BatteryShareController.CallBack
            public void onBatteryShareChange() {
                BatteryShareTile.this.refreshState();
            }
        };
        this.mCallBack = callBack;
        this.mBatteryShareController = batteryShareController;
        batteryShareController.observe(mo1437getLifecycle(), (Lifecycle) callBack);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    /* renamed from: newTileState */
    public QSTile.BooleanState mo1926newTileState() {
        return new QSTile.BooleanState();
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    protected void handleClick(View view) {
        if (((QSTile.BooleanState) this.mState).state == 0) {
            return;
        }
        this.mBatteryShareController.setBatteryShareEnable();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public void handleUpdateState(QSTile.BooleanState booleanState, Object obj) {
        booleanState.icon = this.mIcon;
        booleanState.label = this.mContext.getString(R$string.quick_settings_batteryshare_label);
        booleanState.expandedAccessibilityClassName = Switch.class.getName();
        booleanState.contentDescription = booleanState.label;
        booleanState.value = this.mBatteryShareController.getBatteryShareEnabled();
        int i = 1;
        boolean z = Settings.Global.getInt(this.mContext.getContentResolver(), "nt_reverse_charging_limiting_level", 15) > ((BatteryManager) this.mContext.getSystemService("batterymanager")).getIntProperty(4);
        if (this.mBatteryShareController.isWirelessCharging() || z) {
            i = 0;
        } else if (booleanState.value) {
            i = 2;
        }
        booleanState.state = i;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public Intent getLongClickIntent() {
        Intent intent = new Intent();
        intent.setAction("android.settings.ACTION_BATTERY_SHARE_SETTINGS");
        intent.setPackage("com.android.settings");
        return intent;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public CharSequence getTileLabel() {
        return this.mContext.getString(R$string.quick_settings_batteryshare_label);
    }
}
