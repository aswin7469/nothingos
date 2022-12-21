package com.android.systemui.p012qs.tiles;

import android.content.Intent;
import android.content.res.Resources;
import android.hardware.SensorPrivacyManager;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Switch;
import androidx.lifecycle.LifecycleOwner;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.C1893R;
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
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.RotationLockController;
import com.android.systemui.statusbar.policy.RotationLockControllerImpl;
import com.android.systemui.util.settings.SecureSettings;
import javax.inject.Inject;

/* renamed from: com.android.systemui.qs.tiles.RotationLockTile */
public class RotationLockTile extends QSTileImpl<QSTile.BooleanState> implements BatteryController.BatteryStateChangeCallback {
    private final BatteryController mBatteryController;
    private final RotationLockController.RotationLockControllerCallback mCallback;
    private final RotationLockController mController;
    private final QSTile.Icon mIcon = QSTileImpl.ResourceIcon.get(17302828);
    private final SensorPrivacyManager mPrivacyManager;
    private final SensorPrivacyManager.OnSensorPrivacyChangedListener mSensorPrivacyChangedListener;
    private final SettingObserver mSetting;

    public int getMetricsCategory() {
        return 123;
    }

    @Inject
    public RotationLockTile(QSHost qSHost, @Background Looper looper, @Main Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, RotationLockController rotationLockController, SensorPrivacyManager sensorPrivacyManager, BatteryController batteryController, SecureSettings secureSettings) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        C24072 r2 = new RotationLockController.RotationLockControllerCallback() {
            public void onRotationLockStateChanged(boolean z, boolean z2) {
                RotationLockTile.this.refreshState(Boolean.valueOf(z));
            }
        };
        this.mCallback = r2;
        this.mSensorPrivacyChangedListener = new RotationLockTile$$ExternalSyntheticLambda0(this);
        this.mController = rotationLockController;
        rotationLockController.observe((LifecycleOwner) this, r2);
        this.mPrivacyManager = sensorPrivacyManager;
        this.mBatteryController = batteryController;
        SecureSettings secureSettings2 = secureSettings;
        this.mSetting = new SettingObserver(secureSettings2, this.mHandler, "camera_autorotate", qSHost.getUserContext().getUserId()) {
            /* access modifiers changed from: protected */
            public void handleValueChanged(int i, boolean z) {
                RotationLockTile.this.handleRefreshState((Object) null);
            }
        };
        batteryController.observe(getLifecycle(), this);
    }

    /* access modifiers changed from: protected */
    public void handleInitialize() {
        this.mPrivacyManager.addSensorPrivacyListener(2, this.mSensorPrivacyChangedListener);
    }

    public void onPowerSaveChanged(boolean z) {
        refreshState();
    }

    public QSTile.BooleanState newTileState() {
        return new QSTile.BooleanState();
    }

    public Intent getLongClickIntent() {
        return new Intent("android.settings.AUTO_ROTATE_SETTINGS");
    }

    /* access modifiers changed from: protected */
    public void handleClick(View view) {
        boolean z = !((QSTile.BooleanState) this.mState).value;
        this.mController.setRotationLocked(!z);
        refreshState(Boolean.valueOf(z));
    }

    public CharSequence getTileLabel() {
        return ((QSTile.BooleanState) getState()).label;
    }

    /* access modifiers changed from: protected */
    public void handleUpdateState(QSTile.BooleanState booleanState, Object obj) {
        boolean isRotationLocked = this.mController.isRotationLocked();
        int i = 2;
        boolean z = !this.mBatteryController.isPowerSave() && !this.mPrivacyManager.isSensorPrivacyEnabled(2) && RotationLockControllerImpl.hasSufficientPermission(this.mContext) && this.mController.isCameraRotationEnabled();
        booleanState.value = !isRotationLocked;
        booleanState.label = this.mContext.getString(C1893R.string.quick_settings_rotation_unlocked_label);
        booleanState.icon = this.mIcon;
        booleanState.contentDescription = getAccessibilityString(isRotationLocked);
        if (isRotationLocked || !z) {
            booleanState.secondaryLabel = "";
        } else {
            booleanState.secondaryLabel = this.mContext.getResources().getString(C1893R.string.rotation_lock_camera_rotation_on);
        }
        booleanState.stateDescription = booleanState.secondaryLabel;
        booleanState.expandedAccessibilityClassName = Switch.class.getName();
        if (!booleanState.value) {
            i = 1;
        }
        booleanState.state = i;
    }

    /* access modifiers changed from: protected */
    public void handleDestroy() {
        super.handleDestroy();
        this.mSetting.setListening(false);
        this.mPrivacyManager.removeSensorPrivacyListener(2, this.mSensorPrivacyChangedListener);
    }

    public void handleSetListening(boolean z) {
        super.handleSetListening(z);
        this.mSetting.setListening(z);
    }

    /* access modifiers changed from: protected */
    public void handleUserSwitch(int i) {
        this.mSetting.setUserId(i);
        handleRefreshState((Object) null);
    }

    public static boolean isCurrentOrientationLockPortrait(RotationLockController rotationLockController, Resources resources) {
        int rotationLockOrientation = rotationLockController.getRotationLockOrientation();
        if (rotationLockOrientation == 0) {
            if (resources.getConfiguration().orientation != 2) {
                return true;
            }
            return false;
        } else if (rotationLockOrientation != 2) {
            return true;
        } else {
            return false;
        }
    }

    private String getAccessibilityString(boolean z) {
        return this.mContext.getString(C1893R.string.accessibility_quick_settings_rotation);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-systemui-qs-tiles-RotationLockTile  reason: not valid java name */
    public /* synthetic */ void m2981lambda$new$0$comandroidsystemuiqstilesRotationLockTile(int i, boolean z) {
        refreshState();
    }
}
