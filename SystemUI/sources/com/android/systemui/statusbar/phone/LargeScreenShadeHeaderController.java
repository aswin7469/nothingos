package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;
import androidx.constraintlayout.motion.widget.MotionLayout;
import com.android.settingslib.Utils;
import com.android.systemui.C1893R;
import com.android.systemui.Dumpable;
import com.android.systemui.animation.ShadeInterpolation;
import com.android.systemui.battery.BatteryMeterView;
import com.android.systemui.battery.BatteryMeterViewController;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.flags.BooleanFlag;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import com.android.systemui.p012qs.ChipVisibilityListener;
import com.android.systemui.p012qs.HeaderPrivacyIconsController;
import com.android.systemui.p012qs.carrier.QSCarrierGroup;
import com.android.systemui.p012qs.carrier.QSCarrierGroupController;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import com.android.systemui.statusbar.phone.dagger.CentralSurfacesComponent;
import com.android.systemui.statusbar.policy.ConfigurationController;
import java.p026io.PrintWriter;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.xmlpull.p032v1.XmlPullParser;

@Metadata(mo64986d1 = {"\u0000\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u000e\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\b\f\b\u0007\u0018\u0000 U2\u00020\u0001:\u0001UBK\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r\u0012\b\b\u0001\u0010\u000e\u001a\u00020\u000f\u0012\u0006\u0010\u0010\u001a\u00020\u0011¢\u0006\u0002\u0010\u0012J\b\u0010@\u001a\u00020AH\u0002J\u001e\u0010B\u001a\u00020A2\u0006\u0010C\u001a\u0002022\u0006\u0010D\u001a\u0002022\u0006\u0010E\u001a\u00020\u0014J%\u0010F\u001a\u00020A2\u0006\u0010G\u001a\u00020H2\u000e\u0010I\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u001c0JH\u0016¢\u0006\u0002\u0010KJ\b\u0010L\u001a\u00020AH\u0002J\b\u0010M\u001a\u00020AH\u0002J\b\u0010N\u001a\u00020AH\u0002J\b\u0010O\u001a\u00020AH\u0002J\b\u0010P\u001a\u00020AH\u0002J\b\u0010Q\u001a\u00020AH\u0002J\u0010\u0010R\u001a\u00020A2\u0006\u0010S\u001a\u00020\u0014H\u0002J\b\u0010T\u001a\u00020AH\u0002R$\u0010\u0015\u001a\u00020\u00142\u0006\u0010\u0013\u001a\u00020\u0014@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u0017\"\u0004\b\u0018\u0010\u0019R\u0014\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u001c0\u001bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u001eX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020 X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\u0014X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\"\u001a\u00020 X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020$X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010%\u001a\u00020&X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010'\u001a\u00020(X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010)\u001a\u00020*X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010+\u001a\u00020\u0014X\u000e¢\u0006\u0002\n\u0000R$\u0010-\u001a\u00020,2\u0006\u0010\u0013\u001a\u00020,@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b.\u0010/\"\u0004\b0\u00101R$\u00103\u001a\u0002022\u0006\u0010\u0013\u001a\u000202@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b4\u00105\"\u0004\b6\u00107R$\u00108\u001a\u00020\u00142\u0006\u0010\u0013\u001a\u00020\u0014@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b9\u0010\u0017\"\u0004\b:\u0010\u0019R$\u0010;\u001a\u00020,2\u0006\u0010\u0013\u001a\u00020,@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b<\u0010/\"\u0004\b=\u00101R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u001e\u0010>\u001a\u00020\u00142\u0006\u0010\u0013\u001a\u00020\u0014@BX\u000e¢\u0006\b\n\u0000\"\u0004\b?\u0010\u0019¨\u0006V"}, mo64987d2 = {"Lcom/android/systemui/statusbar/phone/LargeScreenShadeHeaderController;", "Lcom/android/systemui/Dumpable;", "header", "Landroid/view/View;", "statusBarIconController", "Lcom/android/systemui/statusbar/phone/StatusBarIconController;", "privacyIconsController", "Lcom/android/systemui/qs/HeaderPrivacyIconsController;", "configurationController", "Lcom/android/systemui/statusbar/policy/ConfigurationController;", "qsCarrierGroupControllerBuilder", "Lcom/android/systemui/qs/carrier/QSCarrierGroupController$Builder;", "featureFlags", "Lcom/android/systemui/flags/FeatureFlags;", "batteryMeterViewController", "Lcom/android/systemui/battery/BatteryMeterViewController;", "dumpManager", "Lcom/android/systemui/dump/DumpManager;", "(Landroid/view/View;Lcom/android/systemui/statusbar/phone/StatusBarIconController;Lcom/android/systemui/qs/HeaderPrivacyIconsController;Lcom/android/systemui/statusbar/policy/ConfigurationController;Lcom/android/systemui/qs/carrier/QSCarrierGroupController$Builder;Lcom/android/systemui/flags/FeatureFlags;Lcom/android/systemui/battery/BatteryMeterViewController;Lcom/android/systemui/dump/DumpManager;)V", "value", "", "active", "getActive", "()Z", "setActive", "(Z)V", "carrierIconSlots", "", "", "chipVisibilityListener", "Lcom/android/systemui/qs/ChipVisibilityListener;", "clock", "Landroid/widget/TextView;", "combinedHeaders", "date", "iconContainer", "Lcom/android/systemui/statusbar/phone/StatusIconContainer;", "iconManager", "Lcom/android/systemui/statusbar/phone/StatusBarIconController$TintedIconManager;", "qsCarrierGroup", "Lcom/android/systemui/qs/carrier/QSCarrierGroup;", "qsCarrierGroupController", "Lcom/android/systemui/qs/carrier/QSCarrierGroupController;", "qsDisabled", "", "qsExpandedFraction", "getQsExpandedFraction", "()F", "setQsExpandedFraction", "(F)V", "", "qsScrollY", "getQsScrollY", "()I", "setQsScrollY", "(I)V", "shadeExpanded", "getShadeExpanded", "setShadeExpanded", "shadeExpandedFraction", "getShadeExpandedFraction", "setShadeExpandedFraction", "visible", "setVisible", "bindConfigurationListener", "", "disable", "state1", "state2", "animate", "dump", "pw", "Ljava/io/PrintWriter;", "args", "", "(Ljava/io/PrintWriter;[Ljava/lang/String;)V", "onHeaderStateChanged", "onShadeExpandedChanged", "updateConstraints", "updateListeners", "updatePosition", "updateScrollY", "updateSingleCarrier", "singleCarrier", "updateVisibility", "Companion", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
@CentralSurfacesComponent.CentralSurfacesScope
/* compiled from: LargeScreenShadeHeaderController.kt */
public final class LargeScreenShadeHeaderController implements Dumpable {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final int HEADER_TRANSITION_ID = C1893R.C1897id.header_transition;
    /* access modifiers changed from: private */
    public static final int LARGE_SCREEN_HEADER_CONSTRAINT = C1893R.C1897id.large_screen_header_constraint;
    private static final int LARGE_SCREEN_HEADER_TRANSITION_ID = C1893R.C1897id.large_screen_header_transition;
    /* access modifiers changed from: private */
    public static final int QQS_HEADER_CONSTRAINT = C1893R.C1897id.qqs_header_constraint;
    /* access modifiers changed from: private */
    public static final int QS_HEADER_CONSTRAINT = C1893R.C1897id.qs_header_constraint;
    private boolean active;
    private final List<String> carrierIconSlots;
    private final ChipVisibilityListener chipVisibilityListener;
    /* access modifiers changed from: private */
    public final TextView clock;
    private final boolean combinedHeaders;
    private final ConfigurationController configurationController;
    /* access modifiers changed from: private */
    public final TextView date;
    /* access modifiers changed from: private */
    public final View header;
    private final StatusIconContainer iconContainer;
    private final StatusBarIconController.TintedIconManager iconManager;
    private final HeaderPrivacyIconsController privacyIconsController;
    /* access modifiers changed from: private */
    public final QSCarrierGroup qsCarrierGroup;
    private final QSCarrierGroupController qsCarrierGroupController;
    private boolean qsDisabled;
    private float qsExpandedFraction = -1.0f;
    private int qsScrollY;
    private boolean shadeExpanded;
    private float shadeExpandedFraction = -1.0f;
    private final StatusBarIconController statusBarIconController;
    private boolean visible;

    @Inject
    public LargeScreenShadeHeaderController(@Named("large_screen_shade_header") View view, StatusBarIconController statusBarIconController2, HeaderPrivacyIconsController headerPrivacyIconsController, ConfigurationController configurationController2, QSCarrierGroupController.Builder builder, FeatureFlags featureFlags, @Named("split_shade_battery_controller") BatteryMeterViewController batteryMeterViewController, DumpManager dumpManager) {
        List<String> list;
        Intrinsics.checkNotNullParameter(view, "header");
        Intrinsics.checkNotNullParameter(statusBarIconController2, "statusBarIconController");
        Intrinsics.checkNotNullParameter(headerPrivacyIconsController, "privacyIconsController");
        Intrinsics.checkNotNullParameter(configurationController2, "configurationController");
        Intrinsics.checkNotNullParameter(builder, "qsCarrierGroupControllerBuilder");
        Intrinsics.checkNotNullParameter(featureFlags, "featureFlags");
        Intrinsics.checkNotNullParameter(batteryMeterViewController, "batteryMeterViewController");
        Intrinsics.checkNotNullParameter(dumpManager, "dumpManager");
        this.header = view;
        this.statusBarIconController = statusBarIconController2;
        this.privacyIconsController = headerPrivacyIconsController;
        this.configurationController = configurationController2;
        BooleanFlag booleanFlag = Flags.COMBINED_QS_HEADERS;
        Intrinsics.checkNotNullExpressionValue(booleanFlag, "COMBINED_QS_HEADERS");
        this.combinedHeaders = featureFlags.isEnabled(booleanFlag);
        View findViewById = view.findViewById(C1893R.C1897id.clock);
        Intrinsics.checkNotNullExpressionValue(findViewById, "header.findViewById(R.id.clock)");
        this.clock = (TextView) findViewById;
        View findViewById2 = view.findViewById(C1893R.C1897id.date);
        Intrinsics.checkNotNullExpressionValue(findViewById2, "header.findViewById(R.id.date)");
        this.date = (TextView) findViewById2;
        View findViewById3 = view.findViewById(C1893R.C1897id.carrier_group);
        Intrinsics.checkNotNullExpressionValue(findViewById3, "header.findViewById(R.id.carrier_group)");
        this.qsCarrierGroup = (QSCarrierGroup) findViewById3;
        ChipVisibilityListener largeScreenShadeHeaderController$chipVisibilityListener$1 = new LargeScreenShadeHeaderController$chipVisibilityListener$1(this);
        this.chipVisibilityListener = largeScreenShadeHeaderController$chipVisibilityListener$1;
        if (view instanceof MotionLayout) {
            Context context = ((MotionLayout) view).getContext();
            Resources resources = ((MotionLayout) view).getResources();
            ((MotionLayout) view).getConstraintSet(QQS_HEADER_CONSTRAINT).load(context, (XmlPullParser) resources.getXml(C1893R.C1901xml.qqs_header));
            ((MotionLayout) view).getConstraintSet(QS_HEADER_CONSTRAINT).load(context, (XmlPullParser) resources.getXml(C1893R.C1901xml.qs_header));
            ((MotionLayout) view).getConstraintSet(LARGE_SCREEN_HEADER_CONSTRAINT).load(context, (XmlPullParser) resources.getXml(C1893R.C1901xml.large_screen_shade_header));
            headerPrivacyIconsController.setChipVisibilityListener(largeScreenShadeHeaderController$chipVisibilityListener$1);
        }
        bindConfigurationListener();
        batteryMeterViewController.init();
        View findViewById4 = view.findViewById(C1893R.C1897id.batteryRemainingIcon);
        Intrinsics.checkNotNullExpressionValue(findViewById4, "header.findViewById(R.id.batteryRemainingIcon)");
        batteryMeterViewController.ignoreTunerUpdates();
        ((BatteryMeterView) findViewById4).setPercentShowMode(3);
        View findViewById5 = view.findViewById(C1893R.C1897id.statusIcons);
        Intrinsics.checkNotNullExpressionValue(findViewById5, "header.findViewById(R.id.statusIcons)");
        StatusIconContainer statusIconContainer = (StatusIconContainer) findViewById5;
        this.iconContainer = statusIconContainer;
        StatusBarIconController.TintedIconManager tintedIconManager = new StatusBarIconController.TintedIconManager(statusIconContainer, featureFlags);
        this.iconManager = tintedIconManager;
        tintedIconManager.setTint(Utils.getColorAttrDefaultColor(view.getContext(), 16842806));
        BooleanFlag booleanFlag2 = Flags.COMBINED_STATUS_BAR_SIGNAL_ICONS;
        Intrinsics.checkNotNullExpressionValue(booleanFlag2, "COMBINED_STATUS_BAR_SIGNAL_ICONS");
        if (featureFlags.isEnabled(booleanFlag2)) {
            list = CollectionsKt.listOf(view.getContext().getString(17041574), view.getContext().getString(17041557));
        } else {
            list = CollectionsKt.listOf(view.getContext().getString(17041571));
        }
        this.carrierIconSlots = list;
        QSCarrierGroupController build = builder.setQSCarrierGroup((QSCarrierGroup) view.findViewById(C1893R.C1897id.carrier_group)).build();
        Intrinsics.checkNotNullExpressionValue(build, "qsCarrierGroupController…\n                .build()");
        this.qsCarrierGroupController = build;
        dumpManager.registerDumpable(this);
        updateVisibility();
        updateConstraints();
    }

    @Metadata(mo64986d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\f\u0010\t\u001a\u00020\n*\u00020\u0004H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XD¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XD¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004XD¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004XD¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004XD¢\u0006\u0002\n\u0000¨\u0006\u000b"}, mo64987d2 = {"Lcom/android/systemui/statusbar/phone/LargeScreenShadeHeaderController$Companion;", "", "()V", "HEADER_TRANSITION_ID", "", "LARGE_SCREEN_HEADER_CONSTRAINT", "LARGE_SCREEN_HEADER_TRANSITION_ID", "QQS_HEADER_CONSTRAINT", "QS_HEADER_CONSTRAINT", "stateToString", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: LargeScreenShadeHeaderController.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        /* access modifiers changed from: private */
        public final String stateToString(int i) {
            if (i == LargeScreenShadeHeaderController.QQS_HEADER_CONSTRAINT) {
                return "QQS Header";
            }
            if (i == LargeScreenShadeHeaderController.QS_HEADER_CONSTRAINT) {
                return "QS Header";
            }
            return i == LargeScreenShadeHeaderController.LARGE_SCREEN_HEADER_CONSTRAINT ? "Large Screen Header" : "Unknown state";
        }
    }

    private final void setVisible(boolean z) {
        if (this.visible != z) {
            this.visible = z;
            updateListeners();
        }
    }

    public final boolean getShadeExpanded() {
        return this.shadeExpanded;
    }

    public final void setShadeExpanded(boolean z) {
        if (this.shadeExpanded != z) {
            this.shadeExpanded = z;
            onShadeExpandedChanged();
        }
    }

    public final boolean getActive() {
        return this.active;
    }

    public final void setActive(boolean z) {
        if (this.active != z) {
            this.active = z;
            onHeaderStateChanged();
        }
    }

    public final float getShadeExpandedFraction() {
        return this.shadeExpandedFraction;
    }

    public final void setShadeExpandedFraction(float f) {
        if (this.visible) {
            if (!(this.shadeExpandedFraction == f)) {
                this.header.setAlpha(ShadeInterpolation.getContentAlpha(f));
                this.shadeExpandedFraction = f;
            }
        }
    }

    public final float getQsExpandedFraction() {
        return this.qsExpandedFraction;
    }

    public final void setQsExpandedFraction(float f) {
        if (this.visible) {
            if (!(this.qsExpandedFraction == f)) {
                this.qsExpandedFraction = f;
                updateVisibility();
                updatePosition();
            }
        }
    }

    public final int getQsScrollY() {
        return this.qsScrollY;
    }

    public final void setQsScrollY(int i) {
        if (this.qsScrollY != i) {
            this.qsScrollY = i;
            updateScrollY();
        }
    }

    public final void disable(int i, int i2, boolean z) {
        boolean z2 = true;
        if ((i2 & 1) == 0) {
            z2 = false;
        }
        if (z2 != this.qsDisabled) {
            this.qsDisabled = z2;
            updateVisibility();
        }
    }

    private final void updateScrollY() {
        if (!this.active && this.combinedHeaders) {
            this.header.setScrollY(this.qsScrollY);
        }
    }

    private final void bindConfigurationListener() {
        this.configurationController.addCallback(new C2993x12adfc0d(this));
    }

    private final void onShadeExpandedChanged() {
        if (this.shadeExpanded) {
            this.privacyIconsController.startListening();
        } else {
            this.privacyIconsController.stopListening();
        }
        updateVisibility();
        updatePosition();
    }

    private final void onHeaderStateChanged() {
        if (this.active || this.combinedHeaders) {
            this.privacyIconsController.onParentVisible();
        } else {
            this.privacyIconsController.onParentInvisible();
        }
        updateVisibility();
        updateConstraints();
    }

    private final void updateVisibility() {
        int i;
        boolean z = false;
        if ((this.active || this.combinedHeaders) && !this.qsDisabled) {
            i = this.shadeExpanded ? 0 : 4;
        } else {
            i = 8;
        }
        if (this.header.getVisibility() != i) {
            this.header.setVisibility(i);
            if (i == 0) {
                z = true;
            }
            setVisible(z);
        }
    }

    private final void updateConstraints() {
        if (this.combinedHeaders) {
            View view = this.header;
            MotionLayout motionLayout = (MotionLayout) view;
            if (this.active) {
                ((MotionLayout) view).setTransition(LARGE_SCREEN_HEADER_TRANSITION_ID);
                return;
            }
            ((MotionLayout) view).setTransition(HEADER_TRANSITION_ID);
            ((MotionLayout) this.header).transitionToStart();
            updatePosition();
            updateScrollY();
        }
    }

    private final void updatePosition() {
        View view = this.header;
        if ((view instanceof MotionLayout) && !this.active && this.visible) {
            ((MotionLayout) view).setProgress(this.qsExpandedFraction);
        }
    }

    private final void updateListeners() {
        this.qsCarrierGroupController.setListening(this.visible);
        if (this.visible) {
            updateSingleCarrier(this.qsCarrierGroupController.isSingleCarrier());
            this.qsCarrierGroupController.setOnSingleCarrierChangedListener(new LargeScreenShadeHeaderController$$ExternalSyntheticLambda0(this));
            this.statusBarIconController.addIconGroup(this.iconManager);
            return;
        }
        this.qsCarrierGroupController.setOnSingleCarrierChangedListener((QSCarrierGroupController.OnSingleCarrierChangedListener) null);
        this.statusBarIconController.removeIconGroup(this.iconManager);
    }

    /* access modifiers changed from: private */
    /* renamed from: updateListeners$lambda-0  reason: not valid java name */
    public static final void m3179updateListeners$lambda0(LargeScreenShadeHeaderController largeScreenShadeHeaderController, boolean z) {
        Intrinsics.checkNotNullParameter(largeScreenShadeHeaderController, "this$0");
        largeScreenShadeHeaderController.updateSingleCarrier(z);
    }

    private final void updateSingleCarrier(boolean z) {
        if (z) {
            this.iconContainer.removeIgnoredSlots(this.carrierIconSlots);
        } else {
            this.iconContainer.addIgnoredSlots(this.carrierIconSlots);
        }
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(strArr, "args");
        printWriter.println("visible: " + this.visible);
        printWriter.println("shadeExpanded: " + this.shadeExpanded);
        printWriter.println("shadeExpandedFraction: " + this.shadeExpandedFraction);
        printWriter.println("active: " + this.active);
        printWriter.println("qsExpandedFraction: " + this.qsExpandedFraction);
        printWriter.println("qsScrollY: " + this.qsScrollY);
        if (this.combinedHeaders) {
            MotionLayout motionLayout = (MotionLayout) this.header;
            printWriter.println("currentState: " + Companion.stateToString(((MotionLayout) this.header).getCurrentState()));
        }
    }
}
