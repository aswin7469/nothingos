package com.android.systemui.accessibility;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class AccessibilityButtonTargetsObserver_Factory implements Factory<AccessibilityButtonTargetsObserver> {
    private final Provider<Context> contextProvider;

    public AccessibilityButtonTargetsObserver_Factory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public AccessibilityButtonTargetsObserver mo1933get() {
        return newInstance(this.contextProvider.mo1933get());
    }

    public static AccessibilityButtonTargetsObserver_Factory create(Provider<Context> provider) {
        return new AccessibilityButtonTargetsObserver_Factory(provider);
    }

    public static AccessibilityButtonTargetsObserver newInstance(Context context) {
        return new AccessibilityButtonTargetsObserver(context);
    }
}
