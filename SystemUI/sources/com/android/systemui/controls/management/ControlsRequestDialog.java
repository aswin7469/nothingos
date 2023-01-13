package com.android.systemui.controls.management;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.service.controls.Control;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.launcher3.icons.cache.BaseIconCache;
import com.android.systemui.C1894R;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.controls.controller.ControlInfo;
import com.android.systemui.controls.controller.ControlsController;
import com.android.systemui.controls.p010ui.RenderInfo;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import com.android.systemui.util.LifecycleActivity;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000n\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\r\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005*\u0002\f\u0013\b\u0016\u0018\u0000 (2\u00020\u00012\u00020\u00022\u00020\u0003:\u0001(B\u001f\b\u0007\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t¢\u0006\u0002\u0010\nJ\u000e\u0010\u0017\u001a\u00020\u00162\u0006\u0010\u0018\u001a\u00020\u0019J\b\u0010\u001a\u001a\u00020\u001bH\u0002J\u0012\u0010\u001c\u001a\u00020\u001d2\b\u0010\u0015\u001a\u0004\u0018\u00010\u001eH\u0016J\u001a\u0010\u001f\u001a\u00020\u001d2\b\u0010\u0015\u001a\u0004\u0018\u00010\u001e2\u0006\u0010 \u001a\u00020!H\u0016J\u0012\u0010\"\u001a\u00020\u001d2\b\u0010#\u001a\u0004\u0018\u00010$H\u0014J\b\u0010%\u001a\u00020\u001dH\u0014J\b\u0010&\u001a\u00020\u001dH\u0014J\n\u0010'\u001a\u0004\u0018\u00010\u0019H\u0002R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u00020\fX\u0004¢\u0006\u0004\n\u0002\u0010\rR\u000e\u0010\u000e\u001a\u00020\u000fX.¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X.¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0012\u001a\u00020\u0013X\u0004¢\u0006\u0004\n\u0002\u0010\u0014R\u0010\u0010\u0015\u001a\u0004\u0018\u00010\u0016X\u000e¢\u0006\u0002\n\u0000¨\u0006)"}, mo65043d2 = {"Lcom/android/systemui/controls/management/ControlsRequestDialog;", "Lcom/android/systemui/util/LifecycleActivity;", "Landroid/content/DialogInterface$OnClickListener;", "Landroid/content/DialogInterface$OnCancelListener;", "controller", "Lcom/android/systemui/controls/controller/ControlsController;", "broadcastDispatcher", "Lcom/android/systemui/broadcast/BroadcastDispatcher;", "controlsListingController", "Lcom/android/systemui/controls/management/ControlsListingController;", "(Lcom/android/systemui/controls/controller/ControlsController;Lcom/android/systemui/broadcast/BroadcastDispatcher;Lcom/android/systemui/controls/management/ControlsListingController;)V", "callback", "com/android/systemui/controls/management/ControlsRequestDialog$callback$1", "Lcom/android/systemui/controls/management/ControlsRequestDialog$callback$1;", "control", "Landroid/service/controls/Control;", "controlComponent", "Landroid/content/ComponentName;", "currentUserTracker", "com/android/systemui/controls/management/ControlsRequestDialog$currentUserTracker$1", "Lcom/android/systemui/controls/management/ControlsRequestDialog$currentUserTracker$1;", "dialog", "Landroid/app/Dialog;", "createDialog", "label", "", "isCurrentFavorite", "", "onCancel", "", "Landroid/content/DialogInterface;", "onClick", "which", "", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onDestroy", "onResume", "verifyComponentAndGetLabel", "Companion", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ControlsRequestDialog.kt */
public class ControlsRequestDialog extends LifecycleActivity implements DialogInterface.OnClickListener, DialogInterface.OnCancelListener {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final String TAG = "ControlsRequestDialog";
    public Map<Integer, View> _$_findViewCache = new LinkedHashMap();
    private final BroadcastDispatcher broadcastDispatcher;
    private final ControlsRequestDialog$callback$1 callback;
    private Control control;
    private ComponentName controlComponent;
    /* access modifiers changed from: private */
    public final ControlsController controller;
    private final ControlsListingController controlsListingController;
    private final ControlsRequestDialog$currentUserTracker$1 currentUserTracker;
    private Dialog dialog;

    public void _$_clearFindViewByIdCache() {
        this._$_findViewCache.clear();
    }

    public View _$_findCachedViewById(int i) {
        Map<Integer, View> map = this._$_findViewCache;
        View view = map.get(Integer.valueOf(i));
        if (view != null) {
            return view;
        }
        View findViewById = findViewById(i);
        if (findViewById == null) {
            return null;
        }
        map.put(Integer.valueOf(i), findViewById);
        return findViewById;
    }

    @Inject
    public ControlsRequestDialog(ControlsController controlsController, BroadcastDispatcher broadcastDispatcher2, ControlsListingController controlsListingController2) {
        Intrinsics.checkNotNullParameter(controlsController, "controller");
        Intrinsics.checkNotNullParameter(broadcastDispatcher2, "broadcastDispatcher");
        Intrinsics.checkNotNullParameter(controlsListingController2, "controlsListingController");
        this.controller = controlsController;
        this.broadcastDispatcher = broadcastDispatcher2;
        this.controlsListingController = controlsListingController2;
        this.callback = new ControlsRequestDialog$callback$1();
        this.currentUserTracker = new ControlsRequestDialog$currentUserTracker$1(this, broadcastDispatcher2);
    }

    @Metadata(mo65042d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\u0005"}, mo65043d2 = {"Lcom/android/systemui/controls/management/ControlsRequestDialog$Companion;", "", "()V", "TAG", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: ControlsRequestDialog.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.currentUserTracker.startTracking();
        this.controlsListingController.addCallback(this.callback);
        int intExtra = getIntent().getIntExtra("android.intent.extra.USER_ID", -10000);
        int currentUserId = this.controller.getCurrentUserId();
        if (intExtra != currentUserId) {
            Log.w(TAG, "Current user (" + currentUserId + ") different from request user (" + intExtra + ')');
            finish();
        }
        ComponentName componentName = (ComponentName) getIntent().getParcelableExtra("android.intent.extra.COMPONENT_NAME");
        if (componentName == null) {
            ControlsRequestDialog controlsRequestDialog = this;
            Log.e(TAG, "Request did not contain componentName");
            finish();
            return;
        }
        this.controlComponent = componentName;
        Control parcelableExtra = getIntent().getParcelableExtra("android.service.controls.extra.CONTROL");
        if (parcelableExtra == null) {
            ControlsRequestDialog controlsRequestDialog2 = this;
            Log.e(TAG, "Request did not contain control");
            finish();
            return;
        }
        this.control = parcelableExtra;
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        CharSequence verifyComponentAndGetLabel = verifyComponentAndGetLabel();
        ComponentName componentName = null;
        if (verifyComponentAndGetLabel == null) {
            StringBuilder sb = new StringBuilder("The component specified (");
            ComponentName componentName2 = this.controlComponent;
            if (componentName2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("controlComponent");
            } else {
                componentName = componentName2;
            }
            Log.e(TAG, sb.append(componentName.flattenToString()).append(" is not a valid ControlsProviderService").toString());
            finish();
            return;
        }
        if (isCurrentFavorite()) {
            StringBuilder sb2 = new StringBuilder("The control ");
            ComponentName componentName3 = this.control;
            if (componentName3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("control");
            } else {
                componentName = componentName3;
            }
            Log.w(TAG, sb2.append((Object) componentName.getTitle()).append(" is already a favorite").toString());
            finish();
        }
        Dialog createDialog = createDialog(verifyComponentAndGetLabel);
        this.dialog = createDialog;
        if (createDialog != null) {
            createDialog.show();
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        Dialog dialog2 = this.dialog;
        if (dialog2 != null) {
            dialog2.dismiss();
        }
        this.currentUserTracker.stopTracking();
        this.controlsListingController.removeCallback(this.callback);
        super.onDestroy();
    }

    private final CharSequence verifyComponentAndGetLabel() {
        ControlsListingController controlsListingController2 = this.controlsListingController;
        ComponentName componentName = this.controlComponent;
        if (componentName == null) {
            Intrinsics.throwUninitializedPropertyAccessException("controlComponent");
            componentName = null;
        }
        return controlsListingController2.getAppLabel(componentName);
    }

    /* JADX WARNING: Removed duplicated region for block: B:27:0x0073 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final boolean isCurrentFavorite() {
        /*
            r7 = this;
            com.android.systemui.controls.controller.ControlsController r0 = r7.controller
            android.content.ComponentName r1 = r7.controlComponent
            r2 = 0
            if (r1 != 0) goto L_0x000d
            java.lang.String r1 = "controlComponent"
            kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException(r1)
            r1 = r2
        L_0x000d:
            java.util.List r0 = r0.getFavoritesForComponent(r1)
            java.lang.Iterable r0 = (java.lang.Iterable) r0
            boolean r1 = r0 instanceof java.util.Collection
            r3 = 0
            if (r1 == 0) goto L_0x0022
            r1 = r0
            java.util.Collection r1 = (java.util.Collection) r1
            boolean r1 = r1.isEmpty()
            if (r1 == 0) goto L_0x0022
            goto L_0x0074
        L_0x0022:
            java.util.Iterator r0 = r0.iterator()
        L_0x0026:
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L_0x0074
            java.lang.Object r1 = r0.next()
            com.android.systemui.controls.controller.StructureInfo r1 = (com.android.systemui.controls.controller.StructureInfo) r1
            java.util.List r1 = r1.getControls()
            java.lang.Iterable r1 = (java.lang.Iterable) r1
            boolean r4 = r1 instanceof java.util.Collection
            r5 = 1
            if (r4 == 0) goto L_0x0048
            r4 = r1
            java.util.Collection r4 = (java.util.Collection) r4
            boolean r4 = r4.isEmpty()
            if (r4 == 0) goto L_0x0048
        L_0x0046:
            r1 = r3
            goto L_0x0071
        L_0x0048:
            java.util.Iterator r1 = r1.iterator()
        L_0x004c:
            boolean r4 = r1.hasNext()
            if (r4 == 0) goto L_0x0046
            java.lang.Object r4 = r1.next()
            com.android.systemui.controls.controller.ControlInfo r4 = (com.android.systemui.controls.controller.ControlInfo) r4
            java.lang.String r4 = r4.getControlId()
            android.service.controls.Control r6 = r7.control
            if (r6 != 0) goto L_0x0066
            java.lang.String r6 = "control"
            kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException(r6)
            r6 = r2
        L_0x0066:
            java.lang.String r6 = r6.getControlId()
            boolean r4 = kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r4, (java.lang.Object) r6)
            if (r4 == 0) goto L_0x004c
            r1 = r5
        L_0x0071:
            if (r1 == 0) goto L_0x0026
            r3 = r5
        L_0x0074:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.controls.management.ControlsRequestDialog.isCurrentFavorite():boolean");
    }

    public final Dialog createDialog(CharSequence charSequence) {
        ComponentName componentName;
        Intrinsics.checkNotNullParameter(charSequence, BaseIconCache.IconDB.COLUMN_LABEL);
        RenderInfo.Companion companion = RenderInfo.Companion;
        Context context = this;
        ComponentName componentName2 = this.controlComponent;
        Control control2 = null;
        if (componentName2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("controlComponent");
            componentName = null;
        } else {
            componentName = componentName2;
        }
        Control control3 = this.control;
        if (control3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("control");
            control3 = null;
        }
        RenderInfo lookup$default = RenderInfo.Companion.lookup$default(companion, context, componentName, control3.getDeviceType(), 0, 8, (Object) null);
        View inflate = LayoutInflater.from(context).inflate(C1894R.layout.controls_dialog, (ViewGroup) null);
        ImageView imageView = (ImageView) inflate.requireViewById(C1894R.C1898id.icon);
        imageView.setImageDrawable(lookup$default.getIcon());
        imageView.setImageTintList(imageView.getContext().getResources().getColorStateList(lookup$default.getForeground(), imageView.getContext().getTheme()));
        TextView textView = (TextView) inflate.requireViewById(C1894R.C1898id.title);
        Control control4 = this.control;
        if (control4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("control");
            control4 = null;
        }
        textView.setText(control4.getTitle());
        TextView textView2 = (TextView) inflate.requireViewById(C1894R.C1898id.subtitle);
        Control control5 = this.control;
        if (control5 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("control");
        } else {
            control2 = control5;
        }
        textView2.setText(control2.getSubtitle());
        inflate.requireViewById(C1894R.C1898id.control).setElevation(inflate.getResources().getFloat(C1894R.dimen.control_card_elevation));
        AlertDialog.Builder message = new AlertDialog.Builder(context).setTitle(getString(C1894R.string.controls_dialog_title)).setMessage(getString(C1894R.string.controls_dialog_message, new Object[]{charSequence}));
        DialogInterface.OnClickListener onClickListener = this;
        AlertDialog create = message.setPositiveButton(C1894R.string.controls_dialog_ok, onClickListener).setNegativeButton(17039360, onClickListener).setOnCancelListener(this).setView(inflate).create();
        Dialog dialog2 = create;
        SystemUIDialog.registerDismissListener(dialog2);
        create.setCanceledOnTouchOutside(true);
        Intrinsics.checkNotNullExpressionValue(create, "dialog");
        return dialog2;
    }

    public void onCancel(DialogInterface dialogInterface) {
        finish();
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        if (i == -1) {
            ControlsController controlsController = this.controller;
            ComponentName componentName = this.controlComponent;
            Control control2 = null;
            if (componentName == null) {
                Intrinsics.throwUninitializedPropertyAccessException("controlComponent");
                componentName = null;
            }
            Control control3 = this.control;
            if (control3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("control");
                control3 = null;
            }
            CharSequence structure = control3.getStructure();
            if (structure == null) {
            }
            Control control4 = this.control;
            if (control4 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("control");
                control4 = null;
            }
            String controlId = control4.getControlId();
            Intrinsics.checkNotNullExpressionValue(controlId, "control.controlId");
            Control control5 = this.control;
            if (control5 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("control");
                control5 = null;
            }
            CharSequence title = control5.getTitle();
            Intrinsics.checkNotNullExpressionValue(title, "control.title");
            Control control6 = this.control;
            if (control6 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("control");
                control6 = null;
            }
            CharSequence subtitle = control6.getSubtitle();
            Intrinsics.checkNotNullExpressionValue(subtitle, "control.subtitle");
            Control control7 = this.control;
            if (control7 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("control");
            } else {
                control2 = control7;
            }
            controlsController.addFavorite(componentName, structure, new ControlInfo(controlId, title, subtitle, control2.getDeviceType()));
        }
        finish();
    }
}
