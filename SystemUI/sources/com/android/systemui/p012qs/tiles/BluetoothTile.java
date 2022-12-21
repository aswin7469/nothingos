package com.android.systemui.p012qs.tiles;

import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.widget.Switch;
import com.android.internal.logging.MetricsLogger;
import com.android.settingslib.Utils;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.systemui.C1893R;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.p012qs.QSHost;
import com.android.systemui.p012qs.logging.QSLogger;
import com.android.systemui.p012qs.tileimpl.QSTileImpl;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.p011qs.QSIconView;
import com.android.systemui.plugins.p011qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.policy.BluetoothController;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.p024qs.CircleDrawable;
import com.nothing.systemui.p024qs.QSPanelExpandListener;
import com.nothing.systemui.p024qs.tiles.BluetoothTileEx;
import com.nothing.systemui.statusbar.policy.TeslaInfoController;
import com.nothing.systemui.util.NTLogUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.inject.Inject;

/* renamed from: com.android.systemui.qs.tiles.BluetoothTile */
public class BluetoothTile extends QSTileImpl<QSTile.BooleanState> {
    private static final Intent BLUETOOTH_SETTINGS = new Intent("android.settings.BLUETOOTH_SETTINGS");
    BluetoothTileEx mBluetoothTileEx = ((BluetoothTileEx) NTDependencyEx.get(BluetoothTileEx.class));
    private final BluetoothController.Callback mCallback;
    /* access modifiers changed from: private */
    public final BluetoothController mController;
    private QSPanelExpandListener mQSPanelExpandListener = new QSPanelExpandListener() {
        public void setExpanded(boolean z) {
            NTLogUtil.m1680d(BluetoothTile.this.TAG, "setExpanded: " + z);
            if (z && BluetoothTile.this.mController.isBluetoothEnabled() && BluetoothTile.this.mController.isBluetoothConnected()) {
                BluetoothTile.this.mBluetoothTileEx.getCommandBattery();
            }
        }
    };
    private final TeslaInfoController.Callback mTeslaCallback;
    private final TeslaInfoController mTeslaController;

    public int getMetricsCategory() {
        return 113;
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    @Inject
    public BluetoothTile(QSHost qSHost, @Background Looper looper, @Main Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, BluetoothController bluetoothController, TeslaInfoController teslaInfoController) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        BluetoothController bluetoothController2 = bluetoothController;
        TeslaInfoController teslaInfoController2 = teslaInfoController;
        C23882 r0 = new BluetoothController.Callback() {
            public void onBluetoothStateChange(boolean z) {
                BluetoothTile.this.refreshState();
                BluetoothTile.this.mBluetoothTileEx.updateDeviceService(z, BluetoothTile.this.mController.isBluetoothConnected());
            }

            public void onBluetoothDevicesChanged() {
                BluetoothTile.this.refreshState();
            }
        };
        this.mCallback = r0;
        C23893 r8 = new TeslaInfoController.Callback() {
            public void onActiveStateChanged() {
                BluetoothTile.this.refreshState();
            }

            public void onInfoChanged() {
                BluetoothTile.this.refreshState();
            }
        };
        this.mTeslaCallback = r8;
        this.mController = bluetoothController2;
        bluetoothController2.observe(getLifecycle(), r0);
        this.mBluetoothTileEx.init(this.mContext, bluetoothController2, teslaInfoController, this, handler);
        QSHost qSHost2 = qSHost;
        qSHost.setQSPanelExpandListener(this.mQSPanelExpandListener);
        this.mTeslaController = teslaInfoController2;
        teslaInfoController2.observe(getLifecycle(), r8);
    }

    public QSTile.BooleanState newTileState() {
        QSTile.BooleanState booleanState = new QSTile.BooleanState();
        booleanState.iconList = new ArrayList(1);
        booleanState.labelList = new ArrayList(1);
        booleanState.secondaryLabelList = new ArrayList(1);
        booleanState.addressList = new ArrayList(1);
        booleanState.stateList = new int[10];
        return booleanState;
    }

    public QSIconView createTileView(Context context) {
        QSIconView createTileView = super.createTileView(context);
        this.mBluetoothTileEx.setClickListener(createTileView);
        return createTileView;
    }

