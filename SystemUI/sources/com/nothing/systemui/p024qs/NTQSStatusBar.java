package com.nothing.systemui.p024qs;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.view.WindowInsets;
import android.widget.RelativeLayout;
import com.android.systemui.C1894R;
import com.android.systemui.Dependency;
import com.android.systemui.battery.BatteryMeterView;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.privacy.OngoingPrivacyChip;
import com.android.systemui.privacy.PrivacyItem;
import com.android.systemui.statusbar.phone.StatusBarContentInsetsProvider;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import com.android.systemui.statusbar.phone.StatusIconContainer;
import com.android.systemui.util.leak.RotationUtils;
import com.nothing.systemui.p024qs.NTQSStatusBarController;
import com.nothing.systemui.util.NTLogUtil;
import java.util.List;

/* renamed from: com.nothing.systemui.qs.NTQSStatusBar */
public class NTQSStatusBar extends RelativeLayout {
    private static final int COLOR_WHITE = -1;
    protected static final int MAX_ICONS = 7;
    protected static final int MAX_ICONS_WITH_PRIVACY = 5;
    private static final String TAG = "NTQSStatusBar";
    private BatteryMeterView mBatteryRemainingIcon;
    private boolean mConfigShowBatteryEstimate;
    private NTQSStatusBarController mController;
    private FeatureFlags mFeatureFlags;
    /* access modifiers changed from: private */
    public StatusIconContainer mIconContainer;
    private StatusBarIconController.TintedIconManager mIconManager;
    private NTQSStatusBarController.QSSControllerListener mListener = new NTQSStatusBarController.QSSControllerListener() {
        public void setOnClickListener(View.OnClickListener onClickListener) {
            NTQSStatusBar.this.mPrivacyChip.setOnClickListener(onClickListener);
        }

        public void setVisibility(int i) {
            NTQSStatusBar.this.mPrivacyChip.setVisibility(i);
        }

        public void setPrivacyList(List<PrivacyItem> list) {
            NTQSStatusBar.this.mPrivacyChip.setPrivacyList(list);
        }

        public void addIgnoredSlot(String... strArr) {
            for (String addIgnoredSlot : strArr) {
                NTQSStatusBar.this.mIconContainer.addIgnoredSlot(addIgnoredSlot);
            }
        }

        public void removeIgnoredSlot(String... strArr) {
            for (String removeIgnoredSlot : strArr) {
                NTQSStatusBar.this.mIconContainer.removeIgnoredSlot(removeIgnoredSlot);
            }
        }
    };
    /* access modifiers changed from: private */
    public OngoingPrivacyChip mPrivacyChip;
    private StatusBarIconController mStatusBarIconController;
    private final Rect mTmpRectForQSStatusBarBoundsOnScreen = new Rect();

    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return super.generateLayoutParams(attributeSet);
    }

    public /* bridge */ /* synthetic */ ViewOverlay getOverlay() {
        return super.getOverlay();
    }

    public NTQSStatusBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mPrivacyChip = (OngoingPrivacyChip) findViewById(C1894R.C1898id.privacy_chip);
        StatusIconContainer statusIconContainer = (StatusIconContainer) findViewById(C1894R.C1898id.qs_statusIcons);
        this.mIconContainer = statusIconContainer;
        statusIconContainer.setMaxIconsToDisplay(7);
        this.mFeatureFlags = (FeatureFlags) Dependency.get(FeatureFlags.class);
        BatteryMeterView batteryMeterView = (BatteryMeterView) findViewById(C1894R.C1898id.qs_batteryRemainingIcon);
        this.mBatteryRemainingIcon = batteryMeterView;
        batteryMeterView.setPercentShowMode(3);
        updateResources();
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    /* access modifiers changed from: protected */
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        updateResources();
    }

    private void updateResources() {
        updateSafePadding();
        this.mConfigShowBatteryEstimate = getResources().getBoolean(C1894R.bool.config_showBatteryEstimateQSBH);
        updateBatteryMode();
        this.mBatteryRemainingIcon.updateColors(-1, -1, -1);
    }

    private void updateBatteryMode() {
        if (this.mConfigShowBatteryEstimate) {
            this.mBatteryRemainingIcon.setPercentShowMode(3);
        } else {
            this.mBatteryRemainingIcon.setPercentShowMode(1);
        }
    }

    public WindowInsets onApplyWindowInsets(WindowInsets windowInsets) {
        updateSafePadding();
        return super.onApplyWindowInsets(windowInsets);
    }

    private void updateSafePadding() {
        int dimensionPixelOffset = getResources().getDimensionPixelOffset(C1894R.dimen.qs_status_bar_margin_left_right);
        int dimensionPixelSize = getResources().getDimensionPixelSize(C1894R.dimen.rounded_corner_content_padding);
        Pair<Integer, Integer> statusBarContentInsetsForRotation = ((StatusBarContentInsetsProvider) Dependency.get(StatusBarContentInsetsProvider.class)).getStatusBarContentInsetsForRotation(RotationUtils.getExactRotation(getContext()));
        int dimensionPixelSize2 = getResources().getDimensionPixelSize(C1894R.dimen.qs_content_horizontal_padding);
        NTLogUtil.m1686d(TAG, "safePadding: first = " + statusBarContentInsetsForRotation.first + ", second = " + statusBarContentInsetsForRotation.second + " | roundedCornerPadding = " + dimensionPixelSize + " | horizontalPadding" + dimensionPixelSize2);
        int max = ((Integer) statusBarContentInsetsForRotation.first).intValue() > 0 ? Math.max(Math.max(((Integer) statusBarContentInsetsForRotation.first).intValue(), dimensionPixelSize) - dimensionPixelSize2, 0) : dimensionPixelOffset;
        if (((Integer) statusBarContentInsetsForRotation.second).intValue() > 0) {
            dimensionPixelOffset = Math.max(Math.max(((Integer) statusBarContentInsetsForRotation.second).intValue(), dimensionPixelSize) - dimensionPixelSize2, 0);
        }
        setPadding(max, getPaddingTop(), dimensionPixelOffset, getPaddingBottom());
    }
}
