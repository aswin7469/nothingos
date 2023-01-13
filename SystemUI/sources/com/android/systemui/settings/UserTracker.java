package com.android.systemui.settings;

import android.content.Context;
import android.content.pm.UserInfo;
import android.os.UserHandle;
import java.util.List;
import java.util.concurrent.Executor;
import kotlin.Metadata;
import kotlin.jvm.JvmDefault;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\bf\u0018\u00002\u00020\u00012\u00020\u0002:\u0001\u001aJ\u0018\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0018H&J\u0010\u0010\u0019\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0016H&R\u0012\u0010\u0003\u001a\u00020\u0004X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006R\u0012\u0010\u0007\u001a\u00020\bX¦\u0004¢\u0006\u0006\u001a\u0004\b\t\u0010\nR\u0012\u0010\u000b\u001a\u00020\fX¦\u0004¢\u0006\u0006\u001a\u0004\b\r\u0010\u000eR\u0018\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\f0\u0010X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0011\u0010\u0012ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u001bÀ\u0006\u0001"}, mo65043d2 = {"Lcom/android/systemui/settings/UserTracker;", "Lcom/android/systemui/settings/UserContentResolverProvider;", "Lcom/android/systemui/settings/UserContextProvider;", "userHandle", "Landroid/os/UserHandle;", "getUserHandle", "()Landroid/os/UserHandle;", "userId", "", "getUserId", "()I", "userInfo", "Landroid/content/pm/UserInfo;", "getUserInfo", "()Landroid/content/pm/UserInfo;", "userProfiles", "", "getUserProfiles", "()Ljava/util/List;", "addCallback", "", "callback", "Lcom/android/systemui/settings/UserTracker$Callback;", "executor", "Ljava/util/concurrent/Executor;", "removeCallback", "Callback", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: UserTracker.kt */
public interface UserTracker extends UserContentResolverProvider, UserContextProvider {

    @Metadata(mo65042d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u0017J\u0018\u0010\u0007\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0017ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\fÀ\u0006\u0001"}, mo65043d2 = {"Lcom/android/systemui/settings/UserTracker$Callback;", "", "onProfilesChanged", "", "profiles", "", "Landroid/content/pm/UserInfo;", "onUserChanged", "newUser", "", "userContext", "Landroid/content/Context;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: UserTracker.kt */
    public interface Callback {
        @JvmDefault
        void onProfilesChanged(List<? extends UserInfo> list) {
            Intrinsics.checkNotNullParameter(list, "profiles");
        }

        @JvmDefault
        void onUserChanged(int i, Context context) {
            Intrinsics.checkNotNullParameter(context, "userContext");
        }
    }

    void addCallback(Callback callback, Executor executor);

    UserHandle getUserHandle();

    int getUserId();

    UserInfo getUserInfo();

    List<UserInfo> getUserProfiles();

    void removeCallback(Callback callback);
}
