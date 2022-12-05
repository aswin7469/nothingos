package android.app;

import java.util.function.BiConsumer;
/* loaded from: classes.dex */
public final /* synthetic */ class ActivityThread$ApplicationThread$$ExternalSyntheticLambda2 implements BiConsumer {
    public static final /* synthetic */ ActivityThread$ApplicationThread$$ExternalSyntheticLambda2 INSTANCE = new ActivityThread$ApplicationThread$$ExternalSyntheticLambda2();

    private /* synthetic */ ActivityThread$ApplicationThread$$ExternalSyntheticLambda2() {
    }

    @Override // java.util.function.BiConsumer
    public final void accept(Object obj, Object obj2) {
        ((ActivityThread) obj).handleTrimMemory(((Integer) obj2).intValue());
    }
}
