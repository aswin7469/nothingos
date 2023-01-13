package com.android.systemui.user;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.UserInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.UserManager;
import com.android.internal.util.UserIcons;
import com.android.settingslib.users.UserCreatingDialog;
import com.android.settingslib.utils.ThreadUtils;
import java.util.function.Consumer;
import javax.inject.Inject;

public class UserCreator {
    private final Context mContext;
    private final UserManager mUserManager;

    @Inject
    public UserCreator(Context context, UserManager userManager) {
        this.mContext = context;
        this.mUserManager = userManager;
    }

    public void createUser(String str, Drawable drawable, Consumer<UserInfo> consumer, Runnable runnable) {
        UserCreatingDialog userCreatingDialog = new UserCreatingDialog(this.mContext);
        userCreatingDialog.show();
        ThreadUtils.postOnMainThread(new UserCreator$$ExternalSyntheticLambda0(this, str, userCreatingDialog, runnable, drawable, consumer));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$createUser$0$com-android-systemui-user-UserCreator  reason: not valid java name */
    public /* synthetic */ void m3298lambda$createUser$0$comandroidsystemuiuserUserCreator(String str, Dialog dialog, Runnable runnable, Drawable drawable, Consumer consumer) {
        UserInfo createUser = this.mUserManager.createUser(str, "android.os.usertype.full.SECONDARY", 0);
        if (createUser == null) {
            dialog.dismiss();
            runnable.run();
            return;
        }
        Resources resources = this.mContext.getResources();
        if (drawable == null) {
            drawable = UserIcons.getDefaultUserIcon(resources, createUser.id, false);
        }
        this.mUserManager.setUserIcon(createUser.id, UserIcons.convertToBitmapAtUserIconSize(resources, drawable));
        dialog.dismiss();
        consumer.accept(createUser);
    }
}
