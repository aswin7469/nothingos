package com.android.systemui.dreams.touch.dagger;

import android.content.res.Resources;
import android.util.TypedValue;
import com.android.p019wm.shell.animation.FlingAnimationUtils;
import com.android.systemui.C1894R;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.dreams.touch.BouncerSwipeTouchHandler;
import com.android.systemui.dreams.touch.DreamTouchHandler;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import javax.inject.Named;
import javax.inject.Provider;

@Module
public class BouncerSwipeModule {
    public static final String SWIPE_TO_BOUNCER_FLING_ANIMATION_UTILS_CLOSING = "swipe_to_bouncer_fling_animation_utils_closing";
    public static final String SWIPE_TO_BOUNCER_FLING_ANIMATION_UTILS_OPENING = "swipe_to_bouncer_fling_animation_utils_opening";
    public static final String SWIPE_TO_BOUNCER_START_REGION = "swipe_to_bouncer_start_region";

    @IntoSet
    @Provides
    public static DreamTouchHandler providesBouncerSwipeTouchHandler(BouncerSwipeTouchHandler bouncerSwipeTouchHandler) {
        return bouncerSwipeTouchHandler;
    }

    @Provides
    @Named("swipe_to_bouncer_fling_animation_utils_closing")
    public static FlingAnimationUtils providesSwipeToBouncerFlingAnimationUtilsClosing(Provider<FlingAnimationUtils.Builder> provider) {
        return provider.get().reset().setMaxLengthSeconds(0.6f).setSpeedUpFactor(0.6f).build();
    }

    @Provides
    @Named("swipe_to_bouncer_fling_animation_utils_opening")
    public static FlingAnimationUtils providesSwipeToBouncerFlingAnimationUtilsOpening(Provider<FlingAnimationUtils.Builder> provider) {
        return provider.get().reset().setMaxLengthSeconds(0.6f).setSpeedUpFactor(0.6f).build();
    }

    @Provides
    @Named("swipe_to_bouncer_start_region")
    public static float providesSwipeToBouncerStartRegion(@Main Resources resources) {
        TypedValue typedValue = new TypedValue();
        resources.getValue(C1894R.dimen.dream_overlay_bouncer_start_region_screen_percentage, typedValue, true);
        return typedValue.getFloat();
    }

    @Provides
    public static BouncerSwipeTouchHandler.ValueAnimatorCreator providesValueAnimatorCreator() {
        return new BouncerSwipeModule$$ExternalSyntheticLambda1();
    }

    @Provides
    public static BouncerSwipeTouchHandler.VelocityTrackerFactory providesVelocityTrackerFactory() {
        return new BouncerSwipeModule$$ExternalSyntheticLambda0();
    }
}
