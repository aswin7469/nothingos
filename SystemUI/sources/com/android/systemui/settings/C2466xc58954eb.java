package com.android.systemui.settings;

import com.android.systemui.settings.UserTracker;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002¨\u0006\u0003"}, mo64987d2 = {"<anonymous>", "", "run", "com/android/systemui/settings/UserTrackerImpl$notifySubscribers$1$1"}, mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.systemui.settings.UserTrackerImpl$handleProfilesChanged$$inlined$notifySubscribers$1 */
/* compiled from: UserTrackerImpl.kt */
public final class C2466xc58954eb implements Runnable {
    final /* synthetic */ DataItem $it;
    final /* synthetic */ List $profiles$inlined;

    public C2466xc58954eb(DataItem dataItem, List list) {
        this.$it = dataItem;
        this.$profiles$inlined = list;
    }

    public final void run() {
        UserTracker.Callback callback = this.$it.getCallback().get();
        if (callback != null) {
            Intrinsics.checkNotNullExpressionValue(this.$profiles$inlined, "profiles");
            callback.onProfilesChanged(this.$profiles$inlined);
        }
    }
}
