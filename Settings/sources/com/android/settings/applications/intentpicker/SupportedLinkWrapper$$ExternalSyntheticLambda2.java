package com.android.settings.applications.intentpicker;

import android.content.Context;
import android.content.pm.verify.domain.DomainOwner;
import java.util.function.Function;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class SupportedLinkWrapper$$ExternalSyntheticLambda2 implements Function {
    public final /* synthetic */ SupportedLinkWrapper f$0;
    public final /* synthetic */ Context f$1;

    public /* synthetic */ SupportedLinkWrapper$$ExternalSyntheticLambda2(SupportedLinkWrapper supportedLinkWrapper, Context context) {
        this.f$0 = supportedLinkWrapper;
        this.f$1 = context;
    }

    public final Object apply(Object obj) {
        return this.f$0.lambda$getLastPackageLabel$2(this.f$1, (DomainOwner) obj);
    }
}
