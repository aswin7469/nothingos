package com.android.systemui.unfold;

import android.content.Context;
import android.view.IWindowManager;
import java.util.function.Function;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class UnfoldTransitionModule$$ExternalSyntheticLambda1 implements Function {
    public final /* synthetic */ Context f$0;
    public final /* synthetic */ IWindowManager f$1;

    public /* synthetic */ UnfoldTransitionModule$$ExternalSyntheticLambda1(Context context, IWindowManager iWindowManager) {
        this.f$0 = context;
        this.f$1 = iWindowManager;
    }

    public final Object apply(Object obj) {
        return UnfoldTransitionModule.m3283provideNaturalRotationProgressProvider$lambda1(this.f$0, this.f$1, (UnfoldTransitionProgressProvider) obj);
    }
}
