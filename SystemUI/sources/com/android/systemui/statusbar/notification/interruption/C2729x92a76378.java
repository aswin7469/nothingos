package com.android.systemui.statusbar.notification.interruption;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u001d\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u001a\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007H\u0016¨\u0006\b"}, mo65043d2 = {"com/android/systemui/statusbar/notification/interruption/KeyguardNotificationVisibilityProviderImpl$start$settingsObserver$1", "Landroid/database/ContentObserver;", "onChange", "", "selfChange", "", "uri", "Landroid/net/Uri;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.systemui.statusbar.notification.interruption.KeyguardNotificationVisibilityProviderImpl$start$settingsObserver$1 */
/* compiled from: KeyguardNotificationVisibilityProvider.kt */
public final class C2729x92a76378 extends ContentObserver {
    final /* synthetic */ KeyguardNotificationVisibilityProviderImpl this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    C2729x92a76378(KeyguardNotificationVisibilityProviderImpl keyguardNotificationVisibilityProviderImpl, Handler handler) {
        super(handler);
        this.this$0 = keyguardNotificationVisibilityProviderImpl;
    }

    public void onChange(boolean z, Uri uri) {
        if (Intrinsics.areEqual((Object) uri, (Object) this.this$0.showSilentNotifsUri)) {
            this.this$0.readShowSilentNotificationSetting();
        }
        if (this.this$0.isLockedOrLocking()) {
            this.this$0.notifyStateChanged("Settings " + uri + " changed");
        }
    }
}
