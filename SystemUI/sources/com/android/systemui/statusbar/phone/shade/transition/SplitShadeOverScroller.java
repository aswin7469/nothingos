package com.android.systemui.statusbar.phone.shade.transition;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.MathUtils;
import com.android.systemui.C1894R;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.p011qs.C2304QS;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.phone.ScrimController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import dagger.assisted.Assisted;
import dagger.assisted.AssistedFactory;
import dagger.assisted.AssistedInject;
import java.p026io.PrintWriter;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

@Metadata(mo65042d1 = {"\u0000l\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\n\n\u0002\u0010\u000b\n\u0002\b\u0006\u0018\u00002\u00020\u0001:\u00014B;\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\b\b\u0001\u0010\n\u001a\u00020\u000b\u0012\b\b\u0001\u0010\f\u001a\u00020\r¢\u0006\u0002\u0010\u000eJ\u0010\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u0012H\u0002J\u0010\u0010\u001e\u001a\u00020\u00122\u0006\u0010\u000f\u001a\u00020\u0010H\u0002J#\u0010\u001f\u001a\u00020\u001c2\u0006\u0010 \u001a\u00020!2\f\u0010\"\u001a\b\u0012\u0004\u0012\u00020$0#H\u0002¢\u0006\u0002\u0010%J\r\u0010&\u001a\u00020\u001cH\u0001¢\u0006\u0002\b'J\u0010\u0010(\u001a\u00020\u001c2\u0006\u0010)\u001a\u00020\u0010H\u0016J\u0010\u0010*\u001a\u00020\u001c2\u0006\u0010+\u001a\u00020\u0012H\u0016J\u0010\u0010,\u001a\u00020\u001c2\u0006\u0010\u000f\u001a\u00020\u0010H\u0002J\b\u0010-\u001a\u00020\u001cH\u0002J\b\u0010.\u001a\u00020/H\u0002J\u0018\u00100\u001a\u00020/2\u0006\u00101\u001a\u00020\u00122\u0006\u00102\u001a\u00020\u0012H\u0002J\b\u00103\u001a\u00020\u001cH\u0002R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0013\u001a\u00020\u0012X\u000e¢\u0006\b\n\u0000\u0012\u0004\b\u0014\u0010\u0015R\u000e\u0010\u0016\u001a\u00020\u0012X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0017\u001a\u0004\u0018\u00010\u0018X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u001aX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000¨\u00065"}, mo65043d2 = {"Lcom/android/systemui/statusbar/phone/shade/transition/SplitShadeOverScroller;", "Lcom/android/systemui/statusbar/phone/shade/transition/ShadeOverScroller;", "configurationController", "Lcom/android/systemui/statusbar/policy/ConfigurationController;", "dumpManager", "Lcom/android/systemui/dump/DumpManager;", "context", "Landroid/content/Context;", "scrimController", "Lcom/android/systemui/statusbar/phone/ScrimController;", "qS", "Lcom/android/systemui/plugins/qs/QS;", "nsslController", "Lcom/android/systemui/statusbar/notification/stack/NotificationStackScrollLayoutController;", "(Lcom/android/systemui/statusbar/policy/ConfigurationController;Lcom/android/systemui/dump/DumpManager;Landroid/content/Context;Lcom/android/systemui/statusbar/phone/ScrimController;Lcom/android/systemui/plugins/qs/QS;Lcom/android/systemui/statusbar/notification/stack/NotificationStackScrollLayoutController;)V", "dragDownAmount", "", "maxOverScrollAmount", "", "panelState", "getPanelState$annotations", "()V", "previousOverscrollAmount", "releaseOverScrollAnimator", "Landroid/animation/Animator;", "releaseOverScrollDuration", "", "applyOverscroll", "", "overscrollAmount", "calculateOverscrollAmount", "dump", "pw", "Ljava/io/PrintWriter;", "strings", "", "", "(Ljava/io/PrintWriter;[Ljava/lang/String;)V", "finishAnimations", "finishAnimations$SystemUI_nothingRelease", "onDragDownAmountChanged", "newDragDownAmount", "onPanelStateChanged", "newPanelState", "overScroll", "releaseOverScroll", "shouldOverscroll", "", "shouldReleaseOverscroll", "previousState", "newState", "updateResources", "Factory", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: SplitShadeOverScroller.kt */
public final class SplitShadeOverScroller implements ShadeOverScroller {
    private final Context context;
    private float dragDownAmount;
    private int maxOverScrollAmount;
    private final NotificationStackScrollLayoutController nsslController;
    private int panelState;
    private int previousOverscrollAmount;

    /* renamed from: qS */
    private final C2304QS f392qS;
    private Animator releaseOverScrollAnimator;
    private long releaseOverScrollDuration;
    private final ScrimController scrimController;

    @AssistedFactory
    @Metadata(mo65042d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bç\u0001\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\bÀ\u0006\u0001"}, mo65043d2 = {"Lcom/android/systemui/statusbar/phone/shade/transition/SplitShadeOverScroller$Factory;", "", "create", "Lcom/android/systemui/statusbar/phone/shade/transition/SplitShadeOverScroller;", "qS", "Lcom/android/systemui/plugins/qs/QS;", "nsslController", "Lcom/android/systemui/statusbar/notification/stack/NotificationStackScrollLayoutController;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: SplitShadeOverScroller.kt */
    public interface Factory {
        SplitShadeOverScroller create(C2304QS qs, NotificationStackScrollLayoutController notificationStackScrollLayoutController);
    }

    private static /* synthetic */ void getPanelState$annotations() {
    }

    private final boolean shouldReleaseOverscroll(int i, int i2) {
        return i == 1 && i2 != 1;
    }

    @AssistedInject
    public SplitShadeOverScroller(ConfigurationController configurationController, DumpManager dumpManager, Context context2, ScrimController scrimController2, @Assisted C2304QS qs, @Assisted NotificationStackScrollLayoutController notificationStackScrollLayoutController) {
        Intrinsics.checkNotNullParameter(configurationController, "configurationController");
        Intrinsics.checkNotNullParameter(dumpManager, "dumpManager");
        Intrinsics.checkNotNullParameter(context2, "context");
        Intrinsics.checkNotNullParameter(scrimController2, "scrimController");
        Intrinsics.checkNotNullParameter(qs, "qS");
        Intrinsics.checkNotNullParameter(notificationStackScrollLayoutController, "nsslController");
        this.context = context2;
        this.scrimController = scrimController2;
        this.f392qS = qs;
        this.nsslController = notificationStackScrollLayoutController;
        updateResources();
        configurationController.addCallback(new ConfigurationController.ConfigurationListener(this) {
            final /* synthetic */ SplitShadeOverScroller this$0;

            {
                this.this$0 = r1;
            }

            public void onConfigChanged(Configuration configuration) {
                this.this$0.updateResources();
            }
        });
        dumpManager.registerDumpable(new SplitShadeOverScroller$$ExternalSyntheticLambda1(this));
    }

    /* access modifiers changed from: private */
    public final void updateResources() {
        Resources resources = this.context.getResources();
        this.maxOverScrollAmount = resources.getDimensionPixelSize(C1894R.dimen.shade_max_over_scroll_amount);
        this.releaseOverScrollDuration = (long) resources.getInteger(C1894R.integer.lockscreen_shade_over_scroll_release_duration);
    }

    public void onPanelStateChanged(int i) {
        if (shouldReleaseOverscroll(this.panelState, i)) {
            releaseOverScroll();
        }
        this.panelState = i;
    }

    public void onDragDownAmountChanged(float f) {
        if (!(this.dragDownAmount == f)) {
            this.dragDownAmount = f;
            if (shouldOverscroll()) {
                overScroll(f);
            }
        }
    }

    private final boolean shouldOverscroll() {
        return this.panelState == 1;
    }

    private final void overScroll(float f) {
        int calculateOverscrollAmount = calculateOverscrollAmount(f);
        applyOverscroll(calculateOverscrollAmount);
        this.previousOverscrollAmount = calculateOverscrollAmount;
    }

    private final int calculateOverscrollAmount(float f) {
        return (int) (MathUtils.saturate(f / ((float) this.nsslController.getHeight())) * ((float) this.maxOverScrollAmount));
    }

    private final void applyOverscroll(int i) {
        this.f392qS.setOverScrollAmount(i);
        this.scrimController.setNotificationsOverScrollAmount(i);
        this.nsslController.setOverScrollAmount(i);
    }

    private final void releaseOverScroll() {
        ValueAnimator ofInt = ValueAnimator.ofInt(new int[]{this.previousOverscrollAmount, 0});
        ofInt.addUpdateListener(new SplitShadeOverScroller$$ExternalSyntheticLambda0(this));
        ofInt.setInterpolator(Interpolators.STANDARD);
        ofInt.setDuration(this.releaseOverScrollDuration);
        ofInt.start();
        this.releaseOverScrollAnimator = ofInt;
        this.previousOverscrollAmount = 0;
    }

    /* access modifiers changed from: private */
    /* renamed from: releaseOverScroll$lambda-0  reason: not valid java name */
    public static final void m3221releaseOverScroll$lambda0(SplitShadeOverScroller splitShadeOverScroller, ValueAnimator valueAnimator) {
        Intrinsics.checkNotNullParameter(splitShadeOverScroller, "this$0");
        Object animatedValue = valueAnimator.getAnimatedValue();
        if (animatedValue != null) {
            int intValue = ((Integer) animatedValue).intValue();
            splitShadeOverScroller.f392qS.setOverScrollAmount(intValue);
            splitShadeOverScroller.scrimController.setNotificationsOverScrollAmount(intValue);
            splitShadeOverScroller.nsslController.setOverScrollAmount(intValue);
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

    /* access modifiers changed from: private */
    public final void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println(StringsKt.trimIndent("\n            SplitShadeOverScroller:\n                Resources:\n                    releaseOverScrollDuration: " + this.releaseOverScrollDuration + "\n                    maxOverScrollAmount: " + this.maxOverScrollAmount + "\n                State:\n                    previousOverscrollAmount: " + this.previousOverscrollAmount + "\n                    dragDownAmount: " + this.dragDownAmount + "\n                    panelState: " + this.panelState + "\n            "));
    }
}
