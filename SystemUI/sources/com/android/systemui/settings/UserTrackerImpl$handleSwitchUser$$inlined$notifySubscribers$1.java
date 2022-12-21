package com.android.systemui.settings;

import android.content.Context;
import com.android.systemui.settings.UserTracker;
import java.util.List;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002¨\u0006\u0003"}, mo64987d2 = {"<anonymous>", "", "run", "com/android/systemui/settings/UserTrackerImpl$notifySubscribers$1$1"}, mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: UserTrackerImpl.kt */
public final class UserTrackerImpl$handleSwitchUser$$inlined$notifySubscribers$1 implements Runnable {
    final /* synthetic */ Context $ctx$inlined;
    final /* synthetic */ DataItem $it;
    final /* synthetic */ int $newUser$inlined;
    final /* synthetic */ List $profiles$inlined;

    public UserTrackerImpl$handleSwitchUser$$inlined$notifySubscribers$1(DataItem dataItem, int i, Context context, List list) {
        this.$it = dataItem;
        this.$newUser$inlined = i;
        this.$ctx$inlined = context;
        this.$profiles$inlined = list;
    }

    public final void run() {
        UserTracker.Callback callback = this.$it.getCallback().get();
        if (callback != null) {
            callback.onUserChanged(this.$newUser$inlined, this.$ctx$inlined);
            callback.onProfilesChanged(this.$profiles$inlined);
        }
    }
}
