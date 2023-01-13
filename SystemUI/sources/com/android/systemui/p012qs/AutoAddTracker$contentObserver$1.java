package com.android.systemui.p012qs;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import java.util.Collection;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000)\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u001e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J.\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u00072\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\nH\u0016¨\u0006\f"}, mo65043d2 = {"com/android/systemui/qs/AutoAddTracker$contentObserver$1", "Landroid/database/ContentObserver;", "onChange", "", "selfChange", "", "uris", "", "Landroid/net/Uri;", "flags", "", "_userId", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.systemui.qs.AutoAddTracker$contentObserver$1 */
/* compiled from: AutoAddTracker.kt */
public final class AutoAddTracker$contentObserver$1 extends ContentObserver {
    final /* synthetic */ AutoAddTracker this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    AutoAddTracker$contentObserver$1(AutoAddTracker autoAddTracker, Handler handler) {
        super(handler);
        this.this$0 = autoAddTracker;
    }

    public void onChange(boolean z, Collection<? extends Uri> collection, int i, int i2) {
        Intrinsics.checkNotNullParameter(collection, "uris");
        if (i2 == this.this$0.userId) {
            this.this$0.loadTiles();
        }
    }
}
