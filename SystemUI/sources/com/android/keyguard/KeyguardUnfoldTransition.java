package com.android.keyguard;

import android.content.Context;
import android.view.ViewGroup;
import com.android.systemui.C1894R;
import com.android.systemui.shared.animation.UnfoldConstantTranslateAnimator;
import com.android.systemui.unfold.SysUIUnfoldScope;
import com.android.systemui.unfold.util.NaturalRotationUnfoldProgressProvider;
import javax.inject.Inject;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u000e\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0019R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u000b\u001a\u00020\tX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR\u001b\u0010\u0010\u001a\u00020\u00118BX\u0002¢\u0006\f\n\u0004\b\u0014\u0010\u0015\u001a\u0004\b\u0012\u0010\u0013¨\u0006\u001a"}, mo65043d2 = {"Lcom/android/keyguard/KeyguardUnfoldTransition;", "", "context", "Landroid/content/Context;", "unfoldProgressProvider", "Lcom/android/systemui/unfold/util/NaturalRotationUnfoldProgressProvider;", "(Landroid/content/Context;Lcom/android/systemui/unfold/util/NaturalRotationUnfoldProgressProvider;)V", "filterNever", "Lkotlin/Function0;", "", "filterSplitShadeOnly", "statusViewCentered", "getStatusViewCentered", "()Z", "setStatusViewCentered", "(Z)V", "translateAnimator", "Lcom/android/systemui/shared/animation/UnfoldConstantTranslateAnimator;", "getTranslateAnimator", "()Lcom/android/systemui/shared/animation/UnfoldConstantTranslateAnimator;", "translateAnimator$delegate", "Lkotlin/Lazy;", "setup", "", "parent", "Landroid/view/ViewGroup;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
@SysUIUnfoldScope
/* compiled from: KeyguardUnfoldTransition.kt */
public final class KeyguardUnfoldTransition {
    private final Context context;
    /* access modifiers changed from: private */
    public final Function0<Boolean> filterNever = KeyguardUnfoldTransition$filterNever$1.INSTANCE;
    /* access modifiers changed from: private */
    public final Function0<Boolean> filterSplitShadeOnly = new KeyguardUnfoldTransition$filterSplitShadeOnly$1(this);
    private boolean statusViewCentered;
    private final Lazy translateAnimator$delegate;

    @Inject
    public KeyguardUnfoldTransition(Context context2, NaturalRotationUnfoldProgressProvider naturalRotationUnfoldProgressProvider) {
        Intrinsics.checkNotNullParameter(context2, "context");
        Intrinsics.checkNotNullParameter(naturalRotationUnfoldProgressProvider, "unfoldProgressProvider");
        this.context = context2;
        this.translateAnimator$delegate = LazyKt.lazy(new KeyguardUnfoldTransition$translateAnimator$2(this, naturalRotationUnfoldProgressProvider));
    }

    public final boolean getStatusViewCentered() {
        return this.statusViewCentered;
    }

    public final void setStatusViewCentered(boolean z) {
        this.statusViewCentered = z;
    }

    private final UnfoldConstantTranslateAnimator getTranslateAnimator() {
        return (UnfoldConstantTranslateAnimator) this.translateAnimator$delegate.getValue();
    }

    public final void setup(ViewGroup viewGroup) {
        Intrinsics.checkNotNullParameter(viewGroup, "parent");
        getTranslateAnimator().init(viewGroup, (float) this.context.getResources().getDimensionPixelSize(C1894R.dimen.keyguard_unfold_translation_x));
    }
}
