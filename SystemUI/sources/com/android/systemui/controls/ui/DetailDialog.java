package com.android.systemui.controls.ui;

import android.app.ActivityOptions;
import android.app.ActivityTaskManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Insets;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.widget.ImageView;
import com.android.internal.policy.ScreenDecorationsUtils;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.R$style;
import com.android.wm.shell.TaskView;
import kotlin.Unit;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: DetailDialog.kt */
/* loaded from: classes.dex */
public final class DetailDialog extends Dialog {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @Nullable
    private final Context activityContext;
    @NotNull
    private final ControlViewHolder cvh;
    private int detailTaskId;
    @NotNull
    private final Intent fillInIntent;
    @NotNull
    private final PendingIntent pendingIntent;
    @NotNull
    private final TaskView.Listener stateCallback;
    @NotNull
    private final TaskView taskView;

    @Nullable
    public final Context getActivityContext() {
        return this.activityContext;
    }

    @NotNull
    public final TaskView getTaskView() {
        return this.taskView;
    }

    @NotNull
    public final PendingIntent getPendingIntent() {
        return this.pendingIntent;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public DetailDialog(@Nullable Context context, @NotNull TaskView taskView, @NotNull PendingIntent pendingIntent, @NotNull ControlViewHolder cvh) {
        super(context == null ? cvh.getContext() : context, R$style.Theme_SystemUI_Dialog_Control_DetailPanel);
        Intrinsics.checkNotNullParameter(taskView, "taskView");
        Intrinsics.checkNotNullParameter(pendingIntent, "pendingIntent");
        Intrinsics.checkNotNullParameter(cvh, "cvh");
        this.activityContext = context;
        this.taskView = taskView;
        this.pendingIntent = pendingIntent;
        this.cvh = cvh;
        this.detailTaskId = -1;
        Intent intent = new Intent();
        intent.putExtra("controls.DISPLAY_IN_PANEL", true);
        intent.addFlags(524288);
        intent.addFlags(134217728);
        Unit unit = Unit.INSTANCE;
        this.fillInIntent = intent;
        TaskView.Listener listener = new TaskView.Listener() { // from class: com.android.systemui.controls.ui.DetailDialog$stateCallback$1
            @Override // com.android.wm.shell.TaskView.Listener
            public void onInitialized() {
                Intent intent2;
                Context activityContext = DetailDialog.this.getActivityContext();
                ActivityOptions makeCustomAnimation = activityContext == null ? null : ActivityOptions.makeCustomAnimation(activityContext, 0, 0);
                if (makeCustomAnimation == null) {
                    makeCustomAnimation = ActivityOptions.makeBasic();
                }
                TaskView taskView2 = DetailDialog.this.getTaskView();
                PendingIntent pendingIntent2 = DetailDialog.this.getPendingIntent();
                intent2 = DetailDialog.this.fillInIntent;
                taskView2.startActivity(pendingIntent2, intent2, makeCustomAnimation, DetailDialog.this.getTaskViewBounds());
            }

            @Override // com.android.wm.shell.TaskView.Listener
            public void onTaskRemovalStarted(int i) {
                DetailDialog.this.setDetailTaskId(-1);
                DetailDialog.this.dismiss();
            }

            @Override // com.android.wm.shell.TaskView.Listener
            public void onTaskCreated(int i, @Nullable ComponentName componentName) {
                DetailDialog.this.setDetailTaskId(i);
                ((ViewGroup) DetailDialog.this.requireViewById(R$id.controls_activity_view)).setAlpha(1.0f);
            }

            @Override // com.android.wm.shell.TaskView.Listener
            public void onReleased() {
                DetailDialog.this.removeDetailTask();
            }

            @Override // com.android.wm.shell.TaskView.Listener
            public void onBackPressedOnTaskRoot(int i) {
                DetailDialog.this.dismiss();
            }
        };
        this.stateCallback = listener;
        if (context == null) {
            getWindow().setType(2020);
        }
        getWindow().addFlags(32);
        getWindow().addPrivateFlags(536870912);
        setContentView(R$layout.controls_detail_dialog);
        ViewGroup viewGroup = (ViewGroup) requireViewById(R$id.controls_activity_view);
        viewGroup.addView(getTaskView());
        viewGroup.setAlpha(0.0f);
        ((ImageView) requireViewById(R$id.control_detail_close)).setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.controls.ui.DetailDialog$2$1
            @Override // android.view.View.OnClickListener
            public final void onClick(@NotNull View noName_0) {
                Intrinsics.checkNotNullParameter(noName_0, "$noName_0");
                DetailDialog.this.dismiss();
            }
        });
        final ImageView imageView = (ImageView) requireViewById(R$id.control_detail_open_in_app);
        imageView.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.controls.ui.DetailDialog$3$1
            @Override // android.view.View.OnClickListener
            public final void onClick(@NotNull View v) {
                Intrinsics.checkNotNullParameter(v, "v");
                DetailDialog.this.removeDetailTask();
                DetailDialog.this.dismiss();
                imageView.getContext().sendBroadcast(new Intent("android.intent.action.CLOSE_SYSTEM_DIALOGS"));
                DetailDialog.this.getPendingIntent().send();
            }
        });
        getWindow().getDecorView().setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() { // from class: com.android.systemui.controls.ui.DetailDialog.4
            @Override // android.view.View.OnApplyWindowInsetsListener
            public final WindowInsets onApplyWindowInsets(@NotNull View v, @NotNull WindowInsets insets) {
                Intrinsics.checkNotNullParameter(v, "v");
                Intrinsics.checkNotNullParameter(insets, "insets");
                TaskView taskView2 = DetailDialog.this.getTaskView();
                taskView2.setPadding(taskView2.getPaddingLeft(), taskView2.getPaddingTop(), taskView2.getPaddingRight(), insets.getInsets(WindowInsets.Type.systemBars()).bottom);
                int paddingLeft = v.getPaddingLeft();
                int paddingBottom = v.getPaddingBottom();
                v.setPadding(paddingLeft, insets.getInsets(WindowInsets.Type.systemBars()).top, v.getPaddingRight(), paddingBottom);
                return WindowInsets.CONSUMED;
            }
        });
        if (ScreenDecorationsUtils.supportsRoundedCornersOnWindows(getContext().getResources())) {
            taskView.setCornerRadius(getContext().getResources().getDimensionPixelSize(R$dimen.controls_activity_view_corner_radius));
        }
        taskView.setListener(cvh.getUiExecutor(), listener);
    }

    /* compiled from: DetailDialog.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    public final void setDetailTaskId(int i) {
        this.detailTaskId = i;
    }

    public final void removeDetailTask() {
        if (this.detailTaskId == -1) {
            return;
        }
        ActivityTaskManager.getInstance().removeTask(this.detailTaskId);
        this.detailTaskId = -1;
    }

    @NotNull
    public final Rect getTaskViewBounds() {
        WindowMetrics currentWindowMetrics = ((WindowManager) getContext().getSystemService(WindowManager.class)).getCurrentWindowMetrics();
        Rect bounds = currentWindowMetrics.getBounds();
        Insets insetsIgnoringVisibility = currentWindowMetrics.getWindowInsets().getInsetsIgnoringVisibility(WindowInsets.Type.systemBars() | WindowInsets.Type.displayCutout());
        return new Rect(bounds.left - insetsIgnoringVisibility.left, bounds.top + insetsIgnoringVisibility.top + getContext().getResources().getDimensionPixelSize(R$dimen.controls_detail_dialog_header_height), bounds.right - insetsIgnoringVisibility.right, bounds.bottom - insetsIgnoringVisibility.bottom);
    }

    @Override // android.app.Dialog, android.content.DialogInterface
    public void dismiss() {
        if (!isShowing()) {
            return;
        }
        this.taskView.release();
        super.dismiss();
    }
}
