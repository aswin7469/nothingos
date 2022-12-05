package com.android.systemui.util.wakelock;

import android.content.Context;
import com.android.systemui.util.wakelock.WakeLock;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class WakeLock_Builder_Factory implements Factory<WakeLock.Builder> {
    private final Provider<Context> contextProvider;

    public WakeLock_Builder_Factory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public WakeLock.Builder mo1933get() {
        return newInstance(this.contextProvider.mo1933get());
    }

    public static WakeLock_Builder_Factory create(Provider<Context> provider) {
        return new WakeLock_Builder_Factory(provider);
    }

    public static WakeLock.Builder newInstance(Context context) {
        return new WakeLock.Builder(context);
    }
}
