package com.android.settings.applications;

import android.content.pm.ResolveInfo;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class AppLocaleUtil$$ExternalSyntheticLambda0 implements Predicate {
    public final /* synthetic */ String f$0;

    public /* synthetic */ AppLocaleUtil$$ExternalSyntheticLambda0(String str) {
        this.f$0 = str;
    }

    public final boolean test(Object obj) {
        return ((ResolveInfo) obj).activityInfo.packageName.equals(this.f$0);
    }
}
