package com.android.systemui.tracing;

import android.content.Context;
import com.android.systemui.dump.DumpManager;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class ProtoTracer_Factory implements Factory<ProtoTracer> {
    private final Provider<Context> contextProvider;
    private final Provider<DumpManager> dumpManagerProvider;

    public ProtoTracer_Factory(Provider<Context> provider, Provider<DumpManager> provider2) {
        this.contextProvider = provider;
        this.dumpManagerProvider = provider2;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public ProtoTracer mo1933get() {
        return newInstance(this.contextProvider.mo1933get(), this.dumpManagerProvider.mo1933get());
    }

    public static ProtoTracer_Factory create(Provider<Context> provider, Provider<DumpManager> provider2) {
        return new ProtoTracer_Factory(provider, provider2);
    }

    public static ProtoTracer newInstance(Context context, DumpManager dumpManager) {
        return new ProtoTracer(context, dumpManager);
    }
}
