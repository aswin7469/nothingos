package com.android.settingslib.users;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.UserHandle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import com.android.internal.util.UserIcons;
import com.android.settingslib.C1757R;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.RestrictedLockUtilsInternal;
import com.android.settingslib.drawable.CircleFramedDrawable;
import java.p026io.File;
import java.util.function.BiConsumer;

public class EditUserInfoController {
    private static final String KEY_AWAITING_RESULT = "awaiting_result";
    private static final String KEY_SAVED_PHOTO = "pending_photo";
    private Dialog mEditUserInfoDialog;
    private EditUserPhotoController mEditUserPhotoController;
    private final String mFileAuthority;
    private Drawable mSavedDrawable;
    private Bitmap mSavedPhoto;
    private boolean mWaitingForActivityResult = false;

    public EditUserInfoController(String str) {
        this.mFileAuthority = str;
    }

    private void clear() {
        EditUserPhotoController editUserPhotoController = this.mEditUserPhotoController;
        if (editUserPhotoController != null) {
            editUserPhotoController.removeNewUserPhotoBitmapFile();
        }
        this.mEditUserInfoDialog = null;
        this.mSavedPhoto = null;
        this.mSavedDrawable = null;
    }

    public void onRestoreInstanceState(Bundle bundle) {
        String string = bundle.getString(KEY_SAVED_PHOTO);
        if (string != null) {
            this.mSavedPhoto = EditUserPhotoController.loadNewUserPhotoBitmap(new File(string));
        }
        this.mWaitingForActivityResult = bundle.getBoolean(KEY_AWAITING_RESULT, false);
    }

    public void onSaveInstanceState(Bundle bundle) {
        EditUserPhotoController editUserPhotoController;
        File saveNewUserPhotoBitmap;
        if (!(this.mEditUserInfoDialog == null || (editUserPhotoController = this.mEditUserPhotoController) == null || (saveNewUserPhotoBitmap = editUserPhotoController.saveNewUserPhotoBitmap()) == null)) {
            bundle.putString(KEY_SAVED_PHOTO, saveNewUserPhotoBitmap.getPath());
        }
        bundle.putBoolean(KEY_AWAITING_RESULT, this.mWaitingForActivityResult);
    }

    public void startingActivityForResult() {
        this.mWaitingForActivityResult = true;
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        this.mWaitingForActivityResult = false;
        EditUserPhotoController editUserPhotoController = this.mEditUserPhotoController;
        if (editUserPhotoController != null && this.mEditUserInfoDialog != null) {
            editUserPhotoController.onActivityResult(i, i2, intent);
        }
    }

    public Dialog createDialog(Activity activity, ActivityStarter activityStarter, Drawable drawable, String str, String str2, BiConsumer<String, Drawable> biConsumer, Runnable runnable) {
        Drawable drawable2;
        Activity activity2 = activity;
        View inflate = LayoutInflater.from(activity).inflate(C1757R.layout.edit_user_info_dialog_content, (ViewGroup) null);
        EditText editText = (EditText) inflate.findViewById(C1757R.C1760id.user_name);
        String str3 = str;
        editText.setText(str);
        ImageView imageView = (ImageView) inflate.findViewById(C1757R.C1760id.user_photo);
        if (drawable != null) {
            drawable2 = drawable;
        } else {
            drawable2 = UserIcons.getDefaultUserIcon(activity.getResources(), -10000, false);
        }
        imageView.setImageDrawable(getUserIcon(activity, drawable2));
        if (isChangePhotoRestrictedByBase(activity)) {
            inflate.findViewById(C1757R.C1760id.add_a_photo_icon).setVisibility(8);
        } else {
            RestrictedLockUtils.EnforcedAdmin changePhotoAdminRestriction = getChangePhotoAdminRestriction(activity);
            if (changePhotoAdminRestriction != null) {
                imageView.setOnClickListener(new EditUserInfoController$$ExternalSyntheticLambda3(activity, changePhotoAdminRestriction));
            } else {
                ActivityStarter activityStarter2 = activityStarter;
                this.mEditUserPhotoController = createEditUserPhotoController(activity, activityStarter, imageView);
            }
        }
        Dialog buildDialog = buildDialog(activity, inflate, editText, drawable, str, str2, biConsumer, runnable);
        this.mEditUserInfoDialog = buildDialog;
        buildDialog.getWindow().setSoftInputMode(4);
        return this.mEditUserInfoDialog;
    }

    private Drawable getUserIcon(Activity activity, Drawable drawable) {
        Bitmap bitmap = this.mSavedPhoto;
        if (bitmap == null) {
            return drawable;
        }
        CircleFramedDrawable instance = CircleFramedDrawable.getInstance(activity, bitmap);
        this.mSavedDrawable = instance;
        return instance;
    }

    private Dialog buildDialog(Activity activity, View view, EditText editText, Drawable drawable, String str, String str2, BiConsumer<String, Drawable> biConsumer, Runnable runnable) {
        return new AlertDialog.Builder(activity).setTitle(str2).setView(view).setCancelable(true).setPositiveButton(17039370, new EditUserInfoController$$ExternalSyntheticLambda0(this, drawable, editText, str, biConsumer)).setNegativeButton(17039360, new EditUserInfoController$$ExternalSyntheticLambda1(this, runnable)).setOnCancelListener(new EditUserInfoController$$ExternalSyntheticLambda2(this, runnable)).create();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$buildDialog$1$com-android-settingslib-users-EditUserInfoController */
    public /* synthetic */ void mo29225x83902b37(Drawable drawable, EditText editText, String str, BiConsumer biConsumer, DialogInterface dialogInterface, int i) {
        EditUserPhotoController editUserPhotoController = this.mEditUserPhotoController;
        Drawable newUserPhotoDrawable = editUserPhotoController != null ? editUserPhotoController.getNewUserPhotoDrawable() : null;
        if (newUserPhotoDrawable != null) {
            drawable = newUserPhotoDrawable;
        }
        String trim = editText.getText().toString().trim();
        if (!trim.isEmpty()) {
            str = trim;
        }
        clear();
        if (biConsumer != null) {
            biConsumer.accept(str, drawable);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$buildDialog$2$com-android-settingslib-users-EditUserInfoController */
    public /* synthetic */ void mo29226x9daba9d6(Runnable runnable, DialogInterface dialogInterface, int i) {
        clear();
        if (runnable != null) {
            runnable.run();
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$buildDialog$3$com-android-settingslib-users-EditUserInfoController */
    public /* synthetic */ void mo29227xb7c72875(Runnable runnable, DialogInterface dialogInterface) {
        clear();
        if (runnable != null) {
            runnable.run();
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isChangePhotoRestrictedByBase(Context context) {
        return RestrictedLockUtilsInternal.hasBaseUserRestriction(context, "no_set_user_icon", UserHandle.myUserId());
    }

    /* access modifiers changed from: package-private */
    public RestrictedLockUtils.EnforcedAdmin getChangePhotoAdminRestriction(Context context) {
        return RestrictedLockUtilsInternal.checkIfRestrictionEnforced(context, "no_set_user_icon", UserHandle.myUserId());
    }

    /* access modifiers changed from: package-private */
    public EditUserPhotoController createEditUserPhotoController(Activity activity, ActivityStarter activityStarter, ImageView imageView) {
        return new EditUserPhotoController(activity, activityStarter, imageView, this.mSavedPhoto, this.mSavedDrawable, this.mFileAuthority);
    }
}
