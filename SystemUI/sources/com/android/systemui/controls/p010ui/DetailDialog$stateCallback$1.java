package com.android.systemui.controls.p010ui;

import android.app.ActivityOptions;
import android.content.ComponentName;
import android.view.View;
import android.view.ViewGroup;
import com.android.p019wm.shell.TaskView;
import com.android.systemui.C1894R;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000!\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\b\u0010\u0006\u001a\u00020\u0003H\u0016J\b\u0010\u0007\u001a\u00020\u0003H\u0016J\u001a\u0010\b\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\b\u0010\t\u001a\u0004\u0018\u00010\nH\u0016J\u0010\u0010\u000b\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016¨\u0006\f"}, mo65043d2 = {"com/android/systemui/controls/ui/DetailDialog$stateCallback$1", "Lcom/android/wm/shell/TaskView$Listener;", "onBackPressedOnTaskRoot", "", "taskId", "", "onInitialized", "onReleased", "onTaskCreated", "name", "Landroid/content/ComponentName;", "onTaskRemovalStarted", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.systemui.controls.ui.DetailDialog$stateCallback$1 */
/* compiled from: DetailDialog.kt */
public final class DetailDialog$stateCallback$1 implements TaskView.Listener {
    final /* synthetic */ DetailDialog this$0;

    DetailDialog$stateCallback$1(DetailDialog detailDialog) {
        this.this$0 = detailDialog;
    }

    public void onInitialized() {
        View access$getTaskViewContainer$p = this.this$0.taskViewContainer;
        if (access$getTaskViewContainer$p == null) {
            Intrinsics.throwUninitializedPropertyAccessException("taskViewContainer");
            access$getTaskViewContainer$p = null;
        }
        DetailDialog detailDialog = this.this$0;
        ViewGroup.LayoutParams layoutParams = access$getTaskViewContainer$p.getLayoutParams();
        layoutParams.width = (int) (((float) access$getTaskViewContainer$p.getWidth()) * detailDialog.taskWidthPercentWidth);
        access$getTaskViewContainer$p.setLayoutParams(layoutParams);
        this.this$0.getTaskView().startActivity(this.this$0.getPendingIntent(), this.this$0.fillInIntent, ActivityOptions.makeCustomAnimation(this.this$0.getActivityContext(), 0, 0), this.this$0.getTaskViewBounds());
    }

    public void onTaskRemovalStarted(int i) {
        this.this$0.setDetailTaskId(-1);
        this.this$0.dismiss();
    }

    public void onTaskCreated(int i, ComponentName componentName) {
        this.this$0.setDetailTaskId(i);
        ((ViewGroup) this.this$0.requireViewById(C1894R.C1898id.controls_activity_view)).setAlpha(1.0f);
    }

    public void onReleased() {
        this.this$0.removeDetailTask();
    }

    public void onBackPressedOnTaskRoot(int i) {
        this.this$0.dismiss();
    }
}
