package com.android.systemui.p012qs;

import android.os.Bundle;
import com.android.internal.colorextraction.ColorExtractor;
import com.android.systemui.C1893R;
import com.android.systemui.battery.BatteryMeterViewController;
import com.android.systemui.colorextraction.SysuiColorExtractor;
import com.android.systemui.demomode.DemoMode;
import com.android.systemui.demomode.DemoModeController;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import com.android.systemui.p012qs.carrier.QSCarrierGroup;
import com.android.systemui.p012qs.carrier.QSCarrierGroupController;
import com.android.systemui.p012qs.dagger.QSScope;
import com.android.systemui.statusbar.phone.StatusBarContentInsetsProvider;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import com.android.systemui.statusbar.phone.StatusIconContainer;
import com.android.systemui.statusbar.policy.Clock;
import com.android.systemui.statusbar.policy.VariableDateView;
import com.android.systemui.statusbar.policy.VariableDateViewController;
import com.android.systemui.util.ViewController;
import java.util.List;
import java.util.Objects;
import javax.inject.Inject;

@QSScope
/* renamed from: com.android.systemui.qs.QuickStatusBarHeaderController */
class QuickStatusBarHeaderController extends ViewController<QuickStatusBarHeader> implements ChipVisibilityListener {
    private final BatteryMeterViewController mBatteryMeterViewController;
    private final Clock mClockView;
    private SysuiColorExtractor mColorExtractor;
    private final DemoModeController mDemoModeController;
    private final DemoMode mDemoModeReceiver;
    private final FeatureFlags mFeatureFlags;
    private final StatusIconContainer mIconContainer;
    private final StatusBarIconController.TintedIconManager mIconManager;
    private final StatusBarContentInsetsProvider mInsetsProvider;
    private boolean mListening;
    private ColorExtractor.OnColorsChangedListener mOnColorsChangedListener;
    private final HeaderPrivacyIconsController mPrivacyIconsController;
    private final QSCarrierGroupController mQSCarrierGroupController;
    private final QSExpansionPathInterpolator mQSExpansionPathInterpolator;
    private final QuickQSPanelController mQuickQSPanelController;
    private final StatusBarIconController mStatusBarIconController;
    private final VariableDateViewController mVariableDateViewControllerClockDateView;
    private final VariableDateViewController mVariableDateViewControllerDateView;

