package com.android.systemui.user;

import com.android.settingslib.users.EditUserInfoController;
/* loaded from: classes2.dex */
public class UserModule {
    /* JADX INFO: Access modifiers changed from: package-private */
    public EditUserInfoController provideEditUserInfoController() {
        return new EditUserInfoController("com.android.systemui.fileprovider");
    }
}
