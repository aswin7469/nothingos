package com.nothing.systemui.statusbar.phone;

import android.animation.ValueAnimator;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.MathUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import com.android.systemui.C1893R;
import com.android.systemui.Dependency;
import com.android.systemui.animation.ShadeInterpolation;
import com.android.systemui.statusbar.notification.stack.AmbientState;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.statusbar.phone.ScrimController;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.statusbar.notification.NTLightweightHeadsupManager;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000Z\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0019\n\u0002\u0010\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0011\n\u0002\u0018\u0002\n\u0002\b\b\b\u0016\u0018\u0000 i2\u00020\u0001:\u0001iB\u000f\b\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0010\u0010?\u001a\u00020\u001c2\u0006\u0010@\u001a\u00020\u001cH\u0016J\u0010\u0010A\u001a\u00020B2\u0006\u0010C\u001a\u00020\u0006H\u0016J\u001e\u0010D\u001a\u00020\"2\u0006\u0010E\u001a\u00020\"2\u0006\u0010F\u001a\u00020\"2\u0006\u0010G\u001a\u00020\"J\u0006\u0010H\u001a\u00020\u001cJ\u0010\u0010I\u001a\u00020\u00062\u0006\u0010J\u001a\u00020\u001cH\u0002J\u0010\u0010K\u001a\u00020B2\u0006\u0010L\u001a\u00020MH\u0016J\u0010\u0010N\u001a\u00020B2\u0006\u0010O\u001a\u00020PH\u0016J\u0006\u0010Q\u001a\u00020BJ\u0006\u0010R\u001a\u00020BJ\b\u0010S\u001a\u00020BH\u0016J\u0010\u0010T\u001a\u00020B2\u0006\u0010C\u001a\u00020\u0006H\u0016J\u001e\u0010U\u001a\u00020\u00062\u0006\u0010V\u001a\u00020\"2\u0006\u0010W\u001a\u00020\u00062\u0006\u0010X\u001a\u00020\u001cJ\u000e\u0010Y\u001a\u00020\u00062\u0006\u0010Z\u001a\u00020\u001cJ(\u0010[\u001a\u00020\u00062\u0006\u0010\\\u001a\u00020\u00132\b\u0010]\u001a\u0004\u0018\u00010\u00132\u0006\u0010J\u001a\u00020\u001c2\u0006\u0010^\u001a\u00020\u0006J\u0006\u0010_\u001a\u00020\u0006J\u0018\u0010`\u001a\u00020B2\u0006\u0010a\u001a\u00020b2\u0006\u0010c\u001a\u00020\u001cH\u0016J\u0018\u0010d\u001a\u00020B2\u0006\u0010e\u001a\u00020\u00062\u0006\u0010c\u001a\u00020\u001cH\u0016J\b\u0010f\u001a\u00020BH\u0016J\b\u0010g\u001a\u00020BH\u0016J\b\u0010h\u001a\u00020BH\u0016R$\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u0006@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u000bR\u001a\u0010\f\u001a\u00020\u0006X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\t\"\u0004\b\u000e\u0010\u000bR$\u0010\u000f\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u0006@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\t\"\u0004\b\u0011\u0010\u000bR\u001c\u0010\u0012\u001a\u0004\u0018\u00010\u0013X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u0015\"\u0004\b\u0016\u0010\u0017R\u001a\u0010\u0018\u001a\u00020\u0006X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\t\"\u0004\b\u001a\u0010\u000bR\u001a\u0010\u001b\u001a\u00020\u001cX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001d\u0010\u001e\"\u0004\b\u001f\u0010 R\u001a\u0010!\u001a\u00020\"X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b#\u0010$\"\u0004\b%\u0010&R\u001c\u0010'\u001a\u0004\u0018\u00010(X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b)\u0010*\"\u0004\b+\u0010,R\u0011\u0010-\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b.\u0010/R\u001a\u00100\u001a\u00020\u0006X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b1\u0010\t\"\u0004\b2\u0010\u000bR\u001a\u00103\u001a\u00020\"X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b4\u0010$\"\u0004\b5\u0010&R$\u00106\u001a\u00020\"2\u0006\u0010\u0005\u001a\u00020\"@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b7\u0010$\"\u0004\b8\u0010&R\u001a\u00109\u001a\u00020\"X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b:\u0010$\"\u0004\b;\u0010&R\u001a\u0010<\u001a\u00020\u0006X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b=\u0010\t\"\u0004\b>\u0010\u000b¨\u0006j"}, mo64987d2 = {"Lcom/nothing/systemui/statusbar/phone/NotificationPanelViewControllerEx;", "", "controller", "Lcom/android/systemui/statusbar/phone/NotificationPanelViewController;", "(Lcom/android/systemui/statusbar/phone/NotificationPanelViewController;)V", "value", "", "canQsCollapseForLand", "getCanQsCollapseForLand", "()Z", "setCanQsCollapseForLand", "(Z)V", "hasOrientationChanged", "getHasOrientationChanged", "setHasOrientationChanged", "hasQSLayout", "getHasQSLayout", "setHasQSLayout", "lastFlingToHeightAnimator", "Landroid/animation/ValueAnimator;", "getLastFlingToHeightAnimator", "()Landroid/animation/ValueAnimator;", "setLastFlingToHeightAnimator", "(Landroid/animation/ValueAnimator;)V", "lastFlingToHeightExpand", "getLastFlingToHeightExpand", "setLastFlingToHeightExpand", "lastFlingToHeightVel", "", "getLastFlingToHeightVel", "()F", "setLastFlingToHeightVel", "(F)V", "lastOrientation", "", "getLastOrientation", "()I", "setLastOrientation", "(I)V", "mQsStatusBarLayout", "Landroid/widget/RelativeLayout;", "getMQsStatusBarLayout", "()Landroid/widget/RelativeLayout;", "setMQsStatusBarLayout", "(Landroid/widget/RelativeLayout;)V", "notificationPanelViewController", "getNotificationPanelViewController", "()Lcom/android/systemui/statusbar/phone/NotificationPanelViewController;", "panelViewTouchDownShowBouncing", "getPanelViewTouchDownShowBouncing", "setPanelViewTouchDownShowBouncing", "portState", "getPortState", "setPortState", "qSScrollY", "getQSScrollY", "setQSScrollY", "quickQsOffsetHeight", "getQuickQsOffsetHeight", "setQuickQsOffsetHeight", "splitShadeEnabled", "getSplitShadeEnabled", "setSplitShadeEnabled", "computeQsExpansionFraction", "result", "flingExpands", "", "expands", "getKeyguardNotificationStaticPaddingForNotBypassEnableCase", "stackScrollerPadding", "qsMinExpansionHeight", "qsClipTop", "getQSEdgePosition", "isSameDirection", "vel", "makeQsStatusBarView", "npv", "Landroid/view/View;", "onConfigurationChanged", "newConfig", "Landroid/content/res/Configuration;", "onPanelViewTouchTrackStarted", "onUnlockAnimationFinished", "resetViews", "setQsExpanded", "shouldGetQSEdgePosition", "barState", "bypassEnabled", "transitioningToFullShadeProgress", "shouldIgnorePanelViewTouch", "h", "shouldIgnoreStartFlingAnimavor", "newAnimator", "oldAnimator", "expand", "shouldQuickSettingsIntercept", "updateQsFrameAlpha", "qsFrame", "Landroid/widget/FrameLayout;", "expandFraction", "updateQsStatusBarAlpha", "isOnKeyguard", "updateQsStatusBarVisibility", "updateResources", "updateStates", "Companion", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: NotificationPanelViewControllerEx.kt */
public class NotificationPanelViewControllerEx {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    /* access modifiers changed from: private */
    public static final int QQS_STATE = 2;
    /* access modifiers changed from: private */
    public static final int QS_STATE = 1;
    private boolean canQsCollapseForLand;
    private boolean hasOrientationChanged;
    private boolean hasQSLayout = true;
    private ValueAnimator lastFlingToHeightAnimator;
    private boolean lastFlingToHeightExpand;
    private float lastFlingToHeightVel;
    private int lastOrientation;
    private RelativeLayout mQsStatusBarLayout;
    private final NotificationPanelViewController notificationPanelViewController;
    private boolean panelViewTouchDownShowBouncing;
    private int portState;
    private int qSScrollY;
    private int quickQsOffsetHeight;
    private boolean splitShadeEnabled;

    public final boolean shouldGetQSEdgePosition(int i, boolean z, float f) {
        if (i == 2 && !z) {
            if (f == 0.0f) {
                return true;
            }
        }
        return false;
    }

    @Metadata(mo64986d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0014\u0010\u0003\u001a\u00020\u0004XD¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u0014\u0010\u0007\u001a\u00020\u0004XD¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0006¨\u0006\t"}, mo64987d2 = {"Lcom/nothing/systemui/statusbar/phone/NotificationPanelViewControllerEx$Companion;", "", "()V", "QQS_STATE", "", "getQQS_STATE", "()I", "QS_STATE", "getQS_STATE", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: NotificationPanelViewControllerEx.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final int getQS_STATE() {
            return NotificationPanelViewControllerEx.QS_STATE;
        }

        public final int getQQS_STATE() {
            return NotificationPanelViewControllerEx.QQS_STATE;
        }
    }

    public final boolean getCanQsCollapseForLand() {
        return this.canQsCollapseForLand;
    }

    public final void setCanQsCollapseForLand(boolean z) {
        this.canQsCollapseForLand = z;
    }

    public final boolean getHasOrientationChanged() {
        return this.hasOrientationChanged;
    }

    public final void setHasOrientationChanged(boolean z) {
        this.hasOrientationChanged = z;
    }

    public final int getPortState() {
        return this.portState;
    }

    public final void setPortState(int i) {
        this.portState = i;
    }

    public final boolean getHasQSLayout() {
        return this.hasQSLayout;
    }

    public final void setHasQSLayout(boolean z) {
        this.hasQSLayout = z;
    }

    public final NotificationPanelViewController getNotificationPanelViewController() {
        return this.notificationPanelViewController;
    }

    public final int getQuickQsOffsetHeight() {
        return this.quickQsOffsetHeight;
    }

    public final void setQuickQsOffsetHeight(int i) {
        this.quickQsOffsetHeight = i;
    }

    public final boolean getSplitShadeEnabled() {
        return this.splitShadeEnabled;
    }

    public final void setSplitShadeEnabled(boolean z) {
        this.splitShadeEnabled = z;
    }

    public final int getLastOrientation() {
        return this.lastOrientation;
    }

    public final void setLastOrientation(int i) {
        this.lastOrientation = i;
    }

    public final RelativeLayout getMQsStatusBarLayout() {
        return this.mQsStatusBarLayout;
    }

    public final void setMQsStatusBarLayout(RelativeLayout relativeLayout) {
        this.mQsStatusBarLayout = relativeLayout;
    }

    public final int getQSScrollY() {
        return this.qSScrollY;
    }

    public final void setQSScrollY(int i) {
        this.qSScrollY = i;
    }

    public final ValueAnimator getLastFlingToHeightAnimator() {
        return this.lastFlingToHeightAnimator;
    }

    public final void setLastFlingToHeightAnimator(ValueAnimator valueAnimator) {
        this.lastFlingToHeightAnimator = valueAnimator;
    }

    public final float getLastFlingToHeightVel() {
        return this.lastFlingToHeightVel;
    }

    public final void setLastFlingToHeightVel(float f) {
        this.lastFlingToHeightVel = f;
    }

    public final boolean getLastFlingToHeightExpand() {
        return this.lastFlingToHeightExpand;
    }

    public final void setLastFlingToHeightExpand(boolean z) {
        this.lastFlingToHeightExpand = z;
    }

    public final boolean getPanelViewTouchDownShowBouncing() {
        return this.panelViewTouchDownShowBouncing;
    }

    public final void setPanelViewTouchDownShowBouncing(boolean z) {
        this.panelViewTouchDownShowBouncing = z;
    }

    @Inject
    public NotificationPanelViewControllerEx(NotificationPanelViewController notificationPanelViewController2) {
        Intrinsics.checkNotNullParameter(notificationPanelViewController2, "controller");
        this.notificationPanelViewController = notificationPanelViewController2;
        Resources resources = notificationPanelViewController2.getView().getResources();
        this.splitShadeEnabled = resources.getBoolean(C1893R.bool.config_use_split_notification_shade);
        this.lastOrientation = resources.getConfiguration().orientation;
    }

    public void setQsExpanded(boolean z) {
        if (this.notificationPanelViewController.getQsExpanded() != z) {
            setCanQsCollapseForLand(true);
        }
    }

    public void flingExpands(boolean z) {
        if (this.splitShadeEnabled && z) {
            this.notificationPanelViewController.setQsExpandImmediate(true);
        }
        if (z) {
            Object obj = NTDependencyEx.get(NTLightweightHeadsupManager.class);
            if (obj != null) {
                ((NTLightweightHeadsupManager) obj).hidePopNotificationView();
                return;
            }
            throw new NullPointerException("null cannot be cast to non-null type com.nothing.systemui.statusbar.notification.NTLightweightHeadsupManager");
        }
    }

    public void resetViews() {
        this.hasOrientationChanged = false;
    }

    public float computeQsExpansionFraction(float f) {
        if (Float.isNaN(f) && !this.hasQSLayout && this.notificationPanelViewController.getQsFullyExpanded()) {
            return 1.0f;
        }
        if (Float.isNaN(f)) {
            return 0.0f;
        }
        return f;
    }

    public void onConfigurationChanged(Configuration configuration) {
        Intrinsics.checkNotNullParameter(configuration, "newConfig");
        if (configuration.orientation != this.lastOrientation) {
            updateStates();
            this.lastOrientation = configuration.orientation;
        }
        if (configuration.orientation == 1) {
            ((ScrimController) NTDependencyEx.get(ScrimController.class)).setNotificationsOverScrollAmount(0);
        }
    }

    public void updateResources() {
        Resources resources = this.notificationPanelViewController.getView().getResources();
        this.splitShadeEnabled = resources.getBoolean(C1893R.bool.config_use_split_notification_shade);
        setHasQSLayout(false);
        setCanQsCollapseForLand(true);
        this.quickQsOffsetHeight = resources.getDimensionPixelSize(this.splitShadeEnabled ? C1893R.dimen.quick_qs_offset_height : 17105486);
    }

    public void updateStates() {
        int i;
        if (this.notificationPanelViewController.isPanelExpanded() && !this.notificationPanelViewController.isOnKeyguard()) {
            if (this.notificationPanelViewController.getQs() != null) {
                this.notificationPanelViewController.updateQSMinHeight();
            }
            if (this.splitShadeEnabled) {
                this.hasOrientationChanged = true;
                if (this.notificationPanelViewController.getQsFullyExpanded()) {
                    i = QS_STATE;
                } else {
                    i = QQS_STATE;
                }
                this.portState = i;
                this.notificationPanelViewController.setQsFullyExpanded(true);
                this.notificationPanelViewController.setQsExpanded(true);
            } else if (!this.hasOrientationChanged) {
                this.notificationPanelViewController.setQsExpanded(false);
                this.notificationPanelViewController.setQsFullyExpanded(false);
            } else if (this.portState == QQS_STATE) {
                this.notificationPanelViewController.setQsExpanded(false);
                this.notificationPanelViewController.setQsFullyExpanded(false);
            }
            this.notificationPanelViewController.updateQsState();
        }
    }

    public void makeQsStatusBarView(View view) {
        Intrinsics.checkNotNullParameter(view, "npv");
        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(C1893R.C1897id.qs_status_bar_layout);
        this.mQsStatusBarLayout = relativeLayout;
        if (relativeLayout != null && relativeLayout != null) {
            relativeLayout.setAlpha(0.0f);
        }
    }

    public void updateQsStatusBarVisibility() {
        RelativeLayout relativeLayout = this.mQsStatusBarLayout;
        if (relativeLayout != null && relativeLayout != null) {
            relativeLayout.setVisibility(this.splitShadeEnabled ? 0 : 8);
        }
    }

    public void updateQsStatusBarAlpha(boolean z, float f) {
        RelativeLayout relativeLayout;
        if (!z && (relativeLayout = this.mQsStatusBarLayout) != null) {
            boolean z2 = false;
            if (relativeLayout != null && relativeLayout.getVisibility() == 0) {
                z2 = true;
            }
            if (z2) {
                float notificationScrimAlpha = ShadeInterpolation.getNotificationScrimAlpha(f);
                RelativeLayout relativeLayout2 = this.mQsStatusBarLayout;
                if (relativeLayout2 != null) {
                    relativeLayout2.setAlpha(notificationScrimAlpha);
                }
            }
        }
    }

    public void updateQsFrameAlpha(FrameLayout frameLayout, float f) {
        Intrinsics.checkNotNullParameter(frameLayout, "qsFrame");
        if (this.splitShadeEnabled) {
            frameLayout.setAlpha(ShadeInterpolation.getNotificationScrimAlpha(f));
        } else {
            frameLayout.setAlpha(1.0f);
        }
    }

    public final int getKeyguardNotificationStaticPaddingForNotBypassEnableCase(int i, int i2, int i3) {
        float f = ((float) i3) / ((float) i);
        if (f > 1.0f) {
            f = 1.0f;
        }
        return (int) MathUtils.lerp(i, i2, f);
    }

    public final float getQSEdgePosition() {
        AmbientState ambientState = (AmbientState) Dependency.get(AmbientState.class);
        return Math.max((ambientState.getStackY() + (((float) ambientState.getStackTopMargin()) * ambientState.getExpansionFraction())) - ((float) ambientState.getScrollY()), 0.0f);
    }

    public final boolean shouldQuickSettingsIntercept() {
        return this.qSScrollY <= 0 || this.lastOrientation != 1;
    }

    public final boolean shouldIgnoreStartFlingAnimavor(ValueAnimator valueAnimator, ValueAnimator valueAnimator2, float f, boolean z) {
        Intrinsics.checkNotNullParameter(valueAnimator, "newAnimator");
        if (valueAnimator2 != null && Intrinsics.areEqual((Object) valueAnimator2, (Object) this.lastFlingToHeightAnimator) && isSameDirection(f) && z == this.lastFlingToHeightExpand && valueAnimator2.isRunning()) {
            return true;
        }
        this.lastFlingToHeightAnimator = valueAnimator;
        this.lastFlingToHeightVel = f;
        this.lastFlingToHeightExpand = z;
        return false;
    }

    public final boolean shouldIgnorePanelViewTouch(float f) {
        return !this.notificationPanelViewController.isQsExpanded() && this.notificationPanelViewController.isOnKeyguard() && !this.panelViewTouchDownShowBouncing && f > 0.0f;
    }

    private final boolean isSameDirection(float f) {
        return (f <= 0.0f && this.lastFlingToHeightVel <= 0.0f) || (f >= 0.0f && this.lastFlingToHeightVel >= 0.0f);
    }

    public final void onPanelViewTouchTrackStarted() {
        this.panelViewTouchDownShowBouncing = this.notificationPanelViewController.isBouncerShowing();
    }

    public final void onUnlockAnimationFinished() {
        ((ScrimController) NTDependencyEx.get(ScrimController.class)).calculateAndUpdatePanelExpansion(true);
    }
}
