package com.android.systemui.controls.p010ui;

import android.app.ActivityTaskManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Insets;
import android.graphics.Rect;
import android.icu.text.DateFormat;
import android.net.connectivity.com.android.net.module.util.NetworkStackConstants;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.widget.ImageView;
import com.android.internal.policy.ScreenDecorationsUtils;
import com.android.p019wm.shell.TaskView;
import com.android.systemui.C1893R;
import com.android.systemui.broadcast.BroadcastSender;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000h\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u0000 42\u00020\u0001:\u00014B=\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r\u0012\u0006\u0010\u000e\u001a\u00020\u000f¢\u0006\u0002\u0010\u0010J\b\u0010/\u001a\u000200H\u0016J\u0006\u00101\u001a\u000202J\u0006\u00103\u001a\u000200R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\u000e\u001a\u00020\u000f¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0011\u0010\n\u001a\u00020\u000b¢\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u001a\u0010\u0019\u001a\u00020\u001aX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001b\u0010\u001c\"\u0004\b\u001d\u0010\u001eR\u000e\u0010\u001f\u001a\u00020 X\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\f\u001a\u00020\r¢\u0006\b\n\u0000\u001a\u0004\b!\u0010\"R\u0011\u0010\b\u001a\u00020\t¢\u0006\b\n\u0000\u001a\u0004\b#\u0010$R\u0011\u0010%\u001a\u00020&¢\u0006\b\n\u0000\u001a\u0004\b'\u0010(R\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b)\u0010*R\u000e\u0010+\u001a\u00020,X.¢\u0006\u0002\n\u0000R\u000e\u0010-\u001a\u00020.X\u0004¢\u0006\u0002\n\u0000¨\u00065"}, mo64987d2 = {"Lcom/android/systemui/controls/ui/DetailDialog;", "Landroid/app/Dialog;", "activityContext", "Landroid/content/Context;", "broadcastSender", "Lcom/android/systemui/broadcast/BroadcastSender;", "taskView", "Lcom/android/wm/shell/TaskView;", "pendingIntent", "Landroid/app/PendingIntent;", "cvh", "Lcom/android/systemui/controls/ui/ControlViewHolder;", "keyguardStateController", "Lcom/android/systemui/statusbar/policy/KeyguardStateController;", "activityStarter", "Lcom/android/systemui/plugins/ActivityStarter;", "(Landroid/content/Context;Lcom/android/systemui/broadcast/BroadcastSender;Lcom/android/wm/shell/TaskView;Landroid/app/PendingIntent;Lcom/android/systemui/controls/ui/ControlViewHolder;Lcom/android/systemui/statusbar/policy/KeyguardStateController;Lcom/android/systemui/plugins/ActivityStarter;)V", "getActivityContext", "()Landroid/content/Context;", "getActivityStarter", "()Lcom/android/systemui/plugins/ActivityStarter;", "getBroadcastSender", "()Lcom/android/systemui/broadcast/BroadcastSender;", "getCvh", "()Lcom/android/systemui/controls/ui/ControlViewHolder;", "detailTaskId", "", "getDetailTaskId", "()I", "setDetailTaskId", "(I)V", "fillInIntent", "Landroid/content/Intent;", "getKeyguardStateController", "()Lcom/android/systemui/statusbar/policy/KeyguardStateController;", "getPendingIntent", "()Landroid/app/PendingIntent;", "stateCallback", "Lcom/android/wm/shell/TaskView$Listener;", "getStateCallback", "()Lcom/android/wm/shell/TaskView$Listener;", "getTaskView", "()Lcom/android/wm/shell/TaskView;", "taskViewContainer", "Landroid/view/View;", "taskWidthPercentWidth", "", "dismiss", "", "getTaskViewBounds", "Landroid/graphics/Rect;", "removeDetailTask", "Companion", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.systemui.controls.ui.DetailDialog */
/* compiled from: DetailDialog.kt */
public final class DetailDialog extends Dialog {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final String EXTRA_USE_PANEL = "controls.DISPLAY_IN_PANEL";
    private final Context activityContext;
    private final ActivityStarter activityStarter;
    private final BroadcastSender broadcastSender;
    private final ControlViewHolder cvh;
    private int detailTaskId = -1;
    /* access modifiers changed from: private */
    public final Intent fillInIntent;
    private final KeyguardStateController keyguardStateController;
    private final PendingIntent pendingIntent;
    private final TaskView.Listener stateCallback;
    private final TaskView taskView;
    /* access modifiers changed from: private */
    public View taskViewContainer;
    /* access modifiers changed from: private */
    public final float taskWidthPercentWidth;

    public final Context getActivityContext() {
        return this.activityContext;
    }

    public final BroadcastSender getBroadcastSender() {
        return this.broadcastSender;
    }

    public final TaskView getTaskView() {
        return this.taskView;
    }

    public final PendingIntent getPendingIntent() {
        return this.pendingIntent;
    }

    public final ControlViewHolder getCvh() {
        return this.cvh;
    }

    public final KeyguardStateController getKeyguardStateController() {
        return this.keyguardStateController;
    }

    public final ActivityStarter getActivityStarter() {
        return this.activityStarter;
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public DetailDialog(Context context, BroadcastSender broadcastSender2, TaskView taskView2, PendingIntent pendingIntent2, ControlViewHolder controlViewHolder, KeyguardStateController keyguardStateController2, ActivityStarter activityStarter2) {
        super(context, C1893R.style.Theme_SystemUI_Dialog_Control_DetailPanel);
        Intrinsics.checkNotNullParameter(context, "activityContext");
        Intrinsics.checkNotNullParameter(broadcastSender2, "broadcastSender");
        Intrinsics.checkNotNullParameter(taskView2, "taskView");
        Intrinsics.checkNotNullParameter(pendingIntent2, "pendingIntent");
        Intrinsics.checkNotNullParameter(controlViewHolder, "cvh");
        Intrinsics.checkNotNullParameter(keyguardStateController2, "keyguardStateController");
        Intrinsics.checkNotNullParameter(activityStarter2, "activityStarter");
        this.activityContext = context;
        this.broadcastSender = broadcastSender2;
        this.taskView = taskView2;
        this.pendingIntent = pendingIntent2;
        this.cvh = controlViewHolder;
        this.keyguardStateController = keyguardStateController2;
        this.activityStarter = activityStarter2;
        this.taskWidthPercentWidth = context.getResources().getFloat(C1893R.dimen.controls_task_view_width_percentage);
        Intent intent = new Intent();
        intent.putExtra(EXTRA_USE_PANEL, true);
        intent.addFlags(524288);
        intent.addFlags(134217728);
        this.fillInIntent = intent;
        TaskView.Listener detailDialog$stateCallback$1 = new DetailDialog$stateCallback$1(this);
        this.stateCallback = detailDialog$stateCallback$1;
        getWindow().addFlags(32);
        getWindow().addPrivateFlags(NetworkStackConstants.NEIGHBOR_ADVERTISEMENT_FLAG_OVERRIDE);
        setContentView(C1893R.layout.controls_detail_dialog);
        View requireViewById = requireViewById(C1893R.C1897id.control_task_view_container);
        Intrinsics.checkNotNullExpressionValue(requireViewById, "requireViewById<ViewGrou…trol_task_view_container)");
        this.taskViewContainer = requireViewById;
        ViewGroup viewGroup = (ViewGroup) requireViewById(C1893R.C1897id.controls_activity_view);
        viewGroup.addView(taskView2);
        viewGroup.setAlpha(0.0f);
        ((ImageView) requireViewById(C1893R.C1897id.control_detail_close)).setOnClickListener(new DetailDialog$$ExternalSyntheticLambda0(this));
        requireViewById(C1893R.C1897id.control_detail_root).setOnClickListener(new DetailDialog$$ExternalSyntheticLambda1(this));
        ((ImageView) requireViewById(C1893R.C1897id.control_detail_open_in_app)).setOnClickListener(new DetailDialog$$ExternalSyntheticLambda2(this));
        getWindow().getDecorView().setOnApplyWindowInsetsListener(new DetailDialog$$ExternalSyntheticLambda3());
        if (ScreenDecorationsUtils.supportsRoundedCornersOnWindows(getContext().getResources())) {
            taskView2.setCornerRadius((float) getContext().getResources().getDimensionPixelSize(C1893R.dimen.controls_activity_view_corner_radius));
        }
        taskView2.setListener(controlViewHolder.getUiExecutor(), detailDialog$stateCallback$1);
    }

    @Metadata(mo64986d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\u0005"}, mo64987d2 = {"Lcom/android/systemui/controls/ui/DetailDialog$Companion;", "", "()V", "EXTRA_USE_PANEL", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* renamed from: com.android.systemui.controls.ui.DetailDialog$Companion */
    /* compiled from: DetailDialog.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    public final int getDetailTaskId() {
        return this.detailTaskId;
    }

    public final void setDetailTaskId(int i) {
        this.detailTaskId = i;
    }

    public final void removeDetailTask() {
        if (this.detailTaskId != -1) {
            ActivityTaskManager.getInstance().removeTask(this.detailTaskId);
            this.detailTaskId = -1;
        }
    }

    public final TaskView.Listener getStateCallback() {
        return this.stateCallback;
    }

    /* access modifiers changed from: private */
    /* renamed from: lambda-3$lambda-2  reason: not valid java name */
    public static final void m2713lambda3$lambda2(DetailDialog detailDialog, View view) {
        Intrinsics.checkNotNullParameter(detailDialog, "this$0");
        Intrinsics.checkNotNullParameter(view, "<anonymous parameter 0>");
        detailDialog.dismiss();
    }

    /* access modifiers changed from: private */
    /* renamed from: lambda-5$lambda-4  reason: not valid java name */
    public static final void m2714lambda5$lambda4(DetailDialog detailDialog, View view) {
        Intrinsics.checkNotNullParameter(detailDialog, "this$0");
        Intrinsics.checkNotNullParameter(view, "<anonymous parameter 0>");
        detailDialog.dismiss();
    }

    /* access modifiers changed from: private */
    /* renamed from: lambda-8$lambda-7  reason: not valid java name */
    public static final void m2715lambda8$lambda7(DetailDialog detailDialog, View view) {
        Intrinsics.checkNotNullParameter(detailDialog, "this$0");
        Intrinsics.checkNotNullParameter(view, DateFormat.ABBR_GENERIC_TZ);
        detailDialog.removeDetailTask();
        detailDialog.dismiss();
        DetailDialog$$ExternalSyntheticLambda4 detailDialog$$ExternalSyntheticLambda4 = new DetailDialog$$ExternalSyntheticLambda4(detailDialog);
        if (detailDialog.keyguardStateController.isUnlocked()) {
            detailDialog$$ExternalSyntheticLambda4.onDismiss();
        } else {
            detailDialog.activityStarter.dismissKeyguardThenExecute(detailDialog$$ExternalSyntheticLambda4, (Runnable) null, true);
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: lambda-8$lambda-7$lambda-6  reason: not valid java name */
    public static final boolean m2716lambda8$lambda7$lambda6(DetailDialog detailDialog) {
        Intrinsics.checkNotNullParameter(detailDialog, "this$0");
        detailDialog.broadcastSender.closeSystemDialogs();
        detailDialog.pendingIntent.send();
        return false;
    }

    /* access modifiers changed from: private */
    /* renamed from: _init_$lambda-9  reason: not valid java name */
    public static final WindowInsets m2712_init_$lambda9(View view, WindowInsets windowInsets) {
        Intrinsics.checkNotNullParameter(view, DateFormat.ABBR_GENERIC_TZ);
        Intrinsics.checkNotNullParameter(windowInsets, "insets");
        int paddingLeft = view.getPaddingLeft();
        int paddingRight = view.getPaddingRight();
        Insets insets = windowInsets.getInsets(WindowInsets.Type.systemBars());
        view.setPadding(paddingLeft, insets.top, paddingRight, insets.bottom);
        return WindowInsets.CONSUMED;
    }

    public final Rect getTaskViewBounds() {
        WindowMetrics currentWindowMetrics = ((WindowManager) getContext().getSystemService(WindowManager.class)).getCurrentWindowMetrics();
        Rect bounds = currentWindowMetrics.getBounds();
        Insets insetsIgnoringVisibility = currentWindowMetrics.getWindowInsets().getInsetsIgnoringVisibility(WindowInsets.Type.systemBars() | WindowInsets.Type.displayCutout());
        return new Rect(bounds.left - insetsIgnoringVisibility.left, bounds.top + insetsIgnoringVisibility.top + getContext().getResources().getDimensionPixelSize(C1893R.dimen.controls_detail_dialog_header_height), bounds.right - insetsIgnoringVisibility.right, bounds.bottom - insetsIgnoringVisibility.bottom);
    }

    public void dismiss() {
        if (isShowing()) {
            this.taskView.release();
            if (getOwnerActivity() != null && !getOwnerActivity().isFinishing()) {
                super.dismiss();
            }
        }
    }
}
