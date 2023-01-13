package com.android.systemui.unfold;

import android.content.Context;
import android.view.IWindowManager;
import com.android.p019wm.shell.unfold.ShellUnfoldProgressProvider;
import com.android.systemui.keyguard.LifecycleScreenStatusProvider;
import com.android.systemui.unfold.config.UnfoldTransitionConfig;
import com.android.systemui.unfold.updates.FoldStateProvider;
import com.android.systemui.unfold.updates.screen.ScreenStatusProvider;
import com.android.systemui.unfold.util.NaturalRotationUnfoldProgressProvider;
import com.android.systemui.unfold.util.ScopedUnfoldTransitionProgressProvider;
import com.android.systemui.unfold.util.UnfoldTransitionATracePrefix;
import com.android.systemui.util.time.SystemClockImpl;
import dagger.Lazy;
import dagger.Module;
import dagger.Provides;
import java.util.Optional;
import javax.inject.Named;
import javax.inject.Singleton;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000h\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J,\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0004H\u0007J\u001e\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0004H\u0007J\u001c\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00120\u00042\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004H\u0007J\u0010\u0010\u0014\u001a\u00020\u000f2\u0006\u0010\u0006\u001a\u00020\u0007H\u0007J\u001c\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00160\u00042\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00180\u0004H\u0007J$\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00180\u00042\u0006\u0010\u000e\u001a\u00020\u000f2\f\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u001c0\u001bH\u0007J\u0010\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020 H\u0007J\b\u0010!\u001a\u00020\"H\u0007¨\u0006#"}, mo65043d2 = {"Lcom/android/systemui/unfold/UnfoldTransitionModule;", "", "()V", "provideNaturalRotationProgressProvider", "Ljava/util/Optional;", "Lcom/android/systemui/unfold/util/NaturalRotationUnfoldProgressProvider;", "context", "Landroid/content/Context;", "windowManager", "Landroid/view/IWindowManager;", "unfoldTransitionProgressProvider", "Lcom/android/systemui/unfold/UnfoldTransitionProgressProvider;", "provideShellProgressProvider", "Lcom/android/wm/shell/unfold/ShellUnfoldProgressProvider;", "config", "Lcom/android/systemui/unfold/config/UnfoldTransitionConfig;", "provider", "provideStatusBarScopedTransitionProvider", "Lcom/android/systemui/unfold/util/ScopedUnfoldTransitionProgressProvider;", "source", "provideUnfoldTransitionConfig", "providesFoldStateLogger", "Lcom/android/systemui/unfold/FoldStateLogger;", "optionalFoldStateLoggingProvider", "Lcom/android/systemui/unfold/FoldStateLoggingProvider;", "providesFoldStateLoggingProvider", "foldStateProvider", "Ldagger/Lazy;", "Lcom/android/systemui/unfold/updates/FoldStateProvider;", "screenStatusProvider", "Lcom/android/systemui/unfold/updates/screen/ScreenStatusProvider;", "impl", "Lcom/android/systemui/keyguard/LifecycleScreenStatusProvider;", "tracingTagPrefix", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
@Module(includes = {UnfoldSharedModule.class})
/* compiled from: UnfoldTransitionModule.kt */
public final class UnfoldTransitionModule {
    @UnfoldTransitionATracePrefix
    @Provides
    public final String tracingTagPrefix() {
        return "systemui";
    }

    @Singleton
    @Provides
    public final Optional<FoldStateLoggingProvider> providesFoldStateLoggingProvider(UnfoldTransitionConfig unfoldTransitionConfig, Lazy<FoldStateProvider> lazy) {
        Intrinsics.checkNotNullParameter(unfoldTransitionConfig, "config");
        Intrinsics.checkNotNullParameter(lazy, "foldStateProvider");
        if (unfoldTransitionConfig.isHingeAngleEnabled()) {
            FoldStateProvider foldStateProvider = lazy.get();
            Intrinsics.checkNotNullExpressionValue(foldStateProvider, "foldStateProvider.get()");
            Optional<FoldStateLoggingProvider> of = Optional.m1751of(new FoldStateLoggingProviderImpl(foldStateProvider, new SystemClockImpl()));
            Intrinsics.checkNotNullExpressionValue(of, "{\n            Optional.o…emClockImpl()))\n        }");
            return of;
        }
        Optional<FoldStateLoggingProvider> empty = Optional.empty();
        Intrinsics.checkNotNullExpressionValue(empty, "{\n            Optional.empty()\n        }");
        return empty;
    }

    @Singleton
    @Provides
    public final Optional<FoldStateLogger> providesFoldStateLogger(Optional<FoldStateLoggingProvider> optional) {
        Intrinsics.checkNotNullParameter(optional, "optionalFoldStateLoggingProvider");
        Optional<U> map = optional.map(new UnfoldTransitionModule$$ExternalSyntheticLambda0());
        Intrinsics.checkNotNullExpressionValue(map, "optionalFoldStateLogging…oggingProvider)\n        }");
        return map;
    }

    /* access modifiers changed from: private */
    /* renamed from: providesFoldStateLogger$lambda-0  reason: not valid java name */
    public static final FoldStateLogger m3290providesFoldStateLogger$lambda0(FoldStateLoggingProvider foldStateLoggingProvider) {
        Intrinsics.checkNotNullExpressionValue(foldStateLoggingProvider, "FoldStateLoggingProvider");
        return new FoldStateLogger(foldStateLoggingProvider);
    }

    @Singleton
    @Provides
    public final UnfoldTransitionConfig provideUnfoldTransitionConfig(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        return UnfoldTransitionFactory.createConfig(context);
    }

    @Singleton
    @Provides
    public final Optional<NaturalRotationUnfoldProgressProvider> provideNaturalRotationProgressProvider(Context context, IWindowManager iWindowManager, Optional<UnfoldTransitionProgressProvider> optional) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(iWindowManager, "windowManager");
        Intrinsics.checkNotNullParameter(optional, "unfoldTransitionProgressProvider");
        Optional<U> map = optional.map(new UnfoldTransitionModule$$ExternalSyntheticLambda1(context, iWindowManager));
        Intrinsics.checkNotNullExpressionValue(map, "unfoldTransitionProgress…ager, provider)\n        }");
        return map;
    }

    /* access modifiers changed from: private */
    /* renamed from: provideNaturalRotationProgressProvider$lambda-1  reason: not valid java name */
    public static final NaturalRotationUnfoldProgressProvider m3288provideNaturalRotationProgressProvider$lambda1(Context context, IWindowManager iWindowManager, UnfoldTransitionProgressProvider unfoldTransitionProgressProvider) {
        Intrinsics.checkNotNullParameter(context, "$context");
        Intrinsics.checkNotNullParameter(iWindowManager, "$windowManager");
        Intrinsics.checkNotNullExpressionValue(unfoldTransitionProgressProvider, "provider");
        return new NaturalRotationUnfoldProgressProvider(context, iWindowManager, unfoldTransitionProgressProvider);
    }

    /* access modifiers changed from: private */
    /* renamed from: provideStatusBarScopedTransitionProvider$lambda-2  reason: not valid java name */
    public static final ScopedUnfoldTransitionProgressProvider m3289provideStatusBarScopedTransitionProvider$lambda2(NaturalRotationUnfoldProgressProvider naturalRotationUnfoldProgressProvider) {
        return new ScopedUnfoldTransitionProgressProvider(naturalRotationUnfoldProgressProvider);
    }

    @Named("unfold_status_bar")
    @Singleton
    @Provides
    public final Optional<ScopedUnfoldTransitionProgressProvider> provideStatusBarScopedTransitionProvider(Optional<NaturalRotationUnfoldProgressProvider> optional) {
        Intrinsics.checkNotNullParameter(optional, "source");
        Optional<U> map = optional.map(new UnfoldTransitionModule$$ExternalSyntheticLambda2());
        Intrinsics.checkNotNullExpressionValue(map, "source.map { provider ->…gressProvider(provider) }");
        return map;
    }

    @Singleton
    @Provides
    public final ShellUnfoldProgressProvider provideShellProgressProvider(UnfoldTransitionConfig unfoldTransitionConfig, Optional<UnfoldTransitionProgressProvider> optional) {
        Intrinsics.checkNotNullParameter(unfoldTransitionConfig, "config");
        Intrinsics.checkNotNullParameter(optional, "provider");
        if (!unfoldTransitionConfig.isEnabled() || !optional.isPresent()) {
            ShellUnfoldProgressProvider shellUnfoldProgressProvider = ShellUnfoldProgressProvider.NO_PROVIDER;
            Intrinsics.checkNotNullExpressionValue(shellUnfoldProgressProvider, "{\n            ShellUnfol…der.NO_PROVIDER\n        }");
            return shellUnfoldProgressProvider;
        }
        UnfoldTransitionProgressProvider unfoldTransitionProgressProvider = optional.get();
        Intrinsics.checkNotNullExpressionValue(unfoldTransitionProgressProvider, "provider.get()");
        return new UnfoldProgressProvider(unfoldTransitionProgressProvider);
    }

    @Provides
    public final ScreenStatusProvider screenStatusProvider(LifecycleScreenStatusProvider lifecycleScreenStatusProvider) {
        Intrinsics.checkNotNullParameter(lifecycleScreenStatusProvider, "impl");
        return lifecycleScreenStatusProvider;
    }
}
