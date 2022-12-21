package com.android.systemui.statusbar;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.MathUtils;
import android.view.animation.PathInterpolator;
import com.android.systemui.C1893R;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.plugins.p011qs.C2301QS;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.phone.ScrimController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import dagger.assisted.Assisted;
import dagger.assisted.AssistedFactory;
import dagger.assisted.AssistedInject;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000^\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0005\u0018\u0000 *2\u00020\u0001:\u0002*+B;\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\b\b\u0001\u0010\n\u001a\u00020\u000b\u0012\b\b\u0001\u0010\f\u001a\u00020\r¢\u0006\u0002\u0010\u000eJ\u0010\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020\u0017H\u0002J\u0010\u0010!\u001a\u00020\u00172\u0006\u0010\u000f\u001a\u00020\u0010H\u0002J\r\u0010\"\u001a\u00020\u001fH\u0001¢\u0006\u0002\b#J\u0010\u0010$\u001a\u00020\u001f2\u0006\u0010\u000f\u001a\u00020\u0010H\u0002J\b\u0010%\u001a\u00020\u001fH\u0002J\b\u0010&\u001a\u00020'H\u0002J\b\u0010(\u001a\u00020'H\u0002J\b\u0010)\u001a\u00020\u001fH\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R$\u0010\u0011\u001a\u00020\u00102\u0006\u0010\u000f\u001a\u00020\u0010@VX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u0013\"\u0004\b\u0014\u0010\u0015R\u000e\u0010\u0016\u001a\u00020\u0017X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0017X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0019\u001a\u0004\u0018\u00010\u001aX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u001cX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u0017X\u000e¢\u0006\u0002\n\u0000¨\u0006,"}, mo64987d2 = {"Lcom/android/systemui/statusbar/SplitShadeLockScreenOverScroller;", "Lcom/android/systemui/statusbar/LockScreenShadeOverScroller;", "configurationController", "Lcom/android/systemui/statusbar/policy/ConfigurationController;", "context", "Landroid/content/Context;", "scrimController", "Lcom/android/systemui/statusbar/phone/ScrimController;", "statusBarStateController", "Lcom/android/systemui/statusbar/SysuiStatusBarStateController;", "qS", "Lcom/android/systemui/plugins/qs/QS;", "nsslController", "Lcom/android/systemui/statusbar/notification/stack/NotificationStackScrollLayoutController;", "(Lcom/android/systemui/statusbar/policy/ConfigurationController;Landroid/content/Context;Lcom/android/systemui/statusbar/phone/ScrimController;Lcom/android/systemui/statusbar/SysuiStatusBarStateController;Lcom/android/systemui/plugins/qs/QS;Lcom/android/systemui/statusbar/notification/stack/NotificationStackScrollLayoutController;)V", "dragDownAmount", "", "expansionDragDownAmount", "getExpansionDragDownAmount", "()F", "setExpansionDragDownAmount", "(F)V", "maxOverScrollAmount", "", "previousOverscrollAmount", "releaseOverScrollAnimator", "Landroid/animation/Animator;", "releaseOverScrollDuration", "", "transitionToFullShadeDistance", "applyOverscroll", "", "overscrollAmount", "calculateOverscrollAmount", "finishAnimations", "finishAnimations$SystemUI_nothingRelease", "overScroll", "releaseOverScroll", "shouldOverscroll", "", "shouldReleaseOverscroll", "updateResources", "Companion", "Factory", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: SplitShadeLockScreenOverScroller.kt */
public final class SplitShadeLockScreenOverScroller implements LockScreenShadeOverScroller {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final float OVER_SHOOT_AMOUNT = 0.6f;
    private static final PathInterpolator RELEASE_OVER_SCROLL_INTERPOLATOR = new PathInterpolator(0.17f, 0.0f, 0.0f, 1.0f);
    private final Context context;
    private float expansionDragDownAmount;
    private int maxOverScrollAmount;
    private final NotificationStackScrollLayoutController nsslController;
    private int previousOverscrollAmount;

    /* renamed from: qS */
    private final C2301QS f370qS;
    private Animator releaseOverScrollAnimator;
    private long releaseOverScrollDuration;
    private final ScrimController scrimController;
    private final SysuiStatusBarStateController statusBarStateController;
    private int transitionToFullShadeDistance;

    @AssistedFactory
    @Metadata(mo64986d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bç\u0001\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\bÀ\u0006\u0001"}, mo64987d2 = {"Lcom/android/systemui/statusbar/SplitShadeLockScreenOverScroller$Factory;", "", "create", "Lcom/android/systemui/statusbar/SplitShadeLockScreenOverScroller;", "qS", "Lcom/android/systemui/plugins/qs/QS;", "nsslController", "Lcom/android/systemui/statusbar/notification/stack/NotificationStackScrollLayoutController;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: SplitShadeLockScreenOverScroller.kt */
    public interface Factory {
        SplitShadeLockScreenOverScroller create(C2301QS qs, NotificationStackScrollLayoutController notificationStackScrollLayoutController);
    }

    @AssistedInject
    public SplitShadeLockScreenOverScroller(ConfigurationController configurationController, Context context2, ScrimController scrimController2, SysuiStatusBarStateController sysuiStatusBarStateController, @Assisted C2301QS qs, @Assisted NotificationStackScrollLayoutController notificationStackScrollLayoutController) {
        Intrinsics.checkNotNullParameter(configurationController, "configurationController");
        Intrinsics.checkNotNullParameter(context2, "context");
        Intrinsics.checkNotNullParameter(scrimController2, "scrimController");
        Intrinsics.checkNotNullParameter(sysuiStatusBarStateController, "statusBarStateController");
        Intrinsics.checkNotNullParameter(qs, "qS");
        Intrinsics.checkNotNullParameter(notificationStackScrollLayoutController, "nsslController");
        this.context = context2;
        this.scrimController = scrimController2;
        this.statusBarStateController = sysuiStatusBarStateController;
        this.f370qS = qs;
        this.nsslController = notificationStackScrollLayoutController;
        updateResources();
        configurationController.addCallback(new ConfigurationController.ConfigurationListener(this) {
            final /* synthetic */ SplitShadeLockScreenOverScroller this$0;

            {
                this.this$0 = r1;
            }

            public void onConfigChanged(Configuration configuration) {
                this.this$0.updateResources();
            }
        });
    }

    /* access modifiers changed from: private */
    public final void updateResources() {
        Resources resources = this.context.getResources();
        this.transitionToFullShadeDistance = resources.getDimensionPixelSize(C1893R.dimen.lockscreen_shade_full_transition_distance);
        this.maxOverScrollAmount = resources.getDimensionPixelSize(C1893R.dimen.lockscreen_shade_max_over_scroll_amount);
        this.releaseOverScrollDuration = (long) resources.getInteger(C1893R.integer.lockscreen_shade_over_scroll_release_duration);
    }

    public float getExpansionDragDownAmount() {
        return this.expansionDragDownAmount;
    }

    public void setExpansionDragDownAmount(float f) {
        if (!(this.expansionDragDownAmount == f)) {
            this.expansionDragDownAmount = f;
            if (shouldOverscroll()) {
                overScroll(f);
            } else if (shouldReleaseOverscroll()) {
                releaseOverScroll();
            }
        }
    }

    private final boolean shouldOverscroll() {
        return this.statusBarStateController.getState() == 1;
    }

    private final boolean shouldReleaseOverscroll() {
        return !shouldOverscroll() && this.previousOverscrollAmount != 0;
    }

    private final void overScroll(float f) {
        int calculateOverscrollAmount = calculateOverscrollAmount(f);
        applyOverscroll(calculateOverscrollAmount);
        this.previousOverscrollAmount = calculateOverscrollAmount;
    }

    private final void applyOverscroll(int i) {
        this.f370qS.setOverScrollAmount(i);
        this.scrimController.setNotificationsOverScrollAmount(i);
        this.nsslController.setOverScrollAmount(i);
    }

    private final int calculateOverscrollAmount(float f) {
        float height = (float) this.nsslController.getHeight();
        return (int) (Interpolators.getOvershootInterpolation(MathUtils.saturate(f / height), 0.6f, ((float) this.transitionToFullShadeDistance) / height) * ((float) this.maxOverScrollAmount));
    }

    private final void releaseOverScroll() {
        ValueAnimator ofInt = ValueAnimator.ofInt(new int[]{this.previousOverscrollAmount, 0});
        ofInt.addUpdateListener(new SplitShadeLockScreenOverScroller$$ExternalSyntheticLambda0(this));
        ofInt.setInterpolator(RELEASE_OVER_SCROLL_INTERPOLATOR);
        ofInt.setDuration(this.releaseOverScrollDuration);
        ofInt.start();
        this.releaseOverScrollAnimator = ofInt;
        this.previousOverscrollAmount = 0;
    }

    /* access modifiers changed from: private */
    /* renamed from: releaseOverScroll$lambda-0  reason: not valid java name */
    public static final void m3041releaseOverScroll$lambda0(SplitShadeLockScreenOverScroller splitShadeLockScreenOverScroller, ValueAnimator valueAnimator) {
        Intrinsics.checkNotNullParameter(splitShadeLockScreenOverScroller, "this$0");
        Object animatedValue = valueAnimator.getAnimatedValue();
        if (animatedValue != null) {
            int intValue = ((Integer) animatedValue).intValue();
            splitShadeLockScreenOverScroller.f370qS.setOverScrollAmount(intValue);
            splitShadeLockScreenOverScroller.scrimController.setNotificationsOverScrollAmount(intValue);
            splitShadeLockScreenOverScroller.nsslController.setOverScrollAmount(intValue);
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Int");
    }

    public final void finishAnimations$SystemUI_nothingRelease() {
        Animator animator = this.releaseOverScrollAnimator;
        if (animator != null) {
            animator.end();
        }
        this.releaseOverScrollAnimator = null;
    }

    @Metadata(mo64986d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0007"}, mo64987d2 = {"Lcom/android/systemui/statusbar/SplitShadeLockScreenOverScroller$Companion;", "", "()V", "OVER_SHOOT_AMOUNT", "", "RELEASE_OVER_SCROLL_INTERPOLATOR", "Landroid/view/animation/PathInterpolator;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: SplitShadeLockScreenOverScroller.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
