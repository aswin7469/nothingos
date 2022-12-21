package com.android.systemui.statusbar.notification.people;

import android.content.pm.UserInfo;
import android.util.SparseArray;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000#\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u000e\u0010\u0004\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0005H\u0016J\u0010\u0010\u0007\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\tH\u0016¨\u0006\n"}, mo64987d2 = {"com/android/systemui/statusbar/notification/people/PeopleHubDataSourceImpl$registerListener$1", "Lcom/android/systemui/statusbar/NotificationLockscreenUserManager$UserChangedListener;", "onCurrentProfilesChanged", "", "currentProfiles", "Landroid/util/SparseArray;", "Landroid/content/pm/UserInfo;", "onUserChanged", "userId", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: PeopleHubNotificationListener.kt */
public final class PeopleHubDataSourceImpl$registerListener$1 implements NotificationLockscreenUserManager.UserChangedListener {
    final /* synthetic */ PeopleHubDataSourceImpl this$0;

    PeopleHubDataSourceImpl$registerListener$1(PeopleHubDataSourceImpl peopleHubDataSourceImpl) {
        this.this$0 = peopleHubDataSourceImpl;
    }

    public void onUserChanged(int i) {
        this.this$0.updateUi();
    }

    public void onCurrentProfilesChanged(SparseArray<UserInfo> sparseArray) {
        this.this$0.updateUi();
    }
}
