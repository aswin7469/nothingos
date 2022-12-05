package com.android.systemui.controls.management;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.os.Bundle;
import android.service.controls.Control;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.controls.ControlsServiceInfo;
import com.android.systemui.controls.controller.ControlInfo;
import com.android.systemui.controls.controller.ControlsController;
import com.android.systemui.controls.controller.StructureInfo;
import com.android.systemui.controls.management.ControlsListingController;
import com.android.systemui.controls.ui.RenderInfo;
import com.android.systemui.settings.CurrentUserTracker;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import com.android.systemui.util.LifecycleActivity;
import java.util.Collection;
import java.util.List;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: ControlsRequestDialog.kt */
/* loaded from: classes.dex */
public class ControlsRequestDialog extends LifecycleActivity implements DialogInterface.OnClickListener, DialogInterface.OnCancelListener {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private final BroadcastDispatcher broadcastDispatcher;
    @NotNull
    private final ControlsRequestDialog$callback$1 callback = new ControlsListingController.ControlsListingCallback() { // from class: com.android.systemui.controls.management.ControlsRequestDialog$callback$1
        @Override // com.android.systemui.controls.management.ControlsListingController.ControlsListingCallback
        public void onServicesUpdated(@NotNull List<ControlsServiceInfo> serviceInfos) {
            Intrinsics.checkNotNullParameter(serviceInfos, "serviceInfos");
        }
    };
    private Control control;
    private ComponentName controlComponent;
    @NotNull
    private final ControlsController controller;
    @NotNull
    private final ControlsListingController controlsListingController;
    @NotNull
    private final ControlsRequestDialog$currentUserTracker$1 currentUserTracker;
    @Nullable
    private Dialog dialog;

