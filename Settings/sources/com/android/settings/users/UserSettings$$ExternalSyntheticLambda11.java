package com.android.settings.users;

import android.content.pm.UserInfo;
import com.android.settingslib.users.UserCreatingDialog;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class UserSettings$$ExternalSyntheticLambda11 implements Runnable {
    public final /* synthetic */ UserSettings f$0;
    public final /* synthetic */ UserCreatingDialog f$1;
    public final /* synthetic */ UserInfo f$2;

    public /* synthetic */ UserSettings$$ExternalSyntheticLambda11(UserSettings userSettings, UserCreatingDialog userCreatingDialog, UserInfo userInfo) {
        this.f$0 = userSettings;
        this.f$1 = userCreatingDialog;
        this.f$2 = userInfo;
    }

    public final void run() {
        this.f$0.lambda$onAddGuestClicked$0(this.f$1, this.f$2);
    }
}
