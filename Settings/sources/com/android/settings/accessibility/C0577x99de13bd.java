package com.android.settings.accessibility;

import android.bluetooth.BluetoothDevice;
import com.android.settingslib.bluetooth.CachedBluetoothDeviceManager;
import java.util.function.Predicate;

/* renamed from: com.android.settings.accessibility.AccessibilityHearingAidPreferenceController$$ExternalSyntheticLambda1 */
/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class C0577x99de13bd implements Predicate {
    public final /* synthetic */ CachedBluetoothDeviceManager f$0;

    public /* synthetic */ C0577x99de13bd(CachedBluetoothDeviceManager cachedBluetoothDeviceManager) {
        this.f$0 = cachedBluetoothDeviceManager;
    }

    public final boolean test(Object obj) {
        return AccessibilityHearingAidPreferenceController.lambda$getConnectedHearingAidDeviceNum$0(this.f$0, (BluetoothDevice) obj);
    }
}
