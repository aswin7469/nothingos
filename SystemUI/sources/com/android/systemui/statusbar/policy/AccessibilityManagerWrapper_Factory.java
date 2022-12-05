package com.android.systemui.statusbar.policy;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class AccessibilityManagerWrapper_Factory implements Factory<AccessibilityManagerWrapper> {
    private final Provider<Context> contextProvider;

    public AccessibilityManagerWrapper_Factory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public AccessibilityManagerWrapper mo1933get() {
        return newInstance(this.contextProvider.mo1933get());
    }

    public static AccessibilityManagerWrapper_Factory create(Provider<Context> provider) {
        return new AccessibilityManagerWrapper_Factory(provider);
    }

    public static AccessibilityManagerWrapper newInstance(Context context) {
        return new AccessibilityManagerWrapper(context);
    }
}
