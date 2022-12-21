package com.android.systemui.media.dream.dagger;

import android.content.Context;
import android.widget.FrameLayout;
import com.android.systemui.media.dream.dagger.MediaComplicationComponent;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

/* renamed from: com.android.systemui.media.dream.dagger.MediaComplicationComponent_MediaComplicationModule_ProvideComplicationContainerFactory */
public final class C2231x5add62f3 implements Factory<FrameLayout> {
    private final Provider<Context> contextProvider;

    public C2231x5add62f3(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    public FrameLayout get() {
        return provideComplicationContainer(this.contextProvider.get());
    }

    public static C2231x5add62f3 create(Provider<Context> provider) {
        return new C2231x5add62f3(provider);
    }

    public static FrameLayout provideComplicationContainer(Context context) {
        return (FrameLayout) Preconditions.checkNotNullFromProvides(MediaComplicationComponent.MediaComplicationModule.provideComplicationContainer(context));
    }
}
