package com.android.systemui.media;

import com.android.p019wm.shell.animation.PhysicsAnimator;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000\u001c\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\"\u000e\u0010\u0000\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0003XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0004\u001a\u00020\u0005XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0006\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0007\u001a\u00020\u0005XT¢\u0006\u0002\n\u0000\"\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000¨\u0006\n"}, mo64987d2 = {"DISMISS_DELAY", "", "FLING_SLOP", "", "RUBBERBAND_FACTOR", "", "SCROLL_DELAY", "SETTINGS_BUTTON_TRANSLATION_FRACTION", "translationConfig", "Lcom/android/wm/shell/animation/PhysicsAnimator$SpringConfig;", "SystemUI_nothingRelease"}, mo64988k = 2, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: MediaCarouselScrollHandler.kt */
public final class MediaCarouselScrollHandlerKt {
    private static final long DISMISS_DELAY = 100;
    private static final int FLING_SLOP = 1000000;
    private static final float RUBBERBAND_FACTOR = 0.2f;
    private static final long SCROLL_DELAY = 100;
    private static final float SETTINGS_BUTTON_TRANSLATION_FRACTION = 0.3f;
    /* access modifiers changed from: private */
    public static final PhysicsAnimator.SpringConfig translationConfig = new PhysicsAnimator.SpringConfig(1500.0f, 0.75f);
}
