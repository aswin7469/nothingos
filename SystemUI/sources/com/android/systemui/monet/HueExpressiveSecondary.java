package com.android.systemui.monet;

import com.android.internal.graphics.cam.Cam;
import com.android.systemui.statusbar.notification.stack.StackStateAnimator;
import java.util.List;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0006\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0000\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0016R#\u0010\u0003\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00060\u00050\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\b¨\u0006\r"}, mo64987d2 = {"Lcom/android/systemui/monet/HueExpressiveSecondary;", "Lcom/android/systemui/monet/Hue;", "()V", "hueToRotations", "", "Lkotlin/Pair;", "", "getHueToRotations", "()Ljava/util/List;", "get", "", "sourceColor", "Lcom/android/internal/graphics/cam/Cam;", "monet_release"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ColorScheme.kt */
public final class HueExpressiveSecondary implements Hue {
    private final List<Pair<Integer, Integer>> hueToRotations = CollectionsKt.listOf(new Pair(0, 45), new Pair(21, 95), new Pair(51, 45), new Pair(121, 20), new Pair(151, 45), new Pair(191, 90), new Pair(271, 45), new Pair(321, 45), new Pair(Integer.valueOf((int) StackStateAnimator.ANIMATION_DURATION_STANDARD), 45));

    public final List<Pair<Integer, Integer>> getHueToRotations() {
        return this.hueToRotations;
    }

    public double get(Cam cam) {
        Intrinsics.checkNotNullParameter(cam, "sourceColor");
        return getHueRotation(cam.getHue(), this.hueToRotations);
    }
}
