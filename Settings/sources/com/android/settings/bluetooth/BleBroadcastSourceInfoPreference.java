package com.android.settings.bluetooth;

import android.bluetooth.BleBroadcastSourceInfo;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import androidx.preference.Preference;
import com.android.settings.R$drawable;
import com.android.settings.R$layout;
import com.android.settings.widget.GearPreference;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;

public final class BleBroadcastSourceInfoPreference extends GearPreference implements CachedBluetoothDevice.Callback {
    private static String EMPTY_BD_ADDR = "00:00:00:00:00:00";
    private BleBroadcastSourceInfo mBleSourceInfo;
    private final CachedBluetoothDevice mCachedDevice;
    private final long mCurrentTime;
    private final Integer mIndex;
    Resources mResources = getContext().getResources();
    private final int mType;

    public BleBroadcastSourceInfoPreference(Context context, CachedBluetoothDevice cachedBluetoothDevice, BleBroadcastSourceInfo bleBroadcastSourceInfo, Integer num, int i) {
        super(context, (AttributeSet) null);
        this.mIndex = num;
        this.mCachedDevice = cachedBluetoothDevice;
        this.mBleSourceInfo = bleBroadcastSourceInfo;
        cachedBluetoothDevice.registerCallback(this);
        this.mCurrentTime = System.currentTimeMillis();
        this.mType = i;
        onDeviceAttributesChanged();
    }

    /* access modifiers changed from: protected */
    public boolean shouldHideSecondTarget() {
        return this.mBleSourceInfo == null;
    }

    /* access modifiers changed from: protected */
    public int getSecondTargetResId() {
        return R$layout.preference_widget_gear;
    }

    public BleBroadcastSourceInfo getBleBroadcastSourceInfo() {
        return this.mBleSourceInfo;
    }

    public void setBleBroadcastSourceInfo(BleBroadcastSourceInfo bleBroadcastSourceInfo) {
        this.mBleSourceInfo = bleBroadcastSourceInfo;
        onDeviceAttributesChanged();
    }

    /* access modifiers changed from: package-private */
    public Integer getSourceInfoIndex() {
        return this.mIndex;
    }

    /* access modifiers changed from: protected */
    public void onPrepareForRemoval() {
        super.onPrepareForRemoval();
        this.mCachedDevice.unregisterCallback(this);
    }

    /* access modifiers changed from: package-private */
    public String formSyncSummaryString(BleBroadcastSourceInfo bleBroadcastSourceInfo) {
        String str = bleBroadcastSourceInfo.getMetadataSyncState() == 2 ? "Metadata Synced" : "Metadata not synced";
        String str2 = bleBroadcastSourceInfo.getAudioSyncState() == 1 ? "Audio Synced" : "Audio not synced";
        return str + ", " + str2;
    }

    public void onDeviceAttributesChanged() {
        String str;
        BluetoothDevice sourceDevice = this.mBleSourceInfo.getSourceDevice();
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        if (sourceDevice == null || defaultAdapter == null) {
            str = null;
        } else {
            if (defaultAdapter.getAddress().equals(sourceDevice.getAddress())) {
                str = defaultAdapter.getName() + "(Self)";
            } else {
                str = sourceDevice.getAlias();
            }
            if (str == null) {
                str = String.valueOf(sourceDevice.getAddress());
            }
        }
        if (str == null || str.equals(EMPTY_BD_ADDR)) {
            BroadcastScanAssistanceUtils.debug("BleBroadcastSourceInfoPreference", "seem to be an entry source Info");
            str = "EMPTY ENTRY";
        }
        setTitle((CharSequence) str);
        setIcon(R$drawable.ic_media_stream);
        if (!this.mBleSourceInfo.isEmptyEntry()) {
            setSummary((CharSequence) formSyncSummaryString(this.mBleSourceInfo));
        } else {
            setSummary((CharSequence) "");
        }
        setVisible(true);
        notifyHierarchyChanged();
    }

    public boolean equals(Object obj) {
        boolean z = false;
        if (obj == null || !(obj instanceof BleBroadcastSourceInfoPreference)) {
            BroadcastScanAssistanceUtils.debug("BleBroadcastSourceInfoPreference", "Not an Instance of BleBroadcastSourceInfoPreference:");
            return false;
        }
        BleBroadcastSourceInfoPreference bleBroadcastSourceInfoPreference = (BleBroadcastSourceInfoPreference) obj;
        BleBroadcastSourceInfo bleBroadcastSourceInfo = bleBroadcastSourceInfoPreference.mBleSourceInfo;
        BroadcastScanAssistanceUtils.debug("BleBroadcastSourceInfoPreference", "Comparing: " + this.mBleSourceInfo);
        BroadcastScanAssistanceUtils.debug("BleBroadcastSourceInfoPreference", "TO: " + bleBroadcastSourceInfo);
        if (this.mBleSourceInfo.getSourceId() == bleBroadcastSourceInfo.getSourceId() && this.mIndex == bleBroadcastSourceInfoPreference.getSourceInfoIndex()) {
            z = true;
        }
        BroadcastScanAssistanceUtils.debug("BleBroadcastSourceInfoPreference", "equals returns: " + z);
        return z;
    }

    public int hashCode() {
        return this.mBleSourceInfo.hashCode();
    }

    public int compareTo(Preference preference) {
        if (!(preference instanceof BleBroadcastSourceInfoPreference)) {
            return super.compareTo(preference);
        }
        int i = this.mType;
        if (i == 1) {
            BroadcastScanAssistanceUtils.debug("BleBroadcastSourceInfoPreference", ">>compareTo");
            if (this.mIndex.intValue() > ((BleBroadcastSourceInfoPreference) preference).getSourceInfoIndex().intValue()) {
                return 1;
            }
            return -1;
        } else if (i != 2) {
            return super.compareTo(preference);
        } else {
            if (this.mCurrentTime > ((BleBroadcastSourceInfoPreference) preference).mCurrentTime) {
                return 1;
            }
            return -1;
        }
    }
}
