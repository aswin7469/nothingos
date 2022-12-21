package com.android.systemui.monet;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\f\b\u0000\u0018\u00002\u00020\u0001B-\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\u0006\u0010\u0007\u001a\u00020\u0003¢\u0006\u0002\u0010\bR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\nR\u0011\u0010\u0005\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\nR\u0011\u0010\u0006\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\nR\u0011\u0010\u0007\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\n¨\u0006\u000f"}, mo64987d2 = {"Lcom/android/systemui/monet/CoreSpec;", "", "a1", "Lcom/android/systemui/monet/TonalSpec;", "a2", "a3", "n1", "n2", "(Lcom/android/systemui/monet/TonalSpec;Lcom/android/systemui/monet/TonalSpec;Lcom/android/systemui/monet/TonalSpec;Lcom/android/systemui/monet/TonalSpec;Lcom/android/systemui/monet/TonalSpec;)V", "getA1", "()Lcom/android/systemui/monet/TonalSpec;", "getA2", "getA3", "getN1", "getN2", "monet_release"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ColorScheme.kt */
public final class CoreSpec {

    /* renamed from: a1 */
    private final TonalSpec f320a1;

    /* renamed from: a2 */
    private final TonalSpec f321a2;

    /* renamed from: a3 */
    private final TonalSpec f322a3;

    /* renamed from: n1 */
    private final TonalSpec f323n1;

    /* renamed from: n2 */
    private final TonalSpec f324n2;

    public CoreSpec(TonalSpec tonalSpec, TonalSpec tonalSpec2, TonalSpec tonalSpec3, TonalSpec tonalSpec4, TonalSpec tonalSpec5) {
        Intrinsics.checkNotNullParameter(tonalSpec, "a1");
        Intrinsics.checkNotNullParameter(tonalSpec2, "a2");
        Intrinsics.checkNotNullParameter(tonalSpec3, "a3");
        Intrinsics.checkNotNullParameter(tonalSpec4, "n1");
        Intrinsics.checkNotNullParameter(tonalSpec5, "n2");
        this.f320a1 = tonalSpec;
        this.f321a2 = tonalSpec2;
        this.f322a3 = tonalSpec3;
        this.f323n1 = tonalSpec4;
        this.f324n2 = tonalSpec5;
    }

    public final TonalSpec getA1() {
        return this.f320a1;
    }

    public final TonalSpec getA2() {
        return this.f321a2;
    }

    public final TonalSpec getA3() {
        return this.f322a3;
    }

    public final TonalSpec getN1() {
        return this.f323n1;
    }

    public final TonalSpec getN2() {
        return this.f324n2;
    }
}
