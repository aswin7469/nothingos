package com.android.systemui.statusbar.phone;

import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import com.android.systemui.C1893R;
import com.android.systemui.shared.animation.UnfoldMoveFromCenterAnimator;
import com.android.systemui.statusbar.phone.PhoneStatusBarView;
import com.android.systemui.statusbar.phone.userswitcher.StatusBarUserSwitcherController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.unfold.SysUIUnfoldComponent;
import com.android.systemui.unfold.util.ScopedUnfoldTransitionProgressProvider;
import com.android.systemui.util.ViewController;
import com.android.systemui.util.view.ViewUtil;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Named;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0004*\u0001\u0012\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0002#$BE\b\u0002\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\n\b\u0001\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r\u0012\u0006\u0010\u000e\u001a\u00020\u000f¢\u0006\u0002\u0010\u0010J\b\u0010\u0014\u001a\u00020\u0015H\u0014J\b\u0010\u0016\u001a\u00020\u0015H\u0014J\b\u0010\u0017\u001a\u00020\u0015H\u0014J\u000e\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u001bJ\u000e\u0010\u001c\u001a\u00020\u00152\u0006\u0010\u001d\u001a\u00020\u001eJ\u0016\u0010\u001f\u001a\u00020\u00192\u0006\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020!R\u000e\u0010\u000e\u001a\u00020\u000fX\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0011\u001a\u00020\u0012X\u0004¢\u0006\u0004\n\u0002\u0010\u0013R\u0010\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0004\u001a\u0004\u0018\u00010\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\u0002\n\u0000¨\u0006%"}, mo64987d2 = {"Lcom/android/systemui/statusbar/phone/PhoneStatusBarViewController;", "Lcom/android/systemui/util/ViewController;", "Lcom/android/systemui/statusbar/phone/PhoneStatusBarView;", "view", "progressProvider", "Lcom/android/systemui/unfold/util/ScopedUnfoldTransitionProgressProvider;", "moveFromCenterAnimationController", "Lcom/android/systemui/statusbar/phone/StatusBarMoveFromCenterAnimationController;", "userSwitcherController", "Lcom/android/systemui/statusbar/phone/userswitcher/StatusBarUserSwitcherController;", "viewUtil", "Lcom/android/systemui/util/view/ViewUtil;", "touchEventHandler", "Lcom/android/systemui/statusbar/phone/PhoneStatusBarView$TouchEventHandler;", "configurationController", "Lcom/android/systemui/statusbar/policy/ConfigurationController;", "(Lcom/android/systemui/statusbar/phone/PhoneStatusBarView;Lcom/android/systemui/unfold/util/ScopedUnfoldTransitionProgressProvider;Lcom/android/systemui/statusbar/phone/StatusBarMoveFromCenterAnimationController;Lcom/android/systemui/statusbar/phone/userswitcher/StatusBarUserSwitcherController;Lcom/android/systemui/util/view/ViewUtil;Lcom/android/systemui/statusbar/phone/PhoneStatusBarView$TouchEventHandler;Lcom/android/systemui/statusbar/policy/ConfigurationController;)V", "configurationListener", "com/android/systemui/statusbar/phone/PhoneStatusBarViewController$configurationListener$1", "Lcom/android/systemui/statusbar/phone/PhoneStatusBarViewController$configurationListener$1;", "onInit", "", "onViewAttached", "onViewDetached", "sendTouchToView", "", "ev", "Landroid/view/MotionEvent;", "setImportantForAccessibility", "mode", "", "touchIsWithinView", "x", "", "y", "Factory", "StatusBarViewsCenterProvider", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: PhoneStatusBarViewController.kt */
public final class PhoneStatusBarViewController extends ViewController<PhoneStatusBarView> {
    private final ConfigurationController configurationController;
    private final PhoneStatusBarViewController$configurationListener$1 configurationListener;
    /* access modifiers changed from: private */
    public final StatusBarMoveFromCenterAnimationController moveFromCenterAnimationController;
    private final ScopedUnfoldTransitionProgressProvider progressProvider;
    private final StatusBarUserSwitcherController userSwitcherController;
    private final ViewUtil viewUtil;

    public /* synthetic */ PhoneStatusBarViewController(PhoneStatusBarView phoneStatusBarView, ScopedUnfoldTransitionProgressProvider scopedUnfoldTransitionProgressProvider, StatusBarMoveFromCenterAnimationController statusBarMoveFromCenterAnimationController, StatusBarUserSwitcherController statusBarUserSwitcherController, ViewUtil viewUtil2, PhoneStatusBarView.TouchEventHandler touchEventHandler, ConfigurationController configurationController2, DefaultConstructorMarker defaultConstructorMarker) {
        this(phoneStatusBarView, scopedUnfoldTransitionProgressProvider, statusBarMoveFromCenterAnimationController, statusBarUserSwitcherController, viewUtil2, touchEventHandler, configurationController2);
    }

