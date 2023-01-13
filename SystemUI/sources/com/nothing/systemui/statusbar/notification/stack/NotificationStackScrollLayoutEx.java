package com.nothing.systemui.statusbar.notification.stack;

import android.animation.ValueAnimator;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.WindowInsets;
import com.android.systemui.Dependency;
import com.android.systemui.statusbar.notification.stack.AmbientState;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout;
import com.nothing.systemui.NTDependencyEx;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000d\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0016\u0018\u00002\u00020\u0001B\u000f\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0016\u0010&\u001a\u00020\u000e2\u0006\u0010'\u001a\u00020(2\u0006\u0010)\u001a\u00020*J\b\u0010+\u001a\u00020\u000eH\u0016J\u000e\u0010,\u001a\u00020-2\u0006\u0010.\u001a\u00020/J\b\u00100\u001a\u00020-H\u0016J\u0010\u00101\u001a\u00020-2\u0006\u0010'\u001a\u00020(H\u0016J\b\u00102\u001a\u00020-H\u0016J\u0010\u00103\u001a\u00020-2\u0006\u00104\u001a\u00020\u001fH\u0016J\u0010\u00105\u001a\u00020-2\u0006\u00106\u001a\u00020\u001fH\u0016J\u0010\u00107\u001a\u00020\u000e2\u0006\u00108\u001a\u000209H\u0016J\u0010\u0010:\u001a\u00020-2\u0006\u0010;\u001a\u00020\u001fH\u0016R\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\t\u001a\u00020\n¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u001a\u0010\r\u001a\u00020\u000eX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012R\u001a\u0010\u0013\u001a\u00020\u000eX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0010\"\u0004\b\u0014\u0010\u0012R\u0011\u0010\u0015\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u001c\u0010\u0018\u001a\u0004\u0018\u00010\u0019X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u001b\"\u0004\b\u001c\u0010\u001dR\u000e\u0010\u001e\u001a\u00020\u001fX\u000e¢\u0006\u0002\n\u0000R\u001a\u0010 \u001a\u00020!X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\"\u0010#\"\u0004\b$\u0010%¨\u0006<"}, mo65043d2 = {"Lcom/nothing/systemui/statusbar/notification/stack/NotificationStackScrollLayoutEx;", "", "notificationStackSCrollLayout", "Lcom/android/systemui/statusbar/notification/stack/NotificationStackScrollLayout;", "(Lcom/android/systemui/statusbar/notification/stack/NotificationStackScrollLayout;)V", "ambientState", "Lcom/android/systemui/statusbar/notification/stack/AmbientState;", "getAmbientState", "()Lcom/android/systemui/statusbar/notification/stack/AmbientState;", "ambientStateEx", "Lcom/nothing/systemui/statusbar/notification/stack/AmbientStateEx;", "getAmbientStateEx", "()Lcom/nothing/systemui/statusbar/notification/stack/AmbientStateEx;", "canQSCollapse", "", "getCanQSCollapse", "()Z", "setCanQSCollapse", "(Z)V", "isTracking", "setTracking", "layout", "getLayout", "()Lcom/android/systemui/statusbar/notification/stack/NotificationStackScrollLayout;", "resetScrollYAnimator", "Landroid/animation/ValueAnimator;", "getResetScrollYAnimator", "()Landroid/animation/ValueAnimator;", "setResetScrollYAnimator", "(Landroid/animation/ValueAnimator;)V", "stackAlpha", "", "topInset", "", "getTopInset", "()I", "setTopInset", "(I)V", "isInsideQsHeader", "ev", "Landroid/view/MotionEvent;", "qsHeaderBound", "Landroid/graphics/Rect;", "keepExpand", "onApplyWindowInsets", "", "insets", "Landroid/view/WindowInsets;", "onPanelTrackingStarted", "onTouchEvent", "resetScrollAnimation", "setQsExpansionFraction", "qsExpansionFraction", "setStackAlpha", "alpha", "shouldInterceptIsInsideQsHeader", "notificationWindow", "Landroid/view/View;", "updateExpansionFraction", "expansionFraction", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: NotificationStackScrollLayoutEx.kt */
public class NotificationStackScrollLayoutEx {
    private final AmbientState ambientState;
    private final AmbientStateEx ambientStateEx;
    private boolean canQSCollapse;
    private boolean isTracking;
    private final NotificationStackScrollLayout layout;
    private ValueAnimator resetScrollYAnimator;
    private float stackAlpha = -1.0f;
    private int topInset;

    public final NotificationStackScrollLayout getLayout() {
        return this.layout;
    }

    public final boolean isTracking() {
        return this.isTracking;
    }

    public final void setTracking(boolean z) {
        this.isTracking = z;
    }

    public final boolean getCanQSCollapse() {
        return this.canQSCollapse;
    }

    public final void setCanQSCollapse(boolean z) {
        this.canQSCollapse = z;
    }

    public final AmbientState getAmbientState() {
        return this.ambientState;
    }

    public final AmbientStateEx getAmbientStateEx() {
        return this.ambientStateEx;
    }

    public final int getTopInset() {
        return this.topInset;
    }

    public final void setTopInset(int i) {
        this.topInset = i;
    }

    public final ValueAnimator getResetScrollYAnimator() {
        return this.resetScrollYAnimator;
    }

    public final void setResetScrollYAnimator(ValueAnimator valueAnimator) {
        this.resetScrollYAnimator = valueAnimator;
    }

    public NotificationStackScrollLayoutEx(NotificationStackScrollLayout notificationStackScrollLayout) {
        Intrinsics.checkNotNullParameter(notificationStackScrollLayout, "notificationStackSCrollLayout");
        this.layout = notificationStackScrollLayout;
        Object obj = Dependency.get(AmbientState.class);
        Intrinsics.checkNotNullExpressionValue(obj, "get(AmbientState::class.java)");
        this.ambientState = (AmbientState) obj;
        Object obj2 = NTDependencyEx.get(AmbientStateEx.class);
        Intrinsics.checkNotNullExpressionValue(obj2, "get(AmbientStateEx::class.java)");
        this.ambientStateEx = (AmbientStateEx) obj2;
    }

    public void updateExpansionFraction(float f) {
        if (!this.layout.isShouldUseSplitNotificationShade() || !keepExpand()) {
            this.ambientState.setExpansionFraction(f);
        } else {
            this.ambientState.setExpansionFraction(1.0f);
        }
        setStackAlpha(f);
    }

    public void setQsExpansionFraction(float f) {
        boolean z = true;
        if (f == 1.0f) {
            this.canQSCollapse = true;
            return;
        }
        if (f != 0.0f) {
            z = false;
        }
        if (z) {
            this.canQSCollapse = false;
        }
    }

    public void onPanelTrackingStarted() {
        this.isTracking = false;
    }

    public void onTouchEvent(MotionEvent motionEvent) {
        Intrinsics.checkNotNullParameter(motionEvent, "ev");
        if (motionEvent.getActionMasked() == 1) {
            this.isTracking = false;
        } else if (motionEvent.getActionMasked() == 3) {
            this.isTracking = true;
        }
    }

    public boolean keepExpand() {
        return this.layout.isShouldUseSplitNotificationShade() && this.layout.isExpanded() && !this.isTracking;
    }

    public void setStackAlpha(float f) {
        if (!(this.stackAlpha == f)) {
            this.stackAlpha = f;
            this.ambientStateEx.setAlphaFraction(f);
            if (this.layout.isShouldUseSplitNotificationShade()) {
                this.layout.requestChildrenUpdate();
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0029, code lost:
        r4 = (r4 = r4.getDrawable()).getBounds();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean shouldInterceptIsInsideQsHeader(android.view.View r4) {
        /*
            r3 = this;
            java.lang.String r0 = "notificationWindow"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r4, r0)
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout r0 = r3.layout
            boolean r0 = r0.isShouldUseSplitNotificationShade()
            r1 = 1
            if (r0 == 0) goto L_0x000f
            return r1
        L_0x000f:
            r0 = 2131428788(0x7f0b05b4, float:1.847923E38)
            android.view.View r4 = r4.findViewById(r0)
            com.android.systemui.scrim.ScrimView r4 = (com.android.systemui.scrim.ScrimView) r4
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout r0 = r3.layout
            int r0 = r0.getVisibility()
            r2 = 0
            if (r0 != 0) goto L_0x003a
            if (r4 == 0) goto L_0x0032
            android.graphics.drawable.Drawable r4 = r4.getDrawable()
            if (r4 == 0) goto L_0x0032
            android.graphics.Rect r4 = r4.getBounds()
            if (r4 == 0) goto L_0x0032
            int r4 = r4.top
            goto L_0x0035
        L_0x0032:
            r4 = 2147483647(0x7fffffff, float:NaN)
        L_0x0035:
            int r3 = r3.topInset
            if (r4 > r3) goto L_0x003a
            return r1
        L_0x003a:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nothing.systemui.statusbar.notification.stack.NotificationStackScrollLayoutEx.shouldInterceptIsInsideQsHeader(android.view.View):boolean");
    }

    public final boolean isInsideQsHeader(MotionEvent motionEvent, Rect rect) {
        Intrinsics.checkNotNullParameter(motionEvent, "ev");
        Intrinsics.checkNotNullParameter(rect, "qsHeaderBound");
        if (this.layout.isShouldUseSplitNotificationShade()) {
            return rect.contains((int) motionEvent.getRawX(), (int) motionEvent.getRawY());
        }
        return false;
    }

    public final void onApplyWindowInsets(WindowInsets windowInsets) {
        Intrinsics.checkNotNullParameter(windowInsets, "insets");
        this.topInset = windowInsets.getSystemWindowInsetTop();
    }

    public void resetScrollAnimation() {
        if (this.layout.getOwnScrollY() != 0) {
            ValueAnimator valueAnimator = this.resetScrollYAnimator;
            if (valueAnimator != null) {
                Boolean valueOf = valueAnimator != null ? Boolean.valueOf(valueAnimator.isRunning()) : null;
                Intrinsics.checkNotNull(valueOf);
                if (valueOf.booleanValue()) {
                    return;
                }
            }
            ValueAnimator ofInt = ValueAnimator.ofInt(new int[]{this.layout.getOwnScrollY(), 0});
            this.resetScrollYAnimator = ofInt;
            if (ofInt != null) {
                ofInt.setDuration(250);
            }
            ValueAnimator valueAnimator2 = this.resetScrollYAnimator;
            Intrinsics.checkNotNull(valueAnimator2);
            valueAnimator2.addUpdateListener(new NotificationStackScrollLayoutEx$$ExternalSyntheticLambda0(this));
            ValueAnimator valueAnimator3 = this.resetScrollYAnimator;
            if (valueAnimator3 != null) {
                valueAnimator3.start();
            }
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: resetScrollAnimation$lambda-0  reason: not valid java name */
    public static final void m3546resetScrollAnimation$lambda0(NotificationStackScrollLayoutEx notificationStackScrollLayoutEx, ValueAnimator valueAnimator) {
        Intrinsics.checkNotNullParameter(notificationStackScrollLayoutEx, "this$0");
        NotificationStackScrollLayout notificationStackScrollLayout = notificationStackScrollLayoutEx.layout;
        Object animatedValue = valueAnimator.getAnimatedValue();
        if (animatedValue != null) {
            notificationStackScrollLayout.setOwnScrollY(((Integer) animatedValue).intValue());
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Int");
    }
}
