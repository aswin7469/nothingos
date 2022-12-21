package com.android.systemui.classifier;

import android.content.res.Resources;
import android.view.ViewConfiguration;
import com.android.systemui.C1893R;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.statusbar.phone.NotificationTapHelper;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.ElementsIntoSet;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.inject.Named;

@Module
public interface FalsingModule {
    public static final String BRIGHT_LINE_GESTURE_CLASSIFERS = "bright_line_gesture_classifiers";
    public static final String DOUBLE_TAP_TIMEOUT_MS = "falsing_double_tap_timeout_ms";
    public static final String DOUBLE_TAP_TOUCH_SLOP = "falsing_double_tap_touch_slop";
    public static final String SINGLE_TAP_TOUCH_SLOP = "falsing_single_tap_touch_slop";

    @Provides
    @Named("falsing_double_tap_timeout_ms")
    static long providesDoubleTapTimeoutMs() {
        return NotificationTapHelper.DOUBLE_TAP_TIMEOUT_MS;
    }

    @SysUISingleton
    @Binds
    FalsingCollector bindsFalsingCollector(FalsingCollectorImpl falsingCollectorImpl);

    @Named("bright_line_gesture_classifiers")
    @ElementsIntoSet
    @Provides
    static Set<FalsingClassifier> providesBrightLineGestureClassifiers(DistanceClassifier distanceClassifier, ProximityClassifier proximityClassifier, PointerCountClassifier pointerCountClassifier, TypeClassifier typeClassifier, DiagonalClassifier diagonalClassifier, ZigZagClassifier zigZagClassifier) {
        return new HashSet(Arrays.asList(pointerCountClassifier, typeClassifier, diagonalClassifier, distanceClassifier, proximityClassifier, zigZagClassifier));
    }

    @Provides
    @Named("falsing_double_tap_touch_slop")
    static float providesDoubleTapTouchSlop(@Main Resources resources) {
        return resources.getDimension(C1893R.dimen.double_tap_slop);
    }

    @Provides
    @Named("falsing_single_tap_touch_slop")
    static float providesSingleTapTouchSlop(ViewConfiguration viewConfiguration) {
        return (float) viewConfiguration.getScaledTouchSlop();
    }
}
