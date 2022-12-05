package com.android.systemui.accessibility;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class AccessibilityButtonModeObserver_Factory implements Factory<AccessibilityButtonModeObserver> {
    private final Provider<Context> contextProvider;

    public AccessibilityButtonModeObserver_Factory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public AccessibilityButtonModeObserver mo1933get() {
        return newInstance(this.contextProvider.mo1933get());
    }

    public static AccessibilityButtonModeObserver_Factory create(Provider<Context> provider) {
        return new AccessibilityButtonModeObserver_Factory(provider);
    }

    public static AccessibilityButtonModeObserver newInstance(Context context) {
        return new AccessibilityButtonModeObserver(context);
    }
}
