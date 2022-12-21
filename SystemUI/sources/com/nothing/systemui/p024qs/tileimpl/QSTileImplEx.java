package com.nothing.systemui.p024qs.tileimpl;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import com.android.systemui.C1893R;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.p012qs.tiles.dialog.InternetDialogFactory;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.connectivity.AccessPointControllerImpl;
import com.android.systemui.statusbar.connectivity.IconState;
import com.android.systemui.statusbar.connectivity.WifiIndicators;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.p024qs.QSTileHostEx;
import com.nothing.systemui.util.NTLogUtil;
import java.util.Objects;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u0000 !2\u00020\u0001:\u0001!B\u0007\b\u0007¢\u0006\u0002\u0010\u0002J\u0018\u0010\r\u001a\u00020\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u00102\u0006\u0010\u0011\u001a\u00020\u0012J\u0018\u0010\u0013\u001a\u00020\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u00102\u0006\u0010\u0011\u001a\u00020\u0012J\u000e\u0010\u0014\u001a\u00020\u000e2\u0006\u0010\u0015\u001a\u00020\u0016J\u000e\u0010\u0017\u001a\u00020\u000e2\u0006\u0010\u0018\u001a\u00020\u0006J\u001e\u0010\u0019\u001a\u00020\u000e2\u0006\u0010\u001a\u001a\u00020\b2\u0006\u0010\u001b\u001a\u00020\u00042\u0006\u0010\u001c\u001a\u00020\fJ\u001e\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u001f\u001a\u00020 R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fX\u000e¢\u0006\u0002\n\u0000¨\u0006\""}, mo64987d2 = {"Lcom/nothing/systemui/qs/tileimpl/QSTileImplEx;", "", "()V", "accessPointController", "Lcom/android/systemui/statusbar/connectivity/AccessPointControllerImpl;", "barStateController", "Lcom/android/systemui/plugins/statusbar/StatusBarStateController;", "dialogFactory", "Lcom/android/systemui/qs/tiles/dialog/InternetDialogFactory;", "hostEx", "Lcom/nothing/systemui/qs/QSTileHostEx;", "mainHandler", "Landroid/os/Handler;", "createBluetoothDialog", "", "view", "Landroid/view/View;", "activityStarter", "Lcom/android/systemui/plugins/ActivityStarter;", "createInternetDialog", "dumpWifiInfo", "indicators", "Lcom/android/systemui/statusbar/connectivity/WifiIndicators;", "init", "controller", "initNetworkComponent", "internetDialogFactory", "aPController", "handler", "postStartActivityDismissingKeyguard", "", "animationController", "Lcom/android/systemui/animation/ActivityLaunchAnimator$Controller;", "Companion", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.nothing.systemui.qs.tileimpl.QSTileImplEx */
/* compiled from: QSTileImplEx.kt */
public final class QSTileImplEx {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final long DIALOG_DISMISS_KEYGUARD_LAUNCH_DELAY = 600;
    private static final String TAG = "QSTileImplEx";
    private AccessPointControllerImpl accessPointController;
    private StatusBarStateController barStateController;
    private InternetDialogFactory dialogFactory;
    private final QSTileHostEx hostEx;
    private Handler mainHandler;

    @Metadata(mo64986d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006XT¢\u0006\u0002\n\u0000¨\u0006\u0007"}, mo64987d2 = {"Lcom/nothing/systemui/qs/tileimpl/QSTileImplEx$Companion;", "", "()V", "DIALOG_DISMISS_KEYGUARD_LAUNCH_DELAY", "", "TAG", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* renamed from: com.nothing.systemui.qs.tileimpl.QSTileImplEx$Companion */
    /* compiled from: QSTileImplEx.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    @Inject
    public QSTileImplEx() {
        Object obj = NTDependencyEx.get(QSTileHostEx.class);
        Intrinsics.checkNotNullExpressionValue(obj, "get(QSTileHostEx::class.java)");
        this.hostEx = (QSTileHostEx) obj;
    }

    public final void init(StatusBarStateController statusBarStateController) {
        Intrinsics.checkNotNullParameter(statusBarStateController, "controller");
        this.barStateController = statusBarStateController;
    }

    public final void initNetworkComponent(InternetDialogFactory internetDialogFactory, AccessPointControllerImpl accessPointControllerImpl, Handler handler) {
        Intrinsics.checkNotNullParameter(internetDialogFactory, "internetDialogFactory");
        Intrinsics.checkNotNullParameter(accessPointControllerImpl, "aPController");
        Intrinsics.checkNotNullParameter(handler, "handler");
        this.dialogFactory = internetDialogFactory;
        this.accessPointController = accessPointControllerImpl;
        this.mainHandler = handler;
    }

    public final void createInternetDialog(View view, ActivityStarter activityStarter) {
        Intrinsics.checkNotNullParameter(activityStarter, "activityStarter");
        if (this.hostEx.isPanelExpanded()) {
            activityStarter.postQSRunnableDismissingKeyguard(new QSTileImplEx$$ExternalSyntheticLambda0(this, view));
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: createInternetDialog$lambda-1  reason: not valid java name */
    public static final void m3515createInternetDialog$lambda1(QSTileImplEx qSTileImplEx, View view) {
        Intrinsics.checkNotNullParameter(qSTileImplEx, "this$0");
        Handler handler = qSTileImplEx.mainHandler;
        Intrinsics.checkNotNull(handler);
        QSTileImplEx$$ExternalSyntheticLambda3 qSTileImplEx$$ExternalSyntheticLambda3 = new QSTileImplEx$$ExternalSyntheticLambda3(qSTileImplEx, view);
        StatusBarStateController statusBarStateController = qSTileImplEx.barStateController;
        Intrinsics.checkNotNull(statusBarStateController);
        handler.postDelayed(qSTileImplEx$$ExternalSyntheticLambda3, statusBarStateController.getState() != 0 ? DIALOG_DISMISS_KEYGUARD_LAUNCH_DELAY : 0);
    }

    /* access modifiers changed from: private */
    /* renamed from: createInternetDialog$lambda-1$lambda-0  reason: not valid java name */
    public static final void m3516createInternetDialog$lambda1$lambda0(QSTileImplEx qSTileImplEx, View view) {
        Intrinsics.checkNotNullParameter(qSTileImplEx, "this$0");
        InternetDialogFactory internetDialogFactory = qSTileImplEx.dialogFactory;
        Intrinsics.checkNotNull(internetDialogFactory);
        AccessPointControllerImpl accessPointControllerImpl = qSTileImplEx.accessPointController;
        Intrinsics.checkNotNull(accessPointControllerImpl);
        boolean canConfigMobileData = accessPointControllerImpl.canConfigMobileData();
        AccessPointControllerImpl accessPointControllerImpl2 = qSTileImplEx.accessPointController;
        Intrinsics.checkNotNull(accessPointControllerImpl2);
        internetDialogFactory.create(true, canConfigMobileData, accessPointControllerImpl2.canConfigWifi(), view);
    }

    public final void createBluetoothDialog(View view, ActivityStarter activityStarter) {
        Intrinsics.checkNotNullParameter(activityStarter, "activityStarter");
        if (this.hostEx.isPanelExpanded()) {
            activityStarter.postQSRunnableDismissingKeyguard(new QSTileImplEx$$ExternalSyntheticLambda2(this, view));
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: createBluetoothDialog$lambda-3  reason: not valid java name */
    public static final void m3513createBluetoothDialog$lambda3(QSTileImplEx qSTileImplEx, View view) {
        Intrinsics.checkNotNullParameter(qSTileImplEx, "this$0");
        Handler handler = qSTileImplEx.mainHandler;
        Intrinsics.checkNotNull(handler);
        QSTileImplEx$$ExternalSyntheticLambda1 qSTileImplEx$$ExternalSyntheticLambda1 = new QSTileImplEx$$ExternalSyntheticLambda1(qSTileImplEx, view);
        StatusBarStateController statusBarStateController = qSTileImplEx.barStateController;
        Intrinsics.checkNotNull(statusBarStateController);
        handler.postDelayed(qSTileImplEx$$ExternalSyntheticLambda1, statusBarStateController.getState() != 0 ? DIALOG_DISMISS_KEYGUARD_LAUNCH_DELAY : 0);
    }

    /* access modifiers changed from: private */
    /* renamed from: createBluetoothDialog$lambda-3$lambda-2  reason: not valid java name */
    public static final void m3514createBluetoothDialog$lambda3$lambda2(QSTileImplEx qSTileImplEx, View view) {
        Intrinsics.checkNotNullParameter(qSTileImplEx, "this$0");
        InternetDialogFactory internetDialogFactory = qSTileImplEx.dialogFactory;
        Intrinsics.checkNotNull(internetDialogFactory);
        internetDialogFactory.createBluetoothDialog(true, view);
    }

    public final void dumpWifiInfo(WifiIndicators wifiIndicators) {
        Intrinsics.checkNotNullParameter(wifiIndicators, "indicators");
        boolean z = wifiIndicators.enabled;
        IconState iconState = wifiIndicators.qsIcon;
        NTLogUtil.m1680d(TAG, "CallbackInfo: enabled: " + z + ", connected: " + (iconState != null ? Boolean.valueOf(iconState.visible) : null) + ", isTransient: " + wifiIndicators.isTransient);
    }

    public final boolean postStartActivityDismissingKeyguard(View view, ActivityStarter activityStarter, ActivityLaunchAnimator.Controller controller) {
        Intrinsics.checkNotNullParameter(view, "view");
        Intrinsics.checkNotNullParameter(activityStarter, "activityStarter");
        Intrinsics.checkNotNullParameter(controller, "animationController");
        if (!Objects.equals("tesla", view.getTag(C1893R.C1897id.qs_tesla_tag))) {
            return false;
        }
        Intent intent = new Intent("com.nothing.experimental.TESLA");
        intent.setPackage("com.nothing.experimental");
        intent.putExtra("target_fragment", 0);
        activityStarter.postStartActivityDismissingKeyguard(intent, 0, controller);
        return true;
    }
}
