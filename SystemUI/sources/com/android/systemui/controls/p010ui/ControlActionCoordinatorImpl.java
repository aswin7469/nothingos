package com.android.systemui.controls.p010ui;

import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.VibrationEffect;
import android.service.controls.Control;
import android.util.Log;
import com.android.p019wm.shell.TaskView;
import com.android.p019wm.shell.TaskViewFactory;
import com.android.systemui.broadcast.BroadcastSender;
import com.android.systemui.controls.ControlsMetricsLogger;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.settings.UserContextProvider;
import com.android.systemui.statusbar.VibratorHelper;
import com.android.systemui.statusbar.policy.DeviceControlsControllerImpl;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.settings.SecureSettings;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo65042d1 = {"\u0000¬\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010#\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u0000 O2\u00020\u0001:\u0002NOBq\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0001\u0010\u0006\u001a\u00020\u0005\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\n\u0012\u0006\u0010\u000b\u001a\u00020\f\u0012\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000e\u0012\u0006\u0010\u0010\u001a\u00020\u0011\u0012\u0006\u0010\u0012\u001a\u00020\u0013\u0012\u0006\u0010\u0014\u001a\u00020\u0015\u0012\u0006\u0010\u0016\u001a\u00020\u0017\u0012\b\b\u0001\u0010\u0018\u001a\u00020\u0019¢\u0006\u0002\u0010\u001aJ\u0014\u0010,\u001a\u00020-2\n\u0010.\u001a\u00060+R\u00020\u0000H\u0007J\b\u0010/\u001a\u00020-H\u0016J2\u00100\u001a\u00060+R\u00020\u00002\u0006\u00101\u001a\u00020\u001d2\f\u00102\u001a\b\u0012\u0004\u0012\u00020-032\u0006\u00104\u001a\u00020&2\u0006\u00105\u001a\u00020&H\u0007J\u0010\u00106\u001a\u00020-2\u0006\u00107\u001a\u00020&H\u0016J\u0010\u00108\u001a\u00020-2\u0006\u00101\u001a\u00020\u001dH\u0016J\u0010\u00109\u001a\u00020-2\u0006\u0010:\u001a\u00020;H\u0016J\u0010\u0010<\u001a\u00020-2\u0006\u00101\u001a\u00020\u001dH\u0016J \u0010=\u001a\u00020-2\u0006\u0010:\u001a\u00020;2\u0006\u0010>\u001a\u00020\u001d2\u0006\u0010?\u001a\u00020@H\u0016J\u0010\u0010A\u001a\u00020&2\u0006\u00101\u001a\u00020\u001dH\u0002J\u0018\u0010B\u001a\u00020-2\u0006\u0010:\u001a\u00020;2\u0006\u0010C\u001a\u00020DH\u0002J\u0014\u0010E\u001a\u00020-2\n\u0010.\u001a\u00060+R\u00020\u0000H\u0002J \u0010F\u001a\u00020-2\u0006\u0010:\u001a\u00020;2\u0006\u0010>\u001a\u00020\u001d2\u0006\u0010G\u001a\u00020&H\u0016J \u0010H\u001a\u00020-2\u0006\u0010:\u001a\u00020;2\u0006\u0010>\u001a\u00020\u001d2\u0006\u0010I\u001a\u00020JH\u0016J\u0010\u0010K\u001a\u00020-2\u0006\u0010L\u001a\u00020MH\u0002R\u0014\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u001d0\u001cX\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\u001e\u001a\u00020\u0003X.¢\u0006\u000e\n\u0000\u001a\u0004\b\u001f\u0010 \"\u0004\b!\u0010\"R\u000e\u0010\u0007\u001a\u00020\bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010#\u001a\u0004\u0018\u00010$X\u000e¢\u0006\u0002\n\u0000R\u0014\u0010%\u001a\u00020&8BX\u0004¢\u0006\u0006\u001a\u0004\b%\u0010'R\u000e\u0010\u000b\u001a\u00020\fX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010(\u001a\u00020&X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010)\u001a\u00020&X\u000e¢\u0006\u0002\n\u0000R\u0014\u0010*\u001a\b\u0018\u00010+R\u00020\u0000X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0015X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000eX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0017X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0004¢\u0006\u0002\n\u0000¨\u0006P"}, mo65043d2 = {"Lcom/android/systemui/controls/ui/ControlActionCoordinatorImpl;", "Lcom/android/systemui/controls/ui/ControlActionCoordinator;", "context", "Landroid/content/Context;", "bgExecutor", "Lcom/android/systemui/util/concurrency/DelayableExecutor;", "uiExecutor", "activityStarter", "Lcom/android/systemui/plugins/ActivityStarter;", "broadcastSender", "Lcom/android/systemui/broadcast/BroadcastSender;", "keyguardStateController", "Lcom/android/systemui/statusbar/policy/KeyguardStateController;", "taskViewFactory", "Ljava/util/Optional;", "Lcom/android/wm/shell/TaskViewFactory;", "controlsMetricsLogger", "Lcom/android/systemui/controls/ControlsMetricsLogger;", "vibrator", "Lcom/android/systemui/statusbar/VibratorHelper;", "secureSettings", "Lcom/android/systemui/util/settings/SecureSettings;", "userContextProvider", "Lcom/android/systemui/settings/UserContextProvider;", "mainHandler", "Landroid/os/Handler;", "(Landroid/content/Context;Lcom/android/systemui/util/concurrency/DelayableExecutor;Lcom/android/systemui/util/concurrency/DelayableExecutor;Lcom/android/systemui/plugins/ActivityStarter;Lcom/android/systemui/broadcast/BroadcastSender;Lcom/android/systemui/statusbar/policy/KeyguardStateController;Ljava/util/Optional;Lcom/android/systemui/controls/ControlsMetricsLogger;Lcom/android/systemui/statusbar/VibratorHelper;Lcom/android/systemui/util/settings/SecureSettings;Lcom/android/systemui/settings/UserContextProvider;Landroid/os/Handler;)V", "actionsInProgress", "", "", "activityContext", "getActivityContext", "()Landroid/content/Context;", "setActivityContext", "(Landroid/content/Context;)V", "dialog", "Landroid/app/Dialog;", "isLocked", "", "()Z", "mAllowTrivialControls", "mShowDeviceControlsInLockscreen", "pendingAction", "Lcom/android/systemui/controls/ui/ControlActionCoordinatorImpl$Action;", "bouncerOrRun", "", "action", "closeDialogs", "createAction", "controlId", "f", "Lkotlin/Function0;", "blockable", "authIsRequired", "drag", "isEdge", "enableActionOnTouch", "longPress", "cvh", "Lcom/android/systemui/controls/ui/ControlViewHolder;", "runPendingAction", "setValue", "templateId", "newValue", "", "shouldRunAction", "showDetail", "pendingIntent", "Landroid/app/PendingIntent;", "showSettingsDialogIfNeeded", "toggle", "isChecked", "touch", "control", "Landroid/service/controls/Control;", "vibrate", "effect", "Landroid/os/VibrationEffect;", "Action", "Companion", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.systemui.controls.ui.ControlActionCoordinatorImpl */
/* compiled from: ControlActionCoordinatorImpl.kt */
public final class ControlActionCoordinatorImpl implements ControlActionCoordinator {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final int MAX_NUMBER_ATTEMPTS_CONTROLS_DIALOG = 2;
    private static final long RESPONSE_TIMEOUT_IN_MILLIS = 3000;
    private Set<String> actionsInProgress = new LinkedHashSet();
    public Context activityContext;
    private final ActivityStarter activityStarter;
    private final DelayableExecutor bgExecutor;
    private final BroadcastSender broadcastSender;
    /* access modifiers changed from: private */
    public final Context context;
    private final ControlsMetricsLogger controlsMetricsLogger;
    /* access modifiers changed from: private */
    public Dialog dialog;
    private final KeyguardStateController keyguardStateController;
    /* access modifiers changed from: private */
    public boolean mAllowTrivialControls;
    /* access modifiers changed from: private */
    public boolean mShowDeviceControlsInLockscreen;
    private Action pendingAction;
    /* access modifiers changed from: private */
    public final SecureSettings secureSettings;
    private final Optional<TaskViewFactory> taskViewFactory;
    private final DelayableExecutor uiExecutor;
    private final UserContextProvider userContextProvider;
    private final VibratorHelper vibrator;

    @Inject
    public ControlActionCoordinatorImpl(Context context2, DelayableExecutor delayableExecutor, @Main DelayableExecutor delayableExecutor2, ActivityStarter activityStarter2, BroadcastSender broadcastSender2, KeyguardStateController keyguardStateController2, Optional<TaskViewFactory> optional, ControlsMetricsLogger controlsMetricsLogger2, VibratorHelper vibratorHelper, SecureSettings secureSettings2, UserContextProvider userContextProvider2, @Main Handler handler) {
        Intrinsics.checkNotNullParameter(context2, "context");
        Intrinsics.checkNotNullParameter(delayableExecutor, "bgExecutor");
        Intrinsics.checkNotNullParameter(delayableExecutor2, "uiExecutor");
        Intrinsics.checkNotNullParameter(activityStarter2, "activityStarter");
        Intrinsics.checkNotNullParameter(broadcastSender2, "broadcastSender");
        Intrinsics.checkNotNullParameter(keyguardStateController2, "keyguardStateController");
        Intrinsics.checkNotNullParameter(optional, "taskViewFactory");
        Intrinsics.checkNotNullParameter(controlsMetricsLogger2, "controlsMetricsLogger");
        Intrinsics.checkNotNullParameter(vibratorHelper, "vibrator");
        Intrinsics.checkNotNullParameter(secureSettings2, "secureSettings");
        Intrinsics.checkNotNullParameter(userContextProvider2, "userContextProvider");
        Intrinsics.checkNotNullParameter(handler, "mainHandler");
        this.context = context2;
        this.bgExecutor = delayableExecutor;
        this.uiExecutor = delayableExecutor2;
        this.activityStarter = activityStarter2;
        this.broadcastSender = broadcastSender2;
        this.keyguardStateController = keyguardStateController2;
        this.taskViewFactory = optional;
        this.controlsMetricsLogger = controlsMetricsLogger2;
        this.vibrator = vibratorHelper;
        this.secureSettings = secureSettings2;
        this.userContextProvider = userContextProvider2;
        boolean z = true;
        this.mAllowTrivialControls = secureSettings2.getIntForUser("lockscreen_allow_trivial_controls", 0, -2) != 0;
        this.mShowDeviceControlsInLockscreen = secureSettings2.getIntForUser("lockscreen_show_controls", 0, -2) == 0 ? false : z;
        Uri uriFor = secureSettings2.getUriFor("lockscreen_allow_trivial_controls");
        Uri uriFor2 = secureSettings2.getUriFor("lockscreen_show_controls");
        ContentObserver controlActionCoordinatorImpl$controlsContentObserver$1 = new ControlActionCoordinatorImpl$controlsContentObserver$1(handler, uriFor, this, uriFor2);
        secureSettings2.registerContentObserverForUser(uriFor, false, controlActionCoordinatorImpl$controlsContentObserver$1, -1);
        secureSettings2.registerContentObserverForUser(uriFor2, false, controlActionCoordinatorImpl$controlsContentObserver$1, -1);
    }

    private final boolean isLocked() {
        return !this.keyguardStateController.isUnlocked();
    }

    public Context getActivityContext() {
        Context context2 = this.activityContext;
        if (context2 != null) {
            return context2;
        }
        Intrinsics.throwUninitializedPropertyAccessException("activityContext");
        return null;
    }

    public void setActivityContext(Context context2) {
        Intrinsics.checkNotNullParameter(context2, "<set-?>");
        this.activityContext = context2;
    }

    @Metadata(mo65042d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006XT¢\u0006\u0002\n\u0000¨\u0006\u0007"}, mo65043d2 = {"Lcom/android/systemui/controls/ui/ControlActionCoordinatorImpl$Companion;", "", "()V", "MAX_NUMBER_ATTEMPTS_CONTROLS_DIALOG", "", "RESPONSE_TIMEOUT_IN_MILLIS", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* renamed from: com.android.systemui.controls.ui.ControlActionCoordinatorImpl$Companion */
    /* compiled from: ControlActionCoordinatorImpl.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    public void closeDialogs() {
        Dialog dialog2 = this.dialog;
        if (dialog2 != null) {
            dialog2.dismiss();
        }
        this.dialog = null;
    }

    public void toggle(ControlViewHolder controlViewHolder, String str, boolean z) {
        Intrinsics.checkNotNullParameter(controlViewHolder, "cvh");
        Intrinsics.checkNotNullParameter(str, "templateId");
        this.controlsMetricsLogger.touch(controlViewHolder, isLocked());
        String controlId = controlViewHolder.getCws().getCi().getControlId();
        Function0 controlActionCoordinatorImpl$toggle$1 = new ControlActionCoordinatorImpl$toggle$1(controlViewHolder, str, z);
        Control control = controlViewHolder.getCws().getControl();
        bouncerOrRun(createAction(controlId, controlActionCoordinatorImpl$toggle$1, true, control != null ? control.isAuthRequired() : true));
    }

    public void touch(ControlViewHolder controlViewHolder, String str, Control control) {
        Intrinsics.checkNotNullParameter(controlViewHolder, "cvh");
        Intrinsics.checkNotNullParameter(str, "templateId");
        Intrinsics.checkNotNullParameter(control, "control");
        this.controlsMetricsLogger.touch(controlViewHolder, isLocked());
        boolean usePanel = controlViewHolder.usePanel();
        String controlId = controlViewHolder.getCws().getCi().getControlId();
        Function0 controlActionCoordinatorImpl$touch$1 = new ControlActionCoordinatorImpl$touch$1(controlViewHolder, this, control, str);
        Control control2 = controlViewHolder.getCws().getControl();
        bouncerOrRun(createAction(controlId, controlActionCoordinatorImpl$touch$1, usePanel, control2 != null ? control2.isAuthRequired() : true));
    }

    public void drag(boolean z) {
        if (z) {
            vibrate(Vibrations.INSTANCE.getRangeEdgeEffect());
        } else {
            vibrate(Vibrations.INSTANCE.getRangeMiddleEffect());
        }
    }

    public void setValue(ControlViewHolder controlViewHolder, String str, float f) {
        Intrinsics.checkNotNullParameter(controlViewHolder, "cvh");
        Intrinsics.checkNotNullParameter(str, "templateId");
        this.controlsMetricsLogger.drag(controlViewHolder, isLocked());
        String controlId = controlViewHolder.getCws().getCi().getControlId();
        Function0 controlActionCoordinatorImpl$setValue$1 = new ControlActionCoordinatorImpl$setValue$1(controlViewHolder, str, f);
        Control control = controlViewHolder.getCws().getControl();
        bouncerOrRun(createAction(controlId, controlActionCoordinatorImpl$setValue$1, false, control != null ? control.isAuthRequired() : true));
    }

    public void longPress(ControlViewHolder controlViewHolder) {
        Intrinsics.checkNotNullParameter(controlViewHolder, "cvh");
        this.controlsMetricsLogger.longPress(controlViewHolder, isLocked());
        String controlId = controlViewHolder.getCws().getCi().getControlId();
        Function0 controlActionCoordinatorImpl$longPress$1 = new ControlActionCoordinatorImpl$longPress$1(controlViewHolder, this);
        Control control = controlViewHolder.getCws().getControl();
        bouncerOrRun(createAction(controlId, controlActionCoordinatorImpl$longPress$1, false, control != null ? control.isAuthRequired() : true));
    }

    public void runPendingAction(String str) {
        Intrinsics.checkNotNullParameter(str, "controlId");
        if (!isLocked()) {
            Action action = this.pendingAction;
            if (Intrinsics.areEqual((Object) action != null ? action.getControlId() : null, (Object) str)) {
                Action action2 = this.pendingAction;
                Intrinsics.checkNotNull(action2);
                showSettingsDialogIfNeeded(action2);
                Action action3 = this.pendingAction;
                if (action3 != null) {
                    action3.invoke();
                }
                this.pendingAction = null;
            }
        }
    }

    public void enableActionOnTouch(String str) {
        Intrinsics.checkNotNullParameter(str, "controlId");
        this.actionsInProgress.remove(str);
    }

    /* access modifiers changed from: private */
    public final boolean shouldRunAction(String str) {
        if (!this.actionsInProgress.add(str)) {
            return false;
        }
        this.uiExecutor.executeDelayed(new ControlActionCoordinatorImpl$$ExternalSyntheticLambda8(this, str), 3000);
        return true;
    }

    /* access modifiers changed from: private */
    /* renamed from: shouldRunAction$lambda-0  reason: not valid java name */
    public static final void m2680shouldRunAction$lambda0(ControlActionCoordinatorImpl controlActionCoordinatorImpl, String str) {
        Intrinsics.checkNotNullParameter(controlActionCoordinatorImpl, "this$0");
        Intrinsics.checkNotNullParameter(str, "$controlId");
        controlActionCoordinatorImpl.actionsInProgress.remove(str);
    }

    public final void bouncerOrRun(Action action) {
        Intrinsics.checkNotNullParameter(action, "action");
        boolean z = action.getAuthIsRequired() || !this.mAllowTrivialControls;
        if (!this.keyguardStateController.isShowing() || !z) {
            showSettingsDialogIfNeeded(action);
            action.invoke();
            return;
        }
        if (isLocked()) {
            this.broadcastSender.closeSystemDialogs();
            this.pendingAction = action;
        }
        this.activityStarter.dismissKeyguardThenExecute(new ControlActionCoordinatorImpl$$ExternalSyntheticLambda9(action), new ControlActionCoordinatorImpl$$ExternalSyntheticLambda10(this), true);
    }

    /* access modifiers changed from: private */
    /* renamed from: bouncerOrRun$lambda-1  reason: not valid java name */
    public static final boolean m2678bouncerOrRun$lambda1(Action action) {
        Intrinsics.checkNotNullParameter(action, "$action");
        Log.d("ControlsUiController", "Device unlocked, invoking controls action");
        action.invoke();
        return true;
    }

    /* access modifiers changed from: private */
    /* renamed from: bouncerOrRun$lambda-2  reason: not valid java name */
    public static final void m2679bouncerOrRun$lambda2(ControlActionCoordinatorImpl controlActionCoordinatorImpl) {
        Intrinsics.checkNotNullParameter(controlActionCoordinatorImpl, "this$0");
        controlActionCoordinatorImpl.pendingAction = null;
    }

    private final void vibrate(VibrationEffect vibrationEffect) {
        this.vibrator.vibrate(vibrationEffect);
    }

    /* access modifiers changed from: private */
    public final void showDetail(ControlViewHolder controlViewHolder, PendingIntent pendingIntent) {
        this.bgExecutor.execute(new ControlActionCoordinatorImpl$$ExternalSyntheticLambda3(this, pendingIntent, controlViewHolder));
    }

    /* access modifiers changed from: private */
    /* renamed from: showDetail$lambda-7  reason: not valid java name */
    public static final void m2681showDetail$lambda7(ControlActionCoordinatorImpl controlActionCoordinatorImpl, PendingIntent pendingIntent, ControlViewHolder controlViewHolder) {
        Intrinsics.checkNotNullParameter(controlActionCoordinatorImpl, "this$0");
        Intrinsics.checkNotNullParameter(pendingIntent, "$pendingIntent");
        Intrinsics.checkNotNullParameter(controlViewHolder, "$cvh");
        List<ResolveInfo> queryIntentActivities = controlActionCoordinatorImpl.context.getPackageManager().queryIntentActivities(pendingIntent.getIntent(), 65536);
        Intrinsics.checkNotNullExpressionValue(queryIntentActivities, "context.packageManager.q…EFAULT_ONLY\n            )");
        controlActionCoordinatorImpl.uiExecutor.execute(new ControlActionCoordinatorImpl$$ExternalSyntheticLambda2(queryIntentActivities, controlActionCoordinatorImpl, controlViewHolder, pendingIntent));
    }

    /* access modifiers changed from: private */
    /* renamed from: showDetail$lambda-7$lambda-6  reason: not valid java name */
    public static final void m2682showDetail$lambda7$lambda6(List list, ControlActionCoordinatorImpl controlActionCoordinatorImpl, ControlViewHolder controlViewHolder, PendingIntent pendingIntent) {
        Intrinsics.checkNotNullParameter(list, "$activities");
        Intrinsics.checkNotNullParameter(controlActionCoordinatorImpl, "this$0");
        Intrinsics.checkNotNullParameter(controlViewHolder, "$cvh");
        Intrinsics.checkNotNullParameter(pendingIntent, "$pendingIntent");
        if (!(!list.isEmpty()) || !controlActionCoordinatorImpl.taskViewFactory.isPresent()) {
            controlViewHolder.setErrorStatus();
        } else {
            controlActionCoordinatorImpl.taskViewFactory.get().create(controlActionCoordinatorImpl.context, controlActionCoordinatorImpl.uiExecutor, new ControlActionCoordinatorImpl$$ExternalSyntheticLambda0(controlActionCoordinatorImpl, pendingIntent, controlViewHolder));
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: showDetail$lambda-7$lambda-6$lambda-5  reason: not valid java name */
    public static final void m2683showDetail$lambda7$lambda6$lambda5(ControlActionCoordinatorImpl controlActionCoordinatorImpl, PendingIntent pendingIntent, ControlViewHolder controlViewHolder, TaskView taskView) {
        Intrinsics.checkNotNullParameter(controlActionCoordinatorImpl, "this$0");
        Intrinsics.checkNotNullParameter(pendingIntent, "$pendingIntent");
        Intrinsics.checkNotNullParameter(controlViewHolder, "$cvh");
        Context activityContext2 = controlActionCoordinatorImpl.getActivityContext();
        BroadcastSender broadcastSender2 = controlActionCoordinatorImpl.broadcastSender;
        Intrinsics.checkNotNullExpressionValue(taskView, "it");
        DetailDialog detailDialog = new DetailDialog(activityContext2, broadcastSender2, taskView, pendingIntent, controlViewHolder, controlActionCoordinatorImpl.keyguardStateController, controlActionCoordinatorImpl.activityStarter);
        detailDialog.setOnDismissListener(new ControlActionCoordinatorImpl$$ExternalSyntheticLambda1(controlActionCoordinatorImpl));
        detailDialog.show();
        controlActionCoordinatorImpl.dialog = detailDialog;
    }

    /* access modifiers changed from: private */
    /* renamed from: showDetail$lambda-7$lambda-6$lambda-5$lambda-4$lambda-3  reason: not valid java name */
    public static final void m2684showDetail$lambda7$lambda6$lambda5$lambda4$lambda3(ControlActionCoordinatorImpl controlActionCoordinatorImpl, DialogInterface dialogInterface) {
        Intrinsics.checkNotNullParameter(controlActionCoordinatorImpl, "this$0");
        controlActionCoordinatorImpl.dialog = null;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:3:0x0007, code lost:
        r5 = r4.userContextProvider.getUserContext().getSharedPreferences(com.android.systemui.statusbar.policy.DeviceControlsControllerImpl.PREFS_CONTROLS_FILE, 0);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final void showSettingsDialogIfNeeded(com.android.systemui.controls.p010ui.ControlActionCoordinatorImpl.Action r5) {
        /*
            r4 = this;
            boolean r5 = r5.getAuthIsRequired()
            if (r5 == 0) goto L_0x0007
            return
        L_0x0007:
            com.android.systemui.settings.UserContextProvider r5 = r4.userContextProvider
            android.content.Context r5 = r5.getUserContext()
            java.lang.String r0 = "controls_prefs"
            r1 = 0
            android.content.SharedPreferences r5 = r5.getSharedPreferences(r0, r1)
            java.lang.String r0 = "show_settings_attempts"
            int r0 = r5.getInt(r0, r1)
            r1 = 2
            if (r0 >= r1) goto L_0x00bd
            boolean r1 = r4.mShowDeviceControlsInLockscreen
            if (r1 == 0) goto L_0x0028
            boolean r1 = r4.mAllowTrivialControls
            if (r1 == 0) goto L_0x0028
            goto L_0x00bd
        L_0x0028:
            android.app.AlertDialog$Builder r1 = new android.app.AlertDialog$Builder
            android.content.Context r2 = r4.getActivityContext()
            r3 = 2132018213(0x7f140425, float:1.9674726E38)
            r1.<init>(r2, r3)
            r2 = 2131232465(0x7f0806d1, float:1.808104E38)
            android.app.AlertDialog$Builder r1 = r1.setIcon(r2)
            com.android.systemui.controls.ui.ControlActionCoordinatorImpl$$ExternalSyntheticLambda4 r2 = new com.android.systemui.controls.ui.ControlActionCoordinatorImpl$$ExternalSyntheticLambda4
            r2.<init>(r0, r5)
            android.app.AlertDialog$Builder r1 = r1.setOnCancelListener(r2)
            com.android.systemui.controls.ui.ControlActionCoordinatorImpl$$ExternalSyntheticLambda5 r2 = new com.android.systemui.controls.ui.ControlActionCoordinatorImpl$$ExternalSyntheticLambda5
            r2.<init>(r0, r5)
            r3 = 2131952231(0x7f130267, float:1.9540899E38)
            android.app.AlertDialog$Builder r1 = r1.setNeutralButton(r3, r2)
            boolean r2 = r4.mShowDeviceControlsInLockscreen
            r3 = 2131952232(0x7f130268, float:1.95409E38)
            if (r2 == 0) goto L_0x0077
            r2 = 2131952236(0x7f13026c, float:1.954091E38)
            android.app.AlertDialog$Builder r1 = r1.setTitle(r2)
            r2 = 2131952235(0x7f13026b, float:1.9540907E38)
            android.app.AlertDialog$Builder r1 = r1.setMessage(r2)
            com.android.systemui.controls.ui.ControlActionCoordinatorImpl$$ExternalSyntheticLambda6 r2 = new com.android.systemui.controls.ui.ControlActionCoordinatorImpl$$ExternalSyntheticLambda6
            r2.<init>(r0, r5, r4)
            android.app.AlertDialog$Builder r5 = r1.setPositiveButton(r3, r2)
            android.app.AlertDialog r5 = r5.create()
            android.app.Dialog r5 = (android.app.Dialog) r5
            r4.dialog = r5
            goto L_0x0096
        L_0x0077:
            r2 = 2131952234(0x7f13026a, float:1.9540905E38)
            android.app.AlertDialog$Builder r1 = r1.setTitle(r2)
            r2 = 2131952233(0x7f130269, float:1.9540903E38)
            android.app.AlertDialog$Builder r1 = r1.setMessage(r2)
            com.android.systemui.controls.ui.ControlActionCoordinatorImpl$$ExternalSyntheticLambda7 r2 = new com.android.systemui.controls.ui.ControlActionCoordinatorImpl$$ExternalSyntheticLambda7
            r2.<init>(r0, r5, r4)
            android.app.AlertDialog$Builder r5 = r1.setPositiveButton(r3, r2)
            android.app.AlertDialog r5 = r5.create()
            android.app.Dialog r5 = (android.app.Dialog) r5
            r4.dialog = r5
        L_0x0096:
            android.app.Dialog r5 = r4.dialog
            com.android.systemui.statusbar.phone.SystemUIDialog.registerDismissListener(r5)
            android.app.Dialog r5 = r4.dialog
            com.android.systemui.statusbar.phone.SystemUIDialog.setDialogSize(r5)
            android.app.Dialog r5 = r4.dialog
            kotlin.jvm.internal.Intrinsics.checkNotNull(r5)
            com.android.systemui.controls.ui.ControlActionCoordinatorImpl$showSettingsDialogIfNeeded$3 r0 = new com.android.systemui.controls.ui.ControlActionCoordinatorImpl$showSettingsDialogIfNeeded$3
            r0.<init>(r4)
            android.content.DialogInterface$OnShowListener r0 = (android.content.DialogInterface.OnShowListener) r0
            r5.setOnShowListener(r0)
            android.app.Dialog r5 = r4.dialog
            if (r5 == 0) goto L_0x00b6
            r5.create()
        L_0x00b6:
            android.app.Dialog r4 = r4.dialog
            if (r4 == 0) goto L_0x00bd
            r4.show()
        L_0x00bd:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.controls.p010ui.ControlActionCoordinatorImpl.showSettingsDialogIfNeeded(com.android.systemui.controls.ui.ControlActionCoordinatorImpl$Action):void");
    }

    /* access modifiers changed from: private */
    /* renamed from: showSettingsDialogIfNeeded$lambda-8  reason: not valid java name */
    public static final void m2687showSettingsDialogIfNeeded$lambda8(int i, SharedPreferences sharedPreferences, DialogInterface dialogInterface) {
        if (i < 2) {
            sharedPreferences.edit().putInt(DeviceControlsControllerImpl.PREFS_SETTINGS_DIALOG_ATTEMPTS, i + 1).commit();
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: showSettingsDialogIfNeeded$lambda-9  reason: not valid java name */
    public static final void m2688showSettingsDialogIfNeeded$lambda9(int i, SharedPreferences sharedPreferences, DialogInterface dialogInterface, int i2) {
        if (i != 2) {
            sharedPreferences.edit().putInt(DeviceControlsControllerImpl.PREFS_SETTINGS_DIALOG_ATTEMPTS, 2).commit();
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: showSettingsDialogIfNeeded$lambda-10  reason: not valid java name */
    public static final void m2685showSettingsDialogIfNeeded$lambda10(int i, SharedPreferences sharedPreferences, ControlActionCoordinatorImpl controlActionCoordinatorImpl, DialogInterface dialogInterface, int i2) {
        Intrinsics.checkNotNullParameter(controlActionCoordinatorImpl, "this$0");
        if (i != 2) {
            sharedPreferences.edit().putInt(DeviceControlsControllerImpl.PREFS_SETTINGS_DIALOG_ATTEMPTS, 2).commit();
        }
        controlActionCoordinatorImpl.secureSettings.putIntForUser("lockscreen_allow_trivial_controls", 1, -2);
    }

    /* access modifiers changed from: private */
    /* renamed from: showSettingsDialogIfNeeded$lambda-11  reason: not valid java name */
    public static final void m2686showSettingsDialogIfNeeded$lambda11(int i, SharedPreferences sharedPreferences, ControlActionCoordinatorImpl controlActionCoordinatorImpl, DialogInterface dialogInterface, int i2) {
        Intrinsics.checkNotNullParameter(controlActionCoordinatorImpl, "this$0");
        if (i != 2) {
            sharedPreferences.edit().putInt(DeviceControlsControllerImpl.PREFS_SETTINGS_DIALOG_ATTEMPTS, 2).commit();
        }
        controlActionCoordinatorImpl.secureSettings.putIntForUser("lockscreen_show_controls", 1, -2);
        controlActionCoordinatorImpl.secureSettings.putIntForUser("lockscreen_allow_trivial_controls", 1, -2);
    }

    public final Action createAction(String str, Function0<Unit> function0, boolean z, boolean z2) {
        Intrinsics.checkNotNullParameter(str, "controlId");
        Intrinsics.checkNotNullParameter(function0, "f");
        return new Action(this, str, function0, z, z2);
    }

    @Metadata(mo65042d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u000b\b\u0004\u0018\u00002\u00020\u0001B+\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\b¢\u0006\u0002\u0010\nJ\u0006\u0010\u0012\u001a\u00020\u0006R\u0011\u0010\t\u001a\u00020\b¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0007\u001a\u00020\b¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\fR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0017\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011¨\u0006\u0013"}, mo65043d2 = {"Lcom/android/systemui/controls/ui/ControlActionCoordinatorImpl$Action;", "", "controlId", "", "f", "Lkotlin/Function0;", "", "blockable", "", "authIsRequired", "(Lcom/android/systemui/controls/ui/ControlActionCoordinatorImpl;Ljava/lang/String;Lkotlin/jvm/functions/Function0;ZZ)V", "getAuthIsRequired", "()Z", "getBlockable", "getControlId", "()Ljava/lang/String;", "getF", "()Lkotlin/jvm/functions/Function0;", "invoke", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* renamed from: com.android.systemui.controls.ui.ControlActionCoordinatorImpl$Action */
    /* compiled from: ControlActionCoordinatorImpl.kt */
    public final class Action {
        private final boolean authIsRequired;
        private final boolean blockable;
        private final String controlId;

        /* renamed from: f */
        private final Function0<Unit> f298f;
        final /* synthetic */ ControlActionCoordinatorImpl this$0;

        public Action(ControlActionCoordinatorImpl controlActionCoordinatorImpl, String str, Function0<Unit> function0, boolean z, boolean z2) {
            Intrinsics.checkNotNullParameter(str, "controlId");
            Intrinsics.checkNotNullParameter(function0, "f");
            this.this$0 = controlActionCoordinatorImpl;
            this.controlId = str;
            this.f298f = function0;
            this.blockable = z;
            this.authIsRequired = z2;
        }

        public final String getControlId() {
            return this.controlId;
        }

        public final Function0<Unit> getF() {
            return this.f298f;
        }

        public final boolean getBlockable() {
            return this.blockable;
        }

        public final boolean getAuthIsRequired() {
            return this.authIsRequired;
        }

        public final void invoke() {
            if (!this.blockable || this.this$0.shouldRunAction(this.controlId)) {
                this.f298f.invoke();
            }
        }
    }
}
