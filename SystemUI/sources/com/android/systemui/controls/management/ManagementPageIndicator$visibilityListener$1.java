package com.android.systemui.controls.management;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: ManagementPageIndicator.kt */
/* loaded from: classes.dex */
final class ManagementPageIndicator$visibilityListener$1 extends Lambda implements Function1<Integer, Unit> {
    public static final ManagementPageIndicator$visibilityListener$1 INSTANCE = new ManagementPageIndicator$visibilityListener$1();

    ManagementPageIndicator$visibilityListener$1() {
        super(1);
    }

    public final void invoke(int i) {
    }

    @Override // kotlin.jvm.functions.Function1
    /* renamed from: invoke */
    public /* bridge */ /* synthetic */ Unit mo1949invoke(Integer num) {
        invoke(num.intValue());
        return Unit.INSTANCE;
    }
}
