package com.android.systemui.util.wakelock;

import android.content.Context;
import com.android.systemui.util.wakelock.DelayedWakeLock;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class DelayedWakeLock_Builder_Factory implements Factory<DelayedWakeLock.Builder> {
    private final Provider<Context> contextProvider;

    public DelayedWakeLock_Builder_Factory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public DelayedWakeLock.Builder mo1933get() {
        return newInstance(this.contextProvider.mo1933get());
    }

    public static DelayedWakeLock_Builder_Factory create(Provider<Context> provider) {
        return new DelayedWakeLock_Builder_Factory(provider);
    }

    public static DelayedWakeLock.Builder newInstance(Context context) {
        return new DelayedWakeLock.Builder(context);
    }
}
