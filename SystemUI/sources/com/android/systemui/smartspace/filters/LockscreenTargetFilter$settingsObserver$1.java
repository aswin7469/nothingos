package com.android.systemui.smartspace.filters;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000\u001d\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u001a\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007H\u0016¨\u0006\b"}, mo64987d2 = {"com/android/systemui/smartspace/filters/LockscreenTargetFilter$settingsObserver$1", "Landroid/database/ContentObserver;", "onChange", "", "selfChange", "", "uri", "Landroid/net/Uri;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: LockscreenTargetFilter.kt */
public final class LockscreenTargetFilter$settingsObserver$1 extends ContentObserver {
    final /* synthetic */ LockscreenTargetFilter this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    LockscreenTargetFilter$settingsObserver$1(LockscreenTargetFilter lockscreenTargetFilter, Handler handler) {
        super(handler);
        this.this$0 = lockscreenTargetFilter;
    }

    public void onChange(boolean z, Uri uri) {
        this.this$0.execution.assertIsMainThread();
        this.this$0.updateUserContentSettings();
    }
}