    /* JADX WARN: Type inference failed for: r2v1, types: [com.android.systemui.controls.management.ControlsRequestDialog$callback$1] */
    /* JADX WARN: Type inference failed for: r2v2, types: [com.android.systemui.controls.management.ControlsRequestDialog$currentUserTracker$1] */
    public ControlsRequestDialog(@NotNull ControlsController controller, @NotNull final BroadcastDispatcher broadcastDispatcher, @NotNull ControlsListingController controlsListingController) {
        Intrinsics.checkNotNullParameter(controller, "controller");
        Intrinsics.checkNotNullParameter(broadcastDispatcher, "broadcastDispatcher");
        Intrinsics.checkNotNullParameter(controlsListingController, "controlsListingController");
        this.controller = controller;
        this.broadcastDispatcher = broadcastDispatcher;
        this.controlsListingController = controlsListingController;
        this.currentUserTracker = new CurrentUserTracker(broadcastDispatcher) { // from class: com.android.systemui.controls.management.ControlsRequestDialog$currentUserTracker$1
            private final int startingUser;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                ControlsController controlsController;
                controlsController = ControlsRequestDialog.this.controller;
                this.startingUser = controlsController.getCurrentUserId();
            }

            @Override // com.android.systemui.settings.CurrentUserTracker
            public void onUserSwitched(int i) {
                if (i != this.startingUser) {
                    stopTracking();
                    ControlsRequestDialog.this.finish();
                }
            }
        };
    }

    /* compiled from: ControlsRequestDialog.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    @Override // com.android.systemui.util.LifecycleActivity, android.app.Activity
    protected void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        startTracking();
        this.controlsListingController.addCallback(this.callback);
        int intExtra = getIntent().getIntExtra("android.intent.extra.USER_ID", -10000);
        int currentUserId = this.controller.getCurrentUserId();
        if (intExtra != currentUserId) {
            Log.w("ControlsRequestDialog", "Current user (" + currentUserId + ") different from request user (" + intExtra + ')');
            finish();
        }
        ComponentName componentName = (ComponentName) getIntent().getParcelableExtra("android.intent.extra.COMPONENT_NAME");
        if (componentName != null) {
            this.controlComponent = componentName;
            Control control = (Control) getIntent().getParcelableExtra("android.service.controls.extra.CONTROL");
            if (control != null) {
                this.control = control;
                return;
            }
            Log.e("ControlsRequestDialog", "Request did not contain control");
            finish();
            return;
        }
        Log.e("ControlsRequestDialog", "Request did not contain componentName");
        finish();
    }

    @Override // com.android.systemui.util.LifecycleActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        CharSequence verifyComponentAndGetLabel = verifyComponentAndGetLabel();
        if (verifyComponentAndGetLabel == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("The component specified (");
            ComponentName componentName = this.controlComponent;
            if (componentName == null) {
                Intrinsics.throwUninitializedPropertyAccessException("controlComponent");
                throw null;
            }
            sb.append((Object) componentName.flattenToString());
            sb.append(" is not a valid ControlsProviderService");
            Log.e("ControlsRequestDialog", sb.toString());
            finish();
            return;
        }
        if (isCurrentFavorite()) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("The control ");
            Control control = this.control;
            if (control == null) {
                Intrinsics.throwUninitializedPropertyAccessException("control");
                throw null;
            }
            sb2.append((Object) control.getTitle());
            sb2.append(" is already a favorite");
            Log.w("ControlsRequestDialog", sb2.toString());
            finish();
        }
        Dialog createDialog = createDialog(verifyComponentAndGetLabel);
        this.dialog = createDialog;
        if (createDialog == null) {
            return;
        }
        createDialog.show();
    }

    @Override // com.android.systemui.util.LifecycleActivity, android.app.Activity
    protected void onDestroy() {
        Dialog dialog = this.dialog;
        if (dialog != null) {
            dialog.dismiss();
        }
        stopTracking();
        this.controlsListingController.removeCallback(this.callback);
        super.onDestroy();
    }

    private final CharSequence verifyComponentAndGetLabel() {
        ControlsListingController controlsListingController = this.controlsListingController;
        ComponentName componentName = this.controlComponent;
        if (componentName != null) {
            return controlsListingController.getAppLabel(componentName);
        }
        Intrinsics.throwUninitializedPropertyAccessException("controlComponent");
        throw null;
    }

    private final boolean isCurrentFavorite() {
        boolean z;
        ControlsController controlsController = this.controller;
        ComponentName componentName = this.controlComponent;
        if (componentName == null) {
            Intrinsics.throwUninitializedPropertyAccessException("controlComponent");
            throw null;
        }
        List<StructureInfo> favoritesForComponent = controlsController.getFavoritesForComponent(componentName);
        if (!(favoritesForComponent instanceof Collection) || !favoritesForComponent.isEmpty()) {
            for (StructureInfo structureInfo : favoritesForComponent) {
                List<ControlInfo> controls = structureInfo.getControls();
                if (!(controls instanceof Collection) || !controls.isEmpty()) {
                    for (ControlInfo controlInfo : controls) {
                        String controlId = controlInfo.getControlId();
                        Control control = this.control;
                        if (control != null) {
                            if (Intrinsics.areEqual(controlId, control.getControlId())) {
                                z = true;
                                continue;
                                break;
                            }
                        } else {
                            Intrinsics.throwUninitializedPropertyAccessException("control");
                            throw null;
                        }
                    }
                }
                z = false;
                continue;
                if (z) {
                    return true;
                }
            }
        }
        return false;
    }

    @NotNull
    public final Dialog createDialog(@NotNull CharSequence label) {
        Intrinsics.checkNotNullParameter(label, "label");
        RenderInfo.Companion companion = RenderInfo.Companion;
        ComponentName componentName = this.controlComponent;
        if (componentName == null) {
            Intrinsics.throwUninitializedPropertyAccessException("controlComponent");
            throw null;
        }
        Control control = this.control;
        if (control == null) {
            Intrinsics.throwUninitializedPropertyAccessException("control");
            throw null;
        }
        RenderInfo lookup$default = RenderInfo.Companion.lookup$default(companion, this, componentName, control.getDeviceType(), 0, 8, null);
        View inflate = LayoutInflater.from(this).inflate(R$layout.controls_dialog, (ViewGroup) null);
        ImageView imageView = (ImageView) inflate.requireViewById(R$id.icon);
        imageView.setImageDrawable(lookup$default.getIcon());
        imageView.setImageTintList(imageView.getContext().getResources().getColorStateList(lookup$default.getForeground(), imageView.getContext().getTheme()));
        TextView textView = (TextView) inflate.requireViewById(R$id.title);
        Control control2 = this.control;
        if (control2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("control");
            throw null;
        }
        textView.setText(control2.getTitle());
        TextView textView2 = (TextView) inflate.requireViewById(R$id.subtitle);
        Control control3 = this.control;
        if (control3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("control");
            throw null;
        }
        textView2.setText(control3.getSubtitle());
        inflate.requireViewById(R$id.control).setElevation(inflate.getResources().getFloat(R$dimen.control_card_elevation));
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle(getString(R$string.controls_dialog_title)).setMessage(getString(R$string.controls_dialog_message, new Object[]{label})).setPositiveButton(R$string.controls_dialog_ok, this).setNegativeButton(17039360, this).setOnCancelListener(this).setView(inflate).create();
        SystemUIDialog.registerDismissListener(dialog);
        dialog.setCanceledOnTouchOutside(true);
        Intrinsics.checkNotNullExpressionValue(dialog, "dialog");
        return dialog;
    }

    @Override // android.content.DialogInterface.OnCancelListener
    public void onCancel(@Nullable DialogInterface dialogInterface) {
        finish();
    }

    @Override // android.content.DialogInterface.OnClickListener
    public void onClick(@Nullable DialogInterface dialogInterface, int i) {
        if (i == -1) {
            ControlsController controlsController = this.controller;
            ComponentName componentName = this.controlComponent;
            if (componentName == null) {
                Intrinsics.throwUninitializedPropertyAccessException("controlComponent");
                throw null;
            }
            Control control = this.control;
            if (control == null) {
                Intrinsics.throwUninitializedPropertyAccessException("control");
                throw null;
            }
            CharSequence structure = control.getStructure();
            if (structure == null) {
                structure = "";
            }
            Control control2 = this.control;
            if (control2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("control");
                throw null;
            }
            String controlId = control2.getControlId();
            Intrinsics.checkNotNullExpressionValue(controlId, "control.controlId");
            Control control3 = this.control;
            if (control3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("control");
                throw null;
            }
            CharSequence title = control3.getTitle();
            Intrinsics.checkNotNullExpressionValue(title, "control.title");
            Control control4 = this.control;
            if (control4 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("control");
                throw null;
            }
            CharSequence subtitle = control4.getSubtitle();
            Intrinsics.checkNotNullExpressionValue(subtitle, "control.subtitle");
            Control control5 = this.control;
            if (control5 != null) {
                controlsController.addFavorite(componentName, structure, new ControlInfo(controlId, title, subtitle, control5.getDeviceType()));
            } else {
                Intrinsics.throwUninitializedPropertyAccessException("control");
                throw null;
            }
        }
        finish();
    }
}
