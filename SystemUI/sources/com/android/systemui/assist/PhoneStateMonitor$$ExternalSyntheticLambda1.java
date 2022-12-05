package com.android.systemui.assist;

import dagger.Lazy;
import java.util.function.Function;
/* loaded from: classes.dex */
public final /* synthetic */ class PhoneStateMonitor$$ExternalSyntheticLambda1 implements Function {
    public static final /* synthetic */ PhoneStateMonitor$$ExternalSyntheticLambda1 INSTANCE = new PhoneStateMonitor$$ExternalSyntheticLambda1();

    private /* synthetic */ PhoneStateMonitor$$ExternalSyntheticLambda1() {
    }

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        Boolean lambda$isBouncerShowing$1;
        lambda$isBouncerShowing$1 = PhoneStateMonitor.lambda$isBouncerShowing$1((Lazy) obj);
        return lambda$isBouncerShowing$1;
    }
}
