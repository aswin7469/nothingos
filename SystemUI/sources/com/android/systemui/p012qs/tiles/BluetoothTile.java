package com.android.systemui.p012qs.tiles;

import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import com.android.internal.logging.MetricsLogger;
import com.android.settingslib.Utils;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.systemui.C1894R;
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
import com.nothing.systemui.p024qs.QSPanelExpandListener;
import com.nothing.systemui.p024qs.tiles.BluetoothTileEx;
import com.nothing.systemui.statusbar.policy.TeslaInfoController;
import com.nothing.systemui.util.NTLogUtil;
import java.util.ArrayList;
import java.util.List;
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
            NTLogUtil.m1686d(BluetoothTile.this.TAG, "setExpanded: " + z);
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
        C23912 r0 = new BluetoothController.Callback() {
            public void onBluetoothStateChange(boolean z) {
                BluetoothTile.this.refreshState();
                BluetoothTile.this.mBluetoothTileEx.updateDeviceService(z);
            }

            public void onBluetoothDevicesChanged() {
                BluetoothTile.this.mBluetoothTileEx.refreshState((Object) null);
            }
        };
        this.mCallback = r0;
        C23923 r9 = new TeslaInfoController.Callback() {
            public void onActiveStateChanged() {
                BluetoothTile.this.refreshState();
            }

            public void onInfoChanged() {
                BluetoothTile.this.refreshState();
            }
        };
        this.mTeslaCallback = r9;
        this.mController = bluetoothController2;
        bluetoothController2.observe(getLifecycle(), r0);
        this.mBluetoothTileEx.init(this.mContext, looper, bluetoothController2, teslaInfoController, this, handler);
        QSHost qSHost2 = qSHost;
        qSHost.setQSPanelExpandListener(this.mQSPanelExpandListener);
        this.mTeslaController = teslaInfoController2;
        teslaInfoController2.observe(getLifecycle(), r9);
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
        return this.mContext.getString(C1894R.string.quick_settings_bluetooth_label);
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x016b  */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x0189  */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x018c  */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x018e  */
    /* JADX WARNING: Removed duplicated region for block: B:71:0x01a8  */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x01b1  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void handleUpdateState(com.android.systemui.plugins.p011qs.QSTile.BooleanState r14, java.lang.Object r15) {
        /*
            r13 = this;
            java.util.ArrayList r0 = r14.iconList
            r0.clear()
            java.util.ArrayList r0 = r14.labelList
            r0.clear()
            java.util.ArrayList r0 = r14.secondaryLabelList
            r0.clear()
            java.util.ArrayList r0 = r14.addressList
            r0.clear()
            java.lang.String r0 = "no_bluetooth"
            r13.checkIfRestrictionEnforcedByAdminOnly(r14, r0)
            java.lang.Object r0 = ARG_SHOW_TRANSIENT_ENABLING
            r1 = 0
            r2 = 1
            if (r15 != r0) goto L_0x0022
            r15 = r2
            goto L_0x0023
        L_0x0022:
            r15 = r1
        L_0x0023:
            if (r15 != 0) goto L_0x0030
            com.android.systemui.statusbar.policy.BluetoothController r0 = r13.mController
            boolean r0 = r0.isBluetoothEnabled()
            if (r0 == 0) goto L_0x002e
            goto L_0x0030
        L_0x002e:
            r0 = r1
            goto L_0x0031
        L_0x0030:
            r0 = r2
        L_0x0031:
            com.android.systemui.statusbar.policy.BluetoothController r3 = r13.mController
            boolean r3 = r3.isBluetoothConnected()
            com.android.systemui.statusbar.policy.BluetoothController r4 = r13.mController
            boolean r4 = r4.isBluetoothConnecting()
            if (r15 != 0) goto L_0x004e
            if (r4 != 0) goto L_0x004e
            com.android.systemui.statusbar.policy.BluetoothController r15 = r13.mController
            int r15 = r15.getBluetoothState()
            r5 = 11
            if (r15 != r5) goto L_0x004c
            goto L_0x004e
        L_0x004c:
            r15 = r1
            goto L_0x004f
        L_0x004e:
            r15 = r2
        L_0x004f:
            r14.isTransient = r15
            r14.dualTarget = r2
            r14.value = r0
            com.android.systemui.plugins.qs.QSTile$SlashState r15 = r14.slash
            if (r15 != 0) goto L_0x0060
            com.android.systemui.plugins.qs.QSTile$SlashState r15 = new com.android.systemui.plugins.qs.QSTile$SlashState
            r15.<init>()
            r14.slash = r15
        L_0x0060:
            com.android.systemui.plugins.qs.QSTile$SlashState r15 = r14.slash
            r5 = r0 ^ 1
            r15.isSlashed = r5
            android.content.Context r15 = r13.mContext
            r5 = 2131953095(0x7f1305c7, float:1.9542651E38)
            java.lang.String r15 = r15.getString(r5)
            r14.label = r15
            boolean r15 = r14.isTransient
            java.lang.String r15 = r13.getSecondaryLabel(r0, r4, r3, r15)
            java.lang.String r15 = android.text.TextUtils.emptyIfNull(r15)
            r14.secondaryLabel = r15
            android.content.Context r15 = r13.mContext
            r4 = 2131951771(0x7f13009b, float:1.9539966E38)
            java.lang.String r15 = r15.getString(r4)
            r14.contentDescription = r15
            java.lang.String r15 = ""
            r14.stateDescription = r15
            com.nothing.systemui.statusbar.policy.TeslaInfoController r15 = r13.mTeslaController
            boolean r15 = r15.shouldShowTeslaInfo()
            r4 = 0
            if (r15 == 0) goto L_0x00bb
            com.android.systemui.plugins.qs.QSTile$State r5 = r14.copy()
            com.nothing.systemui.statusbar.policy.TeslaInfoController r6 = r13.mTeslaController
            com.android.systemui.plugins.qs.QSTile$Icon r6 = r6.getProfile()
            r14.icon = r6
            com.android.systemui.plugins.qs.QSTile$Icon r6 = r14.icon
            r6.skipTintBt = r2
            com.android.systemui.plugins.qs.QSTile$Icon r6 = r14.icon
            r6.isTesla = r2
            com.nothing.systemui.statusbar.policy.TeslaInfoController r6 = r13.mTeslaController
            java.lang.String r6 = r6.getUserName()
            r14.label = r6
            com.nothing.systemui.statusbar.policy.TeslaInfoController r6 = r13.mTeslaController
            java.lang.String r6 = r6.getBatteryRange()
            r14.secondaryLabel = r6
            r6 = r1
            goto L_0x00c9
        L_0x00bb:
            com.android.systemui.plugins.qs.QSTile$Icon r5 = r14.icon
            if (r5 == 0) goto L_0x00c7
            com.android.systemui.plugins.qs.QSTile$Icon r5 = r14.icon
            r5.isTesla = r1
            com.android.systemui.plugins.qs.QSTile$Icon r5 = r14.icon
            r5.skipTintBt = r1
        L_0x00c7:
            r6 = r2
            r5 = r4
        L_0x00c9:
            r7 = 2
            r8 = 17302830(0x108052e, float:2.498297E-38)
            if (r0 == 0) goto L_0x0249
            if (r3 == 0) goto L_0x01f3
            com.android.systemui.statusbar.policy.BluetoothController r0 = r13.mController
            java.util.List r0 = r0.getConnectedDevices()
            com.android.systemui.statusbar.policy.BluetoothController r3 = r13.mController
            com.android.settingslib.bluetooth.CachedBluetoothDevice r3 = r3.getActiveDevice()
            int r8 = r0.size()
            if (r8 <= r2) goto L_0x00fb
            if (r3 == 0) goto L_0x00fb
            r9 = r1
        L_0x00e6:
            if (r9 >= r8) goto L_0x00fb
            java.lang.Object r10 = r0.get(r9)
            boolean r10 = java.util.Objects.equals(r10, r3)
            if (r10 == 0) goto L_0x00f8
            if (r9 == 0) goto L_0x00f8
            java.util.Collections.swap((java.util.List<?>) r0, (int) r1, (int) r9)
            goto L_0x00fb
        L_0x00f8:
            int r9 = r9 + 1
            goto L_0x00e6
        L_0x00fb:
            java.util.Iterator r0 = r0.iterator()
        L_0x00ff:
            boolean r3 = r0.hasNext()
            if (r3 == 0) goto L_0x01c7
            java.lang.Object r3 = r0.next()
            com.android.settingslib.bluetooth.CachedBluetoothDevice r3 = (com.android.settingslib.bluetooth.CachedBluetoothDevice) r3
            com.nothing.systemui.qs.tiles.BluetoothTileEx r8 = r13.mBluetoothTileEx
            java.lang.String r8 = r8.getModeIDSetToDeviceControl(r3)
            boolean r9 = android.text.TextUtils.isEmpty(r8)
            if (r9 == 0) goto L_0x0125
            com.nothing.systemui.qs.tiles.BluetoothTileEx r9 = r13.mBluetoothTileEx
            boolean r9 = r9.isAirpodsExperimentOn()
            if (r9 == 0) goto L_0x0125
            com.nothing.systemui.qs.tiles.BluetoothTileEx r8 = r13.mBluetoothTileEx
            java.lang.String r8 = r8.getAirpodsVersionSetToDeviceControl(r3)
        L_0x0125:
            boolean r9 = android.text.TextUtils.isEmpty(r8)
            if (r9 != 0) goto L_0x0164
            com.nothing.systemui.qs.tiles.BluetoothTileEx r9 = r13.mBluetoothTileEx
            java.util.HashMap r9 = r9.getIconCache()
            int r9 = r9.size()
            if (r9 <= 0) goto L_0x0156
            com.nothing.systemui.qs.tiles.BluetoothTileEx r9 = r13.mBluetoothTileEx
            java.util.HashMap r9 = r9.getIconCache()
            java.lang.Object r9 = r9.get(r8)
            if (r9 != 0) goto L_0x0144
            goto L_0x0156
        L_0x0144:
            com.android.systemui.qs.tiles.BluetoothTile$BluetoothConnectedTileIcon r9 = new com.android.systemui.qs.tiles.BluetoothTile$BluetoothConnectedTileIcon
            com.nothing.systemui.qs.tiles.BluetoothTileEx r10 = r13.mBluetoothTileEx
            java.util.HashMap r10 = r10.getIconCache()
            java.lang.Object r8 = r10.get(r8)
            android.graphics.drawable.Drawable r8 = (android.graphics.drawable.Drawable) r8
            r9.<init>(r8)
            goto L_0x0165
        L_0x0156:
            com.nothing.systemui.qs.tiles.BluetoothTileEx r9 = r13.mBluetoothTileEx
            android.graphics.drawable.Drawable r8 = r9.getModuleIDBitmap(r8)
            if (r8 == 0) goto L_0x0164
            com.android.systemui.qs.tiles.BluetoothTile$BluetoothConnectedTileIcon r9 = new com.android.systemui.qs.tiles.BluetoothTile$BluetoothConnectedTileIcon
            r9.<init>(r8)
            goto L_0x0165
        L_0x0164:
            r9 = r4
        L_0x0165:
            android.util.Pair r8 = r3.getDrawableWithDescriptionWithoutRainbow()
            if (r9 != 0) goto L_0x0189
            com.android.systemui.qs.tiles.BluetoothTile$BluetoothConnectedTileIcon r10 = new com.android.systemui.qs.tiles.BluetoothTile$BluetoothConnectedTileIcon
            java.lang.Object r11 = r8.second
            java.lang.Boolean r11 = (java.lang.Boolean) r11
            boolean r11 = r11.booleanValue()
            if (r11 == 0) goto L_0x0181
            com.nothing.systemui.qs.CircleDrawable r11 = new com.nothing.systemui.qs.CircleDrawable
            java.lang.Object r12 = r8.first
            android.graphics.drawable.Drawable r12 = (android.graphics.drawable.Drawable) r12
            r11.<init>(r12)
            goto L_0x0185
        L_0x0181:
            java.lang.Object r11 = r8.first
            android.graphics.drawable.Drawable r11 = (android.graphics.drawable.Drawable) r11
        L_0x0185:
            r10.<init>(r11)
            goto L_0x018a
        L_0x0189:
            r10 = r9
        L_0x018a:
            if (r9 == 0) goto L_0x018e
            r8 = r2
            goto L_0x0196
        L_0x018e:
            java.lang.Object r8 = r8.second
            java.lang.Boolean r8 = (java.lang.Boolean) r8
            boolean r8 = r8.booleanValue()
        L_0x0196:
            r10.skipTintBt = r8
            java.lang.String r8 = r3.getName()
            com.nothing.systemui.qs.tiles.BluetoothTileEx r9 = r13.mBluetoothTileEx
            java.lang.CharSequence r9 = r9.getDeviceSecondLabel(r3)
            java.lang.String r3 = r3.getAddress()
            if (r6 == 0) goto L_0x01b1
            r14.icon = r10
            r14.label = r8
            r14.secondaryLabel = r9
            r6 = r1
            goto L_0x00ff
        L_0x01b1:
            java.util.ArrayList r11 = r14.iconList
            r11.add(r10)
            java.util.ArrayList r10 = r14.labelList
            r10.add(r8)
            java.util.ArrayList r8 = r14.secondaryLabelList
            r8.add(r9)
            java.util.ArrayList r8 = r14.addressList
            r8.add(r3)
            goto L_0x00ff
        L_0x01c7:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            android.content.Context r3 = r13.mContext
            java.lang.Object[] r4 = new java.lang.Object[r2]
            java.lang.CharSequence r6 = r14.label
            r4[r1] = r6
            r6 = 2131951687(0x7f130047, float:1.9539796E38)
            java.lang.String r3 = r3.getString(r6, r4)
            java.lang.StringBuilder r0 = r0.append((java.lang.String) r3)
            java.lang.String r3 = ", "
            java.lang.StringBuilder r0 = r0.append((java.lang.String) r3)
            java.lang.CharSequence r3 = r14.secondaryLabel
            java.lang.StringBuilder r0 = r0.append((java.lang.Object) r3)
            java.lang.String r0 = r0.toString()
            r14.stateDescription = r0
            r0 = r1
            goto L_0x022c
        L_0x01f3:
            boolean r0 = r14.isTransient
            if (r0 == 0) goto L_0x0210
            r0 = 17302334(0x108033e, float:2.498158E-38)
            if (r15 == 0) goto L_0x0204
            com.android.systemui.plugins.qs.QSTile$Icon r0 = com.android.systemui.p012qs.tileimpl.QSTileImpl.ResourceIcon.get(r0)
            r5.icon = r0
            r0 = r2
            goto L_0x020b
        L_0x0204:
            com.android.systemui.plugins.qs.QSTile$Icon r0 = com.android.systemui.p012qs.tileimpl.QSTileImpl.ResourceIcon.get(r0)
            r14.icon = r0
            r0 = r1
        L_0x020b:
            java.lang.CharSequence r3 = r14.secondaryLabel
            r14.stateDescription = r3
            goto L_0x022c
        L_0x0210:
            if (r15 == 0) goto L_0x021a
            com.android.systemui.plugins.qs.QSTile$Icon r0 = com.android.systemui.p012qs.tileimpl.QSTileImpl.ResourceIcon.get(r8)
            r5.icon = r0
            r0 = r2
            goto L_0x0221
        L_0x021a:
            com.android.systemui.plugins.qs.QSTile$Icon r0 = com.android.systemui.p012qs.tileimpl.QSTileImpl.ResourceIcon.get(r8)
            r14.icon = r0
            r0 = r1
        L_0x0221:
            android.content.Context r3 = r13.mContext
            r4 = 2131951752(0x7f130088, float:1.9539927E38)
            java.lang.String r3 = r3.getString(r4)
            r14.stateDescription = r3
        L_0x022c:
            if (r15 != 0) goto L_0x0230
            r14.state = r7
        L_0x0230:
            java.util.ArrayList r3 = r14.addressList
            int r3 = r3.size()
            if (r3 <= 0) goto L_0x0242
            r4 = r1
        L_0x0239:
            if (r4 >= r3) goto L_0x0260
            int[] r6 = r14.stateList
            r6[r4] = r7
            int r4 = r4 + 1
            goto L_0x0239
        L_0x0242:
            if (r15 == 0) goto L_0x0260
            int[] r3 = r14.stateList
            r3[r1] = r7
            goto L_0x0260
        L_0x0249:
            if (r15 == 0) goto L_0x0257
            com.android.systemui.plugins.qs.QSTile$Icon r0 = com.android.systemui.p012qs.tileimpl.QSTileImpl.ResourceIcon.get(r8)
            r5.icon = r0
            int[] r0 = r14.stateList
            r0[r1] = r2
            r0 = r2
            goto L_0x0260
        L_0x0257:
            com.android.systemui.plugins.qs.QSTile$Icon r0 = com.android.systemui.p012qs.tileimpl.QSTileImpl.ResourceIcon.get(r8)
            r14.icon = r0
            r14.state = r2
            r0 = r1
        L_0x0260:
            if (r0 == 0) goto L_0x027e
            java.util.ArrayList r0 = r14.iconList
            com.android.systemui.plugins.qs.QSTile$Icon r3 = r5.icon
            r0.add(r3)
            java.util.ArrayList r0 = r14.labelList
            java.lang.CharSequence r3 = r5.label
            r0.add(r3)
            java.util.ArrayList r0 = r14.secondaryLabelList
            java.lang.CharSequence r3 = r5.secondaryLabel
            r0.add(r3)
            java.util.ArrayList r0 = r14.addressList
            java.lang.String r3 = "address_fake"
            r0.add(r3)
        L_0x027e:
            if (r15 == 0) goto L_0x028c
            com.nothing.systemui.statusbar.policy.TeslaInfoController r15 = r13.mTeslaController
            boolean r15 = r15.isConnected()
            if (r15 == 0) goto L_0x0289
            goto L_0x028a
        L_0x0289:
            r7 = r2
        L_0x028a:
            r14.state = r7
        L_0x028c:
            android.content.Context r15 = r13.mContext
            android.content.res.Resources r15 = r15.getResources()
            java.lang.Object[] r0 = new java.lang.Object[r2]
            java.lang.CharSequence r13 = r13.getTileLabel()
            r0[r1] = r13
            r13 = 2131951782(0x7f1300a6, float:1.9539988E38)
            java.lang.String r13 = r15.getString(r13, r0)
            r14.dualLabelContentDescription = r13
            java.lang.Class<android.widget.Switch> r13 = android.widget.Switch.class
            java.lang.String r13 = r13.getName()
            r14.expandedAccessibilityClassName = r13
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.p012qs.tiles.BluetoothTile.handleUpdateState(com.android.systemui.plugins.qs.QSTile$BooleanState, java.lang.Object):void");
    }

    private String getSecondaryLabel(boolean z, boolean z2, boolean z3, boolean z4) {
        if (z2) {
            return this.mContext.getString(C1894R.string.quick_settings_connecting);
        }
        if (z4) {
            return this.mContext.getString(C1894R.string.quick_settings_bluetooth_secondary_label_transient);
        }
        List<CachedBluetoothDevice> connectedDevices = this.mController.getConnectedDevices();
        if (!z || !z3 || connectedDevices.isEmpty()) {
            return null;
        }
        if (connectedDevices.size() > 1) {
            return this.mContext.getResources().getQuantityString(C1894R.plurals.quick_settings_hotspot_secondary_label_num_devices, connectedDevices.size(), new Object[]{Integer.valueOf(connectedDevices.size())});
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
            return this.mContext.getString(C1894R.string.quick_settings_bluetooth_secondary_label_battery_level, new Object[]{Utils.formatPercentage(batteryLevel)});
        }
        BluetoothClass btClass = cachedBluetoothDevice.getBtClass();
        if (btClass == null) {
            return null;
        }
        if (cachedBluetoothDevice.isHearingAidDevice()) {
            return this.mContext.getString(C1894R.string.quick_settings_bluetooth_secondary_label_hearing_aids);
        }
        if (btClass.doesClassMatch(1)) {
            return this.mContext.getString(C1894R.string.quick_settings_bluetooth_secondary_label_audio);
        }
        if (btClass.doesClassMatch(0)) {
            return this.mContext.getString(C1894R.string.quick_settings_bluetooth_secondary_label_headset);
        }
        if (btClass.doesClassMatch(3)) {
            return this.mContext.getString(C1894R.string.quick_settings_bluetooth_secondary_label_input);
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
            return context.getDrawable(C1894R.C1896drawable.ic_bluetooth_connected);
        }
    }
}
