package com.android.systemui.navigationbar;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.inputmethodservice.InputMethodService;
import android.net.connectivity.com.android.net.module.util.NetworkStackConstants;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.ContextThemeWrapper;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import androidx.constraintlayout.motion.widget.Key;
import com.android.p019wm.shell.back.BackAnimation;
import com.android.p019wm.shell.pip.Pip;
import com.android.settingslib.Utils;
import com.android.systemui.C1893R;
import com.android.systemui.Gefingerpoken;
import com.android.systemui.accessibility.SystemActions$$ExternalSyntheticLambda5;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.model.SysUiState;
import com.android.systemui.navigationbar.buttons.ButtonDispatcher;
import com.android.systemui.navigationbar.buttons.ContextualButton;
import com.android.systemui.navigationbar.buttons.ContextualButtonGroup;
import com.android.systemui.navigationbar.buttons.DeadZone;
import com.android.systemui.navigationbar.buttons.KeyButtonDrawable;
import com.android.systemui.navigationbar.buttons.NearestTouchFrame;
import com.android.systemui.navigationbar.buttons.RotationContextButton;
import com.android.systemui.navigationbar.gestural.EdgeBackGestureHandler;
import com.android.systemui.recents.Recents;
import com.android.systemui.shared.rotation.FloatingRotationButton;
import com.android.systemui.shared.rotation.RotationButton;
import com.android.systemui.shared.rotation.RotationButtonController;
import com.android.systemui.shared.system.ActivityManagerWrapper;
import com.android.systemui.shared.system.QuickStepContract;
import com.android.systemui.shared.system.WindowManagerWrapper;
import com.android.systemui.statusbar.phone.AutoHideController;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import com.android.systemui.statusbar.phone.LightBarTransitionsController;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.navigationbar.NavigationBarViewEx;
import java.p026io.PrintWriter;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class NavigationBarView extends FrameLayout {
    static final boolean ALTERNATE_CAR_MODE_UI = false;
    static final boolean DEBUG = false;
    static final String TAG = "NavBarView";
    /* access modifiers changed from: private */
    public AutoHideController mAutoHideController;
    private KeyButtonDrawable mBackIcon;
    private NavigationBarTransitions mBarTransitions;
    private final SparseArray<ButtonDispatcher> mButtonDispatchers;
    private Configuration mConfiguration;
    private final ContextualButtonGroup mContextualButtonGroup;
    private int mCurrentRotation = -1;
    View mCurrentView = null;
    private int mDarkIconColor;
    private final DeadZone mDeadZone;
    int mDisabledFlags = 0;
    private KeyButtonDrawable mDockedIcon;
    private final Consumer<Boolean> mDockedListener;
    private boolean mDockedStackExists;
    private EdgeBackGestureHandler mEdgeBackGestureHandler;
    private FloatingRotationButton mFloatingRotationButton;
    private KeyButtonDrawable mHomeDefaultIcon;
    private View mHorizontal;
    private final boolean mImeCanRenderGesturalNavButtons;
    private boolean mImeDrawsImeNavBar;
    private boolean mInCarMode = false;
    private boolean mIsVertical;
    private boolean mLayoutTransitionsEnabled = true;
    private Context mLightContext;
    private int mLightIconColor;
    boolean mLongClickableAccessibilityButton;
    private int mNavBarMode;
    int mNavigationIconHints = 0;
    private NavigationBarInflaterView mNavigationInflaterView;
    private OnVerticalChangedListener mOnVerticalChangedListener;
    private boolean mOverviewProxyEnabled;
    private NotificationPanelViewController mPanelView;
    private final Consumer<Rect> mPipListener;
    private final View.AccessibilityDelegate mQuickStepAccessibilityDelegate;
    private KeyButtonDrawable mRecentIcon;
    /* access modifiers changed from: private */
    public Optional<Recents> mRecentsOptional;
    private RotationButtonController mRotationButtonController;
    private final RotationButton.RotationButtonUpdatesCallback mRotationButtonListener;
    private RotationContextButton mRotationContextButton;
    private boolean mScreenOn = true;
    private ScreenPinningNotify mScreenPinningNotify;
    private boolean mShowSwipeUpUi;
    private Configuration mTmpLastConfiguration;
    private Gefingerpoken mTouchHandler;
    private final NavTransitionListener mTransitionListener = new NavTransitionListener();
    private UpdateActiveTouchRegionsCallback mUpdateActiveTouchRegionsCallback;
    private boolean mUseCarModeUi = false;
    private View mVertical;
    private boolean mWakeAndUnlocking;

    public interface OnVerticalChangedListener {
        void onVerticalChanged(boolean z);
    }

    interface UpdateActiveTouchRegionsCallback {
        void update();
    }

    private static String visibilityToString(int i) {
        return i != 4 ? i != 8 ? "VISIBLE" : "GONE" : "INVISIBLE";
    }

    /* access modifiers changed from: protected */
    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return super.generateDefaultLayoutParams();
    }

    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return super.generateLayoutParams(attributeSet);
    }

    public /* bridge */ /* synthetic */ ViewOverlay getOverlay() {
        return super.getOverlay();
    }

    private class NavTransitionListener implements LayoutTransition.TransitionListener {
        private boolean mBackTransitioning;
        private long mDuration;
        private boolean mHomeAppearing;
        private TimeInterpolator mInterpolator;
        private long mStartDelay;

        private NavTransitionListener() {
        }

        public void startTransition(LayoutTransition layoutTransition, ViewGroup viewGroup, View view, int i) {
            if (view.getId() == C1893R.C1897id.back) {
                this.mBackTransitioning = true;
            } else if (view.getId() == C1893R.C1897id.home && i == 2) {
                this.mHomeAppearing = true;
                this.mStartDelay = layoutTransition.getStartDelay(i);
                this.mDuration = layoutTransition.getDuration(i);
                this.mInterpolator = layoutTransition.getInterpolator(i);
            }
        }

        public void endTransition(LayoutTransition layoutTransition, ViewGroup viewGroup, View view, int i) {
            if (view.getId() == C1893R.C1897id.back) {
                this.mBackTransitioning = false;
            } else if (view.getId() == C1893R.C1897id.home && i == 2) {
                this.mHomeAppearing = false;
            }
        }

        public void onBackAltCleared() {
            ButtonDispatcher backButton = NavigationBarView.this.getBackButton();
            if (!this.mBackTransitioning && backButton.getVisibility() == 0 && this.mHomeAppearing && NavigationBarView.this.getHomeButton().getAlpha() == 0.0f) {
                NavigationBarView.this.getBackButton().setAlpha(0.0f);
                ObjectAnimator ofFloat = ObjectAnimator.ofFloat(backButton, Key.ALPHA, new float[]{0.0f, 1.0f});
                ofFloat.setStartDelay(this.mStartDelay);
                ofFloat.setDuration(this.mDuration);
                ofFloat.setInterpolator(this.mInterpolator);
                ofFloat.start();
            }
        }
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public NavigationBarView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        Context context2 = context;
        SparseArray<ButtonDispatcher> sparseArray = new SparseArray<>();
        this.mButtonDispatchers = sparseArray;
        this.mRecentsOptional = Optional.empty();
        this.mImeCanRenderGesturalNavButtons = InputMethodService.canImeRenderGesturalNavButtons();
        this.mQuickStepAccessibilityDelegate = new View.AccessibilityDelegate() {
            private AccessibilityNodeInfo.AccessibilityAction mToggleOverviewAction;

            public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
                super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
                if (this.mToggleOverviewAction == null) {
                    this.mToggleOverviewAction = new AccessibilityNodeInfo.AccessibilityAction(C1893R.C1897id.action_toggle_overview, NavigationBarView.this.getContext().getString(C1893R.string.quick_step_accessibility_toggle_overview));
                }
                accessibilityNodeInfo.addAction(this.mToggleOverviewAction);
            }

            public boolean performAccessibilityAction(View view, int i, Bundle bundle) {
                if (i != C1893R.C1897id.action_toggle_overview) {
                    return super.performAccessibilityAction(view, i, bundle);
                }
                NavigationBarView.this.mRecentsOptional.ifPresent(new SystemActions$$ExternalSyntheticLambda5());
                return true;
            }
        };
        this.mRotationButtonListener = new RotationButton.RotationButtonUpdatesCallback() {
            public void onVisibilityChanged(boolean z) {
                if (z && NavigationBarView.this.mAutoHideController != null) {
                    NavigationBarView.this.mAutoHideController.touchAutoHide();
                }
                NavigationBarView.this.notifyActiveTouchRegions();
            }

            public void onPositionChanged() {
                NavigationBarView.this.notifyActiveTouchRegions();
            }
        };
        this.mDockedListener = new NavigationBarView$$ExternalSyntheticLambda0(this);
        this.mPipListener = new NavigationBarView$$ExternalSyntheticLambda1(this);
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(context2, Utils.getThemeAttr(context2, C1893R.attr.darkIconTheme));
        ContextThemeWrapper contextThemeWrapper2 = new ContextThemeWrapper(context2, Utils.getThemeAttr(context2, C1893R.attr.lightIconTheme));
        this.mLightContext = contextThemeWrapper2;
        this.mLightIconColor = Utils.getColorAttrDefaultColor(contextThemeWrapper2, C1893R.attr.singleToneColor);
        this.mDarkIconColor = Utils.getColorAttrDefaultColor(contextThemeWrapper, C1893R.attr.singleToneColor);
        this.mIsVertical = false;
        this.mLongClickableAccessibilityButton = false;
        ContextualButtonGroup contextualButtonGroup = new ContextualButtonGroup(C1893R.C1897id.menu_container);
        this.mContextualButtonGroup = contextualButtonGroup;
        ContextualButton contextualButton = new ContextualButton(C1893R.C1897id.ime_switcher, this.mLightContext, C1893R.C1895drawable.ic_ime_switcher_default);
        ContextualButton contextualButton2 = new ContextualButton(C1893R.C1897id.accessibility_button, this.mLightContext, C1893R.C1895drawable.ic_sysbar_accessibility_button);
        contextualButtonGroup.addButton(contextualButton);
        contextualButtonGroup.addButton(contextualButton2);
        this.mRotationContextButton = new RotationContextButton(C1893R.C1897id.rotate_suggestion, this.mLightContext, C1893R.C1895drawable.ic_sysbar_rotate_button_ccw_start_0);
        this.mFloatingRotationButton = new FloatingRotationButton(this.mContext, C1893R.string.accessibility_rotate_button, C1893R.layout.rotate_suggestion, C1893R.C1897id.rotate_suggestion, C1893R.dimen.floating_rotation_button_min_margin, C1893R.dimen.rounded_corner_content_padding, C1893R.dimen.floating_rotation_button_taskbar_left_margin, C1893R.dimen.floating_rotation_button_taskbar_bottom_margin, C1893R.dimen.floating_rotation_button_diameter, C1893R.dimen.key_button_ripple_max_width);
        this.mRotationButtonController = new RotationButtonController(this.mLightContext, this.mLightIconColor, this.mDarkIconColor, C1893R.C1895drawable.ic_sysbar_rotate_button_ccw_start_0, C1893R.C1895drawable.ic_sysbar_rotate_button_ccw_start_90, C1893R.C1895drawable.ic_sysbar_rotate_button_cw_start_0, C1893R.C1895drawable.ic_sysbar_rotate_button_cw_start_90, new NavigationBarView$$ExternalSyntheticLambda2(this));
        this.mConfiguration = new Configuration();
        this.mTmpLastConfiguration = new Configuration();
        this.mConfiguration.updateFrom(context.getResources().getConfiguration());
        this.mScreenPinningNotify = new ScreenPinningNotify(this.mContext);
        sparseArray.put(C1893R.C1897id.back, new ButtonDispatcher(C1893R.C1897id.back));
        sparseArray.put(C1893R.C1897id.home, new ButtonDispatcher(C1893R.C1897id.home));
        sparseArray.put(C1893R.C1897id.home_handle, new ButtonDispatcher(C1893R.C1897id.home_handle));
        sparseArray.put(C1893R.C1897id.recent_apps, new ButtonDispatcher(C1893R.C1897id.recent_apps));
        sparseArray.put(C1893R.C1897id.ime_switcher, contextualButton);
        sparseArray.put(C1893R.C1897id.accessibility_button, contextualButton2);
        sparseArray.put(C1893R.C1897id.menu_container, contextualButtonGroup);
        this.mDeadZone = new DeadZone(this);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-systemui-navigationbar-NavigationBarView */
    public /* synthetic */ Integer mo34785x59ac49cb() {
        return Integer.valueOf(this.mCurrentRotation);
    }

    public void setEdgeBackGestureHandler(EdgeBackGestureHandler edgeBackGestureHandler) {
        this.mEdgeBackGestureHandler = edgeBackGestureHandler;
    }

    /* access modifiers changed from: package-private */
    public void setBarTransitions(NavigationBarTransitions navigationBarTransitions) {
        this.mBarTransitions = navigationBarTransitions;
    }

    public void setAutoHideController(AutoHideController autoHideController) {
        this.mAutoHideController = autoHideController;
    }

    public LightBarTransitionsController getLightTransitionsController() {
        return this.mBarTransitions.getLightTransitionsController();
    }

    public void setComponents(Optional<Recents> optional) {
        this.mRecentsOptional = optional;
    }

    public void setComponents(NotificationPanelViewController notificationPanelViewController) {
        this.mPanelView = notificationPanelViewController;
        updatePanelSystemUiStateFlags();
    }

    public void setOnVerticalChangedListener(OnVerticalChangedListener onVerticalChangedListener) {
        this.mOnVerticalChangedListener = onVerticalChangedListener;
        notifyVerticalChangedListener(this.mIsVertical);
    }

    public void setTouchHandler(Gefingerpoken gefingerpoken) {
        this.mTouchHandler = gefingerpoken;
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        return this.mTouchHandler.onInterceptTouchEvent(motionEvent) || super.onInterceptTouchEvent(motionEvent);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        this.mTouchHandler.onTouchEvent(motionEvent);
        return super.onTouchEvent(motionEvent);
    }

    public void abortCurrentGesture() {
        getHomeButton().abortCurrentGesture();
    }

    public View getCurrentView() {
        return this.mCurrentView;
    }

    public void forEachView(Consumer<View> consumer) {
        View view = this.mVertical;
        if (view != null) {
            consumer.accept(view);
        }
        View view2 = this.mHorizontal;
        if (view2 != null) {
            consumer.accept(view2);
        }
    }

    public RotationButtonController getRotationButtonController() {
        return this.mRotationButtonController;
    }

    public FloatingRotationButton getFloatingRotationButton() {
        return this.mFloatingRotationButton;
    }

    public ButtonDispatcher getRecentsButton() {
        return this.mButtonDispatchers.get(C1893R.C1897id.recent_apps);
    }

    public ButtonDispatcher getBackButton() {
        return this.mButtonDispatchers.get(C1893R.C1897id.back);
    }

    public ButtonDispatcher getHomeButton() {
        return this.mButtonDispatchers.get(C1893R.C1897id.home);
    }

    public ButtonDispatcher getImeSwitchButton() {
        return this.mButtonDispatchers.get(C1893R.C1897id.ime_switcher);
    }

    public ButtonDispatcher getAccessibilityButton() {
        return this.mButtonDispatchers.get(C1893R.C1897id.accessibility_button);
    }

    public RotationContextButton getRotateSuggestionButton() {
        return (RotationContextButton) this.mButtonDispatchers.get(C1893R.C1897id.rotate_suggestion);
    }

    public ButtonDispatcher getHomeHandle() {
        return this.mButtonDispatchers.get(C1893R.C1897id.home_handle);
    }

    public SparseArray<ButtonDispatcher> getButtonDispatchers() {
        return this.mButtonDispatchers;
    }

    public boolean isRecentsButtonVisible() {
        return getRecentsButton().getVisibility() == 0;
    }

    public boolean isOverviewEnabled() {
        return (this.mDisabledFlags & 16777216) == 0;
    }

    private boolean isQuickStepSwipeUpEnabled() {
        return this.mShowSwipeUpUi && isOverviewEnabled();
    }

    private void reloadNavIcons() {
        updateIcons(Configuration.EMPTY);
    }

    private void updateIcons(Configuration configuration) {
        boolean z = true;
        boolean z2 = configuration.orientation != this.mConfiguration.orientation;
        boolean z3 = configuration.densityDpi != this.mConfiguration.densityDpi;
        if (configuration.getLayoutDirection() == this.mConfiguration.getLayoutDirection()) {
            z = false;
        }
        if (z2 || z3) {
            this.mDockedIcon = getDrawable(C1893R.C1895drawable.ic_sysbar_docked);
            this.mHomeDefaultIcon = getHomeDrawable();
        }
        if (z3 || z) {
            this.mRecentIcon = getDrawable(C1893R.C1895drawable.ic_sysbar_recent);
            this.mContextualButtonGroup.updateIcons(this.mLightIconColor, this.mDarkIconColor);
        }
        if (z2 || z3 || z) {
            this.mBackIcon = getBackDrawable();
        }
    }

    /* access modifiers changed from: package-private */
    public void updateRotationButton() {
        if (QuickStepContract.isGesturalMode(this.mNavBarMode)) {
            this.mContextualButtonGroup.removeButton(C1893R.C1897id.rotate_suggestion);
            this.mButtonDispatchers.remove(C1893R.C1897id.rotate_suggestion);
            this.mRotationButtonController.setRotationButton(this.mFloatingRotationButton, this.mRotationButtonListener);
        } else if (this.mContextualButtonGroup.getContextButton(C1893R.C1897id.rotate_suggestion) == null) {
            this.mContextualButtonGroup.addButton(this.mRotationContextButton);
            this.mButtonDispatchers.put(C1893R.C1897id.rotate_suggestion, this.mRotationContextButton);
            this.mRotationButtonController.setRotationButton(this.mRotationContextButton, this.mRotationButtonListener);
        }
        this.mNavigationInflaterView.setButtonDispatchers(this.mButtonDispatchers);
    }

    public KeyButtonDrawable getBackDrawable() {
        KeyButtonDrawable drawable = getDrawable(getBackDrawableRes());
        orientBackButton(drawable);
        return drawable;
    }

    public int getBackDrawableRes() {
        return chooseNavigationIconDrawableRes(C1893R.C1895drawable.ic_sysbar_back, C1893R.C1895drawable.ic_sysbar_back_quick_step);
    }

    public KeyButtonDrawable getHomeDrawable() {
        KeyButtonDrawable keyButtonDrawable;
        if (this.mShowSwipeUpUi) {
            keyButtonDrawable = getDrawable(C1893R.C1895drawable.ic_sysbar_home_quick_step);
        } else {
            keyButtonDrawable = getDrawable(C1893R.C1895drawable.ic_sysbar_home);
        }
        orientHomeButton(keyButtonDrawable);
        return keyButtonDrawable;
    }

    private void orientBackButton(KeyButtonDrawable keyButtonDrawable) {
        float f;
        boolean z = (this.mNavigationIconHints & 1) != 0;
        boolean z2 = this.mConfiguration.getLayoutDirection() == 1;
        float f2 = 0.0f;
        if (z) {
            f = (float) (z2 ? 90 : -90);
        } else {
            f = 0.0f;
        }
        if (keyButtonDrawable.getRotation() != f) {
            if (QuickStepContract.isGesturalMode(this.mNavBarMode)) {
                keyButtonDrawable.setRotation(f);
                return;
            }
            if (!this.mShowSwipeUpUi && !this.mIsVertical && z) {
                f2 = -getResources().getDimension(C1893R.dimen.navbar_back_button_ime_offset);
            }
            ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(keyButtonDrawable, new PropertyValuesHolder[]{PropertyValuesHolder.ofFloat(KeyButtonDrawable.KEY_DRAWABLE_ROTATE, new float[]{f}), PropertyValuesHolder.ofFloat(KeyButtonDrawable.KEY_DRAWABLE_TRANSLATE_Y, new float[]{f2})});
            ofPropertyValuesHolder.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
            ofPropertyValuesHolder.setDuration(200);
            ofPropertyValuesHolder.start();
        }
    }

    private void orientHomeButton(KeyButtonDrawable keyButtonDrawable) {
        keyButtonDrawable.setRotation(this.mIsVertical ? 90.0f : 0.0f);
    }

    private int chooseNavigationIconDrawableRes(int i, int i2) {
        return this.mShowSwipeUpUi ? i2 : i;
    }

    private KeyButtonDrawable getDrawable(int i) {
        return KeyButtonDrawable.create(this.mLightContext, this.mLightIconColor, this.mDarkIconColor, i, true, (Color) null);
    }

    public void onScreenStateChanged(boolean z) {
        this.mScreenOn = z;
    }

    public void setWindowVisible(boolean z) {
        this.mRotationButtonController.onNavigationBarWindowVisibilityChange(z);
    }

    public void setBehavior(int i) {
        this.mRotationButtonController.onBehaviorChanged(0, i);
    }

    public void setLayoutDirection(int i) {
        reloadNavIcons();
        super.setLayoutDirection(i);
    }

    /* access modifiers changed from: package-private */
    public void setNavigationIconHints(int i) {
        if (i != this.mNavigationIconHints) {
            this.mNavigationIconHints = i;
            updateNavButtonIcons();
        }
    }

    /* access modifiers changed from: package-private */
    public void onImeVisibilityChanged(boolean z) {
        if (!z) {
            this.mTransitionListener.onBackAltCleared();
        }
        this.mRotationButtonController.getRotationButton().setCanShowRotationButton(!z);
    }

    /* access modifiers changed from: package-private */
    public void setDisabledFlags(int i, SysUiState sysUiState) {
        if (this.mDisabledFlags != i) {
            boolean isOverviewEnabled = isOverviewEnabled();
            this.mDisabledFlags = i;
            if (!isOverviewEnabled && isOverviewEnabled()) {
                reloadNavIcons();
            }
            updateNavButtonIcons();
            updateSlippery();
            updateDisabledSystemUiStateFlags(sysUiState);
        }
    }

    public void updateNavButtonIcons() {
        LayoutTransition layoutTransition;
        int i = 0;
        boolean z = (this.mNavigationIconHints & 1) != 0;
        KeyButtonDrawable keyButtonDrawable = this.mBackIcon;
        orientBackButton(keyButtonDrawable);
        KeyButtonDrawable keyButtonDrawable2 = this.mHomeDefaultIcon;
        if (!this.mUseCarModeUi) {
            orientHomeButton(keyButtonDrawable2);
        }
        getHomeButton().setImageDrawable(keyButtonDrawable2);
        getBackButton().setImageDrawable(keyButtonDrawable);
        updateRecentsIcon();
        this.mContextualButtonGroup.setButtonVisibility(C1893R.C1897id.ime_switcher, !((this.mNavigationIconHints & 4) == 0 || isImeRenderingNavButtons()));
        this.mBarTransitions.reapplyDarkIntensity();
        boolean z2 = QuickStepContract.isGesturalMode(this.mNavBarMode) || (this.mDisabledFlags & 2097152) != 0;
        boolean isRecentsButtonDisabled = isRecentsButtonDisabled();
        boolean z3 = isRecentsButtonDisabled && (2097152 & this.mDisabledFlags) != 0;
        boolean z4 = (!z && (this.mEdgeBackGestureHandler.isHandlingGestures() || (this.mDisabledFlags & 4194304) != 0)) || isImeRenderingNavButtons();
        boolean isScreenPinningActive = ActivityManagerWrapper.getInstance().isScreenPinningActive();
        if (this.mOverviewProxyEnabled) {
            isRecentsButtonDisabled |= true ^ QuickStepContract.isLegacyMode(this.mNavBarMode);
            if (isScreenPinningActive && !QuickStepContract.isGesturalMode(this.mNavBarMode)) {
                z4 = false;
                z2 = false;
            }
        } else if (isScreenPinningActive) {
            z4 = false;
            isRecentsButtonDisabled = false;
        }
        ViewGroup viewGroup = (ViewGroup) getCurrentView().findViewById(C1893R.C1897id.nav_buttons);
        if (!(viewGroup == null || (layoutTransition = viewGroup.getLayoutTransition()) == null || layoutTransition.getTransitionListeners().contains(this.mTransitionListener))) {
            layoutTransition.addTransitionListener(this.mTransitionListener);
        }
        getBackButton().setVisibility(z4 ? 4 : 0);
        getHomeButton().setVisibility(z2 ? 4 : 0);
        getRecentsButton().setVisibility(isRecentsButtonDisabled ? 4 : 0);
        ButtonDispatcher homeHandle = getHomeHandle();
        if (z3) {
            i = 4;
        }
        homeHandle.setVisibility(i);
        notifyActiveTouchRegions();
    }

    /* access modifiers changed from: package-private */
    public boolean isImeRenderingNavButtons() {
        return this.mImeDrawsImeNavBar && this.mImeCanRenderGesturalNavButtons && (this.mNavigationIconHints & 2) != 0;
    }

    /* access modifiers changed from: package-private */
    public boolean isRecentsButtonDisabled() {
        return this.mUseCarModeUi || !isOverviewEnabled() || getContext().getDisplayId() != 0;
    }

    private Display getContextDisplay() {
        return getContext().getDisplay();
    }

    public void setLayoutTransitionsEnabled(boolean z) {
        this.mLayoutTransitionsEnabled = z;
        updateLayoutTransitionsEnabled();
    }

    public void setWakeAndUnlocking(boolean z) {
        setUseFadingAnimations(z);
        this.mWakeAndUnlocking = z;
        updateLayoutTransitionsEnabled();
    }

    private void updateLayoutTransitionsEnabled() {
        boolean z = !this.mWakeAndUnlocking && this.mLayoutTransitionsEnabled;
        LayoutTransition layoutTransition = ((ViewGroup) getCurrentView().findViewById(C1893R.C1897id.nav_buttons)).getLayoutTransition();
        if (layoutTransition == null) {
            return;
        }
        if (z) {
            layoutTransition.enableTransitionType(2);
            layoutTransition.enableTransitionType(3);
            layoutTransition.enableTransitionType(0);
            layoutTransition.enableTransitionType(1);
            return;
        }
        layoutTransition.disableTransitionType(2);
        layoutTransition.disableTransitionType(3);
        layoutTransition.disableTransitionType(0);
        layoutTransition.disableTransitionType(1);
    }

    private void setUseFadingAnimations(boolean z) {
        WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) ((ViewGroup) getParent()).getLayoutParams();
        if (layoutParams != null) {
            boolean z2 = layoutParams.windowAnimations != 0;
            if (!z2 && z) {
                layoutParams.windowAnimations = C1893R.style.Animation_NavigationBarFadeIn;
            } else if (z2 && !z) {
                layoutParams.windowAnimations = 0;
            } else {
                return;
            }
            ((WindowManager) getContext().getSystemService(WindowManager.class)).updateViewLayout((View) getParent(), layoutParams);
        }
    }

    public void onStatusBarPanelStateChanged() {
        updateSlippery();
    }

    public void updateDisabledSystemUiStateFlags(SysUiState sysUiState) {
        int displayId = this.mContext.getDisplayId();
        boolean z = true;
        SysUiState flag = sysUiState.setFlag(1, ActivityManagerWrapper.getInstance().isScreenPinningActive()).setFlag(128, (this.mDisabledFlags & 16777216) != 0).setFlag(256, (this.mDisabledFlags & 2097152) != 0);
        if ((this.mDisabledFlags & QuickStepContract.SYSUI_STATE_VOICE_INTERACTION_WINDOW_SHOWING) == 0) {
            z = false;
        }
        flag.setFlag(1024, z).commitUpdate(displayId);
    }

    private void updatePanelSystemUiStateFlags() {
        NotificationPanelViewController notificationPanelViewController = this.mPanelView;
        if (notificationPanelViewController != null) {
            notificationPanelViewController.updateSystemUiStateFlags();
        }
    }

    /* access modifiers changed from: package-private */
    public void onOverviewProxyConnectionChange(boolean z) {
        this.mOverviewProxyEnabled = z;
    }

    /* access modifiers changed from: package-private */
    public void setShouldShowSwipeUpUi(boolean z) {
        this.mShowSwipeUpUi = z;
        updateStates();
    }

    public void updateStates() {
        NavigationBarInflaterView navigationBarInflaterView = this.mNavigationInflaterView;
        if (navigationBarInflaterView != null) {
            navigationBarInflaterView.onLikelyDefaultLayoutChange();
        }
        updateSlippery();
        reloadNavIcons();
        updateNavButtonIcons();
        WindowManagerWrapper.getInstance().setNavBarVirtualKeyHapticFeedbackEnabled(!this.mShowSwipeUpUi);
        getHomeButton().setAccessibilityDelegate(this.mShowSwipeUpUi ? this.mQuickStepAccessibilityDelegate : null);
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0006, code lost:
        r0 = r1.mPanelView;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void updateSlippery() {
        /*
            r1 = this;
            boolean r0 = r1.isQuickStepSwipeUpEnabled()
            if (r0 == 0) goto L_0x001b
            com.android.systemui.statusbar.phone.NotificationPanelViewController r0 = r1.mPanelView
            if (r0 == 0) goto L_0x0019
            boolean r0 = r0.isFullyExpanded()
            if (r0 == 0) goto L_0x0019
            com.android.systemui.statusbar.phone.NotificationPanelViewController r0 = r1.mPanelView
            boolean r0 = r0.isCollapsing()
            if (r0 != 0) goto L_0x0019
            goto L_0x001b
        L_0x0019:
            r0 = 0
            goto L_0x001c
        L_0x001b:
            r0 = 1
        L_0x001c:
            r1.setSlippery(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.navigationbar.NavigationBarView.updateSlippery():void");
    }

    /* access modifiers changed from: package-private */
    public void setSlippery(boolean z) {
        setWindowFlag(NetworkStackConstants.NEIGHBOR_ADVERTISEMENT_FLAG_OVERRIDE, z);
    }

    private void setWindowFlag(int i, boolean z) {
        WindowManager.LayoutParams layoutParams;
        ViewGroup viewGroup = (ViewGroup) getParent();
        if (viewGroup != null && (layoutParams = (WindowManager.LayoutParams) viewGroup.getLayoutParams()) != null) {
            if (z != ((layoutParams.flags & i) != 0)) {
                if (z) {
                    layoutParams.flags = i | layoutParams.flags;
                } else {
                    layoutParams.flags = (~i) & layoutParams.flags;
                }
                ((WindowManager) getContext().getSystemService(WindowManager.class)).updateViewLayout(viewGroup, layoutParams);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void setNavBarMode(int i, boolean z) {
        this.mNavBarMode = i;
        this.mImeDrawsImeNavBar = z;
        this.mBarTransitions.onNavigationModeChanged(i);
        this.mEdgeBackGestureHandler.onNavigationModeChanged(this.mNavBarMode);
        updateRotationButton();
    }

    public void setAccessibilityButtonState(boolean z, boolean z2) {
        this.mLongClickableAccessibilityButton = z2;
        getAccessibilityButton().setLongClickable(z2);
        this.mContextualButtonGroup.setButtonVisibility(C1893R.C1897id.accessibility_button, z);
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        NavigationBarInflaterView navigationBarInflaterView = (NavigationBarInflaterView) findViewById(C1893R.C1897id.navigation_inflater);
        this.mNavigationInflaterView = navigationBarInflaterView;
        navigationBarInflaterView.setButtonDispatchers(this.mButtonDispatchers);
        updateOrientationViews();
        reloadNavIcons();
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        this.mDeadZone.onDraw(canvas);
        super.onDraw(canvas);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        notifyActiveTouchRegions();
    }

    public void notifyActiveTouchRegions() {
        UpdateActiveTouchRegionsCallback updateActiveTouchRegionsCallback = this.mUpdateActiveTouchRegionsCallback;
        if (updateActiveTouchRegionsCallback != null) {
            updateActiveTouchRegionsCallback.update();
        }
    }

    /* access modifiers changed from: package-private */
    public void setUpdateActiveTouchRegionsCallback(UpdateActiveTouchRegionsCallback updateActiveTouchRegionsCallback) {
        this.mUpdateActiveTouchRegionsCallback = updateActiveTouchRegionsCallback;
        notifyActiveTouchRegions();
    }

    /* access modifiers changed from: package-private */
    public Map<View, Rect> getButtonTouchRegionCache() {
        FrameLayout frameLayout;
        if (this.mIsVertical) {
            frameLayout = this.mNavigationInflaterView.mVertical;
        } else {
            frameLayout = this.mNavigationInflaterView.mHorizontal;
        }
        return ((NearestTouchFrame) frameLayout.findViewById(C1893R.C1897id.nav_buttons)).getFullTouchableChildRegions();
    }

    private void updateOrientationViews() {
        this.mHorizontal = findViewById(C1893R.C1897id.horizontal);
        this.mVertical = findViewById(C1893R.C1897id.vertical);
        updateCurrentView();
    }

    /* access modifiers changed from: package-private */
    public boolean needsReorient(int i) {
        return this.mCurrentRotation != i;
    }

    private void updateCurrentRotation() {
        int displayRotation = this.mConfiguration.windowConfiguration.getDisplayRotation();
        if (this.mCurrentRotation != displayRotation) {
            this.mCurrentRotation = displayRotation;
            NavigationBarInflaterView navigationBarInflaterView = this.mNavigationInflaterView;
            boolean z = true;
            if (displayRotation != 1) {
                z = false;
            }
            navigationBarInflaterView.setAlternativeOrder(z);
            this.mDeadZone.onConfigurationChanged(this.mCurrentRotation);
        }
    }

    private void updateCurrentView() {
        resetViews();
        View view = this.mIsVertical ? this.mVertical : this.mHorizontal;
        this.mCurrentView = view;
        view.setVisibility(0);
        this.mNavigationInflaterView.setVertical(this.mIsVertical);
        this.mNavigationInflaterView.updateButtonDispatchersCurrentView();
        updateLayoutTransitionsEnabled();
        updateCurrentRotation();
    }

    private void resetViews() {
        this.mHorizontal.setVisibility(8);
        this.mVertical.setVisibility(8);
    }

    private void updateRecentsIcon() {
        this.mDockedIcon.setRotation((!this.mDockedStackExists || !this.mIsVertical) ? 0.0f : 90.0f);
        getRecentsButton().setImageDrawable(this.mDockedStackExists ? this.mDockedIcon : this.mRecentIcon);
        this.mBarTransitions.reapplyDarkIntensity();
    }

    public void showPinningEnterExitToast(boolean z) {
        if (z) {
            this.mScreenPinningNotify.showPinningStartToast();
        } else {
            this.mScreenPinningNotify.showPinningExitToast();
        }
    }

    public void showPinningEscapeToast() {
        this.mScreenPinningNotify.showEscapeToast(this.mNavBarMode == 2, isRecentsButtonVisible());
    }

    public boolean isVertical() {
        return this.mIsVertical;
    }

    public void reorient() {
        updateCurrentView();
        ((NavigationBarFrame) getRootView()).setDeadZone(this.mDeadZone);
        this.mBarTransitions.init();
        if (!isLayoutDirectionResolved()) {
            resolveLayoutDirection();
        }
        updateNavButtonIcons();
        getHomeButton().setVertical(this.mIsVertical);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        int i3;
        int size = View.MeasureSpec.getSize(i);
        int size2 = View.MeasureSpec.getSize(i2);
        boolean z = size > 0 && size2 > size && !QuickStepContract.isGesturalMode(this.mNavBarMode);
        if (z != this.mIsVertical) {
            this.mIsVertical = z;
            reorient();
            notifyVerticalChangedListener(z);
        }
        if (QuickStepContract.isGesturalMode(this.mNavBarMode)) {
            if (this.mIsVertical) {
                i3 = getResources().getDimensionPixelSize(17105361);
            } else {
                i3 = getResources().getDimensionPixelSize(17105359);
            }
            this.mBarTransitions.setBackgroundFrame(new Rect(0, getResources().getDimensionPixelSize(17105355) - i3, size, size2));
        } else {
            this.mBarTransitions.setBackgroundFrame((Rect) null);
        }
        super.onMeasure(i, i2);
    }

    /* access modifiers changed from: package-private */
    public int getNavBarHeight() {
        if (this.mIsVertical) {
            return getResources().getDimensionPixelSize(17105361);
        }
        return getResources().getDimensionPixelSize(17105359);
    }

    private void notifyVerticalChangedListener(boolean z) {
        OnVerticalChangedListener onVerticalChangedListener = this.mOnVerticalChangedListener;
        if (onVerticalChangedListener != null) {
            onVerticalChangedListener.onVerticalChanged(z);
        }
    }

    /* access modifiers changed from: protected */
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mTmpLastConfiguration.updateFrom(this.mConfiguration);
        this.mFloatingRotationButton.onConfigurationChanged(this.mConfiguration.updateFrom(configuration));
        boolean updateCarMode = updateCarMode();
        updateIcons(this.mTmpLastConfiguration);
        updateRecentsIcon();
        updateCurrentRotation();
        this.mEdgeBackGestureHandler.onConfigurationChanged(this.mConfiguration);
        if (updateCarMode || this.mTmpLastConfiguration.densityDpi != this.mConfiguration.densityDpi || this.mTmpLastConfiguration.getLayoutDirection() != this.mConfiguration.getLayoutDirection()) {
            updateNavButtonIcons();
        }
    }

    private boolean updateCarMode() {
        Configuration configuration = this.mConfiguration;
        if (configuration != null) {
            boolean z = (configuration.uiMode & 15) == 3;
            if (z != this.mInCarMode) {
                this.mInCarMode = z;
                this.mUseCarModeUi = false;
            }
        }
        return false;
    }

    private String getResourceName(int i) {
        if (i == 0) {
            return "(null)";
        }
        try {
            return getContext().getResources().getResourceName(i);
        } catch (Resources.NotFoundException unused) {
            return "(unknown)";
        }
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mEdgeBackGestureHandler.onNavBarAttached();
        requestApplyInsets();
        reorient();
        RotationButtonController rotationButtonController = this.mRotationButtonController;
        if (rotationButtonController != null) {
            rotationButtonController.registerListeners();
        }
        updateNavButtonIcons();
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        for (int i = 0; i < this.mButtonDispatchers.size(); i++) {
            this.mButtonDispatchers.valueAt(i).onDestroy();
        }
        if (this.mRotationButtonController != null) {
            this.mFloatingRotationButton.hide();
            this.mRotationButtonController.unregisterListeners();
        }
        this.mEdgeBackGestureHandler.onNavBarDetached();
    }

    /* access modifiers changed from: package-private */
    public void dump(PrintWriter printWriter) {
        Rect rect = new Rect();
        Point point = new Point();
        getContextDisplay().getRealSize(point);
        printWriter.println("NavigationBarView:");
        printWriter.println(String.format("      this: " + CentralSurfaces.viewInfo(this) + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + visibilityToString(getVisibility()), new Object[0]));
        getWindowVisibleDisplayFrame(rect);
        printWriter.println("      window: " + rect.toShortString() + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + visibilityToString(getWindowVisibility()) + (rect.right > point.x || rect.bottom > point.y ? " OFFSCREEN!" : ""));
        printWriter.println(String.format("      mCurrentView: id=%s (%dx%d) %s %f", getResourceName(getCurrentView().getId()), Integer.valueOf(getCurrentView().getWidth()), Integer.valueOf(getCurrentView().getHeight()), visibilityToString(getCurrentView().getVisibility()), Float.valueOf(getCurrentView().getAlpha())));
        Object[] objArr = new Object[3];
        objArr[0] = Integer.valueOf(this.mDisabledFlags);
        objArr[1] = this.mIsVertical ? "true" : "false";
        objArr[2] = Float.valueOf(getLightTransitionsController().getCurrentDarkIntensity());
        printWriter.println(String.format("      disabled=0x%08x vertical=%s darkIntensity=%.2f", objArr));
        printWriter.println("    mScreenOn: " + this.mScreenOn);
        dumpButton(printWriter, NavigationBarInflaterView.BACK, getBackButton());
        dumpButton(printWriter, NavigationBarInflaterView.HOME, getHomeButton());
        dumpButton(printWriter, "handle", getHomeHandle());
        dumpButton(printWriter, "rcnt", getRecentsButton());
        dumpButton(printWriter, "rota", getRotateSuggestionButton());
        dumpButton(printWriter, "a11y", getAccessibilityButton());
        dumpButton(printWriter, "ime", getImeSwitchButton());
        NavigationBarInflaterView navigationBarInflaterView = this.mNavigationInflaterView;
        if (navigationBarInflaterView != null) {
            navigationBarInflaterView.dump(printWriter);
        }
        this.mBarTransitions.dump(printWriter);
        this.mContextualButtonGroup.dump(printWriter);
        this.mEdgeBackGestureHandler.dump(printWriter);
    }

    public WindowInsets onApplyWindowInsets(WindowInsets windowInsets) {
        int systemWindowInsetLeft = windowInsets.getSystemWindowInsetLeft();
        int systemWindowInsetRight = windowInsets.getSystemWindowInsetRight();
        setPadding(systemWindowInsetLeft, windowInsets.getSystemWindowInsetTop(), systemWindowInsetRight, windowInsets.getSystemWindowInsetBottom());
        this.mEdgeBackGestureHandler.setInsets(systemWindowInsetLeft, systemWindowInsetRight);
        boolean z = !QuickStepContract.isGesturalMode(this.mNavBarMode) || windowInsets.getSystemWindowInsetBottom() == 0;
        setClipChildren(z);
        setClipToPadding(z);
        return super.onApplyWindowInsets(windowInsets);
    }

    /* access modifiers changed from: package-private */
    public void addPipExclusionBoundsChangeListener(Pip pip) {
        pip.addPipExclusionBoundsChangeListener(this.mPipListener);
    }

    /* access modifiers changed from: package-private */
    public void removePipExclusionBoundsChangeListener(Pip pip) {
        pip.removePipExclusionBoundsChangeListener(this.mPipListener);
    }

    /* access modifiers changed from: package-private */
    public void registerBackAnimation(BackAnimation backAnimation) {
        this.mEdgeBackGestureHandler.setBackAnimation(backAnimation);
    }

    private static void dumpButton(PrintWriter printWriter, String str, ButtonDispatcher buttonDispatcher) {
        printWriter.print("      " + str + ": ");
        if (buttonDispatcher == null) {
            printWriter.print("null");
        } else {
            printWriter.print(visibilityToString(buttonDispatcher.getVisibility()) + " alpha=" + buttonDispatcher.getAlpha());
        }
        printWriter.println();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$2$com-android-systemui-navigationbar-NavigationBarView */
    public /* synthetic */ void mo34787x8de34709(Boolean bool) {
        post(new NavigationBarView$$ExternalSyntheticLambda3(this, bool));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$1$com-android-systemui-navigationbar-NavigationBarView */
    public /* synthetic */ void mo34786x73c7c86a(Boolean bool) {
        this.mDockedStackExists = bool.booleanValue();
        updateRecentsIcon();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$4$com-android-systemui-navigationbar-NavigationBarView */
    public /* synthetic */ void mo34789xc21a4447(Rect rect) {
        post(new NavigationBarView$$ExternalSyntheticLambda4(this, rect));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$3$com-android-systemui-navigationbar-NavigationBarView */
    public /* synthetic */ void mo34788xa7fec5a8(Rect rect) {
        this.mEdgeBackGestureHandler.setPipStashExclusionBounds(rect);
    }

    public void updateNavBarCombination() {
        ((NavigationBarViewEx) NTDependencyEx.get(NavigationBarViewEx.class)).updateNavBarCombination(this.mNavigationInflaterView);
    }
}
