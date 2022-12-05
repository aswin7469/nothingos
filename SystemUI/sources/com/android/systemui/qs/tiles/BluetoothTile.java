package com.android.systemui.qs.tiles;

import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import androidx.lifecycle.Lifecycle;
import com.android.internal.logging.MetricsLogger;
import com.android.settingslib.Utils;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.settingslib.graph.BluetoothDeviceLayerDrawable;
import com.android.settingslib.graph.CircleDrawable;
import com.android.systemui.R$drawable;
import com.android.systemui.R$plurals;
import com.android.systemui.R$string;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.DetailAdapter;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSDetailItems;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.statusbar.policy.BluetoothController;
import com.nothingos.systemui.statusbar.policy.TeslaInfoController;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public class BluetoothTile extends QSTileImpl<QSTile.BooleanState> {
    private static final Intent BLUETOOTH_SETTINGS = new Intent("android.settings.BLUETOOTH_SETTINGS");
    private final BluetoothController.Callback mCallback;
    private final BluetoothController mController;
    private final BluetoothDetailAdapter mDetailAdapter = (BluetoothDetailAdapter) createDetailAdapter();
    private final TeslaInfoController.Callback mTeslaCallback;
    private final TeslaInfoController mTeslaController;

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public int getMetricsCategory() {
        return 113;
    }

    public BluetoothTile(QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, BluetoothController bluetoothController, TeslaInfoController teslaInfoController) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        BluetoothController.Callback callback = new BluetoothController.Callback() { // from class: com.android.systemui.qs.tiles.BluetoothTile.1
            @Override // com.android.systemui.statusbar.policy.BluetoothController.Callback
            public void onBluetoothStateChange(boolean z) {
                BluetoothTile.this.refreshState();
                if (BluetoothTile.this.isShowingDetail()) {
                    BluetoothTile.this.mDetailAdapter.updateItems();
                    BluetoothTile bluetoothTile = BluetoothTile.this;
                    bluetoothTile.fireToggleStateChanged(bluetoothTile.mDetailAdapter.getToggleState().booleanValue());
                }
            }

            @Override // com.android.systemui.statusbar.policy.BluetoothController.Callback
            public void onBluetoothDevicesChanged() {
                BluetoothTile.this.refreshState();
                if (BluetoothTile.this.isShowingDetail()) {
                    BluetoothTile.this.mDetailAdapter.updateItems();
                }
            }
        };
        this.mCallback = callback;
        TeslaInfoController.Callback callback2 = new TeslaInfoController.Callback() { // from class: com.android.systemui.qs.tiles.BluetoothTile.2
            @Override // com.nothingos.systemui.statusbar.policy.TeslaInfoController.Callback
            public void onActiveStateChanged() {
                BluetoothTile.this.refreshState();
            }

            @Override // com.nothingos.systemui.statusbar.policy.TeslaInfoController.Callback
            public void onInfoChanged() {
                BluetoothTile.this.refreshState();
            }
        };
        this.mTeslaCallback = callback2;
        this.mController = bluetoothController;
        bluetoothController.observe(mo1437getLifecycle(), (Lifecycle) callback);
        this.mTeslaController = teslaInfoController;
        teslaInfoController.observe(mo1437getLifecycle(), (Lifecycle) callback2);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public DetailAdapter getDetailAdapter() {
        return this.mDetailAdapter;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    /* renamed from: newTileState */
    public QSTile.BooleanState mo1926newTileState() {
        QSTile.BooleanState booleanState = new QSTile.BooleanState();
        booleanState.iconList = new ArrayList<>(1);
        booleanState.labelList = new ArrayList<>(1);
        booleanState.secondaryLabelList = new ArrayList<>(1);
        booleanState.addressList = new ArrayList<>(1);
        booleanState.stateList = new int[10];
        return booleanState;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    protected void handleClick(View view) {
        this.mActivityStarter.postStartActivityDismissingKeyguard(QSTileImpl.BLUETOOTH_PANEL, 0);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public Intent getLongClickIntent() {
        return new Intent("android.settings.BLUETOOTH_SETTINGS");
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    protected void handleSecondaryClick(View view) {
        if (!this.mController.canConfigBluetooth()) {
            this.mActivityStarter.postStartActivityDismissingKeyguard(new Intent("android.settings.BLUETOOTH_SETTINGS"), 0);
            return;
        }
        showDetail(true);
        if (((QSTile.BooleanState) this.mState).value) {
            return;
        }
        this.mController.setBluetoothEnabled(true);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public CharSequence getTileLabel() {
        return this.mContext.getString(R$string.quick_settings_bluetooth_label);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public void handleUpdateState(QSTile.BooleanState booleanState, Object obj) {
        boolean z;
        boolean z2;
        booleanState.iconList.clear();
        booleanState.labelList.clear();
        booleanState.secondaryLabelList.clear();
        booleanState.addressList.clear();
        boolean z3 = obj == QSTileImpl.ARG_SHOW_TRANSIENT_ENABLING;
        boolean z4 = z3 || this.mController.isBluetoothEnabled();
        boolean isBluetoothConnected = this.mController.isBluetoothConnected();
        boolean isBluetoothConnecting = this.mController.isBluetoothConnecting();
        booleanState.isTransient = z3 || isBluetoothConnecting || this.mController.getBluetoothState() == 11;
        booleanState.dualTarget = true;
        booleanState.value = z4;
        if (booleanState.slash == null) {
            booleanState.slash = new QSTile.SlashState();
        }
        booleanState.slash.isSlashed = !z4;
        booleanState.label = this.mContext.getString(R$string.quick_settings_bluetooth_label);
        booleanState.secondaryLabel = TextUtils.emptyIfNull(getSecondaryLabel(z4, isBluetoothConnecting, isBluetoothConnected, booleanState.isTransient));
        booleanState.contentDescription = this.mContext.getString(R$string.accessibility_quick_settings_bluetooth);
        booleanState.stateDescription = "";
        boolean shouldShowTeslaInfo = this.mTeslaController.shouldShowTeslaInfo();
        QSTile.State state = null;
        if (shouldShowTeslaInfo) {
            state = booleanState.copy();
            QSTile.Icon profile = this.mTeslaController.getProfile();
            booleanState.icon = profile;
            profile.skipTintBt = true;
            profile.isTesla = true;
            booleanState.label = this.mTeslaController.getUserName();
            booleanState.secondaryLabel = this.mTeslaController.getBatteryRange();
            z = false;
        } else {
            QSTile.Icon icon = booleanState.icon;
            if (icon != null) {
                icon.isTesla = false;
                icon.skipTintBt = false;
            }
            z = true;
        }
        int i = 2;
        if (z4) {
            if (isBluetoothConnected) {
                List<CachedBluetoothDevice> connectedDevices = this.mController.getConnectedDevices();
                CachedBluetoothDevice activeDevice = this.mController.getActiveDevice();
                int size = connectedDevices.size();
                if (size > 1 && activeDevice != null) {
                    int i2 = 0;
                    while (true) {
                        if (i2 >= size) {
                            break;
                        }
                        if (Objects.equals(connectedDevices.get(i2), activeDevice) && i2 != 0) {
                            Collections.swap(connectedDevices, 0, i2);
                            break;
                        }
                        i2++;
                    }
                }
                for (CachedBluetoothDevice cachedBluetoothDevice : connectedDevices) {
                    Pair<Drawable, Boolean> drawableWithDescriptionWithoutRainbow = cachedBluetoothDevice.getDrawableWithDescriptionWithoutRainbow();
                    BluetoothConnectedTileIcon bluetoothConnectedTileIcon = new BluetoothConnectedTileIcon(((Boolean) drawableWithDescriptionWithoutRainbow.second).booleanValue() ? new CircleDrawable((Drawable) drawableWithDescriptionWithoutRainbow.first) : (Drawable) drawableWithDescriptionWithoutRainbow.first);
                    bluetoothConnectedTileIcon.skipTintBt = ((Boolean) drawableWithDescriptionWithoutRainbow.second).booleanValue();
                    String name = cachedBluetoothDevice.getName();
                    CharSequence deviceSecondLabel = getDeviceSecondLabel(cachedBluetoothDevice);
                    String address = cachedBluetoothDevice.getAddress();
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
                booleanState.stateDescription = this.mContext.getString(R$string.accessibility_bluetooth_name, booleanState.label) + ", " + ((Object) booleanState.secondaryLabel);
                z2 = false;
            } else if (booleanState.isTransient) {
                if (shouldShowTeslaInfo) {
                    state.icon = QSTileImpl.ResourceIcon.get(17302326);
                    z2 = true;
                } else {
                    booleanState.icon = QSTileImpl.ResourceIcon.get(17302326);
                    z2 = false;
                }
                booleanState.stateDescription = booleanState.secondaryLabel;
            } else {
                if (shouldShowTeslaInfo) {
                    state.icon = QSTileImpl.ResourceIcon.get(17302820);
                    z2 = true;
                } else {
                    booleanState.icon = QSTileImpl.ResourceIcon.get(17302820);
                    z2 = false;
                }
                booleanState.stateDescription = this.mContext.getString(R$string.accessibility_not_connected);
            }
            if (shouldShowTeslaInfo) {
                int size2 = booleanState.addressList.size();
                if (size2 > 0) {
                    for (int i3 = 0; i3 < size2; i3++) {
                        booleanState.stateList[i3] = 2;
                    }
                } else {
                    booleanState.stateList[0] = 2;
                }
            } else {
                booleanState.state = 2;
            }
        } else if (shouldShowTeslaInfo) {
            state.icon = QSTileImpl.ResourceIcon.get(17302820);
            booleanState.stateList[0] = 1;
            z2 = true;
        } else {
            booleanState.icon = QSTileImpl.ResourceIcon.get(17302820);
            booleanState.state = 1;
            z2 = false;
        }
        if (z2) {
            booleanState.iconList.add(state.icon);
            booleanState.labelList.add(state.label);
            booleanState.secondaryLabelList.add(state.secondaryLabel);
            booleanState.addressList.add("address_fake");
        }
        if (shouldShowTeslaInfo) {
            if (!this.mTeslaController.isConnected()) {
                i = 1;
            }
            booleanState.state = i;
        }
        booleanState.dualLabelContentDescription = this.mContext.getResources().getString(R$string.accessibility_quick_settings_open_settings, getTileLabel());
        booleanState.expandedAccessibilityClassName = Switch.class.getName();
    }

    private String getSecondaryLabel(boolean z, boolean z2, boolean z3, boolean z4) {
        if (z2) {
            return this.mContext.getString(R$string.quick_settings_connecting);
        }
        if (z4) {
            return this.mContext.getString(R$string.quick_settings_bluetooth_secondary_label_transient);
        }
        List<CachedBluetoothDevice> connectedDevices = this.mController.getConnectedDevices();
        if (!z || !z3 || connectedDevices.isEmpty()) {
            return null;
        }
        if (connectedDevices.size() > 1) {
            return this.mContext.getResources().getQuantityString(R$plurals.quick_settings_hotspot_secondary_label_num_devices, connectedDevices.size(), Integer.valueOf(connectedDevices.size()));
        }
        CachedBluetoothDevice cachedBluetoothDevice = connectedDevices.get(0);
        int batteryLevel = cachedBluetoothDevice.getBatteryLevel();
        if (batteryLevel > -1) {
            return this.mContext.getString(R$string.quick_settings_bluetooth_secondary_label_battery_level, Utils.formatPercentage(batteryLevel));
        }
        BluetoothClass btClass = cachedBluetoothDevice.getBtClass();
        if (btClass == null) {
            return null;
        }
        if (cachedBluetoothDevice.isHearingAidDevice()) {
            return this.mContext.getString(R$string.quick_settings_bluetooth_secondary_label_hearing_aids);
        }
        if (btClass.doesClassMatch(1)) {
            return this.mContext.getString(R$string.quick_settings_bluetooth_secondary_label_audio);
        }
        if (btClass.doesClassMatch(0)) {
            return this.mContext.getString(R$string.quick_settings_bluetooth_secondary_label_headset);
        }
        if (!btClass.doesClassMatch(3)) {
            return null;
        }
        return this.mContext.getString(R$string.quick_settings_bluetooth_secondary_label_input);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    protected String composeChangeAnnouncement() {
        if (((QSTile.BooleanState) this.mState).value) {
            return this.mContext.getString(R$string.accessibility_quick_settings_bluetooth_changed_on);
        }
        return this.mContext.getString(R$string.accessibility_quick_settings_bluetooth_changed_off);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public boolean isAvailable() {
        return this.mController.isBluetoothSupported();
    }

    protected DetailAdapter createDetailAdapter() {
        return new BluetoothDetailAdapter();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class BluetoothBatteryTileIcon extends QSTile.Icon {
        private int mBatteryLevel;
        private float mIconScale;

        BluetoothBatteryTileIcon(int i, float f) {
            this.mBatteryLevel = i;
            this.mIconScale = f;
        }

        @Override // com.android.systemui.plugins.qs.QSTile.Icon
        public Drawable getDrawable(Context context) {
            return BluetoothDeviceLayerDrawable.createLayerDrawable(context, R$drawable.ic_bluetooth_connected, this.mBatteryLevel, this.mIconScale);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class BluetoothConnectedTileIcon extends QSTile.Icon {
        private Drawable drawable;

        BluetoothConnectedTileIcon(Drawable drawable) {
            this.drawable = drawable;
        }

        @Override // com.android.systemui.plugins.qs.QSTile.Icon
        public Drawable getDrawable(Context context) {
            Drawable drawable = this.drawable;
            return drawable != null ? drawable : context.getDrawable(R$drawable.ic_bluetooth_connected);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public class BluetoothDetailAdapter implements DetailAdapter, QSDetailItems.Callback {
        private QSDetailItems mItems;

        @Override // com.android.systemui.plugins.qs.DetailAdapter
        public int getMetricsCategory() {
            return 150;
        }

        protected BluetoothDetailAdapter() {
        }

        @Override // com.android.systemui.plugins.qs.DetailAdapter
        public CharSequence getTitle() {
            return ((QSTileImpl) BluetoothTile.this).mContext.getString(R$string.quick_settings_bluetooth_label);
        }

        @Override // com.android.systemui.plugins.qs.DetailAdapter
        public Boolean getToggleState() {
            return Boolean.valueOf(((QSTile.BooleanState) ((QSTileImpl) BluetoothTile.this).mState).value);
        }

        @Override // com.android.systemui.plugins.qs.DetailAdapter
        public boolean getToggleEnabled() {
            return BluetoothTile.this.mController.getBluetoothState() == 10 || BluetoothTile.this.mController.getBluetoothState() == 12;
        }

        @Override // com.android.systemui.plugins.qs.DetailAdapter
        public Intent getSettingsIntent() {
            return BluetoothTile.BLUETOOTH_SETTINGS;
        }

        @Override // com.android.systemui.plugins.qs.DetailAdapter
        public void setToggleState(boolean z) {
            MetricsLogger.action(((QSTileImpl) BluetoothTile.this).mContext, 154, z);
            BluetoothTile.this.mController.setBluetoothEnabled(z);
        }

        @Override // com.android.systemui.plugins.qs.DetailAdapter
        public View createDetailView(Context context, View view, ViewGroup viewGroup) {
            QSDetailItems convertOrInflate = QSDetailItems.convertOrInflate(context, view, viewGroup);
            this.mItems = convertOrInflate;
            convertOrInflate.setTagSuffix("Bluetooth");
            this.mItems.setCallback(this);
            updateItems();
            setItemsVisible(((QSTile.BooleanState) ((QSTileImpl) BluetoothTile.this).mState).value);
            return this.mItems;
        }

        public void setItemsVisible(boolean z) {
            QSDetailItems qSDetailItems = this.mItems;
            if (qSDetailItems == null) {
                return;
            }
            qSDetailItems.setItemsVisible(z);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateItems() {
            if (this.mItems == null) {
                return;
            }
            if (BluetoothTile.this.mController.isBluetoothEnabled()) {
                this.mItems.setEmptyState(R$drawable.ic_qs_bluetooth_detail_empty, R$string.quick_settings_bluetooth_detail_empty_text);
            } else {
                this.mItems.setEmptyState(R$drawable.ic_qs_bluetooth_detail_empty, R$string.bt_is_off);
            }
            ArrayList arrayList = new ArrayList();
            Collection<CachedBluetoothDevice> devices = BluetoothTile.this.mController.getDevices();
            if (devices != null) {
                int i = 0;
                int i2 = 0;
                for (CachedBluetoothDevice cachedBluetoothDevice : devices) {
                    if (BluetoothTile.this.mController.getBondState(cachedBluetoothDevice) != 10) {
                        QSDetailItems.Item item = new QSDetailItems.Item();
                        item.iconResId = 17302820;
                        item.line1 = cachedBluetoothDevice.getName();
                        item.tag = cachedBluetoothDevice;
                        int maxConnectionState = cachedBluetoothDevice.getMaxConnectionState();
                        if (maxConnectionState == 2) {
                            item.iconResId = R$drawable.ic_bluetooth_connected;
                            int batteryLevel = cachedBluetoothDevice.getBatteryLevel();
                            if (batteryLevel <= -1) {
                                item.line2 = ((QSTileImpl) BluetoothTile.this).mContext.getString(R$string.quick_settings_connected);
                            } else {
                                item.icon = new BluetoothBatteryTileIcon(batteryLevel, 1.0f);
                                item.line2 = ((QSTileImpl) BluetoothTile.this).mContext.getString(R$string.quick_settings_connected_battery_level, Utils.formatPercentage(batteryLevel));
                            }
                            item.canDisconnect = true;
                            arrayList.add(i, item);
                            i++;
                        } else if (maxConnectionState == 1) {
                            item.iconResId = R$drawable.ic_qs_bluetooth_connecting;
                            item.line2 = ((QSTileImpl) BluetoothTile.this).mContext.getString(R$string.quick_settings_connecting);
                            arrayList.add(i, item);
                        } else {
                            arrayList.add(item);
                        }
                        i2++;
                        if (i2 == 20) {
                            break;
                        }
                    }
                }
            }
            this.mItems.setItems((QSDetailItems.Item[]) arrayList.toArray(new QSDetailItems.Item[arrayList.size()]));
        }

        @Override // com.android.systemui.qs.QSDetailItems.Callback
        public void onDetailItemClick(QSDetailItems.Item item) {
            Object obj;
            CachedBluetoothDevice cachedBluetoothDevice;
            if (item == null || (obj = item.tag) == null || (cachedBluetoothDevice = (CachedBluetoothDevice) obj) == null || cachedBluetoothDevice.getMaxConnectionState() != 0) {
                return;
            }
            BluetoothTile.this.mController.connect(cachedBluetoothDevice);
        }

        @Override // com.android.systemui.qs.QSDetailItems.Callback
        public void onDetailItemDisconnect(QSDetailItems.Item item) {
            Object obj;
            CachedBluetoothDevice cachedBluetoothDevice;
            if (item == null || (obj = item.tag) == null || (cachedBluetoothDevice = (CachedBluetoothDevice) obj) == null) {
                return;
            }
            BluetoothTile.this.mController.disconnect(cachedBluetoothDevice);
        }
    }

    private CharSequence getDeviceSecondLabel(CachedBluetoothDevice cachedBluetoothDevice) {
        int batteryLevel = cachedBluetoothDevice.getBatteryLevel();
        if (batteryLevel > -1) {
            return this.mContext.getString(R$string.quick_settings_bluetooth_secondary_label_battery_level, Utils.formatPercentage(batteryLevel));
        }
        BluetoothClass btClass = cachedBluetoothDevice.getBtClass();
        if (btClass == null) {
            return null;
        }
        if (cachedBluetoothDevice.isHearingAidDevice()) {
            return this.mContext.getString(R$string.quick_settings_bluetooth_secondary_label_hearing_aids);
        }
        if (btClass.doesClassMatch(1)) {
            return this.mContext.getString(R$string.quick_settings_bluetooth_secondary_label_audio);
        }
        if (btClass.doesClassMatch(0)) {
            return this.mContext.getString(R$string.quick_settings_bluetooth_secondary_label_headset);
        }
        if (!btClass.doesClassMatch(3)) {
            return null;
        }
        return this.mContext.getString(R$string.quick_settings_bluetooth_secondary_label_input);
    }
}