    public void handleIconClick(View view, boolean z) {
        if (z) {
            super.handleLongClick(view);
        } else {
            handleClick(view);
        }
    }

    public void refreshStateInternal(Object obj) {
        refreshState(obj);
    }

    /* access modifiers changed from: protected */
    public void handleClick(View view) {
        Object obj = null;
        if (this.mEx != null) {
            this.mBluetoothTileEx.handleClick();
            this.mEx.createBluetoothDialog((View) null, this.mActivityStarter);
            return;
        }
        boolean z = ((QSTile.BooleanState) this.mState).value;
        if (!z) {
            obj = ARG_SHOW_TRANSIENT_ENABLING;
        }
        refreshState(obj);
        this.mController.setBluetoothEnabled(!z);
    }

    public Intent getLongClickIntent() {
        BluetoothTileEx bluetoothTileEx = this.mBluetoothTileEx;
        if (bluetoothTileEx != null) {
            return bluetoothTileEx.getLongClickIntentAndUpdateClickItem();
        }
        return new Intent("android.settings.BLUETOOTH_SETTINGS");
    }

    /* access modifiers changed from: protected */
    public void handleSecondaryClick(View view) {
        if (!this.mController.canConfigBluetooth()) {
            this.mActivityStarter.postStartActivityDismissingKeyguard(new Intent("android.settings.BLUETOOTH_SETTINGS"), 0);
        } else if (!((QSTile.BooleanState) this.mState).value) {
            this.mController.setBluetoothEnabled(true);
        }
    }

    public CharSequence getTileLabel() {
        return this.mContext.getString(C1893R.string.quick_settings_bluetooth_label);
    }

