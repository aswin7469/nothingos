package com.android.systemui.controls.p010ui;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.service.controls.Control;
import android.view.View;
import com.android.systemui.C1894R;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0018\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0016J\u0010\u0010\u000f\u001a\u00020\n2\u0006\u0010\u0003\u001a\u00020\u0004H\u0016J\u0018\u0010\u0010\u001a\u00020\n2\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\fH\u0002R\u001a\u0010\u0003\u001a\u00020\u0004X.¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\b¨\u0006\u0011"}, mo65043d2 = {"Lcom/android/systemui/controls/ui/StatusBehavior;", "Lcom/android/systemui/controls/ui/Behavior;", "()V", "cvh", "Lcom/android/systemui/controls/ui/ControlViewHolder;", "getCvh", "()Lcom/android/systemui/controls/ui/ControlViewHolder;", "setCvh", "(Lcom/android/systemui/controls/ui/ControlViewHolder;)V", "bind", "", "cws", "Lcom/android/systemui/controls/ui/ControlWithState;", "colorOffset", "", "initialize", "showNotFoundDialog", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.systemui.controls.ui.StatusBehavior */
/* compiled from: StatusBehavior.kt */
public final class StatusBehavior implements Behavior {
    public ControlViewHolder cvh;

    public final ControlViewHolder getCvh() {
        ControlViewHolder controlViewHolder = this.cvh;
        if (controlViewHolder != null) {
            return controlViewHolder;
        }
        Intrinsics.throwUninitializedPropertyAccessException("cvh");
        return null;
    }

    public final void setCvh(ControlViewHolder controlViewHolder) {
        Intrinsics.checkNotNullParameter(controlViewHolder, "<set-?>");
        this.cvh = controlViewHolder;
    }

    public void initialize(ControlViewHolder controlViewHolder) {
        Intrinsics.checkNotNullParameter(controlViewHolder, "cvh");
        setCvh(controlViewHolder);
    }

    public void bind(ControlWithState controlWithState, int i) {
        int i2;
        Intrinsics.checkNotNullParameter(controlWithState, "cws");
        Control control = controlWithState.getControl();
        int status = control != null ? control.getStatus() : 0;
        if (status == 2) {
            getCvh().getLayout().setOnClickListener(new StatusBehavior$$ExternalSyntheticLambda2(this, controlWithState));
            getCvh().getLayout().setOnLongClickListener(new StatusBehavior$$ExternalSyntheticLambda3(this, controlWithState));
            i2 = C1894R.string.controls_error_removed;
        } else if (status == 3) {
            i2 = C1894R.string.controls_error_generic;
        } else if (status != 4) {
            getCvh().setLoading(true);
            i2 = 17040589;
        } else {
            i2 = C1894R.string.controls_error_timeout;
        }
        ControlViewHolder cvh2 = getCvh();
        String string = getCvh().getContext().getString(i2);
        Intrinsics.checkNotNullExpressionValue(string, "cvh.context.getString(msg)");
        ControlViewHolder.setStatusText$default(cvh2, string, false, 2, (Object) null);
        ControlViewHolder.applyRenderInfo$SystemUI_nothingRelease$default(getCvh(), false, i, false, 4, (Object) null);
    }

    /* access modifiers changed from: private */
    /* renamed from: bind$lambda-0  reason: not valid java name */
    public static final void m2723bind$lambda0(StatusBehavior statusBehavior, ControlWithState controlWithState, View view) {
        Intrinsics.checkNotNullParameter(statusBehavior, "this$0");
        Intrinsics.checkNotNullParameter(controlWithState, "$cws");
        statusBehavior.showNotFoundDialog(statusBehavior.getCvh(), controlWithState);
    }

    /* access modifiers changed from: private */
    /* renamed from: bind$lambda-1  reason: not valid java name */
    public static final boolean m2724bind$lambda1(StatusBehavior statusBehavior, ControlWithState controlWithState, View view) {
        Intrinsics.checkNotNullParameter(statusBehavior, "this$0");
        Intrinsics.checkNotNullParameter(controlWithState, "$cws");
        statusBehavior.showNotFoundDialog(statusBehavior.getCvh(), controlWithState);
        return true;
    }

    private final void showNotFoundDialog(ControlViewHolder controlViewHolder, ControlWithState controlWithState) {
        PackageManager packageManager = controlViewHolder.getContext().getPackageManager();
        CharSequence applicationLabel = packageManager.getApplicationLabel(packageManager.getApplicationInfo(controlWithState.getComponentName().getPackageName(), 128));
        AlertDialog.Builder builder = new AlertDialog.Builder(controlViewHolder.getContext(), 16974545);
        Resources resources = controlViewHolder.getContext().getResources();
        builder.setTitle(resources.getString(C1894R.string.controls_error_removed_title));
        builder.setMessage(resources.getString(C1894R.string.controls_error_removed_message, new Object[]{controlViewHolder.getTitle().getText(), applicationLabel}));
        builder.setPositiveButton(C1894R.string.controls_open_app, new StatusBehavior$$ExternalSyntheticLambda0(controlWithState, builder, controlViewHolder));
        builder.setNegativeButton(17039360, new StatusBehavior$$ExternalSyntheticLambda1());
        AlertDialog create = builder.create();
        create.getWindow().setType(2020);
        create.show();
        controlViewHolder.setVisibleDialog(create);
    }

    /* access modifiers changed from: private */
    /* renamed from: showNotFoundDialog$lambda-4$lambda-2  reason: not valid java name */
    public static final void m2725showNotFoundDialog$lambda4$lambda2(ControlWithState controlWithState, AlertDialog.Builder builder, ControlViewHolder controlViewHolder, DialogInterface dialogInterface, int i) {
        PendingIntent appIntent;
        Intrinsics.checkNotNullParameter(controlWithState, "$cws");
        Intrinsics.checkNotNullParameter(builder, "$this_apply");
        Intrinsics.checkNotNullParameter(controlViewHolder, "$cvh");
        try {
            Control control = controlWithState.getControl();
            if (!(control == null || (appIntent = control.getAppIntent()) == null)) {
                appIntent.send();
            }
            builder.getContext().sendBroadcast(new Intent("android.intent.action.CLOSE_SYSTEM_DIALOGS"));
        } catch (PendingIntent.CanceledException unused) {
            controlViewHolder.setErrorStatus();
        }
        dialogInterface.dismiss();
    }

    /* access modifiers changed from: private */
    /* renamed from: showNotFoundDialog$lambda-4$lambda-3  reason: not valid java name */
    public static final void m2726showNotFoundDialog$lambda4$lambda3(DialogInterface dialogInterface, int i) {
        dialogInterface.cancel();
    }
}
