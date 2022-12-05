package com.android.systemui.recents;

import android.content.Context;
import com.android.systemui.dagger.ContextComponentHelper;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class RecentsModule_ProvideRecentsImplFactory implements Factory<RecentsImplementation> {
    private final Provider<ContextComponentHelper> componentHelperProvider;
    private final Provider<Context> contextProvider;

    public RecentsModule_ProvideRecentsImplFactory(Provider<Context> provider, Provider<ContextComponentHelper> provider2) {
        this.contextProvider = provider;
        this.componentHelperProvider = provider2;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public RecentsImplementation mo1933get() {
        return provideRecentsImpl(this.contextProvider.mo1933get(), this.componentHelperProvider.mo1933get());
    }

    public static RecentsModule_ProvideRecentsImplFactory create(Provider<Context> provider, Provider<ContextComponentHelper> provider2) {
        return new RecentsModule_ProvideRecentsImplFactory(provider, provider2);
    }

    public static RecentsImplementation provideRecentsImpl(Context context, ContextComponentHelper contextComponentHelper) {
        return (RecentsImplementation) Preconditions.checkNotNullFromProvides(RecentsModule.provideRecentsImpl(context, contextComponentHelper));
    }
}
