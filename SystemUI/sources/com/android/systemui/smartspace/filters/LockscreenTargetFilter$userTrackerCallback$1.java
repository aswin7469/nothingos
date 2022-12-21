package com.android.systemui.smartspace.filters;

import android.content.Context;
import com.android.systemui.settings.UserTracker;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\u001d\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0016¨\u0006\b"}, mo64987d2 = {"com/android/systemui/smartspace/filters/LockscreenTargetFilter$userTrackerCallback$1", "Lcom/android/systemui/settings/UserTracker$Callback;", "onUserChanged", "", "newUser", "", "userContext", "Landroid/content/Context;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: LockscreenTargetFilter.kt */
public final class LockscreenTargetFilter$userTrackerCallback$1 implements UserTracker.Callback {
    final /* synthetic */ LockscreenTargetFilter this$0;

    LockscreenTargetFilter$userTrackerCallback$1(LockscreenTargetFilter lockscreenTargetFilter) {
        this.this$0 = lockscreenTargetFilter;
    }

    public void onUserChanged(int i, Context context) {
        Intrinsics.checkNotNullParameter(context, "userContext");
        this.this$0.execution.assertIsMainThread();
        this.this$0.updateUserContentSettings();
    }
}
