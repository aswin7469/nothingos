package com.android.systemui.p012qs;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.IndentingPrintWriter;
import android.util.Pair;
import android.view.DisplayCutout;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.view.WindowInsets;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Space;
import androidx.constraintlayout.motion.widget.Key;
import com.android.internal.policy.SystemBarUtils;
import com.android.settingslib.Utils;
import com.android.systemui.C1894R;
import com.android.systemui.battery.BatteryMeterView;
import com.android.systemui.p012qs.TouchAnimator;
import com.android.systemui.statusbar.phone.StatusBarContentInsetsProvider;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import com.android.systemui.statusbar.phone.StatusIconContainer;
import com.android.systemui.statusbar.policy.Clock;
import com.android.systemui.statusbar.policy.VariableDateView;
import com.android.systemui.util.LargeScreenUtils;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.p024qs.QuickStatusBarHeaderEx;
import java.util.List;

/* renamed from: com.android.systemui.qs.QuickStatusBarHeader */
public class QuickStatusBarHeader extends FrameLayout {
    private TouchAnimator mAlphaAnimator;
    private BatteryMeterView mBatteryRemainingIcon;
    private ViewGroup mClockContainer;
    /* access modifiers changed from: private */
    public VariableDateView mClockDateView;
    private View mClockIconsSeparator;
    private Clock mClockView;
    private boolean mConfigShowBatteryEstimate;
    private View mContainer;
    private int mCutOutPaddingLeft;
    private int mCutOutPaddingRight;
    private View mDateContainer;
    private Space mDatePrivacySeparator;
    private View mDatePrivacyView;
    private View mDateView;
    private QuickStatusBarHeaderEx mEx = ((QuickStatusBarHeaderEx) NTDependencyEx.get(QuickStatusBarHeaderEx.class));
    private boolean mExpanded;
    private boolean mHasCenterCutout;
    protected QuickQSPanel mHeaderQsPanel;
    /* access modifiers changed from: private */
    public StatusIconContainer mIconContainer;
    private TouchAnimator mIconsAlphaAnimator;
    private TouchAnimator mIconsAlphaAnimatorFixed;
    private StatusBarContentInsetsProvider mInsetsProvider;
    /* access modifiers changed from: private */
    public boolean mIsSingleCarrier;
    private float mKeyguardExpansionFraction;
    private View mPrivacyChip;
    private View mPrivacyContainer;
    private View mQSCarriers;
    private QSExpansionPathInterpolator mQSExpansionPathInterpolator;
    private boolean mQsDisabled;
    private View mRightLayout;
    private int mRoundedCornerPadding = 0;
    /* access modifiers changed from: private */
    public List<String> mRssiIgnoredSlots = List.m1728of();
    /* access modifiers changed from: private */
    public boolean mShowClockIconsSeparator;
    private View mStatusIconsView;
    private int mTextColorPrimary = 0;
    private StatusBarIconController.TintedIconManager mTintedIconManager;
    private int mTopViewMeasureHeight;
    private TouchAnimator mTranslationAnimator;
    private boolean mUseCombinedQSHeader;
    private int mWaterfallTopInset;

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

