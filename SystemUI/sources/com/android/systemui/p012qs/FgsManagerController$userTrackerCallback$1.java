package com.android.systemui.p012qs;

import android.content.Context;
import android.content.pm.UserInfo;
import com.android.systemui.settings.UserTracker;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000)\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u0016J\u0018\u0010\u0007\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0016¨\u0006\f"}, mo65043d2 = {"com/android/systemui/qs/FgsManagerController$userTrackerCallback$1", "Lcom/android/systemui/settings/UserTracker$Callback;", "onProfilesChanged", "", "profiles", "", "Landroid/content/pm/UserInfo;", "onUserChanged", "newUser", "", "userContext", "Landroid/content/Context;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.systemui.qs.FgsManagerController$userTrackerCallback$1 */
/* compiled from: FgsManagerController.kt */
public final class FgsManagerController$userTrackerCallback$1 implements UserTracker.Callback {
    final /* synthetic */ FgsManagerController this$0;

    public void onUserChanged(int i, Context context) {
        Intrinsics.checkNotNullParameter(context, "userContext");
    }

    FgsManagerController$userTrackerCallback$1(FgsManagerController fgsManagerController) {
        this.this$0 = fgsManagerController;
    }

    public void onProfilesChanged(List<? extends UserInfo> list) {
        Intrinsics.checkNotNullParameter(list, "profiles");
        Object access$getLock$p = this.this$0.lock;
        FgsManagerController fgsManagerController = this.this$0;
        synchronized (access$getLock$p) {
            fgsManagerController.currentProfileIds.clear();
            Set access$getCurrentProfileIds$p = fgsManagerController.currentProfileIds;
            Iterable<UserInfo> iterable = list;
            Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
            for (UserInfo userInfo : iterable) {
                arrayList.add(Integer.valueOf(userInfo.id));
            }
            access$getCurrentProfileIds$p.addAll((List) arrayList);
            fgsManagerController.lastNumberOfVisiblePackages = 0;
            fgsManagerController.updateNumberOfVisibleRunningPackagesLocked();
            Unit unit = Unit.INSTANCE;
        }
    }
}
