package com.android.systemui.dreams.touch;

import android.util.DisplayMetrics;
import com.android.internal.logging.UiEventLogger;
import com.android.p019wm.shell.animation.FlingAnimationUtils;
import com.android.systemui.dreams.touch.BouncerSwipeTouchHandler;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;

public final class BouncerSwipeTouchHandler_Factory implements Factory<BouncerSwipeTouchHandler> {
    private final Provider<Optional<CentralSurfaces>> centralSurfacesProvider;
    private final Provider<DisplayMetrics> displayMetricsProvider;
    private final Provider<FlingAnimationUtils> flingAnimationUtilsClosingProvider;
    private final Provider<FlingAnimationUtils> flingAnimationUtilsProvider;
    private final Provider<NotificationShadeWindowController> notificationShadeWindowControllerProvider;
    private final Provider<StatusBarKeyguardViewManager> statusBarKeyguardViewManagerProvider;
    private final Provider<Float> swipeRegionPercentageProvider;
    private final Provider<UiEventLogger> uiEventLoggerProvider;
    private final Provider<BouncerSwipeTouchHandler.ValueAnimatorCreator> valueAnimatorCreatorProvider;
    private final Provider<BouncerSwipeTouchHandler.VelocityTrackerFactory> velocityTrackerFactoryProvider;

    public BouncerSwipeTouchHandler_Factory(Provider<DisplayMetrics> provider, Provider<StatusBarKeyguardViewManager> provider2, Provider<Optional<CentralSurfaces>> provider3, Provider<NotificationShadeWindowController> provider4, Provider<BouncerSwipeTouchHandler.ValueAnimatorCreator> provider5, Provider<BouncerSwipeTouchHandler.VelocityTrackerFactory> provider6, Provider<FlingAnimationUtils> provider7, Provider<FlingAnimationUtils> provider8, Provider<Float> provider9, Provider<UiEventLogger> provider10) {
        this.displayMetricsProvider = provider;
        this.statusBarKeyguardViewManagerProvider = provider2;
        this.centralSurfacesProvider = provider3;
        this.notificationShadeWindowControllerProvider = provider4;
        this.valueAnimatorCreatorProvider = provider5;
        this.velocityTrackerFactoryProvider = provider6;
        this.flingAnimationUtilsProvider = provider7;
        this.flingAnimationUtilsClosingProvider = provider8;
        this.swipeRegionPercentageProvider = provider9;
        this.uiEventLoggerProvider = provider10;
    }

    public BouncerSwipeTouchHandler get() {
        return newInstance(this.displayMetricsProvider.get(), this.statusBarKeyguardViewManagerProvider.get(), this.centralSurfacesProvider.get(), this.notificationShadeWindowControllerProvider.get(), this.valueAnimatorCreatorProvider.get(), this.velocityTrackerFactoryProvider.get(), this.flingAnimationUtilsProvider.get(), this.flingAnimationUtilsClosingProvider.get(), this.swipeRegionPercentageProvider.get().floatValue(), this.uiEventLoggerProvider.get());
    }

    public static BouncerSwipeTouchHandler_Factory create(Provider<DisplayMetrics> provider, Provider<StatusBarKeyguardViewManager> provider2, Provider<Optional<CentralSurfaces>> provider3, Provider<NotificationShadeWindowController> provider4, Provider<BouncerSwipeTouchHandler.ValueAnimatorCreator> provider5, Provider<BouncerSwipeTouchHandler.VelocityTrackerFactory> provider6, Provider<FlingAnimationUtils> provider7, Provider<FlingAnimationUtils> provider8, Provider<Float> provider9, Provider<UiEventLogger> provider10) {
        return new BouncerSwipeTouchHandler_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10);
    }

    public static BouncerSwipeTouchHandler newInstance(DisplayMetrics displayMetrics, StatusBarKeyguardViewManager statusBarKeyguardViewManager, Optional<CentralSurfaces> optional, NotificationShadeWindowController notificationShadeWindowController, BouncerSwipeTouchHandler.ValueAnimatorCreator valueAnimatorCreator, BouncerSwipeTouchHandler.VelocityTrackerFactory velocityTrackerFactory, FlingAnimationUtils flingAnimationUtils, FlingAnimationUtils flingAnimationUtils2, float f, UiEventLogger uiEventLogger) {
        return new BouncerSwipeTouchHandler(displayMetrics, statusBarKeyguardViewManager, optional, notificationShadeWindowController, valueAnimatorCreator, velocityTrackerFactory, flingAnimationUtils, flingAnimationUtils2, f, uiEventLogger);
    }
}
