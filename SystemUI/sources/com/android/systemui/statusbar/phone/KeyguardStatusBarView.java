package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.DisplayCutout;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.WindowInsets;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.systemui.C1893R;
import com.android.systemui.ScreenDecorations;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.battery.BatteryMeterView;
import com.android.systemui.plugins.DarkIconDispatcher;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import com.android.systemui.util.Utils;
import com.nothing.systemui.util.NTColorUtil;
import java.p026io.PrintWriter;
import java.util.ArrayList;

public class KeyguardStatusBarView extends RelativeLayout {
    private static final int LAYOUT_CUTOUT = 1;
    private static final int LAYOUT_NONE = 0;
    private static final int LAYOUT_NO_CUTOUT = 2;
    private boolean mBatteryCharging;
    private BatteryMeterView mBatteryView;
    private TextView mCarrierLabel;
    private final Rect mClipRect = new Rect(0, 0, 0, 0);
    private int mCutoutSideNudge = 0;
    private View mCutoutSpace;
    private DisplayCutout mDisplayCutout;
    private final ArrayList<Rect> mEmptyTintRect = new ArrayList<>();
    private boolean mIsPrivacyDotEnabled;
    private boolean mIsUserSwitcherEnabled;
    private boolean mKeyguardUserAvatarEnabled;
    private boolean mKeyguardUserSwitcherEnabled;
    private int mLayoutState = 0;
    private int mMinDotWidth;
    /* access modifiers changed from: private */
    public ImageView mMultiUserAvatar;
    private Pair<Integer, Integer> mPadding = new Pair<>(0, 0);
    private int mRoundedCornerPadding = 0;
    private boolean mShowPercentAvailable;
    private int mStatusBarPaddingEnd;
    /* access modifiers changed from: private */
    public ViewGroup mStatusIconArea;
    private StatusIconContainer mStatusIconContainer;
    /* access modifiers changed from: private */
    public View mSystemIconsContainer;
    private int mSystemIconsSwitcherHiddenExpandedMargin;
    private int mTopClipping;
    private ViewGroup mUserSwitcherContainer;

    private int calculateMargin(int i, int i2) {
        if (i2 >= i) {
            return 0;
        }
        return i - i2;
    }

