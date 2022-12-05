package com.android.systemui.statusbar.phone.dagger;

import com.airbnb.lottie.LottieAnimationView;
import com.android.systemui.statusbar.phone.NotificationShadeWindowView;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class StatusBarViewModule_GetLottieAnimationViewFactory implements Factory<LottieAnimationView> {
    private final Provider<NotificationShadeWindowView> notificationShadeWindowViewProvider;

    public StatusBarViewModule_GetLottieAnimationViewFactory(Provider<NotificationShadeWindowView> provider) {
        this.notificationShadeWindowViewProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public LottieAnimationView mo1933get() {
        return getLottieAnimationView(this.notificationShadeWindowViewProvider.mo1933get());
    }

    public static StatusBarViewModule_GetLottieAnimationViewFactory create(Provider<NotificationShadeWindowView> provider) {
        return new StatusBarViewModule_GetLottieAnimationViewFactory(provider);
    }

    public static LottieAnimationView getLottieAnimationView(NotificationShadeWindowView notificationShadeWindowView) {
        return (LottieAnimationView) Preconditions.checkNotNullFromProvides(StatusBarViewModule.getLottieAnimationView(notificationShadeWindowView));
    }
}
