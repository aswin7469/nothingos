package com.android.systemui.controls.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import kotlin.Pair;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: ChallengeDialogs.kt */
/* loaded from: classes.dex */
public final class ChallengeDialogs {
    @NotNull
    public static final ChallengeDialogs INSTANCE = new ChallengeDialogs();

    private ChallengeDialogs() {
    }

    /* JADX WARN: Type inference failed for: r3v2, types: [android.app.AlertDialog, android.app.Dialog, com.android.systemui.controls.ui.ChallengeDialogs$createPinDialog$1] */
    @Nullable
    public final Dialog createPinDialog(@NotNull final ControlViewHolder cvh, final boolean z, boolean z2, @NotNull final Function0<Unit> onCancel) {
        Pair pair;
        Intrinsics.checkNotNullParameter(cvh, "cvh");
        Intrinsics.checkNotNullParameter(onCancel, "onCancel");
        final ControlAction lastAction = cvh.getLastAction();
        if (lastAction == null) {
            Log.e("ControlsUiController", "PIN Dialog attempted but no last action is set. Will not show");
            return null;
        }
        Resources resources = cvh.getContext().getResources();
        if (z2) {
            pair = new Pair(resources.getString(R$string.controls_pin_wrong), Integer.valueOf(R$string.controls_pin_instructions_retry));
        } else {
            pair = new Pair(resources.getString(R$string.controls_pin_verify, cvh.getTitle().getText()), Integer.valueOf(R$string.controls_pin_instructions));
        }
        final int intValue = ((Number) pair.component2()).intValue();
        final Context context = cvh.getContext();
        final ?? r3 = new AlertDialog(context) { // from class: com.android.systemui.controls.ui.ChallengeDialogs$createPinDialog$1
            @Override // android.app.Dialog, android.content.DialogInterface
            public void dismiss() {
                View decorView;
                InputMethodManager inputMethodManager;
                Window window = getWindow();
                if (window != null && (decorView = window.getDecorView()) != null && (inputMethodManager = (InputMethodManager) decorView.getContext().getSystemService(InputMethodManager.class)) != null) {
                    inputMethodManager.hideSoftInputFromWindow(decorView.getWindowToken(), 0);
                }
                super.dismiss();
            }
        };
        r3.setTitle((String) pair.component1());
        r3.setView(LayoutInflater.from(r3.getContext()).inflate(R$layout.controls_dialog_pin, (ViewGroup) null));
        r3.setButton(-1, r3.getContext().getText(17039370), new DialogInterface.OnClickListener() { // from class: com.android.systemui.controls.ui.ChallengeDialogs$createPinDialog$2$1
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                ControlAction addChallengeValue;
                if (dialogInterface instanceof Dialog) {
                    Dialog dialog = (Dialog) dialogInterface;
                    int i2 = R$id.controls_pin_input;
                    dialog.requireViewById(i2);
                    String obj = ((EditText) dialog.requireViewById(i2)).getText().toString();
                    ControlViewHolder controlViewHolder = ControlViewHolder.this;
                    addChallengeValue = ChallengeDialogs.INSTANCE.addChallengeValue(lastAction, obj);
                    controlViewHolder.action(addChallengeValue);
                    dialogInterface.dismiss();
                }
            }
        });
        r3.setButton(-2, r3.getContext().getText(17039360), new DialogInterface.OnClickListener() { // from class: com.android.systemui.controls.ui.ChallengeDialogs$createPinDialog$2$2
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                onCancel.mo1951invoke();
                dialogInterface.cancel();
            }
        });
        Window window = r3.getWindow();
        window.setType(2020);
        window.setSoftInputMode(4);
        r3.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.android.systemui.controls.ui.ChallengeDialogs$createPinDialog$2$4
            @Override // android.content.DialogInterface.OnShowListener
            public final void onShow(DialogInterface dialogInterface) {
                final EditText editText = (EditText) requireViewById(R$id.controls_pin_input);
                editText.setHint(intValue);
                ChallengeDialogs$createPinDialog$1 challengeDialogs$createPinDialog$1 = ChallengeDialogs$createPinDialog$1.this;
                int i = R$id.controls_pin_use_alpha;
                final CheckBox checkBox = (CheckBox) challengeDialogs$createPinDialog$1.requireViewById(i);
                checkBox.setChecked(z);
                ChallengeDialogs challengeDialogs = ChallengeDialogs.INSTANCE;
                Intrinsics.checkNotNullExpressionValue(editText, "editText");
                challengeDialogs.setInputType(editText, checkBox.isChecked());
                ((CheckBox) requireViewById(i)).setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.controls.ui.ChallengeDialogs$createPinDialog$2$4.1
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        ChallengeDialogs challengeDialogs2 = ChallengeDialogs.INSTANCE;
                        EditText editText2 = editText;
                        Intrinsics.checkNotNullExpressionValue(editText2, "editText");
                        challengeDialogs2.setInputType(editText2, checkBox.isChecked());
                    }
                });
                editText.requestFocus();
            }
        });
        return r3;
    }

    @Nullable
    public final Dialog createConfirmationDialog(@NotNull final ControlViewHolder cvh, @NotNull final Function0<Unit> onCancel) {
        Intrinsics.checkNotNullParameter(cvh, "cvh");
        Intrinsics.checkNotNullParameter(onCancel, "onCancel");
        final ControlAction lastAction = cvh.getLastAction();
        if (lastAction == null) {
            Log.e("ControlsUiController", "Confirmation Dialog attempted but no last action is set. Will not show");
            return null;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(cvh.getContext(), 16974545);
        builder.setTitle(cvh.getContext().getResources().getString(R$string.controls_confirmation_message, cvh.getTitle().getText()));
        builder.setPositiveButton(17039370, new DialogInterface.OnClickListener() { // from class: com.android.systemui.controls.ui.ChallengeDialogs$createConfirmationDialog$builder$1$1
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                ControlAction addChallengeValue;
                ControlViewHolder controlViewHolder = ControlViewHolder.this;
                addChallengeValue = ChallengeDialogs.INSTANCE.addChallengeValue(lastAction, "true");
                controlViewHolder.action(addChallengeValue);
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton(17039360, new DialogInterface.OnClickListener() { // from class: com.android.systemui.controls.ui.ChallengeDialogs$createConfirmationDialog$builder$1$2
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                onCancel.mo1951invoke();
                dialogInterface.cancel();
            }
        });
        AlertDialog create = builder.create();
        create.getWindow().setType(2020);
        return create;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void setInputType(EditText editText, boolean z) {
        if (z) {
            editText.setInputType(129);
        } else {
            editText.setInputType(18);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final ControlAction addChallengeValue(ControlAction controlAction, String str) {
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
        if (!(controlAction instanceof ModeAction)) {
            throw new IllegalStateException(Intrinsics.stringPlus("'action' is not a known type: ", controlAction));
        }
        return new ModeAction(templateId, ((ModeAction) controlAction).getNewMode(), str);
    }
}
