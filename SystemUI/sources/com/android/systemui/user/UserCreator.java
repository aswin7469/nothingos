package com.android.systemui.user;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.UserInfo;
import android.graphics.drawable.Drawable;
import android.os.UserManager;
import com.android.internal.util.UserIcons;
import com.android.settingslib.users.UserCreatingDialog;
import com.android.settingslib.utils.ThreadUtils;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public class UserCreator {
    private final Context mContext;
    private final UserManager mUserManager;

    public UserCreator(Context context, UserManager userManager) {
        this.mContext = context;
        this.mUserManager = userManager;
    }

    public void createUser(final String str, final Drawable drawable, final Consumer<UserInfo> consumer, final Runnable runnable) {
        final UserCreatingDialog userCreatingDialog = new UserCreatingDialog(this.mContext);
        userCreatingDialog.show();
        ThreadUtils.postOnMainThread(new Runnable() { // from class: com.android.systemui.user.UserCreator$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                UserCreator.this.lambda$createUser$0(str, userCreatingDialog, runnable, drawable, consumer);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createUser$0(String str, Dialog dialog, Runnable runnable, Drawable drawable, Consumer consumer) {
        UserInfo createUser = this.mUserManager.createUser(str, "android.os.usertype.full.SECONDARY", 0);
        if (createUser == null) {
            dialog.dismiss();
            runnable.run();
            return;
        }
        if (drawable == null) {
            drawable = UserIcons.getDefaultUserIcon(this.mContext.getResources(), createUser.id, false);
        }
        this.mUserManager.setUserIcon(createUser.id, UserIcons.convertToBitmap(drawable));
        dialog.dismiss();
        consumer.accept(createUser);
    }
}
