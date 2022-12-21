package com.android.systemui.unfold;

import com.android.p019wm.shell.unfold.ShellUnfoldProgressProvider;
import java.util.concurrent.Executor;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0018\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\u000b"}, mo64987d2 = {"Lcom/android/systemui/unfold/UnfoldProgressProvider;", "Lcom/android/wm/shell/unfold/ShellUnfoldProgressProvider;", "unfoldProgressProvider", "Lcom/android/systemui/unfold/UnfoldTransitionProgressProvider;", "(Lcom/android/systemui/unfold/UnfoldTransitionProgressProvider;)V", "addListener", "", "executor", "Ljava/util/concurrent/Executor;", "listener", "Lcom/android/wm/shell/unfold/ShellUnfoldProgressProvider$UnfoldListener;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: UnfoldProgressProvider.kt */
public final class UnfoldProgressProvider implements ShellUnfoldProgressProvider {
    private final UnfoldTransitionProgressProvider unfoldProgressProvider;

    public UnfoldProgressProvider(UnfoldTransitionProgressProvider unfoldTransitionProgressProvider) {
        Intrinsics.checkNotNullParameter(unfoldTransitionProgressProvider, "unfoldProgressProvider");
        this.unfoldProgressProvider = unfoldTransitionProgressProvider;
    }

    public void addListener(Executor executor, ShellUnfoldProgressProvider.UnfoldListener unfoldListener) {
        Intrinsics.checkNotNullParameter(executor, "executor");
        Intrinsics.checkNotNullParameter(unfoldListener, "listener");
        this.unfoldProgressProvider.addCallback(new UnfoldProgressProvider$addListener$1(executor, unfoldListener));
    }
}