    public QuickStatusBarHeader(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public int getOffsetTranslation() {
        return this.mTopViewMeasureHeight;
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mHeaderQsPanel = (QuickQSPanel) findViewById(C1894R.C1898id.quick_qs_panel);
        this.mDatePrivacyView = findViewById(C1894R.C1898id.quick_status_bar_date_privacy);
        this.mStatusIconsView = findViewById(C1894R.C1898id.quick_qs_status_icons);
        this.mQSCarriers = findViewById(C1894R.C1898id.carrier_group);
        this.mContainer = findViewById(C1894R.C1898id.qs_container);
        this.mIconContainer = (StatusIconContainer) findViewById(C1894R.C1898id.statusIcons);
        this.mPrivacyChip = findViewById(C1894R.C1898id.privacy_chip);
        this.mDateView = findViewById(C1894R.C1898id.date);
        this.mClockDateView = (VariableDateView) findViewById(C1894R.C1898id.date_clock);
        this.mClockIconsSeparator = findViewById(C1894R.C1898id.separator);
        this.mRightLayout = findViewById(C1894R.C1898id.rightLayout);
        this.mDateContainer = findViewById(C1894R.C1898id.date_container);
        this.mPrivacyContainer = findViewById(C1894R.C1898id.privacy_container);
        this.mClockContainer = (ViewGroup) findViewById(C1894R.C1898id.clock_container);
        this.mClockView = (Clock) findViewById(C1894R.C1898id.clock);
        this.mDatePrivacySeparator = (Space) findViewById(C1894R.C1898id.space);
        this.mBatteryRemainingIcon = (BatteryMeterView) findViewById(C1894R.C1898id.batteryRemainingIcon);
        this.mEx.init(this.mContext, this);
        updateResources();
        setDatePrivacyContainersWidth(this.mContext.getResources().getConfiguration().orientation == 2);
        this.mBatteryRemainingIcon.setPercentShowMode(3);
        this.mIconsAlphaAnimatorFixed = new TouchAnimator.Builder().addFloat(this.mIconContainer, Key.ALPHA, 0.0f, 1.0f).addFloat(this.mBatteryRemainingIcon, Key.ALPHA, 0.0f, 1.0f).build();
    }

    /* access modifiers changed from: package-private */
    public void onAttach(StatusBarIconController.TintedIconManager tintedIconManager, QSExpansionPathInterpolator qSExpansionPathInterpolator, List<String> list, StatusBarContentInsetsProvider statusBarContentInsetsProvider, boolean z) {
        this.mUseCombinedQSHeader = z;
        this.mTintedIconManager = tintedIconManager;
        this.mRssiIgnoredSlots = list;
        this.mInsetsProvider = statusBarContentInsetsProvider;
        tintedIconManager.setTint(Utils.getColorAttrDefaultColor(getContext(), 16842806));
        this.mQSExpansionPathInterpolator = qSExpansionPathInterpolator;
        updateAnimators();
    }

    /* access modifiers changed from: package-private */
    public void setIsSingleCarrier(boolean z) {
        this.mIsSingleCarrier = z;
        updateAlphaAnimator();
    }

    public QuickQSPanel getHeaderQsPanel() {
        return this.mHeaderQsPanel;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        if (this.mEx.getQSStatusBar().getMeasuredHeight() != this.mTopViewMeasureHeight) {
            this.mTopViewMeasureHeight = this.mEx.getQSStatusBar().getMeasuredHeight();
            post(new QuickStatusBarHeader$$ExternalSyntheticLambda1(this));
        }
    }

    /* access modifiers changed from: protected */
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        updateResources();
        setDatePrivacyContainersWidth(configuration.orientation == 2);
    }

    public void onRtlPropertiesChanged(int i) {
        super.onRtlPropertiesChanged(i);
        updateResources();
    }

