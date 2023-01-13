package com.android.systemui.monet;

import com.android.internal.graphics.cam.Cam;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010 \n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0000\u0018\u00002\u00020\u0001B\u0017\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\f2\u0006\u0010\u000e\u001a\u00020\u000fR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n¨\u0006\u0010"}, mo65043d2 = {"Lcom/android/systemui/monet/TonalSpec;", "", "hue", "Lcom/android/systemui/monet/Hue;", "chroma", "Lcom/android/systemui/monet/Chroma;", "(Lcom/android/systemui/monet/Hue;Lcom/android/systemui/monet/Chroma;)V", "getChroma", "()Lcom/android/systemui/monet/Chroma;", "getHue", "()Lcom/android/systemui/monet/Hue;", "shades", "", "", "sourceColor", "Lcom/android/internal/graphics/cam/Cam;", "monet_release"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ColorScheme.kt */
public final class TonalSpec {
    private final Chroma chroma;
    private final Hue hue;

    public TonalSpec(Hue hue2, Chroma chroma2) {
        Intrinsics.checkNotNullParameter(hue2, "hue");
        Intrinsics.checkNotNullParameter(chroma2, "chroma");
        this.hue = hue2;
        this.chroma = chroma2;
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ TonalSpec(Hue hue2, Chroma chroma2, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? new HueSource() : hue2, chroma2);
    }

    public final Chroma getChroma() {
        return this.chroma;
    }

    public final Hue getHue() {
        return this.hue;
    }

    public final List<Integer> shades(Cam cam) {
        Intrinsics.checkNotNullParameter(cam, "sourceColor");
        int[] of = Shades.m509of((float) this.hue.get(cam), (float) this.chroma.get(cam));
        Intrinsics.checkNotNullExpressionValue(of, "of(hue.toFloat(), chroma.toFloat())");
        return ArraysKt.toList(of);
    }
}
