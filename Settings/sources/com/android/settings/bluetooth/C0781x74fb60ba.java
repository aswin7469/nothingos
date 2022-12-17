package com.android.settings.bluetooth;

import android.companion.AssociationInfo;
import com.google.common.base.Objects;
import java.util.function.Predicate;

/* renamed from: com.android.settings.bluetooth.BluetoothDetailsCompanionAppsController$$ExternalSyntheticLambda5 */
/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class C0781x74fb60ba implements Predicate {
    public final /* synthetic */ String f$0;

    public /* synthetic */ C0781x74fb60ba(String str) {
        this.f$0 = str;
    }

    public final boolean test(Object obj) {
        return Objects.equal(this.f$0, ((AssociationInfo) obj).getDeviceMacAddress());
    }
}
