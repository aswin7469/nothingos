package com.android.settings.bluetooth;

import android.accessibilityservice.AccessibilityShortcutInfo;
import java.util.List;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class BluetoothDetailsRelatedToolsController$$ExternalSyntheticLambda1 implements Predicate {
    public final /* synthetic */ List f$0;

    public /* synthetic */ BluetoothDetailsRelatedToolsController$$ExternalSyntheticLambda1(List list) {
        this.f$0 = list;
    }

    public final boolean test(Object obj) {
        return this.f$0.contains(((AccessibilityShortcutInfo) obj).getComponentName());
    }
}
