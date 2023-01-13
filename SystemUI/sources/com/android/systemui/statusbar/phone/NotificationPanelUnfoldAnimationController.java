package com.android.systemui.statusbar.phone;

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
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u000e\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u001b\u0010\u0007\u001a\u00020\b8BX\u0002¢\u0006\f\n\u0004\b\u000b\u0010\f\u001a\u0004\b\t\u0010\n¨\u0006\u0011"}, mo65043d2 = {"Lcom/android/systemui/statusbar/phone/NotificationPanelUnfoldAnimationController;", "", "context", "Landroid/content/Context;", "progressProvider", "Lcom/android/systemui/unfold/util/NaturalRotationUnfoldProgressProvider;", "(Landroid/content/Context;Lcom/android/systemui/unfold/util/NaturalRotationUnfoldProgressProvider;)V", "translateAnimator", "Lcom/android/systemui/shared/animation/UnfoldConstantTranslateAnimator;", "getTranslateAnimator", "()Lcom/android/systemui/shared/animation/UnfoldConstantTranslateAnimator;", "translateAnimator$delegate", "Lkotlin/Lazy;", "setup", "", "root", "Landroid/view/ViewGroup;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
@SysUIUnfoldScope
/* compiled from: NotificationPanelUnfoldAnimationController.kt */
public final class NotificationPanelUnfoldAnimationController {
    private final Context context;
    private final Lazy translateAnimator$delegate;

    @Inject
    public NotificationPanelUnfoldAnimationController(Context context2, NaturalRotationUnfoldProgressProvider naturalRotationUnfoldProgressProvider) {
        Intrinsics.checkNotNullParameter(context2, "context");
        Intrinsics.checkNotNullParameter(naturalRotationUnfoldProgressProvider, "progressProvider");
        this.context = context2;
        this.translateAnimator$delegate = LazyKt.lazy(new NotificationPanelUnfoldAnimationController$translateAnimator$2(naturalRotationUnfoldProgressProvider));
    }

    private final UnfoldConstantTranslateAnimator getTranslateAnimator() {
        return (UnfoldConstantTranslateAnimator) this.translateAnimator$delegate.getValue();
    }

    public final void setup(ViewGroup viewGroup) {
        Intrinsics.checkNotNullParameter(viewGroup, "root");
        getTranslateAnimator().init(viewGroup, (float) this.context.getResources().getDimensionPixelSize(C1894R.dimen.notification_side_paddings));
    }
}
