package com.android.settings.bluetooth;

import android.accessibilityservice.AccessibilityServiceInfo;
import java.util.List;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class BluetoothDetailsRelatedToolsController$$ExternalSyntheticLambda0 implements Predicate {
    public final /* synthetic */ List f$0;

    public /* synthetic */ BluetoothDetailsRelatedToolsController$$ExternalSyntheticLambda0(List list) {
        this.f$0 = list;
    }

    public final boolean test(Object obj) {
        return this.f$0.contains(((AccessibilityServiceInfo) obj).getComponentName());
    }
}
