package com.android.systemui.statusbar.policy;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class AccessibilityController_Factory implements Factory<AccessibilityController> {
    private final Provider<Context> contextProvider;

    public AccessibilityController_Factory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public AccessibilityController mo1933get() {
        return newInstance(this.contextProvider.mo1933get());
    }

    public static AccessibilityController_Factory create(Provider<Context> provider) {
        return new AccessibilityController_Factory(provider);
    }

    public static AccessibilityController newInstance(Context context) {
        return new AccessibilityController(context);
    }
}
