package com.android.systemui.monet;

import com.android.internal.graphics.cam.Cam;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\b\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0010\u0010\u0007\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\tH\u0016R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\n"}, mo65043d2 = {"Lcom/android/systemui/monet/ChromaConstant;", "Lcom/android/systemui/monet/Chroma;", "chroma", "", "(D)V", "getChroma", "()D", "get", "sourceColor", "Lcom/android/internal/graphics/cam/Cam;", "monet_release"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ColorScheme.kt */
public final class ChromaConstant implements Chroma {
    private final double chroma;

    public ChromaConstant(double d) {
        this.chroma = d;
    }

    public final double getChroma() {
        return this.chroma;
    }

    public double get(Cam cam) {
        Intrinsics.checkNotNullParameter(cam, "sourceColor");
        return this.chroma;
    }
}
