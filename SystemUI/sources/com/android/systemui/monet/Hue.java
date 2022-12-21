package com.android.systemui.monet;

import com.android.internal.graphics.cam.Cam;
import java.util.List;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0006\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\b`\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J*\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\b2\u0018\u0010\t\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\f0\u000b0\nH\u0016ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\rÀ\u0006\u0001"}, mo64987d2 = {"Lcom/android/systemui/monet/Hue;", "", "get", "", "sourceColor", "Lcom/android/internal/graphics/cam/Cam;", "getHueRotation", "sourceHue", "", "hueAndRotations", "", "Lkotlin/Pair;", "", "monet_release"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ColorScheme.kt */
public interface Hue {
    double get(Cam cam);

    double getHueRotation(float f, List<Pair<Integer, Integer>> list) {
        Intrinsics.checkNotNullParameter(list, "hueAndRotations");
        int i = 0;
        float floatValue = ((f < 0.0f || f >= 360.0f) ? 0 : Float.valueOf(f)).floatValue();
        int size = list.size() - 2;
        if (size >= 0) {
            while (true) {
                int i2 = i + 1;
                float intValue = (float) ((Number) list.get(i2).getFirst()).intValue();
                if (((float) ((Number) list.get(i).getFirst()).intValue()) > floatValue || floatValue >= intValue) {
                    if (i == size) {
                        break;
                    }
                    i = i2;
                } else {
                    return ColorScheme.Companion.wrapDegreesDouble(((double) floatValue) + ((Number) list.get(i).getSecond()).doubleValue());
                }
            }
        }
        return (double) f;
    }
}
