package com.android.systemui.unfold;

import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.unfold.SysUIUnfoldComponent;
import com.android.systemui.unfold.util.NaturalRotationUnfoldProgressProvider;
import com.android.systemui.unfold.util.ScopedUnfoldTransitionProgressProvider;
import dagger.Module;
import dagger.Provides;
import java.util.Optional;
import javax.inject.Named;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002JB\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00070\u00042\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\t0\u00042\u000e\b\u0001\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u000b0\u00042\u0006\u0010\f\u001a\u00020\rH\u0007¨\u0006\u000e"}, mo65043d2 = {"Lcom/android/systemui/unfold/SysUIUnfoldModule;", "", "()V", "provideSysUIUnfoldComponent", "Ljava/util/Optional;", "Lcom/android/systemui/unfold/SysUIUnfoldComponent;", "provider", "Lcom/android/systemui/unfold/UnfoldTransitionProgressProvider;", "rotationProvider", "Lcom/android/systemui/unfold/util/NaturalRotationUnfoldProgressProvider;", "scopedProvider", "Lcom/android/systemui/unfold/util/ScopedUnfoldTransitionProgressProvider;", "factory", "Lcom/android/systemui/unfold/SysUIUnfoldComponent$Factory;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
@Module(subcomponents = {SysUIUnfoldComponent.class})
/* compiled from: SysUIUnfoldModule.kt */
public final class SysUIUnfoldModule {
    @SysUISingleton
    @Provides
    public final Optional<SysUIUnfoldComponent> provideSysUIUnfoldComponent(Optional<UnfoldTransitionProgressProvider> optional, Optional<NaturalRotationUnfoldProgressProvider> optional2, @Named("unfold_status_bar") Optional<ScopedUnfoldTransitionProgressProvider> optional3, SysUIUnfoldComponent.Factory factory) {
        Intrinsics.checkNotNullParameter(optional, "provider");
        Intrinsics.checkNotNullParameter(optional2, "rotationProvider");
        Intrinsics.checkNotNullParameter(optional3, "scopedProvider");
        Intrinsics.checkNotNullParameter(factory, "factory");
        UnfoldTransitionProgressProvider orElse = optional.orElse(null);
        NaturalRotationUnfoldProgressProvider orElse2 = optional2.orElse(null);
        ScopedUnfoldTransitionProgressProvider orElse3 = optional3.orElse(null);
        if (orElse == null || orElse2 == null || orElse3 == null) {
            Optional<SysUIUnfoldComponent> empty = Optional.empty();
            Intrinsics.checkNotNullExpressionValue(empty, "{\n            Optional.empty()\n        }");
            return empty;
        }
        Optional<SysUIUnfoldComponent> of = Optional.m1751of(factory.create(orElse, orElse2, orElse3));
        Intrinsics.checkNotNullExpressionValue(of, "{\n            Optional.o…te(p1, p2, p3))\n        }");
        return of;
    }
}
