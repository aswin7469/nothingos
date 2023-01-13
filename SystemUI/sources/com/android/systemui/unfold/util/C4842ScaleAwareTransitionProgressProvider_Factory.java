package com.android.systemui.unfold.util;

import android.content.ContentResolver;
import com.android.systemui.unfold.UnfoldTransitionProgressProvider;
import javax.inject.Provider;

/* renamed from: com.android.systemui.unfold.util.ScaleAwareTransitionProgressProvider_Factory  reason: case insensitive filesystem */
public final class C4842ScaleAwareTransitionProgressProvider_Factory {
    private final Provider<ContentResolver> contentResolverProvider;

    public C4842ScaleAwareTransitionProgressProvider_Factory(Provider<ContentResolver> provider) {
        this.contentResolverProvider = provider;
    }

    public ScaleAwareTransitionProgressProvider get(UnfoldTransitionProgressProvider unfoldTransitionProgressProvider) {
        return newInstance(unfoldTransitionProgressProvider, this.contentResolverProvider.get());
    }

    public static C4842ScaleAwareTransitionProgressProvider_Factory create(Provider<ContentResolver> provider) {
        return new C4842ScaleAwareTransitionProgressProvider_Factory(provider);
    }

    public static ScaleAwareTransitionProgressProvider newInstance(UnfoldTransitionProgressProvider unfoldTransitionProgressProvider, ContentResolver contentResolver) {
        return new ScaleAwareTransitionProgressProvider(unfoldTransitionProgressProvider, contentResolver);
    }
}
