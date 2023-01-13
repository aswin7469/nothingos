package com.android.systemui.monet;

import com.android.internal.graphics.cam.Cam;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\b\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0010\u0010\u0007\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\tH\u0016R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\n"}, mo65043d2 = {"Lcom/android/systemui/monet/HueSubtract;", "Lcom/android/systemui/monet/Hue;", "amountDegrees", "", "(D)V", "getAmountDegrees", "()D", "get", "sourceColor", "Lcom/android/internal/graphics/cam/Cam;", "monet_release"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ColorScheme.kt */
public final class HueSubtract implements Hue {
    private final double amountDegrees;

    public HueSubtract(double d) {
        this.amountDegrees = d;
    }

    public final double getAmountDegrees() {
        return this.amountDegrees;
    }

    public double get(Cam cam) {
        Intrinsics.checkNotNullParameter(cam, "sourceColor");
        return ColorScheme.Companion.wrapDegreesDouble(((double) cam.getHue()) - this.amountDegrees);
    }
}
