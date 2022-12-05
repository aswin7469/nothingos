package com.android.systemui.biometrics;
/* loaded from: classes.dex */
public abstract class NtUdfpsHbmModule {
    public static UdfpsHbmProvider optionalUdfpsHbmProvider() {
        return new NtHBMProviderImpl();
    }
}
