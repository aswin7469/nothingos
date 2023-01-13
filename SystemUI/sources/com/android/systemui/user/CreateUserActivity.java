package com.android.systemui.user;

import android.app.Activity;
import android.app.Dialog;
import android.app.IActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.UserInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import com.android.settingslib.users.EditUserInfoController;
import com.android.systemui.C1894R;
import javax.inject.Inject;

public class CreateUserActivity extends Activity {
    private static final String DIALOG_STATE_KEY = "create_user_dialog_state";
    private static final String TAG = "CreateUserActivity";
    private final IActivityManager mActivityManager;
    private final EditUserInfoController mEditUserInfoController;
    private Dialog mSetupUserDialog;
    private final UserCreator mUserCreator;

    public static Intent createIntentForStart(Context context) {
        Intent intent = new Intent(context, CreateUserActivity.class);
        intent.addFlags(335544320);
        return intent;
    }

    @Inject
    public CreateUserActivity(UserCreator userCreator, EditUserInfoController editUserInfoController, IActivityManager iActivityManager) {
        this.mUserCreator = userCreator;
        this.mEditUserInfoController = editUserInfoController;
        this.mActivityManager = iActivityManager;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setShowWhenLocked(true);
        setContentView(C1894R.layout.activity_create_new_user);
        if (bundle != null) {
            this.mEditUserInfoController.onRestoreInstanceState(bundle);
        }
        Dialog createDialog = createDialog();
        this.mSetupUserDialog = createDialog;
        createDialog.show();
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(Bundle bundle) {
        Dialog dialog = this.mSetupUserDialog;
        if (dialog != null && dialog.isShowing()) {
            bundle.putBundle(DIALOG_STATE_KEY, this.mSetupUserDialog.onSaveInstanceState());
        }
        this.mEditUserInfoController.onSaveInstanceState(bundle);
        super.onSaveInstanceState(bundle);
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Bundle bundle) {
        Dialog dialog;
        super.onRestoreInstanceState(bundle);
        Bundle bundle2 = bundle.getBundle(DIALOG_STATE_KEY);
        if (bundle2 != null && (dialog = this.mSetupUserDialog) != null) {
            dialog.onRestoreInstanceState(bundle2);
        }
    }

    private Dialog createDialog() {
        return this.mEditUserInfoController.createDialog(this, new CreateUserActivity$$ExternalSyntheticLambda0(this), (Drawable) null, getString(C1894R.string.user_new_user_name), getString(C1894R.string.user_add_user), new CreateUserActivity$$ExternalSyntheticLambda1(this), new CreateUserActivity$$ExternalSyntheticLambda2(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$createDialog$0$com-android-systemui-user-CreateUserActivity */
    public /* synthetic */ void mo46681x5fd23fef(Intent intent, int i) {
        this.mEditUserInfoController.startingActivityForResult();
        startActivityForResult(intent, i);
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        this.mEditUserInfoController.onActivityResult(i, i2, intent);
    }

    public void onBackPressed() {
        super.onBackPressed();
        Dialog dialog = this.mSetupUserDialog;
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    /* access modifiers changed from: private */
    public void addUserNow(String str, Drawable drawable) {
        this.mSetupUserDialog.dismiss();
        if (str == null || str.trim().isEmpty()) {
            str = getString(C1894R.string.user_new_user_name);
        }
        this.mUserCreator.createUser(str, drawable, new CreateUserActivity$$ExternalSyntheticLambda3(this), new CreateUserActivity$$ExternalSyntheticLambda4(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$addUserNow$1$com-android-systemui-user-CreateUserActivity  reason: not valid java name */
    public /* synthetic */ void m3296lambda$addUserNow$1$comandroidsystemuiuserCreateUserActivity(UserInfo userInfo) {
        switchToUser(userInfo.id);
        finishIfNeeded();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$addUserNow$2$com-android-systemui-user-CreateUserActivity  reason: not valid java name */
    public /* synthetic */ void m3297lambda$addUserNow$2$comandroidsystemuiuserCreateUserActivity() {
        Log.e(TAG, "Unable to create user");
        finishIfNeeded();
    }

    private void finishIfNeeded() {
        if (!isFinishing() && !isDestroyed()) {
            finish();
        }
    }

    private void switchToUser(int i) {
        try {
            this.mActivityManager.switchUser(i);
        } catch (RemoteException e) {
            Log.e(TAG, "Couldn't switch user.", e);
        }
    }
}
