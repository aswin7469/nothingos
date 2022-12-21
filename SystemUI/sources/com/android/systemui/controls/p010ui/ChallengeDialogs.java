package com.android.systemui.controls.p010ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.service.controls.actions.BooleanAction;
import android.service.controls.actions.CommandAction;
import android.service.controls.actions.ControlAction;
import android.service.controls.actions.FloatAction;
import android.service.controls.actions.ModeAction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import com.android.systemui.C1893R;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0018\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u00072\u0006\u0010\t\u001a\u00020\nH\u0002J\u001e\u0010\u000b\u001a\u0004\u0018\u00010\f2\u0006\u0010\r\u001a\u00020\u000e2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00110\u0010J.\u0010\u0012\u001a\u0004\u0018\u00010\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00142\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00110\u0010J\u0018\u0010\u0016\u001a\u00020\u00112\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u0014H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\u001a"}, mo64987d2 = {"Lcom/android/systemui/controls/ui/ChallengeDialogs;", "", "()V", "STYLE", "", "WINDOW_TYPE", "addChallengeValue", "Landroid/service/controls/actions/ControlAction;", "action", "challengeValue", "", "createConfirmationDialog", "Landroid/app/Dialog;", "cvh", "Lcom/android/systemui/controls/ui/ControlViewHolder;", "onCancel", "Lkotlin/Function0;", "", "createPinDialog", "useAlphaNumeric", "", "useRetryStrings", "setInputType", "editText", "Landroid/widget/EditText;", "useTextInput", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.systemui.controls.ui.ChallengeDialogs */
/* compiled from: ChallengeDialogs.kt */
public final class ChallengeDialogs {
    public static final ChallengeDialogs INSTANCE = new ChallengeDialogs();
    private static final int STYLE = 16974545;
    private static final int WINDOW_TYPE = 2020;

    private ChallengeDialogs() {
    }

    public final Dialog createPinDialog(ControlViewHolder controlViewHolder, boolean z, boolean z2, Function0<Unit> function0) {
        Pair pair;
        Intrinsics.checkNotNullParameter(controlViewHolder, "cvh");
        Intrinsics.checkNotNullParameter(function0, "onCancel");
        ControlAction lastAction = controlViewHolder.getLastAction();
        if (lastAction == null) {
            Log.e("ControlsUiController", "PIN Dialog attempted but no last action is set. Will not show");
            return null;
        }
        Resources resources = controlViewHolder.getContext().getResources();
        if (z2) {
            pair = new Pair(resources.getString(C1893R.string.controls_pin_wrong), Integer.valueOf((int) C1893R.string.controls_pin_instructions_retry));
        } else {
            pair = new Pair(resources.getString(C1893R.string.controls_pin_verify, new Object[]{controlViewHolder.getTitle().getText()}), Integer.valueOf((int) C1893R.string.controls_pin_instructions));
        }
        int intValue = ((Number) pair.component2()).intValue();
        ChallengeDialogs$createPinDialog$1 challengeDialogs$createPinDialog$1 = new ChallengeDialogs$createPinDialog$1(controlViewHolder.getContext());
        challengeDialogs$createPinDialog$1.setTitle((String) pair.component1());
        challengeDialogs$createPinDialog$1.setView(LayoutInflater.from(challengeDialogs$createPinDialog$1.getContext()).inflate(C1893R.layout.controls_dialog_pin, (ViewGroup) null));
        challengeDialogs$createPinDialog$1.setButton(-1, challengeDialogs$createPinDialog$1.getContext().getText(17039370), new ChallengeDialogs$$ExternalSyntheticLambda3(controlViewHolder, lastAction));
        challengeDialogs$createPinDialog$1.setButton(-2, challengeDialogs$createPinDialog$1.getContext().getText(17039360), new ChallengeDialogs$$ExternalSyntheticLambda4(function0));
        Window window = challengeDialogs$createPinDialog$1.getWindow();
        window.setType(WINDOW_TYPE);
        window.setSoftInputMode(4);
        challengeDialogs$createPinDialog$1.setOnShowListener(new ChallengeDialogs$$ExternalSyntheticLambda5(challengeDialogs$createPinDialog$1, intValue, z));
        return challengeDialogs$createPinDialog$1;
    }

