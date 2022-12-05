package com.android.systemui.controls.ui;

import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.service.controls.Control;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.controls.ControlsMetricsLogger;
import com.android.systemui.controls.ui.ControlActionCoordinatorImpl;
import com.android.systemui.globalactions.GlobalActionsComponent;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.wm.shell.TaskView;
import com.android.wm.shell.TaskViewFactory;
import dagger.Lazy;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: ControlActionCoordinatorImpl.kt */
/* loaded from: classes.dex */
public final class ControlActionCoordinatorImpl implements ControlActionCoordinator {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private Set<String> actionsInProgress = new LinkedHashSet();
    public Context activityContext;
    @NotNull
    private final ActivityStarter activityStarter;
    @NotNull
    private final DelayableExecutor bgExecutor;
    @NotNull
    private final BroadcastDispatcher broadcastDispatcher;
    @NotNull
    private final Context context;
    @NotNull
    private final ControlsMetricsLogger controlsMetricsLogger;
    @Nullable
    private Dialog dialog;
    @NotNull
    private final GlobalActionsComponent globalActionsComponent;
    @NotNull
    private final KeyguardStateController keyguardStateController;
    @NotNull
    private final Lazy<ControlsUiController> lazyUiController;
    @Nullable
    private Action pendingAction;
    @NotNull
    private final Optional<TaskViewFactory> taskViewFactory;
    @NotNull
    private final DelayableExecutor uiExecutor;
    @NotNull
    private final Vibrator vibrator;

