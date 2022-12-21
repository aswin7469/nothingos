package com.android.systemui.unfold;

import android.view.DisplayInfo;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(mo64986d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0004\b\u0004\u0010\u0005"}, mo64987d2 = {"<anonymous>", "", "it", "Landroid/view/DisplayInfo;", "invoke", "(Landroid/view/DisplayInfo;)Ljava/lang/Boolean;"}, mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: UnfoldLightRevealOverlayAnimation.kt */
final class UnfoldLightRevealOverlayAnimation$getUnfoldedDisplayInfo$2 extends Lambda implements Function1<DisplayInfo, Boolean> {
    public static final UnfoldLightRevealOverlayAnimation$getUnfoldedDisplayInfo$2 INSTANCE = new UnfoldLightRevealOverlayAnimation$getUnfoldedDisplayInfo$2();

    UnfoldLightRevealOverlayAnimation$getUnfoldedDisplayInfo$2() {
        super(1);
    }

    public final Boolean invoke(DisplayInfo displayInfo) {
        Intrinsics.checkNotNullParameter(displayInfo, "it");
        int i = displayInfo.type;
        boolean z = true;
        if (i != 1) {
            z = false;
        }
        return Boolean.valueOf(z);
    }
}
