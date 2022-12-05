package com.android.systemui.statusbar.policy;

import android.content.Context;
import com.android.systemui.dump.DumpManager;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class CastControllerImpl_Factory implements Factory<CastControllerImpl> {
    private final Provider<Context> contextProvider;
    private final Provider<DumpManager> dumpManagerProvider;

    public CastControllerImpl_Factory(Provider<Context> provider, Provider<DumpManager> provider2) {
        this.contextProvider = provider;
        this.dumpManagerProvider = provider2;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public CastControllerImpl mo1933get() {
        return newInstance(this.contextProvider.mo1933get(), this.dumpManagerProvider.mo1933get());
    }

    public static CastControllerImpl_Factory create(Provider<Context> provider, Provider<DumpManager> provider2) {
        return new CastControllerImpl_Factory(provider, provider2);
    }

    public static CastControllerImpl newInstance(Context context, DumpManager dumpManager) {
        return new CastControllerImpl(context, dumpManager);
    }
}
