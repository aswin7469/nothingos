package com.android.systemui.p012qs.tiles;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import androidx.lifecycle.LifecycleOwner;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.C1894R;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.biometrics.AuthDialog;
import com.android.systemui.controls.dagger.ControlsComponent;
import com.android.systemui.controls.management.ControlsListingController;
import com.android.systemui.controls.p010ui.ControlsActivity;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.p012qs.QSHost;
import com.android.systemui.p012qs.logging.QSLogger;
import com.android.systemui.p012qs.tileimpl.QSTileImpl;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.p011qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\r\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002*\u0001!\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B[\b\u0007\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\b\b\u0001\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0001\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\n\u0012\u0006\u0010\u000b\u001a\u00020\f\u0012\u0006\u0010\r\u001a\u00020\u000e\u0012\u0006\u0010\u000f\u001a\u00020\u0010\u0012\u0006\u0010\u0011\u001a\u00020\u0012\u0012\u0006\u0010\u0013\u001a\u00020\u0014\u0012\u0006\u0010\u0015\u001a\u00020\u0016¢\u0006\u0002\u0010\u0017J\n\u0010#\u001a\u0004\u0018\u00010$H\u0016J\b\u0010%\u001a\u00020&H\u0016J\b\u0010'\u001a\u00020(H\u0016J\u0012\u0010)\u001a\u00020*2\b\u0010+\u001a\u0004\u0018\u00010,H\u0014J\u0012\u0010-\u001a\u00020*2\b\u0010+\u001a\u0004\u0018\u00010,H\u0014J\u001a\u0010.\u001a\u00020*2\u0006\u0010/\u001a\u00020\u00022\b\u00100\u001a\u0004\u0018\u000101H\u0014J\b\u00102\u001a\u000203H\u0016J\b\u00104\u001a\u00020\u0002H\u0016R\u000e\u0010\u0013\u001a\u00020\u0014X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0019X\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\u001a\u001a\u00020\u001b8FX\u0004¢\u0006\f\u0012\u0004\b\u001c\u0010\u001d\u001a\u0004\b\u001e\u0010\u001fR\u000e\u0010\u0015\u001a\u00020\u0016X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010 \u001a\u00020!X\u0004¢\u0006\u0004\n\u0002\u0010\"¨\u00065"}, mo65043d2 = {"Lcom/android/systemui/qs/tiles/DeviceControlsTile;", "Lcom/android/systemui/qs/tileimpl/QSTileImpl;", "Lcom/android/systemui/plugins/qs/QSTile$State;", "host", "Lcom/android/systemui/qs/QSHost;", "backgroundLooper", "Landroid/os/Looper;", "mainHandler", "Landroid/os/Handler;", "falsingManager", "Lcom/android/systemui/plugins/FalsingManager;", "metricsLogger", "Lcom/android/internal/logging/MetricsLogger;", "statusBarStateController", "Lcom/android/systemui/plugins/statusbar/StatusBarStateController;", "activityStarter", "Lcom/android/systemui/plugins/ActivityStarter;", "qsLogger", "Lcom/android/systemui/qs/logging/QSLogger;", "controlsComponent", "Lcom/android/systemui/controls/dagger/ControlsComponent;", "keyguardStateController", "Lcom/android/systemui/statusbar/policy/KeyguardStateController;", "(Lcom/android/systemui/qs/QSHost;Landroid/os/Looper;Landroid/os/Handler;Lcom/android/systemui/plugins/FalsingManager;Lcom/android/internal/logging/MetricsLogger;Lcom/android/systemui/plugins/statusbar/StatusBarStateController;Lcom/android/systemui/plugins/ActivityStarter;Lcom/android/systemui/qs/logging/QSLogger;Lcom/android/systemui/controls/dagger/ControlsComponent;Lcom/android/systemui/statusbar/policy/KeyguardStateController;)V", "hasControlsApps", "Ljava/util/concurrent/atomic/AtomicBoolean;", "icon", "Lcom/android/systemui/plugins/qs/QSTile$Icon;", "getIcon$annotations", "()V", "getIcon", "()Lcom/android/systemui/plugins/qs/QSTile$Icon;", "listingCallback", "com/android/systemui/qs/tiles/DeviceControlsTile$listingCallback$1", "Lcom/android/systemui/qs/tiles/DeviceControlsTile$listingCallback$1;", "getLongClickIntent", "Landroid/content/Intent;", "getMetricsCategory", "", "getTileLabel", "", "handleClick", "", "view", "Landroid/view/View;", "handleLongClick", "handleUpdateState", "state", "arg", "", "isAvailable", "", "newTileState", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.systemui.qs.tiles.DeviceControlsTile */
/* compiled from: DeviceControlsTile.kt */
public final class DeviceControlsTile extends QSTileImpl<QSTile.State> {
    private final ControlsComponent controlsComponent;
    /* access modifiers changed from: private */
    public AtomicBoolean hasControlsApps = new AtomicBoolean(false);
    private final KeyguardStateController keyguardStateController;
    private final DeviceControlsTile$listingCallback$1 listingCallback = new DeviceControlsTile$listingCallback$1(this);

    public static /* synthetic */ void getIcon$annotations() {
    }

    public Intent getLongClickIntent() {
        return null;
    }

    public int getMetricsCategory() {
        return 0;
    }

    /* access modifiers changed from: protected */
    public void handleLongClick(View view) {
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    @Inject
    public DeviceControlsTile(QSHost qSHost, @Background Looper looper, @Main Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, ControlsComponent controlsComponent2, KeyguardStateController keyguardStateController2) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        Intrinsics.checkNotNullParameter(qSHost, "host");
        Intrinsics.checkNotNullParameter(looper, "backgroundLooper");
        Intrinsics.checkNotNullParameter(handler, "mainHandler");
        Intrinsics.checkNotNullParameter(falsingManager, "falsingManager");
        Intrinsics.checkNotNullParameter(metricsLogger, "metricsLogger");
        Intrinsics.checkNotNullParameter(statusBarStateController, "statusBarStateController");
        Intrinsics.checkNotNullParameter(activityStarter, "activityStarter");
        Intrinsics.checkNotNullParameter(qSLogger, "qsLogger");
        Intrinsics.checkNotNullParameter(controlsComponent2, "controlsComponent");
        Intrinsics.checkNotNullParameter(keyguardStateController2, "keyguardStateController");
        this.controlsComponent = controlsComponent2;
        this.keyguardStateController = keyguardStateController2;
        controlsComponent2.getControlsListingController().ifPresent(new DeviceControlsTile$$ExternalSyntheticLambda1(this));
    }

    public final QSTile.Icon getIcon() {
        QSTile.Icon icon = QSTileImpl.ResourceIcon.get(this.controlsComponent.getTileImageId());
        Intrinsics.checkNotNullExpressionValue(icon, "get(controlsComponent.getTileImageId())");
        return icon;
    }

    /* access modifiers changed from: private */
    /* renamed from: _init_$lambda-0  reason: not valid java name */
    public static final void m2979_init_$lambda0(DeviceControlsTile deviceControlsTile, ControlsListingController controlsListingController) {
        Intrinsics.checkNotNullParameter(deviceControlsTile, "this$0");
        Intrinsics.checkNotNullParameter(controlsListingController, "it");
        controlsListingController.observe((LifecycleOwner) deviceControlsTile, deviceControlsTile.listingCallback);
    }

    public boolean isAvailable() {
        return this.controlsComponent.getControlsController().isPresent();
    }

    public QSTile.State newTileState() {
        QSTile.State state = new QSTile.State();
        state.state = 0;
        state.handlesLongClick = false;
        return state;
    }

    /* access modifiers changed from: protected */
    public void handleClick(View view) {
        if (getState().state != 0) {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(this.mContext, ControlsActivity.class));
            intent.addFlags(335544320);
            intent.putExtra("extra_animate", true);
            this.mUiHandler.post(new DeviceControlsTile$$ExternalSyntheticLambda0(this, intent, view != null ? ActivityLaunchAnimator.Controller.Companion.fromView(view, 32) : null));
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: handleClick$lambda-4  reason: not valid java name */
    public static final void m2980handleClick$lambda4(DeviceControlsTile deviceControlsTile, Intent intent, ActivityLaunchAnimator.Controller controller) {
        Intrinsics.checkNotNullParameter(deviceControlsTile, "this$0");
        Intrinsics.checkNotNullParameter(intent, "$intent");
        deviceControlsTile.mActivityStarter.startActivity(intent, true, controller, deviceControlsTile.getState().state == 2);
        deviceControlsTile.mActivityStarter.postStartActivityDismissingKeyguard(intent, 0);
    }

    /* access modifiers changed from: protected */
    public void handleUpdateState(QSTile.State state, Object obj) {
        Intrinsics.checkNotNullParameter(state, AuthDialog.KEY_BIOMETRIC_STATE);
        state.label = getTileLabel();
        state.contentDescription = state.label;
        state.icon = getIcon();
        if (!this.controlsComponent.isEnabled() || !this.hasControlsApps.get()) {
            state.state = 0;
            return;
        }
        if (this.controlsComponent.getVisibility() == ControlsComponent.Visibility.AVAILABLE) {
            CharSequence structure = this.controlsComponent.getControlsController().get().getPreferredStructure().getStructure();
            state.state = 2;
            if (Intrinsics.areEqual((Object) structure, (Object) getTileLabel())) {
                structure = null;
            }
            state.secondaryLabel = structure;
        } else {
            state.state = 1;
            state.secondaryLabel = this.mContext.getText(C1894R.string.controls_tile_locked);
        }
        state.stateDescription = state.secondaryLabel;
    }

    public CharSequence getTileLabel() {
        CharSequence text = this.mContext.getText(this.controlsComponent.getTileTitleId());
        Intrinsics.checkNotNullExpressionValue(text, "mContext.getText(control…mponent.getTileTitleId())");
        return text;
    }
}
