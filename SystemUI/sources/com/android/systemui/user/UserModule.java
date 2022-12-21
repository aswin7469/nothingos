package com.android.systemui.user;

import android.app.Activity;
import com.android.settingslib.users.EditUserInfoController;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;

@Module
public abstract class UserModule {
    private static final String FILE_PROVIDER_AUTHORITY = "com.android.systemui.fileprovider";

    @IntoMap
    @ClassKey(UserSwitcherActivity.class)
    @Binds
    public abstract Activity provideUserSwitcherActivity(UserSwitcherActivity userSwitcherActivity);

    @Provides
    public static EditUserInfoController provideEditUserInfoController() {
        return new EditUserInfoController("com.android.systemui.fileprovider");
    }
}
