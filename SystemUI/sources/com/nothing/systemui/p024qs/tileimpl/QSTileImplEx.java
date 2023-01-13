package com.nothing.systemui.p024qs.tileimpl;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.view.View;
import com.android.systemui.C1894R;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.p012qs.tiles.DataUsageDetailView;
import com.android.systemui.p012qs.tiles.dialog.InternetDialogFactory;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.connectivity.AccessPointControllerImpl;
import com.android.systemui.statusbar.connectivity.IconState;
import com.android.systemui.statusbar.connectivity.NetworkControllerImpl;
import com.android.systemui.statusbar.connectivity.WifiIndicators;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.p024qs.QSTileHostEx;
import com.nothing.systemui.util.NTLogUtil;
import java.util.Objects;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000l\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u0000 )2\u00020\u0001:\u0001)B\u0007\b\u0007¢\u0006\u0002\u0010\u0002J\u0018\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u00122\u0006\u0010\u0013\u001a\u00020\u0014J\u0018\u0010\u0015\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u00122\u0006\u0010\u0013\u001a\u00020\u0014J\u000e\u0010\u0016\u001a\u00020\u00102\u0006\u0010\u0017\u001a\u00020\u0018J\u0016\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001eJ\u000e\u0010\u001f\u001a\u00020\u00102\u0006\u0010 \u001a\u00020\u0006J&\u0010!\u001a\u00020\u00102\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\"\u001a\u00020\b2\u0006\u0010#\u001a\u00020\u00042\u0006\u0010$\u001a\u00020\fJ\u001e\u0010%\u001a\u00020&2\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010'\u001a\u00020(R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX.¢\u0006\u0002\n\u0000¨\u0006*"}, mo65043d2 = {"Lcom/nothing/systemui/qs/tileimpl/QSTileImplEx;", "", "()V", "accessPointController", "Lcom/android/systemui/statusbar/connectivity/AccessPointControllerImpl;", "barStateController", "Lcom/android/systemui/plugins/statusbar/StatusBarStateController;", "dialogFactory", "Lcom/android/systemui/qs/tiles/dialog/InternetDialogFactory;", "hostEx", "Lcom/nothing/systemui/qs/QSTileHostEx;", "mainHandler", "Landroid/os/Handler;", "networkController", "Lcom/android/systemui/statusbar/connectivity/NetworkControllerImpl;", "createBluetoothDialog", "", "view", "Landroid/view/View;", "activityStarter", "Lcom/android/systemui/plugins/ActivityStarter;", "createInternetDialog", "dumpWifiInfo", "indicators", "Lcom/android/systemui/statusbar/connectivity/WifiIndicators;", "getDataUsage", "", "subId", "", "context", "Landroid/content/Context;", "init", "controller", "initNetworkComponent", "internetDialogFactory", "aPController", "handler", "postStartActivityDismissingKeyguard", "", "animationController", "Lcom/android/systemui/animation/ActivityLaunchAnimator$Controller;", "Companion", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
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
    private NetworkControllerImpl networkController;

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

    public final void initNetworkComponent(NetworkControllerImpl networkControllerImpl, InternetDialogFactory internetDialogFactory, AccessPointControllerImpl accessPointControllerImpl, Handler handler) {
        Intrinsics.checkNotNullParameter(networkControllerImpl, "networkController");
        Intrinsics.checkNotNullParameter(internetDialogFactory, "internetDialogFactory");
        Intrinsics.checkNotNullParameter(accessPointControllerImpl, "aPController");
        Intrinsics.checkNotNullParameter(handler, "handler");
        this.networkController = networkControllerImpl;
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
    public static final void m3521createInternetDialog$lambda1(QSTileImplEx qSTileImplEx, View view) {
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
    public static final void m3522createInternetDialog$lambda1$lambda0(QSTileImplEx qSTileImplEx, View view) {
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
    public static final void m3519createBluetoothDialog$lambda3(QSTileImplEx qSTileImplEx, View view) {
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
    public static final void m3520createBluetoothDialog$lambda3$lambda2(QSTileImplEx qSTileImplEx, View view) {
        Intrinsics.checkNotNullParameter(qSTileImplEx, "this$0");
        InternetDialogFactory internetDialogFactory = qSTileImplEx.dialogFactory;
        Intrinsics.checkNotNull(internetDialogFactory);
        internetDialogFactory.createBluetoothDialog(true, view);
    }

    public final String getDataUsage(int i, Context context) {
        long j;
        Intrinsics.checkNotNullParameter(context, "context");
        Resources resources = context.getResources();
        NetworkControllerImpl networkControllerImpl = this.networkController;
        NetworkControllerImpl networkControllerImpl2 = null;
        if (networkControllerImpl == null) {
            Intrinsics.throwUninitializedPropertyAccessException("networkController");
            networkControllerImpl = null;
        }
        networkControllerImpl.getMobileDataController().setSubscriptionId(i);
        try {
            NetworkControllerImpl networkControllerImpl3 = this.networkController;
            if (networkControllerImpl3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("networkController");
            } else {
                networkControllerImpl2 = networkControllerImpl3;
            }
            j = networkControllerImpl2.getMobileDataController().getDataUsageInfo().usageLevel;
        } catch (Exception e) {
            e.printStackTrace();
            j = 0;
        }
        resources.getString(C1894R.string.quick_settings_cellular_detail_data_usage);
        String string = resources.getString(C1894R.string.quick_settings_cellular_detail_data_used, new Object[]{DataUsageDetailView.formatBytes(j)});
        Intrinsics.checkNotNullExpressionValue(string, "res.getString(R.string.q….formatBytes(usageLevel))");
        return string;
    }

    public final void dumpWifiInfo(WifiIndicators wifiIndicators) {
        Intrinsics.checkNotNullParameter(wifiIndicators, "indicators");
        boolean z = wifiIndicators.enabled;
        IconState iconState = wifiIndicators.qsIcon;
        NTLogUtil.m1686d(TAG, "CallbackInfo: enabled: " + z + ", connected: " + (iconState != null ? Boolean.valueOf(iconState.visible) : null) + ", isTransient: " + wifiIndicators.isTransient);
    }

    public final boolean postStartActivityDismissingKeyguard(View view, ActivityStarter activityStarter, ActivityLaunchAnimator.Controller controller) {
        Intrinsics.checkNotNullParameter(view, "view");
        Intrinsics.checkNotNullParameter(activityStarter, "activityStarter");
        Intrinsics.checkNotNullParameter(controller, "animationController");
        if (!Objects.equals("tesla", view.getTag(C1894R.C1898id.qs_tesla_tag))) {
            return false;
        }
        Intent intent = new Intent("com.nothing.experimental.TESLA");
        intent.setPackage("com.nothing.experimental");
        intent.putExtra("target_fragment", 0);
        activityStarter.postStartActivityDismissingKeyguard(intent, 0, controller);
        return true;
    }

    @Metadata(mo65042d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006XT¢\u0006\u0002\n\u0000¨\u0006\u0007"}, mo65043d2 = {"Lcom/nothing/systemui/qs/tileimpl/QSTileImplEx$Companion;", "", "()V", "DIALOG_DISMISS_KEYGUARD_LAUNCH_DELAY", "", "TAG", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* renamed from: com.nothing.systemui.qs.tileimpl.QSTileImplEx$Companion */
    /* compiled from: QSTileImplEx.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
