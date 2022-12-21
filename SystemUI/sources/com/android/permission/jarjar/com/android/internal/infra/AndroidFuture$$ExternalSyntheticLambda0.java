package com.android.permission.jarjar.com.android.internal.infra;

import java.util.function.BiConsumer;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class AndroidFuture$$ExternalSyntheticLambda0 implements BiConsumer {
    public final /* synthetic */ BiConsumer f$0;
    public final /* synthetic */ BiConsumer f$1;

    public /* synthetic */ AndroidFuture$$ExternalSyntheticLambda0(BiConsumer biConsumer, BiConsumer biConsumer2) {
        this.f$0 = biConsumer;
        this.f$1 = biConsumer2;
    }

    public final void accept(Object obj, Object obj2) {
        AndroidFuture.lambda$whenCompleteAsync$0(this.f$0, this.f$1, obj, (Throwable) obj2);
    }
}