    @Inject
    QuickStatusBarHeaderController(QuickStatusBarHeader quickStatusBarHeader, HeaderPrivacyIconsController headerPrivacyIconsController, StatusBarIconController statusBarIconController, DemoModeController demoModeController, QuickQSPanelController quickQSPanelController, QSCarrierGroupController.Builder builder, SysuiColorExtractor sysuiColorExtractor, QSExpansionPathInterpolator qSExpansionPathInterpolator, FeatureFlags featureFlags, VariableDateViewController.Factory factory, BatteryMeterViewController batteryMeterViewController, StatusBarContentInsetsProvider statusBarContentInsetsProvider) {
        super(quickStatusBarHeader);
        this.mPrivacyIconsController = headerPrivacyIconsController;
        this.mStatusBarIconController = statusBarIconController;
        this.mDemoModeController = demoModeController;
        this.mQuickQSPanelController = quickQSPanelController;
        this.mQSExpansionPathInterpolator = qSExpansionPathInterpolator;
        this.mFeatureFlags = featureFlags;
        this.mBatteryMeterViewController = batteryMeterViewController;
        this.mInsetsProvider = statusBarContentInsetsProvider;
        this.mQSCarrierGroupController = builder.setQSCarrierGroup((QSCarrierGroup) ((QuickStatusBarHeader) this.mView).findViewById(C1893R.C1897id.carrier_group)).build();
        Clock clock = (Clock) ((QuickStatusBarHeader) this.mView).findViewById(C1893R.C1897id.clock);
        this.mClockView = clock;
        StatusIconContainer statusIconContainer = (StatusIconContainer) ((QuickStatusBarHeader) this.mView).findViewById(C1893R.C1897id.statusIcons);
        this.mIconContainer = statusIconContainer;
        this.mVariableDateViewControllerDateView = factory.create((VariableDateView) ((QuickStatusBarHeader) this.mView).requireViewById(C1893R.C1897id.date));
        this.mVariableDateViewControllerClockDateView = factory.create((VariableDateView) ((QuickStatusBarHeader) this.mView).requireViewById(C1893R.C1897id.date_clock));
        this.mIconManager = new StatusBarIconController.TintedIconManager(statusIconContainer, featureFlags);
        this.mDemoModeReceiver = new ClockDemoModeReceiver(clock);
        this.mColorExtractor = sysuiColorExtractor;
        QuickStatusBarHeaderController$$ExternalSyntheticLambda0 quickStatusBarHeaderController$$ExternalSyntheticLambda0 = new QuickStatusBarHeaderController$$ExternalSyntheticLambda0(this);
        this.mOnColorsChangedListener = quickStatusBarHeaderController$$ExternalSyntheticLambda0;
        this.mColorExtractor.addOnColorsChangedListener(quickStatusBarHeaderController$$ExternalSyntheticLambda0);
        batteryMeterViewController.ignoreTunerUpdates();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-systemui-qs-QuickStatusBarHeaderController */
    public /* synthetic */ void mo36351x3675a37d(ColorExtractor colorExtractor, int i) {
        this.mClockView.onColorsChanged(this.mColorExtractor.getNeutralColors().supportsDarkText());
    }

    /* access modifiers changed from: protected */
    public void onInit() {
        this.mBatteryMeterViewController.init();
    }

    /* access modifiers changed from: protected */
    public void onViewAttached() {
        List list;
        this.mIconContainer.setShouldRestrictIcons(false);
        this.mStatusBarIconController.addIconGroup(this.mIconManager);
        ((QuickStatusBarHeader) this.mView).setIsSingleCarrier(this.mQSCarrierGroupController.isSingleCarrier());
        QSCarrierGroupController qSCarrierGroupController = this.mQSCarrierGroupController;
        QuickStatusBarHeader quickStatusBarHeader = (QuickStatusBarHeader) this.mView;
        Objects.requireNonNull(quickStatusBarHeader);
        qSCarrierGroupController.setOnSingleCarrierChangedListener(new QuickStatusBarHeaderController$$ExternalSyntheticLambda1(quickStatusBarHeader));
        if (this.mFeatureFlags.isEnabled(Flags.COMBINED_STATUS_BAR_SIGNAL_ICONS)) {
            list = List.m1724of(getResources().getString(17041574), getResources().getString(17041557));
        } else {
            list = List.m1723of(getResources().getString(17041571));
        }
        ((QuickStatusBarHeader) this.mView).onAttach(this.mIconManager, this.mQSExpansionPathInterpolator, list, this.mInsetsProvider, this.mFeatureFlags.isEnabled(Flags.COMBINED_QS_HEADERS));
        this.mDemoModeController.addCallback(this.mDemoModeReceiver);
        this.mVariableDateViewControllerDateView.init();
        this.mVariableDateViewControllerClockDateView.init();
    }

    /* access modifiers changed from: protected */
    public void onViewDetached() {
        this.mColorExtractor.removeOnColorsChangedListener(this.mOnColorsChangedListener);
        this.mPrivacyIconsController.onParentInvisible();
        this.mStatusBarIconController.removeIconGroup(this.mIconManager);
        this.mQSCarrierGroupController.setOnSingleCarrierChangedListener((QSCarrierGroupController.OnSingleCarrierChangedListener) null);
        this.mDemoModeController.removeCallback(this.mDemoModeReceiver);
        setListening(false);
    }

    public void setListening(boolean z) {
        this.mQSCarrierGroupController.setListening(z);
        if (z != this.mListening) {
            this.mListening = z;
            this.mQuickQSPanelController.setListening(z);
            if (this.mQuickQSPanelController.switchTileLayout(false)) {
                ((QuickStatusBarHeader) this.mView).updateResources();
            }
        }
    }

    public void onChipVisibilityRefreshed(boolean z) {
        ((QuickStatusBarHeader) this.mView).setChipVisibility(z);
    }

    public void setContentMargins(int i, int i2) {
        this.mQuickQSPanelController.setContentMargins(i, i2);
    }

    /* renamed from: com.android.systemui.qs.QuickStatusBarHeaderController$ClockDemoModeReceiver */
    private static class ClockDemoModeReceiver implements DemoMode {
        private Clock mClockView;

        public List<String> demoCommands() {
            return List.m1723of(DemoMode.COMMAND_CLOCK);
        }

        ClockDemoModeReceiver(Clock clock) {
            this.mClockView = clock;
        }

        public void dispatchDemoCommand(String str, Bundle bundle) {
            this.mClockView.dispatchDemoCommand(str, bundle);
        }

        public void onDemoModeStarted() {
            this.mClockView.onDemoModeStarted();
        }

        public void onDemoModeFinished() {
            this.mClockView.onDemoModeFinished();
        }
    }
}
