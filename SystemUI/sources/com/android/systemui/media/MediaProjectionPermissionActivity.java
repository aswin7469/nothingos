package com.android.systemui.media;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.media.projection.IMediaProjection;
import android.media.projection.IMediaProjectionManager;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.text.BidiFormatter;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import com.android.systemui.util.Utils;
/* loaded from: classes.dex */
public class MediaProjectionPermissionActivity extends Activity implements DialogInterface.OnClickListener, DialogInterface.OnCancelListener {
    private AlertDialog mDialog;
    private String mPackageName;
    private IMediaProjectionManager mService;
    private int mUid;

    @Override // android.app.Activity
    public void onCreate(Bundle bundle) {
        String string;
        String str;
        super.onCreate(bundle);
        this.mPackageName = getCallingPackage();
        this.mService = IMediaProjectionManager.Stub.asInterface(ServiceManager.getService("media_projection"));
        if (this.mPackageName == null) {
            finish();
            return;
        }
        PackageManager packageManager = getPackageManager();
        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(this.mPackageName, 0);
            int i = applicationInfo.uid;
            this.mUid = i;
            try {
                if (this.mService.hasProjectionPermission(i, this.mPackageName)) {
                    setResult(-1, getMediaProjectionIntent(this.mUid, this.mPackageName));
                    finish();
                    return;
                }
                TextPaint textPaint = new TextPaint();
                textPaint.setTextSize(42.0f);
                if (Utils.isHeadlessRemoteDisplayProvider(packageManager, this.mPackageName)) {
                    str = getString(R$string.media_projection_dialog_service_text);
                    string = getString(R$string.media_projection_dialog_service_title);
                } else {
                    String charSequence = applicationInfo.loadLabel(packageManager).toString();
                    int length = charSequence.length();
                    int i2 = 0;
                    while (i2 < length) {
                        int codePointAt = charSequence.codePointAt(i2);
                        int type = Character.getType(codePointAt);
                        if (type == 13 || type == 15 || type == 14) {
                            charSequence = charSequence.substring(0, i2) + "…";
                            break;
                        }
                        i2 += Character.charCount(codePointAt);
                    }
                    if (charSequence.isEmpty()) {
                        charSequence = this.mPackageName;
                    }
                    String unicodeWrap = BidiFormatter.getInstance().unicodeWrap(TextUtils.ellipsize(charSequence, textPaint, 500.0f, TextUtils.TruncateAt.END).toString());
                    String string2 = getString(R$string.media_projection_dialog_text, new Object[]{unicodeWrap});
                    SpannableString spannableString = new SpannableString(string2);
                    int indexOf = string2.indexOf(unicodeWrap);
                    if (indexOf >= 0) {
                        spannableString.setSpan(new StyleSpan(1), indexOf, unicodeWrap.length() + indexOf, 0);
                    }
                    string = getString(R$string.media_projection_dialog_title, new Object[]{unicodeWrap});
                    str = spannableString;
                }
                View inflate = View.inflate(this, R$layout.media_projection_dialog_title, null);
                ((TextView) inflate.findViewById(R$id.dialog_title)).setText(string);
                AlertDialog create = new AlertDialog.Builder(this).setCustomTitle(inflate).setMessage(str).setPositiveButton(R$string.media_projection_action_text, this).setNegativeButton(17039360, this).setOnCancelListener(this).create();
                this.mDialog = create;
                create.create();
                this.mDialog.getButton(-1).setFilterTouchesWhenObscured(true);
                Window window = this.mDialog.getWindow();
                window.setType(2003);
                window.addSystemFlags(524288);
                this.mDialog.show();
            } catch (RemoteException e) {
                Log.e("MediaProjectionPermissionActivity", "Error checking projection permissions", e);
                finish();
            }
        } catch (PackageManager.NameNotFoundException e2) {
            Log.e("MediaProjectionPermissionActivity", "unable to look up package name", e2);
            finish();
        }
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        AlertDialog alertDialog = this.mDialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:15:0x001f, code lost:
        if (r2 == null) goto L6;
     */
    /* JADX WARN: Code restructure failed: missing block: B:7:0x0037, code lost:
        return;
     */
    @Override // android.content.DialogInterface.OnClickListener
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onClick(DialogInterface dialogInterface, int i) {
        if (i == -1) {
            try {
                try {
                    setResult(-1, getMediaProjectionIntent(this.mUid, this.mPackageName));
                } catch (RemoteException e) {
                    Log.e("MediaProjectionPermissionActivity", "Error granting projection permission", e);
                    setResult(0);
                    AlertDialog alertDialog = this.mDialog;
                }
            } finally {
                AlertDialog alertDialog2 = this.mDialog;
                if (alertDialog2 != null) {
                    alertDialog2.dismiss();
                }
                finish();
            }
        }
    }

    private Intent getMediaProjectionIntent(int i, String str) throws RemoteException {
        IMediaProjection createProjection = this.mService.createProjection(i, str, 0, false);
        Intent intent = new Intent();
        intent.putExtra("android.media.projection.extra.EXTRA_MEDIA_PROJECTION", createProjection.asBinder());
        return intent;
    }

    @Override // android.content.DialogInterface.OnCancelListener
    public void onCancel(DialogInterface dialogInterface) {
        finish();
    }
}
