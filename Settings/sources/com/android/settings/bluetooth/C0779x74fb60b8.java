package com.android.settings.bluetooth;

import androidx.preference.PreferenceCategory;
import java.util.function.Predicate;

/* renamed from: com.android.settings.bluetooth.BluetoothDetailsCompanionAppsController$$ExternalSyntheticLambda3 */
/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class C0779x74fb60b8 implements Predicate {
    public final /* synthetic */ PreferenceCategory f$0;

    public /* synthetic */ C0779x74fb60b8(PreferenceCategory preferenceCategory) {
        this.f$0 = preferenceCategory;
    }

    public final boolean test(Object obj) {
        return BluetoothDetailsCompanionAppsController.lambda$getPreferencesNeedToShow$2(this.f$0, (String) obj);
    }
}
