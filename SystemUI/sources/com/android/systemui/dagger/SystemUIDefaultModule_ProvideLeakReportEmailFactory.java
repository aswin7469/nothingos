package com.android.systemui.dagger;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class SystemUIDefaultModule_ProvideLeakReportEmailFactory implements Factory<String> {
    @Override // javax.inject.Provider
    /* renamed from: get  reason: collision with other method in class */
    public String mo1933get() {
        return provideLeakReportEmail();
    }

    public static SystemUIDefaultModule_ProvideLeakReportEmailFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static String provideLeakReportEmail() {
        return SystemUIDefaultModule.provideLeakReportEmail();
    }

    /* loaded from: classes.dex */
    private static final class InstanceHolder {
        private static final SystemUIDefaultModule_ProvideLeakReportEmailFactory INSTANCE = new SystemUIDefaultModule_ProvideLeakReportEmailFactory();
    }
}