    private PhoneStatusBarViewController(PhoneStatusBarView phoneStatusBarView, @Named("unfold_status_bar") ScopedUnfoldTransitionProgressProvider scopedUnfoldTransitionProgressProvider, StatusBarMoveFromCenterAnimationController statusBarMoveFromCenterAnimationController, StatusBarUserSwitcherController statusBarUserSwitcherController, ViewUtil viewUtil2, PhoneStatusBarView.TouchEventHandler touchEventHandler, ConfigurationController configurationController2) {
        super(phoneStatusBarView);
        this.progressProvider = scopedUnfoldTransitionProgressProvider;
        this.moveFromCenterAnimationController = statusBarMoveFromCenterAnimationController;
        this.userSwitcherController = statusBarUserSwitcherController;
        this.viewUtil = viewUtil2;
        this.configurationController = configurationController2;
        this.configurationListener = new PhoneStatusBarViewController$configurationListener$1(this);
        ((PhoneStatusBarView) this.mView).setTouchEventHandler(touchEventHandler);
    }

    /* access modifiers changed from: protected */
    public void onViewAttached() {
        if (this.moveFromCenterAnimationController != null) {
            View findViewById = ((PhoneStatusBarView) this.mView).findViewById(C1893R.C1897id.status_bar_left_side);
            Intrinsics.checkNotNullExpressionValue(findViewById, "mView.findViewById(R.id.status_bar_left_side)");
            View findViewById2 = ((PhoneStatusBarView) this.mView).findViewById(C1893R.C1897id.system_icon_area);
            Intrinsics.checkNotNullExpressionValue(findViewById2, "mView.findViewById(R.id.system_icon_area)");
            ((PhoneStatusBarView) this.mView).getViewTreeObserver().addOnPreDrawListener(new PhoneStatusBarViewController$onViewAttached$1(this, new View[]{findViewById, (ViewGroup) findViewById2}));
            ((PhoneStatusBarView) this.mView).addOnLayoutChangeListener(new PhoneStatusBarViewController$$ExternalSyntheticLambda0(this));
            ScopedUnfoldTransitionProgressProvider scopedUnfoldTransitionProgressProvider = this.progressProvider;
            if (scopedUnfoldTransitionProgressProvider != null) {
                scopedUnfoldTransitionProgressProvider.setReadyToHandleTransition(true);
            }
            this.configurationController.addCallback(this.configurationListener);
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: onViewAttached$lambda-0  reason: not valid java name */
    public static final void m3188onViewAttached$lambda0(PhoneStatusBarViewController phoneStatusBarViewController, View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        Intrinsics.checkNotNullParameter(phoneStatusBarViewController, "this$0");
        if (i3 - i != i7 - i5) {
            phoneStatusBarViewController.moveFromCenterAnimationController.onStatusBarWidthChanged();
        }
    }

    /* access modifiers changed from: protected */
    public void onViewDetached() {
        ScopedUnfoldTransitionProgressProvider scopedUnfoldTransitionProgressProvider = this.progressProvider;
        if (scopedUnfoldTransitionProgressProvider != null) {
            scopedUnfoldTransitionProgressProvider.setReadyToHandleTransition(false);
        }
        StatusBarMoveFromCenterAnimationController statusBarMoveFromCenterAnimationController = this.moveFromCenterAnimationController;
        if (statusBarMoveFromCenterAnimationController != null) {
            statusBarMoveFromCenterAnimationController.onViewDetached();
        }
        this.configurationController.removeCallback(this.configurationListener);
    }

    /* access modifiers changed from: protected */
    public void onInit() {
        this.userSwitcherController.init();
    }

    public final void setImportantForAccessibility(int i) {
        ((PhoneStatusBarView) this.mView).setImportantForAccessibility(i);
    }

    public final boolean sendTouchToView(MotionEvent motionEvent) {
        Intrinsics.checkNotNullParameter(motionEvent, "ev");
        return ((PhoneStatusBarView) this.mView).dispatchTouchEvent(motionEvent);
    }

    public final boolean touchIsWithinView(float f, float f2) {
        ViewUtil viewUtil2 = this.viewUtil;
        View view = this.mView;
        Intrinsics.checkNotNullExpressionValue(view, "mView");
        return viewUtil2.touchIsWithinView(view, f, f2);
    }

    @Metadata(mo64986d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0018\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016J \u0010\t\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\n\u001a\u00020\u000bH\u0002¨\u0006\f"}, mo64987d2 = {"Lcom/android/systemui/statusbar/phone/PhoneStatusBarViewController$StatusBarViewsCenterProvider;", "Lcom/android/systemui/shared/animation/UnfoldMoveFromCenterAnimator$ViewCenterProvider;", "()V", "getViewCenter", "", "view", "Landroid/view/View;", "outPoint", "Landroid/graphics/Point;", "getViewEdgeCenter", "isStart", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: PhoneStatusBarViewController.kt */
    public static final class StatusBarViewsCenterProvider implements UnfoldMoveFromCenterAnimator.ViewCenterProvider {
        public void getViewCenter(View view, Point point) {
            Intrinsics.checkNotNullParameter(view, "view");
            Intrinsics.checkNotNullParameter(point, "outPoint");
            int id = view.getId();
            if (id == C1893R.C1897id.status_bar_left_side) {
                getViewEdgeCenter(view, point, true);
            } else if (id != C1893R.C1897id.system_icon_area) {
                super.getViewCenter(view, point);
            } else {
                getViewEdgeCenter(view, point, false);
            }
        }

        private final void getViewEdgeCenter(View view, Point point, boolean z) {
            boolean z2 = (view.getResources().getConfiguration().getLayoutDirection() == 1) ^ z;
            int[] iArr = new int[2];
            view.getLocationOnScreen(iArr);
            int i = iArr[0];
            int i2 = iArr[1];
            point.x = i + (z2 ? view.getHeight() / 2 : view.getWidth() - (view.getHeight() / 2));
            point.y = i2 + (view.getHeight() / 2);
        }
    }

    @Metadata(mo64986d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B=\b\u0007\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\u000e\b\u0001\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00060\u0003\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\n\u0012\u0006\u0010\u000b\u001a\u00020\f¢\u0006\u0002\u0010\rJ\u0016\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013R\u000e\u0010\u000b\u001a\u00020\fX\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00060\u0003X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0004¢\u0006\u0002\n\u0000¨\u0006\u0014"}, mo64987d2 = {"Lcom/android/systemui/statusbar/phone/PhoneStatusBarViewController$Factory;", "", "unfoldComponent", "Ljava/util/Optional;", "Lcom/android/systemui/unfold/SysUIUnfoldComponent;", "progressProvider", "Lcom/android/systemui/unfold/util/ScopedUnfoldTransitionProgressProvider;", "userSwitcherController", "Lcom/android/systemui/statusbar/phone/userswitcher/StatusBarUserSwitcherController;", "viewUtil", "Lcom/android/systemui/util/view/ViewUtil;", "configurationController", "Lcom/android/systemui/statusbar/policy/ConfigurationController;", "(Ljava/util/Optional;Ljava/util/Optional;Lcom/android/systemui/statusbar/phone/userswitcher/StatusBarUserSwitcherController;Lcom/android/systemui/util/view/ViewUtil;Lcom/android/systemui/statusbar/policy/ConfigurationController;)V", "create", "Lcom/android/systemui/statusbar/phone/PhoneStatusBarViewController;", "view", "Lcom/android/systemui/statusbar/phone/PhoneStatusBarView;", "touchEventHandler", "Lcom/android/systemui/statusbar/phone/PhoneStatusBarView$TouchEventHandler;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: PhoneStatusBarViewController.kt */
    public static final class Factory {
        private final ConfigurationController configurationController;
        private final Optional<ScopedUnfoldTransitionProgressProvider> progressProvider;
        private final Optional<SysUIUnfoldComponent> unfoldComponent;
        private final StatusBarUserSwitcherController userSwitcherController;
        private final ViewUtil viewUtil;

        @Inject
        public Factory(Optional<SysUIUnfoldComponent> optional, @Named("unfold_status_bar") Optional<ScopedUnfoldTransitionProgressProvider> optional2, StatusBarUserSwitcherController statusBarUserSwitcherController, ViewUtil viewUtil2, ConfigurationController configurationController2) {
            Intrinsics.checkNotNullParameter(optional, "unfoldComponent");
            Intrinsics.checkNotNullParameter(optional2, "progressProvider");
            Intrinsics.checkNotNullParameter(statusBarUserSwitcherController, "userSwitcherController");
            Intrinsics.checkNotNullParameter(viewUtil2, "viewUtil");
            Intrinsics.checkNotNullParameter(configurationController2, "configurationController");
            this.unfoldComponent = optional;
            this.progressProvider = optional2;
            this.userSwitcherController = statusBarUserSwitcherController;
            this.viewUtil = viewUtil2;
            this.configurationController = configurationController2;
        }

        public final PhoneStatusBarViewController create(PhoneStatusBarView phoneStatusBarView, PhoneStatusBarView.TouchEventHandler touchEventHandler) {
            Intrinsics.checkNotNullParameter(phoneStatusBarView, "view");
            Intrinsics.checkNotNullParameter(touchEventHandler, "touchEventHandler");
            ScopedUnfoldTransitionProgressProvider orElse = this.progressProvider.orElse(null);
            SysUIUnfoldComponent orElse2 = this.unfoldComponent.orElse(null);
            return new PhoneStatusBarViewController(phoneStatusBarView, orElse, orElse2 != null ? orElse2.getStatusBarMoveFromCenterAnimationController() : null, this.userSwitcherController, this.viewUtil, touchEventHandler, this.configurationController, (DefaultConstructorMarker) null);
        }
    }
}