    public boolean hasOverlappingRendering() {
        return false;
    }

    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return super.generateLayoutParams(attributeSet);
    }

    public /* bridge */ /* synthetic */ ViewOverlay getOverlay() {
        return super.getOverlay();
    }

    public KeyguardStatusBarView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mSystemIconsContainer = findViewById(C1893R.C1897id.system_icons_container);
        this.mMultiUserAvatar = (ImageView) findViewById(C1893R.C1897id.multi_user_avatar);
        this.mCarrierLabel = (TextView) findViewById(C1893R.C1897id.keyguard_carrier_text);
        this.mBatteryView = (BatteryMeterView) this.mSystemIconsContainer.findViewById(C1893R.C1897id.battery);
        this.mCutoutSpace = findViewById(C1893R.C1897id.cutout_space_view);
        this.mStatusIconArea = (ViewGroup) findViewById(C1893R.C1897id.status_icon_area);
        this.mStatusIconContainer = (StatusIconContainer) findViewById(C1893R.C1897id.statusIcons);
        this.mUserSwitcherContainer = (ViewGroup) findViewById(C1893R.C1897id.user_switcher_container);
        this.mIsPrivacyDotEnabled = this.mContext.getResources().getBoolean(C1893R.bool.config_enablePrivacyDot);
        loadDimens();
    }

    public ViewGroup getUserSwitcherContainer() {
        return this.mUserSwitcherContainer;
    }

    /* access modifiers changed from: protected */
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        loadDimens();
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.mMultiUserAvatar.getLayoutParams();
        int dimensionPixelSize = getResources().getDimensionPixelSize(C1893R.dimen.multi_user_avatar_keyguard_size);
        marginLayoutParams.height = dimensionPixelSize;
        marginLayoutParams.width = dimensionPixelSize;
        this.mMultiUserAvatar.setLayoutParams(marginLayoutParams);
        updateSystemIconsLayoutParams();
        ViewGroup viewGroup = this.mStatusIconArea;
        viewGroup.setPaddingRelative(viewGroup.getPaddingStart(), getResources().getDimensionPixelSize(C1893R.dimen.status_bar_padding_top), this.mStatusIconArea.getPaddingEnd(), this.mStatusIconArea.getPaddingBottom());
        StatusIconContainer statusIconContainer = this.mStatusIconContainer;
        statusIconContainer.setPaddingRelative(statusIconContainer.getPaddingStart(), this.mStatusIconContainer.getPaddingTop(), getResources().getDimensionPixelSize(C1893R.dimen.signal_cluster_battery_padding), this.mStatusIconContainer.getPaddingBottom());
        this.mCarrierLabel.setTextSize(0, (float) getResources().getDimensionPixelSize(17105579));
        ViewGroup.MarginLayoutParams marginLayoutParams2 = (ViewGroup.MarginLayoutParams) this.mCarrierLabel.getLayoutParams();
        marginLayoutParams2.setMarginStart(calculateMargin(getResources().getDimensionPixelSize(C1893R.dimen.keyguard_carrier_text_margin), ((Integer) this.mPadding.first).intValue()));
        this.mCarrierLabel.setLayoutParams(marginLayoutParams2);
        updateKeyguardStatusBarHeight();
    }

    public void setUserSwitcherEnabled(boolean z) {
        this.mIsUserSwitcherEnabled = z;
    }

    private void updateKeyguardStatusBarHeight() {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();
        marginLayoutParams.height = Utils.getStatusBarHeaderHeightKeyguard(this.mContext);
        setLayoutParams(marginLayoutParams);
    }

    /* access modifiers changed from: package-private */
    public void loadDimens() {
        Resources resources = getResources();
        this.mSystemIconsSwitcherHiddenExpandedMargin = resources.getDimensionPixelSize(C1893R.dimen.system_icons_switcher_hidden_expanded_margin);
        this.mStatusBarPaddingEnd = resources.getDimensionPixelSize(C1893R.dimen.status_bar_padding_end);
        this.mMinDotWidth = resources.getDimensionPixelSize(C1893R.dimen.ongoing_appops_dot_min_padding);
        this.mCutoutSideNudge = getResources().getDimensionPixelSize(C1893R.dimen.display_cutout_margin_consumption);
        this.mShowPercentAvailable = getContext().getResources().getBoolean(17891384);
        this.mRoundedCornerPadding = resources.getDimensionPixelSize(C1893R.dimen.rounded_corner_content_padding);
    }

    private void updateVisibilities() {
        if (!this.mKeyguardUserAvatarEnabled) {
            ViewParent parent = this.mMultiUserAvatar.getParent();
            ViewGroup viewGroup = this.mStatusIconArea;
            if (parent == viewGroup) {
                viewGroup.removeView(this.mMultiUserAvatar);
            } else if (this.mMultiUserAvatar.getParent() != null) {
                getOverlay().remove(this.mMultiUserAvatar);
            }
        } else {
            boolean z = false;
            if (this.mMultiUserAvatar.getParent() == this.mStatusIconArea || this.mKeyguardUserSwitcherEnabled) {
                ViewParent parent2 = this.mMultiUserAvatar.getParent();
                ViewGroup viewGroup2 = this.mStatusIconArea;
                if (parent2 == viewGroup2 && this.mKeyguardUserSwitcherEnabled) {
                    viewGroup2.removeView(this.mMultiUserAvatar);
                }
            } else {
                if (this.mMultiUserAvatar.getParent() != null) {
                    getOverlay().remove(this.mMultiUserAvatar);
                }
                this.mStatusIconArea.addView(this.mMultiUserAvatar, 0);
            }
            if (!this.mKeyguardUserSwitcherEnabled) {
                if (this.mIsUserSwitcherEnabled) {
                    this.mMultiUserAvatar.setVisibility(0);
                } else {
                    this.mMultiUserAvatar.setVisibility(8);
                }
            }
            BatteryMeterView batteryMeterView = this.mBatteryView;
            if (this.mBatteryCharging && this.mShowPercentAvailable) {
                z = true;
            }
            batteryMeterView.setForceShowPercent(z);
        }
    }

    private void updateSystemIconsLayoutParams() {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mSystemIconsContainer.getLayoutParams();
        int i = this.mStatusBarPaddingEnd;
        if (this.mKeyguardUserSwitcherEnabled) {
            i = this.mSystemIconsSwitcherHiddenExpandedMargin;
        }
        if (i != layoutParams.getMarginEnd()) {
            layoutParams.setMarginEnd(i);
            this.mSystemIconsContainer.setLayoutParams(layoutParams);
        }
    }

    /* access modifiers changed from: package-private */
    public WindowInsets updateWindowInsets(WindowInsets windowInsets, StatusBarContentInsetsProvider statusBarContentInsetsProvider) {
        this.mLayoutState = 0;
        if (updateLayoutConsideringCutout(statusBarContentInsetsProvider)) {
            requestLayout();
        }
        return super.onApplyWindowInsets(windowInsets);
    }

    private boolean updateLayoutConsideringCutout(StatusBarContentInsetsProvider statusBarContentInsetsProvider) {
        this.mDisplayCutout = getRootWindowInsets().getDisplayCutout();
        updateKeyguardStatusBarHeight();
        updatePadding(statusBarContentInsetsProvider);
        if (this.mDisplayCutout == null || statusBarContentInsetsProvider.currentRotationHasCornerCutout()) {
            return updateLayoutParamsNoCutout();
        }
        return updateLayoutParamsForCutout();
    }

    private void updatePadding(StatusBarContentInsetsProvider statusBarContentInsetsProvider) {
        DisplayCutout displayCutout = this.mDisplayCutout;
        int i = displayCutout == null ? 0 : displayCutout.getWaterfallInsets().top;
        this.mPadding = statusBarContentInsetsProvider.getStatusBarContentInsetsForCurrentRotation();
        setPadding((!isLayoutRtl() || !this.mIsPrivacyDotEnabled) ? ((Integer) this.mPadding.first).intValue() : Math.max(this.mMinDotWidth, ((Integer) this.mPadding.first).intValue()), i, (isLayoutRtl() || !this.mIsPrivacyDotEnabled) ? ((Integer) this.mPadding.second).intValue() : Math.max(this.mMinDotWidth, ((Integer) this.mPadding.second).intValue()), 0);
    }

    private boolean updateLayoutParamsNoCutout() {
        if (this.mLayoutState == 2) {
            return false;
        }
        this.mLayoutState = 2;
        View view = this.mCutoutSpace;
        if (view != null) {
            view.setVisibility(8);
        }
        ((RelativeLayout.LayoutParams) this.mCarrierLabel.getLayoutParams()).addRule(16, C1893R.C1897id.status_icon_area);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.mStatusIconArea.getLayoutParams();
        layoutParams.removeRule(1);
        layoutParams.width = -2;
        ((LinearLayout.LayoutParams) this.mSystemIconsContainer.getLayoutParams()).setMarginStart(getResources().getDimensionPixelSize(C1893R.dimen.system_icons_super_container_margin_start));
        return true;
    }

    private boolean updateLayoutParamsForCutout() {
        if (this.mLayoutState == 1) {
            return false;
        }
        this.mLayoutState = 1;
        if (this.mCutoutSpace == null) {
            updateLayoutParamsNoCutout();
        }
        Rect rect = new Rect();
        ScreenDecorations.DisplayCutoutView.boundsFromDirection(this.mDisplayCutout, 48, rect);
        this.mCutoutSpace.setVisibility(0);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.mCutoutSpace.getLayoutParams();
        rect.left += this.mCutoutSideNudge;
        rect.right -= this.mCutoutSideNudge;
        layoutParams.width = rect.width();
        layoutParams.height = rect.height();
        layoutParams.addRule(13);
        ((RelativeLayout.LayoutParams) this.mCarrierLabel.getLayoutParams()).addRule(16, C1893R.C1897id.cutout_space_view);
        RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) this.mStatusIconArea.getLayoutParams();
        layoutParams2.addRule(1, C1893R.C1897id.cutout_space_view);
        layoutParams2.width = -1;
        ((LinearLayout.LayoutParams) this.mSystemIconsContainer.getLayoutParams()).setMarginStart(0);
        return true;
    }

    /* access modifiers changed from: package-private */
    public void onUserInfoChanged(Drawable drawable) {
        this.mMultiUserAvatar.setImageDrawable(drawable);
    }

    /* access modifiers changed from: package-private */
    public void onBatteryLevelChanged(boolean z) {
        if (this.mBatteryCharging != z) {
            this.mBatteryCharging = z;
            updateVisibilities();
        }
    }

    /* access modifiers changed from: package-private */
    public void setKeyguardUserSwitcherEnabled(boolean z) {
        this.mKeyguardUserSwitcherEnabled = z;
    }

    /* access modifiers changed from: package-private */
    public void setKeyguardUserAvatarEnabled(boolean z) {
        this.mKeyguardUserAvatarEnabled = z;
        updateVisibilities();
    }

    /* access modifiers changed from: package-private */
    public boolean isKeyguardUserAvatarEnabled() {
        return this.mKeyguardUserAvatarEnabled;
    }

    private void animateNextLayoutChange() {
        final int left = this.mSystemIconsContainer.getLeft();
        final boolean z = this.mMultiUserAvatar.getParent() == this.mStatusIconArea;
        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                KeyguardStatusBarView.this.getViewTreeObserver().removeOnPreDrawListener(this);
                boolean z = z && KeyguardStatusBarView.this.mMultiUserAvatar.getParent() != KeyguardStatusBarView.this.mStatusIconArea;
                KeyguardStatusBarView.this.mSystemIconsContainer.setX((float) left);
                KeyguardStatusBarView.this.mSystemIconsContainer.animate().translationX(0.0f).setDuration(400).setStartDelay(z ? 300 : 0).setInterpolator(Interpolators.FAST_OUT_SLOW_IN).start();
                if (z) {
                    KeyguardStatusBarView.this.getOverlay().add(KeyguardStatusBarView.this.mMultiUserAvatar);
                    KeyguardStatusBarView.this.mMultiUserAvatar.animate().alpha(0.0f).setDuration(300).setStartDelay(0).setInterpolator(Interpolators.ALPHA_OUT).withEndAction(new KeyguardStatusBarView$1$$ExternalSyntheticLambda0(this)).start();
                } else {
                    KeyguardStatusBarView.this.mMultiUserAvatar.setAlpha(0.0f);
                    KeyguardStatusBarView.this.mMultiUserAvatar.animate().alpha(1.0f).setDuration(300).setStartDelay(200).setInterpolator(Interpolators.ALPHA_IN);
                }
                return true;
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$onPreDraw$0$com-android-systemui-statusbar-phone-KeyguardStatusBarView$1 */
            public /* synthetic */ void mo44289x31c31e6f() {
                KeyguardStatusBarView.this.mMultiUserAvatar.setAlpha(1.0f);
                KeyguardStatusBarView.this.getOverlay().remove(KeyguardStatusBarView.this.mMultiUserAvatar);
            }
        });
    }

    public void setVisibility(int i) {
        super.setVisibility(i);
        if (i != 0) {
            this.mSystemIconsContainer.animate().cancel();
            this.mSystemIconsContainer.setTranslationX(0.0f);
            this.mMultiUserAvatar.animate().cancel();
            this.mMultiUserAvatar.setAlpha(1.0f);
            return;
        }
        updateVisibilities();
        updateSystemIconsLayoutParams();
    }

    /* access modifiers changed from: package-private */
    public void onThemeChanged(StatusBarIconController.TintedIconManager tintedIconManager) {
        this.mBatteryView.setColorsFromContext(this.mContext);
        updateIconsAndTextColors(tintedIconManager);
    }

    /* access modifiers changed from: package-private */
    public void onOverlayChanged() {
        int themeAttr = com.android.settingslib.Utils.getThemeAttr(this.mContext, 16842818);
        this.mCarrierLabel.setTextAppearance(themeAttr);
        this.mBatteryView.updatePercentView();
        TextView textView = (TextView) this.mUserSwitcherContainer.findViewById(C1893R.C1897id.current_user_name);
        if (textView != null) {
            textView.setTextAppearance(themeAttr);
        }
    }

    private void updateIconsAndTextColors(StatusBarIconController.TintedIconManager tintedIconManager) {
        int nTDefaultTextColor = NTColorUtil.getNTDefaultTextColor(this.mContext);
        this.mCarrierLabel.setTextColor(nTDefaultTextColor);
        TextView textView = (TextView) this.mUserSwitcherContainer.findViewById(C1893R.C1897id.current_user_name);
        if (textView != null) {
            textView.setTextColor(com.android.settingslib.Utils.getColorStateListDefaultColor(this.mContext, C1893R.C1894color.light_mode_icon_color_single_tone));
        }
        if (tintedIconManager != null) {
            tintedIconManager.setTint(nTDefaultTextColor);
        }
        applyDarkness(C1893R.C1897id.battery, this.mEmptyTintRect, 0.0f, nTDefaultTextColor);
        applyDarkness(C1893R.C1897id.clock, this.mEmptyTintRect, 0.0f, nTDefaultTextColor);
    }

    private void applyDarkness(int i, ArrayList<Rect> arrayList, float f, int i2) {
        View findViewById = findViewById(i);
        if (findViewById instanceof DarkIconDispatcher.DarkReceiver) {
            ((DarkIconDispatcher.DarkReceiver) findViewById).onDarkChanged(arrayList, f, i2);
        }
    }

    /* access modifiers changed from: package-private */
    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("KeyguardStatusBarView:");
        printWriter.println("  mBatteryCharging: " + this.mBatteryCharging);
        printWriter.println("  mLayoutState: " + this.mLayoutState);
        printWriter.println("  mKeyguardUserSwitcherEnabled: " + this.mKeyguardUserSwitcherEnabled);
        BatteryMeterView batteryMeterView = this.mBatteryView;
        if (batteryMeterView != null) {
            batteryMeterView.dump(printWriter, strArr);
        }
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        updateClipping();
    }

    /* access modifiers changed from: package-private */
    public void setTopClipping(int i) {
        if (i != this.mTopClipping) {
            this.mTopClipping = i;
            updateClipping();
        }
    }

    private void updateClipping() {
        this.mClipRect.set(0, this.mTopClipping, getWidth(), getHeight());
        setClipBounds(this.mClipRect);
    }
}
