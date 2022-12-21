package com.android.systemui.p014tv;

import dagger.internal.Factory;

/* renamed from: com.android.systemui.tv.TvSystemUIModule_ProvideLeakReportEmailFactory */
public final class TvSystemUIModule_ProvideLeakReportEmailFactory implements Factory<String> {
    public String get() {
        return provideLeakReportEmail();
    }

    public static TvSystemUIModule_ProvideLeakReportEmailFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static String provideLeakReportEmail() {
        return TvSystemUIModule.provideLeakReportEmail();
    }

    /* renamed from: com.android.systemui.tv.TvSystemUIModule_ProvideLeakReportEmailFactory$InstanceHolder */
    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final TvSystemUIModule_ProvideLeakReportEmailFactory INSTANCE = new TvSystemUIModule_ProvideLeakReportEmailFactory();

        private InstanceHolder() {
        }
    }
}
