package com.android.systemui.power;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class EnhancedEstimatesImpl_Factory implements Factory<EnhancedEstimatesImpl> {
    @Override // javax.inject.Provider
    /* renamed from: get */
    public EnhancedEstimatesImpl mo1933get() {
        return newInstance();
    }

    public static EnhancedEstimatesImpl_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static EnhancedEstimatesImpl newInstance() {
        return new EnhancedEstimatesImpl();
    }

    /* loaded from: classes.dex */
    private static final class InstanceHolder {
        private static final EnhancedEstimatesImpl_Factory INSTANCE = new EnhancedEstimatesImpl_Factory();
    }
}
