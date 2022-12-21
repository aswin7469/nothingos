package com.android.systemui.p012qs;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.UserManager;
import android.view.View;
import android.view.ViewGroup;
import androidx.constraintlayout.motion.widget.Key;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.C1893R;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.globalactions.GlobalActionsDialogLite;
import com.android.systemui.p012qs.TouchAnimator;
import com.android.systemui.p012qs.dagger.QSScope;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.phone.MultiUserSwitchController;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.statusbar.policy.UserInfoController;
import com.android.systemui.util.ViewController;
import com.android.systemui.util.settings.GlobalSettings;
import java.net.HttpURLConnection;
import javax.inject.Provider;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@QSScope
@Metadata(mo64986d1 = {"\u0000µ\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\f*\u0001,\b\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0001\b\u0007\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r\u0012\u0006\u0010\u000e\u001a\u00020\u000f\u0012\u0006\u0010\u0010\u001a\u00020\u0011\u0012\u0006\u0010\u0012\u001a\u00020\u0013\u0012\u0006\u0010\u0014\u001a\u00020\u0015\u0012\u0006\u0010\u0016\u001a\u00020\u0017\u0012\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u001a0\u0019\u0012\u0006\u0010\u001b\u001a\u00020\u001c\u0012\b\b\u0001\u0010\u001d\u001a\u00020\u001e\u0012\u0006\u0010\u001f\u001a\u00020 \u0012\u0006\u0010!\u001a\u00020\"¢\u0006\u0002\u0010#J\u000e\u0010D\u001a\u00020E2\u0006\u0010F\u001a\u00020GJ\b\u0010H\u001a\u00020EH\u0014J\b\u0010I\u001a\u00020EH\u0017J\b\u0010J\u001a\u00020EH\u0014J\u000e\u0010K\u001a\u00020E2\u0006\u0010L\u001a\u00020)J\u000e\u0010M\u001a\u00020E2\u0006\u0010N\u001a\u00020\u001eJ\u000e\u0010O\u001a\u00020E2\u0006\u0010*\u001a\u00020\u001eJ\b\u0010P\u001a\u00020EH\u0002J\b\u0010Q\u001a\u00020EH\u0002J\b\u0010R\u001a\u00020EH\u0002R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u0016\u0010$\u001a\n &*\u0004\u0018\u00010%0%X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0015X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010'\u001a\u0004\u0018\u00010\u001aX\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u001a0\u0019X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020 X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\"X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010(\u001a\u00020)X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010*\u001a\u00020\u001eX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0017X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010+\u001a\u00020,X\u0004¢\u0006\u0004\n\u0002\u0010-R\u0016\u0010.\u001a\n &*\u0004\u0018\u00010/0/X\u0004¢\u0006\u0002\n\u0000R\u000e\u00100\u001a\u000201X\u0004¢\u0006\u0002\n\u0000R\u000e\u00102\u001a\u000203X\u0004¢\u0006\u0002\n\u0000R\u000e\u00104\u001a\u000205X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0004¢\u0006\u0002\n\u0000R\u0010\u00106\u001a\u0004\u0018\u000107X\u0004¢\u0006\u0002\n\u0000R\u001c\u00108\u001a\u0002058\u0000X\u0004¢\u0006\u000e\n\u0000\u0012\u0004\b9\u0010:\u001a\u0004\b;\u0010<R\u000e\u0010=\u001a\u000205X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u001eX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u001cX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\u0002\n\u0000R$\u0010?\u001a\u00020\u001e2\u0006\u0010>\u001a\u00020\u001e@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b@\u0010A\"\u0004\bB\u0010C¨\u0006S"}, mo64987d2 = {"Lcom/android/systemui/qs/FooterActionsController;", "Lcom/android/systemui/util/ViewController;", "Lcom/android/systemui/qs/FooterActionsView;", "view", "multiUserSwitchControllerFactory", "Lcom/android/systemui/statusbar/phone/MultiUserSwitchController$Factory;", "activityStarter", "Lcom/android/systemui/plugins/ActivityStarter;", "userManager", "Landroid/os/UserManager;", "userTracker", "Lcom/android/systemui/settings/UserTracker;", "userInfoController", "Lcom/android/systemui/statusbar/policy/UserInfoController;", "deviceProvisionedController", "Lcom/android/systemui/statusbar/policy/DeviceProvisionedController;", "securityFooterController", "Lcom/android/systemui/qs/QSSecurityFooter;", "fgsManagerFooterController", "Lcom/android/systemui/qs/QSFgsManagerFooter;", "falsingManager", "Lcom/android/systemui/plugins/FalsingManager;", "metricsLogger", "Lcom/android/internal/logging/MetricsLogger;", "globalActionsDialogProvider", "Ljavax/inject/Provider;", "Lcom/android/systemui/globalactions/GlobalActionsDialogLite;", "uiEventLogger", "Lcom/android/internal/logging/UiEventLogger;", "showPMLiteButton", "", "globalSetting", "Lcom/android/systemui/util/settings/GlobalSettings;", "handler", "Landroid/os/Handler;", "(Lcom/android/systemui/qs/FooterActionsView;Lcom/android/systemui/statusbar/phone/MultiUserSwitchController$Factory;Lcom/android/systemui/plugins/ActivityStarter;Landroid/os/UserManager;Lcom/android/systemui/settings/UserTracker;Lcom/android/systemui/statusbar/policy/UserInfoController;Lcom/android/systemui/statusbar/policy/DeviceProvisionedController;Lcom/android/systemui/qs/QSSecurityFooter;Lcom/android/systemui/qs/QSFgsManagerFooter;Lcom/android/systemui/plugins/FalsingManager;Lcom/android/internal/logging/MetricsLogger;Ljavax/inject/Provider;Lcom/android/internal/logging/UiEventLogger;ZLcom/android/systemui/util/settings/GlobalSettings;Landroid/os/Handler;)V", "alphaAnimator", "Lcom/android/systemui/qs/TouchAnimator;", "kotlin.jvm.PlatformType", "globalActionsDialog", "lastExpansion", "", "listening", "multiUserSetting", "com/android/systemui/qs/FooterActionsController$multiUserSetting$1", "Lcom/android/systemui/qs/FooterActionsController$multiUserSetting$1;", "multiUserSwitchController", "Lcom/android/systemui/statusbar/phone/MultiUserSwitchController;", "onClickListener", "Landroid/view/View$OnClickListener;", "onUserInfoChangedListener", "Lcom/android/systemui/statusbar/policy/UserInfoController$OnUserInfoChangedListener;", "powerMenuLite", "Landroid/view/View;", "securityFootersContainer", "Landroid/view/ViewGroup;", "securityFootersSeparator", "getSecurityFootersSeparator$SystemUI_nothingRelease$annotations", "()V", "getSecurityFootersSeparator$SystemUI_nothingRelease", "()Landroid/view/View;", "settingsButtonContainer", "value", "visible", "getVisible", "()Z", "setVisible", "(Z)V", "disable", "", "state2", "", "onInit", "onViewAttached", "onViewDetached", "setExpansion", "headerExpansionFraction", "setKeyguardShowing", "showing", "setListening", "startSettingsActivity", "updateView", "updateVisibility", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.systemui.qs.FooterActionsController */
/* compiled from: FooterActionsController.kt */
public final class FooterActionsController extends ViewController<FooterActionsView> {
    private final ActivityStarter activityStarter;
    private final TouchAnimator alphaAnimator = new TouchAnimator.Builder().addFloat(this.mView, Key.ALPHA, 0.0f, 1.0f).setStartDelay(0.9f).build();
    private final DeviceProvisionedController deviceProvisionedController;
    private final FalsingManager falsingManager;
    private final QSFgsManagerFooter fgsManagerFooterController;
    private GlobalActionsDialogLite globalActionsDialog;
    private final Provider<GlobalActionsDialogLite> globalActionsDialogProvider;
    private final GlobalSettings globalSetting;
    private final Handler handler;
    private float lastExpansion = -1.0f;
    private boolean listening;
    private final MetricsLogger metricsLogger;
    private final FooterActionsController$multiUserSetting$1 multiUserSetting;
    private final MultiUserSwitchController multiUserSwitchController;
    private final View.OnClickListener onClickListener;
    private final UserInfoController.OnUserInfoChangedListener onUserInfoChangedListener;
    private final View powerMenuLite;
    private final QSSecurityFooter securityFooterController;
    private final ViewGroup securityFootersContainer;
    private final View securityFootersSeparator;
    private final View settingsButtonContainer;
    private final boolean showPMLiteButton;
    private final UiEventLogger uiEventLogger;
    private final UserInfoController userInfoController;
    private final UserManager userManager;
    private final UserTracker userTracker;
    private boolean visible = true;

    public static /* synthetic */ void getSecurityFootersSeparator$SystemUI_nothingRelease$annotations() {
    }

    /* access modifiers changed from: private */
    /* renamed from: onClickListener$lambda-3$lambda-2  reason: not valid java name */
    public static final void m2896onClickListener$lambda3$lambda2() {
    }

    /* JADX WARNING: Illegal instructions before constructor call */
    @javax.inject.Inject
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public FooterActionsController(com.android.systemui.p012qs.FooterActionsView r17, com.android.systemui.statusbar.phone.MultiUserSwitchController.Factory r18, com.android.systemui.plugins.ActivityStarter r19, android.os.UserManager r20, com.android.systemui.settings.UserTracker r21, com.android.systemui.statusbar.policy.UserInfoController r22, com.android.systemui.statusbar.policy.DeviceProvisionedController r23, com.android.systemui.p012qs.QSSecurityFooter r24, com.android.systemui.p012qs.QSFgsManagerFooter r25, com.android.systemui.plugins.FalsingManager r26, com.android.internal.logging.MetricsLogger r27, javax.inject.Provider<com.android.systemui.globalactions.GlobalActionsDialogLite> r28, com.android.internal.logging.UiEventLogger r29, @javax.inject.Named("pm_lite") boolean r30, com.android.systemui.util.settings.GlobalSettings r31, android.os.Handler r32) {
        /*
            r16 = this;
            r0 = r16
            r1 = r17
            r2 = r18
            r3 = r19
            r4 = r20
            r5 = r21
            r6 = r22
            r7 = r23
            r8 = r24
            r9 = r25
            r10 = r26
            r11 = r27
            r12 = r28
            r13 = r29
            r14 = r31
            r15 = r32
            java.lang.String r0 = "view"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r1, r0)
            java.lang.String r0 = "multiUserSwitchControllerFactory"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r2, r0)
            java.lang.String r0 = "activityStarter"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r3, r0)
            java.lang.String r0 = "userManager"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r4, r0)
            java.lang.String r0 = "userTracker"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r5, r0)
            java.lang.String r0 = "userInfoController"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r6, r0)
            java.lang.String r0 = "deviceProvisionedController"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r7, r0)
            java.lang.String r0 = "securityFooterController"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r8, r0)
            java.lang.String r0 = "fgsManagerFooterController"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r9, r0)
            java.lang.String r0 = "falsingManager"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r10, r0)
            java.lang.String r0 = "metricsLogger"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r11, r0)
            java.lang.String r0 = "globalActionsDialogProvider"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r12, r0)
            java.lang.String r0 = "uiEventLogger"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r13, r0)
            java.lang.String r0 = "globalSetting"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r14, r0)
            java.lang.String r0 = "handler"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r15, r0)
            r0 = r1
            android.view.View r0 = (android.view.View) r0
            r2 = r16
            r2.<init>(r0)
            r2.activityStarter = r3
            r2.userManager = r4
            r2.userTracker = r5
            r2.userInfoController = r6
            r2.deviceProvisionedController = r7
            r2.securityFooterController = r8
            r2.fgsManagerFooterController = r9
            r2.falsingManager = r10
            r2.metricsLogger = r11
            r2.globalActionsDialogProvider = r12
            r2.uiEventLogger = r13
            r0 = r30
            r2.showPMLiteButton = r0
            r2.globalSetting = r14
            r2.handler = r15
            r0 = -1082130432(0xffffffffbf800000, float:-1.0)
            r2.lastExpansion = r0
            com.android.systemui.qs.TouchAnimator$Builder r0 = new com.android.systemui.qs.TouchAnimator$Builder
            r0.<init>()
            android.view.View r3 = r2.mView
            r4 = 2
            float[] r4 = new float[r4]
            r4 = {0, 1065353216} // fill-array
            java.lang.String r6 = "alpha"
            com.android.systemui.qs.TouchAnimator$Builder r0 = r0.addFloat(r3, r6, r4)
            r3 = 1063675494(0x3f666666, float:0.9)
            com.android.systemui.qs.TouchAnimator$Builder r0 = r0.setStartDelay(r3)
            com.android.systemui.qs.TouchAnimator r0 = r0.build()
            r2.alphaAnimator = r0
            r0 = 1
            r2.visible = r0
            r0 = 2131428825(0x7f0b05d9, float:1.8479305E38)
            android.view.View r0 = r1.findViewById(r0)
            java.lang.String r3 = "view.findViewById(R.id.settings_button_container)"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r0, r3)
            r2.settingsButtonContainer = r0
            r0 = 2131428806(0x7f0b05c6, float:1.8479267E38)
            android.view.View r0 = r1.findViewById(r0)
            android.view.ViewGroup r0 = (android.view.ViewGroup) r0
            r2.securityFootersContainer = r0
            r0 = 2131428595(0x7f0b04f3, float:1.8478839E38)
            android.view.View r0 = r1.findViewById(r0)
            java.lang.String r3 = "view.findViewById(R.id.pm_lite)"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r0, r3)
            r2.powerMenuLite = r0
            r0 = r2
            r2 = r18
            com.android.systemui.statusbar.phone.MultiUserSwitchController r1 = r2.create(r1)
            r0.multiUserSwitchController = r1
            android.view.View r1 = new android.view.View
            android.content.Context r2 = r16.getContext()
            r1.<init>(r2)
            r2 = 8
            r1.setVisibility(r2)
            r0.securityFootersSeparator = r1
            com.android.systemui.qs.FooterActionsController$$ExternalSyntheticLambda1 r1 = new com.android.systemui.qs.FooterActionsController$$ExternalSyntheticLambda1
            r1.<init>(r0)
            r0.onUserInfoChangedListener = r1
            int r1 = r21.getUserId()
            com.android.systemui.qs.FooterActionsController$multiUserSetting$1 r2 = new com.android.systemui.qs.FooterActionsController$multiUserSetting$1
            r2.<init>(r0, r14, r15, r1)
            r0.multiUserSetting = r2
            com.android.systemui.qs.FooterActionsController$$ExternalSyntheticLambda2 r1 = new com.android.systemui.qs.FooterActionsController$$ExternalSyntheticLambda2
            r1.<init>(r0)
            r0.onClickListener = r1
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.p012qs.FooterActionsController.<init>(com.android.systemui.qs.FooterActionsView, com.android.systemui.statusbar.phone.MultiUserSwitchController$Factory, com.android.systemui.plugins.ActivityStarter, android.os.UserManager, com.android.systemui.settings.UserTracker, com.android.systemui.statusbar.policy.UserInfoController, com.android.systemui.statusbar.policy.DeviceProvisionedController, com.android.systemui.qs.QSSecurityFooter, com.android.systemui.qs.QSFgsManagerFooter, com.android.systemui.plugins.FalsingManager, com.android.internal.logging.MetricsLogger, javax.inject.Provider, com.android.internal.logging.UiEventLogger, boolean, com.android.systemui.util.settings.GlobalSettings, android.os.Handler):void");
    }

    public final boolean getVisible() {
        return this.visible;
    }

    public final void setVisible(boolean z) {
        this.visible = z;
        updateVisibility();
    }

    public final View getSecurityFootersSeparator$SystemUI_nothingRelease() {
        return this.securityFootersSeparator;
    }

    /* access modifiers changed from: private */
    /* renamed from: onUserInfoChangedListener$lambda-1  reason: not valid java name */
    public static final void m2897onUserInfoChangedListener$lambda1(FooterActionsController footerActionsController, String str, Drawable drawable, String str2) {
        Intrinsics.checkNotNullParameter(footerActionsController, "this$0");
        ((FooterActionsView) footerActionsController.mView).onUserInfoChanged(drawable, footerActionsController.userManager.isGuestUser(KeyguardUpdateMonitor.getCurrentUser()));
    }

    /* access modifiers changed from: private */
    /* renamed from: onClickListener$lambda-3  reason: not valid java name */
    public static final void m2895onClickListener$lambda3(FooterActionsController footerActionsController, View view) {
        Intrinsics.checkNotNullParameter(footerActionsController, "this$0");
        if (footerActionsController.visible && !footerActionsController.falsingManager.isFalseTap(1)) {
            if (view == footerActionsController.settingsButtonContainer) {
                if (!footerActionsController.deviceProvisionedController.isCurrentUserSetup()) {
                    footerActionsController.activityStarter.postQSRunnableDismissingKeyguard(new FooterActionsController$$ExternalSyntheticLambda0());
                    return;
                }
                footerActionsController.metricsLogger.action(HttpURLConnection.HTTP_NOT_ACCEPTABLE);
                footerActionsController.startSettingsActivity();
            } else if (view == footerActionsController.powerMenuLite) {
                footerActionsController.uiEventLogger.log(GlobalActionsDialogLite.GlobalActionsEvent.GA_OPEN_QS);
                GlobalActionsDialogLite globalActionsDialogLite = footerActionsController.globalActionsDialog;
                if (globalActionsDialogLite != null) {
                    globalActionsDialogLite.showOrHideDialog(false, true, view);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onInit() {
        this.multiUserSwitchController.init();
        this.securityFooterController.init();
        this.fgsManagerFooterController.init();
    }

    private final void updateVisibility() {
        int visibility = ((FooterActionsView) this.mView).getVisibility();
        ((FooterActionsView) this.mView).setVisibility(this.visible ? 0 : 4);
        if (visibility != ((FooterActionsView) this.mView).getVisibility()) {
            updateView();
        }
    }

    private final void startSettingsActivity() {
        View view = this.settingsButtonContainer;
        this.activityStarter.startActivity(new Intent("android.settings.SETTINGS"), true, view != null ? ActivityLaunchAnimator.Controller.Companion.fromView(view, 33) : null);
    }

    public void onViewAttached() {
        this.globalActionsDialog = this.globalActionsDialogProvider.get();
        if (this.showPMLiteButton) {
            this.powerMenuLite.setVisibility(0);
            this.powerMenuLite.setOnClickListener(this.onClickListener);
        } else {
            this.powerMenuLite.setVisibility(8);
        }
        this.settingsButtonContainer.setOnClickListener(this.onClickListener);
        this.multiUserSetting.setListening(true);
        View view = this.securityFooterController.getView();
        ViewGroup viewGroup = this.securityFootersContainer;
        if (viewGroup != null) {
            viewGroup.addView(view);
        }
        int dimensionPixelSize = getResources().getDimensionPixelSize(C1893R.dimen.qs_footer_action_inset);
        ViewGroup viewGroup2 = this.securityFootersContainer;
        if (viewGroup2 != null) {
            viewGroup2.addView(this.securityFootersSeparator, dimensionPixelSize, 1);
        }
        View view2 = this.fgsManagerFooterController.getView();
        ViewGroup viewGroup3 = this.securityFootersContainer;
        if (viewGroup3 != null) {
            viewGroup3.addView(view2);
        }
        FooterActionsController$$ExternalSyntheticLambda3 footerActionsController$$ExternalSyntheticLambda3 = new FooterActionsController$$ExternalSyntheticLambda3(view, view2, this);
        this.securityFooterController.setOnVisibilityChangedListener(footerActionsController$$ExternalSyntheticLambda3);
        this.fgsManagerFooterController.setOnVisibilityChangedListener(footerActionsController$$ExternalSyntheticLambda3);
        updateView();
    }

    /* access modifiers changed from: private */
    /* renamed from: onViewAttached$lambda-5  reason: not valid java name */
    public static final void m2898onViewAttached$lambda5(View view, View view2, FooterActionsController footerActionsController, int i) {
        Intrinsics.checkNotNullParameter(footerActionsController, "this$0");
        boolean z = false;
        if (view.getVisibility() == 0 && view2.getVisibility() == 0) {
            footerActionsController.securityFootersSeparator.setVisibility(0);
        } else {
            footerActionsController.securityFootersSeparator.setVisibility(8);
        }
        QSFgsManagerFooter qSFgsManagerFooter = footerActionsController.fgsManagerFooterController;
        if (view.getVisibility() == 0) {
            z = true;
        }
        qSFgsManagerFooter.setCollapsed(z);
    }

    /* access modifiers changed from: private */
    public final void updateView() {
        ((FooterActionsView) this.mView).updateEverything(this.multiUserSwitchController.isMultiUserEnabled());
    }

    /* access modifiers changed from: protected */
    public void onViewDetached() {
        GlobalActionsDialogLite globalActionsDialogLite = this.globalActionsDialog;
        if (globalActionsDialogLite != null) {
            globalActionsDialogLite.destroy();
        }
        this.globalActionsDialog = null;
        setListening(false);
        this.multiUserSetting.setListening(false);
    }

    public final void setListening(boolean z) {
        if (this.listening != z) {
            this.listening = z;
            if (z) {
                this.userInfoController.addCallback(this.onUserInfoChangedListener);
                updateView();
            } else {
                this.userInfoController.removeCallback(this.onUserInfoChangedListener);
            }
            this.fgsManagerFooterController.setListening(z);
            this.securityFooterController.setListening(z);
        }
    }

    public final void disable(int i) {
        ((FooterActionsView) this.mView).disable(i, this.multiUserSwitchController.isMultiUserEnabled());
    }

    public final void setExpansion(float f) {
        this.alphaAnimator.setPosition(f);
    }

    public final void setKeyguardShowing(boolean z) {
        setExpansion(this.lastExpansion);
    }
}
