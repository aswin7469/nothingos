package com.android.systemui.qs.tiles;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.hardware.SensorPrivacyManager;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Switch;
import androidx.appcompat.R$styleable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.R$string;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.SecureSetting;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.RotationLockController;
import com.android.systemui.util.settings.SecureSettings;
/* loaded from: classes.dex */
public class RotationLockTile extends QSTileImpl<QSTile.BooleanState> implements BatteryController.BatteryStateChangeCallback {
    private final BatteryController mBatteryController;
    private final RotationLockController.RotationLockControllerCallback mCallback;
    private final RotationLockController mController;
    private final QSTile.Icon mIcon = QSTileImpl.ResourceIcon.get(17302818);
    private final SensorPrivacyManager mPrivacyManager;
    private final SecureSetting mSetting;

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public int getMetricsCategory() {
        return R$styleable.AppCompatTheme_windowFixedWidthMinor;
    }

    public RotationLockTile(QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, RotationLockController rotationLockController, SensorPrivacyManager sensorPrivacyManager, BatteryController batteryController, SecureSettings secureSettings) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        RotationLockController.RotationLockControllerCallback rotationLockControllerCallback = new RotationLockController.RotationLockControllerCallback() { // from class: com.android.systemui.qs.tiles.RotationLockTile.2
            @Override // com.android.systemui.statusbar.policy.RotationLockController.RotationLockControllerCallback
            public void onRotationLockStateChanged(boolean z, boolean z2) {
                RotationLockTile.this.refreshState(Boolean.valueOf(z));
            }
        };
        this.mCallback = rotationLockControllerCallback;
        this.mController = rotationLockController;
        rotationLockController.observe((LifecycleOwner) this, (RotationLockTile) rotationLockControllerCallback);
        this.mPrivacyManager = sensorPrivacyManager;
        this.mBatteryController = batteryController;
        sensorPrivacyManager.addSensorPrivacyListener(2, new SensorPrivacyManager.OnSensorPrivacyChangedListener() { // from class: com.android.systemui.qs.tiles.RotationLockTile$$ExternalSyntheticLambda0
            public final void onSensorPrivacyChanged(int i, boolean z) {
                RotationLockTile.this.lambda$new$0(i, z);
            }
        });
        this.mSetting = new SecureSetting(secureSettings, this.mHandler, "camera_autorotate", qSHost.getUserContext().getUserId()) { // from class: com.android.systemui.qs.tiles.RotationLockTile.1
            @Override // com.android.systemui.qs.SecureSetting
            protected void handleValueChanged(int i, boolean z) {
                RotationLockTile.this.handleRefreshState(Integer.valueOf(i));
            }
        };
        batteryController.observe(mo1437getLifecycle(), (Lifecycle) this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(int i, boolean z) {
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
    public Intent getLongClickIntent() {
        return new Intent("android.settings.AUTO_ROTATE_SETTINGS");
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    protected void handleClick(View view) {
        boolean z = !((QSTile.BooleanState) this.mState).value;
        this.mController.setRotationLocked(!z);
        refreshState(Boolean.valueOf(z));
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public CharSequence getTileLabel() {
        return getState().label;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public void handleUpdateState(QSTile.BooleanState booleanState, Object obj) {
        boolean isRotationLocked = this.mController.isRotationLocked();
        int i = 2;
        boolean z = !this.mBatteryController.isPowerSave() && !this.mPrivacyManager.isSensorPrivacyEnabled(2) && hasSufficientPermission(this.mContext) && this.mController.isCameraRotationEnabled();
        booleanState.value = !isRotationLocked;
        booleanState.label = this.mContext.getString(R$string.quick_settings_rotation_unlocked_label);
        booleanState.icon = this.mIcon;
        booleanState.contentDescription = getAccessibilityString(isRotationLocked);
        if (!isRotationLocked && z) {
            booleanState.secondaryLabel = this.mContext.getResources().getString(R$string.rotation_lock_camera_rotation_on);
        } else {
            booleanState.secondaryLabel = "";
        }
        booleanState.expandedAccessibilityClassName = Switch.class.getName();
        if (!booleanState.value) {
            i = 1;
        }
        booleanState.state = i;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public void handleUserSwitch(int i) {
        this.mSetting.setUserId(i);
        handleRefreshState(Integer.valueOf(this.mSetting.getValue()));
    }

    public static boolean isCurrentOrientationLockPortrait(RotationLockController rotationLockController, Resources resources) {
        int rotationLockOrientation = rotationLockController.getRotationLockOrientation();
        return rotationLockOrientation == 0 ? resources.getConfiguration().orientation != 2 : rotationLockOrientation != 2;
    }

    private String getAccessibilityString(boolean z) {
        return this.mContext.getString(R$string.accessibility_quick_settings_rotation);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    protected String composeChangeAnnouncement() {
        return getAccessibilityString(((QSTile.BooleanState) this.mState).value);
    }

    private boolean hasSufficientPermission(Context context) {
        PackageManager packageManager = context.getPackageManager();
        String rotationResolverPackageName = packageManager.getRotationResolverPackageName();
        return rotationResolverPackageName != null && packageManager.checkPermission("android.permission.CAMERA", rotationResolverPackageName) == 0;
    }
}
