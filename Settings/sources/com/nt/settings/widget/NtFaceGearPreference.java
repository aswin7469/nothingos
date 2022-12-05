package com.nt.settings.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImeAwareEditText;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.Preference;
import com.android.settings.R;
import com.android.settings.widget.GearPreference;
import com.nt.facerecognition.manager.FaceMetadata;
import com.nt.facerecognition.manager.NtFaceRecognitionManager;
import com.nt.settings.face.NtFaceRecognitionImpl;
import com.nt.settings.face.NtIFaceRecognition;
/* loaded from: classes2.dex */
public class NtFaceGearPreference extends GearPreference {
    private Context mContext;
    private AlertDialog mDeleteDialog;
    private FaceMetadata mFaceMetadata;
    private NtIFaceRecognition mNtIFaceRecognition;
    private NtFaceRecognitionManager.RemovalCallback mRemovalCallback;
    private DialogInterface.OnClickListener mRenameConfirmListener;
    private AlertDialog mRenameDialog;
    private boolean positiveClicked;

    public NtFaceGearPreference(Context context, FaceMetadata faceMetadata, NtFaceRecognitionManager.RemovalCallback removalCallback, DialogInterface.OnClickListener onClickListener) {
        super(context, null);
        this.mContext = context;
        this.mFaceMetadata = faceMetadata;
        this.mRemovalCallback = removalCallback;
        this.mRenameConfirmListener = onClickListener;
        this.mNtIFaceRecognition = new NtFaceRecognitionImpl(context);
        setTitle(faceMetadata.getName());
        setIcon(R.drawable.ic_face);
        setWidgetLayoutResource(R.layout.face_preference_widget_gear);
        setOnGearClickListener(new GearPreference.OnGearClickListener() { // from class: com.nt.settings.widget.NtFaceGearPreference$$ExternalSyntheticLambda2
            @Override // com.android.settings.widget.GearPreference.OnGearClickListener
            public final void onGearClick(GearPreference gearPreference) {
                NtFaceGearPreference.this.lambda$new$0(gearPreference);
            }
        });
        setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: com.nt.settings.widget.NtFaceGearPreference$$ExternalSyntheticLambda1
            @Override // androidx.preference.Preference.OnPreferenceClickListener
            public final boolean onPreferenceClick(Preference preference) {
                boolean lambda$new$1;
                lambda$new$1 = NtFaceGearPreference.this.lambda$new$1(preference);
                return lambda$new$1;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(GearPreference gearPreference) {
        showDeleteDialog();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$new$1(Preference preference) {
        showFingerprintNameDialog();
        return true;
    }

    private void showDeleteDialog() {
        String string;
        AlertDialog alertDialog = this.mDeleteDialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
            this.mDeleteDialog = null;
        }
        boolean z = this.mNtIFaceRecognition.getIds().length <= 1;
        AlertDialog.Builder builder = new AlertDialog.Builder(this.mContext);
        if (z) {
            string = this.mContext.getString(R.string.nt_face_remove_only_one_title);
        } else {
            string = this.mContext.getString(R.string.nt_face_remove_title, this.mFaceMetadata.getName());
        }
        builder.setTitle(string);
        builder.setMessage(z ? R.string.nt_face_remove_only_one_message : R.string.nt_face_remove_message);
        builder.setPositiveButton(R.string.fingerprint_last_delete_confirm, new DialogInterface.OnClickListener() { // from class: com.nt.settings.widget.NtFaceGearPreference$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                NtFaceGearPreference.this.lambda$showDeleteDialog$2(dialogInterface, i);
            }
        });
        builder.setNegativeButton(R.string.cancel, (DialogInterface.OnClickListener) null);
        AlertDialog create = builder.create();
        this.mDeleteDialog = create;
        create.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showDeleteDialog$2(DialogInterface dialogInterface, int i) {
        this.mNtIFaceRecognition.removeFaceMetadata(this.mFaceMetadata, this.mRemovalCallback);
    }

    private void showFingerprintNameDialog() {
        AlertDialog alertDialog = this.mRenameDialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
            this.mRenameDialog = null;
        }
        View inflate = View.inflate(this.mContext, R.layout.fingerprint_rename_dialog, null);
        final ImeAwareEditText findViewById = inflate.findViewById(R.id.fingerprint_rename_field);
        findViewById.setText(this.mFaceMetadata.getName());
        findViewById.setSingleLine(true);
        findViewById.setSelectAllOnFocus(true);
        findViewById.setInputType(524288);
        findViewById.addTextChangedListener(new TextWatcher() { // from class: com.nt.settings.widget.NtFaceGearPreference.1
            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
            }

            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                ((Button) NtFaceGearPreference.this.mRenameDialog.findViewById(16908313)).setEnabled(!TextUtils.isEmpty(charSequence.toString().trim()));
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(this.mContext);
        builder.setPositiveButton(17039370, new DialogInterface.OnClickListener() { // from class: com.nt.settings.widget.NtFaceGearPreference.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                String obj = findViewById.getText().toString();
                NtFaceGearPreference.this.mNtIFaceRecognition.rename(NtFaceGearPreference.this.mFaceMetadata.getFaceId(), NtFaceGearPreference.this.mFaceMetadata.getGroupId(), obj);
                NtFaceGearPreference.this.mFaceMetadata = new FaceMetadata(obj, NtFaceGearPreference.this.mFaceMetadata.getGroupId(), NtFaceGearPreference.this.mFaceMetadata.getFaceId(), NtFaceGearPreference.this.mFaceMetadata.getDeviceId());
                NtFaceGearPreference.this.positiveClicked = true;
            }
        });
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.nt.settings.widget.NtFaceGearPreference.3
            @Override // android.content.DialogInterface.OnDismissListener
            public void onDismiss(DialogInterface dialogInterface) {
                if (NtFaceGearPreference.this.positiveClicked && NtFaceGearPreference.this.mRenameConfirmListener != null) {
                    NtFaceGearPreference.this.mRenameConfirmListener.onClick(dialogInterface, 0);
                }
                NtFaceGearPreference.this.positiveClicked = false;
            }
        });
        builder.setView(inflate);
        AlertDialog create = builder.create();
        create.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.nt.settings.widget.NtFaceGearPreference.4
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface dialogInterface) {
                findViewById.requestFocus();
            }
        });
        create.show();
        this.mRenameDialog = create;
    }
}