    private void setDatePrivacyContainersWidth(boolean z) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mDateContainer.getLayoutParams();
        layoutParams.width = z ? -2 : 0;
        layoutParams.weight = z ? 0.0f : 1.0f;
        this.mDateContainer.setLayoutParams(layoutParams);
    }

    private void updateBatteryMode() {
        if (!this.mConfigShowBatteryEstimate || this.mHasCenterCutout) {
            this.mBatteryRemainingIcon.setPercentShowMode(1);
        } else {
            this.mBatteryRemainingIcon.setPercentShowMode(3);
        }
    }

    /* access modifiers changed from: package-private */
    public void updateResources() {
        Resources resources = this.mContext.getResources();
        boolean shouldUseLargeScreenShadeHeader = LargeScreenUtils.shouldUseLargeScreenShadeHeader(resources);
        if (!shouldUseLargeScreenShadeHeader && !this.mUseCombinedQSHeader) {
            boolean z = this.mQsDisabled;
        }
        this.mConfigShowBatteryEstimate = resources.getBoolean(C1894R.bool.config_showBatteryEstimateQSBH);
        this.mRoundedCornerPadding = resources.getDimensionPixelSize(C1894R.dimen.rounded_corner_content_padding);
        int quickQsOffsetHeight = SystemBarUtils.getQuickQsOffsetHeight(this.mContext);
        this.mDatePrivacyView.getLayoutParams().height = Math.max(quickQsOffsetHeight, this.mDatePrivacyView.getMinimumHeight());
        View view = this.mDatePrivacyView;
        view.setLayoutParams(view.getLayoutParams());
        this.mStatusIconsView.getLayoutParams().height = Math.max(quickQsOffsetHeight, this.mStatusIconsView.getMinimumHeight());
        View view2 = this.mStatusIconsView;
        view2.setLayoutParams(view2.getLayoutParams());
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (this.mQsDisabled) {
            layoutParams.height = this.mStatusIconsView.getLayoutParams().height;
        } else {
            layoutParams.height = -2;
        }
        setLayoutParams(layoutParams);
        int colorAttrDefaultColor = Utils.getColorAttrDefaultColor(this.mContext, 16842806);
        if (colorAttrDefaultColor != this.mTextColorPrimary) {
            int colorAttrDefaultColor2 = Utils.getColorAttrDefaultColor(this.mContext, 16842808);
            this.mTextColorPrimary = colorAttrDefaultColor;
            this.mClockView.setTextColor(colorAttrDefaultColor);
            StatusBarIconController.TintedIconManager tintedIconManager = this.mTintedIconManager;
            if (tintedIconManager != null) {
                tintedIconManager.setTint(colorAttrDefaultColor);
            }
            BatteryMeterView batteryMeterView = this.mBatteryRemainingIcon;
            int i = this.mTextColorPrimary;
            batteryMeterView.updateColors(i, colorAttrDefaultColor2, i);
        }
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.mHeaderQsPanel.getLayoutParams();
        if (shouldUseLargeScreenShadeHeader || !this.mUseCombinedQSHeader) {
            quickQsOffsetHeight = this.mContext.getResources().getDimensionPixelSize(C1894R.dimen.qqs_layout_margin_top);
        }
        marginLayoutParams.topMargin = quickQsOffsetHeight;
        this.mHeaderQsPanel.setLayoutParams(marginLayoutParams);
        updateBatteryMode();
        updateHeadersPadding();
        updateAnimators();
        this.mEx.updatePortQSStatusBarVisibility(this.mContext);
        this.mEx.updatePullDownArrowVisibility(this.mContext, this.mExpanded);
        int dimensionPixelSize = this.mContext.getResources().getDimensionPixelSize(C1894R.dimen.qs_status_bar_height);
        View view3 = this.mContainer;
        view3.setPadding(view3.getPaddingLeft(), dimensionPixelSize, this.mContainer.getPaddingRight(), this.mContainer.getPaddingBottom());
        updateClockDatePadding();
    }

    private void updateClockDatePadding() {
        int dimensionPixelSize = this.mContext.getResources().getDimensionPixelSize(C1894R.dimen.status_bar_left_clock_starting_padding);
        int dimensionPixelSize2 = this.mContext.getResources().getDimensionPixelSize(C1894R.dimen.status_bar_left_clock_end_padding);
        Clock clock = this.mClockView;
        clock.setPaddingRelative(dimensionPixelSize, clock.getPaddingTop(), dimensionPixelSize2, this.mClockView.getPaddingBottom());
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.mClockDateView.getLayoutParams();
        marginLayoutParams.setMarginStart(dimensionPixelSize2);
        this.mClockDateView.setLayoutParams(marginLayoutParams);
    }

    /* access modifiers changed from: private */
    public void updateAnimators() {
        Interpolator interpolator = null;
        if (this.mUseCombinedQSHeader) {
            this.mTranslationAnimator = null;
            return;
        }
        updateAlphaAnimator();
        int i = this.mTopViewMeasureHeight;
        TouchAnimator.Builder addFloat = new TouchAnimator.Builder().addFloat(this.mContainer, Key.TRANSLATION_Y, 0.0f, (float) i);
        QSExpansionPathInterpolator qSExpansionPathInterpolator = this.mQSExpansionPathInterpolator;
        if (qSExpansionPathInterpolator != null) {
            interpolator = qSExpansionPathInterpolator.getYInterpolator();
        }
        this.mTranslationAnimator = addFloat.setInterpolator(interpolator).build();
    }

    private void updateAlphaAnimator() {
        if (this.mUseCombinedQSHeader) {
            this.mAlphaAnimator = null;
        } else {
            this.mAlphaAnimator = new TouchAnimator.Builder().addFloat(this.mDateView, Key.ALPHA, 0.0f, 0.0f, 1.0f).addFloat(this.mClockDateView, Key.ALPHA, 1.0f, 0.0f, 0.0f).addFloat(this.mQSCarriers, Key.ALPHA, 0.0f, 1.0f).setListener(new TouchAnimator.ListenerAdapter() {
                public void onAnimationAtEnd() {
                    super.onAnimationAtEnd();
                    if (!QuickStatusBarHeader.this.mIsSingleCarrier) {
                        QuickStatusBarHeader.this.mIconContainer.addIgnoredSlots(QuickStatusBarHeader.this.mRssiIgnoredSlots);
                    }
                    QuickStatusBarHeader.this.mClockDateView.setVisibility(8);
                }

                public void onAnimationStarted() {
                    QuickStatusBarHeader.this.mClockDateView.setVisibility(0);
                    QuickStatusBarHeader.this.mClockDateView.setFreezeSwitching(true);
                    QuickStatusBarHeader.this.setSeparatorVisibility(false);
                    if (!QuickStatusBarHeader.this.mIsSingleCarrier) {
                        QuickStatusBarHeader.this.mIconContainer.addIgnoredSlots(QuickStatusBarHeader.this.mRssiIgnoredSlots);
                    }
                }

                public void onAnimationAtStart() {
                    super.onAnimationAtStart();
                    QuickStatusBarHeader.this.mClockDateView.setFreezeSwitching(false);
                    QuickStatusBarHeader.this.mClockDateView.setVisibility(0);
                    QuickStatusBarHeader quickStatusBarHeader = QuickStatusBarHeader.this;
                    quickStatusBarHeader.setSeparatorVisibility(quickStatusBarHeader.mShowClockIconsSeparator);
                    QuickStatusBarHeader.this.mIconContainer.removeIgnoredSlots(QuickStatusBarHeader.this.mRssiIgnoredSlots);
                }
            }).build();
        }
    }

    /* access modifiers changed from: package-private */
    public void setChipVisibility(boolean z) {
        if (z) {
            TouchAnimator touchAnimator = this.mIconsAlphaAnimatorFixed;
            this.mIconsAlphaAnimator = touchAnimator;
            touchAnimator.setPosition(this.mKeyguardExpansionFraction);
            return;
        }
        this.mIconsAlphaAnimator = null;
        this.mIconContainer.setAlpha(1.0f);
        this.mBatteryRemainingIcon.setAlpha(1.0f);
    }

    public void setExpanded(boolean z, QuickQSPanelController quickQSPanelController) {
        if (this.mExpanded != z) {
            this.mExpanded = z;
            quickQSPanelController.setExpanded(z);
            updateEverything();
            this.mEx.updatePullDownArrowVisibility(this.mContext, this.mExpanded);
        }
    }

    public void setExpansion(boolean z, float f, float f2) {
        if (z) {
            f = 1.0f;
        }
        TouchAnimator touchAnimator = this.mAlphaAnimator;
        if (touchAnimator != null) {
            touchAnimator.setPosition(f);
        }
        TouchAnimator touchAnimator2 = this.mTranslationAnimator;
        if (touchAnimator2 != null) {
            touchAnimator2.setPosition(f);
        }
        TouchAnimator touchAnimator3 = this.mIconsAlphaAnimator;
        if (touchAnimator3 != null) {
            touchAnimator3.setPosition(f);
        }
        if (z) {
            setTranslationY(f2);
        } else {
            setTranslationY(0.0f);
        }
        this.mKeyguardExpansionFraction = f;
    }

    public void disable(int i, int i2, boolean z) {
        boolean z2 = true;
        int i3 = 0;
        if ((i2 & 1) == 0) {
            z2 = false;
        }
        if (z2 != this.mQsDisabled) {
            this.mQsDisabled = z2;
            this.mHeaderQsPanel.setDisabledByPolicy(z2);
            View view = this.mStatusIconsView;
            if (this.mQsDisabled) {
                i3 = 8;
            }
            view.setVisibility(i3);
            updateResources();
        }
    }

    public WindowInsets onApplyWindowInsets(WindowInsets windowInsets) {
        DisplayCutout displayCutout = windowInsets.getDisplayCutout();
        Pair<Integer, Integer> statusBarContentInsetsForCurrentRotation = this.mInsetsProvider.getStatusBarContentInsetsForCurrentRotation();
        boolean currentRotationHasCornerCutout = this.mInsetsProvider.currentRotationHasCornerCutout();
        int i = 0;
        this.mDatePrivacyView.setPadding(((Integer) statusBarContentInsetsForCurrentRotation.first).intValue(), 0, ((Integer) statusBarContentInsetsForCurrentRotation.second).intValue(), 0);
        this.mStatusIconsView.setPadding(((Integer) statusBarContentInsetsForCurrentRotation.first).intValue(), 0, ((Integer) statusBarContentInsetsForCurrentRotation.second).intValue(), 0);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mDatePrivacySeparator.getLayoutParams();
        LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) this.mClockIconsSeparator.getLayoutParams();
        if (displayCutout != null) {
            Rect boundingRectTop = displayCutout.getBoundingRectTop();
            if (boundingRectTop.isEmpty() || currentRotationHasCornerCutout) {
                layoutParams.width = 0;
                this.mDatePrivacySeparator.setVisibility(8);
                layoutParams2.width = 0;
                setSeparatorVisibility(false);
                this.mShowClockIconsSeparator = false;
                this.mHasCenterCutout = false;
            } else {
                layoutParams.width = boundingRectTop.width();
                this.mDatePrivacySeparator.setVisibility(0);
                layoutParams2.width = boundingRectTop.width();
                this.mShowClockIconsSeparator = true;
                setSeparatorVisibility(this.mKeyguardExpansionFraction == 0.0f);
                this.mHasCenterCutout = true;
            }
        }
        this.mDatePrivacySeparator.setLayoutParams(layoutParams);
        this.mClockIconsSeparator.setLayoutParams(layoutParams2);
        this.mCutOutPaddingLeft = ((Integer) statusBarContentInsetsForCurrentRotation.first).intValue();
        this.mCutOutPaddingRight = ((Integer) statusBarContentInsetsForCurrentRotation.second).intValue();
        if (displayCutout != null) {
            i = displayCutout.getWaterfallInsets().top;
        }
        this.mWaterfallTopInset = i;
        updateBatteryMode();
        updateHeadersPadding();
        return super.onApplyWindowInsets(windowInsets);
    }

    /* access modifiers changed from: private */
    public void setSeparatorVisibility(boolean z) {
        int i = 8;
        int i2 = 0;
        if (this.mClockIconsSeparator.getVisibility() != (z ? 0 : 8)) {
            this.mClockIconsSeparator.setVisibility(z ? 0 : 8);
            View view = this.mQSCarriers;
            if (!z) {
                i = 0;
            }
            view.setVisibility(i);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mClockContainer.getLayoutParams();
            layoutParams.width = z ? 0 : -2;
            float f = 1.0f;
            layoutParams.weight = z ? 1.0f : 0.0f;
            this.mClockContainer.setLayoutParams(layoutParams);
            LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) this.mRightLayout.getLayoutParams();
            if (!z) {
                i2 = -2;
            }
            layoutParams2.width = i2;
            if (!z) {
                f = 0.0f;
            }
            layoutParams2.weight = f;
            this.mRightLayout.setLayoutParams(layoutParams2);
        }
    }

    private void updateHeadersPadding() {
        setContentMargins(this.mDatePrivacyView, 0, 0);
        setContentMargins(this.mStatusIconsView, 0, 0);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) getLayoutParams();
        int i = layoutParams.leftMargin;
        int i2 = layoutParams.rightMargin;
        int i3 = this.mCutOutPaddingLeft;
        int max = i3 > 0 ? Math.max(Math.max(i3, this.mRoundedCornerPadding) - i, 0) : 0;
        int i4 = this.mCutOutPaddingRight;
        int max2 = i4 > 0 ? Math.max(Math.max(i4, this.mRoundedCornerPadding) - i2, 0) : 0;
        this.mDatePrivacyView.setPadding(max, this.mWaterfallTopInset, max2, 0);
        this.mStatusIconsView.setPadding(max, this.mWaterfallTopInset, max2, 0);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$updateEverything$0$com-android-systemui-qs-QuickStatusBarHeader */
    public /* synthetic */ void mo36341xebd8adeb() {
        setClickable(!this.mExpanded);
    }

    public void updateEverything() {
        post(new QuickStatusBarHeader$$ExternalSyntheticLambda0(this));
    }

    private void setContentMargins(View view, int i, int i2) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        marginLayoutParams.setMarginStart(i);
        marginLayoutParams.setMarginEnd(i2);
        view.setLayoutParams(marginLayoutParams);
    }

    public void setExpandedScrollAmount(int i) {
        this.mStatusIconsView.setScrollY(i);
        this.mDatePrivacyView.setScrollY(i);
        this.mEx.setScrollY(i);
    }

    public void dump(IndentingPrintWriter indentingPrintWriter) {
        this.mEx.dump(indentingPrintWriter, this.mDatePrivacyView, this.mStatusIconsView);
    }
}
