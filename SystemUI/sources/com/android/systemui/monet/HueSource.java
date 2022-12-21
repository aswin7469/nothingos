package com.android.systemui.monet;

import com.android.internal.graphics.cam.Cam;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0000\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0016¨\u0006\u0007"}, mo64987d2 = {"Lcom/android/systemui/monet/HueSource;", "Lcom/android/systemui/monet/Hue;", "()V", "get", "", "sourceColor", "Lcom/android/internal/graphics/cam/Cam;", "monet_release"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ColorScheme.kt */
public final class HueSource implements Hue {
    public double get(Cam cam) {
        Intrinsics.checkNotNullParameter(cam, "sourceColor");
        return (double) cam.getHue();
    }
}
