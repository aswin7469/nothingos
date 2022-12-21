package com.nothing.gamemode;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.statusbar.notification.NTLightweightHeadsupManager;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\u001d\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u001a\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007H\u0016¨\u0006\b"}, mo64987d2 = {"com/nothing/gamemode/NTGameModeHelper$settingsObserver$1", "Landroid/database/ContentObserver;", "onChange", "", "selfChange", "", "uri", "Landroid/net/Uri;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: NTGameModeHelper.kt */
public final class NTGameModeHelper$settingsObserver$1 extends ContentObserver {
    final /* synthetic */ NTGameModeHelper this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    NTGameModeHelper$settingsObserver$1(NTGameModeHelper nTGameModeHelper, Handler handler) {
        super(handler);
        this.this$0 = nTGameModeHelper;
    }

    public void onChange(boolean z, Uri uri) {
        if (uri != null) {
            if (Intrinsics.areEqual((Object) uri, (Object) Settings.Secure.getUriFor(NTGameModeHelper.GAME_MODE_ENABLED))) {
                this.this$0.loadGameModeEnabled();
            } else if (Intrinsics.areEqual((Object) uri, (Object) Settings.Secure.getUriFor(NTGameModeHelper.NOTIFICATION_DISPLAY_MODE))) {
                this.this$0.loadNotificationDisplayMode();
            } else if (Intrinsics.areEqual((Object) uri, (Object) Settings.Secure.getUriFor(NTGameModeHelper.MISTOUCH_PREVENTION))) {
                this.this$0.loadMistouchPreventEnabled();
            }
            if (this.this$0.isGameModeOn() != this.this$0.currentShowNotification) {
                NTGameModeHelper nTGameModeHelper = this.this$0;
                nTGameModeHelper.currentShowNotification = nTGameModeHelper.isGameModeOn();
                if (this.this$0.isGameModeOn()) {
                    this.this$0.sendGameModeOnNotification();
                    ((NTLightweightHeadsupManager) NTDependencyEx.get(NTLightweightHeadsupManager.class)).showGameModeToast();
                    return;
                }
                this.this$0.cancelGameModeOnNotification();
            }
        }
    }
}
