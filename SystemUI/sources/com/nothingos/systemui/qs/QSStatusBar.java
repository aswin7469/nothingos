package com.nothingos.systemui.qs;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowInsets;
import android.widget.RelativeLayout;
import com.android.systemui.BatteryMeterView;
import com.android.systemui.Dependency;
import com.android.systemui.R$bool;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.privacy.OngoingPrivacyChip;
import com.android.systemui.privacy.PrivacyItem;
import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.statusbar.phone.StatusBarContentInsetsProvider;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import com.android.systemui.statusbar.phone.StatusIconContainer;
import com.android.systemui.util.leak.RotationUtils;
import com.nothingos.systemui.qs.QSStatusBarController;
import java.util.List;
/* loaded from: classes2.dex */
public class QSStatusBar extends RelativeLayout {
    private BatteryMeterView mBatteryRemainingIcon;
    private boolean mConfigShowBatteryEstimate;
    private QSStatusBarController mController;
    private FeatureFlags mFeatureFlags;
    private StatusIconContainer mIconContainer;
    private StatusBarIconController.TintedIconManager mIconManager;
    private OngoingPrivacyChip mPrivacyChip;
    private StatusBarIconController mStatusBarIconController;
    private final Rect mTmpRectForQSStatusBarBoundsOnScreen = new Rect();
    private QSStatusBarController.QSSControllerListener mListener = new QSStatusBarController.QSSControllerListener() { // from class: com.nothingos.systemui.qs.QSStatusBar.1
        @Override // com.nothingos.systemui.qs.QSStatusBarController.QSSControllerListener
        public void setOnClickListener(View.OnClickListener onClickListener) {
            QSStatusBar.this.mPrivacyChip.setOnClickListener(onClickListener);
        }

        @Override // com.nothingos.systemui.qs.QSStatusBarController.QSSControllerListener
        public void setVisibility(int i) {
            QSStatusBar.this.mPrivacyChip.setVisibility(i);
        }

        @Override // com.nothingos.systemui.qs.QSStatusBarController.QSSControllerListener
        public void setPrivacyList(List<PrivacyItem> list) {
            QSStatusBar.this.mPrivacyChip.setPrivacyList(list);
        }

        @Override // com.nothingos.systemui.qs.QSStatusBarController.QSSControllerListener
        public void addIgnoredSlot(String... strArr) {
            for (String str : strArr) {
                QSStatusBar.this.mIconContainer.addIgnoredSlot(str);
            }
        }

        @Override // com.nothingos.systemui.qs.QSStatusBarController.QSSControllerListener
        public void removeIgnoredSlot(String... strArr) {
            for (String str : strArr) {
                QSStatusBar.this.mIconContainer.removeIgnoredSlot(str);
            }
        }
    };

    public QSStatusBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mPrivacyChip = (OngoingPrivacyChip) findViewById(R$id.privacy_chip);
        this.mIconContainer = (StatusIconContainer) findViewById(R$id.qs_statusIcons);
        this.mController = (QSStatusBarController) Dependency.get(QSStatusBarController.class);
        this.mFeatureFlags = (FeatureFlags) Dependency.get(FeatureFlags.class);
        BatteryMeterView batteryMeterView = (BatteryMeterView) findViewById(R$id.qs_batteryRemainingIcon);
        this.mBatteryRemainingIcon = batteryMeterView;
        batteryMeterView.setIgnoreTunerUpdates(true);
        this.mIconManager = new StatusBarIconController.TintedIconManager(this.mIconContainer, this.mFeatureFlags);
        this.mStatusBarIconController = (StatusBarIconController) Dependency.get(StatusBarIconController.class);
        this.mIconManager.setTint(-1);
        updateResources();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mStatusBarIconController.addIconGroup(this.mIconManager);
        this.mController.onViewAttached(this.mListener);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mStatusBarIconController.removeIconGroup(this.mIconManager);
        this.mController.onViewDetached(this.mListener);
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        updateResources();
    }

    private void updateResources() {
        updateSafePadding();
        this.mConfigShowBatteryEstimate = getResources().getBoolean(R$bool.config_showBatteryEstimateQSBH);
        updateBatteryMode();
    }

    private void updateBatteryMode() {
        if (this.mConfigShowBatteryEstimate) {
            this.mBatteryRemainingIcon.setPercentShowMode(3);
        } else {
            this.mBatteryRemainingIcon.setPercentShowMode(1);
        }
    }

    @Override // android.view.View
    public WindowInsets onApplyWindowInsets(WindowInsets windowInsets) {
        updateSafePadding();
        return super.onApplyWindowInsets(windowInsets);
    }

    private void updateSafePadding() {
        int dimensionPixelOffset = getResources().getDimensionPixelOffset(R$dimen.qs_status_bar_margin_left_right);
        Rect statusBarContentInsetsForRotation = ((StatusBarContentInsetsProvider) Dependency.get(StatusBarContentInsetsProvider.class)).getStatusBarContentInsetsForRotation(RotationUtils.getExactRotation(getContext()));
        getBoundsOnScreen(this.mTmpRectForQSStatusBarBoundsOnScreen, true);
        int i = statusBarContentInsetsForRotation.left;
        Rect rect = this.mTmpRectForQSStatusBarBoundsOnScreen;
        int i2 = rect.left;
        int i3 = i > dimensionPixelOffset + i2 ? i - i2 : dimensionPixelOffset;
        int i4 = statusBarContentInsetsForRotation.right;
        int i5 = rect.right;
        if (i4 < i5 - dimensionPixelOffset) {
            dimensionPixelOffset = i5 - i4;
        }
        setPadding(i3, getPaddingTop(), dimensionPixelOffset, getPaddingBottom());
    }
}