    public ControlActionCoordinatorImpl(@NotNull Context context, @NotNull DelayableExecutor bgExecutor, @NotNull DelayableExecutor uiExecutor, @NotNull ActivityStarter activityStarter, @NotNull KeyguardStateController keyguardStateController, @NotNull GlobalActionsComponent globalActionsComponent, @NotNull Optional<TaskViewFactory> taskViewFactory, @NotNull BroadcastDispatcher broadcastDispatcher, @NotNull Lazy<ControlsUiController> lazyUiController, @NotNull ControlsMetricsLogger controlsMetricsLogger) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(bgExecutor, "bgExecutor");
        Intrinsics.checkNotNullParameter(uiExecutor, "uiExecutor");
        Intrinsics.checkNotNullParameter(activityStarter, "activityStarter");
        Intrinsics.checkNotNullParameter(keyguardStateController, "keyguardStateController");
        Intrinsics.checkNotNullParameter(globalActionsComponent, "globalActionsComponent");
        Intrinsics.checkNotNullParameter(taskViewFactory, "taskViewFactory");
        Intrinsics.checkNotNullParameter(broadcastDispatcher, "broadcastDispatcher");
        Intrinsics.checkNotNullParameter(lazyUiController, "lazyUiController");
        Intrinsics.checkNotNullParameter(controlsMetricsLogger, "controlsMetricsLogger");
        this.context = context;
        this.bgExecutor = bgExecutor;
        this.uiExecutor = uiExecutor;
        this.activityStarter = activityStarter;
        this.keyguardStateController = keyguardStateController;
        this.globalActionsComponent = globalActionsComponent;
        this.taskViewFactory = taskViewFactory;
        this.broadcastDispatcher = broadcastDispatcher;
        this.lazyUiController = lazyUiController;
        this.controlsMetricsLogger = controlsMetricsLogger;
        Object systemService = context.getSystemService("vibrator");
        Objects.requireNonNull(systemService, "null cannot be cast to non-null type android.os.Vibrator");
        this.vibrator = (Vibrator) systemService;
    }

    private final boolean isLocked() {
        return !this.keyguardStateController.isUnlocked();
    }

    @NotNull
    public Context getActivityContext() {
        Context context = this.activityContext;
        if (context != null) {
            return context;
        }
        Intrinsics.throwUninitializedPropertyAccessException("activityContext");
        throw null;
    }

    @Override // com.android.systemui.controls.ui.ControlActionCoordinator
    public void setActivityContext(@NotNull Context context) {
        Intrinsics.checkNotNullParameter(context, "<set-?>");
        this.activityContext = context;
    }

    /* compiled from: ControlActionCoordinatorImpl.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    @Override // com.android.systemui.controls.ui.ControlActionCoordinator
    public void closeDialogs() {
        Dialog dialog = this.dialog;
        if (dialog != null) {
            dialog.dismiss();
        }
        this.dialog = null;
    }

    @Override // com.android.systemui.controls.ui.ControlActionCoordinator
    public void toggle(@NotNull ControlViewHolder cvh, @NotNull String templateId, boolean z) {
        Intrinsics.checkNotNullParameter(cvh, "cvh");
        Intrinsics.checkNotNullParameter(templateId, "templateId");
        this.controlsMetricsLogger.touch(cvh, isLocked());
        bouncerOrRun(createAction(cvh.getCws().getCi().getControlId(), new ControlActionCoordinatorImpl$toggle$1(cvh, templateId, z), true));
    }

    @Override // com.android.systemui.controls.ui.ControlActionCoordinator
    public void touch(@NotNull ControlViewHolder cvh, @NotNull String templateId, @NotNull Control control) {
        Intrinsics.checkNotNullParameter(cvh, "cvh");
        Intrinsics.checkNotNullParameter(templateId, "templateId");
        Intrinsics.checkNotNullParameter(control, "control");
        this.controlsMetricsLogger.touch(cvh, isLocked());
        bouncerOrRun(createAction(cvh.getCws().getCi().getControlId(), new ControlActionCoordinatorImpl$touch$1(cvh, this, control, templateId), cvh.usePanel()));
    }

    @Override // com.android.systemui.controls.ui.ControlActionCoordinator
    public void drag(boolean z) {
        if (z) {
            vibrate(Vibrations.INSTANCE.getRangeEdgeEffect());
        } else {
            vibrate(Vibrations.INSTANCE.getRangeMiddleEffect());
        }
    }

    @Override // com.android.systemui.controls.ui.ControlActionCoordinator
    public void setValue(@NotNull ControlViewHolder cvh, @NotNull String templateId, float f) {
        Intrinsics.checkNotNullParameter(cvh, "cvh");
        Intrinsics.checkNotNullParameter(templateId, "templateId");
        this.controlsMetricsLogger.drag(cvh, isLocked());
        bouncerOrRun(createAction(cvh.getCws().getCi().getControlId(), new ControlActionCoordinatorImpl$setValue$1(cvh, templateId, f), false));
    }

    @Override // com.android.systemui.controls.ui.ControlActionCoordinator
    public void longPress(@NotNull ControlViewHolder cvh) {
        Intrinsics.checkNotNullParameter(cvh, "cvh");
        this.controlsMetricsLogger.longPress(cvh, isLocked());
        bouncerOrRun(createAction(cvh.getCws().getCi().getControlId(), new ControlActionCoordinatorImpl$longPress$1(cvh, this), false));
    }

    @Override // com.android.systemui.controls.ui.ControlActionCoordinator
    public void runPendingAction(@NotNull String controlId) {
        Intrinsics.checkNotNullParameter(controlId, "controlId");
        if (isLocked()) {
            return;
        }
        Action action = this.pendingAction;
        if (!Intrinsics.areEqual(action == null ? null : action.getControlId(), controlId)) {
            return;
        }
        Action action2 = this.pendingAction;
        if (action2 != null) {
            action2.invoke();
        }
        this.pendingAction = null;
    }

    @Override // com.android.systemui.controls.ui.ControlActionCoordinator
    public void enableActionOnTouch(@NotNull String controlId) {
        Intrinsics.checkNotNullParameter(controlId, "controlId");
        this.actionsInProgress.remove(controlId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean shouldRunAction(final String str) {
        if (this.actionsInProgress.add(str)) {
            this.uiExecutor.executeDelayed(new Runnable() { // from class: com.android.systemui.controls.ui.ControlActionCoordinatorImpl$shouldRunAction$1
                @Override // java.lang.Runnable
                public final void run() {
                    Set set;
                    set = ControlActionCoordinatorImpl.this.actionsInProgress;
                    set.remove(str);
                }
            }, 3000L);
            return true;
        }
        return false;
    }

    @VisibleForTesting
    public final void bouncerOrRun(@NotNull final Action action) {
        Intrinsics.checkNotNullParameter(action, "action");
        if (this.keyguardStateController.isShowing()) {
            if (isLocked()) {
                this.context.sendBroadcast(new Intent("android.intent.action.CLOSE_SYSTEM_DIALOGS"));
                this.pendingAction = action;
            }
            this.activityStarter.dismissKeyguardThenExecute(new ActivityStarter.OnDismissAction() { // from class: com.android.systemui.controls.ui.ControlActionCoordinatorImpl$bouncerOrRun$1
                @Override // com.android.systemui.plugins.ActivityStarter.OnDismissAction
                public final boolean onDismiss() {
                    Log.d("ControlsUiController", "Device unlocked, invoking controls action");
                    ControlActionCoordinatorImpl.Action.this.invoke();
                    return true;
                }
            }, new Runnable() { // from class: com.android.systemui.controls.ui.ControlActionCoordinatorImpl$bouncerOrRun$2
                @Override // java.lang.Runnable
                public final void run() {
                    ControlActionCoordinatorImpl.this.pendingAction = null;
                }
            }, true);
            return;
        }
        action.invoke();
    }

    private final void vibrate(final VibrationEffect vibrationEffect) {
        this.bgExecutor.execute(new Runnable() { // from class: com.android.systemui.controls.ui.ControlActionCoordinatorImpl$vibrate$1
            @Override // java.lang.Runnable
            public final void run() {
                Vibrator vibrator;
                vibrator = ControlActionCoordinatorImpl.this.vibrator;
                vibrator.vibrate(vibrationEffect);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void showDetail(final ControlViewHolder controlViewHolder, final PendingIntent pendingIntent) {
        this.bgExecutor.execute(new Runnable() { // from class: com.android.systemui.controls.ui.ControlActionCoordinatorImpl$showDetail$1
            @Override // java.lang.Runnable
            public final void run() {
                Context context;
                DelayableExecutor delayableExecutor;
                context = ControlActionCoordinatorImpl.this.context;
                final List<ResolveInfo> queryIntentActivities = context.getPackageManager().queryIntentActivities(pendingIntent.getIntent(), 65536);
                Intrinsics.checkNotNullExpressionValue(queryIntentActivities, "context.packageManager.queryIntentActivities(\n                pendingIntent.getIntent(),\n                PackageManager.MATCH_DEFAULT_ONLY\n            )");
                delayableExecutor = ControlActionCoordinatorImpl.this.uiExecutor;
                final ControlActionCoordinatorImpl controlActionCoordinatorImpl = ControlActionCoordinatorImpl.this;
                final ControlViewHolder controlViewHolder2 = controlViewHolder;
                final PendingIntent pendingIntent2 = pendingIntent;
                delayableExecutor.execute(new Runnable() { // from class: com.android.systemui.controls.ui.ControlActionCoordinatorImpl$showDetail$1.1
                    @Override // java.lang.Runnable
                    public final void run() {
                        Optional optional;
                        Optional optional2;
                        Context context2;
                        DelayableExecutor delayableExecutor2;
                        if (!queryIntentActivities.isEmpty()) {
                            optional = controlActionCoordinatorImpl.taskViewFactory;
                            if (optional.isPresent()) {
                                optional2 = controlActionCoordinatorImpl.taskViewFactory;
                                context2 = controlActionCoordinatorImpl.context;
                                delayableExecutor2 = controlActionCoordinatorImpl.uiExecutor;
                                final ControlActionCoordinatorImpl controlActionCoordinatorImpl2 = controlActionCoordinatorImpl;
                                final PendingIntent pendingIntent3 = pendingIntent2;
                                final ControlViewHolder controlViewHolder3 = controlViewHolder2;
                                ((TaskViewFactory) optional2.get()).create(context2, delayableExecutor2, new Consumer<TaskView>() { // from class: com.android.systemui.controls.ui.ControlActionCoordinatorImpl.showDetail.1.1.1
                                    @Override // java.util.function.Consumer
                                    public final void accept(TaskView it) {
                                        ControlActionCoordinatorImpl controlActionCoordinatorImpl3 = ControlActionCoordinatorImpl.this;
                                        Context activityContext = ControlActionCoordinatorImpl.this.getActivityContext();
                                        Intrinsics.checkNotNullExpressionValue(it, "it");
                                        DetailDialog detailDialog = new DetailDialog(activityContext, it, pendingIntent3, controlViewHolder3);
                                        final ControlActionCoordinatorImpl controlActionCoordinatorImpl4 = ControlActionCoordinatorImpl.this;
                                        detailDialog.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.android.systemui.controls.ui.ControlActionCoordinatorImpl$showDetail$1$1$1$1$1
                                            @Override // android.content.DialogInterface.OnDismissListener
                                            public final void onDismiss(DialogInterface dialogInterface) {
                                                ControlActionCoordinatorImpl.this.dialog = null;
                                            }
                                        });
                                        detailDialog.show();
                                        Unit unit = Unit.INSTANCE;
                                        controlActionCoordinatorImpl3.dialog = detailDialog;
                                    }
                                });
                                return;
                            }
                        }
                        controlViewHolder2.setErrorStatus();
                    }
                });
            }
        });
    }

    @VisibleForTesting
    @NotNull
    public final Action createAction(@NotNull String controlId, @NotNull Function0<Unit> f, boolean z) {
        Intrinsics.checkNotNullParameter(controlId, "controlId");
        Intrinsics.checkNotNullParameter(f, "f");
        return new Action(this, controlId, f, z);
    }

    /* compiled from: ControlActionCoordinatorImpl.kt */
    /* loaded from: classes.dex */
    public final class Action {
        private final boolean blockable;
        @NotNull
        private final String controlId;
        @NotNull
        private final Function0<Unit> f;
        final /* synthetic */ ControlActionCoordinatorImpl this$0;

        public Action(@NotNull ControlActionCoordinatorImpl this$0, @NotNull String controlId, Function0<Unit> f, boolean z) {
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            Intrinsics.checkNotNullParameter(controlId, "controlId");
            Intrinsics.checkNotNullParameter(f, "f");
            this.this$0 = this$0;
            this.controlId = controlId;
            this.f = f;
            this.blockable = z;
        }

        @NotNull
        public final String getControlId() {
            return this.controlId;
        }

        public final void invoke() {
            if (!this.blockable || this.this$0.shouldRunAction(this.controlId)) {
                this.f.mo1951invoke();
            }
        }
    }
}
