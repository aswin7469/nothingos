package com.android.systemui.statusbar.phone;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import com.android.systemui.C1894R;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.flags.BooleanFlag;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.plugins.p011qs.C2304QS;
import com.android.systemui.plugins.p011qs.QSContainerController;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.shared.system.QuickStepContract;
import com.android.systemui.util.LargeScreenUtils;
import com.android.systemui.util.ViewController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import java.util.function.Consumer;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000i\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0010\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0007*\u0001\u0012\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u00012\u00020\u0003B1\b\u0007\u0012\u0006\u0010\u0004\u001a\u00020\u0002\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\n\u0012\b\b\u0001\u0010\u000b\u001a\u00020\f¢\u0006\u0002\u0010\rJ\b\u0010+\u001a\u00020,H\u0002J\u0010\u0010-\u001a\u00020.2\u0006\u0010/\u001a\u000200H\u0002J\b\u00101\u001a\u00020.H\u0014J\b\u00102\u001a\u00020.H\u0016J\b\u00103\u001a\u00020.H\u0014J\u0010\u00104\u001a\u00020.2\u0006\u00105\u001a\u00020\u0016H\u0016J\u0010\u00106\u001a\u00020.2\u0006\u00107\u001a\u00020\u0016H\u0016J\u0010\u00108\u001a\u00020.2\u0006\u00107\u001a\u00020\u0016H\u0016J\u0010\u00109\u001a\u00020.2\u0006\u0010:\u001a\u00020;H\u0002J\u0010\u0010<\u001a\u00020.2\u0006\u0010:\u001a\u00020;H\u0002J\u0010\u0010=\u001a\u00020.2\u0006\u0010:\u001a\u00020;H\u0002J\u0010\u0010>\u001a\u00020.2\u0006\u0010:\u001a\u00020;H\u0002J\b\u0010?\u001a\u00020.H\u0002J\u0006\u0010@\u001a\u00020.J\u0006\u0010A\u001a\u00020.R\u000e\u0010\u000e\u001a\u00020\u000fX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u000fX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0011\u001a\u00020\u0012X\u0004¢\u0006\u0004\n\u0002\u0010\u0013R\u000e\u0010\t\u001a\u00020\nX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u000fX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0016X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0016X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0016X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u0016X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u000fX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u000fX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u000fX\u000e¢\u0006\u0002\n\u0000R$\u0010\u001f\u001a\u00020\u00162\u0006\u0010\u001e\u001a\u00020\u0016@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b \u0010!\"\u0004\b\"\u0010#R\u000e\u0010$\u001a\u00020\u000fX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010%\u001a\u00020\u0016X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010&\u001a\u00020'X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010(\u001a\u00020\u0016X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010)\u001a\u00020\u000fX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010*\u001a\u00020\u0016X\u0004¢\u0006\u0002\n\u0000¨\u0006B"}, mo65043d2 = {"Lcom/android/systemui/statusbar/phone/NotificationsQSContainerController;", "Lcom/android/systemui/util/ViewController;", "Lcom/android/systemui/statusbar/phone/NotificationsQuickSettingsContainer;", "Lcom/android/systemui/plugins/qs/QSContainerController;", "view", "navigationModeController", "Lcom/android/systemui/navigationbar/NavigationModeController;", "overviewProxyService", "Lcom/android/systemui/recents/OverviewProxyService;", "featureFlags", "Lcom/android/systemui/flags/FeatureFlags;", "delayableExecutor", "Lcom/android/systemui/util/concurrency/DelayableExecutor;", "(Lcom/android/systemui/statusbar/phone/NotificationsQuickSettingsContainer;Lcom/android/systemui/navigationbar/NavigationModeController;Lcom/android/systemui/recents/OverviewProxyService;Lcom/android/systemui/flags/FeatureFlags;Lcom/android/systemui/util/concurrency/DelayableExecutor;)V", "bottomCutoutInsets", "", "bottomStableInsets", "delayedInsetSetter", "com/android/systemui/statusbar/phone/NotificationsQSContainerController$delayedInsetSetter$1", "Lcom/android/systemui/statusbar/phone/NotificationsQSContainerController$delayedInsetSetter$1;", "footerActionsOffset", "isGestureNavigation", "", "isQSCustomizerAnimating", "isQSCustomizing", "isQSDetailShowing", "largeScreenShadeHeaderActive", "largeScreenShadeHeaderHeight", "notificationsBottomMargin", "panelMarginHorizontal", "value", "qsExpanded", "getQsExpanded", "()Z", "setQsExpanded", "(Z)V", "scrimShadeBottomMargin", "splitShadeEnabled", "taskbarVisibilityListener", "Lcom/android/systemui/recents/OverviewProxyService$OverviewProxyListener;", "taskbarVisible", "topMargin", "useCombinedQSHeaders", "calculateBottomSpacing", "Lcom/android/systemui/statusbar/phone/Paddings;", "ensureAllViewsHaveIds", "", "parentView", "Landroid/view/ViewGroup;", "onInit", "onViewAttached", "onViewDetached", "setCustomizerAnimating", "animating", "setCustomizerShowing", "showing", "setDetailShowing", "setKeyguardStatusViewConstraints", "constraintSet", "Landroidx/constraintlayout/widget/ConstraintSet;", "setLargeScreenShadeHeaderConstraints", "setNotificationsConstraints", "setQsConstraints", "updateBottomSpacing", "updateConstraints", "updateResources", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: NotificationsQSContainerController.kt */
public final class NotificationsQSContainerController extends ViewController<NotificationsQuickSettingsContainer> implements QSContainerController {
    /* access modifiers changed from: private */
    public int bottomCutoutInsets;
    /* access modifiers changed from: private */
    public int bottomStableInsets;
    /* access modifiers changed from: private */
    public final DelayableExecutor delayableExecutor;
    private final NotificationsQSContainerController$delayedInsetSetter$1 delayedInsetSetter = new NotificationsQSContainerController$delayedInsetSetter$1(this);
    private final FeatureFlags featureFlags;
    /* access modifiers changed from: private */
    public int footerActionsOffset;
    private boolean isGestureNavigation = true;
    private boolean isQSCustomizerAnimating;
    private boolean isQSCustomizing;
    private boolean isQSDetailShowing;
    private boolean largeScreenShadeHeaderActive;
    private int largeScreenShadeHeaderHeight;
    private final NavigationModeController navigationModeController;
    private int notificationsBottomMargin;
    private final OverviewProxyService overviewProxyService;
    private int panelMarginHorizontal;
    private boolean qsExpanded;
    /* access modifiers changed from: private */
    public int scrimShadeBottomMargin;
    /* access modifiers changed from: private */
    public boolean splitShadeEnabled;
    private final OverviewProxyService.OverviewProxyListener taskbarVisibilityListener = new NotificationsQSContainerController$taskbarVisibilityListener$1(this);
    /* access modifiers changed from: private */
    public boolean taskbarVisible;
    private int topMargin;
    private final boolean useCombinedQSHeaders;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    @Inject
    public NotificationsQSContainerController(NotificationsQuickSettingsContainer notificationsQuickSettingsContainer, NavigationModeController navigationModeController2, OverviewProxyService overviewProxyService2, FeatureFlags featureFlags2, @Main DelayableExecutor delayableExecutor2) {
        super(notificationsQuickSettingsContainer);
        Intrinsics.checkNotNullParameter(notificationsQuickSettingsContainer, "view");
        Intrinsics.checkNotNullParameter(navigationModeController2, "navigationModeController");
        Intrinsics.checkNotNullParameter(overviewProxyService2, "overviewProxyService");
        Intrinsics.checkNotNullParameter(featureFlags2, "featureFlags");
        Intrinsics.checkNotNullParameter(delayableExecutor2, "delayableExecutor");
        this.navigationModeController = navigationModeController2;
        this.overviewProxyService = overviewProxyService2;
        this.featureFlags = featureFlags2;
        this.delayableExecutor = delayableExecutor2;
        BooleanFlag booleanFlag = Flags.COMBINED_QS_HEADERS;
        Intrinsics.checkNotNullExpressionValue(booleanFlag, "COMBINED_QS_HEADERS");
        this.useCombinedQSHeaders = featureFlags2.isEnabled(booleanFlag);
    }

    public final boolean getQsExpanded() {
        return this.qsExpanded;
    }

    public final void setQsExpanded(boolean z) {
        if (this.qsExpanded != z) {
            this.qsExpanded = z;
            ((NotificationsQuickSettingsContainer) this.mView).invalidate();
        }
    }

    /* access modifiers changed from: protected */
    public void onInit() {
        this.isGestureNavigation = QuickStepContract.isGesturalMode(this.navigationModeController.addListener(new NotificationsQSContainerController$$ExternalSyntheticLambda0(this)));
    }

    /* access modifiers changed from: private */
    /* renamed from: onInit$lambda-0  reason: not valid java name */
    public static final void m3189onInit$lambda0(NotificationsQSContainerController notificationsQSContainerController, int i) {
        Intrinsics.checkNotNullParameter(notificationsQSContainerController, "this$0");
        notificationsQSContainerController.isGestureNavigation = QuickStepContract.isGesturalMode(i);
    }

    public void onViewAttached() {
        updateResources();
        this.overviewProxyService.addCallback(this.taskbarVisibilityListener);
        ((NotificationsQuickSettingsContainer) this.mView).setInsetsChangedListener(this.delayedInsetSetter);
        ((NotificationsQuickSettingsContainer) this.mView).setQSFragmentAttachedListener(new NotificationsQSContainerController$$ExternalSyntheticLambda1(this));
        ((NotificationsQuickSettingsContainer) this.mView).setConfigurationChangedListener(new NotificationsQSContainerController$$ExternalSyntheticLambda2(this));
    }

    /* access modifiers changed from: private */
    /* renamed from: onViewAttached$lambda-1  reason: not valid java name */
    public static final void m3190onViewAttached$lambda1(NotificationsQSContainerController notificationsQSContainerController, C2304QS qs) {
        Intrinsics.checkNotNullParameter(notificationsQSContainerController, "this$0");
        Intrinsics.checkNotNullParameter(qs, "qs");
        qs.setContainerController(notificationsQSContainerController);
    }

    /* access modifiers changed from: private */
    /* renamed from: onViewAttached$lambda-2  reason: not valid java name */
    public static final void m3191onViewAttached$lambda2(NotificationsQSContainerController notificationsQSContainerController, Configuration configuration) {
        Intrinsics.checkNotNullParameter(notificationsQSContainerController, "this$0");
        notificationsQSContainerController.updateResources();
    }

    /* access modifiers changed from: protected */
    public void onViewDetached() {
        this.overviewProxyService.removeCallback(this.taskbarVisibilityListener);
        ((NotificationsQuickSettingsContainer) this.mView).removeOnInsetsChangedListener();
        ((NotificationsQuickSettingsContainer) this.mView).removeQSFragmentAttachedListener();
        ((NotificationsQuickSettingsContainer) this.mView).setConfigurationChangedListener((Consumer<Configuration>) null);
    }

    public final void updateResources() {
        int i;
        Resources resources = getResources();
        Intrinsics.checkNotNullExpressionValue(resources, "resources");
        boolean shouldUseSplitNotificationShade = LargeScreenUtils.shouldUseSplitNotificationShade(resources);
        boolean z = true;
        boolean z2 = shouldUseSplitNotificationShade != this.splitShadeEnabled;
        this.splitShadeEnabled = shouldUseSplitNotificationShade;
        Resources resources2 = getResources();
        Intrinsics.checkNotNullExpressionValue(resources2, "resources");
        this.largeScreenShadeHeaderActive = LargeScreenUtils.shouldUseLargeScreenShadeHeader(resources2);
        this.notificationsBottomMargin = getResources().getDimensionPixelSize(C1894R.dimen.notification_panel_margin_bottom);
        this.largeScreenShadeHeaderHeight = getResources().getDimensionPixelSize(C1894R.dimen.large_screen_shade_header_height);
        this.panelMarginHorizontal = getResources().getDimensionPixelSize(C1894R.dimen.notification_panel_margin_horizontal);
        if (this.largeScreenShadeHeaderActive) {
            i = this.largeScreenShadeHeaderHeight;
        } else {
            i = getResources().getDimensionPixelSize(C1894R.dimen.notification_panel_margin_top);
        }
        this.topMargin = i;
        updateConstraints();
        boolean access$setAndReportChange = NotificationsQSContainerControllerKt.setAndReportChange(new C3054xcb887726(this), getResources().getDimensionPixelSize(C1894R.dimen.split_shade_notifications_scrim_margin_bottom));
        boolean access$setAndReportChange2 = NotificationsQSContainerControllerKt.setAndReportChange(new C3053xbda5425a(this), getResources().getDimensionPixelSize(C1894R.dimen.qs_footer_action_inset) + getResources().getDimensionPixelSize(C1894R.dimen.qs_footer_actions_bottom_padding));
        if (!access$setAndReportChange && !access$setAndReportChange2) {
            z = false;
        }
        if (z2 || z) {
            updateBottomSpacing();
        }
    }

    public void setCustomizerAnimating(boolean z) {
        if (this.isQSCustomizerAnimating != z) {
            this.isQSCustomizerAnimating = z;
            ((NotificationsQuickSettingsContainer) this.mView).invalidate();
        }
    }

    public void setCustomizerShowing(boolean z) {
        this.isQSCustomizing = z;
        updateBottomSpacing();
    }

    public void setDetailShowing(boolean z) {
        this.isQSDetailShowing = z;
        updateBottomSpacing();
    }

    /* access modifiers changed from: private */
    public final void updateBottomSpacing() {
        Paddings calculateBottomSpacing = calculateBottomSpacing();
        int component1 = calculateBottomSpacing.component1();
        int component2 = calculateBottomSpacing.component2();
        int component3 = calculateBottomSpacing.component3();
        ((NotificationsQuickSettingsContainer) this.mView).setPadding(0, 0, 0, component1);
        ((NotificationsQuickSettingsContainer) this.mView).setNotificationsMarginBottom(component2);
        ((NotificationsQuickSettingsContainer) this.mView).setQSContainerPaddingBottom(component3);
    }

    /* JADX WARNING: Removed duplicated region for block: B:27:0x0040  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0049  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final com.android.systemui.statusbar.phone.Paddings calculateBottomSpacing() {
        /*
            r5 = this;
            int r0 = r5.notificationsBottomMargin
            boolean r1 = r5.splitShadeEnabled
            r2 = 0
            if (r1 == 0) goto L_0x001a
            boolean r3 = r5.isGestureNavigation
            if (r3 == 0) goto L_0x000e
            int r3 = r5.bottomCutoutInsets
            goto L_0x0036
        L_0x000e:
            boolean r3 = r5.taskbarVisible
            if (r3 == 0) goto L_0x0015
            int r3 = r5.bottomStableInsets
            goto L_0x0036
        L_0x0015:
            int r3 = r5.bottomStableInsets
        L_0x0017:
            int r0 = r0 + r3
            r3 = r2
            goto L_0x0036
        L_0x001a:
            boolean r3 = r5.isQSCustomizing
            if (r3 != 0) goto L_0x0034
            boolean r3 = r5.isQSDetailShowing
            if (r3 == 0) goto L_0x0023
            goto L_0x0034
        L_0x0023:
            boolean r3 = r5.isGestureNavigation
            if (r3 == 0) goto L_0x002a
            int r3 = r5.bottomCutoutInsets
            goto L_0x0036
        L_0x002a:
            boolean r3 = r5.taskbarVisible
            if (r3 == 0) goto L_0x0031
            int r3 = r5.bottomStableInsets
            goto L_0x0036
        L_0x0031:
            int r3 = r5.bottomStableInsets
            goto L_0x0017
        L_0x0034:
            r0 = r2
            r3 = r0
        L_0x0036:
            boolean r4 = r5.isQSCustomizing
            if (r4 != 0) goto L_0x004b
            boolean r4 = r5.isQSDetailShowing
            if (r4 != 0) goto L_0x004b
            if (r1 == 0) goto L_0x0049
            int r1 = r5.scrimShadeBottomMargin
            int r1 = r0 - r1
            int r5 = r5.footerActionsOffset
            int r2 = r1 - r5
            goto L_0x004b
        L_0x0049:
            int r2 = r5.bottomStableInsets
        L_0x004b:
            com.android.systemui.statusbar.phone.Paddings r5 = new com.android.systemui.statusbar.phone.Paddings
            r5.<init>(r3, r0, r2)
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.NotificationsQSContainerController.calculateBottomSpacing():com.android.systemui.statusbar.phone.Paddings");
    }

    public final void updateConstraints() {
        View view = this.mView;
        Intrinsics.checkNotNullExpressionValue(view, "mView");
        ensureAllViewsHaveIds((ViewGroup) view);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone((ConstraintLayout) this.mView);
        setKeyguardStatusViewConstraints(constraintSet);
        setQsConstraints(constraintSet);
        setNotificationsConstraints(constraintSet);
        setLargeScreenShadeHeaderConstraints(constraintSet);
        ((NotificationsQuickSettingsContainer) this.mView).applyConstraints(constraintSet);
    }

    private final void setLargeScreenShadeHeaderConstraints(ConstraintSet constraintSet) {
        if (this.largeScreenShadeHeaderActive) {
            constraintSet.constrainHeight(C1894R.C1898id.split_shade_status_bar, this.largeScreenShadeHeaderHeight);
        } else if (this.useCombinedQSHeaders) {
            constraintSet.constrainHeight(C1894R.C1898id.split_shade_status_bar, -2);
        }
    }

    private final void setNotificationsConstraints(ConstraintSet constraintSet) {
        int i = 0;
        constraintSet.connect(C1894R.C1898id.notification_stack_scroller, 6, this.splitShadeEnabled ? C1894R.C1898id.qs_edge_guideline : 0, 6);
        if (!this.splitShadeEnabled) {
            i = this.panelMarginHorizontal;
        }
        constraintSet.setMargin(C1894R.C1898id.notification_stack_scroller, 6, i);
        constraintSet.setMargin(C1894R.C1898id.notification_stack_scroller, 7, this.panelMarginHorizontal);
        constraintSet.setMargin(C1894R.C1898id.notification_stack_scroller, 3, this.topMargin);
        constraintSet.setMargin(C1894R.C1898id.notification_stack_scroller, 4, this.notificationsBottomMargin);
    }

    private final void setQsConstraints(ConstraintSet constraintSet) {
        int i = 0;
        constraintSet.connect(C1894R.C1898id.qs_frame, 7, this.splitShadeEnabled ? C1894R.C1898id.qs_edge_guideline : 0, 7);
        constraintSet.setMargin(C1894R.C1898id.qs_frame, 6, this.splitShadeEnabled ? 0 : this.panelMarginHorizontal);
        if (!this.splitShadeEnabled) {
            i = this.panelMarginHorizontal;
        }
        constraintSet.setMargin(C1894R.C1898id.qs_frame, 7, i);
        constraintSet.setMargin(C1894R.C1898id.qs_frame, 3, this.topMargin);
    }

    private final void setKeyguardStatusViewConstraints(ConstraintSet constraintSet) {
        int dimensionPixelSize = getResources().getDimensionPixelSize(C1894R.dimen.status_view_margin_horizontal);
        constraintSet.setMargin(C1894R.C1898id.keyguard_status_view, 6, dimensionPixelSize);
        constraintSet.setMargin(C1894R.C1898id.keyguard_status_view, 7, dimensionPixelSize);
    }

    private final void ensureAllViewsHaveIds(ViewGroup viewGroup) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = viewGroup.getChildAt(i);
            if (childAt.getId() == -1) {
                childAt.setId(View.generateViewId());
            }
        }
    }
}
