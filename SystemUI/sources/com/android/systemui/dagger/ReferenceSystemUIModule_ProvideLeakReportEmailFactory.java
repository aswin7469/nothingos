package com.android.systemui.dagger;

import dagger.internal.Factory;

public final class ReferenceSystemUIModule_ProvideLeakReportEmailFactory implements Factory<String> {
    public String get() {
        return provideLeakReportEmail();
    }

    public static ReferenceSystemUIModule_ProvideLeakReportEmailFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static String provideLeakReportEmail() {
        return ReferenceSystemUIModule.provideLeakReportEmail();
    }

    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final ReferenceSystemUIModule_ProvideLeakReportEmailFactory INSTANCE = new ReferenceSystemUIModule_ProvideLeakReportEmailFactory();

        private InstanceHolder() {
        }
    }
}
