package com.android.systemui.dreams.dagger;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import com.android.internal.util.Preconditions;
import com.android.systemui.C1893R;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.dreams.DreamOverlayContainerView;
import com.android.systemui.dreams.DreamOverlayStatusBarView;
import com.android.systemui.dreams.dagger.DreamOverlayComponent;
import com.android.systemui.touch.TouchInsetManager;
import dagger.Lazy;
import dagger.Module;
import dagger.Provides;
import java.util.concurrent.Executor;
import javax.inject.Named;

@Module
public abstract class DreamOverlayModule {
    public static final String BURN_IN_PROTECTION_UPDATE_INTERVAL = "burn_in_protection_update_interval";
    public static final String DREAM_OVERLAY_CONTENT_VIEW = "dream_overlay_content_view";
    public static final String MAX_BURN_IN_OFFSET = "max_burn_in_offset";
    public static final String MILLIS_UNTIL_FULL_JITTER = "millis_until_full_jitter";

    @DreamOverlayComponent.DreamOverlayScope
    @Provides
    public static DreamOverlayContainerView providesDreamOverlayContainerView(LayoutInflater layoutInflater) {
        return (DreamOverlayContainerView) Preconditions.checkNotNull((DreamOverlayContainerView) layoutInflater.inflate(C1893R.layout.dream_overlay_container, (ViewGroup) null), "R.layout.dream_layout_container could not be properly inflated");
    }

    @DreamOverlayComponent.DreamOverlayScope
    @Provides
    @Named("dream_overlay_content_view")
    public static ViewGroup providesDreamOverlayContentView(DreamOverlayContainerView dreamOverlayContainerView) {
        return (ViewGroup) Preconditions.checkNotNull((ViewGroup) dreamOverlayContainerView.findViewById(C1893R.C1897id.dream_overlay_content), "R.id.dream_overlay_content must not be null");
    }

    @Provides
    public static TouchInsetManager.TouchInsetSession providesTouchInsetSession(TouchInsetManager touchInsetManager) {
        return touchInsetManager.createSession();
    }

    @DreamOverlayComponent.DreamOverlayScope
    @Provides
    public static TouchInsetManager providesTouchInsetManager(@Main Executor executor, DreamOverlayContainerView dreamOverlayContainerView) {
        return new TouchInsetManager(executor, dreamOverlayContainerView);
    }

    @DreamOverlayComponent.DreamOverlayScope
    @Provides
    public static DreamOverlayStatusBarView providesDreamOverlayStatusBarView(DreamOverlayContainerView dreamOverlayContainerView) {
        return (DreamOverlayStatusBarView) Preconditions.checkNotNull((DreamOverlayStatusBarView) dreamOverlayContainerView.findViewById(C1893R.C1897id.dream_overlay_status_bar), "R.id.status_bar must not be null");
    }

    @DreamOverlayComponent.DreamOverlayScope
    @Provides
    @Named("max_burn_in_offset")
    static int providesMaxBurnInOffset(@Main Resources resources) {
        return resources.getDimensionPixelSize(C1893R.dimen.default_burn_in_prevention_offset);
    }

    @Provides
    @Named("burn_in_protection_update_interval")
    static long providesBurnInProtectionUpdateInterval(@Main Resources resources) {
        return (long) resources.getInteger(C1893R.integer.config_dreamOverlayBurnInProtectionUpdateIntervalMillis);
    }

    @Provides
    @Named("millis_until_full_jitter")
    static long providesMillisUntilFullJitter(@Main Resources resources) {
        return (long) resources.getInteger(C1893R.integer.config_dreamOverlayMillisUntilFullJitter);
    }

    static /* synthetic */ Lifecycle lambda$providesLifecycleOwner$0(Lazy lazy) {
        return (Lifecycle) lazy.get();
    }

    @DreamOverlayComponent.DreamOverlayScope
    @Provides
    static LifecycleOwner providesLifecycleOwner(Lazy<LifecycleRegistry> lazy) {
        return new DreamOverlayModule$$ExternalSyntheticLambda0(lazy);
    }

    @DreamOverlayComponent.DreamOverlayScope
    @Provides
    static LifecycleRegistry providesLifecycleRegistry(LifecycleOwner lifecycleOwner) {
        return new LifecycleRegistry(lifecycleOwner);
    }

    @DreamOverlayComponent.DreamOverlayScope
    @Provides
    static Lifecycle providesLifecycle(LifecycleOwner lifecycleOwner) {
        return lifecycleOwner.getLifecycle();
    }
}
