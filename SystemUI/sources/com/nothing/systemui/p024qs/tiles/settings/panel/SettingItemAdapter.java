package com.nothing.systemui.p024qs.tiles.settings.panel;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.systemui.C1894R;
import com.nothing.p023os.device.DeviceConstant;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.p024qs.tiles.BluetoothTileEx;
import com.nothing.systemui.p024qs.tiles.settings.tesla.TeslaConnectPanelPlugin;
import com.nothing.systemui.p024qs.tiles.settings.util.AnimUtils;
import com.nothing.systemui.util.NTLogUtil;
import com.nothing.tesla.service.CmdObject;
import com.nothing.tesla.service.CmdObjectList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* renamed from: com.nothing.systemui.qs.tiles.settings.panel.SettingItemAdapter */
public class SettingItemAdapter extends RecyclerView.Adapter<SettingItemViewHolder> {
    private static final String TAG = "BtSettingItemAdapter";
    private static final float TESLA_MAX_TEMP = 27.0f;
    private static final float TESLA_MIN_TEMP = 15.5f;
    private static final float TESLA_TEMP_ONE_STEP = 0.5f;
    private ImageView mActionIcon;
    public Map<String, ViewGroup> mAddressCorrespondingContain = new HashMap();
    public Map<String, Boolean> mAddressCorrespondingExpanded = new HashMap();
    public Map<String, BluetoothQuickPanelSoundLayout> mAddressCorrespondingSoundLayouts = new HashMap();
    public Map<String, CachedBluetoothDevice> mAddressMapCachedBluetoothDevice = new HashMap();
    public Map<String, SettingItemViewHolder> mAddressMapCachedSettingItemViewHolders = new HashMap();
    public Map<String, Boolean> mAddressMapDisServiceConnect = new HashMap();
    private BluetoothTileEx.AncCallback mAncCallback = new BluetoothTileEx.AncCallback() {
        private int mState = -1;

        public void onDeviceServiceConnected() {
            for (Map.Entry value : SettingItemAdapter.this.mAddressMapCachedBluetoothDevice.entrySet()) {
                try {
                    SettingItemAdapter.this.mBluetoothTileEx.setModelIdAndDevice((CachedBluetoothDevice) value.getValue());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public void onDeviceServiceDisConnected() {
            NTLogUtil.m1686d(SettingItemAdapter.TAG, "onDeviceServiceDisConnected isAnimShow: " + SettingItemAdapter.this.mIsBtPanelAnimating);
            if (!SettingItemAdapter.this.mIsBtPanelAnimating) {
                SettingItemAdapter.this.notifyDataSetChanged();
            }
        }

        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v0, resolved type: java.lang.String} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v0, resolved type: com.nothing.systemui.qs.tiles.settings.panel.BluetoothQuickPanelSoundLayout} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v1, resolved type: java.lang.String} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v31, resolved type: com.nothing.systemui.qs.tiles.settings.panel.BluetoothQuickPanelSoundLayout} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v2, resolved type: java.lang.String} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v34, resolved type: com.nothing.systemui.qs.tiles.settings.panel.BluetoothQuickPanelSoundLayout} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v3, resolved type: java.lang.String} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v8, resolved type: java.lang.String} */
        /* JADX WARNING: type inference failed for: r0v7, types: [com.nothing.systemui.qs.tiles.settings.panel.BluetoothQuickPanelSoundLayout] */
        /* JADX WARNING: Multi-variable type inference failed */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onSuccess(int r12, android.os.Bundle r13) {
            /*
                r11 = this;
                r0 = 0
                if (r13 == 0) goto L_0x0019
                java.lang.String r1 = "device_address"
                java.lang.String r1 = r13.getString(r1)
                if (r1 == 0) goto L_0x0015
                com.nothing.systemui.qs.tiles.settings.panel.SettingItemAdapter r0 = com.nothing.systemui.p024qs.tiles.settings.panel.SettingItemAdapter.this
                java.util.Map<java.lang.String, com.nothing.systemui.qs.tiles.settings.panel.BluetoothQuickPanelSoundLayout> r0 = r0.mAddressCorrespondingSoundLayouts
                java.lang.Object r0 = r0.get(r1)
                com.nothing.systemui.qs.tiles.settings.panel.BluetoothQuickPanelSoundLayout r0 = (com.nothing.systemui.p024qs.tiles.settings.panel.BluetoothQuickPanelSoundLayout) r0
            L_0x0015:
                r10 = r1
                r1 = r0
                r0 = r10
                goto L_0x001a
            L_0x0019:
                r1 = r0
            L_0x001a:
                r2 = 0
                r3 = 4
                r4 = 3
                r5 = 610(0x262, float:8.55E-43)
                r6 = 6
                if (r12 != 0) goto L_0x00f0
                com.nothing.systemui.qs.tiles.settings.panel.SettingItemAdapter r12 = com.nothing.systemui.p024qs.tiles.settings.panel.SettingItemAdapter.this
                java.util.Map<java.lang.String, java.lang.Boolean> r12 = r12.mAddressMapDisServiceConnect
                java.lang.Boolean r13 = java.lang.Boolean.FALSE
                r12.put(r0, r13)
                com.nothing.systemui.qs.tiles.settings.panel.SettingItemAdapter r12 = com.nothing.systemui.p024qs.tiles.settings.panel.SettingItemAdapter.this
                java.util.Map<java.lang.String, com.android.settingslib.bluetooth.CachedBluetoothDevice> r12 = r12.mAddressMapCachedBluetoothDevice
                java.util.Set r12 = r12.entrySet()
                java.util.Iterator r12 = r12.iterator()
            L_0x0037:
                boolean r13 = r12.hasNext()
                if (r13 == 0) goto L_0x01ef
                java.lang.Object r13 = r12.next()
                java.util.Map$Entry r13 = (java.util.Map.Entry) r13
                java.lang.Object r13 = r13.getValue()
                com.android.settingslib.bluetooth.CachedBluetoothDevice r13 = (com.android.settingslib.bluetooth.CachedBluetoothDevice) r13
                com.nothing.systemui.qs.tiles.settings.panel.SettingItemAdapter r1 = com.nothing.systemui.p024qs.tiles.settings.panel.SettingItemAdapter.this     // Catch:{ Exception -> 0x00ea }
                com.nothing.systemui.qs.tiles.BluetoothTileEx r1 = r1.mBluetoothTileEx     // Catch:{ Exception -> 0x00ea }
                java.lang.String r7 = r13.getAddress()     // Catch:{ Exception -> 0x00ea }
                r1.getCommand(r6, r7)     // Catch:{ Exception -> 0x00ea }
                com.nothing.systemui.qs.tiles.settings.panel.SettingItemAdapter r1 = com.nothing.systemui.p024qs.tiles.settings.panel.SettingItemAdapter.this     // Catch:{ Exception -> 0x00ea }
                com.nothing.systemui.qs.tiles.BluetoothTileEx r1 = r1.mBluetoothTileEx     // Catch:{ Exception -> 0x00ea }
                java.lang.String r7 = r13.getAddress()     // Catch:{ Exception -> 0x00ea }
                r1.getCommand(r5, r7)     // Catch:{ Exception -> 0x00ea }
                com.nothing.systemui.qs.tiles.settings.panel.SettingItemAdapter r1 = com.nothing.systemui.p024qs.tiles.settings.panel.SettingItemAdapter.this     // Catch:{ Exception -> 0x00ea }
                java.util.Map<java.lang.String, java.lang.Integer> r1 = r1.mBatteryLeft     // Catch:{ Exception -> 0x00ea }
                java.lang.String r7 = r13.getAddress()     // Catch:{ Exception -> 0x00ea }
                r8 = -1
                java.lang.Integer r9 = java.lang.Integer.valueOf((int) r8)     // Catch:{ Exception -> 0x00ea }
                java.lang.Object r1 = r1.getOrDefault(r7, r9)     // Catch:{ Exception -> 0x00ea }
                java.lang.Integer r1 = (java.lang.Integer) r1     // Catch:{ Exception -> 0x00ea }
                int r1 = r1.intValue()     // Catch:{ Exception -> 0x00ea }
                if (r1 == r8) goto L_0x0094
                com.nothing.systemui.qs.tiles.settings.panel.SettingItemAdapter r1 = com.nothing.systemui.p024qs.tiles.settings.panel.SettingItemAdapter.this     // Catch:{ Exception -> 0x00ea }
                java.util.Map<java.lang.String, java.lang.Integer> r1 = r1.mBatteryRight     // Catch:{ Exception -> 0x00ea }
                java.lang.String r7 = r13.getAddress()     // Catch:{ Exception -> 0x00ea }
                java.lang.Integer r9 = java.lang.Integer.valueOf((int) r8)     // Catch:{ Exception -> 0x00ea }
                java.lang.Object r1 = r1.getOrDefault(r7, r9)     // Catch:{ Exception -> 0x00ea }
                java.lang.Integer r1 = (java.lang.Integer) r1     // Catch:{ Exception -> 0x00ea }
                int r1 = r1.intValue()     // Catch:{ Exception -> 0x00ea }
                if (r1 != r8) goto L_0x00a1
            L_0x0094:
                com.nothing.systemui.qs.tiles.settings.panel.SettingItemAdapter r1 = com.nothing.systemui.p024qs.tiles.settings.panel.SettingItemAdapter.this     // Catch:{ Exception -> 0x00ea }
                com.nothing.systemui.qs.tiles.BluetoothTileEx r1 = r1.mBluetoothTileEx     // Catch:{ Exception -> 0x00ea }
                java.lang.String r7 = r13.getAddress()     // Catch:{ Exception -> 0x00ea }
                r1.getCommand(r3, r7)     // Catch:{ Exception -> 0x00ea }
            L_0x00a1:
                com.nothing.systemui.qs.tiles.settings.panel.SettingItemAdapter r1 = com.nothing.systemui.p024qs.tiles.settings.panel.SettingItemAdapter.this     // Catch:{ Exception -> 0x00ea }
                com.nothing.systemui.qs.tiles.BluetoothTileEx r1 = r1.mBluetoothTileEx     // Catch:{ Exception -> 0x00ea }
                java.lang.String r1 = r1.getModeID(r0)     // Catch:{ Exception -> 0x00ea }
                com.nothing.systemui.qs.tiles.settings.panel.SettingItemAdapter r7 = com.nothing.systemui.p024qs.tiles.settings.panel.SettingItemAdapter.this     // Catch:{ Exception -> 0x00ea }
                com.nothing.systemui.qs.tiles.BluetoothTileEx r7 = r7.mBluetoothTileEx     // Catch:{ Exception -> 0x00ea }
                java.util.HashMap r7 = r7.getIconCache()     // Catch:{ Exception -> 0x00ea }
                int r7 = r7.size()     // Catch:{ Exception -> 0x00ea }
                if (r7 == 0) goto L_0x00db
                com.nothing.systemui.qs.tiles.settings.panel.SettingItemAdapter r7 = com.nothing.systemui.p024qs.tiles.settings.panel.SettingItemAdapter.this     // Catch:{ Exception -> 0x00ea }
                com.nothing.systemui.qs.tiles.BluetoothTileEx r7 = r7.mBluetoothTileEx     // Catch:{ Exception -> 0x00ea }
                java.util.HashMap r7 = r7.getIconCache()     // Catch:{ Exception -> 0x00ea }
                int r7 = r7.size()     // Catch:{ Exception -> 0x00ea }
                if (r7 <= 0) goto L_0x0037
                com.nothing.systemui.qs.tiles.settings.panel.SettingItemAdapter r7 = com.nothing.systemui.p024qs.tiles.settings.panel.SettingItemAdapter.this     // Catch:{ Exception -> 0x00ea }
                com.nothing.systemui.qs.tiles.BluetoothTileEx r7 = r7.mBluetoothTileEx     // Catch:{ Exception -> 0x00ea }
                java.util.HashMap r7 = r7.getIconCache()     // Catch:{ Exception -> 0x00ea }
                java.lang.Object r7 = r7.get(r1)     // Catch:{ Exception -> 0x00ea }
                if (r7 != 0) goto L_0x0037
            L_0x00db:
                com.nothing.systemui.qs.tiles.settings.panel.SettingItemAdapter r7 = com.nothing.systemui.p024qs.tiles.settings.panel.SettingItemAdapter.this     // Catch:{ Exception -> 0x00ea }
                com.nothing.systemui.qs.tiles.BluetoothTileEx r7 = r7.mBluetoothTileEx     // Catch:{ Exception -> 0x00ea }
                java.lang.String r13 = r13.getAddress()     // Catch:{ Exception -> 0x00ea }
                r7.getCommand(r4, r13, r2, r1)     // Catch:{ Exception -> 0x00ea }
                goto L_0x0037
            L_0x00ea:
                r13 = move-exception
                r13.printStackTrace()
                goto L_0x0037
            L_0x00f0:
                if (r12 == r6) goto L_0x01e4
                java.lang.String r6 = "BtSettingItemAdapter"
                if (r12 != r5) goto L_0x0121
                if (r1 == 0) goto L_0x00fb
                r1.setRemoteDataAndUpdateUI(r13)
            L_0x00fb:
                java.lang.StringBuilder r11 = new java.lang.StringBuilder
                java.lang.String r12 = "---ORDER_ANC result:"
                r11.<init>((java.lang.String) r12)
                java.lang.String r12 = "KEY_VALUE_STRING"
                java.lang.String r1 = ""
                java.lang.String r12 = r13.getString(r12, r1)
                java.lang.StringBuilder r11 = r11.append((java.lang.String) r12)
                java.lang.String r12 = ", address:"
                java.lang.StringBuilder r11 = r11.append((java.lang.String) r12)
                java.lang.StringBuilder r11 = r11.append((java.lang.String) r0)
                java.lang.String r11 = r11.toString()
                com.nothing.systemui.util.NTLogUtil.m1686d(r6, r11)
                goto L_0x01ef
            L_0x0121:
                r1 = 2
                if (r12 != r1) goto L_0x017a
                java.lang.StringBuilder r12 = new java.lang.StringBuilder
                java.lang.String r13 = "onSuccess DISCONNECT address:"
                r12.<init>((java.lang.String) r13)
                java.lang.StringBuilder r12 = r12.append((java.lang.String) r0)
                java.lang.String r12 = r12.toString()
                com.nothing.systemui.util.NTLogUtil.m1686d(r6, r12)
                com.nothing.systemui.qs.tiles.settings.panel.SettingItemAdapter r12 = com.nothing.systemui.p024qs.tiles.settings.panel.SettingItemAdapter.this
                java.util.Map<java.lang.String, java.lang.Boolean> r12 = r12.mAddressMapDisServiceConnect
                java.lang.Boolean r13 = java.lang.Boolean.TRUE
                r12.put(r0, r13)
                com.nothing.systemui.qs.tiles.settings.panel.SettingItemAdapter r12 = com.nothing.systemui.p024qs.tiles.settings.panel.SettingItemAdapter.this
                java.util.Map<java.lang.String, com.nothing.systemui.qs.tiles.settings.panel.SettingItemAdapter$SettingItemViewHolder> r12 = r12.mAddressMapCachedSettingItemViewHolders
                if (r12 == 0) goto L_0x01ef
                com.nothing.systemui.qs.tiles.settings.panel.SettingItemAdapter r12 = com.nothing.systemui.p024qs.tiles.settings.panel.SettingItemAdapter.this
                java.util.Map<java.lang.String, com.nothing.systemui.qs.tiles.settings.panel.SettingItemAdapter$SettingItemViewHolder> r12 = r12.mAddressMapCachedSettingItemViewHolders
                int r12 = r12.size()
                if (r12 <= 0) goto L_0x01ef
                com.nothing.systemui.qs.tiles.settings.panel.SettingItemAdapter r12 = com.nothing.systemui.p024qs.tiles.settings.panel.SettingItemAdapter.this
                java.util.Map<java.lang.String, com.nothing.systemui.qs.tiles.settings.panel.SettingItemAdapter$SettingItemViewHolder> r12 = r12.mAddressMapCachedSettingItemViewHolders
                java.lang.Object r12 = r12.get(r0)
                if (r12 == 0) goto L_0x01ef
                com.nothing.systemui.qs.tiles.settings.panel.SettingItemAdapter r12 = com.nothing.systemui.p024qs.tiles.settings.panel.SettingItemAdapter.this
                java.util.Map<java.lang.String, com.nothing.systemui.qs.tiles.settings.panel.SettingItemAdapter$SettingItemViewHolder> r12 = r12.mAddressMapCachedSettingItemViewHolders
                java.lang.Object r12 = r12.get(r0)
                com.nothing.systemui.qs.tiles.settings.panel.SettingItemAdapter$SettingItemViewHolder r12 = (com.nothing.systemui.p024qs.tiles.settings.panel.SettingItemAdapter.SettingItemViewHolder) r12
                android.widget.ImageView r12 = r12.mActionIcon
                r12.setVisibility(r2)
                com.nothing.systemui.qs.tiles.settings.panel.SettingItemAdapter r11 = com.nothing.systemui.p024qs.tiles.settings.panel.SettingItemAdapter.this
                java.util.Map<java.lang.String, com.nothing.systemui.qs.tiles.settings.panel.SettingItemAdapter$SettingItemViewHolder> r11 = r11.mAddressMapCachedSettingItemViewHolders
                java.lang.Object r11 = r11.get(r0)
                com.nothing.systemui.qs.tiles.settings.panel.SettingItemAdapter$SettingItemViewHolder r11 = (com.nothing.systemui.p024qs.tiles.settings.panel.SettingItemAdapter.SettingItemViewHolder) r11
                android.view.View r11 = r11.mBluetoothExpandIconContainer
                r12 = 8
                r11.setVisibility(r12)
                goto L_0x01ef
            L_0x017a:
                if (r12 == r4) goto L_0x01ef
                if (r12 != r3) goto L_0x01ef
                java.lang.StringBuilder r12 = new java.lang.StringBuilder
                java.lang.String r1 = "onSuccess DeviceConstant.GET_BATTERY address:"
                r12.<init>((java.lang.String) r1)
                java.lang.StringBuilder r12 = r12.append((java.lang.String) r0)
                java.lang.String r12 = r12.toString()
                com.nothing.systemui.util.NTLogUtil.m1686d(r6, r12)
                if (r0 == 0) goto L_0x01ef
                java.lang.String r12 = "KEY_BATTERY_LEFT"
                int r12 = r13.getInt(r12)
                java.lang.String r1 = "KEY_BATTERY_RIGHT"
                int r13 = r13.getInt(r1)
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r2 = "onSuccess DeviceConstant.GET_BATTERY left:"
                r1.<init>((java.lang.String) r2)
                java.lang.StringBuilder r1 = r1.append((int) r12)
                java.lang.String r2 = ", right:"
                java.lang.StringBuilder r1 = r1.append((java.lang.String) r2)
                java.lang.StringBuilder r1 = r1.append((int) r13)
                java.lang.String r1 = r1.toString()
                com.nothing.systemui.util.NTLogUtil.m1686d(r6, r1)
                com.nothing.systemui.qs.tiles.settings.panel.SettingItemAdapter r1 = com.nothing.systemui.p024qs.tiles.settings.panel.SettingItemAdapter.this
                java.util.Map<java.lang.String, java.lang.Integer> r1 = r1.mBatteryLeft
                java.lang.Integer r12 = java.lang.Integer.valueOf((int) r12)
                r1.put(r0, r12)
                com.nothing.systemui.qs.tiles.settings.panel.SettingItemAdapter r12 = com.nothing.systemui.p024qs.tiles.settings.panel.SettingItemAdapter.this
                java.util.Map<java.lang.String, java.lang.Integer> r12 = r12.mBatteryRight
                java.lang.Integer r13 = java.lang.Integer.valueOf((int) r13)
                r12.put(r0, r13)
                com.nothing.systemui.qs.tiles.settings.panel.SettingItemAdapter r12 = com.nothing.systemui.p024qs.tiles.settings.panel.SettingItemAdapter.this
                boolean r12 = r12.mIsBtPanelAnimating
                if (r12 == 0) goto L_0x01de
                java.lang.String r11 = "GET_BATTERY isAnimShow"
                com.nothing.systemui.util.NTLogUtil.m1686d(r6, r11)
                return
            L_0x01de:
                com.nothing.systemui.qs.tiles.settings.panel.SettingItemAdapter r11 = com.nothing.systemui.p024qs.tiles.settings.panel.SettingItemAdapter.this
                r11.notifyDataSetChanged()
                goto L_0x01ef
            L_0x01e4:
                if (r1 == 0) goto L_0x01ef
                java.lang.String r11 = "KEY_VALUE_INT"
                int r11 = r13.getInt(r11)
                r1.setAncLevel(r11)
            L_0x01ef:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.nothing.systemui.p024qs.tiles.settings.panel.SettingItemAdapter.C42181.onSuccess(int, android.os.Bundle):void");
        }

        public void onDeviceBitmapLoaded(String str, BitmapDrawable bitmapDrawable) {
            NTLogUtil.m1686d(SettingItemAdapter.TAG, "---onDeviceBitmapLoaded command");
            if (SettingItemAdapter.this.mAddressMapCachedSettingItemViewHolders.size() <= 0 || SettingItemAdapter.this.mAddressMapCachedSettingItemViewHolders.get(str) == null) {
                SettingItemAdapter.this.notifyDataSetChanged();
            } else {
                SettingItemAdapter.this.mAddressMapCachedSettingItemViewHolders.get(str).mTitleIcon.setImageDrawable(bitmapDrawable);
            }
        }

        public void onFail(int i, int i2) {
            NTLogUtil.m1686d(SettingItemAdapter.TAG, "---onFail command:" + i + ", error:" + i2);
        }

        public void onDestroy() {
            if (SettingItemAdapter.this.mAddressCorrespondingSoundLayouts != null && SettingItemAdapter.this.mAddressCorrespondingSoundLayouts.size() > 0) {
                SettingItemAdapter.this.mAddressCorrespondingSoundLayouts.clear();
            }
            if (SettingItemAdapter.this.mAddressCorrespondingContain != null && SettingItemAdapter.this.mAddressCorrespondingContain.size() > 0) {
                for (Map.Entry<String, ViewGroup> value : SettingItemAdapter.this.mAddressCorrespondingContain.entrySet()) {
                    ((ViewGroup) value.getValue()).removeAllViews();
                }
                SettingItemAdapter.this.mAddressCorrespondingContain.clear();
            }
            if (SettingItemAdapter.this.mAddressCorrespondingExpanded != null && SettingItemAdapter.this.mAddressCorrespondingExpanded.size() > 0) {
                SettingItemAdapter.this.mAddressCorrespondingExpanded.clear();
            }
            if (SettingItemAdapter.this.mAddressMapCachedBluetoothDevice != null && SettingItemAdapter.this.mAddressMapCachedBluetoothDevice.size() > 0) {
                SettingItemAdapter.this.mAddressMapCachedBluetoothDevice.clear();
            }
            if (SettingItemAdapter.this.mAddressMapCachedSettingItemViewHolders != null && SettingItemAdapter.this.mAddressMapCachedSettingItemViewHolders.size() > 0) {
                SettingItemAdapter.this.mAddressMapCachedSettingItemViewHolders.clear();
            }
            if (SettingItemAdapter.this.mAddressMapDisServiceConnect != null && SettingItemAdapter.this.mAddressMapDisServiceConnect.size() > 0) {
                SettingItemAdapter.this.mAddressMapDisServiceConnect.clear();
            }
        }

        public void onProfileConnectionStateChanged(CachedBluetoothDevice cachedBluetoothDevice, int i, int i2) {
            if (SettingItemAdapter.this.mBluetoothTileEx.isNothingEarDevice(cachedBluetoothDevice.getAddress()) && SettingItemAdapter.this.mBluetoothTileEx.isSupportAnc(cachedBluetoothDevice.getAddress()) && this.mState != i) {
                if (i2 == 2) {
                    Bundle bundle = new Bundle();
                    bundle.putString(DeviceConstant.KEY_MAC_ADDRESS, cachedBluetoothDevice.getAddress());
                    SettingItemAdapter.this.mBluetoothTileEx.sendCommand(0, bundle);
                    NTLogUtil.m1686d(SettingItemAdapter.TAG, "STATE_CONNECTED--");
                    SettingItemAdapter.this.mAddressMapDisServiceConnect.put(cachedBluetoothDevice.getAddress(), Boolean.FALSE);
                }
                this.mState = i;
            }
        }
    };
    public Map<String, Integer> mBatteryLeft = new HashMap();
    public Map<String, Integer> mBatteryRight = new HashMap();
    /* access modifiers changed from: private */
    public BluetoothTileEx mBluetoothTileEx = ((BluetoothTileEx) NTDependencyEx.get(BluetoothTileEx.class));
    private SettingItemViewHolder mClickSettingRowViewHolder;
    Context mContext;
    /* access modifiers changed from: private */
    public float mCurrentTemp = 23.0f;
    public Map<String, String> mDeviceName = new HashMap();
    private View mDivider;
    private boolean mHasPerm;
    private View mHeadedDivider;
    private String mHighTempText = String.valueOf(this.mCurrentTemp);
    /* access modifiers changed from: private */
    public boolean mIsBtPanelAnimating = false;
    /* access modifiers changed from: private */
    public boolean mIsTempFromClick = false;
    /* access modifiers changed from: private */
    public boolean mIsTeslaExpanded;
    private final LazyCheckedDispatcher mLazyCheckedDispatcher = new LazyCheckedDispatcher();
    private LiveData<List<SettingItemData>> mLiveData;
    private String mLowTempText = String.valueOf(23.0f);
    private View mNtTitleIconLayout;
    private View mPinnedHeader;
    private LiveData<List<SettingItemData>> mPinnedHeaderLiveData;
    /* access modifiers changed from: private */
    public List<SettingItemData> mPinnedHeaderSettingListsData;
    /* access modifiers changed from: private */
    public List<SettingItemData> mSettingListsData;
    private TextView mSummary;
    /* access modifiers changed from: private */
    public Switch mSwitch;
    private CmdObjectList mTeslaCmdList;
    private LinearLayout mTeslaCmdParentView;
    /* access modifiers changed from: private */
    public View mTeslaExpandViewParent;
    private ImageView mTeslaIconExpand;
    /* access modifiers changed from: private */
    public ImageView mTeslaIconTempDown;
    /* access modifiers changed from: private */
    public ImageView mTeslaIconTempUp;
    private View mTeslaLoadingView;
    /* access modifiers changed from: private */
    public TeslaConnectPanelPlugin mTeslaPlugin;
    private TextView mTeslaRange;
    private TextView mTeslaTempText;
    private ImageView mTeslaUserIcon;
    private TextView mTeslaUserName;
    private View mTeslaView;
    private TextView mTitle;
    private ImageView mTitleIcon;

    public SettingItemAdapter(Context context) {
        this.mContext = context;
        NTLogUtil.m1686d(TAG, "clickFrom:" + this.mBluetoothTileEx.getClickFrom());
        if (this.mBluetoothTileEx.getClickFrom() == 1 && !TextUtils.isEmpty(this.mBluetoothTileEx.getClickAddress())) {
            this.mAddressCorrespondingExpanded.put(this.mBluetoothTileEx.getClickAddress(), true);
        }
    }

    public void setTeslaView(View view, boolean z) {
        this.mTeslaView = view;
        if (view != null) {
            this.mLowTempText = view.getContext().getResources().getString(C1894R.string.anc_noise_low);
            this.mHighTempText = this.mTeslaView.getContext().getResources().getString(C1894R.string.anc_noise_high);
            int i = 8;
            boolean z2 = false;
            this.mTeslaView.setVisibility(z ? 0 : 8);
            this.mTeslaIconExpand = (ImageView) view.findViewById(C1894R.C1898id.expand_icon);
            this.mTeslaLoadingView = view.findViewById(C1894R.C1898id.loading_progress_bar);
            this.mTeslaUserIcon = (ImageView) view.findViewById(C1894R.C1898id.car_image);
            View findViewById = view.findViewById(C1894R.C1898id.expandeable_control_panel_parent);
            this.mTeslaExpandViewParent = findViewById;
            if (this.mBluetoothTileEx.getClickFrom() == 0) {
                i = 0;
            }
            findViewById.setVisibility(i);
            this.mTeslaCmdParentView = (LinearLayout) view.findViewById(C1894R.C1898id.control_panel);
            this.mTeslaIconTempDown = (ImageView) view.findViewById(C1894R.C1898id.climate_down);
            this.mTeslaIconTempUp = (ImageView) view.findViewById(C1894R.C1898id.climate_up);
            this.mTeslaTempText = (TextView) view.findViewById(C1894R.C1898id.climate_temp);
            this.mTeslaUserName = (TextView) view.findViewById(C1894R.C1898id.car_display_name);
            this.mTeslaRange = (TextView) view.findViewById(C1894R.C1898id.car_range);
            if (this.mTeslaExpandViewParent.getVisibility() == 0) {
                z2 = true;
            }
            this.mIsTeslaExpanded = z2;
            updateExpandIcon(z2);
            this.mTeslaIconExpand.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    if (SettingItemAdapter.this.mIsTeslaExpanded) {
                        AnimUtils.collapse(SettingItemAdapter.this.mTeslaExpandViewParent);
                    } else {
                        AnimUtils.expand(SettingItemAdapter.this.mTeslaExpandViewParent);
                    }
                    SettingItemAdapter settingItemAdapter = SettingItemAdapter.this;
                    boolean unused = settingItemAdapter.mIsTeslaExpanded = !settingItemAdapter.mIsTeslaExpanded;
                    SettingItemAdapter settingItemAdapter2 = SettingItemAdapter.this;
                    settingItemAdapter2.updateExpandIcon(settingItemAdapter2.mIsTeslaExpanded);
                }
            });
            this.mTeslaIconTempUp.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    if (SettingItemAdapter.this.mCurrentTemp < SettingItemAdapter.TESLA_MAX_TEMP) {
                        boolean unused = SettingItemAdapter.this.mIsTempFromClick = true;
                        SettingItemAdapter settingItemAdapter = SettingItemAdapter.this;
                        float unused2 = settingItemAdapter.mCurrentTemp = settingItemAdapter.mCurrentTemp + 0.5f;
                        SettingItemAdapter.this.mTeslaPlugin.sendCmd(9, String.valueOf(SettingItemAdapter.this.mCurrentTemp));
                        SettingItemAdapter settingItemAdapter2 = SettingItemAdapter.this;
                        settingItemAdapter2.calculateTemp(settingItemAdapter2.mCurrentTemp);
                        return;
                    }
                    SettingItemAdapter.this.mTeslaIconTempUp.setEnabled(false);
                }
            });
            this.mTeslaIconTempDown.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    if (SettingItemAdapter.this.mCurrentTemp > SettingItemAdapter.TESLA_MIN_TEMP) {
                        boolean unused = SettingItemAdapter.this.mIsTempFromClick = true;
                        SettingItemAdapter settingItemAdapter = SettingItemAdapter.this;
                        float unused2 = settingItemAdapter.mCurrentTemp = settingItemAdapter.mCurrentTemp - 0.5f;
                        SettingItemAdapter.this.mTeslaPlugin.sendCmd(8, String.valueOf(SettingItemAdapter.this.mCurrentTemp));
                        SettingItemAdapter settingItemAdapter2 = SettingItemAdapter.this;
                        settingItemAdapter2.calculateTemp(settingItemAdapter2.mCurrentTemp);
                        return;
                    }
                    SettingItemAdapter.this.mTeslaIconTempDown.setEnabled(false);
                }
            });
        }
        bindTeslaData(this.mTeslaCmdList);
    }

    public void setupTeslaPlugin(TeslaConnectPanelPlugin teslaConnectPanelPlugin) {
        this.mTeslaPlugin = teslaConnectPanelPlugin;
    }

    public void bindTeslaData(CmdObjectList cmdObjectList) {
        if (cmdObjectList != null) {
            List<CmdObject> cmdObjects = cmdObjectList.getCmdObjects();
            this.mIsTempFromClick = false;
            if (cmdObjects != null && cmdObjects.size() > 0) {
                View view = this.mTeslaLoadingView;
                if (view != null) {
                    view.setVisibility(8);
                }
                this.mTeslaIconExpand.setVisibility(0);
                this.mTeslaCmdParentView.removeAllViews();
                for (CmdObject next : cmdObjects) {
                    boolean z = true;
                    if (next.getType() == 1) {
                        this.mTeslaUserName.setText(next.getTitle());
                        this.mTeslaRange.setText(next.getSubTitle());
                    } else if (next.getType() == 2) {
                        String title = next.getTitle();
                        if (title != null) {
                            try {
                                this.mCurrentTemp = Float.valueOf(title).floatValue();
                            } catch (Exception unused) {
                            }
                        }
                        if (!this.mIsTempFromClick) {
                            calculateTemp(this.mCurrentTemp);
                        }
                        this.mTeslaIconTempDown.setEnabled(this.mCurrentTemp > TESLA_MIN_TEMP);
                        ImageView imageView = this.mTeslaIconTempUp;
                        if (this.mCurrentTemp >= TESLA_MAX_TEMP) {
                            z = false;
                        }
                        imageView.setEnabled(z);
                    } else {
                        try {
                            final View inflate = LayoutInflater.from(this.mTeslaCmdParentView.getContext()).inflate(C1894R.layout.tesla_cmd_item, (ViewGroup) null, false);
                            updateTeslaIconStatus(next.getStatus(), next.getCmd(), (ImageView) inflate.findViewById(C1894R.C1898id.cmd_icon));
                            ((TextView) inflate.findViewById(C1894R.C1898id.cmd_title)).setText(next.getTitle());
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -1);
                            layoutParams.weight = 1.0f;
                            inflate.setId(next.getCmd());
                            inflate.setTag(next);
                            inflate.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View view) {
                                    boolean status = ((CmdObject) inflate.getTag()).getStatus();
                                    SettingItemAdapter.this.mTeslaPlugin.sendCmd(view.getId(), "");
                                    SettingItemAdapter.this.updateTeslaIconStatus(status, view.getId(), (ImageView) view.findViewById(C1894R.C1898id.cmd_icon));
                                }
                            });
                            this.mTeslaCmdParentView.addView(inflate, layoutParams);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void updateTeslaIconStatus(boolean z, int i, ImageView imageView) {
        Integer num = TeslaConnectPanelPlugin.CMD_ICON_ON.get(Integer.valueOf(i));
        Integer num2 = TeslaConnectPanelPlugin.CMD_ICON_MAPPING.get(Integer.valueOf(i));
        boolean z2 = true;
        StringBuilder append = new StringBuilder("updateTeslaIconStatus:").append(z).append(", ").append(num != null).append(", ");
        if (num2 == null) {
            z2 = false;
        }
        NTLogUtil.m1686d(TAG, append.append(z2).toString());
        if (z) {
            imageView.setBackgroundResource(C1894R.C1896drawable.tesla_cmd_item_background_selected);
            if (num != null) {
                imageView.setImageResource(num.intValue());
                return;
            }
            return;
        }
        imageView.setBackgroundResource(C1894R.C1896drawable.tesla_cmd_item_background);
        if (num2 != null) {
            imageView.setImageResource(num2.intValue());
        }
    }

    /* access modifiers changed from: private */
    public void calculateTemp(float f) {
        boolean z = true;
        if (this.mContext.getResources().getConfiguration().getLayoutDirection() != 1) {
            z = false;
        }
        String sb = (z ? new StringBuilder(" ℃").append(f) : new StringBuilder().append(f).append(" ℃")).toString();
        if (f == TESLA_MIN_TEMP) {
            sb = this.mLowTempText;
        } else if (f == TESLA_MAX_TEMP) {
            sb = this.mHighTempText;
        }
        this.mTeslaTempText.setText(sb);
    }

    /* access modifiers changed from: private */
    public void updateExpandIcon(boolean z) {
        if (z) {
            this.mTeslaIconExpand.setImageResource(C1894R.C1896drawable.ic_tesla_panel_up);
        } else {
            this.mTeslaIconExpand.setImageResource(C1894R.C1896drawable.ic_tesla_panel_down);
        }
    }

    public void setPinnedHeader(View view) {
        this.mPinnedHeader = view;
        if (view != null) {
            this.mHeadedDivider = view.findViewById(C1894R.C1898id.headed_divider);
            this.mTitle = (TextView) this.mPinnedHeader.findViewById(C1894R.C1898id.title);
            this.mSummary = (TextView) this.mPinnedHeader.findViewById(C1894R.C1898id.summary);
            this.mDivider = this.mPinnedHeader.findViewById(C1894R.C1898id.divider);
            this.mSwitch = (Switch) this.mPinnedHeader.findViewById(C1894R.C1898id.switch_widget);
            this.mTitleIcon = (ImageView) this.mPinnedHeader.findViewById(C1894R.C1898id.icon_title);
            this.mNtTitleIconLayout = this.mPinnedHeader.findViewById(C1894R.C1898id.nt_icon_title_layout);
            this.mActionIcon = (ImageView) this.mPinnedHeader.findViewById(C1894R.C1898id.icon_action);
        }
    }

    public void setNothingAppHasPermission(boolean z) {
        this.mHasPerm = z;
        NTLogUtil.m1686d(TAG, "HasPermission:" + this.mHasPerm);
    }

    public BluetoothTileEx.AncCallback getAncCallback() {
        return this.mAncCallback;
    }

    public SettingItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new SettingItemViewHolder(viewGroup);
    }

    public void onBindViewHolder(final SettingItemViewHolder settingItemViewHolder, int i) {
        float f;
        Drawable drawable;
        Drawable drawable2;
        if (i < this.mSettingListsData.size()) {
            final SettingItemData settingItemData = this.mSettingListsData.get(i);
            updateDeviceTitleIfNeeded(settingItemData);
            settingItemViewHolder.mTitle.setText(settingItemData.title);
            settingItemViewHolder.mSummary.setVisibility(TextUtils.isEmpty(settingItemData.subTitle) ? 8 : 0);
            settingItemViewHolder.mSummary.setText(settingItemData.subTitle);
            if (this.mContext.getString(C1894R.string.quick_settings_connecting).equals(settingItemData.subTitle) || this.mContext.getString(C1894R.string.bluetooth_pairing).equals(settingItemData.subTitle)) {
                settingItemViewHolder.mTitle.setAlpha(0.33f);
                settingItemViewHolder.mSummary.setAlpha(0.33f);
            } else {
                settingItemViewHolder.mTitle.setAlpha(1.0f);
                settingItemViewHolder.mSummary.setAlpha(1.0f);
            }
            if (settingItemData.hasToggle) {
                this.mSwitch.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener) null);
                if (settingItemViewHolder.mSwitch.getVisibility() != 0) {
                    settingItemViewHolder.mDivider.setVisibility(0);
                    settingItemViewHolder.mSwitch.setVisibility(0);
                    settingItemViewHolder.mSwitch.setChecked(settingItemData.isChecked);
                } else if (settingItemData.isForceUpdate) {
                    settingItemData.isForceUpdate = false;
                    settingItemViewHolder.mSwitch.setChecked(settingItemData.isChecked);
                }
                this.mLazyCheckedDispatcher.setOnCheckedChangeListener(settingItemData.switchListener);
                settingItemViewHolder.mSwitch.setOnCheckedChangeListener(this.mLazyCheckedDispatcher);
            } else {
                settingItemViewHolder.mDivider.setVisibility(8);
                settingItemViewHolder.mSwitch.setVisibility(8);
            }
            settingItemViewHolder.mRowView.setOnClickListener(settingItemData.contentClickListener);
            settingItemViewHolder.mActionIcon.setOnClickListener(settingItemData.actionClickListener);
            String modeID = this.mBluetoothTileEx.getModeID(settingItemData.macAddress);
            if (settingItemData.titleDrawable != null) {
                if (!TextUtils.isEmpty(modeID) && this.mBluetoothTileEx.getIconCache().size() > 0 && this.mBluetoothTileEx.getIconCache().get(modeID) != null && !this.mBluetoothTileEx.isAdvancedDetailsHeader(settingItemData.cachedDevice.getDevice())) {
                    settingItemViewHolder.mTitleIcon.setImageDrawable(this.mBluetoothTileEx.getIconCache().get(modeID));
                } else if (!settingItemData.isNothingEarDevice) {
                    String airpodsVersion = this.mBluetoothTileEx.getAirpodsVersion(settingItemData.macAddress);
                    boolean z = settingItemData.supportAirpods;
                    if (TextUtils.isEmpty(airpodsVersion) || !z) {
                        settingItemViewHolder.mTitleIcon.setImageDrawable(settingItemData.titleDrawable);
                    } else {
                        if (this.mBluetoothTileEx.getIconCache().size() <= 0 || this.mBluetoothTileEx.getIconCache().get(modeID) == null) {
                            Drawable moduleIDBitmap = this.mBluetoothTileEx.getModuleIDBitmap(airpodsVersion);
                            drawable2 = moduleIDBitmap != null ? new PanelCircleDrawable(moduleIDBitmap) : null;
                        } else {
                            drawable2 = this.mBluetoothTileEx.getIconCache().get(modeID);
                        }
                        if (drawable2 != null) {
                            settingItemViewHolder.mTitleIcon.setImageDrawable(drawable2);
                        } else {
                            settingItemViewHolder.mTitleIcon.setImageDrawable(settingItemData.titleDrawable);
                        }
                    }
                } else if (!TextUtils.isEmpty(modeID)) {
                    if (this.mBluetoothTileEx.getIconCache().size() <= 0 || this.mBluetoothTileEx.getIconCache().get(modeID) == null) {
                        Drawable moduleIDBitmap2 = this.mBluetoothTileEx.getModuleIDBitmap(modeID);
                        drawable = moduleIDBitmap2 != null ? new PanelCircleDrawable(moduleIDBitmap2) : null;
                    } else {
                        drawable = this.mBluetoothTileEx.getIconCache().get(modeID);
                    }
                    if (drawable != null) {
                        settingItemViewHolder.mTitleIcon.setImageDrawable(drawable);
                    } else {
                        settingItemViewHolder.mTitleIcon.setImageDrawable(settingItemData.titleDrawable);
                    }
                }
                settingItemViewHolder.mTitleIcon.setVisibility(0);
                settingItemViewHolder.mNtTitleIconLayout.setVisibility(0);
                if (settingItemData.isNothingEarDevice || !TextUtils.isEmpty(this.mBluetoothTileEx.getAirpodsVersion(settingItemData.macAddress))) {
                    f = this.mContext.getResources().getDimension(C1894R.dimen.nt_panel_icon_title_height_small);
                    settingItemViewHolder.mNtTitleIconLayout.setBackgroundResource(C1894R.C1896drawable.nt_circle);
                } else {
                    f = this.mContext.getResources().getDimension(C1894R.dimen.nt_panel_icon_title_height_medium);
                    settingItemViewHolder.mNtTitleIconLayout.setBackground((Drawable) null);
                }
                ViewGroup.LayoutParams layoutParams = settingItemViewHolder.mTitleIcon.getLayoutParams();
                int i2 = (int) f;
                layoutParams.height = i2;
                layoutParams.width = i2;
                settingItemViewHolder.mTitleIcon.setLayoutParams(layoutParams);
            } else {
                settingItemViewHolder.mTitleIcon.setVisibility(8);
                settingItemViewHolder.mNtTitleIconLayout.setVisibility(8);
            }
            settingItemViewHolder.mActionIcon.setImageDrawable(settingItemData.actionDrawable);
            boolean z2 = true;
            boolean z3 = settingItemData.isConnected && settingItemData.isNothingEarDevice && this.mHasPerm;
            boolean z4 = settingItemData.supportAnc;
            updateItemIcon(z3, settingItemViewHolder, settingItemData);
            boolean z5 = this.mAddressCorrespondingExpanded.size() > 0 && settingItemData.macAddress != null && this.mAddressCorrespondingExpanded.get(settingItemData.macAddress) != null && this.mAddressCorrespondingExpanded.get(settingItemData.macAddress).booleanValue() && z3 && z4;
            if (!settingItemData.supportAirpods || TextUtils.isEmpty(this.mBluetoothTileEx.getAirpodsVersion(settingItemData.macAddress)) || !settingItemData.isConnected || !this.mHasPerm) {
                z2 = false;
            }
            if (!z5) {
                settingItemViewHolder.mLayView.setOnClickListener(settingItemData.contentClickListener);
            }
            if (z3 || z2) {
                CachedBluetoothDevice cachedBluetoothDevice = settingItemData.cachedDevice;
                NTLogUtil.m1686d(TAG, "---onBindViewHolder isConnected:" + settingItemData.isConnected + ", macAddress:" + settingItemData.macAddress);
                NTLogUtil.m1686d(TAG, "---onBindViewHolder mAddressCorrespondingSoundLayouts size:" + this.mAddressCorrespondingSoundLayouts.size());
                int intValue = this.mBatteryLeft.getOrDefault(settingItemData.macAddress, -1).intValue();
                int intValue2 = this.mBatteryRight.getOrDefault(settingItemData.macAddress, -1).intValue();
                NTLogUtil.m1686d(TAG, "---onBindViewHolder left:" + intValue + ", right:" + intValue2);
                if (this.mAddressCorrespondingSoundLayouts.size() == 0 || (this.mAddressCorrespondingSoundLayouts.size() > 0 && this.mAddressCorrespondingSoundLayouts.get(settingItemData.macAddress) == null)) {
                    ViewGroup viewGroup = null;
                    BluetoothQuickPanelSoundLayout bluetoothQuickPanelSoundLayout = (BluetoothQuickPanelSoundLayout) LayoutInflater.from(this.mContext).inflate(C1894R.layout.nt_quick_panel_anc_sound, (ViewGroup) null);
                    bluetoothQuickPanelSoundLayout.setAddress(settingItemData.macAddress);
                    bluetoothQuickPanelSoundLayout.setDeviceServiceController(this.mBluetoothTileEx.getDeviceController());
                    bluetoothQuickPanelSoundLayout.setItemAdapter(this);
                    this.mAddressCorrespondingSoundLayouts.put(settingItemData.macAddress, bluetoothQuickPanelSoundLayout);
                    this.mBluetoothTileEx.setModelIdAndDevice(cachedBluetoothDevice);
                    this.mBluetoothTileEx.getCommand(6, settingItemData.macAddress);
                    this.mBluetoothTileEx.getCommand(DeviceConstant.ORDER_ANC, settingItemData.macAddress);
                    if (intValue == -1 || intValue2 == -1) {
                        this.mBluetoothTileEx.getCommand(4, settingItemData.macAddress);
                    }
                    if (!TextUtils.isEmpty(modeID) && (this.mBluetoothTileEx.getIconCache().size() <= 0 || this.mBluetoothTileEx.getIconCache().get(modeID) == null)) {
                        this.mBluetoothTileEx.getCommand(3, cachedBluetoothDevice.getAddress(), false, modeID);
                    }
                    this.mAddressMapCachedBluetoothDevice.put(settingItemData.macAddress, cachedBluetoothDevice);
                    this.mAddressMapCachedSettingItemViewHolders.put(settingItemData.macAddress, settingItemViewHolder);
                }
                if (this.mAddressCorrespondingContain.size() > 0 && this.mAddressCorrespondingContain.get(settingItemData.macAddress) != null) {
                    this.mAddressCorrespondingContain.get(settingItemData.macAddress).removeAllViews();
                }
                CharSequence deviceSecondLabel = this.mBluetoothTileEx.getDeviceSecondLabel(settingItemData.cachedDevice);
                if (!TextUtils.isEmpty(deviceSecondLabel)) {
                    settingItemViewHolder.mSummary.setText(deviceSecondLabel.toString());
                }
                if (!z4) {
                    NTLogUtil.m1686d(TAG, "don't support ANC");
                    return;
                }
                this.mAddressCorrespondingContain.put(settingItemData.macAddress, settingItemViewHolder.mBluetoothExpandViewParent);
                this.mAddressCorrespondingSoundLayouts.get(settingItemData.macAddress).setAddress(settingItemData.macAddress);
                this.mAddressCorrespondingSoundLayouts.get(settingItemData.macAddress).setDeviceServiceController(this.mBluetoothTileEx.getDeviceController());
                settingItemViewHolder.mBluetoothExpandViewParent.addView(this.mAddressCorrespondingSoundLayouts.get(settingItemData.macAddress));
                settingItemViewHolder.mBluetoothExpandIconContainer.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        SettingItemAdapter.this.setBluetoothExpandView(settingItemViewHolder, settingItemData);
                    }
                });
            }
            initBluetoothExpandView(settingItemViewHolder, z5);
        }
    }

    public void setBluetoothExpandView(SettingItemViewHolder settingItemViewHolder, SettingItemData settingItemData) {
        this.mClickSettingRowViewHolder = settingItemViewHolder;
        final LinearLayout linearLayout = settingItemViewHolder.mBluetoothExpandViewParent;
        boolean z = linearLayout.getVisibility() == 0;
        linearLayout.setVisibility(z ? 8 : 0);
        this.mAddressCorrespondingExpanded.put(settingItemData.macAddress, z ? Boolean.FALSE : Boolean.TRUE);
        updateBluetoothExpandIcon(settingItemViewHolder, !z);
        if (z) {
            this.mIsBtPanelAnimating = true;
            AnimUtils.collapse(linearLayout).setAnimationListener(new Animation.AnimationListener() {
                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationStart(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                    boolean unused = SettingItemAdapter.this.mIsBtPanelAnimating = false;
                    SettingItemAdapter.this.notifyDataSetChanged();
                    linearLayout.setVisibility(8);
                }
            });
            return;
        }
        this.mIsBtPanelAnimating = true;
        linearLayout.setAlpha(1.0f);
        linearLayout.setVisibility(0);
        AnimUtils.expand(linearLayout).setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                boolean unused = SettingItemAdapter.this.mIsBtPanelAnimating = false;
                SettingItemAdapter.this.notifyDataSetChanged();
            }
        });
    }

    private void updateDeviceTitleIfNeeded(SettingItemData settingItemData) {
        if (settingItemData.title.equals(settingItemData.macAddress)) {
            settingItemData.title = this.mDeviceName.get(settingItemData.macAddress);
        }
        if (this.mDeviceName.get(settingItemData.macAddress) == null) {
            this.mDeviceName.put(settingItemData.macAddress, settingItemData.title);
            this.mBluetoothTileEx.setModelIdAndDevice(settingItemData.cachedDevice);
        }
    }

    private void initBluetoothExpandView(SettingItemViewHolder settingItemViewHolder, boolean z) {
        settingItemViewHolder.mBluetoothExpandViewParent.setVisibility(z ? 0 : 8);
        updateBluetoothExpandIcon(settingItemViewHolder, z);
    }

    private void updateBluetoothExpandIcon(SettingItemViewHolder settingItemViewHolder, boolean z) {
        settingItemViewHolder.mBluetoothExpandIcon.setImageResource(z ? C1894R.C1896drawable.ic_tesla_panel_up : C1894R.C1896drawable.ic_tesla_panel_down);
        settingItemViewHolder.mBluetoothExpandIcon.setBackgroundResource(C1894R.C1896drawable.tesla_cmd_item_background);
    }

    private void updateItemIcon(boolean z, SettingItemViewHolder settingItemViewHolder, SettingItemData settingItemData) {
        if (!z || !settingItemData.supportAnc) {
            ImageView imageView = settingItemViewHolder.mActionIcon;
            if (settingItemData.actionDrawable == null) {
                settingItemViewHolder.mActionIcon.setVisibility(8);
            } else {
                settingItemViewHolder.mActionIcon.setVisibility(0);
            }
            settingItemViewHolder.mBluetoothExpandIconContainer.setVisibility(8);
        } else if (this.mAddressMapDisServiceConnect.size() <= 0 || this.mAddressMapDisServiceConnect.get(settingItemData.macAddress) == null) {
            settingItemViewHolder.mActionIcon.setVisibility(8);
            settingItemViewHolder.mBluetoothExpandIconContainer.setVisibility(0);
        } else if (this.mAddressMapDisServiceConnect.get(settingItemData.macAddress).booleanValue()) {
            settingItemViewHolder.mActionIcon.setVisibility(0);
            settingItemViewHolder.mBluetoothExpandIconContainer.setVisibility(8);
        } else {
            settingItemViewHolder.mActionIcon.setVisibility(8);
            settingItemViewHolder.mBluetoothExpandIconContainer.setVisibility(0);
        }
    }

    public int getItemCount() {
        List<SettingItemData> list = this.mSettingListsData;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public void setLiveData(LifecycleOwner lifecycleOwner, LiveData<List<SettingItemData>> liveData) {
        if (this.mLiveData != liveData) {
            this.mLiveData = liveData;
            this.mSettingListsData = liveData.getValue();
            this.mLiveData.observe(lifecycleOwner, new Observer<List<SettingItemData>>() {
                public void onChanged(List<SettingItemData> list) {
                    List unused = SettingItemAdapter.this.mSettingListsData = list;
                    if (SettingItemAdapter.this.mIsBtPanelAnimating) {
                        NTLogUtil.m1686d(SettingItemAdapter.TAG, "onChanged isAnimShow");
                    } else {
                        SettingItemAdapter.this.notifyDataSetChanged();
                    }
                }
            });
        }
    }

    public void setPinnedLiveData(LifecycleOwner lifecycleOwner, LiveData<List<SettingItemData>> liveData) {
        if (liveData == null) {
            View view = this.mPinnedHeader;
            if (view != null) {
                view.setVisibility(8);
                return;
            }
            return;
        }
        this.mPinnedHeaderLiveData = liveData;
        List<SettingItemData> value = liveData.getValue();
        this.mPinnedHeaderSettingListsData = value;
        updatePinnedHeader(value);
        this.mPinnedHeaderLiveData.observe(lifecycleOwner, new Observer<List<SettingItemData>>() {
            public void onChanged(List<SettingItemData> list) {
                List unused = SettingItemAdapter.this.mPinnedHeaderSettingListsData = list;
                SettingItemAdapter settingItemAdapter = SettingItemAdapter.this;
                settingItemAdapter.updatePinnedHeader(settingItemAdapter.mPinnedHeaderSettingListsData);
            }
        });
    }

    public void setData(List<SettingItemData> list) {
        this.mSettingListsData = list;
        notifyDataSetChanged();
    }

    public void updatePinnedHeader(List<SettingItemData> list) {
        if (this.mPinnedHeader == null) {
            return;
        }
        if (list == null || list.size() <= 0) {
            this.mPinnedHeader.setVisibility(8);
            return;
        }
        this.mPinnedHeader.setVisibility(0);
        for (SettingItemData next : list) {
            this.mTitle.setText(next.title);
            if (TextUtils.isEmpty(next.subTitle)) {
                this.mSummary.setVisibility(8);
            } else {
                this.mSummary.setVisibility(0);
                this.mSummary.setText(next.subTitle);
            }
            if (next.hasToggle) {
                this.mSwitch.setVisibility(0);
                this.mSwitch.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener) null);
                this.mSwitch.setChecked(next.isChecked);
                this.mLazyCheckedDispatcher.setOnCheckedChangeListener(next.switchListener);
                this.mSwitch.setOnCheckedChangeListener(this.mLazyCheckedDispatcher);
                this.mDivider.setVisibility(0);
            } else {
                this.mSwitch.setVisibility(8);
                this.mDivider.setVisibility(8);
            }
            this.mPinnedHeader.setOnClickListener(next.contentClickListener);
            if (next.titleDrawable != null) {
                this.mTitleIcon.setImageDrawable(next.titleDrawable);
                this.mTitleIcon.setBackground((Drawable) null);
                this.mTitleIcon.setVisibility(0);
                this.mNtTitleIconLayout.setVisibility(0);
            } else {
                this.mTitleIcon.setVisibility(8);
                this.mNtTitleIconLayout.setVisibility(8);
            }
            this.mActionIcon.setImageDrawable(next.actionDrawable);
            this.mActionIcon.setVisibility(next.actionDrawable != null ? 0 : 8);
        }
    }

    /* renamed from: com.nothing.systemui.qs.tiles.settings.panel.SettingItemAdapter$LazyCheckedDispatcher */
    public final class LazyCheckedDispatcher implements CompoundButton.OnCheckedChangeListener {
        private final Runnable mDelayOnCheckedChange;
        private final Handler mHandler;
        /* access modifiers changed from: private */
        public CompoundButton.OnCheckedChangeListener mListener;

        private LazyCheckedDispatcher() {
            this.mHandler = new Handler();
            this.mDelayOnCheckedChange = new Runnable(SettingItemAdapter.this) {
                public void run() {
                    if (LazyCheckedDispatcher.this.mListener != null && SettingItemAdapter.this.mSwitch != null) {
                        LazyCheckedDispatcher.this.mListener.onCheckedChanged(SettingItemAdapter.this.mSwitch, SettingItemAdapter.this.mSwitch.isChecked());
                    }
                }
            };
        }

        public void setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener onCheckedChangeListener) {
            CompoundButton.OnCheckedChangeListener onCheckedChangeListener2 = this.mListener;
            if (!(onCheckedChangeListener2 == null || onCheckedChangeListener == onCheckedChangeListener2)) {
                this.mHandler.removeCallbacks(this.mDelayOnCheckedChange);
            }
            this.mListener = onCheckedChangeListener;
        }

        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            this.mHandler.removeCallbacks(this.mDelayOnCheckedChange);
            this.mHandler.postDelayed(this.mDelayOnCheckedChange, 250);
        }
    }

    /* renamed from: com.nothing.systemui.qs.tiles.settings.panel.SettingItemAdapter$SettingItemViewHolder */
    public class SettingItemViewHolder extends RecyclerView.ViewHolder {
        public ImageView mActionIcon = ((ImageView) this.itemView.findViewById(C1894R.C1898id.icon_action));
        public ImageView mBluetoothExpandIcon = ((ImageView) this.itemView.findViewById(C1894R.C1898id.expand_bluetooth_icon));
        public View mBluetoothExpandIconContainer = this.itemView.findViewById(C1894R.C1898id.expand_bluetooth_icon_container);
        public LinearLayout mBluetoothExpandViewParent = ((LinearLayout) this.itemView.findViewById(C1894R.C1898id.expandeable_bluetooth_control_panel_parent));
        public View mDivider = this.itemView.findViewById(C1894R.C1898id.divider);
        public View mHeadedDivider = this.itemView.findViewById(C1894R.C1898id.headed_divider);
        public LinearLayout mLayView = ((LinearLayout) this.itemView.findViewById(C1894R.C1898id.lay_view));
        public View mNtTitleIconLayout = this.itemView.findViewById(C1894R.C1898id.nt_icon_title_layout);
        public View mRowView = this.itemView.findViewById(C1894R.C1898id.row_view);
        public TextView mSummary = ((TextView) this.itemView.findViewById(C1894R.C1898id.summary));
        public Switch mSwitch = ((Switch) this.itemView.findViewById(C1894R.C1898id.switch_widget));
        public TextView mTitle = ((TextView) this.itemView.findViewById(C1894R.C1898id.title));
        public ImageView mTitleIcon = ((ImageView) this.itemView.findViewById(C1894R.C1898id.icon_title));

        public SettingItemViewHolder(ViewGroup viewGroup) {
            super(LayoutInflater.from(viewGroup.getContext()).inflate(C1894R.layout.nt_panel_setting_item_row, viewGroup, false));
            this.mSwitch.setVisibility(8);
        }
    }
}
