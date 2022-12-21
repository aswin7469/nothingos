package com.android.systemui.media.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.graphics.drawable.IconCompat;
import com.android.settingslib.qrcode.QrCodeGenerator;
import com.android.systemui.C1893R;
import com.android.systemui.broadcast.BroadcastSender;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.media.dialog.MediaOutputController;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import com.google.zxing.WriterException;
import java.sql.Types;

@SysUISingleton
public class MediaOutputBroadcastDialog extends MediaOutputBaseDialog {
    private static final int MAX_BROADCAST_INFO_UPDATE = 3;
    static final int METADATA_BROADCAST_CODE = 1;
    static final int METADATA_BROADCAST_NAME = 0;
    private static final String TAG = "BroadcastDialog";
    private AlertDialog mAlertDialog;
    private TextView mBroadcastCode;
    private ImageView mBroadcastCodeEdit;
    private ImageView mBroadcastCodeEye;
    private TextView mBroadcastErrorMessage;
    private ViewStub mBroadcastInfoArea;
    private TextView mBroadcastName;
    private ImageView mBroadcastNameEdit;
    private ImageView mBroadcastNotify;
    private ImageView mBroadcastQrCodeView;
    private String mCurrentBroadcastCode;
    private String mCurrentBroadcastName;
    private Boolean mIsPasswordHide = true;
    private boolean mIsStopbyUpdateBroadcastCode = false;
    private int mRetryCount = 0;

    /* access modifiers changed from: package-private */
    public int getHeaderIconRes() {
        return 0;
    }

    /* access modifiers changed from: package-private */
    public int getStopButtonVisibility() {
        return 0;
    }

