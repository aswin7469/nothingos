package com.android.settings.bluetooth;

import android.bluetooth.BleBroadcastSourceInfo;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.res.Resources;
import androidx.preference.Preference;
import com.android.settings.R;
import com.android.settings.widget.GearPreference;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
/* loaded from: classes.dex */
public final class BleBroadcastSourceInfoPreference extends GearPreference implements CachedBluetoothDevice.Callback {
    private static String EMPTY_BD_ADDR = "00:00:00:00:00:00";
    private BleBroadcastSourceInfo mBleSourceInfo;
    private final CachedBluetoothDevice mCachedDevice;
    private final Integer mIndex;
    private final int mType;
    Resources mResources = getContext().getResources();
    private final long mCurrentTime = System.currentTimeMillis();

    public BleBroadcastSourceInfoPreference(Context context, CachedBluetoothDevice cachedBluetoothDevice, BleBroadcastSourceInfo bleBroadcastSourceInfo, Integer num, int i) {
        super(context, null);
        this.mIndex = num;
        this.mCachedDevice = cachedBluetoothDevice;
        this.mBleSourceInfo = bleBroadcastSourceInfo;
        cachedBluetoothDevice.registerCallback(this);
        this.mType = i;
        onDeviceAttributesChanged();
    }

    @Override // com.android.settings.widget.GearPreference, com.android.settingslib.RestrictedPreference, com.android.settingslib.widget.TwoTargetPreference
    protected boolean shouldHideSecondTarget() {
        return this.mBleSourceInfo == null;
    }

    @Override // com.android.settings.widget.GearPreference, com.android.settingslib.RestrictedPreference, com.android.settingslib.widget.TwoTargetPreference
    protected int getSecondTargetResId() {
        return R.layout.preference_widget_gear;
    }

    public BleBroadcastSourceInfo getBleBroadcastSourceInfo() {
        return this.mBleSourceInfo;
    }

    public void setBleBroadcastSourceInfo(BleBroadcastSourceInfo bleBroadcastSourceInfo) {
        this.mBleSourceInfo = bleBroadcastSourceInfo;
        onDeviceAttributesChanged();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Integer getSourceInfoIndex() {
        return this.mIndex;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.preference.Preference
    public void onPrepareForRemoval() {
        super.onPrepareForRemoval();
        this.mCachedDevice.unregisterCallback(this);
    }

    String formSyncSummaryString(BleBroadcastSourceInfo bleBroadcastSourceInfo) {
        String str = bleBroadcastSourceInfo.getMetadataSyncState() == 2 ? "Metadata Synced" : "Metadata not synced";
        String str2 = bleBroadcastSourceInfo.getAudioSyncState() == 1 ? "Audio Synced" : "Audio not synced";
        return str + ", " + str2;
    }

    @Override // com.android.settingslib.bluetooth.CachedBluetoothDevice.Callback
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
        setTitle(str);
        setIcon(R.drawable.ic_media_stream);
        if (!this.mBleSourceInfo.isEmptyEntry()) {
            setSummary(formSyncSummaryString(this.mBleSourceInfo));
        } else {
            setSummary("");
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
        BleBroadcastSourceInfo bleBroadcastSourceInfo = ((BleBroadcastSourceInfoPreference) obj).mBleSourceInfo;
        BroadcastScanAssistanceUtils.debug("BleBroadcastSourceInfoPreference", "Comparing: " + this.mBleSourceInfo);
        BroadcastScanAssistanceUtils.debug("BleBroadcastSourceInfoPreference", "TO: " + bleBroadcastSourceInfo);
        if (this.mBleSourceInfo.getSourceId() == bleBroadcastSourceInfo.getSourceId()) {
            z = true;
        }
        BroadcastScanAssistanceUtils.debug("BleBroadcastSourceInfoPreference", "equals returns: " + z);
        return z;
    }

    public int hashCode() {
        return this.mBleSourceInfo.hashCode();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // androidx.preference.Preference, java.lang.Comparable
    public int compareTo(Preference preference) {
        if (!(preference instanceof BleBroadcastSourceInfoPreference)) {
            return super.compareTo(preference);
        }
        int i = this.mType;
        if (i == 1) {
            BroadcastScanAssistanceUtils.debug("BleBroadcastSourceInfoPreference", ">>compareTo");
            return this.mIndex.intValue() > ((BleBroadcastSourceInfoPreference) preference).getSourceInfoIndex().intValue() ? 1 : -1;
        } else if (i != 2) {
            return super.compareTo(preference);
        } else {
            return this.mCurrentTime > ((BleBroadcastSourceInfoPreference) preference).mCurrentTime ? 1 : -1;
        }
    }
}
