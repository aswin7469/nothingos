package com.android.systemui.qs.tiles;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import androidx.lifecycle.LifecycleOwner;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.R$drawable;
import com.android.systemui.R$string;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.controls.ControlsServiceInfo;
import com.android.systemui.controls.dagger.ControlsComponent;
import com.android.systemui.controls.management.ControlsListingController;
import com.android.systemui.controls.ui.ControlsActivity;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: DeviceControlsTile.kt */
/* loaded from: classes.dex */
public final class DeviceControlsTile extends QSTileImpl<QSTile.State> {
    @NotNull
    private final ControlsComponent controlsComponent;
    @NotNull
    private final KeyguardStateController keyguardStateController;
    @NotNull
    private AtomicBoolean hasControlsApps = new AtomicBoolean(false);
    private final QSTile.Icon icon = QSTileImpl.ResourceIcon.get(R$drawable.controls_icon);
    @NotNull
    private final DeviceControlsTile$listingCallback$1 listingCallback = new ControlsListingController.ControlsListingCallback() { // from class: com.android.systemui.qs.tiles.DeviceControlsTile$listingCallback$1
        @Override // com.android.systemui.controls.management.ControlsListingController.ControlsListingCallback
        public void onServicesUpdated(@NotNull List<ControlsServiceInfo> serviceInfos) {
            AtomicBoolean atomicBoolean;
            Intrinsics.checkNotNullParameter(serviceInfos, "serviceInfos");
            atomicBoolean = DeviceControlsTile.this.hasControlsApps;
            if (atomicBoolean.compareAndSet(serviceInfos.isEmpty(), !serviceInfos.isEmpty())) {
                DeviceControlsTile.this.refreshState();
            }
        }
    };

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    @Nullable
    public Intent getLongClickIntent() {
        return null;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public int getMetricsCategory() {
        return 0;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    protected void handleLongClick(@Nullable View view) {
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Type inference failed for: r2v4, types: [com.android.systemui.qs.tiles.DeviceControlsTile$listingCallback$1] */
    public DeviceControlsTile(@NotNull QSHost host, @NotNull Looper backgroundLooper, @NotNull Handler mainHandler, @NotNull FalsingManager falsingManager, @NotNull MetricsLogger metricsLogger, @NotNull StatusBarStateController statusBarStateController, @NotNull ActivityStarter activityStarter, @NotNull QSLogger qsLogger, @NotNull ControlsComponent controlsComponent, @NotNull KeyguardStateController keyguardStateController) {
        super(host, backgroundLooper, mainHandler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qsLogger);
        Intrinsics.checkNotNullParameter(host, "host");
        Intrinsics.checkNotNullParameter(backgroundLooper, "backgroundLooper");
        Intrinsics.checkNotNullParameter(mainHandler, "mainHandler");
        Intrinsics.checkNotNullParameter(falsingManager, "falsingManager");
        Intrinsics.checkNotNullParameter(metricsLogger, "metricsLogger");
        Intrinsics.checkNotNullParameter(statusBarStateController, "statusBarStateController");
        Intrinsics.checkNotNullParameter(activityStarter, "activityStarter");
        Intrinsics.checkNotNullParameter(qsLogger, "qsLogger");
        Intrinsics.checkNotNullParameter(controlsComponent, "controlsComponent");
        Intrinsics.checkNotNullParameter(keyguardStateController, "keyguardStateController");
        this.controlsComponent = controlsComponent;
        this.keyguardStateController = keyguardStateController;
        controlsComponent.getControlsListingController().ifPresent(new Consumer<ControlsListingController>() { // from class: com.android.systemui.qs.tiles.DeviceControlsTile.1
            @Override // java.util.function.Consumer
            public final void accept(@NotNull ControlsListingController it) {
                Intrinsics.checkNotNullParameter(it, "it");
                DeviceControlsTile deviceControlsTile = DeviceControlsTile.this;
                it.observe((LifecycleOwner) deviceControlsTile, (DeviceControlsTile) deviceControlsTile.listingCallback);
            }
        });
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public boolean isAvailable() {
        return this.controlsComponent.getControlsController().isPresent();
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    @NotNull
    /* renamed from: newTileState */
    public QSTile.State mo1926newTileState() {
        QSTile.State state = new QSTile.State();
        state.state = 0;
        state.handlesLongClick = false;
        return state;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    protected void handleClick(@Nullable View view) {
        if (getState().state == 0) {
            return;
        }
        final Intent intent = new Intent();
        intent.setComponent(new ComponentName(this.mContext, ControlsActivity.class));
        intent.addFlags(335544320);
        intent.putExtra("extra_animate", true);
        final ActivityLaunchAnimator.Controller fromView = view == null ? null : ActivityLaunchAnimator.Controller.Companion.fromView(view, 32);
        this.mUiHandler.post(new Runnable() { // from class: com.android.systemui.qs.tiles.DeviceControlsTile$handleClick$1
            @Override // java.lang.Runnable
            public final void run() {
                KeyguardStateController keyguardStateController;
                ActivityStarter activityStarter;
                QSHost qSHost;
                Context context;
                ActivityStarter activityStarter2;
                keyguardStateController = DeviceControlsTile.this.keyguardStateController;
                if (keyguardStateController.isUnlocked()) {
                    activityStarter2 = ((QSTileImpl) DeviceControlsTile.this).mActivityStarter;
                    activityStarter2.startActivity(intent, true, fromView);
                } else if (DeviceControlsTile.this.getState().state == 2) {
                    qSHost = ((QSTileImpl) DeviceControlsTile.this).mHost;
                    qSHost.collapsePanels();
                    context = ((QSTileImpl) DeviceControlsTile.this).mContext;
                    context.startActivity(intent);
                } else {
                    activityStarter = ((QSTileImpl) DeviceControlsTile.this).mActivityStarter;
                    activityStarter.postStartActivityDismissingKeyguard(intent, 0, fromView);
                }
            }
        });
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    protected void handleUpdateState(@NotNull QSTile.State state, @Nullable Object obj) {
        Intrinsics.checkNotNullParameter(state, "state");
        CharSequence tileLabel = getTileLabel();
        state.label = tileLabel;
        state.contentDescription = tileLabel;
        state.icon = this.icon;
        if (this.controlsComponent.isEnabled() && this.hasControlsApps.get()) {
            if (this.controlsComponent.getVisibility() == ControlsComponent.Visibility.AVAILABLE) {
                state.state = 2;
                state.secondaryLabel = this.controlsComponent.getControlsController().get().getPreferredStructure().getStructure();
            } else {
                state.state = 1;
                state.secondaryLabel = this.mContext.getText(R$string.controls_tile_locked);
            }
            state.stateDescription = state.secondaryLabel;
            return;
        }
        state.state = 0;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    @NotNull
    public CharSequence getTileLabel() {
        CharSequence text = this.mContext.getText(R$string.quick_controls_title);
        Intrinsics.checkNotNullExpressionValue(text, "mContext.getText(R.string.quick_controls_title)");
        return text;
    }
}
