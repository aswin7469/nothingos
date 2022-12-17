package com.nothing.settings.face;

import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImeAwareEditText;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.Preference;
import com.android.settings.R$drawable;
import com.android.settings.R$id;
import com.android.settings.R$layout;
import com.android.settings.R$string;
import com.android.settings.widget.GearPreference;
import com.p007nt.facerecognition.manager.FaceMetadata;
import com.p007nt.facerecognition.manager.NtFaceRecognitionManager;

public class FaceGearPreference extends GearPreference {
    private Context mContext;
    private AlertDialog mDeleteDialog;
    /* access modifiers changed from: private */
    public IFaceEnroll mFaceEnroll;
    /* access modifiers changed from: private */
    public FaceMetadata mFaceMetadata;
    /* access modifiers changed from: private */
    public NtFaceRecognitionManager.RemovalCallback mRemovalCallback;
    /* access modifiers changed from: private */
    public DialogInterface.OnClickListener mRenameConfirmListener;
    /* access modifiers changed from: private */
    public AlertDialog mRenameDialog;
    /* access modifiers changed from: private */
    public boolean positiveClicked;

    public FaceGearPreference(Context context, FaceMetadata faceMetadata, NtFaceRecognitionManager.RemovalCallback removalCallback, DialogInterface.OnClickListener onClickListener) {
        super(context, (AttributeSet) null);
        this.mContext = context;
        this.mFaceMetadata = faceMetadata;
        this.mRemovalCallback = removalCallback;
        this.mRenameConfirmListener = onClickListener;
        this.mFaceEnroll = new FaceEnrollImpl(context);
        setTitle(faceMetadata.getName());
        setIcon(R$drawable.ic_face);
        setWidgetLayoutResource(R$layout.face_preference_widget_gear);
        setOnGearClickListener(new GearPreference.OnGearClickListener() {
            public final void onGearClick(GearPreference gearPreference) {
                FaceGearPreference.this.showDeleteDialog();
            }
        });
        setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public final boolean onPreferenceClick(Preference preference) {
                FaceGearPreference.this.showFingerprintNameDialog();
                return true;
            }
        });
    }

    /* access modifiers changed from: private */
    public void showDeleteDialog() {
        String str;
        int i;
        AlertDialog alertDialog = this.mDeleteDialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
            this.mDeleteDialog = null;
        }
        boolean z = this.mFaceEnroll.getIds().length <= 1;
        AlertDialog.Builder builder = new AlertDialog.Builder(this.mContext);
        if (z) {
            str = this.mContext.getString(R$string.nt_face_remove_only_one_title);
        } else {
            str = this.mContext.getString(R$string.nt_face_remove_title, new Object[]{this.mFaceMetadata.getName()});
        }
        builder.setTitle((CharSequence) str);
        if (z) {
            i = R$string.nt_face_remove_only_one_message;
        } else {
            i = R$string.nt_face_remove_message;
        }
        builder.setMessage(i);
        builder.setPositiveButton(R$string.fingerprint_last_delete_confirm, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public final void onClick(DialogInterface dialogInterface, int i) {
                FaceGearPreference.this.mFaceEnroll.removeFaceMetadata(FaceGearPreference.this.mFaceMetadata, FaceGearPreference.this.mRemovalCallback);
            }
        });
        builder.setNegativeButton(R$string.cancel, (DialogInterface.OnClickListener) null);
        AlertDialog create = builder.create();
        this.mDeleteDialog = create;
        create.show();
    }

    /* access modifiers changed from: private */
    public void showFingerprintNameDialog() {
        AlertDialog alertDialog = this.mRenameDialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
            this.mRenameDialog = null;
        }
        View inflate = View.inflate(this.mContext, R$layout.fingerprint_rename_dialog, (ViewGroup) null);
        final ImeAwareEditText findViewById = inflate.findViewById(R$id.fingerprint_rename_field);
        findViewById.setText(this.mFaceMetadata.getName());
        findViewById.setSingleLine(true);
        findViewById.setSelectAllOnFocus(true);
        findViewById.setInputType(524288);
        findViewById.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                ((Button) FaceGearPreference.this.mRenameDialog.findViewById(16908313)).setEnabled(!TextUtils.isEmpty(charSequence.toString().trim()));
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(this.mContext);
        builder.setPositiveButton(17039370, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                String obj = findViewById.getText().toString();
                FaceGearPreference.this.mFaceEnroll.rename(FaceGearPreference.this.mFaceMetadata.getFaceId(), FaceGearPreference.this.mFaceMetadata.getGroupId(), obj);
                FaceGearPreference.this.mFaceMetadata = new FaceMetadata(obj, FaceGearPreference.this.mFaceMetadata.getGroupId(), FaceGearPreference.this.mFaceMetadata.getFaceId(), FaceGearPreference.this.mFaceMetadata.getDeviceId());
                FaceGearPreference.this.positiveClicked = true;
            }
        });
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            public void onDismiss(DialogInterface dialogInterface) {
                if (FaceGearPreference.this.positiveClicked && FaceGearPreference.this.mRenameConfirmListener != null) {
                    FaceGearPreference.this.mRenameConfirmListener.onClick(dialogInterface, 0);
                }
                FaceGearPreference.this.positiveClicked = false;
            }
        });
        builder.setView(inflate);
        AlertDialog create = builder.create();
        create.setOnShowListener(new DialogInterface.OnShowListener() {
            public void onShow(DialogInterface dialogInterface) {
                findViewById.requestFocus();
            }
        });
        create.show();
        this.mRenameDialog = create;
    }
}
