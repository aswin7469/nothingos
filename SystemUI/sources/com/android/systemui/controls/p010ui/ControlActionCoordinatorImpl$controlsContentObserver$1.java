package com.android.systemui.controls.p010ui;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\u001d\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u001a\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007H\u0016¨\u0006\b"}, mo64987d2 = {"com/android/systemui/controls/ui/ControlActionCoordinatorImpl$controlsContentObserver$1", "Landroid/database/ContentObserver;", "onChange", "", "selfChange", "", "uri", "Landroid/net/Uri;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.systemui.controls.ui.ControlActionCoordinatorImpl$controlsContentObserver$1 */
/* compiled from: ControlActionCoordinatorImpl.kt */
public final class ControlActionCoordinatorImpl$controlsContentObserver$1 extends ContentObserver {
    final /* synthetic */ Uri $lockScreenShowControlsUri;
    final /* synthetic */ Uri $showControlsUri;
    final /* synthetic */ ControlActionCoordinatorImpl this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    ControlActionCoordinatorImpl$controlsContentObserver$1(Handler handler, Uri uri, ControlActionCoordinatorImpl controlActionCoordinatorImpl, Uri uri2) {
        super(handler);
        this.$lockScreenShowControlsUri = uri;
        this.this$0 = controlActionCoordinatorImpl;
        this.$showControlsUri = uri2;
    }

    public void onChange(boolean z, Uri uri) {
        super.onChange(z, uri);
        boolean z2 = true;
        if (Intrinsics.areEqual((Object) uri, (Object) this.$lockScreenShowControlsUri)) {
            ControlActionCoordinatorImpl controlActionCoordinatorImpl = this.this$0;
            if (controlActionCoordinatorImpl.secureSettings.getIntForUser("lockscreen_allow_trivial_controls", 0, -2) == 0) {
                z2 = false;
            }
            controlActionCoordinatorImpl.mAllowTrivialControls = z2;
        } else if (Intrinsics.areEqual((Object) uri, (Object) this.$showControlsUri)) {
            ControlActionCoordinatorImpl controlActionCoordinatorImpl2 = this.this$0;
            if (controlActionCoordinatorImpl2.secureSettings.getIntForUser("lockscreen_show_controls", 0, -2) == 0) {
                z2 = false;
            }
            controlActionCoordinatorImpl2.mShowDeviceControlsInLockscreen = z2;
        }
    }
}