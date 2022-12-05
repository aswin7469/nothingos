package com.android.systemui.qs.tiles;

import android.app.UiModeManager;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Switch;
import androidx.lifecycle.Lifecycle;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.R$string;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.LocationController;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
/* loaded from: classes.dex */
public class UiModeNightTile extends QSTileImpl<QSTile.BooleanState> implements ConfigurationController.ConfigurationListener, BatteryController.BatteryStateChangeCallback {
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
    private final BatteryController mBatteryController;
    private final QSTile.Icon mIcon = QSTileImpl.ResourceIcon.get(17302824);
    private final LocationController mLocationController;
    private UiModeManager mUiModeManager;

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public int getMetricsCategory() {
        return 1706;
    }

    public UiModeNightTile(QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, ConfigurationController configurationController, BatteryController batteryController, LocationController locationController) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        this.mBatteryController = batteryController;
        this.mUiModeManager = (UiModeManager) qSHost.getUserContext().getSystemService(UiModeManager.class);
        this.mLocationController = locationController;
        configurationController.observe(mo1437getLifecycle(), (Lifecycle) this);
        batteryController.observe(mo1437getLifecycle(), (Lifecycle) this);
    }

    @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
    public void onUiModeChanged() {
        refreshState();
    }

    @Override // com.android.systemui.statusbar.policy.BatteryController.BatteryStateChangeCallback
    public void onPowerSaveChanged(boolean z) {
        refreshState();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    /* renamed from: newTileState */
    public QSTile.BooleanState mo1926newTileState() {
        return new QSTile.BooleanState();
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    protected void handleClick(View view) {
        if (getState().state == 0) {
            return;
        }
        boolean z = !((QSTile.BooleanState) this.mState).value;
        this.mUiModeManager.setNightModeActivated(z);
        refreshState(Boolean.valueOf(z));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public void handleUpdateState(QSTile.BooleanState booleanState, Object obj) {
        LocalTime customNightModeStart;
        int i;
        int i2;
        int nightMode = this.mUiModeManager.getNightMode();
        boolean isPowerSave = this.mBatteryController.isPowerSave();
        int i3 = 1;
        boolean z = (this.mContext.getResources().getConfiguration().uiMode & 48) == 32;
        if (isPowerSave) {
            booleanState.secondaryLabel = this.mContext.getResources().getString(R$string.quick_settings_dark_mode_secondary_label_battery_saver);
        } else if (nightMode == 0 && this.mLocationController.isLocationEnabled()) {
            Resources resources = this.mContext.getResources();
            if (z) {
                i2 = R$string.quick_settings_dark_mode_secondary_label_until_sunrise;
            } else {
                i2 = R$string.quick_settings_dark_mode_secondary_label_on_at_sunset;
            }
            booleanState.secondaryLabel = resources.getString(i2);
        } else if (nightMode == 3) {
            boolean is24HourFormat = DateFormat.is24HourFormat(this.mContext);
            if (z) {
                customNightModeStart = this.mUiModeManager.getCustomNightModeEnd();
            } else {
                customNightModeStart = this.mUiModeManager.getCustomNightModeStart();
            }
            Resources resources2 = this.mContext.getResources();
            if (z) {
                i = R$string.quick_settings_dark_mode_secondary_label_until;
            } else {
                i = R$string.quick_settings_dark_mode_secondary_label_on_at;
            }
            Object[] objArr = new Object[1];
            objArr[0] = is24HourFormat ? customNightModeStart.toString() : formatter.format(customNightModeStart);
            booleanState.secondaryLabel = resources2.getString(i, objArr);
        } else {
            booleanState.secondaryLabel = null;
        }
        booleanState.value = z;
        booleanState.label = this.mContext.getString(R$string.quick_settings_ui_mode_night_label);
        booleanState.icon = this.mIcon;
        booleanState.contentDescription = TextUtils.isEmpty(booleanState.secondaryLabel) ? booleanState.label : TextUtils.concat(booleanState.label, ", ", booleanState.secondaryLabel);
        if (isPowerSave) {
            booleanState.state = 0;
        } else {
            if (booleanState.value) {
                i3 = 2;
            }
            booleanState.state = i3;
        }
        booleanState.showRippleEffect = false;
        booleanState.expandedAccessibilityClassName = Switch.class.getName();
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public Intent getLongClickIntent() {
        return new Intent("android.settings.DARK_THEME_SETTINGS");
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public CharSequence getTileLabel() {
        return getState().label;
    }
}
