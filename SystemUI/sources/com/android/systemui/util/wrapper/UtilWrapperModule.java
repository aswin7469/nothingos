package com.android.systemui.util.wrapper;

import com.android.systemui.dagger.SysUISingleton;
import dagger.Binds;
import dagger.Module;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b'\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H'¨\u0006\u0007"}, mo64987d2 = {"Lcom/android/systemui/util/wrapper/UtilWrapperModule;", "", "()V", "bindRotationPolicyWrapper", "Lcom/android/systemui/util/wrapper/RotationPolicyWrapper;", "impl", "Lcom/android/systemui/util/wrapper/RotationPolicyWrapperImpl;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
@Module
/* compiled from: UtilWrapperModule.kt */
public abstract class UtilWrapperModule {
    @SysUISingleton
    @Binds
    public abstract RotationPolicyWrapper bindRotationPolicyWrapper(RotationPolicyWrapperImpl rotationPolicyWrapperImpl);
}
