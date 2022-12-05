package com.android.systemui.controls.ui;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.service.controls.Control;
import android.view.View;
import com.android.systemui.R$string;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: StatusBehavior.kt */
/* loaded from: classes.dex */
public final class StatusBehavior implements Behavior {
    public ControlViewHolder cvh;

    @NotNull
    public final ControlViewHolder getCvh() {
        ControlViewHolder controlViewHolder = this.cvh;
        if (controlViewHolder != null) {
            return controlViewHolder;
        }
        Intrinsics.throwUninitializedPropertyAccessException("cvh");
        throw null;
    }

    public final void setCvh(@NotNull ControlViewHolder controlViewHolder) {
        Intrinsics.checkNotNullParameter(controlViewHolder, "<set-?>");
        this.cvh = controlViewHolder;
    }

    @Override // com.android.systemui.controls.ui.Behavior
    public void initialize(@NotNull ControlViewHolder cvh) {
        Intrinsics.checkNotNullParameter(cvh, "cvh");
        setCvh(cvh);
    }

    @Override // com.android.systemui.controls.ui.Behavior
    public void bind(@NotNull final ControlWithState cws, int i) {
        int i2;
        Intrinsics.checkNotNullParameter(cws, "cws");
        Control control = cws.getControl();
        int status = control == null ? 0 : control.getStatus();
        if (status == 2) {
            getCvh().getLayout().setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.controls.ui.StatusBehavior$bind$msg$1
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    StatusBehavior statusBehavior = StatusBehavior.this;
                    statusBehavior.showNotFoundDialog(statusBehavior.getCvh(), cws);
                }
            });
            getCvh().getLayout().setOnLongClickListener(new View.OnLongClickListener() { // from class: com.android.systemui.controls.ui.StatusBehavior$bind$msg$2
                @Override // android.view.View.OnLongClickListener
                public final boolean onLongClick(View view) {
                    StatusBehavior statusBehavior = StatusBehavior.this;
                    statusBehavior.showNotFoundDialog(statusBehavior.getCvh(), cws);
                    return true;
                }
            });
            i2 = R$string.controls_error_removed;
        } else if (status == 3) {
            i2 = R$string.controls_error_generic;
        } else if (status == 4) {
            i2 = R$string.controls_error_timeout;
        } else {
            getCvh().setLoading(true);
            i2 = 17040512;
        }
        ControlViewHolder cvh = getCvh();
        String string = getCvh().getContext().getString(i2);
        Intrinsics.checkNotNullExpressionValue(string, "cvh.context.getString(msg)");
        ControlViewHolder.setStatusText$default(cvh, string, false, 2, null);
        ControlViewHolder.applyRenderInfo$frameworks__base__packages__SystemUI__android_common__SystemUI_core$default(getCvh(), false, i, false, 4, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void showNotFoundDialog(final ControlViewHolder controlViewHolder, final ControlWithState controlWithState) {
        PackageManager packageManager = controlViewHolder.getContext().getPackageManager();
        CharSequence applicationLabel = packageManager.getApplicationLabel(packageManager.getApplicationInfo(controlWithState.getComponentName().getPackageName(), 128));
        final AlertDialog.Builder builder = new AlertDialog.Builder(controlViewHolder.getContext(), 16974545);
        Resources resources = controlViewHolder.getContext().getResources();
        builder.setTitle(resources.getString(R$string.controls_error_removed_title));
        builder.setMessage(resources.getString(R$string.controls_error_removed_message, controlViewHolder.getTitle().getText(), applicationLabel));
        builder.setPositiveButton(R$string.controls_open_app, new DialogInterface.OnClickListener() { // from class: com.android.systemui.controls.ui.StatusBehavior$showNotFoundDialog$builder$1$1
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                PendingIntent appIntent;
                try {
                    Control control = ControlWithState.this.getControl();
                    if (control != null && (appIntent = control.getAppIntent()) != null) {
                        appIntent.send();
                    }
                    builder.getContext().sendBroadcast(new Intent("android.intent.action.CLOSE_SYSTEM_DIALOGS"));
                } catch (PendingIntent.CanceledException unused) {
                    controlViewHolder.setErrorStatus();
                }
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton(17039360, StatusBehavior$showNotFoundDialog$builder$1$2.INSTANCE);
        AlertDialog create = builder.create();
        create.getWindow().setType(2020);
        create.show();
        Unit unit = Unit.INSTANCE;
        controlViewHolder.setVisibleDialog(create);
    }
}