    /* access modifiers changed from: private */
    /* renamed from: createPinDialog$lambda-5$lambda-0  reason: not valid java name */
    public static final void m2666createPinDialog$lambda5$lambda0(ControlViewHolder controlViewHolder, ControlAction controlAction, DialogInterface dialogInterface, int i) {
        Intrinsics.checkNotNullParameter(controlViewHolder, "$cvh");
        if (dialogInterface instanceof Dialog) {
            Dialog dialog = (Dialog) dialogInterface;
            dialog.requireViewById(C1893R.C1897id.controls_pin_input);
            controlViewHolder.action(INSTANCE.addChallengeValue(controlAction, ((EditText) dialog.requireViewById(C1893R.C1897id.controls_pin_input)).getText().toString()));
            dialogInterface.dismiss();
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: createPinDialog$lambda-5$lambda-1  reason: not valid java name */
    public static final void m2667createPinDialog$lambda5$lambda1(Function0 function0, DialogInterface dialogInterface, int i) {
        Intrinsics.checkNotNullParameter(function0, "$onCancel");
        function0.invoke();
        dialogInterface.cancel();
    }

    /* access modifiers changed from: private */
    /* renamed from: createPinDialog$lambda-5$lambda-4  reason: not valid java name */
    public static final void m2668createPinDialog$lambda5$lambda4(ChallengeDialogs$createPinDialog$1 challengeDialogs$createPinDialog$1, int i, boolean z, DialogInterface dialogInterface) {
        Intrinsics.checkNotNullParameter(challengeDialogs$createPinDialog$1, "$this_apply");
        EditText editText = (EditText) challengeDialogs$createPinDialog$1.requireViewById(C1893R.C1897id.controls_pin_input);
        editText.setHint(i);
        CheckBox checkBox = (CheckBox) challengeDialogs$createPinDialog$1.requireViewById(C1893R.C1897id.controls_pin_use_alpha);
        checkBox.setChecked(z);
        ChallengeDialogs challengeDialogs = INSTANCE;
        Intrinsics.checkNotNullExpressionValue(editText, "editText");
        challengeDialogs.setInputType(editText, checkBox.isChecked());
        ((CheckBox) challengeDialogs$createPinDialog$1.requireViewById(C1893R.C1897id.controls_pin_use_alpha)).setOnClickListener(new ChallengeDialogs$$ExternalSyntheticLambda2(editText, checkBox));
        editText.requestFocus();
    }

    /* access modifiers changed from: private */
    /* renamed from: createPinDialog$lambda-5$lambda-4$lambda-3  reason: not valid java name */
    public static final void m2669createPinDialog$lambda5$lambda4$lambda3(EditText editText, CheckBox checkBox, View view) {
        ChallengeDialogs challengeDialogs = INSTANCE;
        Intrinsics.checkNotNullExpressionValue(editText, "editText");
        challengeDialogs.setInputType(editText, checkBox.isChecked());
    }

    public final Dialog createConfirmationDialog(ControlViewHolder controlViewHolder, Function0<Unit> function0) {
        Intrinsics.checkNotNullParameter(controlViewHolder, "cvh");
        Intrinsics.checkNotNullParameter(function0, "onCancel");
        ControlAction lastAction = controlViewHolder.getLastAction();
        if (lastAction == null) {
            Log.e("ControlsUiController", "Confirmation Dialog attempted but no last action is set. Will not show");
            return null;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(controlViewHolder.getContext(), STYLE);
        builder.setTitle(controlViewHolder.getContext().getResources().getString(C1893R.string.controls_confirmation_message, new Object[]{controlViewHolder.getTitle().getText()}));
        builder.setPositiveButton(17039370, new ChallengeDialogs$$ExternalSyntheticLambda0(controlViewHolder, lastAction));
        builder.setNegativeButton(17039360, new ChallengeDialogs$$ExternalSyntheticLambda1(function0));
        AlertDialog create = builder.create();
        create.getWindow().setType(WINDOW_TYPE);
        return create;
    }

    /* access modifiers changed from: private */
    /* renamed from: createConfirmationDialog$lambda-8$lambda-6  reason: not valid java name */
    public static final void m2664createConfirmationDialog$lambda8$lambda6(ControlViewHolder controlViewHolder, ControlAction controlAction, DialogInterface dialogInterface, int i) {
        Intrinsics.checkNotNullParameter(controlViewHolder, "$cvh");
        controlViewHolder.action(INSTANCE.addChallengeValue(controlAction, "true"));
        dialogInterface.dismiss();
    }

    /* access modifiers changed from: private */
    /* renamed from: createConfirmationDialog$lambda-8$lambda-7  reason: not valid java name */
    public static final void m2665createConfirmationDialog$lambda8$lambda7(Function0 function0, DialogInterface dialogInterface, int i) {
        Intrinsics.checkNotNullParameter(function0, "$onCancel");
        function0.invoke();
        dialogInterface.cancel();
    }

    private final void setInputType(EditText editText, boolean z) {
        if (z) {
            editText.setInputType(129);
        } else {
            editText.setInputType(18);
        }
    }

    private final ControlAction addChallengeValue(ControlAction controlAction, String str) {
        String templateId = controlAction.getTemplateId();
        if (controlAction instanceof BooleanAction) {
            return new BooleanAction(templateId, ((BooleanAction) controlAction).getNewState(), str);
        }
        if (controlAction instanceof FloatAction) {
            return new FloatAction(templateId, ((FloatAction) controlAction).getNewValue(), str);
        }
        if (controlAction instanceof CommandAction) {
            return new CommandAction(templateId, str);
        }
        if (controlAction instanceof ModeAction) {
            return new ModeAction(templateId, ((ModeAction) controlAction).getNewMode(), str);
        }
        throw new IllegalStateException("'action' is not a known type: " + controlAction);
    }
}
