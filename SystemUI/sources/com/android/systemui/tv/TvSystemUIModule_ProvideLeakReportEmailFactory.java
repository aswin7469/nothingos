package com.android.systemui.tv;

import dagger.internal.Factory;
/* loaded from: classes2.dex */
public final class TvSystemUIModule_ProvideLeakReportEmailFactory implements Factory<String> {
    @Override // javax.inject.Provider
    /* renamed from: get  reason: collision with other method in class */
    public String mo1933get() {
        return provideLeakReportEmail();
    }

    public static TvSystemUIModule_ProvideLeakReportEmailFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static String provideLeakReportEmail() {
        return TvSystemUIModule.provideLeakReportEmail();
    }

    /* loaded from: classes2.dex */
    private static final class InstanceHolder {
        private static final TvSystemUIModule_ProvideLeakReportEmailFactory INSTANCE = new TvSystemUIModule_ProvideLeakReportEmailFactory();
    }
}
