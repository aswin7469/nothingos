package com.android.systemui.controls;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class ControlsMetricsLoggerImpl_Factory implements Factory<ControlsMetricsLoggerImpl> {
    @Override // javax.inject.Provider
    /* renamed from: get */
    public ControlsMetricsLoggerImpl mo1933get() {
        return newInstance();
    }

    public static ControlsMetricsLoggerImpl_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static ControlsMetricsLoggerImpl newInstance() {
        return new ControlsMetricsLoggerImpl();
    }

    /* loaded from: classes.dex */
    private static final class InstanceHolder {
        private static final ControlsMetricsLoggerImpl_Factory INSTANCE = new ControlsMetricsLoggerImpl_Factory();
    }
}