    /* access modifiers changed from: protected */
    public void handleUpdateState(QSTile.BooleanState booleanState, Object obj) {
        boolean z;
        QSTile.State state;
        boolean z2;
        boolean z3;
        Drawable drawable;
        boolean z4;
        booleanState.iconList.clear();
        booleanState.labelList.clear();
        booleanState.secondaryLabelList.clear();
        booleanState.addressList.clear();
        checkIfRestrictionEnforcedByAdminOnly(booleanState, "no_bluetooth");
        boolean z5 = obj == ARG_SHOW_TRANSIENT_ENABLING;
        boolean z6 = z5 || this.mController.isBluetoothEnabled();
        boolean isBluetoothConnected = this.mController.isBluetoothConnected();
        boolean isBluetoothConnecting = this.mController.isBluetoothConnecting();
        booleanState.isTransient = z5 || isBluetoothConnecting || this.mController.getBluetoothState() == 11;
        booleanState.dualTarget = true;
        booleanState.value = z6;
        if (booleanState.slash == null) {
            booleanState.slash = new QSTile.SlashState();
        }
        booleanState.slash.isSlashed = !z6;
        booleanState.label = this.mContext.getString(C1893R.string.quick_settings_bluetooth_label);
        booleanState.secondaryLabel = TextUtils.emptyIfNull(getSecondaryLabel(z6, isBluetoothConnecting, isBluetoothConnected, booleanState.isTransient));
        booleanState.contentDescription = this.mContext.getString(C1893R.string.accessibility_quick_settings_bluetooth);
        booleanState.stateDescription = "";
        boolean shouldShowTeslaInfo = this.mTeslaController.shouldShowTeslaInfo();
        if (shouldShowTeslaInfo) {
            state = booleanState.copy();
            booleanState.icon = this.mTeslaController.getProfile();
            booleanState.icon.skipTintBt = true;
            booleanState.icon.isTesla = true;
            booleanState.label = this.mTeslaController.getUserName();
            booleanState.secondaryLabel = this.mTeslaController.getBatteryRange();
            z = false;
        } else {
            if (booleanState.icon != null) {
                booleanState.icon.isTesla = false;
                booleanState.icon.skipTintBt = false;
            }
            z = true;
            state = null;
        }
        int i = 2;
        if (z6) {
            if (isBluetoothConnected) {
                List<CachedBluetoothDevice> connectedDevices = this.mController.getConnectedDevices();
                CachedBluetoothDevice activeDevice = this.mController.getActiveDevice();
                int size = connectedDevices.size();
                if (size > 1 && activeDevice != null) {
                    int i2 = 0;
                    while (true) {
                        if (i2 < size) {
                            if (Objects.equals(connectedDevices.get(i2), activeDevice) && i2 != 0) {
                                Collections.swap((List<?>) connectedDevices, 0, i2);
                                break;
                            }
                            i2++;
                        } else {
                            break;
                        }
                    }
                }
                for (CachedBluetoothDevice next : connectedDevices) {
                    BluetoothTileEx bluetoothTileEx = this.mBluetoothTileEx;
                    String bLEModuleForSettingGlobal = bluetoothTileEx.getBLEModuleForSettingGlobal(bluetoothTileEx.changeToSSPAdress(String.valueOf((Object) next.getAddress())));
                    if (TextUtils.isEmpty(bLEModuleForSettingGlobal) && this.mBluetoothTileEx.isAirpodsExperimentOn()) {
                        bLEModuleForSettingGlobal = this.mBluetoothTileEx.getAirpodsVersion(String.valueOf((Object) next.getAddress()));
                    }
                    Drawable moduleIDBitmap = !TextUtils.isEmpty(bLEModuleForSettingGlobal) ? this.mBluetoothTileEx.getModuleIDBitmap(bLEModuleForSettingGlobal) : null;
                    Pair<Drawable, Boolean> drawableWithDescriptionWithoutRainbow = next.getDrawableWithDescriptionWithoutRainbow();
                    if (moduleIDBitmap != null) {
                        drawable = moduleIDBitmap;
                    } else {
                        drawable = ((Boolean) drawableWithDescriptionWithoutRainbow.second).booleanValue() ? new CircleDrawable((Drawable) drawableWithDescriptionWithoutRainbow.first) : (Drawable) drawableWithDescriptionWithoutRainbow.first;
                    }
                    BluetoothConnectedTileIcon bluetoothConnectedTileIcon = new BluetoothConnectedTileIcon(drawable);
                    if (moduleIDBitmap != null) {
                        z4 = true;
                    } else {
                        z4 = ((Boolean) drawableWithDescriptionWithoutRainbow.second).booleanValue();
                    }
                    bluetoothConnectedTileIcon.skipTintBt = z4;
                    String name = next.getName();
                    CharSequence deviceSecondLabel = this.mBluetoothTileEx.getDeviceSecondLabel(next);
                    String address = next.getAddress();
                    if (z) {
                        booleanState.icon = bluetoothConnectedTileIcon;
                        booleanState.label = name;
                        booleanState.secondaryLabel = deviceSecondLabel;
                        z = false;
                    } else {
                        booleanState.iconList.add(bluetoothConnectedTileIcon);
                        booleanState.labelList.add(name);
                        booleanState.secondaryLabelList.add(deviceSecondLabel);
                        booleanState.addressList.add(address);
                    }
                }
                booleanState.stateDescription = this.mContext.getString(C1893R.string.accessibility_bluetooth_name, new Object[]{booleanState.label}) + ", " + booleanState.secondaryLabel;
                z2 = false;
            } else if (booleanState.isTransient) {
                if (shouldShowTeslaInfo) {
                    state.icon = QSTileImpl.ResourceIcon.get(17302334);
                    z2 = true;
                } else {
                    booleanState.icon = QSTileImpl.ResourceIcon.get(17302334);
                    z2 = false;
                }
                booleanState.stateDescription = booleanState.secondaryLabel;
            } else {
                if (shouldShowTeslaInfo) {
                    state.icon = QSTileImpl.ResourceIcon.get(17302830);
                    z3 = true;
                } else {
                    booleanState.icon = QSTileImpl.ResourceIcon.get(17302830);
                    z3 = false;
                }
                booleanState.stateDescription = this.mContext.getString(C1893R.string.accessibility_not_connected);
            }
            if (!shouldShowTeslaInfo) {
                booleanState.state = 2;
            }
            int size2 = booleanState.addressList.size();
            if (size2 > 0) {
                for (int i3 = 0; i3 < size2; i3++) {
                    booleanState.stateList[i3] = 2;
                }
            } else if (shouldShowTeslaInfo) {
                booleanState.stateList[0] = 2;
            }
        } else if (shouldShowTeslaInfo) {
            state.icon = QSTileImpl.ResourceIcon.get(17302830);
            booleanState.stateList[0] = 1;
            z2 = true;
        } else {
            booleanState.icon = QSTileImpl.ResourceIcon.get(17302830);
            booleanState.state = 1;
            z2 = false;
        }
        if (z2) {
            booleanState.iconList.add(state.icon);
            booleanState.labelList.add(state.label);
            booleanState.secondaryLabelList.add(state.secondaryLabel);
            booleanState.addressList.add(BluetoothTileEx.ADDRESS_FAKE);
        }
        if (shouldShowTeslaInfo) {
            if (!this.mTeslaController.isConnected()) {
                i = 1;
            }
            booleanState.state = i;
        }
        booleanState.dualLabelContentDescription = this.mContext.getResources().getString(C1893R.string.accessibility_quick_settings_open_settings, new Object[]{getTileLabel()});
        booleanState.expandedAccessibilityClassName = Switch.class.getName();
    }

