package com.android.systemui.statusbar;

import android.content.res.Resources;
import com.android.systemui.C1893R;
import java.util.function.Supplier;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class KeyguardIndicationController$$ExternalSyntheticLambda8 implements Supplier {
    public final /* synthetic */ Resources f$0;
    public final /* synthetic */ CharSequence f$1;

    public /* synthetic */ KeyguardIndicationController$$ExternalSyntheticLambda8(Resources resources, CharSequence charSequence) {
        this.f$0 = resources;
        this.f$1 = charSequence;
    }

    public final Object get() {
        return this.f$0.getString(C1893R.string.do_disclosure_with_name, new Object[]{this.f$1});
    }
}
