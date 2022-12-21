package com.nothing.systemui;

import com.nothing.systemui.NTDependencyEx;
import dagger.Lazy;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class NTDependencyEx$$ExternalSyntheticLambda0 implements NTDependencyEx.LazyDependencyCreator {
    public final /* synthetic */ Lazy f$0;

    public /* synthetic */ NTDependencyEx$$ExternalSyntheticLambda0(Lazy lazy) {
        this.f$0 = lazy;
    }

    public final Object createDependency() {
        return this.f$0.get();
    }
}