    MediaOutputBroadcastDialog(Context context, boolean z, BroadcastSender broadcastSender, MediaOutputController mediaOutputController) {
        super(context, broadcastSender, mediaOutputController);
        this.mAdapter = new MediaOutputGroupAdapter(this.mMediaOutputController);
        if (!z) {
            getWindow().setType(2038);
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        initBtQrCodeUI();
    }

    public void onStart() {
        super.onStart();
        refreshUi();
    }

    /* access modifiers changed from: package-private */
    public IconCompat getHeaderIcon() {
        return this.mMediaOutputController.getHeaderIcon();
    }

    /* access modifiers changed from: package-private */
    public int getHeaderIconSize() {
        return this.mContext.getResources().getDimensionPixelSize(C1893R.dimen.media_output_dialog_header_album_icon_size);
    }

    /* access modifiers changed from: package-private */
    public CharSequence getHeaderText() {
        return this.mMediaOutputController.getHeaderTitle();
    }

    /* access modifiers changed from: package-private */
    public CharSequence getHeaderSubtitle() {
        return this.mMediaOutputController.getHeaderSubTitle();
    }

    /* access modifiers changed from: package-private */
    public Drawable getAppSourceIcon() {
        return this.mMediaOutputController.getAppSourceIcon();
    }

    public void onStopButtonClick() {
        this.mMediaOutputController.stopBluetoothLeBroadcast();
        dismiss();
    }

    private String getBroadcastMetadataInfo(int i) {
        if (i != 0) {
            return i != 1 ? "" : this.mMediaOutputController.getBroadcastCode();
        }
        return this.mMediaOutputController.getBroadcastName();
    }

    private void initBtQrCodeUI() {
        inflateBroadcastInfoArea();
        this.mBroadcastQrCodeView = (ImageView) getDialogView().requireViewById(C1893R.C1897id.qrcode_view);
        ImageView imageView = (ImageView) getDialogView().requireViewById(C1893R.C1897id.broadcast_info);
        this.mBroadcastNotify = imageView;
        imageView.setOnClickListener(new MediaOutputBroadcastDialog$$ExternalSyntheticLambda0(this));
        this.mBroadcastName = (TextView) getDialogView().requireViewById(C1893R.C1897id.broadcast_name_summary);
        ImageView imageView2 = (ImageView) getDialogView().requireViewById(C1893R.C1897id.broadcast_name_edit);
        this.mBroadcastNameEdit = imageView2;
        imageView2.setOnClickListener(new MediaOutputBroadcastDialog$$ExternalSyntheticLambda1(this));
        TextView textView = (TextView) getDialogView().requireViewById(C1893R.C1897id.broadcast_code_summary);
        this.mBroadcastCode = textView;
        textView.setTransformationMethod(PasswordTransformationMethod.getInstance());
        ImageView imageView3 = (ImageView) getDialogView().requireViewById(C1893R.C1897id.broadcast_code_eye);
        this.mBroadcastCodeEye = imageView3;
        imageView3.setOnClickListener(new MediaOutputBroadcastDialog$$ExternalSyntheticLambda2(this));
        ImageView imageView4 = (ImageView) getDialogView().requireViewById(C1893R.C1897id.broadcast_code_edit);
        this.mBroadcastCodeEdit = imageView4;
        imageView4.setOnClickListener(new MediaOutputBroadcastDialog$$ExternalSyntheticLambda3(this));
        refreshUi();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$initBtQrCodeUI$0$com-android-systemui-media-dialog-MediaOutputBroadcastDialog */
    public /* synthetic */ void mo34367xc14e5c77(View view) {
        this.mMediaOutputController.launchLeBroadcastNotifyDialog((View) null, (BroadcastSender) null, MediaOutputController.BroadcastNotifyDialog.ACTION_BROADCAST_INFO_ICON, (DialogInterface.OnClickListener) null);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$initBtQrCodeUI$1$com-android-systemui-media-dialog-MediaOutputBroadcastDialog */
    public /* synthetic */ void mo34368x558ccc16(View view) {
        launchBroadcastUpdatedDialog(false, this.mBroadcastName.getText().toString());
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$initBtQrCodeUI$2$com-android-systemui-media-dialog-MediaOutputBroadcastDialog */
    public /* synthetic */ void mo34369xe9cb3bb5(View view) {
        updateBroadcastCodeVisibility();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$initBtQrCodeUI$3$com-android-systemui-media-dialog-MediaOutputBroadcastDialog */
    public /* synthetic */ void mo34370x7e09ab54(View view) {
        launchBroadcastUpdatedDialog(true, this.mBroadcastCode.getText().toString());
    }

    private void refreshUi() {
        setQrCodeView();
        this.mCurrentBroadcastName = getBroadcastMetadataInfo(0);
        this.mCurrentBroadcastCode = getBroadcastMetadataInfo(1);
        this.mBroadcastName.setText(this.mCurrentBroadcastName);
        this.mBroadcastCode.setText(this.mCurrentBroadcastCode);
    }

    private void inflateBroadcastInfoArea() {
        ViewStub viewStub = (ViewStub) getDialogView().requireViewById(C1893R.C1897id.broadcast_qrcode);
        this.mBroadcastInfoArea = viewStub;
        viewStub.inflate();
    }

    private void setQrCodeView() {
        String broadcastMetadata = getBroadcastMetadata();
        if (!broadcastMetadata.isEmpty()) {
            try {
                this.mBroadcastQrCodeView.setImageBitmap(QrCodeGenerator.encodeQrCode(broadcastMetadata, getContext().getResources().getDimensionPixelSize(C1893R.dimen.media_output_qrcode_size)));
            } catch (WriterException e) {
                Log.e(TAG, "Error generatirng QR code bitmap " + e);
            }
        }
    }

    private void updateBroadcastCodeVisibility() {
        TransformationMethod transformationMethod;
        TextView textView = this.mBroadcastCode;
        if (this.mIsPasswordHide.booleanValue()) {
            transformationMethod = HideReturnsTransformationMethod.getInstance();
        } else {
            transformationMethod = PasswordTransformationMethod.getInstance();
        }
        textView.setTransformationMethod(transformationMethod);
        this.mIsPasswordHide = Boolean.valueOf(!this.mIsPasswordHide.booleanValue());
    }

    private void launchBroadcastUpdatedDialog(boolean z, String str) {
        View inflate = LayoutInflater.from(this.mContext).inflate(C1893R.layout.media_output_broadcast_update_dialog, (ViewGroup) null);
        EditText editText = (EditText) inflate.requireViewById(C1893R.C1897id.broadcast_edit_text);
        editText.setText(str);
        this.mBroadcastErrorMessage = (TextView) inflate.requireViewById(C1893R.C1897id.broadcast_error_message);
        AlertDialog create = new AlertDialog.Builder(this.mContext).setTitle(z ? C1893R.string.media_output_broadcast_code : C1893R.string.media_output_broadcast_name).setView(inflate).setNegativeButton(17039360, (DialogInterface.OnClickListener) null).setPositiveButton(C1893R.string.media_output_broadcast_dialog_save, new MediaOutputBroadcastDialog$$ExternalSyntheticLambda4(this, z, editText)).create();
        this.mAlertDialog = create;
        create.getWindow().setType(Types.SQLXML);
        SystemUIDialog.setShowForAllUsers(this.mAlertDialog, true);
        SystemUIDialog.registerDismissListener(this.mAlertDialog);
        this.mAlertDialog.show();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$launchBroadcastUpdatedDialog$4$com-android-systemui-media-dialog-MediaOutputBroadcastDialog */
    public /* synthetic */ void mo34371xe92903c2(boolean z, EditText editText, DialogInterface dialogInterface, int i) {
        updateBroadcastInfo(z, editText.getText().toString());
    }

    private String getBroadcastMetadata() {
        return this.mMediaOutputController.getBroadcastMetadata();
    }

    private void updateBroadcastInfo(boolean z, String str) {
        Button button = this.mAlertDialog.getButton(-1);
        if (button != null) {
            button.setEnabled(false);
        }
        if (z) {
            this.mIsStopbyUpdateBroadcastCode = true;
            this.mMediaOutputController.setBroadcastCode(str);
            if (!this.mMediaOutputController.stopBluetoothLeBroadcast()) {
                handleLeBroadcastStopFailed();
                return;
            }
            return;
        }
        this.mMediaOutputController.setBroadcastName(str);
        if (!this.mMediaOutputController.updateBluetoothLeBroadcast()) {
            handleLeBroadcastUpdateFailed();
        }
    }

    public void handleLeBroadcastStarted() {
        this.mRetryCount = 0;
        AlertDialog alertDialog = this.mAlertDialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        refreshUi();
    }

    public void handleLeBroadcastStartFailed() {
        this.mMediaOutputController.setBroadcastCode(this.mCurrentBroadcastCode);
        this.mRetryCount++;
        handleUpdateFailedUi();
    }

    public void handleLeBroadcastMetadataChanged() {
        Log.d(TAG, "handleLeBroadcastMetadataChanged ");
        refreshUi();
    }

    public void handleLeBroadcastUpdated() {
        this.mRetryCount = 0;
        AlertDialog alertDialog = this.mAlertDialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        refreshUi();
    }

    public void handleLeBroadcastUpdateFailed() {
        this.mMediaOutputController.setBroadcastName(this.mCurrentBroadcastName);
        this.mRetryCount++;
        handleUpdateFailedUi();
    }

    public void handleLeBroadcastStopped() {
        if (this.mIsStopbyUpdateBroadcastCode) {
            this.mIsStopbyUpdateBroadcastCode = false;
            this.mRetryCount = 0;
            if (!this.mMediaOutputController.startBluetoothLeBroadcast()) {
                handleLeBroadcastStartFailed();
                return;
            }
            return;
        }
        dismiss();
    }

    public void handleLeBroadcastStopFailed() {
        this.mMediaOutputController.setBroadcastCode(this.mCurrentBroadcastCode);
        this.mRetryCount++;
        handleUpdateFailedUi();
    }

    public boolean isBroadcastSupported() {
        Log.d(TAG, "isBroadcastSupported: " + this.mMediaOutputController.isBroadcastSupported());
        return this.mMediaOutputController.isBroadcastSupported();
    }

    private void handleUpdateFailedUi() {
        Button button = this.mAlertDialog.getButton(-1);
        this.mBroadcastErrorMessage.setVisibility(0);
        if (this.mRetryCount < 3) {
            if (button != null) {
                button.setEnabled(true);
            }
            this.mBroadcastErrorMessage.setText(C1893R.string.media_output_broadcast_update_error);
            return;
        }
        this.mRetryCount = 0;
        this.mBroadcastErrorMessage.setText(C1893R.string.media_output_broadcast_last_update_error);
    }
}
