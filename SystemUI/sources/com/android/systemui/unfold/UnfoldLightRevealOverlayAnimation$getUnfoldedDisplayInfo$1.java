package com.android.systemui.unfold;

import android.view.Display;
import android.view.DisplayInfo;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;

@Metadata(mo64986d1 = {"\u0000\u0010\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u00012\u000e\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u0003H\n¢\u0006\u0002\b\u0005"}, mo64987d2 = {"<anonymous>", "Landroid/view/DisplayInfo;", "it", "Landroid/view/Display;", "kotlin.jvm.PlatformType", "invoke"}, mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: UnfoldLightRevealOverlayAnimation.kt */
final class UnfoldLightRevealOverlayAnimation$getUnfoldedDisplayInfo$1 extends Lambda implements Function1<Display, DisplayInfo> {
    public static final UnfoldLightRevealOverlayAnimation$getUnfoldedDisplayInfo$1 INSTANCE = new UnfoldLightRevealOverlayAnimation$getUnfoldedDisplayInfo$1();

    UnfoldLightRevealOverlayAnimation$getUnfoldedDisplayInfo$1() {
        super(1);
    }

    public final DisplayInfo invoke(Display display) {
        DisplayInfo displayInfo = new DisplayInfo();
        display.getDisplayInfo(displayInfo);
        return displayInfo;
    }
}
