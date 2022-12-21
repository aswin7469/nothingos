package com.android.systemui.statusbar;

import java.util.function.Supplier;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class KeyguardIndicationController$$ExternalSyntheticLambda9 implements Supplier {
    public final /* synthetic */ KeyguardIndicationController f$0;

    public /* synthetic */ KeyguardIndicationController$$ExternalSyntheticLambda9(KeyguardIndicationController keyguardIndicationController) {
        this.f$0 = keyguardIndicationController;
    }

    public final Object get() {
        return Boolean.valueOf(this.f$0.isOrganizationOwnedDevice());
    }
}