    private String getSecondaryLabel(boolean z, boolean z2, boolean z3, boolean z4) {
        if (z2) {
            return this.mContext.getString(C1893R.string.quick_settings_connecting);
        }
        if (z4) {
            return this.mContext.getString(C1893R.string.quick_settings_bluetooth_secondary_label_transient);
        }
        List<CachedBluetoothDevice> connectedDevices = this.mController.getConnectedDevices();
        if (!z || !z3 || connectedDevices.isEmpty()) {
            return null;
        }
        if (connectedDevices.size() > 1) {
            return this.mContext.getResources().getQuantityString(C1893R.plurals.quick_settings_hotspot_secondary_label_num_devices, connectedDevices.size(), new Object[]{Integer.valueOf(connectedDevices.size())});
        }
        CachedBluetoothDevice cachedBluetoothDevice = connectedDevices.get(0);
        int batteryLevel = cachedBluetoothDevice.getBatteryLevel();
        if (!(this.mBluetoothTileEx.getBluetoothBatteryDates() == null || this.mBluetoothTileEx.getBluetoothBatteryDates().get(cachedBluetoothDevice.getAddress()) == null)) {
            String batteryLevel2 = this.mBluetoothTileEx.getBatteryLevel(this.mBluetoothTileEx.getBluetoothBatteryDates().get(cachedBluetoothDevice.getAddress()));
            if (!TextUtils.isEmpty(batteryLevel2)) {
                return batteryLevel2;
            }
        }
        if (batteryLevel > -1) {
            return this.mContext.getString(C1893R.string.quick_settings_bluetooth_secondary_label_battery_level, new Object[]{Utils.formatPercentage(batteryLevel)});
        }
        BluetoothClass btClass = cachedBluetoothDevice.getBtClass();
        if (btClass == null) {
            return null;
        }
        if (cachedBluetoothDevice.isHearingAidDevice()) {
            return this.mContext.getString(C1893R.string.quick_settings_bluetooth_secondary_label_hearing_aids);
        }
        if (btClass.doesClassMatch(1)) {
            return this.mContext.getString(C1893R.string.quick_settings_bluetooth_secondary_label_audio);
        }
        if (btClass.doesClassMatch(0)) {
            return this.mContext.getString(C1893R.string.quick_settings_bluetooth_secondary_label_headset);
        }
        if (btClass.doesClassMatch(3)) {
            return this.mContext.getString(C1893R.string.quick_settings_bluetooth_secondary_label_input);
        }
        return null;
    }

    public boolean isAvailable() {
        return this.mController.isBluetoothSupported();
    }

    /* renamed from: com.android.systemui.qs.tiles.BluetoothTile$BluetoothConnectedTileIcon */
    private class BluetoothConnectedTileIcon extends QSTile.Icon {
        private Drawable drawable;

        BluetoothConnectedTileIcon(Drawable drawable2) {
            this.drawable = drawable2;
        }

        BluetoothConnectedTileIcon() {
        }

        public Drawable getDrawable(Context context) {
            Drawable drawable2 = this.drawable;
            if (drawable2 != null) {
                return drawable2;
            }
            return context.getDrawable(C1893R.C1895drawable.ic_bluetooth_connected);
        }
    }
}
